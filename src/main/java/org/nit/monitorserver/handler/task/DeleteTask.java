package org.nit.monitorserver.handler.task;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.RoutingContext;
import org.apache.log4j.Logger;
import org.nit.monitorserver.constant.HttpHeaderContentType;
import org.nit.monitorserver.database.MongoConnection;
import org.nit.monitorserver.message.AbstractRequestHandler;
import org.nit.monitorserver.message.Request;
import org.nit.monitorserver.message.ResponseFactory;
import org.nit.monitorserver.util.Tools;

import java.io.IOException;

import static org.nit.monitorserver.constant.ResponseError.DELETE_TARGET_ERROR;
import static org.nit.monitorserver.constant.ResponseError.TASKID;

/**
 * @ClassName DeleteProject
 * @Description TODO删除任务
 * @Author 20643
 * @Date 2020-9-1 14:36
 * @Version 1.0
 **/
public class DeleteTask extends AbstractRequestHandler {
    protected static final Logger logger = Logger.getLogger(DeleteTask.class);
    //    private final SQLClient mySQLClient = new MysqlConnection().getMySQLClient();
    private final MongoClient mongoClient = new MongoConnection().getMongoClient();


    @Override
    public void handle(final RoutingContext routingContext,final Request request) throws IOException {
        routingContext.response().putHeader(HttpHeaderContentType.CONTENT_TYPE_STR, HttpHeaderContentType.JSON);
        ResponseFactory response = new ResponseFactory(routingContext, request);
        String id = request.getParams().getString("id");
        if(id == null || id.equals("")){
            logger.error(String.format("delete exception: %s", "采集任务id为必填参数"));
            response.error(TASKID.getCode(), TASKID.getMsg());
            return;
        }
        JsonObject deleteObject = new JsonObject().put("id",id);
        mongoClient.removeDocuments("task",deleteObject,r->{
            if(r.failed()){
                logger.error(String.format("task delete exception: %s", Tools.getTrace(r.cause())));
                response.error(DELETE_TARGET_ERROR.getCode(), DELETE_TARGET_ERROR.getMsg());
                return;
            }
            JsonObject result = new JsonObject();
            response.success(result);
        });


    }
}
