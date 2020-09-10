package org.nit.monitorserver.handler.ICD;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.RoutingContext;
import org.apache.log4j.Logger;
import org.nit.monitorserver.constant.HttpHeaderContentType;
import org.nit.monitorserver.database.MongoConnection;
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
    @Override

    public void handle(final RoutingContext routingContext,final Request request) throws IOException {
        routingContext.response().putHeader(HttpHeaderContentType.CONTENT_TYPE_STR, HttpHeaderContentType.JSON);
        ResponseFactory response = new ResponseFactory(routingContext, request);

        String name = request.getParams().getString("name");
        if(name.equals("") || name == null){
            logger.error(String.format("insert exception: %s", "ICD名称为必填参数"));
            response.error(ICDNAME_IS_REQUIRED.getCode(), ICDNAME_IS_REQUIRED.getMsg());
            return;

        }

        Object ICDObject = request.getParams().getValue("ICD");
        if(ICDObject == null){
            logger.error(String.format("insert exception: %s", "ICD内容为无效参数"));
            response.error(ICD_IS_REQUIRED.getCode(), ICD_IS_REQUIRED.getMsg());
            return;

        }
        if(!FormValidator.isJsonArray(ICDObject)){
            logger.error(String.format("insert exception: %s", "ICD内容格式错误"));
            response.error(ICD_FORMAT_ERROR.getCode(), ICD_FORMAT_ERROR.getMsg());
            return;
        }
        JsonArray ICD = (JsonArray) ICDObject;
        System.out.println("ICD的jsonArray:"+ICD);

        String id = Tools.generateId();
        JsonObject insertObject = new JsonObject().put("id",id).put("name",name).put("ICD",ICD);
        JsonObject searchObjct = new JsonObject().put("name",name).put("ICD",ICD);
        mongoClient.find("ICD",searchObjct,re->{
            if(re.failed()){
                logger.error(String.format("search ICD: %s 查找失败", Tools.getTrace(re.cause())));
                response.error(QUERY_FAILURE.getCode(), QUERY_FAILURE.getMsg());
                return;
            }else if(re.result().size() == 0){
                System.out.println("数据库中没有该记录");
                mongoClient.insert("ICD",insertObject,r->{
                    if(r.failed()){
                        logger.error(String.format("new ICD insert exception: %s", Tools.getTrace(r.cause())));
                        response.error(INSERT_FAILURE.getCode(), INSERT_FAILURE.getMsg());
                        return;
                    }else {
                        JsonObject result = new JsonObject().put("id",id);
                        response.success(result);
                        logger.info("创建ICD成功："+id);
                    }
                });
            }else {
                response.error(RECORD_EXISTED.getCode(),RECORD_EXISTED.getMsg());
                logger.error(String.format("new ICD insert exception: %s","该记录已经存在"));
            }
        });





    }
}
