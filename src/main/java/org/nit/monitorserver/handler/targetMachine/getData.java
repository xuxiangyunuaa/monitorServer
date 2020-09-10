//package org.nit.monitorserver.handler.targetMachine;
//import static org.nit.monitorserver.constant.TargetManageConsts.*;
//
//import io.vertx.core.json.JsonArray;
//import io.vertx.core.json.JsonObject;
//import io.vertx.ext.mongo.MongoClient;
//import io.vertx.ext.web.RoutingContext;
//import org.apache.log4j.Logger;
//import org.nit.monitorserver.constant.HttpHeaderContentType;
//import org.nit.monitorserver.database.MongoConnection;
//import org.nit.monitorserver.message.AbstractRequestHandler;
//import org.nit.monitorserver.message.Request;
//import org.nit.monitorserver.message.ResponseFactory;
//import org.nit.monitorserver.util.Tools;
//
//import java.util.List;
//import static org.nit.monitorserver.constant.ResponseError.*;
//
///**
// * @author gsjt
// * @date 2020/2/5
// */
///**
// * 功能描述: <和search合并，之后可以删除>
// * 〈〉
// * @Author: 20643
// * @Date: 2020-9-1 14:23
// */
//public class getData extends AbstractRequestHandler {
//
//    protected static final Logger logger = Logger.getLogger(getData.class);
////    private final SQLClient mySQLClient = new MysqlConnection().getMySQLClient();
//    private final MongoClient mongoClient = new MongoConnection().getMongoClient();
//
//
//    @Override
//    public void handle(final RoutingContext routingContext, final Request request) {
//
//        //System.out.println("1111111111111111");
//        routingContext.response().putHeader(HttpHeaderContentType.CONTENT_TYPE_STR, HttpHeaderContentType.JSON);
//        ResponseFactory response = new ResponseFactory(routingContext, request);
//
//        // Object userIdObj = request.getParamWithKey(USER_ID_STR);
//
//        mongoClient.find("targetMachine",new JsonObject(),r->{
//            if(r.failed()){
//                logger.error(String.format("find target exception: %s", Tools.getTrace(r.cause())));
//                response.error(DATA_QUERY_ERROR.getCode(), DATA_QUERY_ERROR.getMsg());
//                return;
//            }else {
//                JsonArray resultData = new JsonArray();
//                List<JsonObject> dataList = r.result();
//                for (int i = 0; i < dataList.size(); i++) {
//                    JsonObject jsonArray = dataList.get(i);
//                    //System.out.println(jsonArray);
//                    resultData.add(new JsonObject().put(TARGET_NAME, jsonArray.getValue("name"))
//                            .put(TARGET_IP, jsonArray.getValue("ip"))
//                            .put(TARGET_OS, jsonArray.getValue("os")));
//                }
//
//                JsonObject result = new JsonObject();
//                result.put("machineList", resultData);
//                response.success(result);
//
//            }
//        });
//
////        String sql = "SELECT * FROM " + TABLE_NAME + ";";
////
////        mySQLClient.query(sql, ar -> {
////            if (ar.failed()) {
////                //System.out.println(ar.result());
////                //System.out.println(ar.cause());
////                //logger.error("222222222222222222");
////                response.error(DATA_REQUIRED_ERROR.getCode(), DATA_REQUIRED_ERROR.getMsg());
////                return;
////            }
////            logger.info("");
////
////            JsonArray resultData = new JsonArray();
////            List<JsonArray> dataList = ar.result().getResults();
////            for (int i = 0; i < dataList.size(); i++) {
////                JsonArray jsonArray = dataList.get(i);
////                //System.out.println(jsonArray);
////                resultData.add(new JsonObject().put(TARGET_NAME, jsonArray.getValue(0))
////                        .put(TARGET_IP, jsonArray.getValue(1))
////                        .put(TARGET_OS, jsonArray.getValue(2)));
////            }
////
////            JsonObject result = new JsonObject();
////            result.put("machineList", resultData);
////            response.success(result);
////
////        });
//
//
//    }
//
//}
