//package org.nit.monitorserver.handler.task;
//import java.io.IOException;
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
//import static org.nit.monitorserver.constant.GlobalConsts.*;
//import static org.nit.monitorserver.constant.ResponseError.FORMAT_ERROR;
//import static org.nit.monitorserver.constant.ResponseError.RECORD_NOT_EXISTED;
//import static org.nit.monitorserver.constant.ResponseError.SERVER_ERROR;
//public class CheckoutRecord extends AbstractRequestHandler {
//    protected static final Logger logger = Logger.getLogger(DeleteRecord.class);
//    private final SQLClient sqlClient = new MysqlConnection().getMySQLClient();
//    @Override
//    public void handle(RoutingContext routingContext, Request request) throws IOException {
//        routingContext.response().putHeader(HttpHeaderContentType.CONTENT_TYPE_STR, HttpHeaderContentType.JSON);
//        ResponseFactory response = new ResponseFactory(routingContext, request);//根据路由上下文以及请求产生响应
//        //获取参数
//        Object taskIndexObj = request.getParamWithKey(TASK_INDEX);
//
////        验证参数格式化
//        JsonArray params = new JsonArray();
//        if (taskIndexObj != null) {
//            if (!FormValidator.isInteger(taskIndexObj)) {
//                logger.info(String.format("exception:%s", "任务索引存在格式非法的参数"));
//                response.error(FORMAT_ERROR.getCode(), FORMAT_ERROR.getMsg());
//                return;
//            }
//            Integer taskIndex = (int)taskIndexObj;
//            params.add(taskIndex).add(1);
//        }
//
////        数据库查询选择
//        String queryRecord="SELECT AJ_Num,TM_Name,AJ_StartTime,AJ_EndTime,AJ_Type FROM PM_AcquisitionJobs WHERE AJ_Num=? AND AJ_Flag=?";
//        sqlClient.queryWithParams(queryRecord,params,res->{
//            if(res.failed()){
//                logger.error(String .format("query:%s exception:%s",queryRecord,Tools.getTrace(res.cause())));
//                response.error(SERVER_ERROR.getCode(),SERVER_ERROR.getMsg());
//                return;
//            }
//            if(res.result().getNumRows() == 0){
//                logger.error(String.format("exception:%s","该记录不存在"));
//                response.error(RECORD_NOT_EXISTED.getCode(),RECORD_NOT_EXISTED.getMsg());
//                return;
//            }
//
//            JsonObject result = new JsonObject();
//
//            result.put("dataList", res.result().getResults());
//            response.success(result);
//        });
//    }
//}
