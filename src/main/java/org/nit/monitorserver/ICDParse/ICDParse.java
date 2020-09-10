package org.nit.monitorserver.ICDParse;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
//import org.everit.json.schema.
//import org.json.JSONArray;
//import org.json.JSONObject;
//import org.json.JSONObject;
//import org.json.JSONObject;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.nit.monitorserver.byteConversion.ByteConversion;


import java.math.BigDecimal;
import java.util.*;

public class ICDParse {
    public  JsonObject icd;
    public  String xml;

    public void setIcd(JsonObject icd) {
        this.icd=icd;
    }

    public void setXml(String xml) {
        this.xml=xml;
    }

    //xml转换为json
//    public JSONObject xml2json(String xml){
//        this.xml=xml;
//        JSONObject xmlToJson=XML.toJSONObject(this.xml);
//        return xmlToJson;
//    }
//
//    //验证json文件是否符合jsonICD
//    public boolean validateXmlWithICD(JSONObject valIcd, JSONObject resJson){
//        JSONObject resultObject = new JSONObject();
//        if (null == resJson || null==valIcd) {
//            return false;
//        }
////        JSONObject schemaObject = new JSONObject(new JSONTokener(schema));
//        Schema jsonSchema = SchemaLoader.load(valIcd);
//
//        try {
//            jsonSchema.validate(resJson);
//            return true;
//        } catch (ValidationException e) {
//            return false;
//        }
//    }

    //利用jsonPath获取json数据中的某个值
//    public List<Object> getValueUsingJsonPath(JSONObject json, String path){
//        if (json == null || path == null) {
//            return null;
//        }
//        try {
//            List<Object> val = JsonPath.read(json,path);
//            return val;
//        } catch (Exception ex) {
//            return null;
//        }
//    }

    public static Map<String,Object> parseJSONToMap (JSONObject jsonObject){
        Map<String, Object> map = new HashMap<String, Object>();//创建hasMap
        for(Object key : jsonObject.keySet()){
            Object value = jsonObject.get(key);
//            System.out.println(value);
            if(value instanceof JSONArray){//判断value是否为JSONArray
//                System.out.println("对应的value为数组");
                List<Map<String,Object>> list = new ArrayList<Map<String, Object>>();
                Iterator<JSONObject> iterator = ((JSONArray) value).iterator();
                while (iterator.hasNext()){
                    JSONObject sonObject = iterator.next();
                    list.add(parseJSONToMap(sonObject));
                }
                map.put(key.toString(),list);
            }else if (value instanceof JSONObject){
                map.put(key.toString(),parseJSONToMap((JSONObject) value));
            }else {
                map.put(key.toString(),value);
            }
        }
        return map;
    }

    public static Map jsonToMap(JsonObject jsonObject){
        Map<String, Object> map = new HashMap<String, Object>();
        for(Map.Entry<String,Object> entry : jsonObject.getMap().entrySet()){
            Object value = entry.getValue();
            if(value instanceof JsonArray){
                List<Map<String,Object>> list = new ArrayList<Map<String, Object>>();
                JsonArray jsonArray = (JsonArray)value;
                for(int i = 0 ; i < jsonArray.size(); i++){
                    list.add(jsonToMap(jsonArray.getJsonObject(i)));
                }
                map.put(entry.getKey().toString(),list);

            }else if(value instanceof JsonObject){
                map.put(entry.getKey().toString(),jsonToMap((JsonObject) value));
            }else {
                map.put(entry.getKey().toString(),value);
            }
        }
        return map;
    }



    public static JsonObject portNameParse(Map<String,Object> thirdICDMap, String secondICDKey,int firstStartBit, byte[] bytes){
        System.out.println("---------------------------------------------------");
        System.out.println("需要解析的数据为："+bytes);
        System.out.println("第三层的Map:"+ thirdICDMap);
        JsonObject result = new JsonObject();
        int startBit = Integer.parseInt(thirdICDMap.get("_startBit").toString())+firstStartBit;
        int bitLength = Integer.parseInt(thirdICDMap.get("_bitLength").toString());
        if(thirdICDMap instanceof Map == false){
            return null;
        }
        ByteConversion byteConversion = new ByteConversion(); //二进制类型转换
        JsonObject secondResult = new JsonObject();
        JsonObject thirdResult = new JsonObject();
        System.out.println("开始遍历三层所有元素");
        for(Map.Entry<String,Object> entry : thirdICDMap.entrySet()){//遍历map元素
            if(entry.getValue() instanceof Map){
                System.out.println("其为MAP"+entry);
                String thirdICDKey = entry.getKey();
                System.out.println("第三层的key为"+thirdICDKey);
                Object thirdICDObject = entry.getValue();
                System.out.println("第三层"+thirdICDKey+"的Object:"+ thirdICDObject);

                Map<String,Object> forthICDMap = (Map<String,Object>)entry.getValue();
                System.out.println("第三层"+thirdICDKey+"的Map为"+forthICDMap);
                if(forthICDMap.containsKey("_type") && forthICDMap.containsKey("_description") &&
                        forthICDMap.containsKey("_start") && forthICDMap.containsKey("_end")){ //验证是否为属性层
                    System.out.println(thirdICDKey+"为属性值");

                    JsonObject forthResult = new JsonObject();

                    int offSet = Integer.parseInt(forthICDMap.get("_start").toString());//获取二进制起始位
                    int length = Integer.parseInt(forthICDMap.get("_end").toString()) - offSet + 1;//获取截取长度
                    String type = forthICDMap.get("_type").toString();//获取数据类型
                    String description =forthICDMap.get("_description").toString();
                    byte[] ICDData = byteConversion.subByte(bytes,startBit + offSet,length);//获取截至的子二进制
                    Object byteParsed = null;

                    switch (type){
                        case "String":
                            byteParsed = (String)byteConversion.bytes2String(ICDData);
                            break;
                        case "Decimal":
                            byteParsed = (BigDecimal)byteConversion.bytesToDecimal(ICDData);
                            break;
                        case "Integer":
                            byteParsed = (int)byteConversion.byteArrayToInt(ICDData);
                            System.out.println("---------------------------------------------------");
                            System.out.println("解析出的数据为："+byteParsed);
                            break;
                        case "Boolean":
                            byteParsed= (boolean)byteConversion.byteToBoolean(ICDData) ;
                            break;
                        default:
                            break;
                    }
                    System.out.println("byte转各类型");

                    forthResult.put("_type",type).put("_description",description).put("data",byteParsed);
                    System.out.println("thirdICDKey:"+thirdICDKey);
                    secondResult.put(thirdICDKey,forthResult);

                    System.out.println("forthResult:"+forthResult);
                    System.out.println("secondResult:"+secondResult);

                }else if(forthICDMap.containsKey("_startBit") && forthICDMap.containsKey("_bitLength")){//存在嵌套结构
                    System.out.println(forthICDMap+"存在嵌套结构");
                    secondResult.put(thirdICDKey,portNameParse(forthICDMap,thirdICDKey,startBit,bytes));
                    System.out.println("secondResult:"+secondResult);
//                    thirdResult.put(thirdICDKey,secondResult);
//                    System.out.println("resultSon:"+thirdResult)
                }

            }else {
                System.out.println("不是MAP"+entry);
                continue;
            }


        }
//        result.put(secondICDkey,secondResult);
        return secondResult;

//        Set<String> thirdICDKeys = thirdICD.keySet();
//        Iterator iterator = thirdICDKeys.iterator();
//        while (iterator.hasNext()){//获取每一个key
//            String thirdICDKey = (String)iterator.next();
//            Object thirdICDObject = thirdICD.get(thirdICDKey);
//            if(thirdICDObject != null){
//                Map<String,Object> thirdICDContent = new org.apache.commons.beanutils.BeanMap(thirdICDObject);//第三层ICD
//                if(thirdICDContent.containsKey())
//            }
//        }



    }


//    public static Map<String,Object> parseJsonToMap (JsonObject jsonObject){
//
//        Map<String, Object> mapFirst =jsonObject.getMap();
//        Map<String,Object> map = null ;
//        Set<String> keys = mapFirst.keySet();
//        Iterator<String> iterator = keys.iterator();
//        while (iterator.hasNext()){
//            String firstKey = iterator.next();
//        }
//
//
//
//
//
//        for(String key : mapFirst.keySet()){
//            Object value = mapFirst.get(key);
////            System.out.println(value);
//            if(value instanceof JsonArray){//判断value是否为JSONArray
////                System.out.println("对应的value为数组");
//               JsonArray jsonArray =  (JsonArray)value;
//                List<Map<String,Object>> list = new ArrayList<Map<String, Object>>();
//                for (int i = 0; i < jsonArray.size(); i++) {
//                    list.add(parseJsonToMap(jsonArray.getJsonObject(i)));
//                }
//                map.put(key.toString(),list);
//            }else if (value instanceof JsonObject){
//                map.put(key.toString(),parseJsonToMap((JsonObject) value));
//            }else {
//                map.put(key.toString(),value);
//            }
//        }
//        return map;
//    }




//    XMLParser xmlParser=new XMLParser();
}
