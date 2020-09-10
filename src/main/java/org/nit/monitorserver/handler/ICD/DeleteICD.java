package org.nit.monitorserver.handler.ICD;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.RoutingContext;
import org.apache.log4j.Logger;
import org.nit.monitorserver.constant.HttpHeaderContentType;
import org.nit.monitorserver.database.MongoConnection;
import org.nit.monitorserver.message.AbstractRequestHandler;
import org.nit.monitorserver.message.Request;
import org.nit.monitorserver.message.ResponseFactory;

import java.io.IOException;

import static org.nit.monitorserver.constant.ResponseError.*;

/**
 * @ClassName DeleteProject
 * @Description TODO删除ICD
 * @Author 20643
 * @Date 2020-9-1 14:49
 * @Version 1.0
 **/
public class DeleteICD extends AbstractRequestHandler {
    protected static final Logger logger = Logger.getLogger(DeleteICD.class);
    private final MongoClient mongoClient = new MongoConnection().getMongoClient();

    @Override
    public void handle(final RoutingContext routingContext,final Request request) throws IOException {
        routingContext.response().putHeader(HttpHeaderContentType.CONTENT_TYPE_STR, HttpHeaderContentType.JSON);
        ResponseFactory response = new ResponseFactory(routingContext, request);
        String id = request.getParams().getString("id");
        if(id == null ||  id.equals("")){
            logger.error(String.format("delete exception: %s", "ICD的id为必填参数"));
            response.error(ICDID_IS_REQUIRED.getCode(), ICDID_IS_REQUIRED.getMsg());
            return;
        }
        JsonObject removeObject = new JsonObject().put("id",id);
        mongoClient.removeDocuments("ICD",removeObject,r->{
            if(r.failed()){
                logger.error(String.format("delete ICD: %s 删除失败",id));
                response.error(DELETE_FAILURE.getCode(), DELETE_FAILURE.getMsg());
                return;
            }
            JsonObject result = new JsonObject();
            response.success(result);
        });


    }
}
