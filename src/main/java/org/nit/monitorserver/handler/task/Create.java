package org.nit.monitorserver.handler.task;

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

import javax.sound.midi.Soundbank;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.nit.monitorserver.constant.ResponseError.DEFAULTCHECKED;
import static org.nit.monitorserver.constant.ResponseError.*;

public class Create extends AbstractRequestHandler {

    protected static final Logger logger = Logger.getLogger(Create.class);
    //    private final SQLClient mySQLClient = new MysqlConnection().getMySQLClient();
    private final MongoClient mongoClient = new MongoConnection().getMongoClient();
    CreateLog createLog = new CreateLog();

    @Override
    public void handle(RoutingContext routingContext, Request request) throws IOException, Exception {
        routingContext.response().putHeader(HttpHeaderContentType.CONTENT_TYPE_STR, HttpHeaderContentType.JSON);
        ResponseFactory response = new ResponseFactory(routingContext, request);

        JsonObject createObject = new JsonObject();

        //taskName
        Object taskNameObject = request.getParams().getValue("taskName");
        if(taskNameObject.toString().equals("") || taskNameObject.toString() == null){
            logger.error(String.format("create task exception: %s", "采集任务名称为必填参数"));
            response.error(TASKNAME.getCode(), TASKNAME.getMsg());
            createLog.createLogRecord("任务管理","error","新建任务","采集任务名称为必填参数");
            return;
        }

        if(!FormValidator.isString(taskNameObject)){
            logger.error(String.format("create task exception: %s", "采集任务名称格式错误"));
            response.error(TASKNAME_FORMAT_ERROR.getCode(), TASKNAME_FORMAT_ERROR.getMsg());
            createLog.createLogRecord("任务管理","error","新建任务","采集任务名称格式错误");
            return;
        }
        String taskName = (String) taskNameObject;

        createObject.put("taskName",taskName);

        //targetIP
        Object targetIPObject = request.getParams().getValue("targetIP");
        System.out.println("targetIP:"+targetIPObject);
        if(targetIPObject.toString() == null || targetIPObject.toString().equals("")){
            logger.error(String.format("create task exception: %s", "目标机IP为必填参数"));
            response.error(IP_IS_REQUIRED.getCode(), IP_IS_REQUIRED.getMsg());
            createLog.createLogRecord("任务管理","error","新建任务","目标机IP为必填参数");
            return;
        }
        if(!FormValidator.isString(targetIPObject)){
            logger.error(String.format("create task exception: %s", "目标机IP格式错误"));
            response.error(TARGETIP_FORMAT_ERROR.getCode(), TARGETIP_FORMAT_ERROR.getMsg());
            createLog.createLogRecord("任务管理","error","新建任务","目标机IP格式错误");
            return;
        }
        String targetIP = (String) targetIPObject;

        createObject.put("targetIP",targetIP);


        //ICDId
        Object ICDIdObject = request.getParams().getValue("ICDId");//ICD的id,可选参数
        System.out.println("ICDId:"+ICDIdObject);
        if(ICDIdObject.toString() == null || ICDIdObject.toString().equals("")){
            logger.error(String.format("create task exception: %s", "ICD的Id为必填参数"));
            response.error(ICDID_IS_REQUIRED.getCode(), ICDID_IS_REQUIRED.getMsg());
            createLog.createLogRecord("任务管理","error","新建任务","ICD的d为必填参数");
            return;
        }
        if(!FormValidator.isString(ICDIdObject)){
            logger.error(String.format("create task exception: %s", "ICDId格式错误"));
            response.error(ICDID_FORMAT_ERROR.getCode(), ICDID_FORMAT_ERROR.getMsg());
            createLog.createLogRecord("任务管理","error","新建任务","ICD的d格式错误");
            return;
        }
        String ICDId = (String) ICDIdObject;

        createObject.put("ICDId",ICDId);

        //defaultChecked
        Object defaultCheckedObject = request.getParams().getValue("defaultChecked");

        if(defaultCheckedObject == null || defaultCheckedObject.toString().equals("[]")){
            logger.error(String.format("create task exception: %s", "配置参数为必填参数"));
            response.error(DEFAULTCHECKED.getCode(), DEFAULTCHECKED.getMsg());
            createLog.createLogRecord("任务管理","error","新建任务","配置参数为必填参数");
            return;
        }
        if(!FormValidator.isJsonArray(defaultCheckedObject)){
            logger.error(String.format("create task exception: %s", "配置参数格式错误"));
            response.error(DEFAULTCHECKED_FORMAT_ERRR.getCode(), DEFAULTCHECKED_FORMAT_ERRR.getMsg());
            createLog.createLogRecord("任务管理","error","新建任务","配置参数格式错误");
            return;
        }
        JsonArray defaultChecked = (JsonArray)defaultCheckedObject;
        createObject.put("defaultChecked",defaultChecked);

        Object defaultEvtIdObject = request.getParams().getValue("defaultEvtId");
        if(defaultEvtIdObject == null || defaultEvtIdObject.toString().equals("[]")){
            logger.error(String.format("create task exception: %s", "配置参数的evtId为必填参数"));
            response.error(DEFAULTEVTID.getCode(), DEFAULTEVTID.getMsg());
            createLog.createLogRecord("任务管理","error","新建任务","配置参数的evtId为必填参数");
            return;
        }
        if(!FormValidator.isJsonArray(defaultEvtIdObject)){
            logger.error(String.format("create task exception: %s", "配置参数的evtId格式错误"));
            response.error(DEFAULTEVTID_FORMAT_ERROR.getCode(), DEFAULTEVTID_FORMAT_ERROR.getMsg());
            createLog.createLogRecord("任务管理","error","新建任务","配置参数的evtId格式错误");
            return;
        }
        JsonArray defaultEvtId = (JsonArray) defaultEvtIdObject;
        createObject.put("defaultEvtId",defaultEvtId);


        //tdrCfg
        Object tdrCfgObject = request.getParams().getValue("tdrCfg");
        if(tdrCfgObject != null && !tdrCfgObject.toString().equals("{}")){
            if(!FormValidator.isJsonObject(tdrCfgObject)){
                logger.error(String.format("create task exception: %s", "通信配置格式错误"));
                response.error(TDRCFG_FORMAT_ERROR.getCode(), TDRCFG_FORMAT_ERROR.getMsg());
                createLog.createLogRecord("任务管理","error","新建任务","通信配置格式错误");
                return;
            }
            JsonObject tdrCfg = (JsonObject)  tdrCfgObject;
            createObject.put("tdrCfg",tdrCfg);
        }

        //anaCfg
        Object anaCfgObject = request.getParams().getValue("anaCfg");
        if(anaCfgObject != null && !anaCfgObject.toString().equals("{}")){
            if(!FormValidator.isJsonObject(anaCfgObject)){
                logger.error(String.format("create task exception: %s", "分析配置格式错误"));
                response.error(ANACFG_FORMAT_ERROR.getCode(), ANACFG_FORMAT_ERROR.getMsg());
                createLog.createLogRecord("任务管理","error","新建任务","分析配置格式错误");
                return;
            }

            JsonObject anaCfg = (JsonObject) anaCfgObject;
            createObject.put("anaCfg",anaCfg);

        }

        mongoClient.find("task",createObject,re->{
            if(re.failed()){
                logger.error(String.format("search 采集任务: %s 查找失败", Tools.getTrace(re.cause())));
                response.error(QUERY_FAILURE.getCode(), QUERY_FAILURE.getMsg());
                createLog.createLogRecord("任务管理","error","新建任务","查找分析任务失败");
                return;
            }else if(re.result().size()== 0){//在不存在相同采集任务时才插入数据库
                String id = Tools.generateId();
                createObject.put("id",id).put("runFlag",0).put("drt_id",0);//生成任务id
                JsonArray evtIdList = new JsonArray();
                for(int i = 0; i < defaultEvtId.size(); i++){
                    int evtId = defaultEvtId.getInteger(i);
                    String idLatest = id+evtId;
                    JsonObject element = new JsonObject();
                    element.put(idLatest,evtId);
                    evtIdList.add(element);
                }
                createObject.put("defaultEvtId",evtIdList);

                mongoClient.insert("task",createObject,r->{
                    if(r.failed()){
                        logger.error(String.format("new task insert exception: %s", Tools.getTrace(r.cause())));
                        response.error(INSERT_FAILURE.getCode(), INSERT_FAILURE.getMsg());
                        createLog.createLogRecord("任务管理","error","新建任务",String.format("采集任务：%s 插入数据库失败",id));
                        return;
                    }

//                    for(int i = 0; i < defaultEvtId.size(); i++){
//                        int evtId = defaultEvtId.getInteger(i);
//                        String idLatest = id+evtId;
//                        JsonObject element = new JsonObject();
//                        element.put("id",idLatest).put("evtId",evtId);

//                        mongoClient.insert(collectionName,element,ry->{
//                            if(ry.failed()){
//                                logger.error(String.format("create evtIdList exception: %s", Tools.getTrace(ry.cause())));
//                                response.error(INSERT_FAILURE.getCode(), INSERT_FAILURE.getMsg());
//                                return;
//                            }
//                        });
//                    }
                    JsonObject result = new JsonObject().put("id",id);
                    response.success(result);
                    logger.info("采集任务创建成功："+ id);
                    createLog.createLogRecord("任务管理","info","新建任务",String.format("采集任务：%s 插入数据库成功",id));
                    return;

                });

            }else {
                response.error(RECORD_EXISTED.getCode(), RECORD_EXISTED.getMsg());
                logger.error(String.format("new task insert exception: %s", "该记录已经存在"));
                createLog.createLogRecord("任务管理","error","新建任务","采集任务已经存在");
                return;
            }
        }
        );

    }
}
