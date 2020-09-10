package org.nit.monitorserver.util;

import de.odysseus.staxon.json.JsonXMLConfig;
import de.odysseus.staxon.json.JsonXMLConfigBuilder;
import de.odysseus.staxon.json.JsonXMLInputFactory;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.nit.monitorserver.ICDParse.ICDParse;

import javax.xml.stream.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.nit.monitorserver.constant.GlobalConsts.YZ_HOME;

/**
 * @author sensordb
 * @date 2018/5/21
 */

public class Tools {

    public static void initSystemProperties() {
        if (System.getenv(YZ_HOME) != null) {
            System.setProperty(YZ_HOME, System.getenv(YZ_HOME));
        }
        else {
            System.setProperty(YZ_HOME, ".");
        }
    }


    public static String getTrace(final Throwable t) {
        final StringWriter stringWriter = new StringWriter();
        final PrintWriter writer = new PrintWriter(stringWriter);
        t.printStackTrace(writer);
        final StringBuffer buffer = stringWriter.getBuffer();
        return buffer.toString();
    }


    public static String getTrace(final String request, final Throwable t) {
        final StringWriter stringWriter = new StringWriter();
        final PrintWriter writer = new PrintWriter(stringWriter);
        if (request != null) {
            writer.append("request:");
            writer.append(request);
            writer.append('\n');
        }
        t.printStackTrace(writer);
        final StringBuffer buffer = stringWriter.getBuffer();
        return buffer.toString();
    }

    public static String getTrace(final String request, final String context, final Throwable t) {
        final StringWriter stringWriter = new StringWriter();
        final PrintWriter writer = new PrintWriter(stringWriter);
        if (request != null) {
            writer.append("request:");
            writer.append(request);
            writer.append('\n');
        }
        if (context != null) {
            writer.append("context:");
            writer.append(context);
            writer.append('\n');
        }
        t.printStackTrace(writer);
        final StringBuffer buffer = stringWriter.getBuffer();
        return buffer.toString();
    }

    public static JsonArray jsonObjectToJsonArray(JsonObject arr, String x, String y) {
        JsonArray res = new JsonArray();
        arr.forEach(tmp -> {
            JsonObject onePoint = new JsonObject();
            onePoint.put(x, tmp.getKey());
            onePoint.put(y, tmp.getValue());
            res.add(onePoint);
        });
        return res;
    }
//    将date转化为yyyy-MM-dd HH:mm:ss
    public static String dateToStrD(final Date date) {
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        final String str = format.format(date);
        return str;
    }
    public static String dateToStrT(final Date date) {
        final SimpleDateFormat format = new SimpleDateFormat("HH-mm-ss");
        final String str = format.format(date);
        return str;
    }

    //json转xml
    public static String jsonToXML(JsonObject jsonObject){
        if(jsonObject == null){
            return null;
        }
        if(jsonObject instanceof JsonObject == false){
            return null;
        }
        String jsonString = jsonObject.toString();
        StringReader input = new StringReader(jsonString);
        StringWriter output = new StringWriter();
        JsonXMLConfig config = new JsonXMLConfigBuilder().multiplePI(false).repairingNamespaces(false).build();
        try {
            XMLEventReader reader = new JsonXMLInputFactory(config).createXMLEventReader(input);
            XMLEventWriter writer = XMLOutputFactory.newInstance().createXMLEventWriter(output);
            writer.add(reader);
            reader.close();
            writer.close();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }finally {

            try {
                output.close();
                input.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return output.toString();
    }


    //json转excel
    public static HSSFWorkbook jsonToExcel(JsonObject jsonObject){
        Set<String> keys = null;
        //创建HSSFWorkbook对象
        HSSFWorkbook wb =  new HSSFWorkbook();
        //创建HSSFSheet对象
        HSSFSheet sheet = wb.createSheet("sheet0");


        int roleNo = 0;
        int rowNo = 0;
        HSSFRow row = sheet.createRow(roleNo++);
        ICDParse icdParse = new ICDParse();
        Map<String,Object> map = icdParse.jsonToMap(jsonObject);
        if(keys == null){
            //标题
            keys = map.keySet();
            for(String s : keys){
                HSSFCell cell = row.createCell(rowNo++);
                cell.setCellValue(s);
            }
            rowNo = 0;
            row = sheet.createRow(roleNo++);
        }
        for(String s : keys){
            HSSFCell cellFirst = row.createCell(rowNo++);
            cellFirst.setCellValue(map.get(s).toString());
        }
        return wb;





    }

    //生成主键
    public static String generateKey(){
        long nowDate = new Date().getTime();

        String sid = Integer.toHexString((int)nowDate);
        return sid;
    }

    public static String generateId(){
        String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
//        Random random = new Random();
//        int rannum = (int)(random.nextDouble()*(9999-1000+1))+1000;//获取4位随机数


        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        String Id = time+uuid;

        return Id;

    }


//    public static void executionCommunication(String input, Logger logger, Scanner scanner){
//        //为了忽略大小写，全部大写，并判断是否包含子字符串，收集数据
//        while (input.length() <= 7){
//            logger.error(String.format("指令： %s 格式错误",input));
//            input = null;
//            System.out.println("请输入完整的指令");
//
//            input = scanner.nextLine();
//        }
//        while(!input.substring(7,8).equals(" ")){
//            System.out.println(input.substring(7,8));
//
//            logger.error(String.format("指令： %s 格式错误",input));
//            input = null;
//            System.out.println("指令格式错误，请重新输入指令");
//            input = scanner.nextLine();
//        }
//        String documentPath = input.substring(8);
//        File ICDFile = new File(documentPath);
//        while (!ICDFile.exists()){
//
//            logger.error(String.format("文件：%s 未找到",documentPath));
//            input = null;
//            System.out.println("未找到该文件,请重新输入文件路径");
//            input = scanner.nextLine();
//        }
//
//        JsonObject templateObject = null;
//        try {
//            FileInputStream fileInputStream = new FileInputStream(ICDFile);
//            InputStreamReader reader = new InputStreamReader(fileInputStream,"GBK");
//
//            char[] buf = new char[1024];
//            int len;
//            StringBuffer stringBuffer = new StringBuffer();
//            while ((len = reader.read(buf)) != -1){
//                stringBuffer.append(new String(buf,0,len));//读取文件内容
//
//            }
//            System.out.println("收集的数据为："+ stringBuffer);
//            templateObject = new JsonObject(stringBuffer.toString());
//            System.out.println(templateObject);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        if(templateObject.containsKey("communication")){
//            System.out.println("包含通信信息");
//            JsonObject paramsForCommunication =  CollectByOrder.collectCommunicationByOrder(templateObject);
//            Object contentObject = paramsForCommunication.getValue("content");
//            System.out.println("通信的配置为："+ contentObject);
//            Object flagObject = paramsForCommunication.getValue("flag");
//            System.out.println("通信的falg："+ flagObject);
//            Object targetIpObject = paramsForCommunication.getValue("targetIP");
//            System.out.println("通信的IP："+ targetIpObject);
//            JsonObject responseObject = new CreateMachine().communicationParse(contentObject,flagObject,targetIpObject,logger);
//            if(responseObject.getString("type").equals("error")){
//                System.out.println("type: error");
//                System.out.println("code: "+ responseObject.getInteger("code")+" "+"message: "+responseObject.getString("message"));
//                return;
//            }else {
//                System.out.println("开始收集通信数据");
//            }
//        }
//        if(templateObject.containsKey("analysis")){
//            JsonObject paramsForAnalysis = CollectByOrder.collectAnalysisByOrder(templateObject);
//            Object contentObject = paramsForAnalysis.getValue("content");
//            System.out.println("分析为配置："+ contentObject);
//            Object flagObject = paramsForAnalysis.getValue("flag");
//            System.out.println("分析flag："+ flagObject);
//            Object targetIpObject = paramsForAnalysis.getValue("targetIP");
//            System.out.println("分析IP："+ targetIpObject);
//            JsonObject responseObject = new AnalysisTemplate().analysisParse(contentObject,flagObject,targetIpObject,logger);
//            if(responseObject.getString("type").equals("error")){
//                System.out.println("type: error");
//                System.out.println("code: "+ responseObject.getInteger("code")+" "+"message: "+responseObject.getString("message"));
//                return;
//            }else {
//                System.out.println("开始收集分析数据");
//            }
//        }
//    }
}
