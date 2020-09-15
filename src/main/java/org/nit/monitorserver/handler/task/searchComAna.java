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
import org.nit.monitorserver.util.FormValidator;
import org.nit.monitorserver.util.Tools;

import java.io.IOException;

import static org.nit.monitorserver.constant.ResponseError.QUERY_FAILURE;
import static org.nit.monitorserver.constant.ResponseError.TASKID;
import static org.nit.monitorserver.constant.ResponseError.TASKID_FORMAT_ERROR;
import static org.nit.monitorserver.constant.ResponseError.*;

/**
 * @author 20817
 * @version 1.0
 * @className searchComAna
 * @description
 * @date 2020/9/13 20:12
 */
public class searchComAna extends AbstractRequestHandler {
    protected static final Logger logger = Logger.getLogger(SearchTask.class);
    //    private final SQLClient mySQLClient = new MysqlConnection().getMySQLClient();
    private final MongoClient mongoClient = new MongoConnection().getMongoClient();

    @Override
    public void handle(RoutingContext routingContext, Request request) throws IOException, Exception {
        routingContext.response().putHeader(HttpHeaderContentType.CONTENT_TYPE_STR, HttpHeaderContentType.JSON);
        ResponseFactory response = new ResponseFactory(routingContext, request);

        Object idObject = request.getParams().getValue("id");//采集任务id
        if(idObject == null || idObject.toString().equals("")){
            logger.error(String.format("search comAna exception: %s", "采集任务id为必填参数"));
            response.error(TASKID.getCode(), TASKID.getMsg());
            return;
        }
        if(!FormValidator.isString(idObject)){
            logger.error(String.format("search exception: %s", "采集任务id为必填参数"));
            response.error(TASKID_FORMAT_ERROR.getCode(), TASKID_FORMAT_ERROR.getMsg());
            return;
        }
        String id = (String) idObject;

        JsonObject searchObject = new JsonObject().put("id",id);
        mongoClient.find("task",searchObject,r->{
            if(r.failed()){
                logger.error(String.format("search comAna: %s 查找失败", Tools.getTrace(r.cause())));
                response.error(QUERY_FAILURE.getCode(), QUERY_FAILURE.getMsg());
                return;
            }else if(r.result().size() == 0){
                logger.info(String.format("采集任务：% 不存在",id));
                response.error(RECORD_NOT_EXISTED.getCode(), RECORD_NOT_EXISTED.getMsg());
                return;
            }
            JsonObject element = r.result().get(0);
            JsonObject resultList = new JsonObject();
            Object tdrCfgObject = element.getValue("tdrCfg");
            if(tdrCfgObject != null && !tdrCfgObject.toString().equals("{}")){
                if(FormValidator.isJsonObject(tdrCfgObject)){
                    JsonObject tdrCfg = (JsonObject) tdrCfgObject;
                    resultList.put("tdrCfg",tdrCfg);
                }

            }

            Object anaCfgObject = element.getValue("anaCfg");
            if(anaCfgObject != null && !anaCfgObject.toString().equals("{}")){
                if(FormValidator.isJsonObject(anaCfgObject)){
                    JsonObject anaCfg = (JsonObject) anaCfgObject;
                    resultList.put("anaCfg",anaCfg);
                }

            }
            response.success(resultList);
            logger.info(String.format("获取采集任务：%s 的通信分析配置数成功",id));
            return;

        });
    }
}
