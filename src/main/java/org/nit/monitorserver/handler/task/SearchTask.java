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

import javax.swing.*;
import java.io.IOException;

import static org.nit.monitorserver.constant.ResponseError.QUERY_FAILURE;
import static org.nit.monitorserver.constant.ResponseError.TARGETIP_FORMAT_ERROR;
import static org.nit.monitorserver.constant.ResponseError.TASKNAME_FORMAT_ERROR;

/**
 * @ClassName SearchProject
 * @Description TODO查找任务
 * @Author 20643
 * @Date 2020-9-1 14:37
 * @Version 1.0
 **/
public class SearchTask extends AbstractRequestHandler {
    protected static final Logger logger = Logger.getLogger(SearchTask.class);
    //    private final SQLClient mySQLClient = new MysqlConnection().getMySQLClient();
    private final MongoClient mongoClient = new MongoConnection().getMongoClient();
    CreateLog createLog = new CreateLog();



    @Override
    public void handle(final RoutingContext routingContext,final Request request) throws IOException {
        routingContext.response().putHeader(HttpHeaderContentType.CONTENT_TYPE_STR, HttpHeaderContentType.JSON);
        ResponseFactory response = new ResponseFactory(routingContext, request);

        JsonObject searchObject = new JsonObject();
        //taskName
        Object taskNameObject = request.getParams().getValue("taskName");
        if(taskNameObject != null && !taskNameObject.toString().equals("")){
            if(!FormValidator.isString(taskNameObject)){
                logger.error(String.format("search task exception: %s", "采集任务名称格式错误"));
                response.error(TASKNAME_FORMAT_ERROR.getCode(), TASKNAME_FORMAT_ERROR.getMsg());
                createLog.createLogRecord("任务管理","error","查找任务","采集任务名称格式错误");
                return;
            }
            String taskName =  taskNameObject.toString();
            searchObject.put("taskName",taskName);
        }


        //targetIP
        Object targetIPObject = request.getParams().getValue("targetIP");
        if(targetIPObject != null && !targetIPObject.toString().equals("")){
            if(!FormValidator.isString(targetIPObject)){
                logger.error(String.format("search task exception: %s", "目标机ip格式错误"));
                response.error(TARGETIP_FORMAT_ERROR.getCode(), TARGETIP_FORMAT_ERROR.getMsg());
                createLog.createLogRecord("任务管理","error","查找任务","目标机ip格式错误");
                return;
            }
            String targetIP =  targetIPObject.toString();
            searchObject.put("targetIP",targetIP);
        }


        mongoClient.find("task",searchObject,r->{
            if(r.failed()){
                logger.error(String.format("search 采集任务: %s 查找失败", Tools.getTrace(r.cause())));
                response.error(QUERY_FAILURE.getCode(), QUERY_FAILURE.getMsg());
                createLog.createLogRecord("任务管理","error","查找任务","采集任务查找失败");
                return;

            }
            JsonObject taskList = new JsonObject();
            taskList.put("taskList",r.result());
            response.success(taskList);
            createLog.createLogRecord("任务管理","info","查找任务","采集任务查找成功");



        });

    }
}
