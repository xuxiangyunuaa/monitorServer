//package org.nit.monitorserver.handler.data;
//
//import io.vertx.core.json.JsonObject;
//import io.vertx.ext.mongo.MongoClient;
//import io.vertx.ext.web.RoutingContext;
//import org.apache.log4j.Logger;
//import org.nit.monitorserver.constant.HttpHeaderContentType;
//import org.nit.monitorserver.database.MongoConnection;
//
//import org.nit.monitorserver.message.AbstractRequestHandler;
//import org.nit.monitorserver.message.Request;
//import org.nit.monitorserver.message.ResponseFactory;
//import org.nit.monitorserver.util.FormValidator;
//import org.nit.monitorserver.util.Tools;
//
//import static org.nit.monitorserver.constant.ResponseError.FORMAT_ERROR;
//import static org.nit.monitorserver.constant.ResponseError.PARAM_REQUIRED;
//import static org.nit.monitorserver.constant.ResponseError.SERVER_ERROR;
//
//public class DeleteDataPre extends AbstractRequestHandler {
//
//    protected static final Logger logger = Logger.getLogger(DeleteDataPre.class);
//    private final MongoClient mongoClient = new MongoConnection().getMongoClient();
//
//    @Override
//    public void handle(final RoutingContext routingContext, final Request request) {
//        routingContext.response().putHeader(HttpHeaderContentType.CONTENT_TYPE_STR, HttpHeaderContentType.JSON);
//        ResponseFactory response = new ResponseFactory(routingContext, request);
//
//        JsonObject params = request.getParams();
//        if(params == null){
//            logger.error(String.format("exception: %s","参数为空"));
//            response.error(PARAM_REQUIRED.getCode(),PARAM_REQUIRED.getMsg());
//            return;
//        }
//
//
//        Object sidObject = request.getParamWithKey("id");//主键
//        Object flagObject = request.getParamWithKey("flag");//通信还是分析
//
//        if(sidObject == null){
//            logger.error(String.format("exception: %s","id为必填参数"));
//            response.error(PARAM_REQUIRED.getCode(),PARAM_REQUIRED.getMsg());
//            return;
//        }
//        if(!FormValidator.isString(sidObject)){
//            logger.error(String.format("exception: %s","sid格式非法"));
//            response.error(FORMAT_ERROR.getCode(),FORMAT_ERROR.getMsg());
//            return;
//        }
//
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
//        JsonObject deleteCondition = new JsonObject().put("sid",sid);
//
//        switch (flag){
//            case 0:
//                mongoClient.removeDocuments("communication_record",deleteCondition,r->{
//                    System.out.println("开始删除数据");
//                    if(r.failed()){
//                        logger.info(String.format("delete:%s, exception:%s",deleteCondition, Tools.getTrace(r.cause())));
//                        response.error(SERVER_ERROR.getCode(), SERVER_ERROR.getMsg());
//                        return;
//                    }
//                    System.out.println("删除通信数据成功");
//                    response.success(new JsonObject());
//                });
//
//            case 1:
//                mongoClient.removeDocuments("analysis_record",deleteCondition,res->{
//                    System.out.println("开始删除数据");
//                    if(res.failed()){
//                        logger.info(String.format("delete:%s, exception:%s",deleteCondition, Tools.getTrace(res.cause())));
//                        response.error(SERVER_ERROR.getCode(), SERVER_ERROR.getMsg());
//                        return;
//                    }
//                    System.out.println("删除分析数据成功");
//                    response.success(new JsonObject());
//                });
//        }
//
//
//
//
////        String sql_body = "DELETE FROM PM_CommunicationRecord WHERE AJ_Num = " + id;
////
////        mySQLClient.UpdateProject(sql_body, ar -> {
////            if (ar.failed()) {
////                response.error(DATA_REQUIRED_ERROR.getCode(), DATA_REQUIRED_ERROR.getMsg());
////                return;
////            }
////            JsonObject result = new JsonObject();
////            response.success(result);
////
////        });
//
//
//    }
//
//}
