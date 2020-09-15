package org.nit.monitorserver.handler.log;

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

import java.io.IOException;
import java.text.Normalizer;

import static org.nit.monitorserver.constant.ResponseError.DELETE_FAILURE;
import static org.nit.monitorserver.constant.ResponseError.LEVEL;
import static org.nit.monitorserver.constant.ResponseError.*;

/**
 * @ClassName DeleteLog
 * @Description TODO删除日志
 * @Author 20643
 * @Date 2020-9-1 14:59
 * @Version 1.0
 **/
public class DeleteLog extends AbstractRequestHandler {
    protected static final Logger logger = Logger.getLogger(DeleteLog.class);
    private final MongoClient mongoClient = new MongoConnection().getMongoClient();

    @Override
    public void handle(final RoutingContext routingContext,final Request request) throws IOException {
        routingContext.response().putHeader(HttpHeaderContentType.CONTENT_TYPE_STR, HttpHeaderContentType.JSON);
        ResponseFactory response = new ResponseFactory(routingContext, request);

        Object idObject = request.getParams().getValue("id");
        if(idObject == null || idObject.toString().equals("")){
            logger.error(String.format("delete log exception: %s", "日志id为必填参数"));
            response.error(LOGID.getCode(), LOGID.getMsg());
            return;
        }
        if(!FormValidator.isString(idObject)){
            logger.error(String.format("delete log exception: %s", "日志id格式错误"));
            response.error(LOGID_FORMAT_ERROR.getCode(), LOGID_FORMAT_ERROR.getMsg());
            return;
        }
        String id = idObject.toString();

        JsonObject deleteObject = new JsonObject().put("id",id);
        mongoClient.removeDocuments("log",deleteObject,r->{
            if(r.failed()){
                logger.error(String.format("delete log: %s 删除失败",id));
                response.error(DELETE_FAILURE.getCode(), DELETE_FAILURE.getMsg());
                return;
            }
            JsonObject result = new JsonObject();
            response.success(result);
        });


    }
}
