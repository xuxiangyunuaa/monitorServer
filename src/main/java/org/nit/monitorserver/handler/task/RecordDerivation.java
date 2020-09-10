//package org.nit.monitorserver.handler.task;
//
//import io.vertx.core.json.JsonArray;
//import io.vertx.core.json.JsonObject;
//import io.vertx.ext.sql.SQLClient;
//import io.vertx.ext.web.RoutingContext;
//import org.apache.log4j.Logger;
//import org.apache.poi.hssf.usermodel.*;
//import org.nit.monitorserver.constant.HttpHeaderContentType;
//import org.nit.monitorserver.database.MysqlConnection;
//import org.nit.monitorserver.message.AbstractRequestHandler;
//import org.nit.monitorserver.message.Request;
//import org.nit.monitorserver.message.ResponseFactory;
//import org.nit.monitorserver.util.FormValidator;
//import org.nit.monitorserver.util.Tools;
//
//import java.io.*;
//
//import java.util.Date;
//import java.util.List;
//
//import static org.nit.monitorserver.constant.GlobalConsts.*;
//import static org.nit.monitorserver.constant.ResponseError.FORMAT_ERROR;
//import static org.nit.monitorserver.constant.ResponseError.RECORD_NOT_EXISTED;
//import static org.nit.monitorserver.constant.ResponseError.SERVER_ERROR;
//
//public class RecordDerivation extends AbstractRequestHandler {
//    protected static final Logger logger = Logger.getLogger(RecordDerivation.class);
//    private final SQLClient sqlClient = new MysqlConnection().getMySQLClient();
//    @Override
//    public void handle(RoutingContext routingContext, Request request) {
//        routingContext.response().putHeader(HttpHeaderContentType.CONTENT_TYPE_STR,"HttpHeaderContentType.JSON");
//        ResponseFactory response = new ResponseFactory(routingContext, request);//根据路由上下文以及请求产生响应
//        //获取参数
//        Object taskIndexObj = request.getParamWithKey(TASK_INDEX);
//
////        验证参数格式化
//        JsonArray params = new JsonArray();
//        if (taskIndexObj != null) {
//            if (!FormValidator.isInteger(taskIndexObj)) {
//                logger.info(String.format("exception:%s", "任务索引存在格式非法的参数"));
//                response.error(FORMAT_ERROR.getCode(), FORMAT_ERROR.getMsg());
//                return;
//            }
//            Integer taskIndex = (int)taskIndexObj;
//            params.add(taskIndex).add(1);
//        }
//
//        //查询特定的记录
//        String queryRecord="SELECT AJ_Num,TM_Name,AJ_StartTime,AJ_EndTime,AJ_Type FROM PM_AcquisitionJobs WHERE AJ_Num=? AND AJ_Flag=?";
//        sqlClient.queryWithParams(queryRecord,params,res->{
//            if(res.failed()){
//                logger.error(String .format("query:%s exception:%s",queryRecord,Tools.getTrace(res.cause())));
//                response.error(SERVER_ERROR.getCode(),SERVER_ERROR.getMsg());
//                return;
//            }
//            if(res.result().getNumRows() == 0){
//                logger.error(String.format("exception:%s","该记录不存在"));
//                response.error(RECORD_NOT_EXISTED.getCode(),RECORD_NOT_EXISTED.getMsg());
//                return;
//            }
//
//            //创建excel表格
//            HSSFWorkbook workBook=new HSSFWorkbook();
//            //创建一张工作表
//            HSSFSheet sheet =workBook.createSheet();
//            HSSFCellStyle style = workBook.createCellStyle();
//            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
//            sheet.setColumnWidth(0,5000);
//            sheet.setColumnWidth(1,5000);
//            sheet.setColumnWidth(2,5000);
//            sheet.setColumnWidth(3,5000);
//            sheet.setColumnWidth(4,5000);
//            //创建第一行
//            HSSFRow row1=sheet.createRow(0);
//            //创建表格的字段
//            row1.createCell(0).setCellValue("任务编号");
//            row1.createCell(1).setCellValue("目标机");
//            row1.createCell(2).setCellValue("开始时间");
//            row1.createCell(3).setCellValue("结束时间");
//            row1.createCell(4).setCellValue("任务类型");
//           // 在表格中填充查询的字段
//           List<JsonArray> list=res.result().getResults();
//            for(int i=0;i<list.size();i++){
//                HSSFRow row=sheet.createRow(i+1);
//                row.createCell(0).setCellValue(list.get(i).getValue(0).toString());
//                row.createCell(1).setCellValue(list.get(i).getValue(1).toString());
//                row.createCell(2).setCellValue(list.get(i).getValue(2).toString());
//                row.createCell(3).setCellValue(list.get(i).getValue(3).toString());
//                row.createCell(4).setCellValue(list.get(i).getValue(4).toString());
//             }
//            Date date=new Date();
//            long time=date.getTime();
//            String dateD=Tools.dateToStrD(date);
//            String dateT=Tools.dateToStrT(date);
//            String outPath="D:/temp/"+dateD+"/";
////            按照路径创建该文件
//            File dirfile=new File(outPath);
//            if(!dirfile.exists()){
////                创建多个文件夹，包括父
//                dirfile.mkdirs();
//            }
//            String outFile=outPath+dateT+".xls";
//            String url="http://39.104.87.140:8080/pm/"+outFile;
//            FileOutputStream fileOut = null;
//            try{
//                fileOut=new FileOutputStream(outFile);
//            }catch (FileNotFoundException e) {
//                e.printStackTrace();
//                return;
//            }
//            try {
//                workBook.write(fileOut);
//                fileOut.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            JsonObject result=new JsonObject();
//            result.put("URL",url);
//            response.success(result);
//        });
//    }
//}
