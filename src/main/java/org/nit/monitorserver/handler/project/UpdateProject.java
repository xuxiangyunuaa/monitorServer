package org.nit.monitorserver.handler.project;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.RoutingContext;
import org.apache.log4j.Logger;
import org.nit.monitorserver.constant.HttpHeaderContentType;
import org.nit.monitorserver.database.MongoConnection;
import org.nit.monitorserver.handler.ICD.UpdateICD;
import org.nit.monitorserver.handler.log.CreateLog;
import org.nit.monitorserver.handler.task.Create;
import org.nit.monitorserver.message.AbstractRequestHandler;
import org.nit.monitorserver.message.Request;
import org.nit.monitorserver.message.ResponseFactory;
import org.nit.monitorserver.util.FormValidator;
import org.nit.monitorserver.util.Tools;

import java.io.IOException;

import static org.nit.monitorserver.constant.ResponseError.PROJECTID;
import static org.nit.monitorserver.constant.ResponseError.PROJECTID_FORMAT_ERROR;
import static org.nit.monitorserver.constant.ResponseError.PROJECTNAME;
import static org.nit.monitorserver.constant.ResponseError.*;

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
    CreateLog createLog = new CreateLog();

    @Override
    public void handle(final RoutingContext routingContext,final Request request) throws IOException {
        routingContext.response().putHeader(HttpHeaderContentType.CONTENT_TYPE_STR, HttpHeaderContentType.JSON);
        ResponseFactory response = new ResponseFactory(routingContext, request);

        //工程id
        Object idObject = request.getParams().getValue("id");
        if(idObject == null || idObject.toString().equals("")){
            logger.error(String.format("update project exception: %s", "工程的id为必填参数"));
            response.error(PROJECTID.getCode(), PROJECTID.getMsg());
            createLog.createLogRecord("工程管理","error","更新工程","工程的id为必填参数");
            return;
        }
        if(!FormValidator.isString(idObject)){
            logger.error(String.format("update project exception: %s", "工程的id格式错误"));
            response.error(PROJECTID_FORMAT_ERROR.getCode(), PROJECTID_FORMAT_ERROR.getMsg());
            createLog.createLogRecord("工程管理","error","更新工程","工程的id格式错误");
            return;
        }
        String id = idObject.toString();
        JsonObject update = new JsonObject().put("id",id);

        JsonObject updateObjectElement = new JsonObject();

        //名称
        Object nameObject = request.getParams().getValue("name");
        if(nameObject != null && !nameObject.toString().equals("")){
            if(!FormValidator.isString(nameObject)){
                logger.error(String.format("update project exception: %s", "工程名称格式错误"));
                response.error(PROJECTNAME_FORMAT_ERROR.getCode(), PROJECTNAME_FORMAT_ERROR.getMsg());
                createLog.createLogRecord("工程管理","error","更新工程","工程名称格式错误");
                return;
            }
            String name = nameObject.toString();
            updateObjectElement.put("name",name);
        }

        //工程内容
        Object contentObject = request.getParams().getValue("content");
        if(contentObject != null && !contentObject.toString().equals("[]")){
            if(!FormValidator.isJsonArray(contentObject)){
                logger.error(String.format("update project exception: %s", "工程内容格式错误"));
                response.error(CONTENT_FORMAT_ERROR.getCode(), CONTENT_FORMAT_ERROR.getMsg());
                createLog.createLogRecord("工程管理","error","更新工程","工程内容格式错误");
                return;
            }
            JsonArray content = (JsonArray) contentObject;
            updateObjectElement.put("content",content);
        }

        JsonObject updateObject = new JsonObject();
        if(!updateObjectElement.toString().equals("{}")){
            updateObject.put("$set",updateObjectElement);
        }else {
            logger.info(String.format("update project: %s success:",id));
            response.success(new JsonObject());
            return;
        }
        System.out.println("updateObject:"+updateObject);


        mongoClient.findOneAndUpdate("project",update,updateObject,r->{
            if(r.failed()){
                logger.error(String.format("Update project exception: %s", Tools.getTrace(r.cause())));
                response.error(UPDATE_FAILURE.getCode(), UPDATE_FAILURE.getMsg());
                createLog.createLogRecord("工程管理","error","更新工程",String.format("工程：%s 更新失败",id));
                return;
            }
            JsonObject result = new JsonObject();
            response.success(result);
            createLog.createLogRecord("工程管理","info","更新工程",String.format("工程：%s 更新成功",id));
            return;
        });
    }
}
