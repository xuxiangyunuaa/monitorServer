//package org.nit.monitorserver.handler.data;
//
//
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
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.List;
//
//import static org.nit.monitorserver.constant.DataRecordConsts.*;
//import static org.nit.monitorserver.constant.ResponseError.*;
//
//
//public class GetShowingDataSet extends AbstractRequestHandler {
//    //应用中记录日志
//    protected static final Logger logger = Logger.getLogger(GetShowingDataSet.class);
//    private final SQLClient mySQLClient = new MysqlConnection().getMySQLClient();
//
//
//
//
//    //vertx 路由的概念：接收 http 请求之后，找到请求对应的 route 的 handle(处理器) 处理
//    @Override
//    public void handle(final RoutingContext routingContext, final Request request) throws Exception {
//        //所有的请求都会调用这个处理器处理
//        routingContext.response().putHeader(HttpHeaderContentType.CONTENT_TYPE_STR, HttpHeaderContentType.JSON);
//        ResponseFactory response = new ResponseFactory(routingContext, request);
//
//        //获取前端发送过来的参数 （数据类型，开始时间，结束时间）
//        Object dataTypeObj = request.getParamWithKey(DATA_TYPE);
//        Object trNameObj = request.getParamWithKey(TR_NAME);
//        Object startTimeObj = request.getParamWithKey(START_TIME);
//        Object endTimeObj = request.getParamWithKey(END_TIME);
//
//
//
///*
//* 判断前端参数的格式是否满足条件（处理前端）
//* */
//        //首先判断是否为空
//        if (dataTypeObj == null) {
//            logger.info(String.format("exception:%s", "dataType为必填参数"));
//            response.error(USERNAME_IS_REQUIRED.getCode(), DATATYPE_IS_REQUIRED.getMsg());
//            return;
//        }
//        if (trNameObj == null) {
//            logger.info(String.format("exception:%s", "trName为必填参数"));
//            response.error(USERNAME_IS_REQUIRED.getCode(), DATATYPE_IS_REQUIRED.getMsg());
//            return;
//        }
//        if (startTimeObj == null) {
//            logger.info(String.format("exception:%s", "startTime为必填参数"));
//            response.error(PASSWORD_IS_REQUIRED.getCode(), STARTTIME_IS_REQUIRED.getMsg());
//            return;
//        }
//        if (endTimeObj == null) {
//            logger.info(String.format("exception:%s", "endTime为必填参数"));
//            response.error(PASSWORD_IS_REQUIRED.getCode(), ENDTIME_IS_REQUIRED.getMsg());
//            return;
//        }
//
//
//        //判断格式是否符合要求
//        if (!FormValidator.isString(startTimeObj)) {
//            logger.info(String.format("exception:%s", "startTime格式非法"));
//            response.error(PASSWORD_FORMAT_ERROR.getCode(), PASSWORD_FORMAT_ERROR.getMsg());
//            return;
//        }
//        //endTime：格式要求同上，且，大于startTime
//        if (!FormValidator.isString(endTimeObj)) {
//            logger.info(String.format("exception:%s", "endTime格式非法"));
//            response.error(PASSWORD_FORMAT_ERROR.getCode(), PASSWORD_FORMAT_ERROR.getMsg());
//            return;
//        }
//
//
///*
//* 根据输入的参数，执行数据库查询操作（处理后端）
//*
//* */
//        String dataType = dataTypeObj.toString();
//        dataType = dataType.replace("[","").replace("]","").replace("\"","");
//        String trName = trNameObj.toString();
//        trName = trName.replace("[","").replace("]","").replace("\"","");
//        String startTime = startTimeObj.toString();
//        String endTime = endTimeObj.toString();
//
///*
//* 获取开始时间和结束时间的标准格式
//* */
//        //将 startTime, endTime 解析为对应形式的 Date
//        startTime= startTime.replace("Z", " UTC");
//        endTime = endTime.replace("Z", " UTC");
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
//        Date date1 = df.parse(startTime);
//        Date date2 = df.parse(endTime);
//
//
//        //将 date 转化为 df2 的形式，转化成了 yyyy-MM-dd HH:mm:ss 形式
//        DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String startTime_date = df2.format(date1);
//        String endTime_date = df2.format(date2);
//
//
//
///*
//* 对于不同的数据类型，构造不同的 sql 语句。sql 的第一个查询字段都是时间，这是为了和前端对应，前端会取结果的第一个字段作为横坐标
//* */
//        String query = null;
//        String type = null;
//        /*partitionTimeMargin*/
//        if (dataType.equals("partitionTimeMargin,PTM_RunNum")) {
////            type = "分区时间余量";
//            query = "SELECT b.PTM_Time, b.PTM_RunNum FROM PM_AcquisitionJobs a join PM_PartitionTimeRemaining b join PM_TargetMachines c ON a.AJ_Num = b.AJ_ID AND a.TM_Name = c.TM_Name WHERE unix_timestamp(AJ_StartTime) >= unix_timestamp(?) AND unix_timestamp(AJ_EndTime) <= unix_timestamp(?) AND c.TM_Name = ? ;";
//        }
////        else if(dataType.equals("partitionTimeMargin,PTM_MaxTimeRemaining")) {
////            query = "SELECT b.PTM_Time, b.PTM_MaxTimeRemaining FROM PM_AcquisitionJobs a join PM_PartitionTimeRemaining b ON a.AJ_ID = b.AJ_ID WHERE unix_timestamp(AJ_StartTime) >= unix_timestamp(?) AND unix_timestamp(AJ_EndTime) <= unix_timestamp(?) ;";
////        }else if(dataType.equals("partitionTimeMargin,PTM_MinTimeRemaining")) {
////            query = "SELECT b.PTM_Time, b.PTM_MinTimeRemaining FROM PM_AcquisitionJobs a join PM_PartitionTimeRemaining b ON a.AJ_ID = b.AJ_ID WHERE unix_timestamp(AJ_StartTime) >= unix_timestamp(?) AND unix_timestamp(AJ_EndTime) <= unix_timestamp(?) ;";
////        }else if(dataType.equals("partitionTimeMargin,PTM_AvgTimeRemaining")) {
////            query = "SELECT b.PTM_Time b.PTM_AvgTimeRemaining FROM PM_AcquisitionJobs a join PM_PartitionTimeRemaining b ON a.AJ_ID = b.AJ_ID WHERE unix_timestamp(AJ_StartTime) >= unix_timestamp(?) AND unix_timestamp(AJ_EndTime) <= unix_timestamp(?) ;";
////        }
//
//
//
//        /*memoryUsage*/
//        else if(dataType.equals("memoryUsage")) {
//            query = "SELECT b.MZ_time, b.MZ_TextActualSize, b.MZ_TextAssignSize, b.MZ_TextPercentag, b.MZ_RodataActualSize, b.MZ_RodataAssignSize, b.MZ_RodataPercentag, b.MZ_DataActualSize, b.MZ_DataAssignSize, b.MZ_DataPercentag, b.MZ_BssActualSize, b.MZ_BssAssignSize, b.MZ_BssPercentag FROM PM_AcquisitionJobs a join PM_MirrorSize b ON a.AJ_Num = b.AJ_ID WHERE unix_timestamp(AJ_StartTime) >= unix_timestamp(?) AND unix_timestamp(AJ_EndTime) <= unix_timestamp(?) ;";
//        }
//
//
//        /*taskRunningTime*/
//        else if(dataType.equals("taskRunningTime,TR_TaskNum")) {
//            query = "SELECT b.TR_Time, b.TR_TaskNum FROM PM_AcquisitionJobs a join PM_TasksRuntime b join PM_TargetMachines c ON a.AJ_Num = b.AJ_ID AND a.TM_Name = c.TM_Name WHERE unix_timestamp(AJ_StartTime) >= unix_timestamp(?) AND unix_timestamp(AJ_EndTime) <= unix_timestamp(?) AND c.TM_Name = ?  ;";
//        }else if(dataType.equals("taskRunningTime,TR_TaskNID")) {
//            query = "SELECT b.TR_Time, b.TR_TaskNID FROM PM_AcquisitionJobs a join PM_TasksRuntime b join PM_TargetMachines c ON a.AJ_Num = b.AJ_ID AND a.TM_Name = c.TM_Name WHERE unix_timestamp(AJ_StartTime) >= unix_timestamp(?) AND unix_timestamp(AJ_EndTime) <= unix_timestamp(?) AND c.TM_Name = ?  ;";
//        }
////        else if(dataType.equals("taskRunningTime,TR_TaskNRuntime")) {
////            query = "SELECT b.TR_Time, b.TR_TaskNRuntime FROM PM_AcquisitionJobs a join PM_TasksRuntime b join PM_TargetMachines c ON a.AJ_ID = b.AJ_ID AND a.TM_Name = c.TM_Name WHERE unix_timestamp(AJ_StartTime) >= unix_timestamp(?) AND unix_timestamp(AJ_EndTime) <= unix_timestamp(?) AND c.TM_Name = ?  ;";
////        }
//        else if(dataType.equals("taskRunningTime,TR_TaskPercentage")) {
//            query = "SELECT b.TR_Time, b.TR_TaskPercentage FROM PM_AcquisitionJobs a join PM_TasksRuntime b join PM_TargetMachines c ON a.AJ_Num = b.AJ_ID AND a.TM_Name = c.TM_Name WHERE unix_timestamp(AJ_StartTime) >= unix_timestamp(?) AND unix_timestamp(AJ_EndTime) <= unix_timestamp(?) AND c.TM_Name = ?  ;";
//        }
//
//
//        /*specificOperation*/
//        else if (dataType.equals("specificOperation")){
//            //sql查询语句，查询特定时间段的 特定操作统计， 时间放在第一位！（要求）
//            query = "SELECT b.SO_Time, b.SO_NUM FROM PM_AcquisitionJobs a join PM_SpecificOperation b join PM_TargetMachines c ON a.AJ_Num = b.AJ_ID AND a.TM_Name = c.TM_Name WHERE unix_timestamp(AJ_StartTime) >= unix_timestamp(?) AND unix_timestamp(AJ_EndTime) <= unix_timestamp(?) AND c.TM_Name = ? ;";
//
//        }
//
//        //imageSizeAnalysis
//        else if(dataType.equals("imageSizeAnalysis,MZ_TextActualSize")) {
//            query = "SELECT b.MZ_Time, b.MZ_TextActualSize FROM PM_AcquisitionJobs a join PM_MirrorSize b join PM_TargetMachines c ON a.AJ_Num = b.AJ_ID AND a.TM_Name = c.TM_Name WHERE unix_timestamp(AJ_StartTime) >= unix_timestamp(?) AND unix_timestamp(AJ_EndTime) <= unix_timestamp(?) AND c.TM_Name = ? ;";
//
//        }else if(dataType.equals("imageSizeAnalysis,MZ_TextAssignSize")) {
//            query = "SELECT b.MZ_Time, b.MZ_TextAssignSize FROM PM_AcquisitionJobs a join PM_MirrorSize b join PM_TargetMachines c ON a.AJ_Num = b.AJ_ID AND a.TM_Name = c.TM_Name WHERE unix_timestamp(AJ_StartTime) >= unix_timestamp(?) AND unix_timestamp(AJ_EndTime) <= unix_timestamp(?) AND c.TM_Name = ? ;";
//
//        }else if(dataType.equals("imageSizeAnalysis,MZ_TextPercentage")) {
//            query = "SELECT b.MZ_Time, b.MZ_TextPercentag FROM PM_AcquisitionJobs a join PM_MirrorSize b join PM_TargetMachines c ON a.AJ_Num = b.AJ_ID AND a.TM_Name = c.TM_Name WHERE unix_timestamp(AJ_StartTime) >= unix_timestamp(?) AND unix_timestamp(AJ_EndTime) <= unix_timestamp(?) AND c.TM_Name = ? ;";
//
//        }else if(dataType.equals("imageSizeAnalysis,MZ_RodataActualSize")) {
//            query = "SELECT b.MZ_Time, b.MZ_RodataActualSize FROM PM_AcquisitionJobs a join PM_MirrorSize b join PM_TargetMachines c ON a.AJ_Num = b.AJ_ID AND a.TM_Name = c.TM_Name WHERE unix_timestamp(AJ_StartTime) >= unix_timestamp(?) AND unix_timestamp(AJ_EndTime) <= unix_timestamp(?) AND c.TM_Name = ? ;";
//
//        }else if(dataType.equals("imageSizeAnalysis,MZ_RodataAssignSize")) {
//            query = "SELECT b.MZ_Time, b.MZ_RodataAssignSize FROM PM_AcquisitionJobs a join PM_MirrorSize b join PM_TargetMachines c ON a.AJ_Num = b.AJ_ID AND a.TM_Name = c.TM_Name WHERE unix_timestamp(AJ_StartTime) >= unix_timestamp(?) AND unix_timestamp(AJ_EndTime) <= unix_timestamp(?) AND c.TM_Name = ? ;";
//
//        }else if(dataType.equals("imageSizeAnalysis,MZ_RodataPercentage")) {
//            query = "SELECT b.MZ_Time, b.MZ_RodataPercentag FROM PM_AcquisitionJobs a join PM_MirrorSize b join PM_TargetMachines c ON a.AJ_Num = b.AJ_ID AND a.TM_Name = c.TM_Name WHERE unix_timestamp(AJ_StartTime) >= unix_timestamp(?) AND unix_timestamp(AJ_EndTime) <= unix_timestamp(?) AND c.TM_Name = ? ;";
//
//        }else if(dataType.equals("imageSizeAnalysis,MZ_DataActualSize")) {
//            query = "SELECT b.MZ_Time, b.MZ_DataActualSize FROM PM_AcquisitionJobs a join PM_MirrorSize b join PM_TargetMachines c ON a.AJ_Num = b.AJ_ID AND a.TM_Name = c.TM_Name WHERE unix_timestamp(AJ_StartTime) >= unix_timestamp(?) AND unix_timestamp(AJ_EndTime) <= unix_timestamp(?) AND c.TM_Name = ? ;";
//
//        }else if(dataType.equals("imageSizeAnalysis,MZ_DataAssignSize")) {
//            query = "SELECT b.MZ_Time, b.MZ_DataAssignSize FROM PM_AcquisitionJobs a join PM_MirrorSize b join PM_TargetMachines c ON a.AJ_Num = b.AJ_ID AND a.TM_Name = c.TM_Name WHERE unix_timestamp(AJ_StartTime) >= unix_timestamp(?) AND unix_timestamp(AJ_EndTime) <= unix_timestamp(?) AND c.TM_Name = ? ;";
//
//        }else if(dataType.equals("imageSizeAnalysis,MZ_DataPercentage")) {
//            query = "SELECT b.MZ_Time, b.MZ_DataPercentag FROM PM_AcquisitionJobs a join PM_MirrorSize b join PM_TargetMachines c ON a.AJ_Num = b.AJ_ID AND a.TM_Name = c.TM_Name WHERE unix_timestamp(AJ_StartTime) >= unix_timestamp(?) AND unix_timestamp(AJ_EndTime) <= unix_timestamp(?) AND c.TM_Name = ? ;";
//
//        }else if(dataType.equals("imageSizeAnalysis,MZ_BssActualSize")) {
//            query = "SELECT b.MZ_Time, b.MZ_BssActualSize FROM PM_AcquisitionJobs a join PM_MirrorSize b join PM_TargetMachines c ON a.AJ_Num = b.AJ_ID AND a.TM_Name = c.TM_Name WHERE unix_timestamp(AJ_StartTime) >= unix_timestamp(?) AND unix_timestamp(AJ_EndTime) <= unix_timestamp(?) AND c.TM_Name = ? ;";
//
//        }else if(dataType.equals("imageSizeAnalysis,MZ_BssAssignSize")) {
//            query = "SELECT b.MZ_Time, b.MZ_BssAssignSize FROM PM_AcquisitionJobs a join PM_MirrorSize b join PM_TargetMachines c ON a.AJ_Num = b.AJ_ID AND a.TM_Name = c.TM_Name WHERE unix_timestamp(AJ_StartTime) >= unix_timestamp(?) AND unix_timestamp(AJ_EndTime) <= unix_timestamp(?) AND c.TM_Name = ? ;";
//
//        }else if(dataType.equals("imageSizeAnalysis,MZ_BssPercentage")) {
//            query = "SELECT b.MZ_Time, b.MZ_BssPercentag FROM PM_AcquisitionJobs a join PM_MirrorSize b join PM_TargetMachines c ON a.AJ_Num = b.AJ_ID AND a.TM_Name = c.TM_Name WHERE unix_timestamp(AJ_StartTime) >= unix_timestamp(?) AND unix_timestamp(AJ_EndTime) <= unix_timestamp(?) AND c.TM_Name = ? ;";
//        }
//
//
//        else {
//            logger.info(String.format("exception:%s", "dataType格式非法"));
//            response.error(DATATYPE_FORMAT_ERROR.getCode(), DATATYPE_FORMAT_ERROR.getMsg());
//            return;
//        }
//
//        // 查询的参数
//        JsonArray params = new JsonArray();
//        params.add(startTime_date.toString());
//        params.add(endTime_date.toString());
//        params.add(trName);
////        params.add(type);
//
////        System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
////        System.out.println(trName);
////        System.out.println(type);
//
//        //查询
//        String finalQuery = query;
//        mySQLClient.queryWithParams(finalQuery, params, ar->{
//
//                // 参数是 ar 是用来处理查询结果的 handler
//            if (ar.failed()) {
//                logger.error(String.format("query:%s exception:%s", finalQuery, Tools.getTrace(ar.cause())));
//                response.error(SERVER_ERROR.getCode(), SERVER_ERROR.getMsg());
//                return;
//            }
//
//            if (ar.result().getNumRows() == 0) {
//                //数据类型不属于要求的那些数据类型，时间段内没有数据
//                logger.info(String.format("exception:%s", "查询失败！"));
//                response.error(INPUT_QUERY_WITHOUT_RESULT.getCode(), INPUT_QUERY_WITHOUT_RESULT.getMsg());
//                return;
//            }
//
//            List<JsonArray> result = ar.result().getResults();        //获得的结果放到 Json 数组中
//
//            System.out.println("_______________________" + result + "__________________________");
//            response.success(new JsonObject().put("queryResult",result));
//            //这个拿到的结果要返回给前端
//
//        });
//
//
//
//    }
//}
