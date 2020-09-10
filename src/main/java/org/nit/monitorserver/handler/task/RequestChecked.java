//package org.nit.monitorserver.handler.task;
//
//import io.vertx.core.json.JsonObject;
//import io.vertx.ext.mongo.MongoClient;
//import io.vertx.ext.web.RoutingContext;
//import org.apache.log4j.Logger;
//import org.nit.monitorserver.constant.HttpHeaderContentType;
//import org.nit.monitorserver.database.MongoConnection;
//import org.nit.monitorserver.message.AbstractRequestHandler;
//import org.nit.monitorserver.message.Request;
//import org.nit.monitorserver.message.ResponseFactory;
//import org.nit.monitorserver.util.FormValidator;
//import org.nit.monitorserver.util.Tools;
//
//import java.io.IOException;
//import java.util.List;
//
//import static org.nit.monitorserver.constant.ResponseError.FORMAT_ERROR;
//import static org.nit.monitorserver.constant.ResponseError.PARAM_REQUIRED;
//import static org.nit.monitorserver.constant.ResponseError.QUERY_FAILURE;
//
//public class RequestChecked extends AbstractRequestHandler {
//    protected static final Logger logger = Logger.getLogger(SaveChecked.class);
//
//    private final MongoClient mongoClient = new MongoConnection().getMongoClient();
//
//    @Override
//    public void handle(RoutingContext routingContext, Request request) throws IOException, Exception {
//        routingContext.response().putHeader(HttpHeaderContentType.CONTENT_TYPE_STR, HttpHeaderContentType.JSON);
//        ResponseFactory response = new ResponseFactory(routingContext, request);//根据路由上下文以及请求产生响应
//
//        Object targetIPObject = request.getParamWithKey("ip");
//        if(targetIPObject == null){
//            logger.error(String.format("exception: %s","目标机名称为必填参数"));
//            response.error(PARAM_REQUIRED.getCode(),PARAM_REQUIRED.getMsg());
//            return;
//        }else if(!FormValidator.isString(targetIPObject)){
//            logger.error(String.format("exception: %s","目标机名称格式错误"));
//            response.error(FORMAT_ERROR.getCode(),FORMAT_ERROR.getMsg());
//            return;
//        }
//        String targetIP = targetIPObject.toString();
//        System.out.println("targetIP:"+targetIP);
//        JsonObject query = new JsonObject().put("targetIP",targetIP);
//        mongoClient.find("template",query,r->{
//            if(r.failed()){
//                logger.error(String.format("query: %s, exception: %s",query, Tools.getTrace(r.cause())));
//                response.error(QUERY_FAILURE.getCode(),QUERY_FAILURE.getMsg());
//                return;
//            }else {
//                List<JsonObject> resultObtained = r.result();
//                System.out.println("查询到的结果个数为"+r.result().size());
//                JsonObject result = new JsonObject().put("result",resultObtained);
//                System.out.println("result"+result);
//                response.success(result);
//            }
//        });
//
//
//    }
//}
