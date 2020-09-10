package org.nit.monitorserver.handler.ICD;

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
import sun.awt.geom.AreaOp;

import java.io.IOException;

import static org.nit.monitorserver.constant.ResponseError.ICDID_IS_REQUIRED;
import static org.nit.monitorserver.constant.ResponseError.*;

/**
 * @ClassName UpdateProject
 * @Description TODO修改ICD
 * @Author 20643
 * @Date 2020-9-1 14:50
 * @Version 1.0
 **/
public class UpdateICD extends AbstractRequestHandler {

    protected static final Logger logger = Logger.getLogger(UpdateICD.class);
    private final MongoClient mongoClient = new MongoConnection().getMongoClient();

    @Override
    public void handle(final RoutingContext routingContext,final Request request) throws IOException {
        routingContext.response().putHeader(HttpHeaderContentType.CONTENT_TYPE_STR, HttpHeaderContentType.JSON);
        ResponseFactory response = new ResponseFactory(routingContext, request);
        String id = request.getParams().getString("id");
        String name = request.getParams().getString("name");
        JsonArray ICD = request.getParams().getJsonArray("ICD");
        if(id.equals("") || id == null){
            logger.error(String.format("update exception: %s", "ICD的id为必填参数"));
            response.error(ICDID_IS_REQUIRED.getCode(), ICDID_IS_REQUIRED.getMsg());
            return;
        }
        JsonObject update = new JsonObject().put("id",id);//定位
        JsonObject updateObjectElement = new JsonObject();//更新
        if(!name.equals("")  && name != null){
           updateObjectElement.put("name",name);
        }
        if(ICD != null){
            updateObjectElement.put("ICD",ICD);
        }
        JsonObject updateObject = new JsonObject().put("$set",updateObjectElement);
        mongoClient.findOneAndUpdate("ICD",update,updateObject,r->{
            if(r.failed()){
                logger.error(String.format("UpdateProject ICD exception: %s", Tools.getTrace(r.cause())));
                response.error(UPDATE_FAILURE.getCode(), UPDATE_FAILURE.getMsg());
                return;
            }
            JsonObject result = new JsonObject();
            response.success(result);
            logger.info("更新ICD成功："+id);
        });




    }
}
