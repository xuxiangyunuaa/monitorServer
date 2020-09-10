package org.nit.monitorserver.handler.targetMachine;

import static org.nit.monitorserver.constant.ResponseError.*;

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

    @Override
    public void handle(final RoutingContext routingContext, final Request request) {

        routingContext.response().putHeader(HttpHeaderContentType.CONTENT_TYPE_STR, HttpHeaderContentType.JSON);
        ResponseFactory response = new ResponseFactory(routingContext, request);

        String id = request.getParams().getString("id");

        if(id == null || id.equals("")){
            logger.error(String.format("insert exception: %s", "目标机id为必填参数"));
            response.error(ID_IS_REQUIRED.getCode(), ID_IS_REQUIRED.getMsg());
            return;
        }
        JsonObject removeCondition = new JsonObject().put("id",id);
        mongoClient.removeDocuments("targetMachine",removeCondition,r->{
            if(r.failed()){
                logger.error(String.format("target delete exception: %s", Tools.getTrace(r.cause())));
                response.error(DELETE_TARGET_ERROR.getCode(), DELETE_TARGET_ERROR.getMsg());
                return;
            }
            logger.info("删除成功目标机成功："+id);
            JsonObject result=new JsonObject();
            response.success(result);

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
