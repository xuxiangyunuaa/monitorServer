package org.nit.monitorserver.handler.dataIncentive;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.datagram.DatagramSocket;
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

import static org.nit.monitorserver.constant.ResponseError.INCENTIVEID;
import static org.nit.monitorserver.constant.ResponseError.RUNFLAG;
import static org.nit.monitorserver.constant.ResponseError.*;

/**
 * @ClassName CtrDataIncentive
 * @Description TODO控制数据激励任务停止或者开始
 * @Author 20643
 * @Date 2020-9-1 14:53
 * @Version 1.0
 **/
public class CtrDataIncentive extends AbstractRequestHandler {
    protected static final Logger logger = Logger.getLogger(CtrDataIncentive.class);
    private final MongoClient mongoClient = new MongoConnection().getMongoClient();
    private Vertx vertx = Vertx.vertx();
    static long timerID = Long.MIN_VALUE;

    @Override
    public void handle(final RoutingContext routingContext,final Request request) throws IOException {
        routingContext.response().putHeader(HttpHeaderContentType.CONTENT_TYPE_STR, HttpHeaderContentType.JSON);
        ResponseFactory response = new ResponseFactory(routingContext, request);

        String id = request.getParams().getString("id");
        Object runFlagObject = request.getParams().getValue("runFlag");

        if( id == null || id.equals("")){
            logger.error(String.format("update exception: %s", "激励任务id为必填参数"));
            response.error(INCENTIVEID.getCode(), INCENTIVEID.getMsg());
            return;
        }
        JsonObject searchObject = new JsonObject().put("id",id);

        if(runFlagObject == null){
            logger.error(String.format("update exception: %s", "激励任务的运行标识为必填参数"));
            response.error(RUNFLAG.getCode(), RUNFLAG.getMsg());
            return;
        }else if(!FormValidator.isInteger(runFlagObject)){
            logger.error(String.format("update exception: %s", "激励任务的运行标识格式错误"));
            response.error(RUNFLAG_FORMAT_ERROR.getCode(), RUNFLAG_FORMAT_ERROR.getMsg());
            return;
        }
        int runFlag = (int) runFlagObject;
        if(runFlag != 0 && runFlag != 1){
            logger.error(String.format("update exception: %s", "激励任务的运行标识格式错误"));
            response.error(RUNFLAG_FORMAT_ERROR.getCode(), RUNFLAG_FORMAT_ERROR.getMsg());
            return;
        }
        System.out.println("timeID:"+Long.MIN_VALUE);
        if(runFlag == 1){//启动数据激励
            if(timerID == Long.MIN_VALUE){//么有正在执行的激励任务
                mongoClient.find("dataIncentive",searchObject,r->{
                    if(r.failed()){
                        logger.error(String.format("search dataIncenive: %s 查找失败", Tools.getTrace(r.cause())));
                        response.error(QUERY_FAILURE.getCode(), QUERY_FAILURE.getMsg());
                        return;
                    }
                    String port = r.result().get(0).getString("port");
                    String targetIP = r.result().get(0).getString("targetIP");
                    JsonObject data = r.result().get(0).getJsonObject("data");
                    String period = r.result().get(0).getString("period");
                    DatagramSocket commandSendsocket = vertx.createDatagramSocket();
                    if(period.equals("0")){
                        commandSendsocket.send(Buffer.buffer(data.toString().getBytes()),Integer.parseInt(port),targetIP,re->{
                            if(re.failed()){
                                logger.error(String.format("send dataIncenive 失败: %s ", Tools.getTrace(re.cause())));
                                response.error(DATAINCENTIVE_FAILURE.getCode(), DATAINCENTIVE_FAILURE.getMsg());
                                return;
                            }
                            logger.info(String.format("start dataIncenive: %s 成功", id));
                            response.success(new JsonObject());
                            return;
                        });
                    }else {

                        timerID = vertx.setPeriodic(Integer.parseInt(period),rt->{
                            System.out.println("周期性任务的min:"+timerID);
                            commandSendsocket.send(Buffer.buffer(data.toString().getBytes()),Integer.parseInt(port),targetIP,re->{
                                if(re.failed()){
                                    logger.error(String.format("send dataIncenive 失败: %s ", Tools.getTrace(re.cause())));
                                    response.error(DATAINCENTIVE_FAILURE.getCode(), DATAINCENTIVE_FAILURE.getMsg());
                                    return;
                                }
                                logger.info(String.format("start dataIncenive: %s 成功", id));
                                response.success(new JsonObject());
                                return;


                            });
                        });

                    }
                });

            }else {//存在正在执行的激励任务

                response.error(DATAINCENTIVEISEXECUTING.getCode(),DATAINCENTIVEISEXECUTING.getMsg());
                logger.error(String.format("start dataIncenive: %s failure", id));
                return;
            }

        }else {//关闭数据激励
            if(timerID != Long.MIN_VALUE){
                vertx.cancelTimer(timerID);
                timerID = Long.MIN_VALUE;
                logger.info(String.format("end dataIncenive: %s 成功", id));
                response.success(new JsonObject());
                return;
            }



        }









    }
}
