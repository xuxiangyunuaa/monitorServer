package org.nit.monitorserver.handler.task;

import com.sun.org.apache.bcel.internal.generic.NEW;
import io.vertx.core.json.JsonArray;
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
    CreateLog createLog = new CreateLog();


    @Override
    public void handle(final RoutingContext routingContext,final Request request) throws IOException {
        routingContext.response().putHeader(HttpHeaderContentType.CONTENT_TYPE_STR, HttpHeaderContentType.JSON);
        ResponseFactory response = new ResponseFactory(routingContext, request);


        JsonObject update = new JsonObject();
        JsonObject updateObjectElement = new JsonObject();
        Object idObject = request.getParams().getValue("id");
        //id
        if(idObject == null || idObject.toString().equals("")){
            logger.error(String.format("update task exception: %s", "采集任务id为必填参数"));
            response.error(TASKID.getCode(), TASKID.getMsg());
            createLog.createLogRecord("任务管理","error","更新任务","采集任务id为必填参数");
            return;
        }
        if(!FormValidator.isString(idObject)){
            logger.error(String.format("update task exception: %s", "采集任务id格式错误"));
            response.error(TASKID_FORMAT_ERROR.getCode(), TASKID_FORMAT_ERROR.getMsg());
            createLog.createLogRecord("任务管理","error","更新任务","采集任务id格式错误");
            return;
        }
        String id = idObject.toString();
        update.put("id",id);

        //taskName
        Object taskNameObject = request.getParams().getValue("taskName");
        if(taskNameObject != null && !taskNameObject.toString().equals("")){
            if(!FormValidator.isString(taskNameObject)){
                logger.error(String.format("update task exception: %s", "采集任务名称格式错误"));
                response.error(TASKNAME_FORMAT_ERROR.getCode(), TASKNAME_FORMAT_ERROR.getMsg());
                createLog.createLogRecord("任务管理","error","更新任务","采集任务名称格式错误");
                return;
            }
            String taskName = taskNameObject.toString();
            updateObjectElement.put("taskName",taskName);
        }

        //targetIP
        Object targetIPObject = request.getParams().getValue("targetIP");
        if(targetIPObject != null && !targetIPObject.toString().equals("")){
            if(!FormValidator.isString(targetIPObject)){
                logger.error(String.format("update task exception: %s", "目标机IP格式错误"));
                response.error(TARGETIP_FORMAT_ERROR.getCode(), TARGETIP_FORMAT_ERROR.getMsg());
                createLog.createLogRecord("任务管理","error","更新任务","目标机IP格式错误");
                return;
            }
            String targetIP = targetIPObject.toString();
            updateObjectElement.put("targetIP",targetIP);
        }


        //ICD
        Object ICDIdObject = request.getParams().getValue("ICDId");
        if(ICDIdObject != null && !ICDIdObject.toString().equals("")){
            if(!FormValidator.isString(ICDIdObject)){
                logger.error(String.format("update task exception: %s", "ICD的Id格式错误"));
                response.error(ICDID_FORMAT_ERROR.getCode(), ICDID_FORMAT_ERROR.getMsg());
                createLog.createLogRecord("任务管理","error","更新任务","ICD的Id格式错误");
                return;
            }
            String ICDId = ICDIdObject.toString();
            updateObjectElement.put("ICDId",ICDId);
        }

        //通信配置
        Object tdrCfgObject = request.getParams().getValue("tdrCfg");
        if(tdrCfgObject != null && !tdrCfgObject.toString().equals("{}") ){
            if(!FormValidator.isJsonObject(tdrCfgObject)){
                logger.error(String.format("update task exception: %s", "通信配置格式错误"));
                response.error(TDRCFG_FORMAT_ERROR.getCode(), TDRCFG_FORMAT_ERROR.getMsg());
                createLog.createLogRecord("任务管理","error","更新任务","通信配置格式错误");
                return;
            }
            JsonObject tdrCfg =(JsonObject) tdrCfgObject;
            updateObjectElement.put("tdrCfg",tdrCfg);
        }

        //分析配置
        Object anaCfgObject = request.getParams().getValue("anaCfg");
        if(anaCfgObject != null && !anaCfgObject.toString().equals("{}")){
            if(!FormValidator.isJsonObject(anaCfgObject)){
                logger.error(String.format("update task exception: %s", "分析配置格式错误"));
                response.error(ANACFG_FORMAT_ERROR.getCode(), ANACFG_FORMAT_ERROR.getMsg());
                createLog.createLogRecord("任务管理","error","更新任务","分析配置格式错误");
                return;
            }
            JsonObject anaCfg = (JsonObject) anaCfgObject;
            updateObjectElement.put("anaCfg",anaCfg);
        }

        //defaultChecked
        Object defaultCheckedObject= request.getParams().getValue("defaultChecked");
        if(defaultCheckedObject != null && !defaultCheckedObject.toString().equals("[]")){
            if(!FormValidator.isJsonArray(defaultCheckedObject)){
                logger.error(String.format("update task exception: %s", "配置参数格式错误"));
                response.error(DEFAULTCHECKED_FORMAT_ERRR.getCode(), DEFAULTCHECKED_FORMAT_ERRR.getMsg());
                createLog.createLogRecord("任务管理","error","更新任务","配置参数格式错误");
                return;
            }
            JsonArray defaultChecked = (JsonArray) defaultCheckedObject;
            updateObjectElement.put("defaultChecked",defaultChecked);
        }


        JsonObject updateObject = new JsonObject();
        if(!updateObjectElement.toString().equals("{}")){
            updateObject.put("$set",updateObjectElement);
        }else {
            logger.info(String.format("update task: %s success:",id));
            response.success(new JsonObject());
            return;
        }


        mongoClient.findOneAndUpdate("task",update,updateObject,r->{
            if(r.failed()){
                logger.error(String.format("Update task exception: %s", Tools.getTrace(r.cause())));
                response.error(UPDATE_TARGET_ERROR.getCode(), UPDATE_TARGET_ERROR.getMsg());
                createLog.createLogRecord("任务管理","error","更新任务",String.format("采集任务：%s 更新失败",id));
                return;
            }
            JsonObject result = new JsonObject();
            response.success(result);
            logger.info("采集任务修改成功："+id);
            createLog.createLogRecord("任务管理","info","更新任务",String.format("采集任务：%s 更新成功",id));
        });


    }
}
