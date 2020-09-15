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

import static org.nit.monitorserver.constant.ResponseError.INCENTIVENAME_FORMAT_ERROR;
import static org.nit.monitorserver.constant.ResponseError.*;

/**
 * @ClassName SearchProject
 * @Description TODO查找数据激励任务
 * @Author 20643
 * @Date 2020-9-1 14:50
 * @Version 1.0
 **/
public class SearchDataIncentive extends AbstractRequestHandler {
    protected static final Logger logger = Logger.getLogger(SearchDataIncentive.class);
    private final MongoClient mongoClient = new MongoConnection().getMongoClient();
    CreateLog createLog = new CreateLog();

    @Override
    public void handle(final RoutingContext routingContext,final Request request) throws IOException {
        routingContext.response().putHeader(HttpHeaderContentType.CONTENT_TYPE_STR, HttpHeaderContentType.JSON);
        ResponseFactory response = new ResponseFactory(routingContext, request);

        Object nameObject = request.getParams().getValue("name");
        Object portObject= request.getParams().getValue("port");
        Object targetIPObject = request.getParams().getValue("targetIP");
        JsonObject searchObject =  new JsonObject();
        //name
        if(nameObject != null && !nameObject.toString().equals("")){
            if(!FormValidator.isString(nameObject)){
                logger.error(String.format("search dataIncentive exception: %s", "数据激励名称格式错误"));
                response.error(INCENTIVENAME_FORMAT_ERROR.getCode(), INCENTIVENAME_FORMAT_ERROR.getMsg());
                createLog.createLogRecord("数据激励","error","查找数据激励","数据激励名称格式错误");
                return;
            }
            String name = nameObject.toString();
            searchObject.put("name",name);

        }

        //port
        if(portObject != null && !portObject.toString().equals("")){
            if(!FormValidator.isString(portObject)){
                logger.error(String.format("search dataIncentive exception: %s", "激励的虚端口格式错误"));
                response.error(INCENTIVEPORT_FORMAT_ERROR.getCode(), INCENTIVEPORT_FORMAT_ERROR.getMsg());
                createLog.createLogRecord("数据激励","error","查找数据激励","激励的虚端口格式错误");
                return;
            }
            String port = (String) portObject;
            searchObject.put("port",port);
        }

        //targrtIP
        if(targetIPObject != null && !targetIPObject.toString().equals("")){
            if(!FormValidator.isString(targetIPObject)){
                logger.error(String.format("search dataIncentive exception: %s", "激励的目标机IP格式错误"));
                response.error(INCENTIVETARGETIP_FORMAT_ERROR.getCode(), INCENTIVETARGETIP_FORMAT_ERROR.getMsg());
                createLog.createLogRecord("数据激励","error","查找数据激励","激励的目标机IP格式错误");
                return;
            }
            String targetIP = targetIPObject.toString();
            searchObject.put("targetIP",targetIP);

        }

        mongoClient.find("dataIncentive",searchObject,r->{
            if(r.failed()){
                logger.error(String.format("search dataIncenive: %s 查找失败", Tools.getTrace(r.cause())));
                response.error(QUERY_FAILURE.getCode(), QUERY_FAILURE.getMsg());
                createLog.createLogRecord("数据激励","error","查找数据激励","查找数据激励失败");
                return;
            }
            JsonObject incentiveList = new JsonObject().put("incentiveList",r.result());
            response.success(incentiveList);
            logger.info("数据激励任务查询成功");
            createLog.createLogRecord("数据激励","info","查找数据激励","查找数据激励成功");
            return;

        });



    }
}
