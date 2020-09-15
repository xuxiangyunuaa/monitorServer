package org.nit.monitorserver.handler.task;

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
import org.nit.monitorserver.util.Tools;

import java.io.IOException;

import static org.nit.monitorserver.constant.ResponseError.DELETE_TARGET_ERROR;
import static org.nit.monitorserver.constant.ResponseError.*;

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
    CreateLog createLog = new CreateLog();


    @Override
    public void handle(final RoutingContext routingContext,final Request request) throws IOException {
        routingContext.response().putHeader(HttpHeaderContentType.CONTENT_TYPE_STR, HttpHeaderContentType.JSON);
        ResponseFactory response = new ResponseFactory(routingContext, request);

        //id
        Object idObject = request.getParams().getValue("id");
        if(idObject == null || idObject.toString().equals("")){
            logger.error(String.format("delete task exception: %s", "采集任务id为必填参数"));
            response.error(TASKID.getCode(), TASKID.getMsg());
            createLog.createLogRecord("任务管理","error","删除采集任务","采集任务id为必填参数");
            return;
        }
        if(!FormValidator.isString(idObject)){
            logger.error(String.format("delete task exception: %s", "采集任务id格式错误"));
            response.error(TASKID_FORMAT_ERROR.getCode(), TASKID_FORMAT_ERROR.getMsg());
            createLog.createLogRecord("任务管理","error","删除采集任务","采集任务id格式错误");
            return;
        }
        String id = (String) idObject;
        JsonObject deleteObject = new JsonObject().put("id",id);

        mongoClient.removeDocuments("task",deleteObject,r->{
            if(r.failed()){
                logger.error(String.format("delete task exception: %s", Tools.getTrace(r.cause())));
                response.error(DELETE_TARGET_ERROR.getCode(), DELETE_TARGET_ERROR.getMsg());
                createLog.createLogRecord("任务管理","error","删除采集任务",String.format("采集任务：%s 删除失败",id));
                return;
            }
            JsonObject result = new JsonObject();
            response.success(result);
            createLog.createLogRecord("任务管理","info","删除采集任务",String.format("采集任务：%s 删除成功",id));
            return;
        });


    }
}
