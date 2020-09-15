package org.nit.monitorserver.handler.data;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.RoutingContext;
import org.apache.log4j.Logger;
import org.nit.monitorserver.constant.HttpHeaderContentType;
import org.nit.monitorserver.database.MongoConnection;
import org.nit.monitorserver.message.AbstractRequestHandler;
import org.nit.monitorserver.message.Request;
import org.nit.monitorserver.message.ResponseFactory;
import org.nit.monitorserver.util.FormValidator;
import org.nit.monitorserver.util.Tools;

import java.io.IOException;

import static org.nit.monitorserver.constant.ResponseError.*;

/**
 * @author 20817
 * @version 1.0
 * @className DeleteDataIncentive
 * @description
 * @date 2020/9/4 22:32
 */
public class DeleteData extends AbstractRequestHandler {
    protected static final Logger logger = Logger.getLogger(DeleteData.class);
    private final MongoClient mongoClient = new MongoConnection().getMongoClient();

    @Override
    public void handle(RoutingContext routingContext, Request request) throws IOException, Exception {
        routingContext.response().putHeader(HttpHeaderContentType.CONTENT_TYPE_STR, HttpHeaderContentType.JSON);
        ResponseFactory response = new ResponseFactory(routingContext, request);

        Object idObject = request.getParams().getValue("id");
        if(idObject == null || idObject.toString().equals("")){
            logger.error(String.format("delete data exception: %s", "数据管理的id为必填参数"));
            response.error(DATAID.getCode(), DATAID.getMsg());
            return;
        }
        if(!FormValidator.isString(idObject)){
            logger.error(String.format("delete ICD exception: %s", "ICD的id格式错误"));
            response.error(DATAID_FORMAT_ERROR.getCode(), DATAID_FORMAT_ERROR.getMsg());
            return;
        }
        String id = idObject.toString();
        JsonObject deleteObject = new JsonObject().put("id",id);

        Object dataTypeObject = request.getParams().getValue("dataType");
        if(dataTypeObject == null){
            logger.error(String.format("delete data exception: %s", "数据管理的id为必填参数"));
            response.error(DATAID.getCode(), DATAID.getMsg());
            return;
        }
        if(!FormValidator.isInteger(dataTypeObject)){
            response.error(DATATYPE_FORMAT_ERROR.getCode(),DATATYPE_FORMAT_ERROR.getMsg());
            logger.error(String.format("search historyTdr exception: %s", "事件类型格式错误"));
            return;
        }
        int dataType = (int) dataTypeObject;
        if(dataType != 0 && dataType != 1){
            response.error(DATATYPE_FORMAT_ERROR.getCode(),DATATYPE_FORMAT_ERROR.getMsg());
            logger.error(String.format("search historyTdr exception: %s", "事件类型格式错误"));
            return;
        }
        if(dataType == 0){
            mongoClient.findOneAndDelete("comdrt",deleteObject,r->{
                if(r.failed()){
                    logger.error(String.format("delete data: %s 删除失败",id));
                    response.error(DELETE_FAILURE.getCode(), DELETE_FAILURE.getMsg());
                    return;
                }
                JsonObject result = new JsonObject();
                response.success(result);
            });

        }else {
            mongoClient.findOneAndDelete("targetIPEvtId",deleteObject,r->{
                if(r.failed()){
                    logger.error(String.format("delete data: %s 删除失败",id));
                    response.error(DELETE_FAILURE.getCode(), DELETE_FAILURE.getMsg());
                    return;
                }
                JsonObject result = new JsonObject();
                response.success(result);
            });
        }
    }
}
