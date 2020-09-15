package org.nit.monitorserver.ICDParse;


import io.vertx.core.json.JsonObject;

import java.util.*;

public class test {


    public static void main(String[] args) {

//        JsonObject division= new JsonObject().put("type","integer").put("description","a").put("start","24");;
//        JsonObject a = new JsonObject().put("division",division);
//
//        JsonObject value= new JsonObject().put("type","integer").put("description","b").put("start","16");
//        JsonObject b = new JsonObject();
//        b.put("value",value);
//
//
//        JsonObject name= new JsonObject().put("type","integer").put("description","c").put("start","8");
//        JsonObject c = new JsonObject();
//        c.put("name",name);
//
//
//        JsonObject id= new JsonObject().put("type","integer").put("description","d").put("start","0");
//        JsonObject d = new JsonObject();
//        d.put("id",id);
//
//        JsonObject BUS = new JsonObject().put("BUS")
//
//
//        JsonObject division2= new JsonObject().put("type","integer").put("description","a").put("start","24");;
//        JsonObject a2 = new JsonObject();
//        a.put("division",division2);
//
//        JsonObject value2= new JsonObject().put("type","integer").put("description","b").put("start","16");
//        JsonObject b2 = new JsonObject();
//        b.put("value",value2);
//
//
//        JsonObject name2= new JsonObject().put("type","integer").put("description","c").put("start","8");
//        JsonObject c2 = new JsonObject();
//        c.put("name",name2);
//
//
//        JsonObject id2= new JsonObject().put("type","integer").put("description","d").put("start","0");
//        JsonObject d2 = new JsonObject();
//        d.put("id",id2);
//
//
//        JsonArray jsonArray2 = new JsonArray();
//        jsonArray2.add(a2).add(b2).add(c2).add(d2);
//
//
//
//        JsonObject g= new JsonObject().put("BUS",jsonArray);
//        JsonObject g2= new JsonObject().put("BUS2",jsonArray2);
//        JsonObject com = new JsonObject().
////        System.out.println(g);
//
//        System.out.println(h);
//        ICDParse icdParse = new ICDParse();
//        System.out.println(icdParse.jsonToMap(h));


//        JsonObject g= new JsonObject();
//        g.put("ICD",f);
//        System.out.println(g);
//        ICDParse icdParse = new ICDParse();
//        System.out.println(icdParse.jsonToMap(g));


//        String task = "{\n" +
//                "\t\"ICD \": {\n" +
//                "\t\t\"Bus\": {\n" +
//                "\t\t\t\"HAHA\": {\n" +
//                "\t\t\t\t\"_startBit\": \"64\",\n" +
//                "\t\t\t\t\"id\": {\n" +
//                "\t\t\t\t\t\"_type\": \"string\",\n" +
//                "\t\t\t\t\t\"_description\": \"HAHAid\",\n" +
//                "\t\t\t\t\t\"_start\": 0,\n" +
//                "\t\t\t\t\t\"_end\": 63\n" +
//                "\t\t\t\t},\n" +
//                "\t\t\t\t\"_bitLength\": \"64\"\n" +
//                "\t\t\t},\n" +
//                "\t\t\t\"_startBit\": \"0\",\n" +
//                "\t\t\t\"id\": {\n" +
//                "\t\t\t\t\"_type\": \"string\",\n" +
//                "\t\t\t\t\"_description\": \"qqqqqq\",\n" +
//                "\t\t\t\t\"_start\": \"0\",\n" +
//                "\t\t\t\t\"_end\": \"7\"\n" +
//                "\t\t\t},\n" +
//                "\t\t\t\"name\": {\n" +
//                "\t\t\t\t\"_type\": \"string\",\n" +
//                "\t\t\t\t\"_description\": \"wwwwww\",\n" +
//                "\t\t\t\t\"_start\": \"8\",\n" +
//                "\t\t\t\t\"_end\": \"15\"\n" +
//                "\t\t\t},\n" +
//                "\t\t\t\"value\": {\n" +
//                "\t\t\t\t\"_type\": \"integer\",\n" +
//                "\t\t\t\t\"_description\": \"eeeeee\",\n" +
//                "\t\t\t\t\"_start\": \"16\",\n" +
//                "\t\t\t\t\"_end\": \"23\"\n" +
//                "\t\t\t},\n" +
//                "\t\t\t\"division\": {\n" +
//                "\t\t\t\t\"_type\": \"boolean\",\n" +
//                "\t\t\t\t\"_description\": \"rrrrrrr\",\n" +
//                "\t\t\t\t\"_start\": \"24\",\n" +
//                "\t\t\t\t\"_end\": \"31\"\n" +
//                "\t\t\t},\n" +
//                "\t\t\t\"property5\": {\n" +
//                "\t\t\t\t\"_type\": \"string\",\n" +
//                "\t\t\t\t\"_description\": \"tttttttttt\",\n" +
//                "\t\t\t\t\"_start\": \"32\",\n" +
//                "\t\t\t\t\"_end\": \"47\"\n" +
//                "\t\t\t},\n" +
//                "\t\t\t\"property6\": {\n" +
//                "\t\t\t\t\"_type\": \"string\",\n" +
//                "\t\t\t\t\"_description\": \"yyyyyyyyyyyyyyy\",\n" +
//                "\t\t\t\t\"_start\": \"48\",\n" +
//                "\t\t\t\t\"_end\": \"63\"\n" +
//                "\t\t\t},\n" +
//                "\t\t\t\"_bitLength\": \"128\",\n" +
//                "\t\t\t\"_portName\": \"1\"\n" +
//                "\t\t},\n" +
//                "\t\t\"BUS2\": {\n" +
//                "\t\t\t\"_startBit\": \"128\",\n" +
//                "\t\t\t\"property\": {\n" +
//                "\t\t\t\t\"_type\": \"string\",\n" +
//                "\t\t\t\t\"_description\": \"uuuuuuuuuu\",\n" +
//                "\t\t\t\t\"_start\": 0,\n" +
//                "\t\t\t\t\"_end\": \"31\"\n" +
//                "\t\t\t},\n" +
//                "\t\t\t\"_bitLength\": \"32\",\n" +
//                "\t\t\t\"_portName\": \"2\"\n" +
//                "\t\t},\n" +
//                "\t\t\"_bitLength\": \"160\"\n" +
//                "\t}\n" +
//                "}";

        String test="{\n" +
                "\t\"ICD\": {\n" +
                "\t\t\"Bus\": {\n" +
                "\t\t\t\"HAHA\": {\n" +
                "\t\t\t\t\"_startBit\": \"64\",\n" +
                "\t\t\t\t\"HAHA2\": {\n" +
                "\t\t\t\t\t\"_startBit\": \"64\",\n" +
                "\t\t\t\t\t\"id\": {\n" +
                "\t\t\t\t\t\t\"_type\": \"Integer\",\n" +
                "\t\t\t\t\t\t\"_description\": \"HAHAid\",\n" +
                "\t\t\t\t\t\t\"_start\": 0,\n" +
                "\t\t\t\t\t\t\"_end\": 63\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t\"_bitLength\": \"64\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"id\": {\n" +
                "\t\t\t\t\t\"_type\": \"Integer\",\n" +
                "\t\t\t\t\t\"_description\": \"HAHAid\",\n" +
                "\t\t\t\t\t\"_start\": 0,\n" +
                "\t\t\t\t\t\"_end\": 63\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"_bitLength\": \"64\"\n" +
                "\t\t\t},\n" +
                "\t\t\t\"_startBit\": \"0\",\n" +
                "\t\t\t\"id\": {\n" +
                "\t\t\t\t\"_type\": \"Integer\",\n" +
                "\t\t\t\t\"_description\": \"qqqqqq\",\n" +
                "\t\t\t\t\"_start\": \"0\",\n" +
                "\t\t\t\t\"_end\": \"7\"\n" +
                "\t\t\t},\n" +
                "\t\t\t\"name\": {\n" +
                "\t\t\t\t\"_type\": \"Integer\",\n" +
                "\t\t\t\t\"_description\": \"wwwwww\",\n" +
                "\t\t\t\t\"_start\": \"8\",\n" +
                "\t\t\t\t\"_end\": \"15\"\n" +
                "\t\t\t},\n" +
                "\t\t\t\"value\": {\n" +
                "\t\t\t\t\"_type\": \"integer\",\n" +
                "\t\t\t\t\"_description\": \"eeeeee\",\n" +
                "\t\t\t\t\"_start\": \"16\",\n" +
                "\t\t\t\t\"_end\": \"23\"\n" +
                "\t\t\t},\n" +
                "\t\t\t\"division\": {\n" +
                "\t\t\t\t\"_type\": \"Integer\",\n" +
                "\t\t\t\t\"_description\": \"rrrrrrr\",\n" +
                "\t\t\t\t\"_start\": \"24\",\n" +
                "\t\t\t\t\"_end\": \"31\"\n" +
                "\t\t\t},\n" +
                "\t\t\t\"property5\": {\n" +
                "\t\t\t\t\"_type\": \"Integer\",\n" +
                "\t\t\t\t\"_description\": \"tttttttttt\",\n" +
                "\t\t\t\t\"_start\": \"32\",\n" +
                "\t\t\t\t\"_end\": \"47\"\n" +
                "\t\t\t},\n" +
                "\t\t\t\"property6\": {\n" +
                "\t\t\t\t\"_type\": \"Integer\",\n" +
                "\t\t\t\t\"_description\": \"yyyyyyyyyyyyyyy\",\n" +
                "\t\t\t\t\"_start\": \"48\",\n" +
                "\t\t\t\t\"_end\": \"63\"\n" +
                "\t\t\t},\n" +
                "\t\t\t\"_bitLength\": \"128\",\n" +
                "\t\t\t\"_portName\": \"1\"\n" +
                "\t\t},\n" +
                "\t\t\"BUS2\": {\n" +
                "\t\t\t\"_startBit\": \"128\",\n" +
                "\t\t\t\"property\": {\n" +
                "\t\t\t\t\"_type\": \"Integer\",\n" +
                "\t\t\t\t\"_description\": \"uuuuuuuuuu\",\n" +
                "\t\t\t\t\"_start\": 0,\n" +
                "\t\t\t\t\"_end\": \"31\"\n" +
                "\t\t\t},\n" +
                "\t\t\t\"_bitLength\": \"32\",\n" +
                "\t\t\t\"_portName\": \"2\"\n" +
                "\t\t},\n" +
                "\t\t\"_bitLength\": \"160\"\n" +
                "\t}\n" +
                "}";
        byte[] bytes = {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};

        System.out.println(test);
        ICDParse icdParse = new ICDParse();
        String portName = "1";
        Map<String,Object> map = icdParse.jsonToMap(new JsonObject(test));//json转Map
        System.out.println(map);
        Set<String> nameSet = map.keySet();
        Iterator iterator = nameSet.iterator();
        String icdName = iterator.next().toString();//获取第一层icd的key
        System.out.println("icd的名字为："+icdName);
        Object firstICDObject = map.get(icdName);//获取第一层的对象
        System.out.println("第一层的对象为："+firstICDObject);
        JsonObject firstResult = new JsonObject();
        JsonObject result = new JsonObject();
        if(firstICDObject != null){
//            Map<String,Object>  secondICDMap= new org.apache.commons.beanutils.BeanMap(firstICDObject);//第二层ICD的Map
            if(firstICDObject instanceof Map == false){
                return;
            }
            System.out.println("第一层对象为Map");
            Map<String,Object>  secondICDMap = (Map<String,Object>) firstICDObject;
            System.out.println("第二层的Map为"+secondICDMap);
            JsonObject secondResult = new JsonObject();
            System.out.println("开始遍历第二层元素");
            for(Map.Entry<String,Object> entry : secondICDMap.entrySet()){//遍历第二层ICD元素

                String secondICDKey = entry.getKey();//第二层
                System.out.println("第二层的key："+secondICDKey);
                Object secondICDObject = entry.getValue();
                System.out.println("第二层的"+secondICDKey+"的Value为"+secondICDObject);

                if(secondICDObject instanceof Map){
                    System.out.println("第二层的"+secondICDKey+"的Value为Map");
                    Map<String,Object> thirdICDMap = (Map<String,Object>)secondICDObject;//第三层ICD
                    System.out.println("第三层的Map为"+thirdICDMap);
                    if(thirdICDMap.containsKey("_portName") ){
                        System.out.println("第三层包含portName");

                        String thirdICDPortName = thirdICDMap.get("_portName").toString();
                        System.out.println("第三层的portName:"+thirdICDPortName);
                        if(thirdICDPortName.equals(portName)){//寻找与二进制数据对应的portName
                            System.out.println("该portName适配");
                            secondResult= icdParse.portNameParse(thirdICDMap,secondICDKey,0,bytes);

                            System.out.println("secondResult"+secondResult);
                        }else {
                            continue;
                        }
                    }else {
                        System.out.println("请求解析错误");
                        return;
                    }
                   }else {
                    continue;
                }
                firstResult.put(secondICDKey,secondResult);

                }
            result.put(icdName,firstResult);
            }else {

        }

            System.out.println(result);
        }


//        HashMap<String, Object> data = new HashMap<>();
//        Iterator iterator = store.entrySet().iterator();
//        System.out.println(iterator);
//        while (iterator.hasNext()){
//            Map.Entry<String,Object> entry =(Map.Entry<String, Object>) iterator.next();
//            data.put(entry.getKey(),entry.getValue());
//        }
//        System.out.println(data.keySet());
//        ICDParse icdParse =  new ICDParse();
//        System.out.println(g);
//        System.out.println(hashMap);


    }


