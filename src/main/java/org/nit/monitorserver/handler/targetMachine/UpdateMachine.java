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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.nit.monitorserver.constant.ResponseError.*;

/**
 * 功能描述: <修改目标机信息>
 * 〈〉
 * @Author: 20643
 * @Date: 2020-9-1 14:22
 */
public class UpdateMachine extends AbstractRequestHandler {

    protected static final Logger logger = Logger.getLogger(UpdateMachine.class);
//    private final SQLClient mySQLClient = new MysqlConnection().getMySQLClient();
    private final MongoClient mongoClient = new MongoConnection().getMongoClient();

    @Override
    public void handle(final RoutingContext routingContext, final Request request) {

        routingContext.response().putHeader(HttpHeaderContentType.CONTENT_TYPE_STR, HttpHeaderContentType.JSON);
        ResponseFactory response = new ResponseFactory(routingContext, request);

//        int id = request.getParams().getInteger(TARGET_ID);
        String id = request.getParams().getString("id");
        System.out.println("id:"+id);
        String name = request.getParams().getString("name");
        String ip = request.getParams().getString("ip");
        String os = request.getParams().getString("os");

        // 验证非空
        if (id == null || id .equals("")) {
            logger.error(String.format("insert exception: %s", "目标机id为必填参数"));
            response.error(ID_IS_REQUIRED.getCode(), ID_IS_REQUIRED.getMsg());
            return;
        }

        JsonObject updateCondition = new JsonObject().put("id",id);//定位

        JsonObject updateElement = new JsonObject();//
        if (!name.equals("") && name != null) {
            updateElement.put("name",name);

        }
        if (!ip.equals("") && ip != null) {
            updateElement.put("ip",ip);
        }
        if(!os.equals("") && os != null){
            updateElement.put("os",os);
        }

        JsonObject updateObject = new JsonObject().put("$set",updateElement);



//        String name = request.getParams().getString(TARGET_NAME);
//        String ip = request.getParams().getString(TARGET_IP);
//        String os = request.getParams().getString(TARGET_OS);

        //System.out.println(id + name + ip + os);

        // 验证非空
//        if (name == "" || name == null) {
//            logger.info(String.format("insert exception: %s", "目标机名称为必填参数"));
//            response.error(NAME_IS_REQUIRED.getCode(), NAME_IS_REQUIRED.getMsg());
//            return;
//        }
//        if (ip == "" || ip == null) {
//            logger.info(String.format("insert exception: %s", "目标机ip为必填参数"));
//            response.error(IP_IS_REQUIRED.getCode(), IP_IS_REQUIRED.getMsg());
//            return;
//        }
//        if (!isIPLegal(ip)) {
//            logger.info(String.format("insert exception: %s", "ip不合法"));
//            response.error(IP_IS_ILLEGAL.getCode(), IP_IS_ILLEGAL.getMsg());
//            return;
//        }




//        mongoClient.find("targetMachine",updateCondition,r->{
//            if(r.failed()){
//                logger.error(String.format("search targetMachine: %s 查找失败", Tools.getTrace(r.cause())));
//                response.error(QUERY_FAILURE.getCode(), QUERY_FAILURE.getMsg());
//                return;
//            }else if(r.result().size()==0){
//                logger.error(String.format("search targetMachine: %s 查找失败", "该记录已存在"));
//                response.error(RECORD_EXISTED.getCode(), RECORD_EXISTED.getMsg());
//                return;
//            }
//            JsonObject resultObtained = r.result().get(0);
//            if (!name.equals("") && name != null) {
//                resultObtained.put("name",name);
//
//            }
//            if (!ip.equals("") && ip != null) {
//                resultObtained.put("ip",ip);
//            }
//            if(!os.equals("") && os != null){
//                resultObtained.put("os",os);
//            }
//            mongoClient.save("targetMachine",resultObtained,r->{
//
//            });
//
//
//        });

        mongoClient.findOneAndUpdate("targetMachine",updateCondition,updateObject,r->{
            if(r.failed()) {
                logger.error(String.format("Update targetMachine exception: %s", Tools.getTrace(r.cause())));
                response.error(UPDATE_TARGET_ERROR.getCode(), UPDATE_TARGET_ERROR.getMsg());
                return;
            }

            logger.info("修改目标机成功："+ id);
            JsonObject result=new JsonObject();
            response.success(result);
        });


//
//        String sql = "UPDATE" + TABLE_NAME +
//                "SET" + BASE_TARGET_NAME + "= ?," +
//                         BASE_TARGET_OS + "= ? " +
//                "WHERE" + BASE_TARGET_IP + "= ?" ;
//
//        JsonArray params = new JsonArray().add(name.toString()).add(os.toString()).add(ip.toString());
//
//        mySQLClient.updateWithParams(sql, params, ar -> {
//            System.out.println(sql);
//            if (ar.failed()) {
//                logger.error(String.format("UpdateProject target exception: %s", Tools.getTrace(ar.cause())));
//                response.error(UPDATE_TARGET_ERROR.getCode(), UPDATE_TARGET_ERROR.getMsg());
//                return;
//            }
//
//            logger.info("修改成功");
//            JsonObject result=new JsonObject();
//            result.put("result","目标机信息修改成功！");
//            response.success(result);
//
//        });

    }

    public static boolean isIPLegal(String ipStr) {

        String regexStr = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."

                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."

                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."

                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";

        Pattern pattern = Pattern.compile(regexStr);    //编译正则表达式

        Matcher matcher = pattern.matcher(ipStr);    // 创建给定输入模式的匹配器

        return matcher.matches();

    }
}
