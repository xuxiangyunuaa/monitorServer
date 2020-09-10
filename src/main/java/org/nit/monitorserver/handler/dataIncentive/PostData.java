//package org.nit.monitorserver.handler.dataIncentive;
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
//import org.nit.monitorserver.util.FormValidator;
//import org.nit.monitorserver.util.Tools;
//
//import java.io.IOException;
//
//import static org.nit.monitorserver.constant.ResponseError.FORMAT_ERROR;
//import static org.nit.monitorserver.constant.ResponseError.PARAM_REQUIRED;
//import static org.nit.monitorserver.constant.ResponseError.SERVER_ERROR;
///**
// * 功能描述: <合并到创建和控制功能里面>
// * 〈〉
// * @Author: 20643
// * @Date: 2020-9-1 15:02
// */
//public class PostData extends AbstractRequestHandler {
//    protected static final Logger logger = Logger.getLogger(PostData.class);
//    private final MongoClient mongoClient = new MongoConnection().getMongoClient();
//    @Override
//    public void handle(RoutingContext routingContext, Request request) throws IOException, Exception {
//        routingContext.response().putHeader(HttpHeaderContentType.CONTENT_TYPE_STR, HttpHeaderContentType.JSON);
//        ResponseFactory response = new ResponseFactory(routingContext, request);//根据路由上下文以及请求产生响应
//
//        Object portNameObject = request.getParamWithKey("portName");
//        if(portNameObject == null){
//            logger.error(String.format("exception: %s","portName为必填参数"));
//            response.error(PARAM_REQUIRED.getCode(),PARAM_REQUIRED.getMsg());
//            return;
//        }
//        if(!FormValidator.isString(portNameObject)){
//            logger.error(String.format("exception: %s","portName格式非法"));
//            response.error(FORMAT_ERROR.getCode(),FORMAT_ERROR.getMsg());
//            return;
//        }
//        String portName = portNameObject.toString();
//
//        Object dataObject = request.getParamWithKey("data");
//        if(dataObject == null){
//            logger.error(String.format("exception: %s","data为必填参数"));
//            response.error(PARAM_REQUIRED.getCode(),PARAM_REQUIRED.getMsg());
//            return;
//        }
//
//
//
//        Object targetIPObject = request.getParamWithKey("targetIP");
//        if(targetIPObject == null){
//            logger.error(String.format("exception: %s","targetIP为必填参数"));
//            response.error(PARAM_REQUIRED.getCode(),PARAM_REQUIRED.getMsg());
//            return;
//        }
//        if(!FormValidator.isString(targetIPObject)){
//            logger.error(String.format("exception: %s","targetIP格式非法"));
//            response.error(FORMAT_ERROR.getCode(),FORMAT_ERROR.getMsg());
//            return;
//        }
//        String targetIP = targetIPObject.toString();
//
//
//
//        mongoClient.find("portNamePortID",new JsonObject(),r->{
//            if(r.failed()){
//                logger.info(String.format("query exception:%s", Tools.getTrace(r.cause())));
//                response.error(SERVER_ERROR.getCode(), SERVER_ERROR.getMsg());
//                return;
//            }else {
//                int portID = 0;
//                JsonArray ICDArray = r.result().get(0).getJsonArray("ICDArray");
//                for(int i = 0; i <ICDArray.size(); i++){
//                    JsonObject elememt = ICDArray.getJsonObject(i);
//                    if(elememt.getString("portName").equals(portName)){
//                        portID = elememt.getInteger("portID");
//                        break;
//                    }else {
//                        continue;
//                    }
//                }
//                final int portIDFinal = portID;
//                mongoClient.removeDocuments("targetIPPortID",new JsonObject(),ra->{
//                    if(ra.failed()){
//                        logger.info(String.format("delete %s, exception:%s","targetIP+portID",Tools.getTrace(ra.cause())));
//                        response.error(SERVER_ERROR.getCode(), SERVER_ERROR.getMsg());
//                        return;
//                    }else {
//
//                        mongoClient.insert("targetIPPortID",new JsonObject().put("targetIP",targetIP).put("portID",portIDFinal).put("data",request.getParams().getValue("data")),re->{
//                            if(re.failed()){
//                                logger.info(String.format("insert %s, exception:%s","targetIP+portID",Tools.getTrace(re.cause())));
//                                response.error(SERVER_ERROR.getCode(), SERVER_ERROR.getMsg());
//                                return;
//                            }else {
//                                response.success(new JsonObject());
//                            }
//
//                        });
//                    }
//                });
//
//
//            }
//        });
//
//
//    }
//}
