//package org.nit.monitorserver.handler.data;
//
//import io.vertx.core.json.JsonObject;
//import io.vertx.ext.mongo.FindOptions;
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
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.nit.monitorserver.constant.ResponseError.*;
//
//public class QueryData extends AbstractRequestHandler {
//
//    protected static final Logger logger = Logger.getLogger(getData.class);
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
//
////        Object id = request.getParamWithKey("id");//数据编号
//        String aim = params.getString("aim");//目标机
//        String type = params.getString("type");//数据类型
//        String startTime = params.getString("startTime");//开始时间
//        String endTime = params.getString("endTime");//结束时间
//        int flag = params.getInteger("flag");//通信还是分析
//        int pageNum = params.getInteger("pageNum");//页码
//        int pageSize = params.getInteger("pageSize");//页面大小
//
//        if(pageNum < 0){
//            pageNum = 1;
//        }
//        final int pageNumFinal = pageNum;
//
//        if(pageSize < 0){
//            pageSize = 10;
//        }
//        final int pageSizeFinal = pageSize;
//
//        JsonObject queryCondition = new JsonObject();
//        if(startTime == null || startTime.equals("")){
//            startTime = "1949-10-1 00:00:00";
//        }
//        if(endTime == null || endTime.equals("")){
//            endTime = "2500-10-1 23:59:59";
//        }
//        queryCondition.put("timeStamp",new JsonObject().put("$gte",startTime).put("$lte",endTime));
//        System.out.println(queryCondition);
//
//
//        if(!FormValidator.isValidYYYYMMDDHHmmSSString(startTime)){
//            logger.info(String.format("exception:%s", "开始时间格式非法"));
//            response.error(START_TIME_H_FORMAT_ERROR.getCode(), START_TIME_H_FORMAT_ERROR.getMsg());
//            return;
//        }
//        if(!FormValidator.isValidYYYYMMDDHHmmSSString(endTime)){
//            logger.info(String.format("exception:%s", "开始时间格式非法"));
//            response.error(END_TIME_H_FORMAT_ERROR.getCode(), END_TIME_H_FORMAT_ERROR.getMsg());
//            return;
//        }
//
//        if(flag != 0 && flag != 1){
//            logger.info(String.format("exception:%s", "flag格式非法"));
//            response.error(FORMAT_ERROR.getCode(), FORMAT_ERROR.getMsg()+": flag");
//            return;
//        }
//
//
////
////        if(id != null){
////            queryCondition.put("_id",id);
////        }
//        if(!aim.equals("") && aim != null){
//            queryCondition.put("targetIP",aim);
//        }
//        if(!type.equals("") && type != null){
//            switch (type){
//                case "1":
//                    type = "特定空间操作对内存资源的影响";
//                    break;
//                case "2":
//                    type= "任务及各种空间操作对内存资源的影响";
//                    break;
//                case "3":
//                    type = "特定函数的执行用时统计";
//                    break;
//                case "4":
//                    type = "分区运行分析结果";
//                    break;
//                case "5":
//                    type = "任务运行分析结果";
//                    break;
//                case "6":
//                    type = "分区的各段的大小分析结果";
//                    break;
//                default:
//                    break;
//            }
//            queryCondition.put("type",type);
//        }
//
//
//
//
//
//        System.out.println(queryCondition);
//
//        FindOptions findOptions = new FindOptions();
//        findOptions.setSkip((pageNum-1)*pageSize).setLimit(pageSize);
//        JsonObject result = new JsonObject();
//        int startIndex = (pageNum-1)*pageSize;
//        int endIndex= startIndex + pageSize;
//        List<JsonObject> dataList = new ArrayList<>();
//
//
//        switch (flag){
//            case 0:
//                System.out.println("开始查询数据库通信");
//                mongoClient.find("communication_record",queryCondition,r->{
//                    if(r.failed()){
//                        logger.info(String.format("query:%s, exception:%s",queryCondition, Tools.getTrace(r.cause())));
//                        response.error(DATA_REQUIRED_ERROR.getCode(), DATA_REQUIRED_ERROR.getMsg());
//                        return;
//                    }else {
//                        int sumDocument = r.result().size();
//
//                        if(sumDocument >= endIndex){
//                           for(int i = startIndex; i < endIndex; i ++){
//                               dataList.add(r.result().get(i));
//
//                           }
//
//                        }else if(sumDocument > startIndex && sumDocument < endIndex){
//                            for(int i = startIndex; i < sumDocument; i ++){
//                                dataList.add(r.result().get(i));
//                            }
//                        }
//                        result.put("dataList",dataList);
//
//                        result.put("sumDocument",sumDocument);
//                        response.success(result);
////                        mongoClient.findWithOptions("communication_record",queryCondition,findOptions,res->{
////                            if(res.failed()){
////                                logger.info(String.format("query:%s, exception:%s",queryCondition, Tools.getTrace(r.cause())));
////                                response.error(DATA_REQUIRED_ERROR.getCode(), DATA_REQUIRED_ERROR.getMsg());
////                                return;
////                            }
////                            result.put("documentList",res.result());
////                            response.success(result);
////                            System.out.println("结果为："+result);
////
////                        });
//                    }
//                });
//                break;
//            case 1:
//                System.out.println("开始查询数据库分析");
//                mongoClient.find("analysis_record",queryCondition,r->{
//                    if(r.failed()){
//                        logger.info(String.format("query:%s, exception:%s",queryCondition, Tools.getTrace(r.cause())));
//                        response.error(DATA_REQUIRED_ERROR.getCode(), DATA_REQUIRED_ERROR.getMsg());
//                        return;
//                    }else {
//                        int sumDocument = r.result().size();
//                        System.out.println(r.result());
//
//
//                        result.put("sumDocument",sumDocument);
//                        if(sumDocument >= endIndex){
//                            for(int i = startIndex; i < endIndex-1; i ++){
//                                dataList.add(r.result().get(i));
//
//                            }
//
//                        }else if(sumDocument > startIndex && sumDocument < endIndex){
//                            for(int i = startIndex; i < sumDocument; i ++){
//                                dataList.add(r.result().get(i));
//                            }
//                        }
//                        result.put("dataList",dataList);
//
//                        result.put("sumDocument",sumDocument);
//                        response.success(result);
//                        System.out.println(result);
////                        mongoClient.findWithOptions("communication_record",queryCondition,findOptions,res->{
////
////                            if(res.failed()){
////                                logger.info(String.format("query:%s, exception:%s",queryCondition, Tools.getTrace(r.cause())));
////                                response.error(DATA_REQUIRED_ERROR.getCode(), DATA_REQUIRED_ERROR.getMsg());
////                                return;
////                            }
////                            System.out.println("分析查询成功，长度为：" + res.result().size());
////                            result.put("documentList",res.result());
////                            response.success(result);
////                            System.out.println("结果为："+result);
////
////                        });
//                    }
//                });
//                break;
//            default:
//                break;
//
//        }
//
//
//
////        if(idObject == null){
////
////        }else if(!FormValidator.isString(targetIPObject)){
////            logger.error(String.format("exception: %s","目标机名称格式错误"));
////            response.error(FORMAT_ERROR.getCode(),FORMAT_ERROR.getMsg());
////            return;
////        }
////        String targetIP = targetIPObject.toString();
////
////        JsonObject params = request.getParams();
////        int pagenum = params.getInteger("pageNum");//页码
////        int pagesize = params.getInteger("pageSize");//页面大小
////        JsonObject search = params.getJsonObject("search");
////
////        String aim = search.getString("aim");
////        if(aim.equals("") || aim.equals("全部")) aim = "";
////        else aim = " AND TM_Name = \"" + search.getString("aim") + "\"";
////
////        String type = search.getString("type");
////        if(type.equals("") || type.equals("全部")) type = "";
////        else type = " AND AJ_Type = \"" + search.getString("type") + "\"";
////
////        String id = search.getString("id");
////        if(id.equals("")) id = "";
////        else id = " AND AJ_Num = " + search.getString("id");
////
////        String sql_body = "SELECT * FROM PM_CommunicationRecord";
////        String sql_limit = " LIMIT " + (pagenum - 1) * pagesize + ',' + pagesize;
////        String sql_where = (aim + type + id).equals("") ? "" : " WHERE AJ_Num > 0" + aim + type + id;
////        String sql = sql_body + sql_where + sql_limit;
////
////        mySQLClient.query(sql, ar -> {
////            if (ar.failed()) {
////                response.error(DATA_REQUIRED_ERROR.getCode(), DATA_REQUIRED_ERROR.getMsg());
////                return;
////            }
////            logger.info("");
////            JsonArray resultData = new JsonArray();
////            List<JsonArray> dataList = ar.result().getResults();
////            for (int i = 0; i < dataList.size(); i++) {
////                JsonArray jsonArray = dataList.get(i);
////                resultData.add(new JsonObject().put("id", jsonArray.getValue(0))
////                        .put("aim", jsonArray.getValue(1))
////                        .put("time",jsonArray.getValue(2))
////                        .put("end_time",jsonArray.getValue(3))
////                        .put("type", jsonArray.getValue(4)));
////            }
////            JsonObject result = new JsonObject();
////            result.put("dataList", resultData);
////            response.success(result);
////
////        });
//
//
//    }
//
//}
