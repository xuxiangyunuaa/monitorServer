package org.nit.monitorserver.handler.data;

import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.RoutingContext;
import org.apache.log4j.Logger;
import org.nit.monitorserver.constant.HttpHeaderContentType;
import org.nit.monitorserver.database.MongoConnection;
import org.nit.monitorserver.message.AbstractRequestHandler;
import org.nit.monitorserver.message.Request;
import org.nit.monitorserver.message.ResponseFactory;
import org.nit.monitorserver.util.FormValidator;

import java.io.IOException;

import static org.nit.monitorserver.constant.ResponseError.TARGETIPLIST;
import static org.nit.monitorserver.constant.ResponseError.TARGETIPLIST_FORMAT_ERROR;

/**
 * @author 20817
 * @version 1.0
 * @className SearchProp
 * @description
 * @date 2020/9/6 18:51
 */
public class SearchProp extends AbstractRequestHandler {
    protected static final Logger logger = Logger.getLogger(SearchProp.class);
    private final MongoClient mongoClient = new MongoConnection().getMongoClient();

    @Override
    public void handle(RoutingContext routingContext, Request request) throws IOException, Exception {
        routingContext.response().putHeader(HttpHeaderContentType.CONTENT_TYPE_STR, HttpHeaderContentType.JSON);
        ResponseFactory response = new ResponseFactory(routingContext, request);

        Object targrtIPObject = request.getParams().getValue("targetIP");
        if(targrtIPObject == null){
            logger.error(String.format("search targetIP propertity exception: %s", "目标机ip列表为必填参数"));
            response.error(TARGETIPLIST.getCode(), TARGETIPLIST.getMsg());
            return;
        }
        if(!FormValidator.isJsonArray(targrtIPObject)){
            logger.error(String.format("search targetIP propertity exception: %s", "目标机ip列表格式错误"));
            response.error(TARGETIPLIST_FORMAT_ERROR.getCode(), TARGETIPLIST_FORMAT_ERROR.getMsg());
            return;
        }

    }
}
