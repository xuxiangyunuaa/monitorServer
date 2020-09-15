package org.nit.monitorserver.handler.project;

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

import java.io.IOException;
import java.text.Normalizer;

import static org.nit.monitorserver.constant.ResponseError.DELETE_FAILURE;
import static org.nit.monitorserver.constant.ResponseError.ICDID_IS_REQUIRED;
import static org.nit.monitorserver.constant.ResponseError.PROJECTID;
import static org.nit.monitorserver.constant.ResponseError.PROJECTID_FORMAT_ERROR;

/**
 * @ClassName DeleteProject
 * @Description TODO删除数据分析工程
 * @Author 20643
 * @Date 2020-9-1 14:49
 * @Version 1.0
 **/
public class DeleteProject extends AbstractRequestHandler {
    protected static final Logger logger = Logger.getLogger(DeleteProject.class);
    private final MongoClient mongoClient = new MongoConnection().getMongoClient();
    CreateLog createLog = new CreateLog();

    @Override
    public void handle(final RoutingContext routingContext,final Request request) throws IOException {
        routingContext.response().putHeader(HttpHeaderContentType.CONTENT_TYPE_STR, HttpHeaderContentType.JSON);
        ResponseFactory response = new ResponseFactory(routingContext, request);

        //工程Id
        Object idObject = request.getParams().getValue("id");
        if(idObject== null || idObject.toString().equals("")){
            logger.error(String.format("delete project exception: %s", "工程的id为必填参数"));
            response.error(PROJECTID.getCode(), PROJECTID.getMsg());
            createLog.createLogRecord("工程管理","error","删除工程","工程的id为必填参数");
            return;
        }
        if(!FormValidator.isString(idObject)){
            logger.error(String.format("delete project exception: %s", "工程的id格式错误"));
            response.error(PROJECTID_FORMAT_ERROR.getCode(), PROJECTID_FORMAT_ERROR.getMsg());
            createLog.createLogRecord("工程管理","error","删除工程","工程的id格式错误");
            return;
        }
        String id = idObject.toString();
        JsonObject deleteObject = new JsonObject().put("id",id);
        mongoClient.removeDocuments("project",deleteObject,r->{
            if(r.failed()){
                logger.error(String.format("delete project: %s 删除失败",id));
                response.error(DELETE_FAILURE.getCode(), DELETE_FAILURE.getMsg());
                createLog.createLogRecord("工程管理","error","删除工程",String.format("工程的：%s 删除失败",id));
                return;
            }
            JsonObject result = new JsonObject();
            response.success(result);
            createLog.createLogRecord("工程管理","info","删除工程",String.format("工程的：%s 删除成功",id));
            return;
        });

    }
}
