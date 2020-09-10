package org.nit.monitorserver;


import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.RoutingContext;
import org.apache.log4j.Logger;

import org.nit.monitorserver.ICDParse.ICDAcquisition;
import org.nit.monitorserver.handler.ICD.CreateICD;
import org.nit.monitorserver.handler.ICD.DeleteICD;
import org.nit.monitorserver.handler.ICD.SearchICD;
import org.nit.monitorserver.handler.ICD.UpdateICD;
import org.nit.monitorserver.handler.data.*;
import org.nit.monitorserver.handler.dataIncentive.*;
import org.nit.monitorserver.handler.log.DeleteLog;
import org.nit.monitorserver.handler.log.GetLog;
import org.nit.monitorserver.handler.project.CreateProject;
import org.nit.monitorserver.handler.project.DeleteProject;
import org.nit.monitorserver.handler.project.SearchProject;
import org.nit.monitorserver.handler.project.UpdateProject;
import org.nit.monitorserver.handler.task.*;

import org.nit.monitorserver.handler.targetMachine.*;
import org.nit.monitorserver.message.AbstractRequestHandler;
import org.nit.monitorserver.message.Request;
import org.nit.monitorserver.message.ResponseFactory;

import java.util.Hashtable;

import static org.nit.monitorserver.constant.ResponseError.METHOD_NOT_FOUND;


/**
 * @author sensordb
 * @date 2018/5/21
 */

public class RequestProcessor {
    protected static final Logger logger = Logger.getLogger(RequestProcessor.class);
    private static RequestProcessor instance;
    private Hashtable<String, AbstractRequestHandler> requestHandlerMap;

    private RequestProcessor() {
        this.requestHandlerMap = new Hashtable<>();
        this.initRequestHandlers();
    }

    private void initRequestHandlers() {


        //目标机管理模块
//        this.requestHandlerMap.put("targetMachine.RequestData", new getData());
        this.requestHandlerMap.put("targetMachine.create", new CreateMachine());
        this.requestHandlerMap.put("targetMachine.update", new UpdateMachine());
        this.requestHandlerMap.put("targetMachine.delete", new DeleteMachie());
        this.requestHandlerMap.put("targetMachine.search", new SearchMachine());
//        this.requestHandlerMap.put("targetMachine.CommandSetCommit", new createCommandSet());//新建指令集
//        this.requestHandlerMap.put("targetMachine.CommandSetName", new getCommandSet());//获取指令集列表
//        this.requestHandlerMap.put("targetMachine.CommandRequestInfo", new getCommandSet());//获取指令集内容
//        this.requestHandlerMap.put("targetMachine.CommandRunInfo", new commandRunInfo());//运行指令集

//采集任务管理
//        this.requestHandlerMap.put("CollectionTask.GetTaskRecordList",new GetTaskRecordList());
//        this.requestHandlerMap.put("CollectionTask.GetPlaneList",new GetPlaneList());
//        this.requestHandlerMap.put("CollectionTask.DeleteRecord",new DeleteRecord());
//        this.requestHandlerMap.put("CollectionTask.CheckoutRecord",new CheckoutRecord());
//        this.requestHandlerMap.put("CollectionTask.RecordDerivation",new RecordDerivation());


//this.requestHandlerMap.put("CollectionTask.GetDefaultList",new GetDefaultList());
        //采集数据管理
//        this.requestHandlerMap.put("AcquisitionDataManage.GetAcquisitionData", new GetAcquisitionData());
//        this.requestHandlerMap.put("AcquisitionDataManage.GetTypes", new GetTypes());
//        this.requestHandlerMap.put("AcquisitionDataManage.GetAims", new GetAims());
//        this.requestHandlerMap.put("AcquisitionDataManage.GetTotal", new GetTotal());
//        this.requestHandlerMap.put("AcquisitionDataManage.ShowAcquisitionData", new ShowAcquisitionData());

        //数据采集配置模块
//        this.requestHandlerMap.put("Acquisition.DataCollect", new DataCollect());//采集数据
//        this.requestHandlerMap.put("Acquisition.GetMirrorSize", new GetMirrorSize());//采集数据
//        this.requestHandlerMap.put("Acquisition.CommunicationCommand", new CommunicationCommand());//采集数据
//        this.requestHandlerMap.put("AcquisitionConfig.GetTaskRunTime", new GetTaskRunTime());//采集数据
        this.requestHandlerMap.put("AcquisitionConfig.CommunicationCommand", new CommunicationCommand());//采集数据
//        this.requestHandlerMap.put("AcquisitionConfig.RequestChecked", new RequestChecked());//采集数据
//        this.requestHandlerMap.put("AcquisitionConfig.SaveChecked", new SaveChecked());//采集数据

//        this.requestHandlerMap.put("DataManage.QueryData",new QueryData());
//        this.requestHandlerMap.put("DataManage.DeleteDataPre",new DeleteDataPre());
//        this.requestHandlerMap.put("DataManage.DownloadData",new DownloadData());
//        this.requestHandlerMap.put("DataManage.RequestDetailData",new RequestDetailData());

        this.requestHandlerMap.put("ICD.ICDAcquisition",new ICDAcquisition());


        //ICD管理
        this.requestHandlerMap.put("ICD.create",new CreateICD());
        this.requestHandlerMap.put("ICD.delete",new DeleteICD());
        this.requestHandlerMap.put("ICD.update",new UpdateICD());
        this.requestHandlerMap.put("ICD.search",new SearchICD());

        //采集任务管理
        this.requestHandlerMap.put("task.create",new Create());
        this.requestHandlerMap.put("task.delete",new DeleteTask());
        this.requestHandlerMap.put("task.update",new UpdateTask());
        this.requestHandlerMap.put("task.search",new SearchTask());
        this.requestHandlerMap.put("task.ctr",new Ctr());

        //数据管理
//        this.requestHandlerMap.put("data.search",new SearchData());
        this.requestHandlerMap.put("data.delete",new DeleteData());


        //工程管理
        this.requestHandlerMap.put("project.create",new CreateProject());
        this.requestHandlerMap.put("project.delete",new DeleteProject());
        this.requestHandlerMap.put("project.update",new UpdateProject());
        this.requestHandlerMap.put("project.search",new SearchProject());

        //数据激励
        this.requestHandlerMap.put("dataIncentive.create",new CreateDataIncentive());
        this.requestHandlerMap.put("dataIncentive.delete",new DeleteDataIncentive());
        this.requestHandlerMap.put("dataIncentive.update",new UpdateDataIncentive());
        this.requestHandlerMap.put("dataIncentive.search",new SearchDataIncentive());
        this.requestHandlerMap.put("dataIncentive.ctr",new CtrDataIncentive());

        //日志管理
        this.requestHandlerMap.put("log.get",new GetLog());
        this.requestHandlerMap.put("log.delete",new DeleteLog());









    }


    public static RequestProcessor getInstance() {
        if (RequestProcessor.instance == null) {
            RequestProcessor.instance = new RequestProcessor();
        }
        return RequestProcessor.instance;
    }

    private boolean isRequestValid(final Request request) {
        return true;
    }


    public void processRequest(final RoutingContext routingContext, final Request request) throws Exception {
        final AbstractRequestHandler requestHandler = this.requestHandlerMap.get(request.getMethod());
        if (requestHandler == null) {
            RequestProcessor.logger.error(String.format("request:%s cannot find a handler", request.toString()));
            ResponseFactory response = new ResponseFactory(routingContext, request);
            response.error(METHOD_NOT_FOUND.getCode(), METHOD_NOT_FOUND.getMsg());
            return;
        }

        // 注册中间件
        Middleware mw = new Middleware(routingContext, request, requestHandler);

        // 不需要验证token的接口
        JsonArray notVerifyUserTokenArr = new JsonArray().add("user.Login").add("TargetMachineManage.CheckSysStatus")
                .add("TargetMachineManage.getData").add("TargetMachineManage.CreateTarget").add("Acquisition.DataCollect")
                .add("AcquisitionDataAnalyse.GetShowingDataSet").add("AcquisitionConfig.getTargetMachine").add("Acquisition.CommunicationCommand")
                .add("TargetMachineManage.CommandSetCommit").add("TargetMachineManage.CommandSetName").add("TargetMachineManage.CommandRequestInfo")
                .add("TargetMachineManage.CommandRunInfo").add("Acquisition.CommunicationCommand").add("Acquisition.GetMirrorSize");
//
//        if (!notVerifyUserTokenArr.contains(request.getMethod())) {
//            mw.verifyUserToken();
//        } else {
            logger.info(String.format("request:%s find a handler", request.toString()));
            requestHandler.handle(routingContext, request);
//        }

    }
}
