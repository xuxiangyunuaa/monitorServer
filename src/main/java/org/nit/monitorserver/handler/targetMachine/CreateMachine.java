package org.nit.monitorserver.handler.targetMachine;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.RoutingContext;
import org.apache.log4j.Logger;
import org.nit.monitorserver.constant.HttpHeaderContentType;
import org.nit.monitorserver.database.MongoConnection;
import org.nit.monitorserver.message.AbstractRequestHandler;
import org.nit.monitorserver.message.Request;
import org.nit.monitorserver.message.ResponseFactory;
import org.nit.monitorserver.util.Tools;

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


    @Override
    public void handle(final RoutingContext routingContext, final Request request) {

        //System.out.println("00000000");

        routingContext.response().putHeader(HttpHeaderContentType.CONTENT_TYPE_STR, HttpHeaderContentType.JSON);
        ResponseFactory response = new ResponseFactory(routingContext, request);

        //日志管理
//        mongoClient.insert("log",)

        //int id = request.getParams().getInteger(TARGET_ID);        //创建目标机不填写id
        String name = request.getParams().getString(TARGET_NAME);
        String ip = request.getParams().getString(TARGET_IP);
        String os = request.getParams().getString(TARGET_OS);
        System.out.println("name:"+name);
        System.out.println("ip:"+ip);
        System.out.println("os:"+os);

        // 验证非空
        if ( name == null || name.equals("")) {
            logger.error(String.format("insert exception: %s", "目标机名称为必填参数"));
            response.error(NAME_IS_REQUIRED.getCode(), NAME_IS_REQUIRED.getMsg());
            return;
        }
        if ( ip == null || ip.equals("")) {
            logger.error(String.format("insert exception: %s", "目标机ip为必填参数"));
            response.error(IP_IS_REQUIRED.getCode(), IP_IS_REQUIRED.getMsg());
            return;
        }

        String id = Tools.generateId();
        JsonObject insertCondition = new JsonObject()
                .put("id",id)
                .put("name",name)
                .put("ip",ip)
                .put("os",os);
        JsonObject searchObject = new JsonObject().put("name",name).put("ip",ip).put("os",os);
        mongoClient.find("targetMachine",searchObject,re->{
            if(re.failed()){
                logger.error(String.format("search targetMachine: %s 查找失败", Tools.getTrace(re.cause())));
                response.error(QUERY_FAILURE.getCode(), QUERY_FAILURE.getMsg());
                return;
            }else if(re.result().size() == 0){
                mongoClient.insert("targetMachine",insertCondition,r->{
                    if(r.failed()){
                        logger.error(String.format("new targetMachne insert exception: %s", Tools.getTrace(r.cause())));
                        response.error(NEW_TARGET_ERROR.getCode(), NEW_TARGET_ERROR.getMsg());
                        return;
                    }
                    JsonObject result=new JsonObject();
                    result.put("id",id);
                    logger.info("创建目标机成功:"+id);
                    response.success(result);
                });
            }else {
                response.error(RECORD_EXISTED.getCode(),RECORD_EXISTED.getMsg());
                logger.error(String.format("new target Machine insert exception: %s","该记录已经存在"));
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
