package org.nit.monitorserver.handler.dataIncentive;

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
import java.lang.reflect.Array;

import static org.nit.monitorserver.constant.ResponseError.*;

public class CreateDataIncentive extends AbstractRequestHandler {
    protected static final Logger logger = Logger.getLogger(CreateDataIncentive.class);
    private final MongoClient mongoClient = new MongoConnection().getMongoClient();

    @Override
    public void handle(RoutingContext routingContext, Request request) throws IOException, Exception {
        routingContext.response().putHeader(HttpHeaderContentType.CONTENT_TYPE_STR, HttpHeaderContentType.JSON);
        ResponseFactory response = new ResponseFactory(routingContext, request);
        String name = request.getParams().getString("name");
        Object dataObject = request.getParams().getValue("data");

        Object periodObject = request.getParams().getValue("period");
        Object portObject = request.getParams().getValue("port");
        String targetIP = request.getParams().getString("targetIP");
        if(name.equals("") || name == null){
            logger.error(String.format("create exception: %s", "激励任务名称为必填参数"));
            response.error(INCENTIVENAME.getCode(), INCENTIVENAME.getMsg());
            return;
        }
        if(dataObject == null){
            logger.error(String.format("create exception: %s", "激励数据为必填参数"));
            response.error(INCENTIVEDATA.getCode(), INCENTIVEDATA.getMsg());
            return;
        }
        if(!FormValidator.isJsonObject(dataObject)){
            logger.error(String.format("create exception: %s", "激励数据格式错误"));
            response.error(DATA_FORMAT_ERROR.getCode(), DATA_FORMAT_ERROR.getMsg());
            return;
        }
        JsonObject data = (JsonObject) dataObject;


        if(portObject == null){
            logger.error(String.format("create exception: %s", "激励的虚端口为必填参数"));
            response.error(INCENTIVEPORT.getCode(), INCENTIVEPORT.getMsg());
            return;
        }
        if(!FormValidator.isString(portObject)){
            logger.error(String.format("create exception: %s", "激励的虚端口格式错误"));
            response.error(INCENTIVEPORT_FORMAT_ERROR.getCode(), INCENTIVEPORT_FORMAT_ERROR.getMsg());
            return;
        }
        String port = portObject.toString();
        if(targetIP.equals("") || targetIP== null){
            logger.error(String.format("create exception: %s", "激励的目标机ip为必填参数"));
            response.error(INCENTIVETARGETIP.getCode(), INCENTIVETARGETIP.getMsg());
            return;
        }

        int runFlag = 0;

        JsonObject createObject = new JsonObject()
                .put("name",name)
                .put("data",data)
                .put("port",port)
                .put("targetIP",targetIP);

        if(periodObject != null){
            if(FormValidator.isString(periodObject)){
                String period =  periodObject.toString();
                createObject.put("period",period);
            }
        }
        mongoClient.find("dataIncentive",createObject,re->{
            if(re.failed()){
                logger.error(String.format("search dataIncenive: %s 查找失败", Tools.getTrace(re.cause())));
                response.error(QUERY_FAILURE.getCode(), QUERY_FAILURE.getMsg());
                return;
            }else if(re.result().size() == 0){
                String id = Tools.generateId();
                createObject.put("id",id);
                mongoClient.insert("dataIncentive",createObject,r->{
                    if(r.failed()){
                        logger.error(String.format("new dataIncentive insert exception: %s", Tools.getTrace(r.cause())));
                        response.error(INSERT_FAILURE.getCode(), INSERT_FAILURE.getMsg());
                        return;
                    }
                    response.success(new JsonObject().put("result",id));
                    logger.info("激励任务创建成功："+id);

                });

            }
        });


    }
}
