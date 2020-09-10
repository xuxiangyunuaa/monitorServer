package org.nit.monitorserver.handler.targetMachine;

import io.vertx.core.json.JsonArray;
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

import java.util.List;

import static org.nit.monitorserver.constant.ResponseError.*;
import static org.nit.monitorserver.constant.TargetManageConsts.*;
/**
 * 功能描述: <查找目标机>
 * 〈〉
 * @Author: 20643
 * @Date: 2020-9-1 14:20
 */
public class SearchMachine extends AbstractRequestHandler {

    protected static final Logger logger = Logger.getLogger(SearchMachine.class);
//    private final SQLClient mySQLClient = new MysqlConnection().getMySQLClient();
    private final MongoClient mongoClient = new MongoConnection().getMongoClient();

    @Override
    public void handle(RoutingContext routingContext, Request request) {

        routingContext.response().putHeader(HttpHeaderContentType.CONTENT_TYPE_STR, HttpHeaderContentType.JSON);
        ResponseFactory response = new ResponseFactory(routingContext, request);

        String name = request.getParams().getString("name");
        String ip = request.getParams().getString("ip");
        String os = request.getParams().getString("os");

//        System.out.println(search_type);
//        System.out.println(search_str);

//
//        String sql_name = "SELECT * FROM " + TABLE_NAME + "WHERE "
//                + BASE_TARGET_NAME +
//                " LIKE " + "'%" + search_str + "%' ; ";
//        String sql_ip =  "SELECT * FROM " + TABLE_NAME + "WHERE "
//                + BASE_TARGET_IP +
//                " LIKE " + "'%" + search_str + "%' ; ";
//        String sql_os =  "SELECT * FROM " + TABLE_NAME + "WHERE "
//                + BASE_TARGET_OS +
//                " LIKE " + "'%" + search_str + "%' ; ";
//
//        String sql = "";


        JsonObject condition = new JsonObject();

        if(!name.equals("") && name != null){
            condition.put("name",name);
        }
        if(!ip.equals("") && ip != null){
            condition.put("ip",ip);
        }
        if(!os.equals("") && os != null){
            condition.put("os",os);
        }

        //根据条件确定sql

        mongoClient.find("targetMachine",condition,r->{
            if(r.failed()){
                logger.error(String.format("find target exception: %s", Tools.getTrace(r.cause())));
                response.error(DATA_QUERY_ERROR.getCode(), DATA_QUERY_ERROR.getMsg());
                return;
            }
            JsonArray resultData = new JsonArray();
            List<JsonObject> dataList = r.result();
            for (int i = 0; i < dataList.size(); i++) {
                JsonObject jsonArray = dataList.get(i);
                //System.out.println(jsonArray);
                resultData.add(new JsonObject().put(TARGET_NAME, jsonArray.getValue("name"))
                        .put(TARGET_IP, jsonArray.getValue("ip"))
                        .put(TARGET_OS, jsonArray.getValue("os"))
                        .put("id",jsonArray.getValue("id")));
            }

            JsonObject result = new JsonObject();
            result.put("machineList", resultData);
            response.success(result);



        });




//        mySQLClient.query(sql, ar -> {
//
//            if (ar.failed()) {
//                response.error(DATA_QUERY_ERROR.getCode(), DATA_QUERY_ERROR.getMsg());
//                return;
//            }
//
//            JsonArray resultData = new JsonArray();
//            List<JsonArray> dataList = ar.result().getResults();
//            for (int i = 0; i < dataList.size(); i++) {
//                JsonArray jsonArray = dataList.get(i);
//                //System.out.println(jsonArray);
//                resultData.add(new JsonObject().put(TARGET_NAME, jsonArray.getValue(0))
//                        .put(TARGET_IP, jsonArray.getValue(1))
//                        .put(TARGET_OS, jsonArray.getValue(2)));
//            }
//
//            JsonObject result = new JsonObject();
//            result.put("machineList", resultData);
//            response.success(result);
//
//        });

    }

}
