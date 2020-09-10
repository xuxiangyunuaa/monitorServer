package org.nit.monitorserver.handler.project;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.RoutingContext;
import org.apache.log4j.Logger;
import org.nit.monitorserver.constant.HttpHeaderContentType;
import org.nit.monitorserver.database.MongoConnection;
import org.nit.monitorserver.handler.ICD.CreateICD;
import org.nit.monitorserver.message.AbstractRequestHandler;
import org.nit.monitorserver.message.Request;
import org.nit.monitorserver.message.ResponseFactory;
import org.nit.monitorserver.util.Tools;

import java.io.IOException;

import static org.nit.monitorserver.constant.ResponseError.INSERT_FAILURE;
import static org.nit.monitorserver.constant.ResponseError.PROJECTNAME;
import static org.nit.monitorserver.constant.ResponseError.QUERY_FAILURE;
import static org.nit.monitorserver.constant.ResponseError.RECORD_EXISTED;

/**
 * @ClassName CreateProject
 * @Description TODO创建一个数据分析工程
 * @Author 20643
 * @Date 2020-9-1 14:49
 * @Version 1.0
 **/
public class CreateProject extends AbstractRequestHandler {
    protected static final Logger logger = Logger.getLogger(CreateICD.class);
    private final MongoClient mongoClient = new MongoConnection().getMongoClient();

    @Override
    public void handle(final RoutingContext routingContext,final Request request) throws IOException {
        routingContext.response().putHeader(HttpHeaderContentType.CONTENT_TYPE_STR, HttpHeaderContentType.JSON);
        ResponseFactory response = new ResponseFactory(routingContext, request);

        JsonObject createObject = new JsonObject();
        String name = request.getParams().getString("name");
        if(name.equals("") || name == null){
            logger.error(String.format("insert exception: %s", "工程名称为必填参数"));
            response.error(PROJECTNAME.getCode(), PROJECTNAME.getMsg());
            return;
        }
        createObject.put("name",name);
        JsonArray content = request.getParams().getJsonArray("content");
        if(content != null){
            createObject.put("content",content);
        }

        mongoClient.find("project",createObject,re->{
            if(re.failed()){
                logger.error(String.format("search project: %s 查找失败", Tools.getTrace(re.cause())));
                response.error(QUERY_FAILURE.getCode(), QUERY_FAILURE.getMsg());
                return;
            }else if(re.result().size() == 0){
                String id = Tools.generateId();
                createObject.put("id",id);

                mongoClient.insert("project",createObject,r->{
                    if(r.failed()){
                        logger.error(String.format("new project insert exception: %s", Tools.getTrace(r.cause())));
                        response.error(INSERT_FAILURE.getCode(), INSERT_FAILURE.getMsg());
                        return;
                    }
                    JsonObject result = new JsonObject().put("id",id);
                    response.success(result);

                });

            }else {
                response.error(RECORD_EXISTED.getCode(),RECORD_EXISTED.getMsg());
                logger.error(String.format("new project insert exception: %s", "该记录已经存在"));

            }
        });

    }
}
