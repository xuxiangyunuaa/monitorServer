package org.nit.monitorserver.handler.dataIncentive;

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

import static org.nit.monitorserver.constant.ResponseError.INCENTIVEID;
import static org.nit.monitorserver.constant.ResponseError.*;
import static org.nit.monitorserver.constant.ResponseError.UPDATE_FAILURE;

public class UpdateDataIncentive extends AbstractRequestHandler {
    protected static final Logger logger = Logger.getLogger(UpdateDataIncentive.class);
    private final MongoClient mongoClient = new MongoConnection().getMongoClient();
    CreateLog createLog = new CreateLog();
    @Override
    public void handle(RoutingContext routingContext, Request request) throws IOException, Exception {
        routingContext.response().putHeader(HttpHeaderContentType.CONTENT_TYPE_STR, HttpHeaderContentType.JSON);
        ResponseFactory response = new ResponseFactory(routingContext, request);


        Object idObject = request.getParams().getValue("id");
        Object nameObject = request.getParams().getValue("name");
        Object dataObject = request.getParams().getValue("data");
        Object periodObject = request.getParams().getValue("period");
        Object portObject = request.getParams().getValue("port");
        Object targetIPObject = request.getParams().getValue("targetIP");

        //id
        if(idObject== null || idObject.toString().equals("")){
            logger.error(String.format("update dataIncentive exception: %s", "数据激励id为必填参数"));
            response.error(INCENTIVEID.getCode(), INCENTIVEID.getMsg());
            createLog.createLogRecord("数据激励","error","更新数据激励","数据激励id为必填参数");
            return;
        }
        if(!FormValidator.isString(idObject)){
            logger.error(String.format("update dataIncentive exception: %s", "数据激励id格式错误"));
            response.error(INCENTIVEID.getCode(), INCENTIVEID.getMsg());
            createLog.createLogRecord("数据激励","error","更新数据激励","数据激励id格式错误");
            return;
        }
        String id = idObject.toString();
        JsonObject update = new JsonObject().put("id",id);//定位

        //name
        JsonObject updateElement = new JsonObject();
        if(nameObject != null && !nameObject.toString().equals("")){
            if(!FormValidator.isString(nameObject)){
                logger.error(String.format("update dataIncentive exception: %s", "数据激励名称格式错误"));
                response.error(INCENTIVENAME_FORMAT_ERROR.getCode(), INCENTIVENAME_FORMAT_ERROR.getMsg());
                createLog.createLogRecord("数据激励","error","更新数据激励","数据激励名称格式错误");
                return;
            }
            String name = nameObject.toString();
            updateElement.put("name",name);
        }
        //data
        if(dataObject != null && !dataObject.toString().equals("{}")){
            if (!FormValidator.isJsonObject(dataObject)){
                logger.error(String.format("update dataIncentive exception: %s", "激励数据格式错误"));
                response.error(DATA_FORMAT_ERROR.getCode(), DATA_FORMAT_ERROR.getMsg());
                createLog.createLogRecord("数据激励","error","更新数据激励","激励数据格式错误");
                return;
            }
            JsonObject data = (JsonObject)dataObject;
            updateElement.put("data",data);

        }

        //period
        if(periodObject != null && !periodObject.toString().equals("")){
            if(!FormValidator.isString(periodObject)){
                logger.error(String.format("update dataIncentive exception: %s", "激励周期格式错误"));
                response.error(PERIOD_FORMAT_ERROR.getCode(), PERIOD_FORMAT_ERROR.getMsg());
                createLog.createLogRecord("数据激励","error","更新数据激励","激励周期格式错误");
                return;
            }
            String period = periodObject.toString();
            updateElement.put("period",period);
        }

        //port
        if(portObject != null && !portObject.toString().equals("")){
            if(!FormValidator.isString(portObject)){
                logger.error(String.format("update dataIncentive exception: %s", "激励的虚端口格式错误"));
                response.error(INCENTIVEPORT_FORMAT_ERROR.getCode(), INCENTIVEPORT_FORMAT_ERROR.getMsg());
                createLog.createLogRecord("数据激励","error","更新数据激励","激励的虚端口格式错误");
                return;
            }
            String port = portObject.toString();
            updateElement.put("port",port);
        }

        //targetIP
        if(targetIPObject != null && !targetIPObject.toString().equals("")){
            if(!FormValidator.isString(targetIPObject)){
                logger.error(String.format("update dataIncentive exception: %s", "激励的目标机IP错误"));
                response.error(INCENTIVETARGETIP_FORMAT_ERROR.getCode(), INCENTIVETARGETIP_FORMAT_ERROR.getMsg());
                createLog.createLogRecord("数据激励","error","更新数据激励","激励的目标机IP错误");
                return;
            }
            String targetIP = targetIPObject.toString();
            updateElement.put("targetIP",targetIP);
        }

        JsonObject updateObject = new JsonObject();
        if(!updateElement.toString().equals("{}")){
            updateObject.put("$set",updateElement);
        }else {
            logger.info(String.format("update dataIncentive: %s success:",id));
            response.success(new JsonObject());
            return;
        }


        mongoClient.findOneAndUpdate("dataIncentive",update,updateObject,r->{
            if(r.failed()){
                logger.error(String.format("Update dataIncentive exception: %s", Tools.getTrace(r.cause())));
                response.error(UPDATE_FAILURE.getCode(), UPDATE_FAILURE.getMsg());
                createLog.createLogRecord("数据激励","error","更新数据激励",String.format("数据激励：%s 更新失败",id));
                return;
            }
            JsonObject result = new JsonObject();
            response.success(result);
            logger.info("激励数据更新成功："+id);
            createLog.createLogRecord("数据激励","info","更新数据激励",String.format("数据激励：%s 更新成功",id));
            return;
        });



    }
}
