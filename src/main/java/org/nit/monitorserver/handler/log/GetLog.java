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
        String level = request.getParams().getString("level");
        if(level.equals("") || level == null){
            logger.error(String.format("search exception: %s", "日志级别为必填参数"));
            response.error(LEVEL.getCode(), LEVEL.getMsg());
            return;
        }
        searchObject.put("level",level);
        //开始时间
        JsonObject timeZoneObject = new JsonObject();
        String startTime = request.getParams().getString("startTime");
        if(startTime.equals("") || startTime == null){
            startTime = "1949-10-1 00:00:00";
        }
        //结束时间
        String endTime = request.getParams().getString("endTime");
        if(endTime.equals("") || endTime == null){
            endTime = "2050-01-01 00:00:00";
        }
        JsonArray timeZoneArray = new JsonArray();
        JsonObject startTimeObject = new JsonObject().put("dataTime",new JsonObject().put("$gte",startTime));
        JsonObject endTimeObject = new JsonObject().put("dataTime",new JsonObject().put("$lte",endTime));
        timeZoneArray.add(startTimeObject).add(endTimeObject);
        searchObject.put("$and",timeZoneArray);
        //事件ID
        Object eventIDObject = request.getValue("eventID");
        if(eventIDObject != null){
            if(FormValidator.isInteger(eventIDObject)){
                int eventID = (int) eventIDObject;
                searchObject.put("eventID",eventID);

            }
        }
        String targetIP = request.getParams().getString("targetIP");
        if(!targetIP.equals("") && targetIP != null){
            searchObject.put("targetIP",targetIP);
        }
        //数据库查询
        mongoClient.find("log",searchObject,r->{
            if(r.failed()){
                logger.error(String.format("find log exception: %s", Tools.getTrace(r.cause())));
                response.error(DATA_QUERY_ERROR.getCode(), DATA_QUERY_ERROR.getMsg());
                return;
            }else if(r.result().size() == 0){
                logger.error(String.format("find log exception: %s","记录不存在"));
                response.error(RECORD_NOT_EXISTED.getCode(), RECORD_NOT_EXISTED.getMsg());
                return;
            }
            JsonObject logList = new JsonObject();
            logList.put("logList",r.result());
            response.success(logList);

        });



    }
}
