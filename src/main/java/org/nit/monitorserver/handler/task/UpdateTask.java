package org.nit.monitorserver.handler.task;

import io.vertx.core.json.JsonArray;
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
 * @ClassName UpdateProject
 * @Description TODO修改任务
 * @Author 20643
 * @Date 2020-9-1 14:38
 * @Version 1.0
 **/
public class UpdateTask extends AbstractRequestHandler {
    protected static final Logger logger = Logger.getLogger(UpdateTask.class);
    //    private final SQLClient mySQLClient = new MysqlConnection().getMySQLClient();
    private final MongoClient mongoClient = new MongoConnection().getMongoClient();


    @Override
    public void handle(final RoutingContext routingContext,final Request request) throws IOException {
        routingContext.response().putHeader(HttpHeaderContentType.CONTENT_TYPE_STR, HttpHeaderContentType.JSON);
        ResponseFactory response = new ResponseFactory(routingContext, request);


        JsonObject update = new JsonObject();
        JsonObject updateObjectElement = new JsonObject();
        String id = request.getParams().getString("id");
        if(id.equals("") || id == null){
            logger.error(String.format("update exception: %s", "采集任务id为必填参数"));
            response.error(TASKID.getCode(), TASKID.getMsg());
            return;
        }
        update.put("id",id);
        String taskName = request.getParams().getString("taskName");
        if(!taskName.equals("") && taskName != null){
            updateObjectElement.put("taskName",taskName);
        }
        String targetIP = request.getParams().getString("targetIP");
        if(!targetIP.equals("") && targetIP != null){
            updateObjectElement.put("targetIP",targetIP);
        }
        String ICDId = request.getParams().getString("ICDId");
        if( ICDId != null && !ICDId.equals("")){
            updateObjectElement.put("ICDId",ICDId);
        }

        JsonObject tdrCfg = request.getParams().getJsonObject("tdrCfg");
        if(tdrCfg != null){
            updateObjectElement.put("tdrCfg",tdrCfg);
        }

        JsonObject anaCfg = request.getParams().getJsonObject("anaCfg");
        if(anaCfg != null){
            updateObjectElement.put("anaCfg",anaCfg);
        }
        Object defaultCheckedObjet= request.getParams().getValue("defaultChecked");
        if(defaultCheckedObjet != null){
            if(!FormValidator.isJsonArray(defaultCheckedObjet)){
                logger.error(String.format("update exception: %s", "defaultChecked格式错误"));
                response.error(DEFAULTCHECKED_FORMAT_ERRR.getCode(), DEFAULTCHECKED_FORMAT_ERRR.getMsg());
                return;
            }
            JsonArray defaultChecked = (JsonArray) defaultCheckedObjet;
            updateObjectElement.put("defaultChecked",defaultChecked);
        }
        JsonObject updateObject = new JsonObject().put("$set",updateObjectElement);

        mongoClient.findOneAndUpdate("task",update,updateObject,r->{
            if(r.failed()){
                logger.error(String.format("UpdateProject task exception: %s", Tools.getTrace(r.cause())));
                response.error(UPDATE_TARGET_ERROR.getCode(), UPDATE_TARGET_ERROR.getMsg());
                return;
            }
            JsonObject result = new JsonObject();
            response.success(result);
            logger.info("采集任务修改成功："+id);
        });


    }
}
