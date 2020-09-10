//package org.nit.monitorserver.handler.data;
//
//import io.vertx.core.json.JsonObject;
//import io.vertx.ext.mongo.MongoClient;
//import io.vertx.ext.web.RoutingContext;
//import org.apache.log4j.Logger;
//import org.nit.monitorserver.constant.HttpHeaderContentType;
//import org.nit.monitorserver.database.MongoConnection;
//import org.nit.monitorserver.handler.targetMachine.getData;
//import org.nit.monitorserver.message.AbstractRequestHandler;
//import org.nit.monitorserver.message.Request;
//import org.nit.monitorserver.message.ResponseFactory;
//import org.nit.monitorserver.util.FormValidator;
//import org.nit.monitorserver.util.Tools;
//
//import java.io.IOException;
//
//import static org.nit.monitorserver.constant.ResponseError.DATA_REQUIRED_ERROR;
//import static org.nit.monitorserver.constant.ResponseError.FORMAT_ERROR;
//import static org.nit.monitorserver.constant.ResponseError.PARAM_REQUIRED;
//import static org.nit.monitorserver.constant.ResponseError.RECORD_NOT_EXISTED;
//
//public class RequestDetailData extends AbstractRequestHandler {
//    protected static final Logger logger = Logger.getLogger(getData.class);
//    private final MongoClient mongoClient = new MongoConnection().getMongoClient();
//    @Override
//    public void handle(RoutingContext routingContext, Request request) throws IOException, Exception {
//        ResponseFactory response = new ResponseFactory(routingContext, request);
//        routingContext.response().putHeader(HttpHeaderContentType.CONTENT_TYPE_STR, HttpHeaderContentType.JSON);
//
//        JsonObject params = request.getParams();
//        if(params == null){
//            logger.error(String.format("exception: %s","参数为空"));
//            response.error(PARAM_REQUIRED.getCode(),PARAM_REQUIRED.getMsg());
//            return;
//        }
//
//
//        Object sidObject = request.getParamWithKey("id");//数据编号
//        Object flagObject = request.getParamWithKey("flag");//通信还是分析
//
//        if(sidObject == null){
//            logger.error(String.format("exception: %s","sid为必填参数"));
//            response.error(PARAM_REQUIRED.getCode(),PARAM_REQUIRED.getMsg());
//            return;
//        }
//        if(!FormValidator.isString(sidObject)){
//            logger.error(String.format("exception: %s","id格式非法"));
//            response.error(FORMAT_ERROR.getCode(),FORMAT_ERROR.getMsg());
//            return;
//        }
//        String sid = sidObject.toString();
//
//        if (flagObject == null){
//            logger.error(String.format("exception: %s","flag为必填参数"));
//            response.error(PARAM_REQUIRED.getCode(),PARAM_REQUIRED.getMsg());
//            return;
//        }
//
//        if(!FormValidator.isInteger(flagObject)){
//            logger.error(String.format("exception: %s","flag格式非法"));
//            response.error(FORMAT_ERROR.getCode(),FORMAT_ERROR.getMsg());
//            return;
//        }
//
//        int flag = Integer.parseInt(flagObject.toString());
//
//        JsonObject queryCondition = new JsonObject().put("sid",sid);
//
//        switch (flag){
//            case 0:
//                mongoClient.find("communication_record",queryCondition,r->{
//                    System.out.println("通信数据的数量为："+r.result().size());
//                    if(r.failed()){
//                        logger.info(String.format("query:%s, exception:%s",queryCondition, Tools.getTrace(r.cause())));
//                        response.error(DATA_REQUIRED_ERROR.getCode(), DATA_REQUIRED_ERROR.getMsg());
//                        return;
//                    }else if(r.result().size() == 0){
//
//                        response.error(RECORD_NOT_EXISTED.getCode(), RECORD_NOT_EXISTED.getMsg());
//                        return;
//                    }else{
//
//                        response.success(r.result().get(0));
//                        System.out.println(r.result().get(0));
//                    }
//                });
//                break;
//            case 1:
//                mongoClient.find("analysis_record",queryCondition,r->{
//                    System.out.println("分析数据的数量为："+r.result().size());
//                    if(r.failed()){
//                        logger.info(String.format("query:%s, exception:%s",queryCondition, Tools.getTrace(r.cause())));
//                        response.error(DATA_REQUIRED_ERROR.getCode(), DATA_REQUIRED_ERROR.getMsg());
//                        return;
//                    }else if (r.result().size() == 0){
//                        response.error(RECORD_NOT_EXISTED.getCode(), RECORD_NOT_EXISTED.getMsg());
//                        return;
//
//                    }else {
//                        response.success(r.result().get(0));
//                        System.out.println(r.result().get(0));
//                    }
//                });
//                break;
//            default:
//                break;
//        }
//
//
//
//
//
//
//
//
//    }
//}
