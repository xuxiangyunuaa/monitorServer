package org.nit.monitorserver.handler.data;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.RoutingContext;
import org.nit.monitorserver.constant.HttpHeaderContentType;
import org.nit.monitorserver.database.MongoConnection;
import org.nit.monitorserver.message.AbstractRequestHandler;
import org.nit.monitorserver.message.Request;

import java.io.IOException;
import org.apache.log4j.Logger;
import org.nit.monitorserver.message.ResponseFactory;
import org.nit.monitorserver.util.FormValidator;
import org.nit.monitorserver.util.Tools;

import static org.nit.monitorserver.constant.ResponseError.*;

public class SearchData extends AbstractRequestHandler {
    protected static final Logger logger = Logger.getLogger(SearchData.class);
    private final MongoClient mongoClient = new MongoConnection().getMongoClient();

    @Override
    public void handle(RoutingContext routingContext, Request request) throws IOException, Exception {
        routingContext.response().putHeader(HttpHeaderContentType.CONTENT_TYPE_STR, HttpHeaderContentType.JSON);
        ResponseFactory response = new ResponseFactory(routingContext, request);

        JsonObject searchObject = new JsonObject();
        //targetIP
        String targetIP = request.getParams().getString("targetIP");
        if(targetIP != null && !targetIP.equals("")){
            searchObject.put("targetIP",targetIP);
        }
        //eventIP
        Object eventIDObject = request.getParams().getValue("eventID");
        if(eventIDObject != null){
            if(!FormValidator.isInteger(eventIDObject)){
                response.error(EVENTTYPE_FORMAT_ERROR.getCode(),EVENTTYPE_FORMAT_ERROR.getMsg());
                logger.error(String.format("search exception: %s", "事件ID格式错误"));
            }
            int evtId = (int) eventIDObject;
            searchObject.put("evtId",evtId);

        }



        mongoClient.find("targetIPEvtId",searchObject,r->{
            if(r.failed()){
                logger.error(String.format("search targetIP+evtId: %s 查找失败", Tools.getTrace(r.cause())));
                response.error(QUERY_FAILURE.getCode(), QUERY_FAILURE.getMsg());
                return;
            }
            JsonObject dataList = new JsonObject().put("dataList",r.result());
            response.success(dataList);
        });















    }
}



//package org.nit.monitorserver.handler.data;
//
//import io.vertx.core.json.JsonArray;
//import io.vertx.core.json.JsonObject;
//import io.vertx.ext.mongo.MongoClient;
//import io.vertx.ext.sql.SQLClient;
//import io.vertx.ext.web.RoutingContext;
//import org.apache.log4j.Logger;
//import org.nit.monitorserver.constant.HttpHeaderContentType;
//import org.nit.monitorserver.database.MongoConnection;
//import org.nit.monitorserver.database.MysqlConnection;
//import org.nit.monitorserver.message.AbstractRequestHandler;
//import org.nit.monitorserver.message.Request;
//import org.nit.monitorserver.message.ResponseFactory;
//import org.nit.monitorserver.util.FormValidator;
//import org.nit.monitorserver.util.Tools;
//
//import java.io.IOException;
//
//import static org.nit.monitorserver.constant.ResponseError.*;
//
///**
// * @ClassName SearchDataIncentive
// * @Description TODO查找数据
// * @Author 20643
// * @Date 2020-9-1 14:45
// * @Version 1.0
// **/
//public class SearchData extends AbstractRequestHandler {
//    protected static final Logger logger = Logger.getLogger(SearchData.class);
//    private final MongoClient mongoClient = new MongoConnection().getMongoClient();
//    private final SQLClient mySQLClient = new MysqlConnection().getMySQLClient();
//
//    @Override
//    public void handle(final RoutingContext routingContext,final Request request) throws IOException {
//        routingContext.response().putHeader(HttpHeaderContentType.CONTENT_TYPE_STR, HttpHeaderContentType.JSON);
//        ResponseFactory response = new ResponseFactory(routingContext, request);
//
//        JsonObject searchObject = new JsonObject();
//
//        //采集事件ID
//        String taskID = request.getParams().getString("taskID");
//        if(!taskID.equals("") && taskID != null){//实时数据
//            searchObject.put("taskID",taskID);
//            String query = "SELECT * FROM detrcd_latest_data;";
//            mySQLClient.query(query,r->{
//                if(r.failed()){
//                    logger.error(String.format("search exception: %s",Tools.getTrace(r.cause())));
//                    response.error(QUERY_FAILURE.getCode(), QUERY_FAILURE.getMsg());
//                    return;
//                }else if(r.result().getResults().size() ==0){
//                    logger.error("该记录不存在");
//                    response.error(RECORD_NOT_EXISTED.getCode(), RECORD_NOT_EXISTED.getMsg());
//                    return;
//                }
//                int eventID = r.result().getRows().get(0).getInteger("eventType");
//                byte[] data = r.result().getRows().get(0).getBinary("drt_data");
//                mongoClient.find("targetExecuting",new JsonObject(),re->{
//                    if(re.failed()){
//                        logger.error(String.format("search exception: %s",Tools.getTrace(re.cause())));
//                        response.error(QUERY_FAILURE.getCode(), QUERY_FAILURE.getMsg());
//                        return;
//                    }
//                    String id = re.result().get(0).getString("ctrID");
//                    String targetIP = re.result().get(0).getString("targetIP");
//
//
//                });
//
//
//            });
//
//        }
//
//
//
//        //目标机IP
//        String targetIP = request.getParams().getString("targetIP");
//        if(!targetIP.equals("") && targetIP != null){
//           searchObject.put("targetIP",targetIP);
//
//
//
//
//
//
//        }
//        Object eventTypeObject = request.getParams().getValue("eventType");
//        if(eventTypeObject != null){
//            if(!FormValidator.isJsonObject(eventTypeObject)){
//                logger.error(String.format("search exception: %s", "事件配置为必填参数"));
//                response.error(EVENTTYPE_FORMAT_ERROR.getCode(), EVENTID_FORMAT_ERROR.getMsg());
//                return;
//            }
//            JsonObject eventType = (JsonObject) eventTypeObject;
//            //eventID
//            Object eventIDObject = eventType.getValue("eventID");
//            if(eventIDObject == null){
//                logger.error(String.format("search exception: %s", "事件id为必填参数"));
//                response.error(EVENTID.getCode(), EVENTID.getMsg());
//                return;
//            }else if(!FormValidator.isInteger(eventIDObject)){
//                logger.error(String.format("search exception: %s", "事件id格式错误"));
//                response.error(EVENTID_FORMAT_ERROR.getCode(), EVENTID_FORMAT_ERROR.getMsg());
//                return;
//            }
//            int eventID = (int) eventIDObject;
//            ////field
//            Object fieldObject = eventType.getValue("field");
//            if(fieldObject != null){
//                if(!FormValidator.isJsonObject(fieldObject)){
//                    logger.error(String.format("search exception: %s", "筛选域格式错误"));
//                    response.error(FIELD_FORMAT_ERROR.getCode(), FIELD_FORMAT_ERROR.getMsg());
//                    return;
//                }
//                JsonObject field = (JsonObject) fieldObject;
//                //pdID
//                Object pdIDObject = field.getValue("pdID");
//                if(pdIDObject != null){
//                    if(!FormValidator.isInteger(pdIDObject)){
//                        logger.error(String.format("search exception: %s", "pdID格式错误"));
//                        response.error(PDID_FORMAT_ERROR.getCode(), PDID_FORMAT_ERROR.getMsg());
//                        return;
//                    }
//                    int pdID = (int) pdIDObject;
//                }
//
//                //startTime
//                Object startTimeObject = field.getValue("startTime");
//                if(startTimeObject != null){
//                    if(FormValidator.isString(startTimeObject)){
//                        logger.error(String.format("search exception: %s", "startTime格式错误"));
//                        response.error(STARTTIME_FORMAT_ERROR.getCode(), STARTTIME_FORMAT_ERROR.getMsg());
//                        return;
//                    }
//                    String startTime = startTimeObject.toString();
//                }
//
//                //endTime
//                Object endTimeObject = field.getValue("endTime");
//                if(endTimeObject != null){
//                    if(FormValidator.isString(endTimeObject)){
//                        logger.error(String.format("search exception: %s", "endTime格式错误"));
//                        response.error(ENDTIME_FORMAT_ERROR.getCode(), ENDTIME_FORMAT_ERROR.getMsg());
//                        return;
//                    }
//                    String endTime = endTimeObject.toString();
//                }
//            }
//        }
//
//
//
//
//
//
//
//
//
//
//            //pdID
//            Object pdIDObject = eventType.getValue("pdID");
//            if(pdIDObject != null){
//                if(!FormValidator.isInteger(pdIDObject)){
//                    logger.error(String.format("search exception: %s", "域格式错误"));
//                    response.error(FIELD_FORMAT_ERROR.getCode(), FIELD_FORMAT_ERROR.getMsg());
//                    return;
//                }
//                int pdID = (int) pdIDObject;
//
//
//            }
//            //startTime
//            String startTime = eventType.getString("startTime");
//            if(startTime.equals("") || startTime == null){
//                startTime = "1949-10-1 00:00:00";
//            }
//            JsonObject startTimeObject = new JsonObject().put("$gte",startTime);
//            //endTime
//            String endTime = eventType.getString("endTime");
//            if(endTime.equals("") || endTime == null){
//                endTime = "2050-10-01 00:00:00";
//            }
//            JsonObject endTimeObject = new JsonObject().put("$lte",endTime);
//            fieldObject.put("timeStamp",new JsonArray().add(startTimeObject).add(endTimeObject));
//
//            Object portIDObject = eventType.getValue("portID");
//            if(portIDObject != null){
//                if(!FormValidator.isInteger(pdIDObject)){
//                    logger.error(String.format("search exception: %s", "对象ID格式错误"));
//                    response.error(PORTID_FORMAT_ERROR.getCode(), PORTID_FORMAT_ERROR.getMsg());
//                    return;
//                }
//                int portID = (int) portIDObject;
//
//        }
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//        //查看采集任务数据类型
//        Object typeObject = request.getParams().getValue("type");
//        if(typeObject != null){
//            if(!FormValidator.isInteger(typeObject)){
//                logger.error(String.format("search exception: %s", "采集任务id为必填参数"));
//                response.error(SEARCHDATATYPE.getCode(), SEARCHDATATYPE.getMsg());
//                return;
//            }
//            int type = (int) typeObject;
//            if(type != 0 && type != 1){
//                logger.error(String.format("search exception: %s", "采集任务id为必填参数"));
//                response.error(SEARCHDATATYPE.getCode(), SEARCHDATATYPE.getMsg());
//                return;
//            }
//            if(type == 0){//实时数据
//                String query = "SELECT * FROM detrcd_latest_data;";
//                mySQLClient.query(query,r->{
//                    if(r.failed()){
//                        logger.error(String.format("search exception: %s",Tools.getTrace(r.cause())));
//                        response.error(QUERY_FAILURE.getCode(), QUERY_FAILURE.getMsg());
//                        return;
//                    }else if(r.result().getResults().size() ==0){
//                        logger.error("该记录不存在");
//                        response.error(RECORD_NOT_EXISTED.getCode(), RECORD_NOT_EXISTED.getMsg());
//                        return;
//                    }
//                    String drt_id = r.result().getRows().get(0).getString("drt_id");
//                    int eventType = r.result().getRows().get(0).getInteger("eventType");
//                    String drt_partitonname = r.result().getRows().get(0).getString("drt_partitoname");
////                    String drt_timestamp =  r.result().getRows().get(0).getString("drt_timestamp");
//                    String drt_portname = r.result().getRows().get(0).getString("drt_portname");
//                    byte[] drt_data = r.result().getRows().get(0).getBinary("drt_data");
//                    DataParse dataParse = new DataParse();
//                    JsonObject communicationDataParsed = dataParse.communicationDataParse(eventType,drt_data,drt_portname);
//                    String id = Tools.generateId();
//                    communicationDataParsed.put("id",id)
//                            .put("drt_id",drt_id)
//                            .put("partitonName",drt_partitonname)
//                            .put("taskId",taskId);
//
//                    mongoClient.insert("collectionData",communicationDataParsed,res->{
//                        if(r.failed()){
//                            logger.error(String.format("new communication data insert exception: %s", Tools.getTrace(res.cause())));
//                            response.error(INSERT_FAILURE.getCode(), INSERT_FAILURE.getMsg());
//                            return;
//                        }
//
//                    });
//                });
//            }else {//历史数据
//
//
//            }
//        }
//        Object eventTypeObject = request.getParams().getValue("eventType");
//
//        if(eventTypeObject != null){
//            if(!FormValidator.isJsonObject(eventTypeObject)){
//                logger.error(String.format("search exception: %s", "事件配置为必填参数"));
//                response.error(EVENTTYPE_FORMAT_ERROR.getCode(), EVENTID_FORMAT_ERROR.getMsg());
//                return;
//            }
//
//            JsonObject eventType = (JsonObject) eventTypeObject;
//
//            Object eventIDObject = eventType.getValue("eventID");
//            if(eventIDObject == null){
//                logger.error(String.format("search exception: %s", "事件id为必填参数"));
//                response.error(EVENTID.getCode(), EVENTID.getMsg());
//                return;
//            }else if(!FormValidator.isInteger(eventIDObject)){
//                logger.error(String.format("search exception: %s", "事件id格式错误"));
//                response.error(EVENTID_FORMAT_ERROR.getCode(), EVENTID_FORMAT_ERROR.getMsg());
//                return;
//            }
//            int eventID = (int) eventIDObject;
//            JsonObject fieldObject = new JsonObject().put("eventID",eventID);
//            //pdID
//            Object pdIDObject = eventType.getValue("pdID");
//            if(pdIDObject != null){
//                if(!FormValidator.isInteger(pdIDObject)){
//                    logger.error(String.format("search exception: %s", "域格式错误"));
//                    response.error(FIELD_FORMAT_ERROR.getCode(), FIELD_FORMAT_ERROR.getMsg());
//                    return;
//                }
//                int pdID = (int) pdIDObject;
//                fieldObject.put("pdID",pdID);
//
//            }
//            //startTime
//            String startTime = eventType.getString("startTime");
//            if(startTime.equals("") || startTime == null){
//                startTime = "1949-10-1 00:00:00";
//            }
//            JsonObject startTimeObject = new JsonObject().put("$gte",startTime);
//            //endTime
//            String endTime = eventType.getString("endTime");
//            if(endTime.equals("") || endTime == null){
//                endTime = "2050-10-01 00:00:00";
//            }
//            JsonObject endTimeObject = new JsonObject().put("$lte",endTime);
//            fieldObject.put("timeStamp",new JsonArray().add(startTimeObject).add(endTimeObject));
//
//            Object portIDObject = eventType.getValue("portID");
//            if(portIDObject != null){
//                if(!FormValidator.isInteger(pdIDObject)){
//                    logger.error(String.format("search exception: %s", "对象ID格式错误"));
//                    response.error(PORTID_FORMAT_ERROR.getCode(), PORTID_FORMAT_ERROR.getMsg());
//                    return;
//                }
//                int portID = (int) portIDObject;
//                fieldObject.put("portID",portID);
//            }
//            createObject.put("eventType",fieldObject);
//        }
//        mongoClient.find("collectionData",createObject,r->{
//            if(r.failed()){
//                logger.error(String.format("search collectionData: %s 查找失败", Tools.getTrace(r.cause())));
//                response.error(QUERY_FAILURE.getCode(), QUERY_FAILURE.getMsg());
//                return;
//            }
//            JsonObject result = new JsonObject();
//            for(int i = 0; i < r.result().size();i++){
//                JsonObject resultElement = r.result().get(i);
//                int eventID = resultElement.getInteger("eventID");
//
//
//            }
//
//
//        });
//
//
//
//
//
//
//
//
//
//
//
//
//    }
//}
