package org.nit.monitorserver;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.nit.monitorserver.ICDParse.CollectingICDParse;
import org.nit.monitorserver.handler.task.AnalysisTemplate;
import org.nit.monitorserver.handler.task.CreateTask;
import org.nit.monitorserver.util.CollectByOrder;
import org.nit.monitorserver.util.DateUtil;
import org.nit.monitorserver.util.StringUtil;
import org.nit.monitorserver.util.Tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Scanner;
import io.vertx.ext.sql.SQLClient;
import org.nit.monitorserver.database.MysqlConnection;

import static org.nit.monitorserver.constant.GlobalConsts.YZ_HOME;
import static org.nit.monitorserver.constant.ResponseError.QUERY_FAILURE;

/**
 * @author eda
 * @date 2018/5/21
 */

public class StartupWithLocal {
    private static String LOG_FILE = ((System.getenv(YZ_HOME) == null) ? "./conf/log4j.properties" :
            (System.getenv(YZ_HOME) + "/conf/log4j.properties"));
    protected static final Logger logger = Logger.getLogger(StartupWithLocal.class);
    private static String idPrevious = null;

    public static void main(final String[] args) {
        Tools.initSystemProperties();

        // 使用log4j日志而非默认的JUL
        System.setProperty("vertx.logger-delegate-factory-class-name", "io.vertx.core.logging.Log4jLogDelegateFactory");
        PropertyConfigurator.configure(LOG_FILE);
        //设置数据库配置为本地数据库
        Config.setLocalDatabaseConfFile();
        //设置Config为本地
        Config.setLocalConfFile();

        logger.info(("@@@@@@@@@@@@@@@" + DateUtil.dateToStr(new Date()) + " Data quality manager system Server startup"));
        try {
            //获取 vertx 实例
            final Vertx vertx = Vertx.vertx();
            VertxInstance.getInstance().setVertx(vertx);
            //实例化 Server 对象
            final Server server = new Server();
            final ListenAcquisition listenAcquisition=new ListenAcquisition();
            vertx.deployVerticle(server);
            vertx.deployVerticle(listenAcquisition);


//            if(args.length > 0){
            System.out.println("args的长度为："+args.length);
                System.out.println("请输入一条指令！");
                System.out.print("--->");//提示输入指令
                Scanner scanner = new Scanner(System.in) ;
                String input = scanner.nextLine();

                if(input.toUpperCase().indexOf("COLLECT".toUpperCase()) >=  0 ){
                    //为了忽略大小写，全部大写，并判断是否包含子字符串，收集数据
                   executionCommunication(input,scanner,vertx);

                }else if(input.toUpperCase().indexOf("SHOW".toUpperCase()) >= 0 ){//展示数据
                    executionShow(vertx);

                }else {
                    System.out.println("指令格式错误。请重新输入");
                    System.out.println("请输入一条指令！");
                    System.out.print("--->");//提示输入指令
                    input = scanner.nextLine();
                    if(input.toUpperCase().indexOf("COLLECT".toUpperCase()) >=  0 ){
                        executionCommunication(input,scanner,vertx);
                    }else if(input.toUpperCase().indexOf("SHOW".toUpperCase()) >= 0 ){
                        executionShow(vertx);
                    }else {
                        System.out.println("指令格式错误。请重新输入");

                    }

                }




//            }

        } catch (Throwable e) {
            final String es = Tools.getTrace(e);
            logger.error(es);
            logger.info(("@@@@@@@@@@@@@@@" + DateUtil.dateToStr(new Date()) + " Data quality manager system Server exit with exception:" + es));
        }
    }

    public static void executionCommunication(String input,Scanner scanner,Vertx vertx){
        while (input.length() <= 7){
            logger.error(String.format("指令： %s 格式错误",input));
            input = null;
            System.out.println("请输入完整的指令");

            input = scanner.nextLine();
        }
        while(!input.substring(7,8).equals(" ")){
            System.out.println(input.substring(7,8));

            logger.error(String.format("指令： %s 格式错误",input));
            input = null;
            System.out.println("指令格式错误，请重新输入指令");
            input = scanner.nextLine();
        }
        String documentPath = input.substring(8);
        File ICDFile = new File(documentPath);
        while (!ICDFile.exists()){

            logger.error(String.format("文件：%s 未找到",documentPath));

            System.out.println("未找到该文件,请重新输入文件路径");

            input = scanner.nextLine();
        }

        JsonObject templateObject = null;

        try {
            FileInputStream fileInputStream = new FileInputStream(ICDFile);
            InputStreamReader reader = new InputStreamReader(fileInputStream,"GBK");

            char[] buf = new char[1024];
            int len;
            StringBuffer stringBuffer = new StringBuffer();
            while ((len = reader.read(buf)) != -1){
                stringBuffer.append(new String(buf,0,len));//读取文件内容

            }
            System.out.println("收集的数据为："+ stringBuffer);
            templateObject = new JsonObject(stringBuffer.toString());
            System.out.println(templateObject);

        } catch (Exception e) {
            e.printStackTrace();
        }
        if(templateObject.containsKey("communication")){
            System.out.println("包含通信信息");
            JsonObject paramsForCommunication =  CollectByOrder.collectCommunicationByOrder(templateObject);
            Object contentObject = paramsForCommunication.getValue("content");
            System.out.println("通信的配置为："+ contentObject);
            Object flagObject = paramsForCommunication.getValue("flag");
            System.out.println("通信的falg："+ flagObject);
            Object targetIpObject = paramsForCommunication.getValue("targetIP");
            System.out.println("通信的IP："+ targetIpObject);
            JsonObject responseObject = new CreateTask().communicationParse(contentObject,flagObject,targetIpObject,logger);
            if(responseObject.getString("type").equals("error")){
                System.out.println("type: error");
                System.out.println("code: "+ responseObject.getInteger("code")+" "+"message: "+responseObject.getString("message"));
                return;
            }else {
                System.out.println("开始收集通信数据");
            }
        }
        if(templateObject.containsKey("analysis")){
            JsonObject paramsForAnalysis = CollectByOrder.collectAnalysisByOrder(templateObject);
            Object contentObject = paramsForAnalysis.getValue("content");
            System.out.println("分析为配置："+ contentObject);
            Object flagObject = paramsForAnalysis.getValue("flag");
            System.out.println("分析flag："+ flagObject);
            Object targetIpObject = paramsForAnalysis.getValue("targetIP");
            System.out.println("分析IP："+ targetIpObject);
            JsonObject responseObject = new AnalysisTemplate().analysisParse(contentObject,flagObject,targetIpObject,logger);
            if(responseObject.getString("type").equals("error")){
                System.out.println("type: error");
                System.out.println("code: "+ responseObject.getInteger("code")+" "+"message: "+responseObject.getString("message"));
                return;
            }else {
                System.out.println("开始收集分析数据");
            }
        }
        System.out.println("请输入一条指令！");
        System.out.print("--->");//提示输入指令
        input = scanner.nextLine();
        if(input.toUpperCase().indexOf("COLLECT".toUpperCase()) >=  0 ){
            executionCommunication(input,scanner,vertx);
        }else if(input.toUpperCase().indexOf("SHOW".toUpperCase()) >= 0 ){
            executionShow(vertx);
        }else {
            System.out.println("指令格式错误。请重新输入");
            System.out.println("请输入一条指令！");
            System.out.print("--->");//提示输入指令
            input = scanner.nextLine();
            if(input.toUpperCase().indexOf("COLLECT".toUpperCase()) >=  0 ){
                executionCommunication(input,scanner,vertx);
            }else if(input.toUpperCase().indexOf("SHOW".toUpperCase()) >= 0 ){
                executionShow(vertx);
            }else {
                System.out.println("指令格式错误。请重新输入");

            }


        }

    }

    public static void executionShow(Vertx vertx){
        System.out.println("开始展示数据");
//                    Scanner scanner1 = new Scanner(System.in);
//                    String escString = scanner1.toString();//是否键入esc
//                Calendar calendar = Calendar.getInstance();
//                SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                String currentTime = dateFormat.format(calendar.getTime());//获取当前时间



        long timeID= vertx.setPeriodic(30,r->{
//
//                        if(escString.toUpperCase().indexOf("esc".toUpperCase()) < 0){
//                            vertx.cancelTimer(r);//键入esc或者ESC,停止展示
//                        };
            String query = "SELECT * FROM detrcd_latest_table;";

            String databaseConfFile = Config.getDatabaseConfFile();
            final JsonObject config = new JsonObject(StringUtil.readFileToString(databaseConfFile));

            SQLClient mySQLClient = new MysqlConnection().getMySQLClient();
            mySQLClient.query(query,re->{
                if(re.failed()){
                    System.out.println("type: error");
                    System.out.println("code: "+QUERY_FAILURE.getCode()+" "+"message: "+QUERY_FAILURE.getMsg());
                }else {

                    JsonObject currentRecord = re.result().getRows().get(0);

                    String idCurrent = currentRecord.getString("drt_id");

                    if( !idCurrent.equals(idPrevious)){
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
