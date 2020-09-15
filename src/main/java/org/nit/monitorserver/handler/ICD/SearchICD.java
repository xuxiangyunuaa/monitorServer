package org.nit.monitorserver.handler.ICD;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.RoutingContext;
import org.apache.log4j.Logger;
import org.nit.monitorserver.constant.HttpHeaderContentType;
import org.nit.monitorserver.database.MongoConnection;
import org.nit.monitorserver.handler.log.CreateLog;
import org.nit.monitorserver.message.AbstractRequestHandler;
import org.nit.monitorserver.message.Request;
import org.nit.monitorserver.message.ResponseFactory;
import org.nit.monitorserver.util.FormValidator;
import org.nit.monitorserver.util.Tools;

import java.io.IOException;

import static org.nit.monitorserver.constant.ResponseError.*;

/**
 * @ClassName SearchProject
 * @Description TODO查找ICD
 * @Author 20643
 * @Date 2020-9-1 14:50
 * @Version 1.0
 **/
public class SearchICD extends AbstractRequestHandler {

    protected static final Logger logger = Logger.getLogger(SearchICD.class);
    private final MongoClient mongoClient = new MongoConnection().getMongoClient();
    CreateLog createLog = new CreateLog();

    @Override
    public void handle(final RoutingContext routingContext,final Request request) throws IOException {
        routingContext.response().putHeader(HttpHeaderContentType.CONTENT_TYPE_STR, HttpHeaderContentType.JSON);
        ResponseFactory response = new ResponseFactory(routingContext, request);

        //name
        Object nameObject = request.getParams().getValue("name");
        JsonObject searchObject = new JsonObject();
        if(nameObject != null && !nameObject.toString().equals("")){
            if(!FormValidator.isString(nameObject)){
                logger.error(String.format("search ICD exception: %s", "ICD名称格式错误"));
                response.error(ICDNAME_FORMAT_ERROR.getCode(), ICDNAME_FORMAT_ERROR.getMsg());
                createLog.createLogRecord("ICD管理","error","查找ICD","ICD名称格式错误");
                return;
            }
            String name = nameObject.toString();
            searchObject.put("name",name);
        }

        mongoClient.find("ICD",searchObject,r->{
            if(r.failed()){
                logger.error(String.format("search ICD: %s 查找失败", Tools.getTrace(r.cause())));
                response.error(QUERY_FAILURE.getCode(), QUERY_FAILURE.getMsg());
                createLog.createLogRecord("ICD管理","error","查找ICD","ICD查找失败");
                return;
            }
            JsonObject result = new JsonObject();
            JsonArray icdList = new JsonArray();
            for(int i = 0; i < r.result().size(); i++){
                icdList.add(r.result().get(i));
            }
            result.put("icdList",icdList);
            response.success(result);
            logger.info("ICD查询成功");
            createLog.createLogRecord("ICD管理","info","查找ICD","ICD查找成功");
            return;

        });


    }
}
