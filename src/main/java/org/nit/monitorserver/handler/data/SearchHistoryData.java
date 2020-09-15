package org.nit.monitorserver.handler.data;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.RoutingContext;
import org.nit.monitorserver.constant.HttpHeaderContentType;
import org.nit.monitorserver.database.MongoConnection;
import org.nit.monitorserver.message.AbstractRequestHandler;
import org.nit.monitorserver.message.Request;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;
import org.nit.monitorserver.message.ResponseFactory;
import org.nit.monitorserver.util.FormValidator;
import org.nit.monitorserver.util.Tools;
import io.vertx.ext.sql.SQLClient;
import org.nit.monitorserver.database.MysqlConnection;


import javax.lang.model.element.Element;

import static org.nit.monitorserver.constant.ResponseError.*;

public class SearchHistoryData extends AbstractRequestHandler {
    protected static final Logger logger = Logger.getLogger(SearchHistoryData.class);
    private final MongoClient mongoClient = new MongoConnection().getMongoClient();
    private final SQLClient mySQLClient = new MysqlConnection().getMySQLClient();


    @Override
    public void handle(RoutingContext routingContext, Request request) throws IOException, Exception {
        routingContext.response().putHeader(HttpHeaderContentType.CONTENT_TYPE_STR, HttpHeaderContentType.JSON);
        ResponseFactory response = new ResponseFactory(routingContext, request);

        JsonObject searchObject = new JsonObject();
        //targetIP
        Object targetIPObject = request.getParams().getValue("targetIP");
        String targetIP = new String();
        if(targetIPObject != null && !targetIPObject.toString().equals("")){
            if(!FormValidator.isString(targetIPObject)){
                logger.error(String.format("search historyData exception: %s", "目标机IP格式错误"));
                response.error(TARGETIP_FORMAT_ERROR.getCode(), TARGETIP_FORMAT_ERROR.getMsg());
                return;
            }
            targetIP = targetIPObject.toString();
            searchObject.put("targetIP",targetIP);
        }


        //eventIP
        Object eventIDObject = request.getParams().getValue("eventID");
        int evtId = -1;
        if(eventIDObject != null){
            if(!FormValidator.isInteger(eventIDObject)){
                response.error(EVENTTYPE_FORMAT_ERROR.getCode(),EVENTTYPE_FORMAT_ERROR.getMsg());
                logger.error(String.format("search historyTdr exception: %s", "事件ID格式错误"));
                return;
            }
            evtId = (int) eventIDObject;
            searchObject.put("evtId",evtId);
        }
        final int evtIdFinal =evtId;

        //dataType
        int dataType = -1;
        Object dataTypeObject = request.getParams().getValue("dataType");
        if(dataTypeObject != null){
            if(!FormValidator.isInteger(dataTypeObject)){
                response.error(DATATYPE_FORMAT_ERROR.getCode(),DATATYPE_FORMAT_ERROR.getMsg());
                logger.error(String.format("search historyTdr exception: %s", "事件类型格式错误"));
                return;
            }
            dataType = (int) dataTypeObject;
            if(dataType != 0 && dataType != 1){
                response.error(DATATYPE_FORMAT_ERROR.getCode(),DATATYPE_FORMAT_ERROR.getMsg());
                logger.error(String.format("search historyTdr exception: %s", "事件类型格式错误"));
                return;
            }

        }

        //如果查看的是通信数据，需要获取玖翼Mysql
        switch (dataType){
            case 0://查看通信数据
                JsonObject findElement = new JsonObject().put("$ne","0");
                JsonObject findObject = new JsonObject().put("drt_id",findElement);//已经运行过的任务
                if(targetIP != null){
                    findObject.put("targetIP",targetIP);//增加前端过滤条件
                }
                mongoClient.find("task",findObject,rq->{
                    if(rq.failed()){
                        logger.error(String.format("search data : %s 查找失败", Tools.getTrace(rq.cause())));
                        response.error(QUERY_FAILURE.getCode(), QUERY_FAILURE.getMsg());
                        return;
                    }else if(rq.result().size() == 0){//不存在满足条件的任务
                        logger.info("search data: 记录不存在");
                        response.success(new JsonObject());
                        return;
                    }else {
                        String where = "WHERE";
                        int size = rq.result().size();
                        HashMap<String,String> drt_id_targetIP = new HashMap<>();
                        String query = "SELECT drt_id,drt_eventtype,drt_timestamp FROM detrcd_raws_table";
                        List<JsonObject> dataList = new ArrayList<>();

                        for(int i = 0; i < size; i ++){
                            String taskID = rq.result().get(i).getString("id");
                            String drt_idElement = rq.result().get(i).getString("drt_id");
                            String targetIPElement = rq.result().get(i).getString("targetIP");
                            String whereEvtId = " WHERE drt_id = "+drt_idElement;//不区分eventtype
                            drt_id_targetIP.put(drt_idElement,targetIPElement);

                            JsonArray defaultEvtId = rq.result().get(i).getJsonArray("defaultEvtId");
                            if(evtIdFinal == -1){//不区分evt
                                whereEvtId = whereEvtId.substring(0,whereEvtId.length()-2)+" AND";
                                for(int j = 0; j <defaultEvtId.size(); j ++){
                                    whereEvtId = whereEvtId + " eventtype = "+ defaultEvtId.getValue(j).toString()+" OR";
                                }
                                whereEvtId = whereEvtId.substring(0,whereEvtId.length()-2)+";";
                            }else {
                                whereEvtId = whereEvtId.substring(0,whereEvtId.length()-1)+" AND eventtype = "+evtIdFinal;
                            }
                            query = query+whereEvtId;
                            mySQLClient.query(query,rs->{
                                if(rs.failed()){
                                    logger.error(String.format("search raw communicationData: %s 查找失败", Tools.getTrace(rs.cause())));
                                    response.error(QUERY_FAILURE.getCode(), QUERY_FAILURE.getMsg());
                                    return;
                                }else if(rs.result().getNumRows() == 0){

                                }else {
                                    int sizeCom = rs.result().getNumRows();
                                    for(JsonObject element: rs.result().getRows()){
                                        String id = taskID+"&"+element.getString("eventtype");
                                        element.put("targetIP",drt_id_targetIP.get(element.getString("drt_id")))
                                        .put("id",id);
                                        dataList.add(element);
                                    }
                                }
                            });
                        }

//                        int whereLength = where.length();
//                        String subWhere = where.substring(0,whereLength-3);
//                        String query = "SELECT drt_id,drt_eventtype,drt_timestamp FROM detrcd_raws_table"+" "+subWhere;
//                        if(evtIdFinal != -1){
//                            query = query + " AND eventtype = "+evtIdFinal+";";//加入前端筛选条件
//                        }else {
//                            query = query+";";
//                        }
//                        mySQLClient.query(query,rs->{
//                            if(rs.failed()){
//                                logger.error(String.format("search raw communicationData: %s 查找失败", Tools.getTrace(rs.cause())));
//                                response.error(QUERY_FAILURE.getCode(), QUERY_FAILURE.getMsg());
//                                return;
//                            }else if(rs.result().getNumRows()> 0){
//                                int sizeCom = rs.result().getNumRows();//满足条件的mysql记录数
//                                List<JsonObject> dataList = new ArrayList<>();
//                                for(int i = 0 ; i < sizeCom; i++){
//                                    JsonObject element = rs.result().getRows().get(i);
//                                    String drt_id = element.getString("drt_id");
//                                    String id = drt_id_id.get(drt_id+element.getString("eventtype"));//数据id
//                                    if( id != null && !id.equals("")){
//                                        JsonObject listElement = new JsonObject()
//                                                .put("id",id)
//                                                .put("targetIP",drt_id_targetIP.get(drt_id))
//                                                .put("eventID",element.getString("eventtype"))
//                                                .put("timeStamp",element.getString("timestamp"));
//                                        dataList.add(listElement);
//                                    }
//
//                                }
//                                logger.info("历史通信数据获取成功");
//                                response.success(new JsonObject().put("dataList",dataList));
//                                return;
//                            }
//
//                        });



                    }
                });
            case 1:
                JsonObject searchAnaObject = new JsonObject();
                if(targetIP != null){
                    searchAnaObject.put("targetIP",targetIP);
                }
                if(evtId != -1){
                    searchAnaObject.put("evtId",evtId);
                }
                mongoClient.find("targetIPEvtId",searchAnaObject,ra->{
                    if(ra.failed()){
                        logger.error(String.format("search raw analysisData: %s 查找失败", Tools.getTrace(ra.cause())));
                        response.error(QUERY_FAILURE.getCode(), QUERY_FAILURE.getMsg());
                        return;
                    }else {
                        logger.info("历史分析数据获取成功");
                        JsonObject dataList = new JsonObject().put("dataList",ra.result());
                        response.success(dataList);
                        return;
                    }

                });



        }




















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
//public class SearchHistoryData extends AbstractRequestHandler {
//    protected static final Logger logger = Logger.getLogger(SearchHistoryData.class);
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
