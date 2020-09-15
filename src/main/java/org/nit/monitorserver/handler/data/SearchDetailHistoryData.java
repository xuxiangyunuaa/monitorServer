package org.nit.monitorserver.handler.data;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.FindOptions;
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

import static org.nit.monitorserver.constant.ResponseError.*;

/**
 * @author 20817
 * @version 1.0
 * @className SearchDetailHistoryData
 * @description
 * @date 2020/9/8 21:15
 */
public class SearchDetailHistoryData extends AbstractRequestHandler {
    protected static final Logger logger = Logger.getLogger(SearchDetailHistoryData.class);
    private final MongoClient mongoClient = new MongoConnection().getMongoClient();

    @Override
    public void handle(RoutingContext routingContext, Request request) throws IOException, Exception {
        routingContext.response().putHeader(HttpHeaderContentType.CONTENT_TYPE_STR, HttpHeaderContentType.JSON);
        ResponseFactory response = new ResponseFactory(routingContext, request);

        Object idObject = request.getParams().getValue("id");
        if(!FormValidator.isString(idObject)){
            logger.error(String.format("search detailHistoryData exception: %s", "targetIP+evtId记录id格式错误"));
            response.error(TARGETEVTIDID_FORMAT_ERROR.getCode(), TARGETEVTIDID_FORMAT_ERROR.getMsg());
            return;
        }
        String id = idObject.toString();
        if(id == null || id.equals("")){
            logger.error(String.format("search exception: %s", "targetIP+evtId记录id为必填参数"));
            response.error(TARGETEVTIDID.getCode(), TARGETEVTIDID.getMsg());
            return;
        }
        Object targetIPObject = request.getParams().getValue("targetIP");
        if(!FormValidator.isString(targetIPObject)){
            logger.error(String.format("search detailHistoryData exception: %s", "targetIP格式错误"));
            response.error(ID_FORMAT_ERROR.getCode(), ID_FORMAT_ERROR.getMsg());
            return;
        }
        String targetIP = targetIPObject.toString();
        if(targetIP == null || targetIP.equals("")){
            logger.error(String.format("search detailHistoryData exception: %s", "targetIP格式错误"));
            response.error(ID_IS_REQUIRED.getCode(), ID_IS_REQUIRED.getMsg());
            return;
        }

        Object evtIdObject = request.getParams().getValue("eventID");
        if(evtIdObject != null){
            if(!FormValidator.isInteger(evtIdObject)){
                logger.error(String.format("search exception: %s", "evtID格式错误"));
                response.error(EVENTID_FORMAT_ERROR.getCode(), EVENTID_FORMAT_ERROR.getMsg());
                return;
            }
        }
        int evtId = (int)evtIdObject;

        Object pageIndexObject = request.getParams().getValue("page");
        if(pageIndexObject == null){
            logger.error(String.format("search exception: %s", "页码为必填参数"));
            response.error(PAGEINDEX.getCode(), PAGEINDEX.getMsg());
            return;
        }
        if(!FormValidator.isInteger(pageIndexObject)){
            logger.error(String.format("search exception: %s", "页码格式错误"));
            response.error(PAGE_FORMAT_ERROR.getCode(), PAGE_FORMAT_ERROR.getMsg());
            return;
        }
        int pageIndex = (int) pageIndexObject;

        Object pageSizeObject = request.getParams().getValue("pageNum");
        if(pageSizeObject == null){
            logger.error(String.format("search exception: %s", "每页记录数为必填参数"));
            response.error(PAGESIZE.getCode(), PAGESIZE.getMsg());
            return;
        }
        if(!FormValidator.isInteger(pageSizeObject)){
            logger.error(String.format("search exception: %s", "页码格式错误"));
            response.error(PAGMSIZE_FORMAT_ERROR.getCode(), PAGMSIZE_FORMAT_ERROR.getMsg());
            return;
        }
        int pageSize = (int) pageSizeObject;


        String collectionName = targetIP+String.valueOf(evtId);
        FindOptions options = new FindOptions().setSkip((pageIndex-1)*pageSize).setLimit(pageSize);
        mongoClient.findWithOptions(collectionName,new JsonObject(),options,r->{
            if(r.failed()){
                logger.error(String.format("search ICD: %s 查找失败", Tools.getTrace(r.cause())));
                response.error(QUERY_FAILURE.getCode(), QUERY_FAILURE.getMsg());
                return;
            }
            JsonObject dataList = new JsonObject().put("dataList",r.result());
            response.success(dataList);

        });

    }
}
