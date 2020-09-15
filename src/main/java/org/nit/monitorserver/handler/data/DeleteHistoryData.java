package org.nit.monitorserver.handler.data;

import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.web.RoutingContext;
import org.apache.log4j.Logger;
import org.nit.monitorserver.database.MongoConnection;
import org.nit.monitorserver.database.MysqlConnection;
import org.nit.monitorserver.message.AbstractRequestHandler;
import org.nit.monitorserver.message.Request;

import java.io.IOException;

/**
 * @author 20817
 * @version 1.0
 * @className DeleteHistoryData
 * @description
 * @date 2020/9/12 15:08
 */
public class DeleteHistoryData extends AbstractRequestHandler {
    protected static final Logger logger = Logger.getLogger(SearchHistoryData.class);
    private final MongoClient mongoClient = new MongoConnection().getMongoClient();
    private final SQLClient mySQLClient = new MysqlConnection().getMySQLClient();


    @Override
    public void handle(RoutingContext routingContext, Request request) throws IOException, Exception {


    }
}
