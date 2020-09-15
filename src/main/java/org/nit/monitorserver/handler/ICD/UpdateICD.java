package org.nit.monitorserver.handler.ICD;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.RoutingContext;
import org.apache.log4j.Logger;
import org.nit.monitorserver.constant.HttpHeaderContentType;
import org.nit.monitorserver.database.MongoConnection;
import org.nit.monitorserver.handler.log.CreateLog;
import org.nit.monitorserver.handler.task.Create;
import org.nit.monitorserver.message.AbstractRequestHandler;
import org.nit.monitorserver.message.Request;
import org.nit.monitorserver.message.ResponseFactory;
import org.nit.monitorserver.util.FormValidator;
import org.nit.monitorserver.util.Tools;
import sun.awt.geom.AreaOp;

import java.io.IOException;

import static org.nit.monitorserver.constant.ResponseError.ICDID_IS_REQUIRED;
import static org.nit.monitorserver.constant.ResponseError.*;

/**
 * @ClassName UpdateProject
 * @Description TODO修改ICD
 * @Author 20643
 * @Date 2020-9-1 14:50
 * @Version 1.0
 **/
public class UpdateICD extends AbstractRequestHandler {

    protected static final Logger logger = Logger.getLogger(UpdateICD.class);
    private final MongoClient mongoClient = new MongoConnection().getMongoClient();
    CreateLog createLog = new CreateLog();

    @Override
    public void handle(final RoutingContext routingContext,final Request request) throws IOException {
        routingContext.response().putHeader(HttpHeaderContentType.CONTENT_TYPE_STR, HttpHeaderContentType.JSON);
        ResponseFactory response = new ResponseFactory(routingContext, request);


        Object idObject = request.getParams().getValue("id");
        Object nameObject = request.getParams().getValue("name");
        Object ICDObject = request.getParams().getValue("ICD");

        //id
        if(idObject == null || idObject.toString().equals("")){
            logger.error(String.format("update ICD exception: %s", "ICD的id为必填参数"));
            response.error(ICDID_IS_REQUIRED.getCode(), ICDID_IS_REQUIRED.getMsg());
            createLog.createLogRecord("ICD管理","error","更新ICD","ICD的id为必填参数");
            return;
        }
        if(!FormValidator.isString(idObject)){
            logger.error(String.format("update ICD exception: %s", "ICD的id格式错误"));
            response.error(ICDID_FORMAT_ERROR.getCode(), ICDID_FORMAT_ERROR.getMsg());
            createLog.createLogRecord("ICD管理","error","更新ICD","ICD的id格式错误");
            return;
        }
        String id = idObject.toString();
        JsonObject update = new JsonObject().put("id",id);//定位
        JsonObject updateObjectElement = new JsonObject();//更新

        //name
        if(nameObject != null && !nameObject.toString().equals("")){
            if(!FormValidator.isString(nameObject)){
                logger.error(String.format("update ICD exception: %s", "ICD名称格式错误"));
                response.error(ICDNAME_FORMAT_ERROR.getCode(), ICDNAME_FORMAT_ERROR.getMsg());
                createLog.createLogRecord("ICD管理","error","更新ICD","ICD名称格式错误");
                return;
            }
            String name = nameObject.toString();
            updateObjectElement.put("name",name);
        }

        if(ICDObject != null && !ICDObject.toString().equals("[]")){
            if(!FormValidator.isJsonArray(ICDObject)){
                logger.error(String.format("update ICD exception: %s", "ICD内容格式错误"));
                response.error(ICD_FORMAT_ERROR.getCode(), ICD_FORMAT_ERROR.getMsg());
                createLog.createLogRecord("ICD管理","error","更新ICD","ICD内容格式错误");
                return;
            }
            JsonArray ICD = (JsonArray) ICDObject;
            updateObjectElement.put("ICD",ICD);
        }

        JsonObject updateObject = new JsonObject();
        if(!updateObjectElement.toString().equals("{}")){
            updateObject.put("$set",updateObjectElement);
        }else {
            logger.info(String.format("update ICD: %s success:",id));
            response.success(new JsonObject());
            return;
        }

        mongoClient.findOneAndUpdate("ICD",update,updateObject,r->{
            if(r.failed()){
                logger.error(String.format("UpdateProject ICD exception: %s", Tools.getTrace(r.cause())));
                response.error(UPDATE_FAILURE.getCode(), UPDATE_FAILURE.getMsg());
                createLog.createLogRecord("ICD管理","error","更新ICD",String.format("ICD:%s 更新失败",id));
                return;
            }
            JsonObject result = new JsonObject();
            response.success(result);
            logger.info("更新ICD成功："+id);
            createLog.createLogRecord("ICD管理","info","更新ICD",String.format("ICD:%s 更新成功",id));
            return;
        });




    }
}
