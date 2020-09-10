//package org.nit.monitorserver.handler.targetMachine;
//
//import io.vertx.core.json.JsonArray;
//import io.vertx.ext.sql.SQLClient;
//import io.vertx.ext.web.RoutingContext;
//import org.apache.log4j.Logger;
//import org.nit.monitorserver.constant.HttpHeaderContentType;
//import org.nit.monitorserver.database.MysqlConnection;
//import org.nit.monitorserver.message.AbstractRequestHandler;
//import org.nit.monitorserver.message.Request;
//import org.nit.monitorserver.message.ResponseFactory;
//import org.nit.monitorserver.util.Tools;
//
//import static org.nit.monitorserver.constant.ResponseError.*;
//import static org.nit.monitorserver.constant.TargetManageConsts.*;
///**
// * 功能描述: <创建指令集这个没用了>
// * 〈〉
// * @Author: 20643
// * @Date: 2020-9-1 14:23
// */
//public class createCommandSet extends AbstractRequestHandler {
//
//    protected static final Logger logger = Logger.getLogger(createCommandSet.class);
//    private final SQLClient mySQLClient = new MysqlConnection().getMySQLClient();
//
//    @Override
//    public void handle(final RoutingContext routingContext,final Request request) {
//
//        //System.out.println("00000000");
//
//        routingContext.response().putHeader(HttpHeaderContentType.CONTENT_TYPE_STR, HttpHeaderContentType.JSON);
//        ResponseFactory response = new ResponseFactory(routingContext, request);
//
//
//        String name = request.getParams().getString(COMMAND_NAME); //获取指令集名称
//        String content = request.getParams().getString(COMMAND_CONTENT); //获取指令集内容
//
//        // 验证非空
//        if (name == "" || name == null) {
//            logger.info(String.format("insert exception: %s", "指令集名称为必填参数"));
//            response.error(COMMANDNAME_IS_REQUIRED.getCode(), COMMANDNAME_IS_REQUIRED.getMsg());
//            return;
//        }
//        if (content == "" || content == null) {
//            logger.info(String.format("insert exception: %s", "指令集内容为必填参数"));
//            response.error(COMMANDCONTENT_IS_REQUIRED.getCode(), COMMANDCONTENT_IS_REQUIRED.getMsg());
//            return;
//        }
//
//        //插入操作
//        String sql = " INSERT INTO " + COMMANDTABLE_NAME + "VALUES (NULL, ?, ?) ;";
//        //System.out.println("1");
//
//        JsonArray params = new JsonArray().add(name).add(content);
//        //System.out.println("2");
//
//        mySQLClient.queryWithParams(sql, params, ar -> {
//
//            if (ar.failed()) {
//                logger.error(String.format("new command insert exception: %s", Tools.getTrace(ar.cause())));
//                response.error(NEW_COMMAND_ERROR.getCode(), NEW_COMMAND_ERROR.getMsg());
//                return;
//            }
//
//            logger.info("新建指令集插入成功");
//            response.success(null);
//
//        });
//
//    }
//
//}
