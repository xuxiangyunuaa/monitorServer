//package org.nit.monitorserver.handler.data;
//
//import com.sun.org.apache.xerces.internal.dom.PSVIAttrNSImpl;
//import io.vertx.core.json.JsonArray;
//
///**
// * @author 20817
// * @version 1.0
// * @className test
// * @description
// * @date 2020/9/12 12:05
// */
//public class test {
//    public static void main(String[] args) {
//        JsonArray array  = new JsonArray().add("a").add("b").add("c");
//        String where = "where";
//        for(int i = 0; i < array.size(); i ++){
//            where = where+" "+"drt_id = "+array.getValue(i)+" OR";
//        }
//        int length = where.length();
//        String condition = where.substring(0,length-3)+";";
//
//        System.out.println(condition);
//    }
//
//}
