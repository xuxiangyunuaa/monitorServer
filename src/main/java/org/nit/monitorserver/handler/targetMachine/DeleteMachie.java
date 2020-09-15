package org.nit.monitorserver.handler.targetMachine;

import static org.nit.monitorserver.constant.ResponseError.*;

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

/**
 * 功能描述: <删除目标机>
 * 〈〉
 * @Author: 20643
 * @Date: 2020-9-1 14:20
 */
public class DeleteMachie extends AbstractRequestHandler {

    protected static final Logger logger = Logger.getLogger(DeleteMachie.class);
//    private final SQLClient mySQLClient = new MysqlConnection().getMySQLClient();
    private final MongoClient mongoClient = new MongoConnection().getMongoClient();
    CreateLog createLog = new CreateLog();

    @Override
    public void handle(final RoutingContext routingContext, final Request request) {

        routingContext.response().putHeader(HttpHeaderContentType.CONTENT_TYPE_STR, HttpHeaderContentType.JSON);
        ResponseFactory response = new ResponseFactory(routingContext, request);

        //
        Object idObject = request.getParams().getValue("id");
        if(idObject == null || idObject.toString().equals("")){
            logger.error(String.format("delete exception: %s", "目标机id为必填参数"));
            response.error(ID_IS_REQUIRED.getCode(), ID_IS_REQUIRED.getMsg());
            createLog.createLogRecord("目标机管理","error","删除目标机","目标机id为必填参数");
            return;
        }
        if(!FormValidator.isString(idObject)){
            logger.error(String.format("delete exception: %s", "目标机id格式错误"));
            response.error(ID_IS_REQUIRED.getCode(), ID_IS_REQUIRED.getMsg());
            createLog.createLogRecord("目标机管理","error","删除目标机","目标机id格式错误");
            return;
        }
        String id = idObject.toString();
        JsonObject removeCondition  = new JsonObject().put("id",id);
        mongoClient.removeDocuments("targetMachine",removeCondition,r->{
            if(r.failed()){
                logger.error(String.format("target delete exception: %s", Tools.getTrace(r.cause())));
                response.error(DELETE_TARGET_ERROR.getCode(), DELETE_TARGET_ERROR.getMsg());
                createLog.createLogRecord("目标机管理","error","删除目标机",String.format("目标机记录:%s 删除失败",id));
                return;
            }

            logger.info("删除成功目标机："+id);
            JsonObject result=new JsonObject();
            response.success(result);
            createLog.createLogRecord("目标机管理","info","删除目标机",String.format("目标机记录:%s 删除成功",id));
            return;

        });



        //System.out.println(request);
//
//        String sql = "DELETE FROM " + TABLE_NAME + "WHERE " + BASE_TARGET_IP + " = ? ";
//        JsonArray params = new JsonArray().add(ip);
//
//        mySQLClient.queryWithParams(sql, params, ar -> {
//
//            if (ar.failed()) {
//                logger.error(String.format("target delete exception: %s", Tools.getTrace(ar.cause())));
//                response.error(DELETE_TARGET_ERROR.getCode(), DELETE_TARGET_ERROR.getMsg());
//                return;
//            }
//
//            logger.info("删除成功");
//            JsonObject result=new JsonObject();
//            result.put("result","目标机"+ip+"删除成功!");
//            response.success(result);
//
//        });

    }
}
