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

import static org.nit.monitorserver.constant.ResponseError.ICDNAME_IS_REQUIRED;
import static org.nit.monitorserver.constant.ResponseError.*;

/**
 * @ClassName CreateProject
 * @Description TODO创建ICD
 * @Author 20643
 * @Date 2020-9-1 14:49
 * @Version 1.0
 **/
public class CreateICD extends AbstractRequestHandler {
    protected static final Logger logger = Logger.getLogger(CreateICD.class);
    private final MongoClient mongoClient = new MongoConnection().getMongoClient();
    CreateLog createLog = new CreateLog();
    @Override

    public void handle(final RoutingContext routingContext,final Request request) throws IOException {
        routingContext.response().putHeader(HttpHeaderContentType.CONTENT_TYPE_STR, HttpHeaderContentType.JSON);
        ResponseFactory response = new ResponseFactory(routingContext, request);

        JsonObject insertObject = new JsonObject();

        //name
        Object nameObject = request.getParams().getValue("name");
        if(nameObject == null || nameObject.toString().equals("")){
            logger.error(String.format("create ICD exception: %s", "ICD名称为必填参数"));
            response.error(ICDNAME_IS_REQUIRED.getCode(), ICDNAME_IS_REQUIRED.getMsg());
            createLog.createLogRecord("ICD管理","error","新建ICD","ICD名称为必填参数");
            return;
        }
        if(!FormValidator.isString(nameObject)){
            logger.error(String.format("create ICD exception: %s", "ICD名称格式错误"));
            response.error(ICDNAME_FORMAT_ERROR.getCode(), ICDNAME_FORMAT_ERROR.getMsg());
            createLog.createLogRecord("ICD管理","error","新建ICD","ICD名称格式错误");
            return;
        }
        String name = nameObject.toString();
        insertObject.put("name",name);

        //ICD
        Object ICDObject = request.getParams().getValue("ICD");
        if(ICDObject == null || ICDObject.toString().equals("[]")){
            logger.error(String.format("create ICD exception: %s", "ICD内容为必填参数"));
            response.error(ICD_IS_REQUIRED.getCode(), ICD_IS_REQUIRED.getMsg());
            createLog.createLogRecord("ICD管理","error","新建ICD","ICD内容为必填参数");
            return;
        }
        if(!FormValidator.isJsonArray(ICDObject)){
            logger.error(String.format("create ICD exception: %s", "ICD内容格式错误"));
            response.error(ICD_FORMAT_ERROR.getCode(), ICD_FORMAT_ERROR.getMsg());
            createLog.createLogRecord("ICD管理","error","新建ICD","ICD内容格式错误");
            return;
        }
        JsonArray ICD = (JsonArray) ICDObject;
        insertObject.put("ICD",ICD);


        mongoClient.find("ICD",insertObject,re->{
            if(re.failed()){
                logger.error(String.format("search ICD: %s 查找失败", Tools.getTrace(re.cause())));
                response.error(QUERY_FAILURE.getCode(), QUERY_FAILURE.getMsg());
                createLog.createLogRecord("ICD管理","error","新建ICD","数据库查找ICD失败");
                return;
            }else if(re.result().size() == 0){
                System.out.println("数据库中没有该记录");
                String id = Tools.generateId();
                insertObject.put("id",id);
                mongoClient.insert("ICD",insertObject,r->{
                    if(r.failed()){
                        logger.error(String.format("new ICD insert exception: %s", Tools.getTrace(r.cause())));
                        response.error(INSERT_FAILURE.getCode(), INSERT_FAILURE.getMsg());
                        createLog.createLogRecord("ICD管理","error","新建ICD","ICD记录插入数据库失败");
                        return;
                    }else {
                        JsonObject result = new JsonObject().put("id",id);
                        response.success(result);
                        logger.info("创建ICD成功："+id);
                        createLog.createLogRecord("ICD管理","info","新建ICD",String.format("ICD记录插入数据库成功",id));
                        return;
                    }
                });
            }else {
                response.error(RECORD_EXISTED.getCode(),RECORD_EXISTED.getMsg());
                logger.error(String.format("new ICD insert exception: %s","该记录已经存在"));
                createLog.createLogRecord("ICD管理","error","新建ICD","ICD记录已经存在");
                return;

            }
        });





    }
}
