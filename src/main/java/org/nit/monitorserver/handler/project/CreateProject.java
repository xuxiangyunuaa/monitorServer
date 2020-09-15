package org.nit.monitorserver.handler.project;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.RoutingContext;
import org.apache.log4j.Logger;
import org.nit.monitorserver.constant.HttpHeaderContentType;
import org.nit.monitorserver.database.MongoConnection;
import org.nit.monitorserver.handler.ICD.CreateICD;
import org.nit.monitorserver.handler.log.CreateLog;
import org.nit.monitorserver.handler.task.Create;
import org.nit.monitorserver.message.AbstractRequestHandler;
import org.nit.monitorserver.message.Request;
import org.nit.monitorserver.message.ResponseFactory;
import org.nit.monitorserver.util.FormValidator;
import org.nit.monitorserver.util.Tools;

import java.io.IOException;

import static org.nit.monitorserver.constant.ResponseError.INSERT_FAILURE;
import static org.nit.monitorserver.constant.ResponseError.PROJECTNAME;
import static org.nit.monitorserver.constant.ResponseError.QUERY_FAILURE;
import static org.nit.monitorserver.constant.ResponseError.*;

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
    CreateLog createLog = new CreateLog();

    @Override
    public void handle(final RoutingContext routingContext,final Request request) throws IOException {
        routingContext.response().putHeader(HttpHeaderContentType.CONTENT_TYPE_STR, HttpHeaderContentType.JSON);
        ResponseFactory response = new ResponseFactory(routingContext, request);

        JsonObject createObject = new JsonObject();

        //name
        Object nameObject = request.getParams().getValue("name");
        if(nameObject == null || nameObject.toString().equals("")){
            logger.error(String.format("create project exception: %s", "工程名称为必填参数"));
            response.error(PROJECTNAME.getCode(), PROJECTNAME.getMsg());
            createLog.createLogRecord("工程管理","error","新建工程","工程名称为必填参数");
            return;
        }
        if(!FormValidator.isString(nameObject)){
            logger.error(String.format("create project exception: %s", "工程名称格式错误"));
            response.error(PROJECTNAME_FORMAT_ERROR.getCode(), PROJECTNAME_FORMAT_ERROR.getMsg());
            createLog.createLogRecord("工程管理","error","新建工程","工程名称格式错误");
            return;
        }
        String name = nameObject.toString();
        createObject.put("name",name);

        //content
        Object contentObject = request.getParams().getValue("content");
        if(contentObject != null && !contentObject.toString().equals("[]")){
            if(!FormValidator.isJsonArray(contentObject)){
                logger.error(String.format("create project exception: %s", "工程内容格式错误"));
                response.error(CONTENT_FORMAT_ERROR.getCode(), CONTENT_FORMAT_ERROR.getMsg());
                createLog.createLogRecord("工程管理","error","新建工程","工程内容格式错误");
                return;
            }
            JsonArray content = (JsonArray) contentObject;
            createObject.put("content",content);
        }


        mongoClient.find("project",createObject,re->{
            if(re.failed()){
                logger.error(String.format("create project: %s 查找失败", Tools.getTrace(re.cause())));
                response.error(QUERY_FAILURE.getCode(), QUERY_FAILURE.getMsg());
                createLog.createLogRecord("工程管理","error","新建工程","工程查找失败");
                return;
            }else if(re.result().size() == 0){
                String id = Tools.generateId();
                createObject.put("id",id);

                mongoClient.insert("project",createObject,r->{
                    if(r.failed()){
                        logger.error(String.format("new project insert exception: %s", Tools.getTrace(r.cause())));
                        response.error(INSERT_FAILURE.getCode(), INSERT_FAILURE.getMsg());
                        createLog.createLogRecord("工程管理","error","新建工程","工程新建失败");
                        return;
                    }

                    JsonObject result = new JsonObject().put("id",id);
                    response.success(result);
                    createLog.createLogRecord("工程管理","info","新建工程",String.format("工程:%s 新建成功",id));
                    return;

                });

            }else {
                response.error(RECORD_EXISTED.getCode(),RECORD_EXISTED.getMsg());
                logger.error(String.format("new project insert exception: %s", "该记录已经存在"));
                createLog.createLogRecord("工程管理","error","新建工程","工程已经存在");
                return;

            }
        });

    }
}
