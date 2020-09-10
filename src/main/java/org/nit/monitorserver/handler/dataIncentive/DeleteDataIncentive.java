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
import org.nit.monitorserver.util.Tools;

import java.io.IOException;

import static org.nit.monitorserver.constant.ResponseError.*;

/**
 * @ClassName DeleteProject
 * @Description TODO删除数据激励任务
 * @Author 20643
 * @Date 2020-9-1 14:49
 * @Version 1.0
 **/
public class DeleteDataIncentive extends AbstractRequestHandler {
    protected static final Logger logger = Logger.getLogger(DeleteDataIncentive.class);
    private final MongoClient mongoClient = new MongoConnection().getMongoClient();

    @Override
    public void handle(final RoutingContext routingContext,final Request request) throws IOException {
        routingContext.response().putHeader(HttpHeaderContentType.CONTENT_TYPE_STR, HttpHeaderContentType.JSON);
        ResponseFactory response = new ResponseFactory(routingContext, request);

        String id = request.getParams().getString("id");
        if(id ==null || id.equals("")){
            logger.error(String.format("delete exception: %s", "激励任务id为必填参数"));
            response.error(INCENTIVEID.getCode(), INCENTIVEID.getMsg());
            return;
        }
        JsonObject deleteObject =  new JsonObject().put("id",id);
        mongoClient.removeDocuments("dataIncentive",deleteObject,r->{
            if(r.failed()){
                logger.error(String.format("delete exception: %s", Tools.getTrace(r.cause())));
                response.error(DELETE_FAILURE.getCode(), DELETE_FAILURE.getMsg());
                return;
            }
            JsonObject result = new JsonObject();
            response.success(result);
            logger.info("数据激励任务删除失败："+id);
        });



    }
}
