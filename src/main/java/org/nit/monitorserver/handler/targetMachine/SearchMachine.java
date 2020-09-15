package org.nit.monitorserver.handler.targetMachine;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.RoutingContext;
import org.apache.log4j.Logger;
import org.nit.monitorserver.constant.HttpHeaderContentType;
import org.nit.monitorserver.database.MongoConnection;
import org.nit.monitorserver.handler.log.CreateLog;
import org.nit.monitorserver.handler.task.Create;
import org.nit.monitorserver.message.AbstractRequestHandler;
import org.nit.monitorserver.message.Request;
import org.nit.monitorserver.message.ResponseFactory;
import org.nit.monitorserver.util.FormValidator;
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
    CreateLog createLog = new CreateLog();

    @Override
    public void handle(RoutingContext routingContext, Request request) {

        routingContext.response().putHeader(HttpHeaderContentType.CONTENT_TYPE_STR, HttpHeaderContentType.JSON);
        ResponseFactory response = new ResponseFactory(routingContext, request);

        Object nameObject = request.getParams().getValue("name");
        Object ipObject = request.getParams().getValue("ip");
        Object osObject = request.getParams().getValue("os");

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
        //name
        if(nameObject != null && !nameObject.toString().equals("")){
            if(!FormValidator.isString(nameObject)){
                logger.error(String.format("search targetMachine exception: %s", "目标机名称格式错误"));
                response.error(TARGETMACHINENAME_FORMAT_ERROR.getCode(), TARGETMACHINENAME_FORMAT_ERROR.getMsg());
                createLog.createLogRecord("目标机管理","error","查找目标机","目标机名称格式错误");
                return;
            }
            String name = nameObject.toString();
            condition.put("name",name);
        }
        //ip
        if(ipObject != null && !ipObject.toString().equals("")){
            if(!FormValidator.isString(ipObject)){
                logger.error(String.format("search targetMachine exception: %s", "目标机IP格式错误"));
                response.error(TARGETMACHINEIP_FORMAT_ERROR.getCode(), TARGETMACHINEIP_FORMAT_ERROR.getMsg());
                createLog.createLogRecord("目标机管理","error","查找目标机","目标机IP格式错误");
                return;
            }
            String ip = ipObject.toString();
            condition.put("ip",ip);
        }
        //os
        if(osObject != null && !osObject.toString().equals("")){
            if(!FormValidator.isString(osObject)){
                logger.error(String.format("search targetMachine exception: %s", "目标机OS格式错误"));
                response.error(TARGETMACHINEOS_FORMAT_ERROR.getCode(), TARGETMACHINEOS_FORMAT_ERROR.getMsg());
                createLog.createLogRecord("目标机管理","error","查找目标机","目标机OS格式错误");
                return;
            }
            String os = osObject.toString();
            condition.put("os",os);
        }


        //根据条件确定sql

        mongoClient.find("targetMachine",condition,r->{
            if(r.failed()){
                logger.error(String.format("find target exception: %s", Tools.getTrace(r.cause())));
                response.error(DATA_QUERY_ERROR.getCode(), DATA_QUERY_ERROR.getMsg());
                createLog.createLogRecord("目标机管理","error","查找目标机","目标机查找失败");
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
            createLog.createLogRecord("目标机管理","info","查找目标机","目标机查找成功");
            return;




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
