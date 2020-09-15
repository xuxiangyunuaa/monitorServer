package org.nit.monitorserver.handler.log;

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
import sun.awt.geom.AreaOp;

import java.io.IOException;

import static org.nit.monitorserver.constant.ResponseError.*;

/**
 * @ClassName GetLog
 * @Description TODO获取日志信息
 * @Author 20643
 * @Date 2020-9-1 14:59
 * @Version 1.0
 **/
public class GetLog extends AbstractRequestHandler {
    protected static final Logger logger = Logger.getLogger(GetLog.class);
    private final MongoClient mongoClient = new MongoConnection().getMongoClient();

    @Override
    public void handle(final RoutingContext routingContext,final Request request) throws IOException {
        routingContext.response().putHeader(HttpHeaderContentType.CONTENT_TYPE_STR, HttpHeaderContentType.JSON);
        ResponseFactory response = new ResponseFactory(routingContext, request);

        JsonObject searchObject = new JsonObject();
        //日志级别
        Object levelObject = request.getParams().getValue("level");
        if (levelObject == null || levelObject.toString().equals("")){
            logger.error(String.format("search log exception: %s", "日志级别为必填参数"));
            response.error(LEVEL.getCode(), LEVEL.getMsg());
            return;
        }
        if(!FormValidator.isString(levelObject)){
            logger.error(String.format("search log exception: %s", "日志级别格式错误"));
            response.error(LEVEL_FORMAT_ERROR.getCode(), LEVEL_FORMAT_ERROR.getMsg());
            return;
        }
        String level = levelObject.toString();
        if(!level.equals("error") && !level.equals("info") && !level.equals("all")){
            logger.error(String.format("search log exception: %s", "日志级别格式错误"));
            response.error(LEVEL_FORMAT_ERROR.getCode(), LEVEL_FORMAT_ERROR.getMsg());
            return;
        }
        searchObject.put("level",level);

        //开始时间

        Object startTimeObject = request.getParams().getValue("startTime");
        String startTime = new String();
        if(startTimeObject == null || startTimeObject.toString().equals("")){
            startTime = "1949-10-1 00:00:00";
        }else if(!FormValidator.isString(startTimeObject)){
            logger.error(String.format("search log exception: %s", "开始时间格式错误"));
            response.error(STARTTIME_FORMAT_ERROR.getCode(), STARTTIME_FORMAT_ERROR.getMsg());
            return;
        }else {
            startTime = startTimeObject.toString();
        }



        //结束时间
        Object endTimeObject = request.getParams().getValue("endTime");
        String endTime = new String();
        if(endTimeObject == null || endTimeObject.toString().equals("")){
            endTime = "2050-10-1 00:00:00";
        }else if(!FormValidator.isString(endTimeObject)){
            logger.error(String.format("search log exception: %s", "结束时间格式错误"));
            response.error(ENDTIME_FORMAT_ERROR.getCode(), ENDTIME_FORMAT_ERROR.getMsg());
            return;
        }else {
            endTime = endTimeObject.toString();
        }

        JsonArray timeZoneArray = new JsonArray();
        JsonObject startTimeZone = new JsonObject().put("timeStamp",new JsonObject().put("$gte",startTime));
        JsonObject endTimeZone = new JsonObject().put("timeStamp",new JsonObject().put("$lte",endTime));
        timeZoneArray.add(startTimeObject).add(startTimeObject);
        searchObject.put("$and",timeZoneArray);

        //module
        Object moduleObject = request.getParams().getValue("module");
        if(moduleObject != null && moduleObject.toString().equals("")){
            if(!FormValidator.isString(moduleObject)){
                logger.error(String.format("search log exception: %s", "模块名称格式错误"));
                response.error(MODULENAME_FORMAT_ERROR.getCode(), MODULENAME_FORMAT_ERROR.getMsg());
                return;
            }
            String module = moduleObject.toString();
            searchObject.put("module",module);
        }
        //数据库查询
        mongoClient.find("log",searchObject,r->{
            if(r.failed()){
                logger.error(String.format("search log exception: %s", Tools.getTrace(r.cause())));
                response.error(DATA_QUERY_ERROR.getCode(), DATA_QUERY_ERROR.getMsg());
                return;
            }
            JsonObject logList = new JsonObject();
            logList.put("logList",r.result());
            response.success(logList);

        });



    }
}
