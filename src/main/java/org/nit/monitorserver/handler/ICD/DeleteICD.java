package org.nit.monitorserver.handler.ICD;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.RoutingContext;
import org.apache.log4j.Logger;
import org.nit.monitorserver.constant.HttpHeaderContentType;
import org.nit.monitorserver.database.MongoConnection;
import org.nit.monitorserver.handler.log.CreateLog;
import org.nit.monitorserver.message.AbstractRequestHandler;
import org.nit.monitorserver.message.Request;
import org.nit.monitorserver.message.ResponseFactory;
import org.nit.monitorserver.util.FormValidator;

import java.io.IOException;
import java.util.concurrent.ForkJoinPool;

import static org.nit.monitorserver.constant.ResponseError.*;

/**
 * @ClassName DeleteProject
 * @Description TODO删除ICD
 * @Author 20643
 * @Date 2020-9-1 14:49
 * @Version 1.0
 **/
public class DeleteICD extends AbstractRequestHandler {
    protected static final Logger logger = Logger.getLogger(DeleteICD.class);
    private final MongoClient mongoClient = new MongoConnection().getMongoClient();
    CreateLog createLog = new CreateLog();

    @Override
    public void handle(final RoutingContext routingContext,final Request request) throws IOException {
        routingContext.response().putHeader(HttpHeaderContentType.CONTENT_TYPE_STR, HttpHeaderContentType.JSON);
        ResponseFactory response = new ResponseFactory(routingContext, request);

        //id
        Object idObject = request.getParams().getValue("id");
        if(idObject == null || idObject.toString().equals("")){
            logger.error(String.format("delete ICD exception: %s", "ICD的id为必填参数"));
            response.error(ICDID_IS_REQUIRED.getCode(), ICDID_IS_REQUIRED.getMsg());
            createLog.createLogRecord("ICD管理","error","删除ICD","ICD的id为必填参数");
            return;
        }
        if(!FormValidator.isString(idObject)){
            logger.error(String.format("delete ICD exception: %s", "ICD的id格式错误"));
            response.error(ICDID_FORMAT_ERROR.getCode(), ICDID_FORMAT_ERROR.getMsg());
            createLog.createLogRecord("ICD管理","error","删除ICD","ICD的id格式错误");
            return;
        }
        String id = idObject.toString();
        JsonObject removeObject = new JsonObject().put("id",id);

        mongoClient.removeDocuments("ICD",removeObject,r->{
            if(r.failed()){
                logger.error(String.format("delete ICD: %s 删除失败",id));
                response.error(DELETE_FAILURE.getCode(), DELETE_FAILURE.getMsg());
                createLog.createLogRecord("ICD管理","error","删除ICD",String.format("删除ICD:%s 失败",id));
                return;
            }
            JsonObject result = new JsonObject();
            response.success(result);
            createLog.createLogRecord("ICD管理","info","删除ICD",String.format("删除ICD:%s 成功",id));
            return;
        });


    }
}
