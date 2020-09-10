package org.nit.monitorserver.database;

import com.sun.javaws.Globals;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import org.apache.log4j.Logger;
import org.nit.monitorserver.Config;
import org.nit.monitorserver.VertxInstance;
import org.nit.monitorserver.util.StringUtil;

public class MongoConnection {
    private static Logger logger = Logger.getLogger(MongoConnection.class);
    private Vertx vertx;
    private MongoClient mongoClient;

    public MongoConnection() {
        //读取MongoDB配置文件
        String databaseConfFile = Config.getDatabaseMongoConfFile();

        //将配置转换为json
        final JsonObject config = new JsonObject(StringUtil.readFileToString(databaseConfFile));
        this.vertx = VertxInstance.getInstance().getVertx();
//        this.vertx = Vertx.vertx();
        this.mongoClient = MongoClient.createShared(vertx, config);
        this.mongoClient.getCollections(res->{
            if (res.succeeded()) {
                logger.info(String.format("mongodb database:%s init success", config.toString()));
            }
            else {
                logger.error(String.format("mongodb database:%s init failed", config.toString()));
                this.vertx.close();
            }

        });
    }

    public MongoClient getMongoClient() {
        return this.mongoClient;
    }

}
