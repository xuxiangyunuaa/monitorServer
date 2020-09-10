package org.nit.monitorserver.database;

import io.vertx.core.json.JsonObject;

import io.vertx.ext.sql.SQLClient;


import org.nit.monitorserver.handler.task.AnalysisTemplate;
import org.nit.monitorserver.handler.task.Create;


import java.io.File;
import java.io.FileInputStream;

import java.util.Scanner;
import io.vertx.core.Vertx;

import org.apache.log4j.Logger;
import org.nit.monitorserver.handler.task.CreateTask;
import org.nit.monitorserver.util.CollectByOrder;

import static org.nit.monitorserver.constant.ResponseError.QUERY_FAILURE;




public class test {
    protected static final Logger logger = Logger.getLogger(test.class);
    final static Vertx vertx = Vertx.vertx();
    private static String idPrevious = null;

    public static void main(String[] args) {
        if(args.length > 0){
            System.out.println("请输入一条指令！");
            System.out.print("--->");//提示输入指令
            Scanner scanner = new Scanner(System.in) ;
            String input = scanner.nextLine();
            if(input.toUpperCase().indexOf("COLLECT".toUpperCase()) < 0 ){//为了忽略大小写，全部大写，并判断是否包含子字符串，收集数据
                while(input.substring(7,8) != " "){
                    System.out.println("指令格式错误");
                    logger.error(String.format("指令： %s 格式错误",input));
                    input = null;
                    System.out.println("请重新输入指令");
                    input = scanner.nextLine();
                }
                String documentPath = input.substring(8);
                File ICDFile = new File(documentPath);
                while (!ICDFile.exists()){
                    System.out.println("未找到该文件");
                    logger.error(String.format("文件：%s 未找到",documentPath));
                    input = null;
                    System.out.println("请重新输入指令");
                    input = scanner.nextLine();
                }
                System.out.println("开始收集数据");
                JsonObject templateObject = null;
                try {
                    FileInputStream fileInputStream = new FileInputStream(ICDFile);
                    byte[] buf = new byte[1024];
                    int len = 0;
                    StringBuffer stringBuffer = new StringBuffer();
                    while ((len = fileInputStream.read(buf)) != -1){
                        stringBuffer.append(new String(buf,0,len));//读取文件内容
                        templateObject = new JsonObject(stringBuffer.toString());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(templateObject.containsKey("communication")){
                    JsonObject paramsForCommunication =  CollectByOrder.collectCommunicationByOrder(templateObject);
                    Object contentObject = paramsForCommunication.getValue("content");
                    Object flagObject = paramsForCommunication.getValue("flag");
                    Object targetIpObject = paramsForCommunication.getValue("targetIP");
                    JsonObject responseObject = new CreateTask().communicationParse(contentObject,flagObject,targetIpObject,logger);
                    if(responseObject.getString("type").equals("error")){
                        System.out.println("type: error");
                        System.out.println("code: "+ responseObject.getInteger("code")+" "+"message: "+responseObject.getString("message"));
                        return;
                    }

                }
                if(templateObject.containsKey("analysis")){
                    JsonObject paramsForAnalysis = CollectByOrder.collectAnalysisByOrder(templateObject);
                    Object contentObject = paramsForAnalysis.getValue("content");
                    Object flagObject = paramsForAnalysis.getValue("flag");
                    Object targetIpObject = paramsForAnalysis.getValue("targetIP");
                    JsonObject responseObject = new AnalysisTemplate().analysisParse(contentObject,flagObject,targetIpObject,logger);
                    if(responseObject.getString("type").equals("error")){
                        System.out.println("type: error");
                        System.out.println("code: "+ responseObject.getInteger("code")+" "+"message: "+responseObject.getString("message"));
                        return;
                    }

                }

            }
            if(input.toUpperCase().indexOf("SHOW".toUpperCase()) < 0 ){//展示数据
                System.out.println("开始展示数据，若需停止请键入esc");
                Scanner scanner1 = new Scanner(System.in);
                String escString = scanner1.toString();//是否键入esc
//                Calendar calendar = Calendar.getInstance();
//                SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                String currentTime = dateFormat.format(calendar.getTime());//获取当前时间



                long timeID= vertx.setPeriodic(30,r->{

                    if(escString.toUpperCase().indexOf("esc".toUpperCase()) < 0){
                        vertx.cancelTimer(r);//键入esc或者ESC,停止展示
                    };
                    String query = "SELECT * FROM detrcd_latest_table;";
                    SQLClient mySQLClient = new MysqlConnection().getMySQLClient();
                    mySQLClient.query(query,re->{
                        if(re.failed()){
                            System.out.println("type: error");
                            System.out.println("code: "+QUERY_FAILURE.getCode()+" "+"message: "+QUERY_FAILURE.getMsg());
                        }else {

                            JsonObject currentRecord = re.result().getRows().get(0);
                            String idCurrent = currentRecord.getString("drt_id");

                            if( idCurrent.equals(idPrevious)){
                                System.out.println("···················");
                                System.out.println("id: "+ currentRecord.getValue("drt_id") );
                                System.out.println("eventType: "+ currentRecord.getValue("drt_eventtype") );
                                System.out.println("partitonName: "+ currentRecord.getValue("drt_partitonname") );
                                System.out.println("portName: "+ currentRecord.getValue("drt_portname") );
                                System.out.println("timeStamp: "+ currentRecord.getValue("drt_timestamp") );
                                System.out.println("data: "+ currentRecord.getValue("drt_data") );
                                idPrevious = idCurrent;
                            }

                        }
                    });








                });





            }
        }



    }
}
