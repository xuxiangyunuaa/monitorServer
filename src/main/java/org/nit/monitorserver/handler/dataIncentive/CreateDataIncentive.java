package org.nit.monitorserver.handler.dataIncentive;

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

import java.io.IOException;
import java.lang.reflect.Array;

import static org.nit.monitorserver.constant.ResponseError.*;

public class CreateDataIncentive extends AbstractRequestHandler {
    protected static final Logger logger = Logger.getLogger(CreateDataIncentive.class);
    private final MongoClient mongoClient = new MongoConnection().getMongoClient();
    CreateLog createLog = new CreateLog();

    @Override
    public void handle(RoutingContext routingContext, Request request) throws IOException, Exception {
        routingContext.response().putHeader(HttpHeaderContentType.CONTENT_TYPE_STR, HttpHeaderContentType.JSON);
        ResponseFactory response = new ResponseFactory(routingContext, request);
        Object nameObject = request.getParams().getValue("name");
        Object dataObject = request.getParams().getValue("data");
        Object periodObject = request.getParams().getValue("period");
        Object portObject = request.getParams().getValue("port");
        Object targetIPObject = request.getParams().getValue("targetIP");
        JsonObject createObject = new JsonObject();

        //name
        if(nameObject == null || nameObject.toString().equals("")){
            logger.error(String.format("create dataIncentive exception: %s", "激励任务名称为必填参数"));
            response.error(INCENTIVENAME.getCode(), INCENTIVENAME.getMsg());
            createLog.createLogRecord("数据激励","error","新建数据激励","激励任务名称为必填参数");
            return;
        }
        if(!FormValidator.isString(nameObject)){
            logger.error(String.format("create dataIncentive exception: %s", "激励任务名称格式错误"));
            response.error(INCENTIVENAME_FORMAT_ERROR.getCode(), INCENTIVENAME_FORMAT_ERROR.getMsg());
            createLog.createLogRecord("数据激励","error","新建数据激励","激励任务名称格式错误");
            return;
        }
        String name = nameObject.toString();
        createObject.put("name",name);

        //data
        if(dataObject == null || dataObject.toString().equals("{}")){
            logger.error(String.format("create dataIncentive exception: %s", "激励数据为必填参数"));
            response.error(INCENTIVEDATA.getCode(), INCENTIVEDATA.getMsg());
            createLog.createLogRecord("数据激励","error","新建数据激励","激励数据为必填参数");
            return;
        }
        if(!FormValidator.isJsonObject(dataObject)){
            logger.error(String.format("create dataIncentive exception: %s", "激励数据格式错误"));
            response.error(DATA_FORMAT_ERROR.getCode(), DATA_FORMAT_ERROR.getMsg());
            createLog.createLogRecord("数据激励","error","新建数据激励","激励数据格式错误");
            return;
        }
        JsonObject data = (JsonObject) dataObject;
        createObject.put("data",data);

        if(portObject == null || portObject.toString().equals("")){
            logger.error(String.format("create dataIncentive exception: %s", "激励的虚端口为必填参数"));
            response.error(INCENTIVEPORT.getCode(), INCENTIVEPORT.getMsg());
            createLog.createLogRecord("数据激励","error","新建数据激励","激励的虚端口为必填参数");
            return;
        }
        if(!FormValidator.isString(portObject)){
            logger.error(String.format("create dataIncentive exception: %s", "激励的虚端口格式错误"));
            response.error(INCENTIVEPORT_FORMAT_ERROR.getCode(), INCENTIVEPORT_FORMAT_ERROR.getMsg());
            createLog.createLogRecord("数据激励","error","新建数据激励","激励的虚端口格式错误");
            return;
        }
        String port = portObject.toString();
        createObject.put("port",port);

        //targetIP
        if(targetIPObject == null || targetIPObject.toString().equals("")){
            logger.error(String.format("create dataIncentive exception: %s", "激励的目标机IP为必填参数"));
            response.error(INCENTIVETARGETIP.getCode(), INCENTIVETARGETIP.getMsg());
            createLog.createLogRecord("数据激励","error","新建数据激励","激励的目标机IP为必填参数");
            return;
        }
        if(!FormValidator.isString(targetIPObject)){
            logger.error(String.format("create dataIncentive exception: %s", "激励的目标机IP格式错误"));
            response.error(INCENTIVETARGETIP_FORMAT_ERROR.getCode(), INCENTIVETARGETIP_FORMAT_ERROR.getMsg());
            createLog.createLogRecord("数据激励","error","新建数据激励","激励的目标机IP格式错误");
            return;
        }
        String targetIP = targetIPObject.toString();
        createObject.put("targetIP",targetIP);


        int runFlag = 0;

        if(periodObject != null){
            if(!FormValidator.isString(periodObject)){
                logger.error(String.format("create dataIncentive exception: %s", "激励的周期格式错误"));
                response.error(PERIOD_FORMAT_ERROR.getCode(), PERIOD_FORMAT_ERROR.getMsg());
                return;
            }
            String period =  periodObject.toString();
            createObject.put("period",period);
        }

        mongoClient.find("dataIncentive",createObject,re->{
            if(re.failed()){
                logger.error(String.format("search dataIncenive: %s 查找失败", Tools.getTrace(re.cause())));
                response.error(QUERY_FAILURE.getCode(), QUERY_FAILURE.getMsg());
                createLog.createLogRecord("数据激励","error","新建数据激励","查找数据激励失败");
                return;
            }else if(re.result().size() == 0){
                String id = Tools.generateId();
                createObject.put("id",id);
                mongoClient.insert("dataIncentive",createObject,r->{
                    if(r.failed()){
                        logger.error(String.format("new dataIncentive create exception: %s", Tools.getTrace(r.cause())));
                        response.error(INSERT_FAILURE.getCode(), INSERT_FAILURE.getMsg());
                        createLog.createLogRecord("数据激励","error","新建数据激励","数据激励新建失败");
                        return;
                    }
                    response.success(new JsonObject().put("result",id));
                    logger.info("激励任务创建成功："+id);
                    createLog.createLogRecord("数据激励","info","新建数据激励",String.format("数据激励: %s 新建失败",id));
                    return;


                });

            }else {
                logger.error("该数据激励记录已经存在");
                response.error(RECORD_EXISTED.getCode(), RECORD_EXISTED.getMsg());
                createLog.createLogRecord("数据激励","error","新建数据激励","该数据激励已经存在");
                return;
            }
        });


    }
}
