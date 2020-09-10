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

import static org.nit.monitorserver.constant.ResponseError.QUERY_FAILURE;

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


    @Override
    public void handle(final RoutingContext routingContext,final Request request) throws IOException {
        routingContext.response().putHeader(HttpHeaderContentType.CONTENT_TYPE_STR, HttpHeaderContentType.JSON);
        ResponseFactory response = new ResponseFactory(routingContext, request);

        JsonObject searchObject = new JsonObject();
        String taskName = request.getParams().getString("taskName");
        if(!taskName.equals("") && taskName != null){
            searchObject.put("taskName",taskName);
        }
        String targetIP = request.getParams().getString("targetIP");
        if(!targetIP.equals("") && targetIP != null){
            searchObject.put("targetIP",targetIP);
        }

        mongoClient.find("task",searchObject,r->{
            if(r.failed()){
                logger.error(String.format("search 采集任务: %s 查找失败", Tools.getTrace(r.cause())));
                response.error(QUERY_FAILURE.getCode(), QUERY_FAILURE.getMsg());
                return;
            }
            JsonObject taskList = new JsonObject();
            taskList.put("taskList",r.result());
            response.success(taskList);


        });

    }
}
