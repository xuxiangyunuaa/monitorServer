//package org.nit.monitorserver.handler.task;
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
//import org.nit.monitorserver.util.Tools;
//
//import java.util.List;
//
//import static org.nit.monitorserver.constant.ResponseError.QUERY_FAILURE;
//import static org.nit.monitorserver.constant.ResponseError.SERVER_ERROR;
//
//public class GetPlaneList extends AbstractRequestHandler {
//    protected static final Logger logger = Logger.getLogger(GetPlaneList.class);
//    private final SQLClient sqlClient = new MysqlConnection().getMySQLClient();
//    @Override
//    public void handle(RoutingContext routingContext, Request request)  {
//
//        routingContext.response().putHeader(HttpHeaderContentType.CONTENT_TYPE_STR, HttpHeaderContentType.JSON);
//        ResponseFactory response = new ResponseFactory(routingContext, request);
////        查询语句
//        String sql="SELECT DISTINCT TM_Name FROM PM_TargetMachines";
//        sqlClient.query(sql, ar ->{
//            if(ar.failed()){
//                logger.error(String.format("query:%s exception %s",sql, Tools.getTrace(ar.cause())));
//                response.error(SERVER_ERROR.getCode(),SERVER_ERROR.getMsg());
//                return;
//            }
//            if(ar.result().getNumRows()==0){
//                logger.info(String.format(String.format("exception:%s","查询失败")));
//                response.error(QUERY_FAILURE.getCode(),QUERY_FAILURE.getMsg());
//                return;
//            }
//
////            对响应的结果进行包装并返回给前端
//            JsonArray resultData = new JsonArray();
//            List<JsonArray> dataList = ar.result().getResults();
//            JsonObject result=new JsonObject();
//            for(int i=0;i<dataList.size();i++){
//                JsonArray jsonArray=dataList.get(i);
//                resultData.add(new JsonObject().put("PLANE_NAME",jsonArray.getValue(0)));
//            }
//            result.put("dataList",resultData);
//            response.success(result);
//        });
//    }
//}
