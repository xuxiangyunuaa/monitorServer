package org.nit.monitorserver.handler.data;

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

import static org.nit.monitorserver.constant.ResponseError.*;

/**
 * @author 20817
 * @version 1.0
 * @className DeleteDataIncentive
 * @description
 * @date 2020/9/4 22:32
 */
public class DeleteData extends AbstractRequestHandler {
    protected static final Logger logger = Logger.getLogger(DeleteData.class);
    private final MongoClient mongoClient = new MongoConnection().getMongoClient();

    @Override
    public void handle(RoutingContext routingContext, Request request) throws IOException, Exception {
        routingContext.response().putHeader(HttpHeaderContentType.CONTENT_TYPE_STR, HttpHeaderContentType.JSON);
        ResponseFactory response = new ResponseFactory(routingContext, request);

        JsonObject deleteObject = new JsonObject();
        String id = request.getParams().getString("id");
        if(id.equals("") || id == null){
            logger.error(String.format("delete exception: %s", "采集数据id为必填参数"));
            response.error(DATAID.getCode(), DATAID.getMsg());
            return;
        }
        deleteObject.put("id",id);
        mongoClient.removeDocuments("collectionData",deleteObject,r->{
            if(r.failed()){
                logger.error(String.format("delete exception: %s", Tools.getTrace(r.cause())));
                response.error(DELETE_FAILURE.getCode(), DELETE_FAILURE.getMsg());
                return;
            }
            JsonObject result = new JsonObject();
            response.success(result);
        });
//        if(eventTypeObject != null){
//            if(!FormValidator.isJsonObject(eventTypeObject)){
//                logger.error(String.format("delete exception: %s", "事件id为必填参数"));
//                response.error(EVENTTYPE_FORMAT_ERROR.getCode(), EVENTTYPE_FORMAT_ERROR.getMsg());
//                return;
//            }
//            JsonObject eventType = (JsonObject) eventTypeObject;
//            JsonObject eventTypeJsonObject = new JsonObject();
//            Object eventIDObject = eventType.getInteger("eventID");
//            if(eventIDObject != null){
//                if(!FormValidator.isInteger(eventIDObject)){
//                    logger.error(String.format("delete exception: %s", "事件类型ID为必填参数"));
//                    response.error(EVENTID_FORMAT_ERROR.getCode(), EVENTID_FORMAT_ERROR.getMsg());
//                    return;
//                }
//                int eventID = (int) eventIDObject;
//                eventTypeJsonObject.put("eventID",eventID);
//            }
//            deleteObject.put("eventType",eventType);
//
//
//
//        }






    }
}
