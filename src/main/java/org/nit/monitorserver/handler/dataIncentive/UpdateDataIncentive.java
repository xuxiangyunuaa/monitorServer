package org.nit.monitorserver.handler.dataIncentive;

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

import static org.nit.monitorserver.constant.ResponseError.INCENTIVEID;
import static org.nit.monitorserver.constant.ResponseError.PROJECTID;
import static org.nit.monitorserver.constant.ResponseError.UPDATE_FAILURE;

public class UpdateDataIncentive extends AbstractRequestHandler {
    protected static final Logger logger = Logger.getLogger(UpdateDataIncentive.class);
    private final MongoClient mongoClient = new MongoConnection().getMongoClient();
    @Override
    public void handle(RoutingContext routingContext, Request request) throws IOException, Exception {
        routingContext.response().putHeader(HttpHeaderContentType.CONTENT_TYPE_STR, HttpHeaderContentType.JSON);
        ResponseFactory response = new ResponseFactory(routingContext, request);
        String id = request.getParams().getString("id");
        String name = request.getParams().getString("name");
        Object dataObject = request.getParams().getValue("data");
        System.out.println("data"+dataObject);
        String period = request.getParams().getString("period");
        String port = request.getParams().getString("port");
        String targetIP = request.getParams().getString("targetIP");

        if(id == null || id.equals("")){
            logger.error(String.format("update exception: %s", "激励任务id为必填参数"));
            response.error(INCENTIVEID.getCode(), INCENTIVEID.getMsg());
            return;
        }
        JsonObject update = new JsonObject().put("id",id);//定位
        JsonObject updateElement = new JsonObject();
        if(!name.equals("") && name != null){
            updateElement.put("name",name);
        }
        if(dataObject != null ){
            if (FormValidator.isJsonObject(dataObject)){
                JsonObject data = (JsonObject)dataObject;
                updateElement.put("data",data);
            }

        }
        if(period != null && !period.equals("")){
            updateElement.put("period",period);

        }
        if(port != null && !port.equals("")){
            updateElement.put("port",port);

        }
        if( targetIP !=null &&!targetIP.equals("")){
            updateElement.put("targetIP",targetIP);
        }
        System.out.println(updateElement);

        JsonObject updateObject = new JsonObject().put("$set",updateElement);
        System.out.println("updateObject"+updateObject);

        mongoClient.findOneAndUpdate("dataIncentive",update,updateObject,r->{
            if(r.failed()){
                logger.error(String.format("UpdateProject dataIncentive exception: %s", Tools.getTrace(r.cause())));
                response.error(UPDATE_FAILURE.getCode(), UPDATE_FAILURE.getMsg());
                return;
            }
            JsonObject result = new JsonObject();
            response.success(result);
            logger.info("激励数据更新成功："+id);
        });



    }
}
