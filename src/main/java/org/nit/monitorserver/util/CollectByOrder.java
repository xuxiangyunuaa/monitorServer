package org.nit.monitorserver.util;

import io.vertx.core.json.JsonObject;
import org.apache.log4j.Logger;

public class CollectByOrder {
    protected static final Logger logger = Logger.getLogger(CollectByOrder.class);
    public static JsonObject collectCommunicationByOrder(JsonObject templateObject){
//        if(!templateObject.containsKey("targetIP")){
//            System.out.println("该模板格式错误");
//            logger.error(String.format("模板： %s 缺少目标机IP",templateObject));
//            return null;
//        }
//        String targetIP = templateObject.getString("targetIP");

        if(templateObject.getJsonObject("communication") == null || !templateObject.getJsonObject("communication").containsKey("content") ){
            System.out.println("该模板格式错误");
            logger.error(String.format("模板： %s 缺少配置项",templateObject));
            return null;
        }
        if(!templateObject.getJsonObject("communication").containsKey("targetIP") ){
            System.out.println("该模板格式错误");
            logger.error(String.format("模板： %s 缺少目标机",templateObject));
            return null;
        }
        Object targetIPObject = templateObject.getJsonObject("communication").getValue("targetIP");

        if(  !templateObject.getJsonObject("communication").containsKey("flag")){
            System.out.println("该模板格式错误");
            logger.error(String.format("模板： %s 缺少flag",templateObject));
            return null;
        }



        Object contentObject = templateObject.getJsonObject("communication").getValue("content");

        JsonObject params = new JsonObject().put("targetIP",targetIPObject).put("content",contentObject).put("flag",templateObject.getJsonObject("communication").getValue("flag"));
        return params;
    }

    public static JsonObject collectAnalysisByOrder(JsonObject templateObject){
//        if(!templateObject.containsKey("targetIP")){
//            System.out.println("该模板格式错误");
//            logger.error(String.format("模板： %s 缺少目标机IP",templateObject));
//            return null;
//        }
//        String targetIP = templateObject.getString("targetIP");

        if(templateObject.getJsonObject("analysis") == null || !templateObject.getJsonObject("analysis").containsKey("content") ){
            System.out.println("该模板格式错误");
            logger.error(String.format("模板： %s 缺少配置项",templateObject));
            return null;
        }


        if(!templateObject.getJsonObject("analysis").containsKey("targetIP") ){
            System.out.println("该模板格式错误");
            logger.error(String.format("模板： %s 缺少目标机",templateObject));
            return null;
        }
        Object targetIPObject = templateObject.getJsonObject("analysis").getValue("targetIP");

        if( !templateObject.getJsonObject("analysis").containsKey("flag")){
            System.out.println("该模板格式错误");
            logger.error(String.format("模板： %s 缺少flag",templateObject));
            return null;
        }

        Object contentObject = templateObject.getJsonObject("analysis").getValue("content");
        JsonObject params = new JsonObject().put("targetIP",targetIPObject).put("content",contentObject).put("flag",templateObject.getJsonObject("analysis").getValue("flag"));
        return params;
    }
}
