//package org.nit.monitorserver.handler.task;
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.List;
////import com.sun.org.apache.bcel.internal.generic.NEW;
//import io.vertx.core.json.JsonArray;
//import io.vertx.core.json.JsonObject;
//import io.vertx.ext.sql.SQLClient;
//import io.vertx.ext.web.RoutingContext;
//import org.apache.log4j.Logger;
//import org.nit.monitorserver.constant.HttpHeaderContentType;
//import org.nit.monitorserver.database.MysqlConnection;
//import org.nit.monitorserver.message.AbstractRequestHandler;
//import org.nit.monitorserver.message.Request;
//import org.nit.monitorserver.message.ResponseFactory;
//import org.nit.monitorserver.util.FormValidator;
//import org.nit.monitorserver.util.Tools;
//
//import java.text.ParseException;
//
//import static org.nit.monitorserver.constant.GlobalConsts.*;
//import static org.nit.monitorserver.constant.ResponseError.*;
//
//public class GetTaskRecordList extends AbstractRequestHandler {
//    protected static final Logger logger = Logger.getLogger(GetTaskRecordList.class);
//    private final SQLClient sqlClient = new MysqlConnection().getMySQLClient();
//
//    @Override
//    public void handle(RoutingContext routingContext, Request request) throws ParseException {
//        routingContext.response().putHeader(HttpHeaderContentType.CONTENT_TYPE_STR, HttpHeaderContentType.STREAM);
//        ResponseFactory response = new ResponseFactory(routingContext, request);//根据路由上下文以及请求产生响应
//        //获取参数
//        Object planeNameObj = request.getParamWithKey(PLANE_NAME);
//        Object startTimeObj = request.getParamWithKey(START_TIME);
//        Object endTimeObj = request.getParamWithKey(END_TIME);
//        Object taskTypeObj = request.getParamWithKey(TASK_TYPE);
//        Object pageNumberObj = request.getParamWithKey(PAGE_NUMBER);
////可全局使用
//        JsonArray params = new JsonArray();
//        JsonArray params2=new JsonArray();
//        JsonArray params3=new JsonArray();
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
//        DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        int offsetNumber;
////        验证页码格式的正确性
//        if (pageNumberObj == null) {
//            logger.info(String.format("exception:%s", "页码为必选参数"));
//            response.error(SERVER_ERROR.getCode(), SERVER_ERROR.getMsg());
//            return;
//        } else {
//            if (!FormValidator.isInteger(pageNumberObj)) {
//                logger.info(String.format("exception:%s", "页码格式错误"));
//                response.error(FORMAT_ERROR.getCode(), FORMAT_ERROR.getMsg());
//                return;
//            } else {
//                Integer pageNumber = (int) pageNumberObj;
//                offsetNumber = (pageNumber - 1) * 10;
//                params.add(offsetNumber);
//            }
//        }
//
//        if (planeNameObj == null && startTimeObj == null && endTimeObj == null && taskTypeObj == null) {
//            //默认显示前100条数据
////占位符不把数据写死
//            String sql = "SELECT AJ_Num,TM_Name,AJ_StartTime,AJ_EndTime,AJ_Type FROM PM_AcquisitionJobs WHERE AJ_Flag=1  LIMIT ?,10;";
//            sqlClient.queryWithParams(sql, params, ar -> {
//                if (ar.failed()) {
//                    logger.error(String.format("query:%s exception %s", sql, Tools.getTrace(ar.cause())));
//                    response.error(SERVER_ERROR.getCode(), SERVER_ERROR.getMsg());
//                }
//                if (ar.result().getNumRows() == 0) {
//                    logger.error(String.format("exception:%s", "查询失败"));
//                    response.error(QUERY_FAILURE.getCode(), QUERY_FAILURE.getMsg());
//                }
//                logger.info(String.format("查询记录成功：%s", ar.result()));
//
//                String countsql = "SELECT COUNT(*) FROM PM_AcquisitionJobs WHERE AJ_Flag=1;";
//                sqlClient.query(countsql, countres -> {
//                    if (countres.failed()) {
//                        logger.error(String.format("query:%s exception %s", countsql, Tools.getTrace(countres.cause())));
//                        response.error(SERVER_ERROR.getCode(), SERVER_ERROR.getMsg());
//                    }
////取结果集中的数据
//                    Object object = countres.result().getResults().get(0).getValue(0);
//                    long page = 1;
//                    if (object != null) {
//                        long itemNumber = (long) object;
////                        限制默认显示的页数
//                        if(itemNumber<=100 && itemNumber>0){
//                            page = (itemNumber - 1) / 10 + 1;
//                        }else{
//                            page=10;
//                        }
//                        logger.info(String.format("返回的总页数为：%s", page));
//                    }
//
//                    //            对结果进行包装，并返回给前端,保持与前端接收数据结构一致性,若不包装，返回的是数组列表，并不是json型
//
//                    JsonArray resultArray = new JsonArray();
//                    List<JsonArray> dataList = ar.result().getResults();
//                    JsonObject result = new JsonObject();
//                    for (int i = 0; i < dataList.size(); i++) {
//                        JsonArray jsonArray = dataList.get(i);
//                        resultArray.add(new JsonObject().put("TASK_INDEX", jsonArray.getValue(0))
//                                .put("PLANE_NAME", jsonArray.getValue(1))
//                                .put("START_TIME", jsonArray.getValue(2).toString().replace("T"," ").replace("Z"," "))
//                                .put("END_TIME", jsonArray.getValue(3).toString().replace("T"," ").replace("Z"," "))
//                                .put("TASK_TYPE", jsonArray.getValue(4)));
//                    }
//                    result.put("dataList", resultArray).put("pageNumber", page);
//                    response.success(result);
//                    return;
//                });
//            });
//        } else {
//            //验证目标机非空以及格式
//            if (planeNameObj == null) {
//                logger.info(String.format("exception:%s", "目标机为必填参数"));
//                response.error(SERVER_ERROR.getCode(), SERVER_ERROR.getMsg());
//                return;
//            } else {
//                if (!FormValidator.isString(planeNameObj)) {
//                    logger.info(String.format("exception:%s", "目标机格式错误"));
//                    response.error(FORMAT_ERROR.getCode(), FORMAT_ERROR.getMsg());
//                    return;
//                } else {
//                    String planeName = planeNameObj.toString();
//                    params3.add(planeName);
//                    params2.add(planeName);
//
//                }
//            }
//            //验证查询日期非空以及格式
//            if (startTimeObj == null) {
//                logger.info(String.format("exception:%s", "开始时间为必填参数"));
//                response.error(SERVER_ERROR.getCode(), SERVER_ERROR.getMsg());
//                return;
//            } else {
////                开始时间对象转字符型
//                String startTime = startTimeObj.toString();
//                startTime= startTime.replace("Z", " UTC");
////                字符型转date型
//                Date date1= df.parse(startTime);
////                date型格式化
//                String startTime_date = df2.format(date1);
//
//                if (!FormValidator.isValidYYYYMMDDHHmmSSString(startTime_date)) {
//                    logger.info(String.format("exception:%s", "开始时间格式错误"));
//                    response.error(FORMAT_ERROR.getCode(), FORMAT_ERROR.getMsg());
//                    return;
//                } else {
//                    params3.add(startTime_date);
//                    params2.add(startTime_date );
//
//                }
//            }
//
//            if (endTimeObj == null) {
//                logger.info(String.format("exception:%s", "结束时间为必填参数"));
//                response.error(SERVER_ERROR.getCode(), SERVER_ERROR.getMsg());
//                return;
//            } else {
//                //                结束时间对象转字符型
//                String endTime = endTimeObj.toString();
//                endTime=endTime.replace("Z", " UTC");
////                字符型转date型
//                Date date1= df.parse(endTime);
////                date型格式化
//                String endTime_date = df2.format(date1);
//
//                if (!FormValidator.isValidYYYYMMDDHHmmSSString(endTime_date)) {
//                    logger.info(String.format("exception:%s", "结束时间格式错误"));
//                    response.error(FORMAT_ERROR.getCode(), FORMAT_ERROR.getMsg());
//                    return;
//                } else {
//                    params3.add(endTime_date);
//                    params2.add(endTime_date);
//
//
//                }
//            }
//            //验证采集任务类型的格式
//            if (taskTypeObj == null) {
//                logger.info(String.format("exception:%s", "任务类型为必填参数"));
//                response.error(SERVER_ERROR.getCode(), SERVER_ERROR.getMsg());
//                return;
//            } else {
//                     String taskType = taskTypeObj.toString();
//                     taskType = taskType.replace("[","").replace("]","").replace("\"","");
//
//                if (!FormValidator.isString(taskType)) {
//                    logger.info(String.format("exception:%s", "任务类型格式错误"));
//                    response.error(FORMAT_ERROR.getCode(), FORMAT_ERROR.getMsg());
//                    return;
//                } else {
//                    params3.add(taskType);
//                    params2.add(taskType);
////                    System.out.println(taskType);
//                }
//            }
//            params2.add(offsetNumber) ;
//            for(int i=0;i<params2.size();i++){
//                System.out.println(params2.getValue(i));
//            }
//            //数据库条件查询
////            String sql = "SELECT AJ_Num,TM_Name,AJ_StartTime,AJ_EndTime，AJ_Type FROM PM_AcquisitionJobs LIMIT ?,10 WHERE TM_Name=? AND AJ_StartTime >= ? AND AJ_EndTime? AND AJ_Type=?  AND AJ_Flag=1";
//            String sql = "SELECT AJ_Num,TM_Name,AJ_StartTime,AJ_EndTime,AJ_Type FROM PM_AcquisitionJobs  " +
//                    "WHERE TM_Name=? AND  UNIX_TIMESTAMP(AJ_StartTime) >= UNIX_TIMESTAMP(?)  AND UNIX_TIMESTAMP(?) >= UNIX_TIMESTAMP(AJ_EndTime)  " +
//                    "AND AJ_Type=? AND AJ_Flag=1  LIMIT ?,10";
//            sqlClient.queryWithParams(sql,params2, ar -> {
//                if (ar.failed()) {
//                    logger.error(String.format("query:%s exception %s", sql, Tools.getTrace(ar.cause())));
//                    response.error(SERVER_ERROR.getCode(), SERVER_ERROR.getMsg());
//                    return;
//                }
//                if (ar.result().getNumRows() == 0) {
//                    System.out.println(ar.result().getNumRows());
//                    logger.info(String.format(String.format("exception:%s", "条件查询失败")));
//                    response.error(QUERY_FAILURE.getCode(), QUERY_FAILURE.getMsg());
//                    return;
//                }
//                //            统计总共满足条件的记录数
//                String countSQL = "SELECT COUNT(*) FROM PM_AcquisitionJobs"
//                                  + "WHERE TM_Name=?   AND  UNIX_TIMESTAMP(AJ_StartTime) >= UNIX_TIMESTAMP(?)  AND UNIX_TIMESTAMP(?) >= UNIX_TIMESTAMP(AJ_EndTime)  AND AJ_Type=? AND AJ_Flag=1 ";
//                sqlClient.queryWithParams(countSQL,params3,countRes -> {
//                    if (countRes.failed()) {
//                        logger.error(String.format("query:%s exception:%s", countSQL, Tools.getTrace(countRes.cause())));
//                        response.error(SERVER_ERROR.getCode(), SERVER_ERROR.getMsg());
//                        return;
//                    }
//
//                    long page = ((long)countRes.result().getResults().get(0).getValue(0)-1)/ 10 + 1;
//                    logger.info(String.format("返回的总页数为：%s", page));
//                    logger.info(String.format("查询记录成功：%s", ar.result()));
//                    JsonArray resultArray = new JsonArray();
//                    List<JsonArray> resultArrayList = ar.result().getResults();
//                    for (int i = 0; i < resultArrayList.size(); i++) {
//                        JsonArray jsonArray = resultArrayList.get(i);
//                        resultArray.add(new JsonObject().put(TASK_INDEX, jsonArray.getValue(0))
//                                .put(PLANE_NAME, jsonArray.getValue(1))
//                                .put(START_TIME, jsonArray.getValue(2).toString().replace("T"," ").replace("Z"," "))
//                                .put(END_TIME, jsonArray.getValue(3).toString().replace("T"," ").replace("Z"," "))
//                                .put(TASK_TYPE, jsonArray.getValue(4)));
//                    }
//                    response.success(new JsonObject().put("dataList", resultArray).put("pageNumber", page));
//                });
//            });
//        }
//    }
//}
//
//
//
//
//
