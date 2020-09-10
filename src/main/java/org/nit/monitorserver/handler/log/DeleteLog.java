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

import java.io.IOException;

import static org.nit.monitorserver.constant.ResponseError.DELETE_FAILURE;
import static org.nit.monitorserver.constant.ResponseError.LEVEL;
import static org.nit.monitorserver.constant.ResponseError.LOGNAME;

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
        String logName = request.getParams().getString("logName");
        if(logName == null || logName.equals("")){
            logger.error(String.format("delete exception: %s", "日志名称为必填参数"));
            response.error(LOGNAME.getCode(), LOGNAME.getMsg());
            return;
        }
        JsonObject deleteObject = new JsonObject().put("logName",logName);
        mongoClient.removeDocuments("log",deleteObject,r->{
            if(r.failed()){
                logger.error(String.format("delete log: %s 删除失败",logName));
                response.error(DELETE_FAILURE.getCode(), DELETE_FAILURE.getMsg());
                return;
            }
            JsonObject result = new JsonObject();
            response.success(result);
        });


    }
}
