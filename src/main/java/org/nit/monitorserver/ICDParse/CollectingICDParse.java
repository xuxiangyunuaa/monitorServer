package org.nit.monitorserver.ICDParse;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import org.apache.log4j.Logger;
import org.nit.monitorserver.ListenAcquisition;
import org.nit.monitorserver.database.MongoConnection;
import org.nit.monitorserver.util.Tools;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class CollectingICDParse {
    private final MongoClient mongoClient = new MongoConnection().getMongoClient();
    protected static final Logger logger = Logger.getLogger(CollectingICDParse.class);

    public void collectICDParse(String portName,byte[] bytes,long timeStamp){
        Future<JsonObject> future = Future.future();
        mongoClient.find("ICD",new JsonObject(),r->{//获取最新的ICD
            if (r.failed()){
                logger.error(String.format("ICD query exception: %s", Tools.getTrace(r.cause())));
                return;
            }else {
                future.complete(r.result().get(0).getJsonObject("ICD"));
            }
        });
        future.setHandler(r->{
            ICDParse icdParse = new ICDParse();
            Map<String,Object> map = icdParse.jsonToMap(future.result());
            Set<String> nameSet = map.keySet();//
            Iterator iterator = nameSet.iterator();
            String icdName = iterator.next().toString();//获取第一层icd的key
            Object firstICDObject = map.get(icdName);//获取第一层的对象
            JsonObject firstResult = new JsonObject();
            JsonObject result = new JsonObject();

            if(firstICDObject != null){
                if(firstICDObject instanceof Map == false){
                    logger.error(String.format("exception:%s","请求解析错误"));
                    return;
                }
                Map<String,Object>  secondICDMap = (Map<String,Object>) firstICDObject;
                JsonObject secondResult = null;
                for(Map.Entry<String,Object> entry : secondICDMap.entrySet()){//遍历第二层ICD元素

                    String secondICDKey = entry.getKey();//第二层
                    Object secondICDObject = entry.getValue();

                    if(secondICDObject instanceof Map){
                        Map<String,Object> thirdICDMap = (Map<String,Object>)secondICDObject;//第三层ICD
                        if(thirdICDMap.containsKey("_portName") ){

                            String thirdICDPortName = thirdICDMap.get("_portName").toString();
                            if(thirdICDPortName.equals(portName)){//寻找与二进制数据对应的portName
                                secondResult= icdParse.portNameParse(thirdICDMap,secondICDKey,0,bytes);
                            }else {
                                continue;
                            }
                        }else {
                            logger.error(String.format("packet: %ld, exception:%s",timeStamp,"请求解析错误"));
                            return;
                        }
                    }else {
                        continue;
                    }
                    firstResult.put(secondICDKey,secondResult);
                }
                result.put(icdName,firstResult);
                mongoClient.insert("icdParsedData",result,rm->{
                    if(rm.failed()){
                        logger.error(String.format("packet: %ld, exception:%s",timeStamp,"数据持久化错误"));
                        return;

                    }else {
                        logger.info(String.format("packet: %ld, info:%s",timeStamp,"请求解析成功"));
                        return;
                    }
                });
            }else {
                logger.error(String.format("packet: %ld, exception:%s",timeStamp,"请求解析错误"));
                return;
            }
        });

    }
}
