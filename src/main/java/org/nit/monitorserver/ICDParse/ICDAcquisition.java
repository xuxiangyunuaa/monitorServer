package org.nit.monitorserver.ICDParse;


import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.RoutingContext;
import org.apache.log4j.Logger;
import org.nit.monitorserver.constant.HttpHeaderContentType;
import org.nit.monitorserver.constant.ResponseError;
import org.nit.monitorserver.database.MongoConnection;
import org.nit.monitorserver.message.AbstractRequestHandler;
import org.nit.monitorserver.message.Request;
import org.nit.monitorserver.message.ResponseFactory;
import org.nit.monitorserver.util.FormValidator;
import org.nit.monitorserver.util.Tools;

import java.io.IOException;

public class ICDAcquisition extends AbstractRequestHandler {
    protected static final Logger logger = Logger.getLogger(ICDAcquisition.class);
//    public static JsonObject ICD = null;//引用
    private final MongoClient mongoClient = new MongoConnection().getMongoClient();


    @Override
    public void handle(RoutingContext routingContext, Request request) throws IOException, Exception {
        routingContext.response().putHeader(HttpHeaderContentType.CONTENT_TYPE_STR, HttpHeaderContentType.JSON);
        ResponseFactory response = new ResponseFactory(routingContext, request);
        //参数判空
        Object ICDObject = request.getParamWithKey("ICD");
        if(ICDObject == null){
            response.error(ResponseError.PARAM_REQUIRED.getCode(),ResponseError.PARAM_REQUIRED.getMsg());
            logger.error(String.format("exception:%s","ICD为非法参数"));
            return;
        }
        //参数是否为jsonObject
        if(!FormValidator.isJsonObject(ICDObject)){
            response.error(ResponseError.FORMAT_ERROR.getCode(),ResponseError.FORMAT_ERROR.getMsg());
            logger.error(String.format("exception:%s","ICD格式错误"));
            return;
        }
        JsonObject ICD = new JsonObject().put("ICD",request.getParamWithKey("ICD"));


        Object ICDArrayObject = request.getParamWithKey("ICDArray");
        System.out.println("ICDArray:"+ICDArrayObject);
        if(ICDArrayObject == null){
            response.error(ResponseError.PARAM_REQUIRED.getCode(),ResponseError.PARAM_REQUIRED.getMsg());
            logger.error(String.format("exception:%s","ICDArray为非法参数"));
            return;
        }
        if(!FormValidator.isJsonArray(ICDArrayObject)){
            response.error(ResponseError.FORMAT_ERROR.getCode(),ResponseError.FORMAT_ERROR.getMsg());
            logger.error(String.format("exception:%s","ICDArray格式错误"));
            return;
        }

        JsonArray ICDArray = (JsonArray) request.getParamWithKey("ICDArray");
        JsonObject ICDArrayJsonObject = new JsonObject().put("ICDArray",ICDArray);
        Future future = Future.future();



        mongoClient.removeDocuments("ICD",new JsonObject(),res->{//删除icd
            if(res.failed()){
                response.error(ResponseError.DELETE_FAILURE.getCode(),ResponseError.DELETE_FAILURE.getMsg());
                logger.error(String.format("ICD query and delete exception: %s",Tools.getTrace(res.cause())));
                return;
            }else {
                System.out.println("删除ICD");
                mongoClient.insert("ICD",ICD,r->{//新增ICD
                    if(r.failed()){
                        response.error(ResponseError.INSERT_FAILURE.getCode(),ResponseError.INSERT_FAILURE.getMsg());
                        logger.error(String.format("%s insert exception: %s",ICD,Tools.getTrace(r.cause())));
                        return;
                    }else {
                        System.out.println("插入ICD成功");
                        future.complete();

                    }

                });
            }
        });
        future.setHandler(r->{
            mongoClient.removeDocuments("portNamePortID",new JsonObject(),rb->{//删除所有的portName+portID
                if(rb.failed()){
                    response.error(ResponseError.DELETE_FAILURE.getCode(),ResponseError.DELETE_FAILURE.getMsg());
                    logger.error(String.format("ICD query and delete exception: %s",Tools.getTrace(rb.cause())));
                    return;
                }else {
                    System.out.println("删除portName和portID成功");
                    mongoClient.insert("portNamePortID",ICDArrayJsonObject,rt->{//插入portName与portID
                        if(rt.failed()) {
                            response.error(ResponseError.INSERT_FAILURE.getCode(), ResponseError.INSERT_FAILURE.getMsg());
                            logger.error(String.format("%s insert exception: %s", ICDArrayJsonObject, Tools.getTrace(rt.cause())));
                            return;
                        }else {
                            System.out.println("插入portName和portID成功");
                            response.success(new JsonObject().put("responseCode",200));
                        }
                    });
                }
            });
        });





        JsonObject result = new JsonObject().put("code",200);
        response.success(result);



//        ICDParse icdParse = new ICDParse();
//        Map<String,Object> map = icdParse.jsonToMap(params);
//
//        //从MySql数据库中取最新的二进制文件
//        String query = "SELECT * FROM TABLE1";
//        sqlClient.query(query,r->{
//            if(r.failed()){
//                logger.error(String .format("query:%s exception:%s",query, Tools.getTrace(r.cause())));
//                response.error(SERVER_ERROR.getCode(),SERVER_ERROR.getMsg());
//                return;
//            }else if(r.result().getNumRows() == 0){
//                logger.error(String.format("exception:%s","该记录不存在"));
//                response.error(RECORD_NOT_EXISTED.getCode(),RECORD_NOT_EXISTED.getMsg());
//                return;
//            }else {
//                List<JsonArray> resultArray =  r.result().getResults();
//                JsonArray resultFirst = resultArray.get(0);
//                String portName = resultFirst.getString(0);//取到玖翼数据库中的portName
//                byte[] bytes = resultFirst.getBinary(0);//获取玖翼数据库中的二进制文件
//
//                //ICD中寻找对应的portName
//                Set<String> nameSet = map.keySet();//
//                Iterator iterator = nameSet.iterator();
//                String icdName = iterator.next().toString();//获取第一层icd的key
//                Object firstICDObject = map.get(icdName);//获取第一层的对象
//                JsonObject firstResult = new JsonObject();
//                JsonObject result = new JsonObject();
//
//                if(firstICDObject != null){
////            Map<String,Object>  secondICDMap= new org.apache.commons.beanutils.BeanMap(firstICDObject);//第二层ICD的Map
//                    if(firstICDObject instanceof Map == false){
//                        logger.error(String.format("exception:%s","请求解析错误"));
//                        response.error(DECODE_ERROR.getCode(),DECODE_ERROR.getMsg());
//                        return;
//                    }
//                    Map<String,Object>  secondICDMap = (Map<String,Object>) firstICDObject;
//                    JsonObject secondResult = new JsonObject();
//                    for(Map.Entry<String,Object> entry : secondICDMap.entrySet()){//遍历第二层ICD元素
//
//                        String secondICDKey = entry.getKey();//第二层
//                        Object secondICDObject = entry.getValue();
//
//                        if(secondICDObject instanceof Map){
//                            Map<String,Object> thirdICDMap = (Map<String,Object>)secondICDObject;//第三层ICD
//                            if(thirdICDMap.containsKey("_portName") ){
//
//                                String thirdICDPortName = thirdICDMap.get("_portName").toString();
//                                if(thirdICDPortName.equals(portName)){//寻找与二进制数据对应的portName
//                                    secondResult= icdParse.portNameParse(thirdICDMap,secondICDKey,bytes);
//                                }else {
//                                    continue;
//                                }
//                            }else {
//                                logger.error(String.format("exception:%s","请求解析错误"));
//                                response.error(DECODE_ERROR.getCode(),DECODE_ERROR.getMsg());
//                                return;
//                            }
//                        }else {
//                            continue;
//                        }
//                        firstResult.put(secondICDKey,secondResult);
//                    }
//                    result.put(icdName,firstResult);
//                }else {
//                    logger.error(String.format("exception:%s","请求解析错误"));
//                    response.error(DECODE_ERROR.getCode(),DECODE_ERROR.getMsg());
//                    return;
//                }
//                mongoClient.insert("pm",result,re->{
//                    if(re.failed()){
//                        logger.error(String .format("insert:%s exception:%s","mongo插入异常", Tools.getTrace(re.cause())));
//                        response.error(SERVER_ERROR.getCode(),SERVER_ERROR.getMsg());
//                        return;
//                    }else {
//                        response.success(result);
//                    }
//                });
//            }
//        });
    }
}
