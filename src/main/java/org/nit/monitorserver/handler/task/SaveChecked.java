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
//import java.util.Calendar;
//
//import static org.nit.monitorserver.constant.ResponseError.FORMAT_ERROR;
//import static org.nit.monitorserver.constant.ResponseError.PARAM_REQUIRED;
//import static org.nit.monitorserver.constant.ResponseError.RECORD_EXISTED;
//import static org.nit.monitorserver.constant.ResponseError.SERVER_ERROR;
//
//public class SaveChecked extends AbstractRequestHandler {
//    protected static final Logger logger = Logger.getLogger(SaveChecked.class);
//
//    private final MongoClient mongoClient = new MongoConnection().getMongoClient();
//
//    @Override
//    public void handle(RoutingContext routingContext, Request request) throws IOException, Exception {
//        routingContext.response().putHeader(HttpHeaderContentType.CONTENT_TYPE_STR, HttpHeaderContentType.JSON);
//        ResponseFactory response = new ResponseFactory(routingContext, request);//根据路由上下文以及请求产生响应
//
//        //验证参数
//        Object nameObject = request.getParamWithKey("name");
//
//        Object targetIPObject = request.getParamWithKey("targetIP");
//
//        Object defaultCheckedObject = request.getParamWithKey("defaultChecked");
//
//        if(nameObject == null){
//           logger.error(String.format("exception: %s","模板名称为必填参数"));
//           response.error(PARAM_REQUIRED.getCode(),PARAM_REQUIRED.getMsg());
//           return;
//        }else if(!FormValidator.isString(nameObject)){
//           logger.error(String.format("exception: %s","模板名称格式错误"));
//           response.error(FORMAT_ERROR.getCode(),FORMAT_ERROR.getMsg());
//           return;
//        }
//        Calendar systemMillon = Calendar.getInstance();
//        long milionSecond = systemMillon.getTimeInMillis();
//        String name = nameObject.toString()+String.valueOf(milionSecond);
//
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
//
//        if(defaultCheckedObject == null){
//            logger.error(String.format("exception: %s","模板为必填参数"));
//            response.error(PARAM_REQUIRED.getCode(),PARAM_REQUIRED.getMsg());
//            return;
//        }else if(!FormValidator.isString(defaultCheckedObject)){
//            logger.error(String.format("exception: %s","模板格式错误"));
//            response.error(FORMAT_ERROR.getCode(),FORMAT_ERROR.getMsg());
//            return;
//        }
//        String defaultChecked= defaultCheckedObject.toString();
//
//        JsonObject template = new JsonObject().put("name",name).put("targetIP",targetIP).put("defaultChecked",defaultChecked);
//        JsonObject query = new JsonObject().put("targetIP",targetIP);
//        mongoClient.find("template",query,res->{
//            if(res.failed()){
//                logger.error(String.format("query:%s exception %s",name, Tools.getTrace(res.cause())));
//                response.error(SERVER_ERROR.getCode(), SERVER_ERROR.getMsg());
//                return;
//            }else if(res.result().size() != 0){
//                for(JsonObject element : res.result()){
//                    String existingChecked = element.getString("defaultChecked");
//                    System.out.println(element);
//                    System.out.println(existingChecked);
//                    System.out.println(defaultChecked);
//                    if(existingChecked.equals(defaultChecked)){
//                        logger.error(String.format("exception: %s","该模板已经存在"));
//                        response.error(RECORD_EXISTED.getCode(),RECORD_EXISTED.getMsg());
//                        return;
//                    }
//                }
//                mongoClient.insert("template",template,r->{
//                    System.out.println("开始插入新模板数据库");
//                    if(r.failed()){
//                        logger.error(String.format("insert:%s info %s",name, Tools.getTrace(r.cause())));
//                        response.error(SERVER_ERROR.getCode(), SERVER_ERROR.getMsg());
//                        return;
//                    }else {
//
//                        JsonObject result = new JsonObject();
//                        response.success(result);
//                    }
//                });
//            }else{
//                mongoClient.insert("template",template,r->{
//                    System.out.println("开始插入全新数据库");
//                    if(r.failed()){
//                        logger.error(String.format("insert:%s exception %s",name, Tools.getTrace(r.cause())));
//                        response.error(SERVER_ERROR.getCode(), SERVER_ERROR.getMsg());
//                        return;
//                    }else {
//
//                        JsonObject result = new JsonObject();
//                        response.success(result);
//                    }
//                });
//            }
//
//        }
//        );
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
