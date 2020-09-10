//package org.nit.monitorserver.handler.targetMachine;
//
//import io.vertx.core.json.JsonObject;
//import io.vertx.ext.sql.SQLClient;
//import io.vertx.ext.web.RoutingContext;
//import org.apache.log4j.Logger;
//import org.nit.monitorserver.constant.HttpHeaderContentType;
//import org.nit.monitorserver.database.MysqlConnection;
//import org.nit.monitorserver.message.AbstractRequestHandler;
//import org.nit.monitorserver.message.Request;
//import org.nit.monitorserver.message.ResponseFactory;
//import org.nit.monitorserver.util.Tools;
//
//import static org.nit.monitorserver.constant.ResponseError.*;
//import static org.nit.monitorserver.constant.TargetManageConsts.*;
//
//public class getCommandSet extends AbstractRequestHandler {
//
//    protected static final Logger logger = Logger.getLogger(getCommandSet.class);
//    private final SQLClient mySQLClient = new MysqlConnection().getMySQLClient();
//
//    @Override
//    public void handle(final RoutingContext routingContext,final Request request) {
//
//        routingContext.response().putHeader(HttpHeaderContentType.CONTENT_TYPE_STR, HttpHeaderContentType.JSON);
//        ResponseFactory response = new ResponseFactory(routingContext, request);
//
//
////        String name = request.getParams().getString(COMMAND_NAME); //获取指令集名称
//
////        // 验证非空
////        if (request.getParams().getString(COMMAND_NAME).isEmpty()) {
////            String sql = " SELECT DISTINCT name FROM " + COMMANDTABLE_NAME;
////            mySQLClient.query(sql, ar ->{
////                if(ar.failed()){
////                    logger.error(String.format("query:%s exception %s",sql, Tools.getTrace(ar.cause())));
////                    response.error(SERVER_ERROR.getCode(),SERVER_ERROR.getMsg());
////                    return;
////                }
////                if(ar.result().getNumRows()==0){
////                    logger.info(String.format(String.format("exception:%s","查询失败")));
////                    response.error(QUERY_FAILURE.getCode(),QUERY_FAILURE.getMsg());
////                    return;
////                }
////
//////            对响应的结果进行包装并返回给前端
////                JsonArray resultData = new JsonArray();
////                List<JsonArray> dataList = ar.result().getResults();
////                JsonObject result=new JsonObject();
////                for(int i=0;i<dataList.size();i++){
////                    JsonArray jsonArray=dataList.get(i);
////                    resultData.add(new JsonObject().put("COMMAND_NAME",jsonArray.getValue(0)));
////                }
////                result.put("dataList",resultData);
////                response.success(result);
////            });
////        }else{
//            String sql = " SELECT content FROM " + COMMANDTABLE_NAME;
//            mySQLClient.query(sql, ar ->{
//                if(ar.failed()){
//                    logger.error(String.format("query:%s exception %s",sql, Tools.getTrace(ar.cause())));
//                    response.error(SERVER_ERROR.getCode(),SERVER_ERROR.getMsg());
//                    return;
//                }
//                if(ar.result().getNumRows()==0){
//                    logger.info(String.format(String.format("exception:%s","查询失败")));
//                    response.error(QUERY_FAILURE.getCode(),QUERY_FAILURE.getMsg());
//                    return;
//                }
//
//
//                JsonObject result = new JsonObject();
//                JsonObject resultData=ar.result().getRows().get(0);
//                result.put("content",resultData);
//                response.success(result);
//            });
////        }
//
//    }
//}
