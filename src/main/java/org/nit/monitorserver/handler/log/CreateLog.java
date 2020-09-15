package org.nit.monitorserver.handler.log;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import org.apache.log4j.Logger;
import org.nit.monitorserver.database.MongoConnection;
import org.nit.monitorserver.util.Tools;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static org.nit.monitorserver.constant.ResponseError.INSERT_FAILURE;

/**
 * @author 20817
 * @version 1.0
 * @className CreateLog
 * @description
 * @date 2020/9/15 0:08
 */
public class CreateLog {
    protected static final Logger logger = Logger.getLogger(DeleteLog.class);
    private final MongoClient mongoClient = new MongoConnection().getMongoClient();

    public void createLogRecord(String module,String level,String subModule,String content){
        JsonObject logRecord = new JsonObject();
        String id = Tools.generateId();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        String timeStamp = formatter.format(now);
        logRecord.put("id",id)
                .put("module",module)
                .put("timeStamp",timeStamp)
                .put("content",content)
                .put("subContent",subModule)
                .put("level",level);
        mongoClient.insert("log",logRecord,r->{
            if(r.failed()){
                logger.error(String.format("new log insert exception: %s", Tools.getTrace(r.cause())));
                return;
            }
        });
    }

}
