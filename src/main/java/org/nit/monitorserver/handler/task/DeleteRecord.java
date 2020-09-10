//package org.nit.monitorserver.handler.task;
////import com.sun.org.apache.bcel.internal.generic.NEW;
//import io.vertx.core.json.JsonArray;
//import io.vertx.core.json.JsonObject;
//        import io.vertx.ext.sql.SQLClient;
//import io.vertx.ext.web.RoutingContext;
//import org.apache.log4j.Logger;
////import org.nit.monitorserver.constant.Config;
//import org.nit.monitorserver.constant.HttpHeaderContentType;
//import org.nit.monitorserver.database.MysqlConnection;
//import org.nit.monitorserver.message.AbstractRequestHandler;
//import org.nit.monitorserver.message.Request;
//import org.nit.monitorserver.message.ResponseFactory;
//import org.nit.monitorserver.util.FormValidator;
//import org.nit.monitorserver.util.Tools;
//
//        import static org.nit.monitorserver.constant.GlobalConsts.*;
//import static org.nit.monitorserver.constant.ResponseError.*;
//
//
//public class DeleteRecord extends AbstractRequestHandler{
//    protected static final Logger logger = Logger.getLogger(DeleteRecord.class);
//    private final SQLClient sqlClient = new MysqlConnection().getMySQLClient();
//    @Override
//    public void handle(RoutingContext routingContext, Request request) {
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
////        数据库更新记录，先确定是否存在，在进行删除
//        String queryRecord="SELECT * FROM PM_AcquisitionJobs WHERE AJ_Num=? AND AJ_Flag=?";
//         sqlClient.queryWithParams(queryRecord,params,res->{
//           if(res.failed()){
//             logger.error(String .format("query:%s exception:%s",queryRecord,Tools.getTrace(res.cause())));
//             response.error(SERVER_ERROR.getCode(),SERVER_ERROR.getMsg());
//             return;
//            }
//           if(res.result().getNumRows() == 0){
//             logger.error(String.format("exception:%s","该记录不存在"));
//             response.error(RECORD_NOT_EXISTED.getCode(),RECORD_NOT_EXISTED.getMsg());
//             return;
//           }
////           更新数据库记录
//           String updateRecord="UPDATE PM_AcquisitionJobs SET AJ_Flag=0 WHERE AJ_Num=? AND AJ_Flag=?";
//           sqlClient.updateWithParams(updateRecord,params,res2->{
//             if(res2.failed()){
//                 logger.error(String.format(Tools.getTrace(res2.cause())));
//                 response.error(SERVER_ERROR.getCode(), SERVER_ERROR.getMsg());
//                 return;
//             }
//             logger.info(String.format("删除记录%s成功", res2.result()));
//             JsonObject result = new JsonObject();
//             response.success(result);
//           });
//         });
//    }
//}
//
