//package org.nit.monitorserver.handler.data;
//
//import io.vertx.core.json.JsonObject;
//import io.vertx.ext.mongo.MongoClient;
//import io.vertx.ext.web.RoutingContext;
//import org.apache.log4j.Logger;
//import org.nit.monitorserver.ICDParse.ICDParse;
//import org.nit.monitorserver.constant.HttpHeaderContentType;
//import org.nit.monitorserver.database.MongoConnection;
//import org.nit.monitorserver.message.AbstractRequestHandler;
//import org.nit.monitorserver.message.Request;
//import org.nit.monitorserver.message.ResponseFactory;
//import org.nit.monitorserver.util.Tools;
//
//import static org.nit.monitorserver.constant.ResponseError.QUERY_FAILURE;
//
//import java.io.IOException;
//import java.util.*;
//
//
//public class RequestProp extends AbstractRequestHandler {
//
//    private final MongoClient mongoClient = new MongoConnection().getMongoClient();
//    protected static final Logger logger = Logger.getLogger(RequestProp.class);
//
//
//    @Override
//    public void handle(RoutingContext routingContext, Request request ) throws IOException, Exception {
//        routingContext.response().putHeader(HttpHeaderContentType.CONTENT_TYPE_STR, HttpHeaderContentType.JSON);
//        ResponseFactory response = new ResponseFactory(routingContext, request);//根据路由上下文以及请求产生响应
//        JsonObject condition = new JsonObject();
//        mongoClient.find("analysisRtn",condition,r->{
//            if(r.failed()){
//                response.error(QUERY_FAILURE.getCode(), QUERY_FAILURE.getMsg());
//                logger.error(String.format("mongodb find analysisRtn data error: %s", Tools.getTrace(r.cause())));
//                return;
//            }
//            JsonObject latestData = r.result().get(0);//取出最新一则数据
//            JsonObject result = new JsonObject();
//            List<JsonObject> keySetElement = new ArrayList<>();
//            Map<String,Object> latestDataMap = ICDParse.jsonToMap(latestData);
//            Set<String> keySet = latestDataMap.keySet();
//            Iterator iterator = keySet.iterator();
//            String EDT_DATA_ANALYSIS_RTN = iterator.next().toString();
//            Object EDT_DATA_ANALYSIS_RTN_VALUE = latestDataMap.get(EDT_DATA_ANALYSIS_RTN);
//            if(EDT_DATA_ANALYSIS_RTN_VALUE instanceof Map){
//
//                Map<String,Object> secondMap = (Map<String,Object>) EDT_DATA_ANALYSIS_RTN_VALUE;
//                for(Map.Entry<String,Object> entry:secondMap.entrySet()){
//                    JsonObject elementObject = new JsonObject();
//                    String elementKey = entry.getKey();//evt_mem_effect_rtn
//                    Object elementValue = entry.getValue();
//
//                    if(elementValue instanceof Map){
//                        Map<String,Object> thirdMap = (Map<String,Object>) elementValue;
//                        Set<String> thirdKeySet = thirdMap.keySet();
//                        elementObject.put(elementKey,thirdKeySet);
//                        keySetElement.add(elementObject);
//
//                    }
//                }
//                result.put(EDT_DATA_ANALYSIS_RTN,keySetElement);
//                response.success(result);
//            }
//
//        });
//
//
//    }
//}
