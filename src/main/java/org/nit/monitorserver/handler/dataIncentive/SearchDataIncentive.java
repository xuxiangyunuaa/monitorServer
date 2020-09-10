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

import static org.nit.monitorserver.constant.ResponseError.QUERY_FAILURE;

/**
 * @ClassName SearchProject
 * @Description TODO查找数据激励任务
 * @Author 20643
 * @Date 2020-9-1 14:50
 * @Version 1.0
 **/
public class SearchDataIncentive extends AbstractRequestHandler {
    protected static final Logger logger = Logger.getLogger(SearchDataIncentive.class);
    private final MongoClient mongoClient = new MongoConnection().getMongoClient();

    @Override
    public void handle(final RoutingContext routingContext,final Request request) throws IOException {
        routingContext.response().putHeader(HttpHeaderContentType.CONTENT_TYPE_STR, HttpHeaderContentType.JSON);
        ResponseFactory response = new ResponseFactory(routingContext, request);

        String name = request.getParams().getString("name");
        Object portObject= request.getValue("port");
        String targetIP = request.getParams().getString("targetIP");
        JsonObject searchObject =  new JsonObject();
        if(!name.equals("") && name != null){
            searchObject.put("name",name);
        }
        if(portObject != null ){
            if(FormValidator.isInteger(portObject)){
                int port = (int) portObject;
                searchObject.put("port",port);
            }
        }
        if(!targetIP.equals("") && targetIP != null){
            searchObject.put("targetIP",targetIP);
        }
        mongoClient.find("dataIncentive",searchObject,r->{
            if(r.failed()){
                logger.error(String.format("search dataIncenive: %s 查找失败", Tools.getTrace(r.cause())));
                response.error(QUERY_FAILURE.getCode(), QUERY_FAILURE.getMsg());
                return;
            }
            JsonObject incentiveList = new JsonObject().put("incentiveList",r.result());
            response.success(incentiveList);

            logger.info("数据激励任务查询成功");

        });



    }
}
