package org.nit.monitorserver.handler.data;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.FindOptions;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.web.RoutingContext;
import org.apache.log4j.Logger;
import org.nit.monitorserver.constant.HttpHeaderContentType;
import org.nit.monitorserver.database.MongoConnection;
import org.nit.monitorserver.database.MysqlConnection;
import org.nit.monitorserver.message.AbstractRequestHandler;
import org.nit.monitorserver.message.Request;
import org.nit.monitorserver.message.ResponseFactory;
import org.nit.monitorserver.util.FormValidator;
import org.nit.monitorserver.util.Tools;

import java.io.IOException;

import static org.nit.monitorserver.constant.ResponseError.TASKID;
import static org.nit.monitorserver.constant.ResponseError.*;

/**
 * @author 20817
 * @version 1.0
 * @className SearchRealTimeData
 * @description
 * @date 2020/9/10 13:39
 */
public class SearchRealTimeData extends AbstractRequestHandler {
    protected static final Logger logger = Logger.getLogger(SearchRealTimeData.class);
    private final MongoClient mongoClient = new MongoConnection().getMongoClient();
    private final SQLClient mySQLClient = new MysqlConnection().getMySQLClient();
    static JsonObject comResult = new JsonObject();
    static JsonObject anaResult = new JsonObject();

    @Override
    public void handle(RoutingContext routingContext, Request request) throws IOException, Exception {
        routingContext.response().putHeader(HttpHeaderContentType.CONTENT_TYPE_STR, HttpHeaderContentType.JSON);
        ResponseFactory response = new ResponseFactory(routingContext, request);

        Object idObject = request.getParams().getValue("id");
        if(idObject == null || idObject.toString().equals("")){
            logger.error(String.format("search realtime date exception: %s", "采集任务id为必填参数"));
            response.error(TASKID.getCode(), TASKID.getMsg());
            return;
        }
        if(!FormValidator.isString(idObject)){
            logger.error(String.format("search realtime date exception: %s", "采集任务id为必填参数"));
            response.error(TASKID_FORMAT_ERROR.getCode(), TASKID_FORMAT_ERROR.getMsg());
            return;
        }
        String id = idObject.toString();//采集任务id

        //eventID
        Object eventIDObject = request.getParams().getValue("eventID");
        if(eventIDObject == null || eventIDObject.toString().equals("")){
            logger.error(String.format("search realtime date exception: %s", "eventID为必填参数"));
            response.error(DEFAULTEVTID.getCode(), DEFAULTEVTID.getMsg());
            return;
        }
        if(!FormValidator.isInteger(eventIDObject)){
            logger.error(String.format("search realtime date exception: %s", "evtID格式错误"));
            response.error(DEFAULTEVTID_FORMAT_ERROR.getCode(), DEFAULTEVTID_FORMAT_ERROR.getMsg());
            return;
        }
        int eventID = (int) eventIDObject;

        //dataType

        Object dataTypeObject = request.getParams().getValue("dataType");
        if(dataTypeObject == null){
            response.error(DATATYPE.getCode(),DATATYPE.getMsg());
            logger.error(String.format("search realTime Data exception: %s", "事件类型为必填参数"));
            return;
        }
        if(!FormValidator.isInteger(dataTypeObject)){
            response.error(DATATYPE_FORMAT_ERROR.getCode(),DATATYPE_FORMAT_ERROR.getMsg());
            logger.error(String.format("search realTime Data exception: %s", "事件类型格式错误"));
            return;
        }
        int dataType = (int) dataTypeObject;
        if(dataType != 0 && dataType != 1){
            response.error(DATATYPE_FORMAT_ERROR.getCode(),DATATYPE_FORMAT_ERROR.getMsg());
            logger.error(String.format("search realTime Data exception: %s", "事件类型格式错误"));
            return;
        }




        mongoClient.find("comdrt",new JsonObject().put("taskID",id),r->{
            if(r.failed()){
                logger.error(String.format("search realTime data: %s 查找失败", Tools.getTrace(r.cause())));
                response.error(QUERY_FAILURE.getCode(), QUERY_FAILURE.getMsg());
                return;
            }else if(r.result().size() == 0){
                logger.info(String.format("采集任务：%s 的实时数据不存在",id));
                response.success(new JsonObject());
                return;
            }
            String drt_id = r.result().get(0).getString("drt_id");
            switch (dataType){
                case 0://通信实时数据
                    String query = "SELECT * FROM detrcd_raws_table WHERE drt_id = "+drt_id+" AND evevttype = "+eventID+" ORDER BY timestamp DESC LIMIT 1";
                    mySQLClient.query(query,re->{
                        if(re.failed()){
                            logger.error(String.format("search realTime data : %s 查找失败", Tools.getTrace(re.cause())));
                            response.error(QUERY_FAILURE.getCode(), QUERY_FAILURE.getMsg());
                            return;
                        }else if(re.result().getNumRows() == 0){
                            logger.info("search realTime data: 实时通信数据不存在");
                            response.success(new JsonObject());
                            return;
                        }
                        JsonObject element = re.result().getRows().get(0);
                        if(element != comResult){
                            logger.info("实时通信数据获取成功");
                            response.success(new JsonObject().put("dataList",element));
                            comResult = element;
                            return;
                        }else {
                            logger.info("search realTime data: 实时通信数据不存在");
                            response.success(new JsonObject());
                            return;
                        }

                    });
                case 1:
                    JsonObject searchObject = new JsonObject().put("taskID",id).put("evtId",eventID);
                    mongoClient.find("targetIPEvtId",searchObject,rq->{
                        if(rq.failed()){
                            logger.error(String.format("search realTime data: %s 查找失败", Tools.getTrace(rq.cause())));
                            response.error(QUERY_FAILURE.getCode(),QUERY_FAILURE.getMsg());
                            return;
                        }else if(rq.result().size() == 0){
                            logger.info("search realTime data: 实时分析数据不存在");
                            response.success(new JsonObject());
                            return;
                        }
                        String targetIP = rq.result().get(0).getString("targetIP");
                        String collectionName = targetIP+eventID;
                        FindOptions options = new FindOptions().setLimit(1).setSort(new JsonObject().put("timeStamp",-1));
                        mongoClient.findWithOptions(collectionName,new JsonObject(),options,rs->{
                            if(rs.failed()){
                                logger.error(String.format("search realTime data: %s 查找失败", Tools.getTrace(rs.cause())));
                                response.error(QUERY_FAILURE.getCode(),QUERY_FAILURE.getMsg());
                                return;
                            }else if(rs.result().size() == 0){
                                logger.info("search realTime data: 实时分析数据不存在");
                                response.success(new JsonObject());
                                return;
                            }
                            JsonObject element = rs.result().get(0);
                            if(anaResult != element){
                                logger.info("实时分析数据获取成功");
                                response.success(new JsonObject().put("dataList",element));
                                anaResult = element;
                                return;
                            }
                        });

                    });
            }


        });
    }
}
