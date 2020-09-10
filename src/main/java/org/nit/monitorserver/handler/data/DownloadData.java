//package org.nit.monitorserver.handler.data;
//
//import io.vertx.core.json.JsonObject;
//import io.vertx.ext.mongo.MongoClient;
//import io.vertx.ext.web.RoutingContext;
//import org.apache.log4j.Logger;
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import org.nit.monitorserver.constant.HttpHeaderContentType;
//import org.nit.monitorserver.database.MongoConnection;
//import org.nit.monitorserver.handler.targetMachine.getData;
//import org.nit.monitorserver.message.AbstractRequestHandler;
//import org.nit.monitorserver.message.Request;
//import org.nit.monitorserver.message.ResponseFactory;
//import org.nit.monitorserver.util.FormValidator;
//import org.nit.monitorserver.util.Tools;
//
//import java.io.*;
//
//import static org.nit.monitorserver.constant.ResponseError.FORMAT_ERROR;
//import static org.nit.monitorserver.constant.ResponseError.PARAM_REQUIRED;
//import static org.nit.monitorserver.constant.ResponseError.QUERY_FAILURE;
//
///**
// * 功能描述: <整合至exportdata>
// * 〈〉
// * @Author: 20643
// * @Date: 2020-9-1 15:23
// */
//public class DownloadData extends AbstractRequestHandler {
//
//    protected static final Logger logger = Logger.getLogger(getData.class);
//    private final MongoClient mongoClient = new MongoConnection().getMongoClient();
//
//    @Override
//    public void handle(final RoutingContext routingContext, final Request request) {
//        routingContext.response().putHeader(HttpHeaderContentType.CONTENT_TYPE_STR, HttpHeaderContentType.JSON);
//        ResponseFactory response = new ResponseFactory(routingContext, request);
//
//        Object sidObject = request.getParamWithKey("id");//获取数据ID
//        if (sidObject == null){
//            response.error(PARAM_REQUIRED.getCode(),PARAM_REQUIRED.getMsg());
//            logger.error("数据Id为必填参数");
//            return;
//        }
//        if(!FormValidator.isString(sidObject)){
//            response.error(FORMAT_ERROR.getCode(),FORMAT_ERROR.getMsg());
//            logger.error("数据sId格式错误");
//            return;
//        }
//
//        String sid = sidObject.toString();
//
//        Object typeObject = request.getParamWithKey("type");//导出类型
//        if (typeObject == null){
//            response.error(PARAM_REQUIRED.getCode(),PARAM_REQUIRED.getMsg());
//            logger.error("导出类型为必填参数");
//            return;
//        }
//        if(!FormValidator.isString(typeObject)){
//            response.error(FORMAT_ERROR.getCode(),FORMAT_ERROR.getMsg());
//            logger.error("导出类型格式错误");
//            return;
//        }
//        String type = typeObject.toString();
//
//        Object flagObject = request.getParamWithKey("flag");//通信还是分析
//        if(flagObject == null){
//            response.error(PARAM_REQUIRED.getCode(),PARAM_REQUIRED.getMsg());
//            logger.error("数据类型为必填参数");
//            return;
//        }
//        if(!FormValidator.isInteger(flagObject)){
//            response.error(FORMAT_ERROR.getCode(),FORMAT_ERROR.getMsg());
//            logger.error("数据类型格式错误");
//            return;
//        }
//
//        int flag = Integer.parseInt(flagObject.toString());
//
//        JsonObject conditionQuery = new JsonObject().put("sid",sid);
//
//
//
//        switch (flag){
//            case 0:
//                mongoClient.find("communication_record",conditionQuery,r->{
//                    if(r.failed()){
//                        logger.error(String.format("query: %s, exception: %s",conditionQuery, Tools.getTrace(r.cause())));
//                        response.error(QUERY_FAILURE.getCode(),QUERY_FAILURE.getMsg());
//                        return;
//                    }
//                    String url = "D:\\实验室项目\\宿主机\\前端-杨文昕";//文件的保存地址
//                    System.out.println("成功找到需要导出的通信数据");
//                    JsonObject result = r.result().get(0);
//
//                    switch (type){
//                        case "xml":
//                            byte[] document = Tools.jsonToXML(result).getBytes();
//                            url = url +"\\"+"测试.txt";
//                            if (document != null){
//                                try {
//                                    File file = new File(url);
//                                    if(!file.exists()) {
//                                        File dir = new File(file.getParent());
//                                        dir.mkdirs();
//                                        file.createNewFile();
//                                    }
//                                    FileOutputStream fileOutputStream = new FileOutputStream(file);
//                                    fileOutputStream.write(document);
//                                    fileOutputStream.close();
//                                    response.success(new JsonObject().put("resutlt",url));
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                            System.out.println("导出类型为xml");
//                            break;
//                        case "excel":
//                            System.out.println("导出类型为excel");
//                            HSSFWorkbook documentExcel = Tools.jsonToExcel(result);
//                            url = url +"\\"+"测试.xls";
//                            try {
//                                FileOutputStream fileOutputStream = new FileOutputStream(url);
//                                documentExcel.write(fileOutputStream);
//                                fileOutputStream.close();
//                                response.success(new JsonObject().put("resutlt",url));
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                            break;
//
//                        case "二进制":
//                            document = result.toString().getBytes();//二进制文件
//                            url = url +"\\"+"测试.bin";
//                            System.out.println("导出类型为二进制");
//                            File target = new File(url);
//                            if (target.exists() && target.isFile()){
//                                boolean flagBin = target.delete();
//                            }
//                            try {
//                                if (target.createNewFile()){
//                                    for (int i = 0; i < 4096; i++) {
//                                        DataOutputStream out = new DataOutputStream(new FileOutputStream(url, true));
//                                        out.write(document);
////                    out.writeInt(i);
//                                        out.close();
//                                    }
//
//                                }
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                            break;
//                        default:
//                            break;
//                    }
//
//
//
//
//
//
//                });
//                break;
//            case 1:
//                mongoClient.find("analysis_record",conditionQuery,r->{
//                    if(r.failed()){
//                        logger.error(String.format("query: %s, exception: %s",conditionQuery, Tools.getTrace(r.cause())));
//                        response.error(QUERY_FAILURE.getCode(),QUERY_FAILURE.getMsg());
//                        return;
//                    }
//
//                    String url = "D:\\实验室项目\\宿主机\\前端-杨文昕";
//                    System.out.println("成功找到需要导出的分析数据");
//                    JsonObject result = r.result().get(0);
//
//                    switch (type){
//                        case "xml":
//                            byte[] document = Tools.jsonToXML(result).getBytes();
//                            url = url +"\\"+"测试.txt";
//                            System.out.println("导出类型为xml");
//                            if (document != null){
//                                try {
//                                    File file = new File(url);
//                                    if(!file.exists()) {
//                                        File dir = new File(file.getParent());
//                                        dir.mkdirs();
//                                        file.createNewFile();
//                                    }
//                                    FileOutputStream fileOutputStream = new FileOutputStream(file);
//                                    fileOutputStream.write(document);
//                                    fileOutputStream.close();
//                                    response.success(new JsonObject().put("resutlt",url));
//
//
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                            break;
//                        case "excel":
//                            HSSFWorkbook documentExcel = Tools.jsonToExcel(result);
//                            url = url +"\\"+"测试.xls";
//                            System.out.println("导出类型为excel");
//                            try {
//                                FileOutputStream fileOutputStream = new FileOutputStream(url);
//                                documentExcel.write(fileOutputStream);
//                                fileOutputStream.close();
//                                response.success(new JsonObject().put("resutlt",url));
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                            break;
//                        case "二进制":
//                            document = result.toString().getBytes();//二进制文件
//                            url = url +"\\"+"测试.bin";
//                            System.out.println("导出类型为二进制");
//                            break;
//                        default:
//                            break;
//                    }
//
//
//                });
//                break;
//             default:
//                 break;
//
//        }
//
//
//
//
////        mongoClient.find("tdrMsg",condition,r->{
////            if(r.failed()){
////                logger.error(String.format("query: %s, exception: %s",condition, Tools.getTrace(r.cause())));
////                response.error(QUERY_FAILURE.getCode(),QUERY_FAILURE.getMsg());
////                return;
////            }else{
////                JsonObject resultObtained = r.result().get(0);
////                switch (form){
////                    case "xml":
////
////
////                }
////
////            }
////        });
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//        JsonObject result = new JsonObject();
//        result.put("url", "http://39.104.87.140:8080/test.jpg");
//        response.success(result);
//    }
//
//}
