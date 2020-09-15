package org.nit.monitorserver.handler.dataIncentive;

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
 * @ClassName DeleteProject
 * @Description TODO删除数据激励任务
 * @Author 20643
 * @Date 2020-9-1 14:49
 * @Version 1.0
 **/
public class DeleteDataIncentive extends AbstractRequestHandler {
    protected static final Logger logger = Logger.getLogger(DeleteDataIncentive.class);
    private final MongoClient mongoClient = new MongoConnection().getMongoClient();
    CreateLog createLog = new CreateLog();

    @Override
    public void handle(final RoutingContext routingContext,final Request request) throws IOException {
        routingContext.response().putHeader(HttpHeaderContentType.CONTENT_TYPE_STR, HttpHeaderContentType.JSON);
        ResponseFactory response = new ResponseFactory(routingContext, request);

        //数据激励id
        Object idObject = request.getParams().getValue("id");
        if(idObject == null || idObject.toString().equals("")){
            logger.error(String.format("delete dataIncentive exception: %s", "数据激励id为必填参数"));
            response.error(INCENTIVEID.getCode(), INCENTIVEID.getMsg());
            createLog.createLogRecord("数据激励","error","删除数据激励","数据激励id为必填参数");
            return;
        }
        if(!FormValidator.isString(idObject)){
            logger.error(String.format("delete dataIncentive exception: %s", "数据激励id格式错误"));
            response.error(INCENTIVEID_FORMAT_ERROR.getCode(), INCENTIVEID_FORMAT_ERROR.getMsg());
            createLog.createLogRecord("数据激励","error","删除数据激励","数据激励id格式错误");
            return;
        }
        String id = idObject.toString();
        JsonObject deleteObject =  new JsonObject().put("id",id);


        mongoClient.removeDocuments("dataIncentive",deleteObject,r->{
            if(r.failed()){
                logger.error(String.format("delete exception: %s", Tools.getTrace(r.cause())));
                response.error(DELETE_FAILURE.getCode(), DELETE_FAILURE.getMsg());
                createLog.createLogRecord("数据激励","error","删除数据激励",String.format("数据激励：%s 删除失败",id));
                return;
            }
            JsonObject result = new JsonObject();
            response.success(result);
            logger.info("数据激励任务删除失败："+id);
            createLog.createLogRecord("数据激励","info","删除数据激励",String.format("数据激励：%s 删除成功",id));
            return;
        });



    }
}
