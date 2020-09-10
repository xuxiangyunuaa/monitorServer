package org.nit.monitorserver.handler.data;

import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.RoutingContext;
import org.apache.log4j.Logger;
import org.nit.monitorserver.constant.HttpHeaderContentType;
import org.nit.monitorserver.database.MongoConnection;
import org.nit.monitorserver.message.AbstractRequestHandler;
import org.nit.monitorserver.message.Request;
import org.nit.monitorserver.message.ResponseFactory;

import java.io.IOException;

/**
 * @author 20817
 * @version 1.0
 * @className SearchPropData
 * @description
 * @date 2020/9/6 19:13
 */
public class SearchPropData extends AbstractRequestHandler {
    protected static final Logger logger = Logger.getLogger(SearchPropData.class);
    private final MongoClient mongoClient = new MongoConnection().getMongoClient();

    @Override
    public void handle(RoutingContext routingContext, Request request) throws IOException, Exception {
        routingContext.response().putHeader(HttpHeaderContentType.CONTENT_TYPE_STR, HttpHeaderContentType.JSON);
        ResponseFactory response = new ResponseFactory(routingContext, request);



    }
}
