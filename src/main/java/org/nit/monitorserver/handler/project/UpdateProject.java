package org.nit.monitorserver.handler.project;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.RoutingContext;
import org.apache.log4j.Logger;
import org.nit.monitorserver.constant.HttpHeaderContentType;
import org.nit.monitorserver.database.MongoConnection;
import org.nit.monitorserver.handler.ICD.UpdateICD;
import org.nit.monitorserver.message.AbstractRequestHandler;
import org.nit.monitorserver.message.Request;
import org.nit.monitorserver.message.ResponseFactory;
import org.nit.monitorserver.util.Tools;

import java.io.IOException;

import static org.nit.monitorserver.constant.ResponseError.PROJECTID;
import static org.nit.monitorserver.constant.ResponseError.UPDATE_FAILURE;

/**
 * @ClassName UpdateProject
 * @Description TODO修改数据分析工程
 * @Author 20643
 * @Date 2020-9-1 14:50
 * @Version 1.0
 **/
public class UpdateProject extends AbstractRequestHandler {
    protected static final Logger logger = Logger.getLogger(UpdateICD.class);
    private final MongoClient mongoClient = new MongoConnection().getMongoClient();

    @Override
    public void handle(final RoutingContext routingContext,final Request request) throws IOException {
        routingContext.response().putHeader(HttpHeaderContentType.CONTENT_TYPE_STR, HttpHeaderContentType.JSON);
        ResponseFactory response = new ResponseFactory(routingContext, request);

        //id
        String id = request.getParams().getString("id");
        if(id.equals("") || id == null){
            logger.error(String.format("delete exception: %s", "project的id为必填参数"));
            response.error(PROJECTID.getCode(), PROJECTID.getMsg());
            return;
        }
        JsonObject update = new JsonObject().put("id",id);

        JsonObject updateObjectElement = new JsonObject();
        //名称
        String name = request.getParams().getString("name");
        if(!name.equals("") && name != null){
            updateObjectElement.put("name",name);
        }
        JsonArray content = request.getParams().getJsonArray("content");
        if(content != null){
            updateObjectElement.put("content",content);
        }
        JsonObject updateObject = new JsonObject().put("$set",updateObjectElement);
        mongoClient.findOneAndUpdate("project",update,updateObject,r->{
            if(r.failed()){
                logger.error(String.format("UpdateProject project exception: %s", Tools.getTrace(r.cause())));
                response.error(UPDATE_FAILURE.getCode(), UPDATE_FAILURE.getMsg());
                return;
            }
            JsonObject result = new JsonObject();
            response.success(result);



        });



    }
}
