package org.nit.monitorserver.handler.task;

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

import java.io.IOException;

import static org.nit.monitorserver.constant.ResponseError.DEFAULTCHECKED;
import static org.nit.monitorserver.constant.ResponseError.*;

public class Create extends AbstractRequestHandler {

    protected static final Logger logger = Logger.getLogger(Create.class);
    //    private final SQLClient mySQLClient = new MysqlConnection().getMySQLClient();
    private final MongoClient mongoClient = new MongoConnection().getMongoClient();

    @Override
    public void handle(RoutingContext routingContext, Request request) throws IOException, Exception {
        routingContext.response().putHeader(HttpHeaderContentType.CONTENT_TYPE_STR, HttpHeaderContentType.JSON);
        ResponseFactory response = new ResponseFactory(routingContext, request);

        JsonObject createObject = new JsonObject();

        String taskName = request.getParams().getString("taskName");
        if(!taskName.equals("") && taskName != null){
            createObject.put("taskName",taskName);
        }
        String targetIP = request.getParams().getString("targetIP");
        if(targetIP.equals("") || targetIP == null){
            logger.error(String.format("create exception: %s", "目标机ip为必填参数"));
            response.error(IP_IS_REQUIRED.getCode(), IP_IS_REQUIRED.getMsg());
            return;
        }
        createObject.put("targetIP",targetIP);
        String ICDId = request.getParams().getString("ICDId");//ICD的id,可选参数
        if(!ICDId.equals("") && ICDId != null){
            createObject.put("ICDId",ICDId);
        }
        JsonArray defaultChecked = request.getParams().getJsonArray("defaultChecked");
        if(defaultChecked == null){
            logger.error(String.format("create exception: %s", "配置参数为必填参数"));
            response.error(DEFAULTCHECKED.getCode(), DEFAULTCHECKED.getMsg());
            return;
        }
        createObject.put("defaultChecked",defaultChecked);
        JsonObject tdrCfg = request.getParams().getJsonObject("tdrCfg");
        if(tdrCfg != null){
            createObject.put("tdrCfg",tdrCfg);
        }
        JsonObject anaCfg = request.getParams().getJsonObject("anaCfg");
        if(anaCfg != null){
            createObject.put("anaCfg",anaCfg);
        }

        mongoClient.find("task",createObject,re->{
            if(re.failed()){
                logger.error(String.format("search 采集任务: %s 查找失败", Tools.getTrace(re.cause())));
                response.error(QUERY_FAILURE.getCode(), QUERY_FAILURE.getMsg());
                return;
            }else if(re.result().size()== 0){
                String id = Tools.generateId();
                createObject.put("id",id);

                mongoClient.insert("task",createObject,r->{
                    if(r.failed()){
                        logger.error(String.format("new task insert exception: %s", Tools.getTrace(r.cause())));
                        response.error(INSERT_FAILURE.getCode(), INSERT_FAILURE.getMsg());
                        return;
                    }
                    JsonObject result = new JsonObject().put("id",id);
                    response.success(result);
                    logger.info("采集任务创建成功："+ id);
                    return;

                });

            }else {
                response.error(RECORD_EXISTED.getCode(), RECORD_EXISTED.getMsg());
                logger.error(String.format("new task insert exception: %s", "该记录已经存在"));
                return;
            }
        }
        );




    }
}
