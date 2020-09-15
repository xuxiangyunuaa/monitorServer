package org.nit.monitorserver.handler.targetMachine;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.RoutingContext;
import org.apache.log4j.Logger;
import org.nit.monitorserver.constant.HttpHeaderContentType;
import org.nit.monitorserver.database.MongoConnection;
import org.nit.monitorserver.handler.log.CreateLog;
import org.nit.monitorserver.message.AbstractRequestHandler;
import org.nit.monitorserver.message.Request;
import org.nit.monitorserver.message.ResponseFactory;
import org.nit.monitorserver.util.FormValidator;
import org.nit.monitorserver.util.Tools;

import java.util.concurrent.ForkJoinPool;

import static org.nit.monitorserver.constant.ResponseError.*;
import static org.nit.monitorserver.constant.TargetManageConsts.*;

//import jdk.nashorn.internal.runtime.regexp.joni.Regex;

/**
 * 功能描述: <创建目标机>
 * 〈〉
 * @Author: 20643
 * @Date: 2020-9-1 14:19
 */
public class CreateMachine extends AbstractRequestHandler {

    protected static final Logger logger = Logger.getLogger(CreateMachine.class);
//    private final SQLClient mySQLClient = new MysqlConnection().getMySQLClient();
    private final MongoClient mongoClient = new MongoConnection().getMongoClient();
    CreateLog createLog = new CreateLog();


    @Override
    public void handle(final RoutingContext routingContext, final Request request) {

        //System.out.println("00000000");

        routingContext.response().putHeader(HttpHeaderContentType.CONTENT_TYPE_STR, HttpHeaderContentType.JSON);
        ResponseFactory response = new ResponseFactory(routingContext, request);

        JsonObject insertObject = new JsonObject();




        //日志管理
//        mongoClient.insert("log",)

        //int id = request.getParams().getInteger(TARGET_ID);        //创建目标机不填写id
        Object nameObject = request.getParams().getValue(TARGET_NAME);
        Object ipObject = request.getParams().getValue(TARGET_IP);
        Object osObject = request.getParams().getValue(TARGET_OS);


        // 验证非空
        if ( nameObject == null || nameObject.toString().equals("")) {
            logger.error(String.format("create targetMachine exception: %s", "目标机名称为必填参数"));
            response.error(NAME_IS_REQUIRED.getCode(), NAME_IS_REQUIRED.getMsg());
            createLog.createLogRecord("目标机管理","error","新建目标机","目标机名称为必填参数");
            return;
        }
        if(!FormValidator.isString(nameObject)){
            logger.error(String.format("create targetMachine exception: %s", "目标机名称格式错误"));
            response.error(TARGETMACHINENAME_FORMAT_ERROR.getCode(), TARGETMACHINENAME_FORMAT_ERROR.getMsg());
            createLog.createLogRecord("目标机管理","error","新建目标机","目标机名称格式错误");
            return;
        }
        String name = nameObject.toString();
        insertObject.put("name",name);

        //ip
        if ( ipObject == null || ipObject.toString().equals("")) {
            logger.error(String.format("create targetMachine exception: %s", "目标机IP为必填参数"));
            response.error(IP_IS_REQUIRED.getCode(), IP_IS_REQUIRED.getMsg());
            createLog.createLogRecord("目标机管理","error","新建目标机","目标机IP为必填参数");
            return;
        }
        if(!FormValidator.isString(ipObject)){
            logger.error(String.format("create targetMachine exception: %s", "目标机IP格式错误"));
            response.error(TARGETIP_FORMAT_ERROR.getCode(), TARGETIP_FORMAT_ERROR.getMsg());
            createLog.createLogRecord("目标机管理","error","新建目标机","目标机IP格式错误");

            return;
        }
        String ip = ipObject.toString();
        insertObject.put("ip",ip);

        //OS
        if(osObject !=null && !osObject.toString().equals("")){
            if(!FormValidator.isString(osObject)){
                logger.error(String.format("create targetMachine exception: %s", "目标机OS格式错误"));
                response.error(TARGETMACHINEOS_FORMAT_ERROR.getCode(), TARGETMACHINEOS_FORMAT_ERROR.getMsg());
                createLog.createLogRecord("目标机管理","error","新建目标机","目标机OS格式错误");
                return;
            }
            String os=osObject.toString();
            insertObject.put("os",os);

        }




        mongoClient.find("targetMachine",insertObject,re->{//校验是否已存在
            if(re.failed()){
                logger.error(String.format("search targetMachine: %s 查找失败", Tools.getTrace(re.cause())));
                response.error(QUERY_FAILURE.getCode(), QUERY_FAILURE.getMsg());
                createLog.createLogRecord("目标机管理","error","新建目标机","数据库查询目标机失败");
                return;
            }else if(re.result().size() == 0){//记录不存在

                String id = Tools.generateId();
                insertObject.put("id",id);
                mongoClient.insert("targetMachine",insertObject,r->{
                    if(r.failed()){
                        logger.error(String.format("new targetMachne insert exception: %s", Tools.getTrace(r.cause())));
                        response.error(NEW_TARGET_ERROR.getCode(), NEW_TARGET_ERROR.getMsg());
                        createLog.createLogRecord("目标机管理","error","新建目标机","目标机:%s 插入数据库失败");
                        return;
                    }
                    createLog.createLogRecord("目标机管理","info","新建目标机",String.format("目标机:%s 插入数据库成功",id));
                    JsonObject result=new JsonObject();
                    result.put("id",id);
                    logger.info("创建目标机成功:"+id);
                    response.success(result);
                    return;
                });
            }else {
                createLog.createLogRecord("目标机管理","error","新建目标机","目标机记录已经存在");
                response.error(RECORD_EXISTED.getCode(),RECORD_EXISTED.getMsg());
                logger.error(String.format("new target Machine insert exception: %s","该记录已经存在"));
                return;
            }
        });

//        if (!isIPLegal(ip)) {
//            logger.info(String.format("insert exception: %s", "ip不合法"));
//            response.error(IP_IS_ILLEGAL.getCode(), IP_IS_ILLEGAL.getMsg());
//            return;
//        }

        //插入操作
//        String sql = " INSERT INTO " + TABLE_NAME + "VALUES (?, ?, ?) ;";
//        //System.out.println("1");
//
//        JsonArray params = new JsonArray().add(name).add(ip).add(os);
//        System.out.println(params);
        //System.out.println("2");

//        mySQLClient.queryWithParams(sql, params, ar -> {
//            System.out.println("开始插入数据库");
//
//            if (ar.failed()) {
//                logger.error(String.format("new target insert exception: %s", Tools.getTrace(ar.cause())));
//                response.error(NEW_TARGET_ERROR.getCode(), NEW_TARGET_ERROR.getMsg());
//                return;
//            }
//
//            JsonObject result=new JsonObject();
//            result.put("result","目标机"+name+"新建成功！");
//            logger.info("插入成功");
//            response.success(result);
//
//        });

    }

//    public static boolean isIPLegal(String ipStr) {
//
//        String regexStr = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
//
//                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
//
//                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
//
//                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
//
//        Pattern pattern = Pattern.compile(regexStr);    //编译正则表达式
//
//        Matcher matcher = pattern.matcher(ipStr);    // 创建给定输入模式的匹配器
//
//        return matcher.matches();
//
//    }

}
