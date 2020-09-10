package org.nit.monitorserver.handler.task;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.datagram.DatagramSocket;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.sql.SQLClient;
import org.apache.log4j.Logger;
import org.nit.monitorserver.ICDParse.CollectingICDParse;
import org.nit.monitorserver.constant.GlobalConsts;
import org.nit.monitorserver.database.MongoConnection;
import org.nit.monitorserver.database.MysqlConnection;
import org.nit.monitorserver.proto.*;
import org.nit.monitorserver.util.FormValidator;
import org.nit.monitorserver.util.IniUtil;
import org.nit.monitorserver.util.Tools;


import java.util.ArrayList;
import java.util.List;

import static org.nit.monitorserver.constant.ResponseError.FORMAT_ERROR;
import static org.nit.monitorserver.constant.ResponseError.INSERT_FAILURE;
import static org.nit.monitorserver.constant.ResponseError.PARAM_REQUIRED;
/**
 * 功能描述: <创建采集任务，不需要开始和停止，包括分析数据的采集>
 * 〈〉
 * @Author: 20643
 * @Date: 2020-9-1 14:26
 */
public class CreateTask {
    private final SQLClient mySQLClient = new MysqlConnection().getMySQLClient();
    private final MongoClient mongoClient = new MongoConnection().getMongoClient();
    private Vertx vertx = Vertx.vertx();
    private int port = IniUtil.getInstance().getUdpServerPort();
    private String host = IniUtil.getInstance().getUdpServerHost();
    public static String targetIP = null;
    CollectingICDParse collectingICDParse = new CollectingICDParse();


    public Future<JsonObject> EDT_TDR_CFGCTR_MSG(String targetIp, Logger logger) {
        JsonObject condition = new JsonObject();
        JsonObject result = new JsonObject();
        JsonObject analysisCfg = new JsonObject();
        JsonObject response = new JsonObject();
        String query = "SELECT * FROM detrcd_latest_table;";
        Future future = Future.future();

        mySQLClient.query(query, res -> {
            System.out.println("开始查询数据库");
            if(res.succeeded()){
                System.out.println("eventType:" + res.result().getRows());
                int eventType = res.result().getRows().get(0).getInteger("drt_eventtype");//事件ID


                byte[] TdrMsgBuffer = res.result().getRows().get(0).getBinary("drt_data");
                String portName = res.result().getRows().get(0).getString("drt_portname");
                System.out.println("TdrMsgBuffer:" + TdrMsgBuffer);
                result.put("drt_data", TdrMsgBuffer).put("targetIP", targetIp).put("portName", portName);

                try {
                    switch (eventType) {
                        case 10000:
                            System.out.println("开始执行10000");

//                        TdrMsg.EDT_TASKSPAWN_INFO taskspawnResult=TdrMsg.EDT_TASKSPAWN_INFO.parseFrom(TdrMsgBuffer);
//                        result.put("evtId",taskspawnResult.getEvtId());
//                        result.put("timeStamp",taskspawnResult.getTimeStamp());
//                        result.put("pdId",taskspawnResult.getPdId());
//                        result.put("portId",taskspawnResult.getPortId());
//                        result.put("taskId",taskspawnResult.getTskSpawn().getTaskId());
//                        result.put("priority",taskspawnResult.getTskSpawn().getPriority());
//                        result.put("stackSize",taskspawnResult.getTskSpawn().getStackSize());
//                        result.put("entryPoint",taskspawnResult.getTskSpawn().getEntryPoint());
//                        result.put("options",taskspawnResult.getTskSpawn().getOptions());
//                        result.put("objOwnerId",taskspawnResult.getTskSpawn().getObjOwnerId());

                            int a = ((int) (Math.random() * (100 - 0))) + 0;
                            int b = ((int) (Math.random() * (100 - 0))) + 0;
                            int c = ((int) (Math.random() * (100 - 0))) + 0;


                            EDT_SEMCCREATE_INFO edt_taskspawn_info = new EDT_SEMCCREATE_INFO(10000, a, b, c);
                            EDT_SEMCCREATE_INFO.EDT_TASKSPAWN_T edt_taskspawn_t = edt_taskspawn_info.getEDT_TASKSPAWN_T();

                            result.put("evtId", edt_taskspawn_info.getEvtId());
                            result.put("timeStamp", edt_taskspawn_info.getTimeStamp());
                            result.put("pdId", edt_taskspawn_info.getPdId());
                            result.put("portId", edt_taskspawn_info.getPortId());
                            result.put("taskId", edt_taskspawn_t.getTaskId());
                            result.put("priority", edt_taskspawn_t.getPriority());
                            result.put("stackSize", edt_taskspawn_t.getStackSize());
                            result.put("entryPoint", edt_taskspawn_t.getEntryPoint());
                            result.put("options", edt_taskspawn_t.getOptions());
                            result.put("objOwnerId", edt_taskspawn_t.getObjOwnerId()).put("type", "任务创建");
                            System.out.println("10000中的结果为："+result);



                            break;
                        case 10001:
                            TdrMsg.EDT_TASKDESTROY_INFO taskdestroy = TdrMsg.EDT_TASKDESTROY_INFO.parseFrom(TdrMsgBuffer);
                            result.put("evtId", taskdestroy.getEvtId());
                            result.put("timeStamp", taskdestroy.getTimeStamp());
                            result.put("pdId", taskdestroy.getPdId());
                            result.put("portId", taskdestroy.getPortId());
                            result.put("taskId", taskdestroy.getTskDestroy().getTaskId());
                            result.put("taskClassId", taskdestroy.getTskDestroy().getTaskClassId());
                            result.put("taskId_1", taskdestroy.getTskDestroy().getTaskId1());
                            result.put("safeCnt", taskdestroy.getTskDestroy().getSafeCnt()).put("type", "任务删除");


                            break;
                        case 10048:
                            TdrMsg.EDT_IOCREATE_OPEN_INFO edt_iocreate_open_info_open = TdrMsg.EDT_IOCREATE_OPEN_INFO.parseFrom(TdrMsgBuffer);
                            result.put("evtId", edt_iocreate_open_info_open.getEvtId());
                            result.put("timeStamp", edt_iocreate_open_info_open.getTimeStamp());
                            result.put("pdId", edt_iocreate_open_info_open.getPdId());
                            result.put("portId", edt_iocreate_open_info_open.getPortId());
                            result.put("objId", edt_iocreate_open_info_open.getIoCOpen().getObjId());
                            result.put("classId", edt_iocreate_open_info_open.getIoCOpen().getClassId());
                            result.put("nameLen", edt_iocreate_open_info_open.getIoCOpen().getNameLen());
                            result.put("name", edt_iocreate_open_info_open.getIoCOpen().getName());
                            result.put("fd", edt_iocreate_open_info_open.getIoCOpen().getFd());
                            result.put("flag", edt_iocreate_open_info_open.getIoCOpen().getFlag()).put("type", "IO_open");


                            break;
                        case 10053:
                            TdrMsg.EDT_IOCREATE_OPEN_INFO edt_iocreate_open_info_create = TdrMsg.EDT_IOCREATE_OPEN_INFO.parseFrom(TdrMsgBuffer);
                            result.put("evtId", edt_iocreate_open_info_create.getEvtId());
                            result.put("timeStamp", edt_iocreate_open_info_create.getTimeStamp());
                            result.put("pdId", edt_iocreate_open_info_create.getPdId());
                            result.put("portId", edt_iocreate_open_info_create.getPortId());
                            result.put("objId", edt_iocreate_open_info_create.getIoCOpen().getObjId());
                            result.put("classId", edt_iocreate_open_info_create.getIoCOpen().getClassId());
                            result.put("nameLen", edt_iocreate_open_info_create.getIoCOpen().getNameLen());
                            result.put("name", edt_iocreate_open_info_create.getIoCOpen().getName());
                            result.put("fd", edt_iocreate_open_info_create.getIoCOpen().getFd());
                            result.put("flag", edt_iocreate_open_info_create.getIoCOpen().getFlag()).put("type", "IO_create");


                            break;

                        case 10052:

                            TdrMsg.EDT_IOCLOSE_INFO ioclose_info = TdrMsg.EDT_IOCLOSE_INFO.parseFrom(TdrMsgBuffer);
                            result.put("evtId", ioclose_info.getEvtId());
                            result.put("timeStamp", ioclose_info.getTimeStamp());
                            result.put("pdId", ioclose_info.getPdId());
                            result.put("portId", ioclose_info.getPortId());
                            result.put("objId", ioclose_info.getIoClose().getObjId());
                            result.put("classId", ioclose_info.getIoClose().getClassId());
                            result.put("fd", ioclose_info.getIoClose().getFd()).put("type", "IO_close");

                            break;
                        case 10054:
                            TdrMsg.EDT_IODELETE_INFO iodelete_info = TdrMsg.EDT_IODELETE_INFO.parseFrom(TdrMsgBuffer);
                            result.put("evtId", iodelete_info.getEvtId());
                            result.put("timeStamp", iodelete_info.getTimeStamp());
                            result.put("pdId", iodelete_info.getPdId());
                            result.put("portId", iodelete_info.getPortId());
                            result.put("length", iodelete_info.getIoDelete().getLength());
                            result.put("classId", iodelete_info.getIoDelete().getClassId());
                            result.put("name", iodelete_info.getIoDelete().getName()).put("type", "IO_delete");

                            break;
                        case 10008:
                            TdrMsg.EDT_SEMBCREATE_INFO sembcreate = TdrMsg.EDT_SEMBCREATE_INFO.parseFrom(TdrMsgBuffer);
                            result.put("evtId", sembcreate.getEvtId());
                            result.put("timeStamp", sembcreate.getTimeStamp());
                            result.put("pdId", sembcreate.getPdId());
                            result.put("portId", sembcreate.getPortId());
                            result.put("semId", sembcreate.getSemBCreat().getSemId());
                            result.put("classId", sembcreate.getSemBCreat().getClassId());
                            result.put("semId_1", sembcreate.getSemBCreat().getSemId1());
                            result.put("options", sembcreate.getSemBCreat().getOptions());
                            result.put("owner", sembcreate.getSemBCreat().getOwner());
                            result.put("objOwnerId", sembcreate.getSemBCreat().getObjOwnerId()).put("type", "二值信号量创建");


                            break;
                        case 10009:
                            TdrMsg.EDT_SEMCCREATE_INFO EDT_SEMCCREATE_INFO = TdrMsg.EDT_SEMCCREATE_INFO.parseFrom(TdrMsgBuffer);
                            result.put("evtId", EDT_SEMCCREATE_INFO.getEvtId());
                            result.put("timeStamp", EDT_SEMCCREATE_INFO.getTimeStamp());
                            result.put("pdId", EDT_SEMCCREATE_INFO.getPdId());
                            result.put("portId", EDT_SEMCCREATE_INFO.getPortId());
                            result.put("semId", EDT_SEMCCREATE_INFO.getSemCCreat().getSemId());
                            result.put("classId", EDT_SEMCCREATE_INFO.getSemCCreat().getClassId());
                            result.put("semId_1", EDT_SEMCCREATE_INFO.getSemCCreat().getSemId1());
                            result.put("options", EDT_SEMCCREATE_INFO.getSemCCreat().getOptions());
                            result.put("initCount", EDT_SEMCCREATE_INFO.getSemCCreat().getInitCount());
                            result.put("objOwnerId", EDT_SEMCCREATE_INFO.getSemCCreat().getObjOwnerId()).put("type", "计数信号量创建");
//                        EDT_SEMCCREATE_INFO edt_semccreate_info = new EDT_SEMCCREATE_INFO(10009,4,8,9);
//                        EDT_SEMCCREATE_INFO.semCCreat semCCreat = edt_semccreate_info.getsemCCreat();
//                        result.put("evtId",edt_semccreate_info.getEvtId());
//                        result.put("timeStamp",edt_semccreate_info.getTimeStamp());
//                        result.put("pdId",edt_semccreate_info.getPdId());
//                        result.put("portId",edt_semccreate_info.getPortId());
//                        result.put("semId",semCCreat.getSemId());
//                        result.put("classId",semCCreat.getClassId());
//                        result.put("semId_1",semCCreat.getSemId_1());
//                        result.put("options",semCCreat.getOptions());
//                        result.put("initCount",semCCreat.getInitCount());
//                        result.put("objOwnerId",semCCreat.getObjOwnerId());


                            break;
                        case 10013:
                            TdrMsg.EDT_SEMMCREATE_INFO semmcreate = TdrMsg.EDT_SEMMCREATE_INFO.parseFrom(TdrMsgBuffer);
                            result.put("evtId", semmcreate.getEvtId());
                            result.put("timeStamp", semmcreate.getTimeStamp());
                            result.put("pdId", semmcreate.getPdId());
                            result.put("portId", semmcreate.getPortId());
                            result.put("semId", semmcreate.getSemMCreat().getSemId());
                            result.put("classId", semmcreate.getSemMCreat().getClassId());
                            result.put("semId_1", semmcreate.getSemMCreat().getSemId1());
                            result.put("options", semmcreate.getSemMCreat().getOptions());
                            result.put("options", semmcreate.getSemMCreat().getOptions());
                            result.put("objOwnerId", semmcreate.getSemMCreat().getObjOwnerId()).put("type", "互斥信号量创建");


                            break;
                        case 10010:
                            TdrMsg.EDT_SEMDELETE_INFO semdelete_info = TdrMsg.EDT_SEMDELETE_INFO.parseFrom(TdrMsgBuffer);
                            result.put("evtId", semdelete_info.getEvtId());
                            result.put("timeStamp", semdelete_info.getTimeStamp());
                            result.put("pdId", semdelete_info.getPdId());
                            result.put("portId", semdelete_info.getPortId());
                            result.put("semId", semdelete_info.getSemDelete().getSemId());
                            result.put("classId", semdelete_info.getSemDelete().getClassId());
                            result.put("semId_1", semdelete_info.getSemDelete().getSemId1());
                            result.put("owner", semdelete_info.getSemDelete().getOwner());
                            result.put("recurse", semdelete_info.getSemDelete().getRecurse());
                            result.put("qHeadAddr", semdelete_info.getSemDelete().getQHeadAddr()).put("type", "信号量删除");


                            break;
                        case 10016:
                            TdrMsg.EDT_WDCREATE_INFO wdcreate = TdrMsg.EDT_WDCREATE_INFO.parseFrom(TdrMsgBuffer);
                            result.put("evtId", wdcreate.getEvtId());
                            result.put("timeStamp", wdcreate.getTimeStamp());
                            result.put("pdId", wdcreate.getPdId());
                            result.put("portId", wdcreate.getPortId());
                            result.put("wdId", wdcreate.getWdCreat().getWdId());
                            result.put("classId", wdcreate.getWdCreat().getClassId());
                            result.put("wdId_1", wdcreate.getWdCreat().getWdId1());
                            result.put("objOwnerId", wdcreate.getWdCreat().getObjOwnerId()).put("type", "看门狗定时器创建");


                            break;
                        case 10017:
                            TdrMsg.EDT_WDDELETE_INFO wddelete_info = TdrMsg.EDT_WDDELETE_INFO.parseFrom(TdrMsgBuffer);
                            result.put("evtId", wddelete_info.getEvtId());
                            result.put("timeStamp", wddelete_info.getTimeStamp());
                            result.put("pdId", wddelete_info.getPdId());
                            result.put("portId", wddelete_info.getPortId());
                            result.put("wdId", wddelete_info.getWdDelete().getWdId());
                            result.put("classId", wddelete_info.getWdDelete().getClassId());
                            result.put("wdId_1", wddelete_info.getWdDelete().getWdId1()).put("type", "看门狗定时器删除");


                            break;
                        case 10020:
                            TdrMsg.EDT_MSGQCREATE_INFO msgqcreate_info = TdrMsg.EDT_MSGQCREATE_INFO.parseFrom(TdrMsgBuffer);
                            result.put("evtId", msgqcreate_info.getEvtId());
                            result.put("timeStamp", msgqcreate_info.getTimeStamp());
                            result.put("pdId", msgqcreate_info.getPdId());
                            result.put("portId", msgqcreate_info.getPortId());
                            result.put("msgQId", msgqcreate_info.getMsgQCreat().getMsgQId());
                            result.put("classId", msgqcreate_info.getMsgQCreat().getClassId());
                            result.put("msgQId_1", msgqcreate_info.getMsgQCreat().getMsgQId1());
                            result.put("maxMsgs", msgqcreate_info.getMsgQCreat().getMaxMsgs());
                            result.put("maxMsgLen", msgqcreate_info.getMsgQCreat().getMaxMsgLen());
                            result.put("options", msgqcreate_info.getMsgQCreat().getOptions()).put("type", "消息队列创建");

                            break;
                        case 10021:
                            TdrMsg.EDT_MSGQDELETE_INFO msgqdelete_info = TdrMsg.EDT_MSGQDELETE_INFO.parseFrom(TdrMsgBuffer);
                            result.put("evtId", msgqdelete_info.getEvtId());
                            result.put("timeStamp", msgqdelete_info.getTimeStamp());
                            result.put("pdId", msgqdelete_info.getPdId());
                            result.put("portId", msgqdelete_info.getPortId());
                            result.put("msgQId", msgqdelete_info.getMsgQDelete().getMsgQId());
                            result.put("classId", msgqdelete_info.getMsgQDelete().getClassId());
                            result.put("msgQId_1", msgqdelete_info.getMsgQDelete().getMsgQId1()).put("type", "消息队列删除");


                            break;
                        case 40018:
                            TdrMsg.EDT_SYNTIME_INFO edt_syntime_info = TdrMsg.EDT_SYNTIME_INFO.parseFrom(TdrMsgBuffer);
                            result.put("evtId", edt_syntime_info.getEvtId());
                            result.put("timeStamp", edt_syntime_info.getTimeStamp());
                            result.put("pdId", edt_syntime_info.getPdId());
                            result.put("portId", edt_syntime_info.getPortId());
                            result.put("localTime", edt_syntime_info.getLocalTime());
                            result.put("vxFlag", edt_syntime_info.getVxFlag()).put("type", "同步信息");


                            break;
                        case 40013:
                            TdrMsg.EDT_FUNC_TIME_USE_INFO edt_func_time_use_info = TdrMsg.EDT_FUNC_TIME_USE_INFO.parseFrom(TdrMsgBuffer);
                            result.put("evtId", edt_func_time_use_info.getEvtId());
                            result.put("timeStamp", edt_func_time_use_info.getTimeStamp());
                            result.put("pdId", edt_func_time_use_info.getPdId());
                            result.put("portId", edt_func_time_use_info.getPortId());
                            result.put("evtflag", edt_func_time_use_info.getEvtflag());
                            result.put("tmpCnt", edt_func_time_use_info.getTmpCnt());
                            result.put("funcId", edt_func_time_use_info.getFuncId()).put("type", "特定函数执行用时");

                            break;
                        case 10022:
                            TdrMsg.EDT_MSGQRECV_INFO msgqrecv_info = TdrMsg.EDT_MSGQRECV_INFO.parseFrom(TdrMsgBuffer);
                            result.put("evtId", msgqrecv_info.getEvtId());
                            result.put("timeStamp", msgqrecv_info.getTimeStamp());
                            result.put("pdId", msgqrecv_info.getPdId());
                            result.put("portId", msgqrecv_info.getPortId());
                            result.put("msgQId", msgqrecv_info.getMsgQId());
                            result.put("objType", msgqrecv_info.getObjType());
                            result.put("timeout", msgqrecv_info.getTimeout());
                            result.put("recvMaxLen", msgqrecv_info.getRecvMaxLen()).put("type", "消息队列接收");
                            for (int i = 0; i < msgqrecv_info.getMsgQBufCount(); i++) {
                                JsonObject msgqrecv_res = new JsonObject();
                                msgqrecv_res.put("msgQBuf", msgqrecv_info.getMsgQBuf(i));
                                result.put("msgQBuf" + i, msgqrecv_res);
                            }


                            break;
                        case 10023:
                            TdrMsg.EDT_MSGQSEND_INFO msgqsend_info = TdrMsg.EDT_MSGQSEND_INFO.parseFrom(TdrMsgBuffer);
                            result.put("evtId", msgqsend_info.getEvtId());
                            result.put("timeStamp", msgqsend_info.getTimeStamp());
                            result.put("pdId", msgqsend_info.getPdId());
                            result.put("portId", msgqsend_info.getPortId());
                            result.put("msgQId", msgqsend_info.getMsgQId());
                            result.put("objType", msgqsend_info.getObjType());
                            result.put("timeout", msgqsend_info.getTimeout());
                            result.put("priority", msgqsend_info.getPriority());
                            result.put("sendLen", msgqsend_info.getSendLen()).put("type", "消息队列发送");
                            for (int i = 0; i < msgqsend_info.getMsgQBufCount(); i++) {
                                JsonObject msgqsend_res = new JsonObject();
                                msgqsend_res.put("msgQBuf" + i, msgqsend_info.getMsgQBuf(i));
                                result.put("msgQBuf" + i, msgqsend_res);
                            }


                            break;
                        case 10500:
                            TdrMsg.EDT_CRE_SEMA_INFO edt_cre_sema_info = TdrMsg.EDT_CRE_SEMA_INFO.parseFrom(TdrMsgBuffer);
                            result.put("evtId", edt_cre_sema_info.getEvtId());
                            result.put("timeStamp", edt_cre_sema_info.getTimeStamp());
                            result.put("pdId", edt_cre_sema_info.getPdId());
                            result.put("portId", edt_cre_sema_info.getPortId());
                            result.put("semaId", edt_cre_sema_info.getSemaId());
                            result.put("currentValue", edt_cre_sema_info.getCurrentValue());
                            result.put("maxValue", edt_cre_sema_info.getMaxValue());
                            result.put("queuingDiscipline", edt_cre_sema_info.getQueuingDiscipline());
                            result.put("name", edt_cre_sema_info.getName()).put("type", "APEX_SEMPHORE创建");

                            break;
                        case 10510:
                            TdrMsg.EDT_CRE_BLACKBOARD_INFO edt_cre_blackboard = TdrMsg.EDT_CRE_BLACKBOARD_INFO.parseFrom(TdrMsgBuffer);
                            result.put("evtId", edt_cre_blackboard.getEvtId());
                            result.put("timeStamp", edt_cre_blackboard.getTimeStamp());
                            result.put("pdId", edt_cre_blackboard.getPdId());
                            result.put("portId", edt_cre_blackboard.getPortId());
                            result.put("blackBoardId", edt_cre_blackboard.getBlackBoardId());
                            result.put("msgSize", edt_cre_blackboard.getMsgSize());
                            result.put("name", edt_cre_blackboard.getName()).put("type", "APEX_BL");

                            break;
                        case 10503:
                            TdrMsg.EDT_CRE_BUFFER_INFO edt_cre_buffer_info = TdrMsg.EDT_CRE_BUFFER_INFO.parseFrom(TdrMsgBuffer);
                            result.put("evtId", edt_cre_buffer_info.getEvtId());
                            result.put("timeStamp", edt_cre_buffer_info.getTimeStamp());
                            result.put("pdId", edt_cre_buffer_info.getPdId());
                            result.put("portId", edt_cre_buffer_info.getPortId());

                            result.put("bufferId", edt_cre_buffer_info.getBufferId());
                            result.put("maxMsgSize", edt_cre_buffer_info.getMaxMsgSize());
                            result.put("maxMsgNum", edt_cre_buffer_info.getMaxMsgNum());
                            result.put("queuingDiscipling", edt_cre_buffer_info.getQueuingDiscipling());
                            result.put("name", edt_cre_buffer_info.getName()).put("type", "APEX_BUFFER创建");


                            break;
                        case 10517:
                            TdrMsg.EDT_PROCCREATE_INFO edt_proccreate_info = TdrMsg.EDT_PROCCREATE_INFO.parseFrom(TdrMsgBuffer);
                            result.put("evtId", edt_proccreate_info.getEvtId());
                            result.put("timeStamp", edt_proccreate_info.getTimeStamp());
                            result.put("pdId", edt_proccreate_info.getPdId());
                            result.put("portId", edt_proccreate_info.getPortId());

                            result.put("procId", edt_proccreate_info.getProcId());
                            result.put("pri", edt_proccreate_info.getPri());
                            result.put("stackSize", edt_proccreate_info.getStackSize());
                            result.put("period", edt_proccreate_info.getPeriod());
                            result.put("deadline", edt_proccreate_info.getDeadline());
                            result.put("name", edt_proccreate_info.getName()).put("type", "APEX_PROCESS创建");

                            break;
                        case 10506:
                            TdrMsg.EDT_CRE_EVENT_INFO edt_cre_event_info = TdrMsg.EDT_CRE_EVENT_INFO.parseFrom(TdrMsgBuffer);
                            result.put("evtId", edt_cre_event_info.getEvtId());
                            result.put("timeStamp", edt_cre_event_info.getTimeStamp());
                            result.put("pdId", edt_cre_event_info.getPdId());
                            result.put("portId", edt_cre_event_info.getPortId());

                            result.put("eventId", edt_cre_event_info.getEventId());
                            result.put("state", edt_cre_event_info.getState());
                            result.put("name", edt_cre_event_info.getName()).put("type", "APEX_EVENT创建");


                            break;
                        case 10527:
                            TdrMsg.EDT_CRE_SAMPLING_PORT_INFO edt_cre_sampling_port_info = TdrMsg.EDT_CRE_SAMPLING_PORT_INFO.parseFrom(TdrMsgBuffer);
                            result.put("evtId", edt_cre_sampling_port_info.getEvtId());
                            result.put("timeStamp", edt_cre_sampling_port_info.getTimeStamp());
                            result.put("pdId", edt_cre_sampling_port_info.getPdId());
                            result.put("portId", edt_cre_sampling_port_info.getPortId());

                            result.put("sPortId", edt_cre_sampling_port_info.getSPortId());
                            result.put("refreshPeriod", edt_cre_sampling_port_info.getRefreshPeriod());
                            result.put("maxMsgSize", edt_cre_sampling_port_info.getMaxMsgSize());
                            result.put("portDirection", edt_cre_sampling_port_info.getPortDirection());
                            result.put("name", edt_cre_sampling_port_info.getName()).put("type", "APEX_SANPLIN_PORT创建");

                            break;
                        case 10530:
                            TdrMsg.EDT_CRE_QUEUING_PORT_INFO edt_cre_queuing_port_info = TdrMsg.EDT_CRE_QUEUING_PORT_INFO.parseFrom(TdrMsgBuffer);
                            result.put("evtId", edt_cre_queuing_port_info.getEvtId());
                            result.put("timeStamp", edt_cre_queuing_port_info.getTimeStamp());
                            result.put("pdId", edt_cre_queuing_port_info.getPdId());
                            result.put("portId", edt_cre_queuing_port_info.getPortId());

                            result.put("qPortId", edt_cre_queuing_port_info.getQPortId());
                            result.put("maxMsgSize", edt_cre_queuing_port_info.getMaxMsgSize());
                            result.put("maxMsgNum", edt_cre_queuing_port_info.getMaxMsgNum());
                            result.put("queuingDiscipline", edt_cre_queuing_port_info.getQueuingDiscipline());
                            result.put("portDirection", edt_cre_queuing_port_info.getPortDirection());
                            result.put("name", edt_cre_queuing_port_info.getName()).put("type", "APEX_QUEUING_PORT创建");


                            break;
                        case 10600:
                            TdrMsg.EDT_TIMERCREAT_INFO edt_timercreta = TdrMsg.EDT_TIMERCREAT_INFO.parseFrom(TdrMsgBuffer);
                            result.put("evtId", edt_timercreta.getEvtId());
                            result.put("timeStamp", edt_timercreta.getTimeStamp());
                            result.put("pdId", edt_timercreta.getPdId());
                            result.put("portId", edt_timercreta.getPortId());
                            result.put("clock_id", edt_timercreta.getClockId());
                            result.put("handle", edt_timercreta.getHandle());
                            result.put("timer_id", edt_timercreta.getTimerId()).put("type", "POSIX_TIMER创建");


                            break;
                        case 10603:
                            TdrMsg.EDT_TIMERDELETE_INFO edt_timerdelete = TdrMsg.EDT_TIMERDELETE_INFO.parseFrom(TdrMsgBuffer);
                            result.put("evtId", edt_timerdelete.getEvtId());
                            result.put("timeStamp", edt_timerdelete.getTimeStamp());
                            result.put("pdId", edt_timerdelete.getPdId());
                            result.put("portId", edt_timerdelete.getPortId());
                            result.put("timer_id", edt_timerdelete.getTimerId()).put("type", "POSIX_TIMR删除");


                            break;
                        case 10512:
                            TdrMsg.EDT_READ_BLACKBOARD_INFO read_blackboard_info = TdrMsg.EDT_READ_BLACKBOARD_INFO.parseFrom(TdrMsgBuffer);
//                        if(read_blackboard_info.hasBalckBoardId()){
//
//                        }
                            result.put("evtId", read_blackboard_info.getEvtId());
                            result.put("timeStamp", read_blackboard_info.getTimeStamp());
                            result.put("pdId", read_blackboard_info.getPdId());
                            result.put("portId", read_blackboard_info.getPortId());

                            result.put("balckBoardId", read_blackboard_info.getBalckBoardId());
                            result.put("timeout", read_blackboard_info.getTimeOut());
                            result.put("state", read_blackboard_info.getState());
                            result.put("objType", read_blackboard_info.getObjType());
                            result.put("name", read_blackboard_info.getName());
                            result.put("msgLength", read_blackboard_info.getMsgLength()).put("type", "读黑板");
                            for (int i = 0; i < read_blackboard_info.getMessageCount(); i++) {
                                JsonObject read_blackboard_res = new JsonObject();
                                read_blackboard_res.put("message", read_blackboard_info.getMessage(i));
                                result.put("message" + i, read_blackboard_res);
                            }
                            collectingICDParse.collectICDParse(portName, TdrMsgBuffer, read_blackboard_info.getTimeStamp());


                            break;
                        case 10513:
                            TdrMsg.EDT_CLR_BLACKBOARD_INFO clr_blackboard_info = TdrMsg.EDT_CLR_BLACKBOARD_INFO.parseFrom(TdrMsgBuffer);
                            result.put("evtId", clr_blackboard_info.getEvtId());
                            result.put("timeStamp", clr_blackboard_info.getTimeStamp());
                            result.put("pdId", clr_blackboard_info.getPdId());
                            result.put("portId", clr_blackboard_info.getPortId());
                            result.put("balckBoardId", clr_blackboard_info.getBalckBoardId());
                            result.put("state", clr_blackboard_info.getState());
                            result.put("objType", clr_blackboard_info.getObjType());
                            result.put("name", clr_blackboard_info.getName()).put("type", "清除黑板");


                            break;
                        case 10511:
                            TdrMsg.EDT_DIS_BLACKBOARD_INFO dis_blackboard_info = TdrMsg.EDT_DIS_BLACKBOARD_INFO.parseFrom(TdrMsgBuffer);
                            result.put("evtId", dis_blackboard_info.getEvtId());
                            result.put("timeStamp", dis_blackboard_info.getTimeStamp());
                            result.put("pdId", dis_blackboard_info.getPdId());
                            result.put("portId", dis_blackboard_info.getPortId());

                            result.put("balckBoardId", dis_blackboard_info.getBalckBoardId());
                            result.put("displayLength", dis_blackboard_info.getDisplayLength());
                            result.put("name", dis_blackboard_info.getName());
                            result.put("objType", dis_blackboard_info.getObjType());
                            result.put("msgLength", dis_blackboard_info.getMsgLength()).put("type", "显示黑板");
                            for (int i = 0; i < dis_blackboard_info.getMessageCount(); i++) {
                                JsonObject dis_blackboard_res = new JsonObject();
                                dis_blackboard_res.put("message", dis_blackboard_info.getMessage(i));
                                result.put("message" + i, dis_blackboard_res);
                            }
                            collectingICDParse.collectICDParse(portName, TdrMsgBuffer, dis_blackboard_info.getTimeStamp());


                            break;
                        case 10504:
                            TdrMsg.EDT_SEND_BUFFER_INFO send_buffer_info = TdrMsg.EDT_SEND_BUFFER_INFO.parseFrom(TdrMsgBuffer);
                            result.put("evtId", send_buffer_info.getEvtId());
                            result.put("timeStamp", send_buffer_info.getTimeStamp());
                            result.put("pdId", send_buffer_info.getPdId());
                            result.put("portId", send_buffer_info.getPortId());
                            result.put("bufferId", send_buffer_info.getBufferId());
                            result.put("timeOut", send_buffer_info.getTimeOut());
                            result.put("length", send_buffer_info.getLength());
                            result.put("objType", send_buffer_info.getObjType());
                            result.put("name", send_buffer_info.getName()).put("type", "buffer发送");


                            break;
                        case 10505:
                            TdrMsg.EDT_RECV_BUFFER_INFO recv_buffer_info = TdrMsg.EDT_RECV_BUFFER_INFO.parseFrom(TdrMsgBuffer);
                            result.put("evtId", recv_buffer_info.getEvtId());
                            result.put("timeStamp", recv_buffer_info.getTimeStamp());
                            result.put("pdId", recv_buffer_info.getPdId());
                            result.put("portId", recv_buffer_info.getPortId());
                            result.put("bufferId", recv_buffer_info.getBufferId());
                            result.put("timeOut", recv_buffer_info.getTimeOut());
                            result.put("objType", recv_buffer_info.getObjType());
                            result.put("name", recv_buffer_info.getName()).put("type", "buffer接收");


                            break;
                        case 10507:
                            TdrMsg.EDT_SET_EVENT_INFO set_event_info = TdrMsg.EDT_SET_EVENT_INFO.parseFrom(TdrMsgBuffer);
                            result.put("evtId", set_event_info.getEvtId());
                            result.put("timeStamp", set_event_info.getTimeStamp());
                            result.put("pdId", set_event_info.getPdId());
                            result.put("portId", set_event_info.getPortId());
                            result.put("eventId", set_event_info.getEventId());
                            result.put("state", set_event_info.getState());
                            result.put("objType", set_event_info.getObjType());
                            result.put("name", set_event_info.getName()).put("type", "Set Event");

                            break;
                        case 10508:
                            TdrMsg.EDT_RESET_EVENT_INFO reset_event_info = TdrMsg.EDT_RESET_EVENT_INFO.parseFrom(TdrMsgBuffer);
                            result.put("evtId", reset_event_info.getEvtId());
                            result.put("timeStamp", reset_event_info.getTimeStamp());
                            result.put("pdId", reset_event_info.getPdId());
                            result.put("portId", reset_event_info.getPortId());
                            result.put("eventId", reset_event_info.getEventId());
                            result.put("state", reset_event_info.getState());
                            result.put("objType", reset_event_info.getObjType());
                            result.put("name", reset_event_info.getName()).put("type", "Rest Event");


                            break;
                        case 10509:
                            TdrMsg.EDT_WAIT_EVENT_INFO wait_event_info = TdrMsg.EDT_WAIT_EVENT_INFO.parseFrom(TdrMsgBuffer);
                            result.put("evtId", wait_event_info.getEvtId());
                            result.put("timeStamp", wait_event_info.getTimeStamp());
                            result.put("pdId", wait_event_info.getPdId());
                            result.put("portId", wait_event_info.getPortId());

                            result.put("eventId", wait_event_info.getEventId());
                            result.put("state", wait_event_info.getState());
                            result.put("objType", wait_event_info.getObjType());
                            result.put("name", wait_event_info.getName()).put("type", "Wait Event");


                            break;
                        case 10528:
                            TdrMsg.EDT_WRITE_SAMPLING_MSG_INFO write_sampling_msg_info = TdrMsg.EDT_WRITE_SAMPLING_MSG_INFO.parseFrom(TdrMsgBuffer);
                            result.put("evtId", write_sampling_msg_info.getEvtId());
                            result.put("timeStamp", write_sampling_msg_info.getTimeStamp());
                            result.put("pdId", write_sampling_msg_info.getPdId());
                            result.put("portId", write_sampling_msg_info.getPortId());

                            result.put("sPortId", write_sampling_msg_info.getSPortId());
                            result.put("length", write_sampling_msg_info.getLength());
                            result.put("objType", write_sampling_msg_info.getObjType());
                            result.put("name", write_sampling_msg_info.getName()).put("type", "写Sport");
                            for (int i = 0; i < write_sampling_msg_info.getMessageCount(); i++) {
                                JsonObject write_sampling_msg_res = new JsonObject();
                                write_sampling_msg_res.put("message", write_sampling_msg_info.getMessage(i));
                                result.put("message" + i, write_sampling_msg_res);
                            }
                            collectingICDParse.collectICDParse(portName, TdrMsgBuffer, write_sampling_msg_info.getTimeStamp());

                            break;
                        case 10529:
                            TdrMsg.EDT_READ_SAMPLING_MSG_INFO read_sampling_msg_info = TdrMsg.EDT_READ_SAMPLING_MSG_INFO.parseFrom(TdrMsgBuffer);
                            result.put("evtId", read_sampling_msg_info.getEvtId());
                            result.put("timeStamp", read_sampling_msg_info.getTimeStamp());
                            result.put("pdId", read_sampling_msg_info.getPdId());
                            result.put("portId", read_sampling_msg_info.getPortId());
                            result.put("sPortId", read_sampling_msg_info.getSPortId());
                            result.put("objType", read_sampling_msg_info.getObjType());
                            result.put("name", read_sampling_msg_info.getName()).put("type", "读Sport");
                            collectingICDParse.collectICDParse(portName, TdrMsgBuffer, read_sampling_msg_info.getTimeStamp());


                            break;
                        case 10531:
                            TdrMsg.EDT_SEND_QUEUING_MSG_INFO send_queuing_msg_info = TdrMsg.EDT_SEND_QUEUING_MSG_INFO.parseFrom(TdrMsgBuffer);
                            result.put("evtId", send_queuing_msg_info.getEvtId());
                            result.put("timeStamp", send_queuing_msg_info.getTimeStamp());
                            result.put("pdId", send_queuing_msg_info.getPdId());
                            result.put("portId", send_queuing_msg_info.getPortId());
                            result.put("qPortId", send_queuing_msg_info.getQPortId());
                            result.put("timeOut", send_queuing_msg_info.getTimeOut());
                            result.put("length", send_queuing_msg_info.getLength());
                            result.put("objType", send_queuing_msg_info.getObjType());
                            result.put("name", send_queuing_msg_info.getName()).put("type", "写Qport");
                            for (int i = 0; i < send_queuing_msg_info.getMessageCount(); i++) {
                                JsonObject send_queuing_msg_res = new JsonObject();
                                send_queuing_msg_res.put("message", send_queuing_msg_info.getMessage(i));
                                result.put("message" + i, send_queuing_msg_res);
                            }
                            collectingICDParse.collectICDParse(portName, TdrMsgBuffer, send_queuing_msg_info.getTimeStamp());


                            break;
                        case 10532:
                            TdrMsg.EDT_RECV_QUEUING_MSG_INFO recv_queuing_msg_info = TdrMsg.EDT_RECV_QUEUING_MSG_INFO.parseFrom(TdrMsgBuffer);
                            result.put("evtId", recv_queuing_msg_info.getEvtId());
                            result.put("timeStamp", recv_queuing_msg_info.getTimeStamp());
                            result.put("pdId", recv_queuing_msg_info.getPdId());
                            result.put("portId", recv_queuing_msg_info.getPortId());
                            result.put("qPortId", recv_queuing_msg_info.getQPortId());
                            result.put("timeOut", recv_queuing_msg_info.getTimeOut());
                            result.put("maxMsgSize", recv_queuing_msg_info.getMaxMsgSize());
                            result.put("objType", recv_queuing_msg_info.getObjType());
                            result.put("name", recv_queuing_msg_info.getName()).put("type", "读Qport");
                            for (int i = 0; i < recv_queuing_msg_info.getMessageCount(); i++) {
                                JsonObject recv_queuing_msg_res = new JsonObject();
                                recv_queuing_msg_res.put("message", recv_queuing_msg_info.getMessage(i));
                                result.put("message" + i, recv_queuing_msg_res);
                            }
                            collectingICDParse.collectICDParse(portName, TdrMsgBuffer, recv_queuing_msg_info.getTimeStamp());


                            break;
                        case 40010:
                            TdrMsg.EDT_OSACSEND_INFO edt_osacsend_info = TdrMsg.EDT_OSACSEND_INFO.parseFrom(TdrMsgBuffer);
                            result.put("evtId", edt_osacsend_info.getEvtId());
                            result.put("timeStamp", edt_osacsend_info.getTimeStamp());
                            result.put("pdId", edt_osacsend_info.getPdId());
                            result.put("portId", edt_osacsend_info.getPortId());

                            result.put("osacId", edt_osacsend_info.getOsacId());
                            result.put("portType", edt_osacsend_info.getPortType());
                            result.put("timeOut", edt_osacsend_info.getTimeOut());
                            result.put("length", edt_osacsend_info.getName());
                            result.put("name", edt_osacsend_info.getName()).put("type", "osac发送");
                            for (int i = 0; i < edt_osacsend_info.getMessageCount(); i++) {
                                JsonObject recv_queuing_msg_res = new JsonObject();
                                recv_queuing_msg_res.put("message", edt_osacsend_info.getMessage(i));
                                result.put("message" + i, recv_queuing_msg_res);
                            }
                            collectingICDParse.collectICDParse(portName, TdrMsgBuffer, edt_osacsend_info.getTimeStamp());


                            break;
                        case 40019:
                            TdrMsg.EDT_OSACRECV_INFO edt_osacrecv = TdrMsg.EDT_OSACRECV_INFO.parseFrom(TdrMsgBuffer);
                            result.put("evtId", edt_osacrecv.getEvtId());
                            result.put("timeStamp", edt_osacrecv.getTimeStamp());
                            result.put("pdId", edt_osacrecv.getPdId());
                            result.put("portId", edt_osacrecv.getPortId());

                            result.put("osacId", edt_osacrecv.getOsacId());
                            result.put("portType", edt_osacrecv.getPortType());
                            result.put("timeOut", edt_osacrecv.getTimeOut());
                            result.put("length", edt_osacrecv.getName());
                            result.put("name", edt_osacrecv.getName()).put("type", "osac接收");
                            for (int i = 0; i < edt_osacrecv.getMessageCount(); i++) {
                                JsonObject recv_queuing_msg_res = new JsonObject();
                                recv_queuing_msg_res.put("message", edt_osacrecv.getMessage(i));
                                result.put("message" + i, recv_queuing_msg_res);
                            }
                            collectingICDParse.collectICDParse(portName, TdrMsgBuffer, edt_osacrecv.getTimeStamp());


                            break;
                        case 40028:
                            TdrMsg.EDT_CMDRST_INFO edt_cmdrst = TdrMsg.EDT_CMDRST_INFO.parseFrom(TdrMsgBuffer);
                            result.put("evtId", edt_cmdrst.getEvtId());
                            result.put("timeStamp", edt_cmdrst.getTimeStamp());
                            result.put("errnoID", edt_cmdrst.getErrnoID()).put("type", "命令执行结果");


                            break;
                        case 10030:
                            TdrMsg.EDT_MEMALLOC_INFO memalloc_info = TdrMsg.EDT_MEMALLOC_INFO.parseFrom(TdrMsgBuffer);
                            result.put("evtId", memalloc_info.getEvtId());
                            result.put("timeStamp", memalloc_info.getTimeStamp());
                            result.put("partId", memalloc_info.getMemallocInfo().getPartId());
                            result.put("alignAddr", memalloc_info.getMemallocInfo().getAlignAddr());
                            result.put("chunkSize", memalloc_info.getMemallocInfo().getChunkSize());
                            result.put("nBytes", memalloc_info.getMemallocInfo().getNBytes()).put("type", "malloc");


                            break;
                        case 10031:
                            TdrMsg.EDT_MEMFREE_INFO memfree_info = TdrMsg.EDT_MEMFREE_INFO.parseFrom(TdrMsgBuffer);
                            result.put("evtId", memfree_info.getEvtId());
                            result.put("timeStamp", memfree_info.getTimeStamp());
                            result.put("pdId", memfree_info.getPdId());
                            result.put("portId", memfree_info.getPortId());

                            result.put("partId", memfree_info.getMemFreeInfo().getPartId());
                            result.put("pBlock", memfree_info.getMemFreeInfo().getPBlock());
                            result.put("Size", memfree_info.getMemFreeInfo().getSize()).put("type", "free");


                            break;
                        case 10032:
                            TdrMsg.EDT_MEMINFOGET_INFO edt_meminfoget = TdrMsg.EDT_MEMINFOGET_INFO.parseFrom(TdrMsgBuffer);
                            result.put("evtId", edt_meminfoget.getEvtId());
                            result.put("timeStamp", edt_meminfoget.getTimeStamp());
                            result.put("pdId", edt_meminfoget.getPdId());
                            result.put("portId", edt_meminfoget.getPortId());

                            result.put("freeBytes", edt_meminfoget.getMemInfoGet().getFreeBytes());
                            result.put("freeBlocks", edt_meminfoget.getMemInfoGet().getFreeBlocks());
                            result.put("freeAvgBlock", edt_meminfoget.getMemInfoGet().getFreeAvgBlock());
                            result.put("freeMaxBlock", edt_meminfoget.getMemInfoGet().getFreeMaxBlock());
                            result.put("allocBytes", edt_meminfoget.getMemInfoGet().getAllocBytes());
                            result.put("allocBlocks", edt_meminfoget.getMemInfoGet().getAllocBlocks());
                            result.put("allocAvgBlock", edt_meminfoget.getMemInfoGet().getAllocAvgBlock());
                            result.put("allocMaxBytes", edt_meminfoget.getMemInfoGet().getAllocMaxBytes());
                            result.put("internalBytes", edt_meminfoget.getMemInfoGet().getInternalBytes());
                            result.put("internalBlocks", edt_meminfoget.getMemInfoGet().getInternalBlocks());
                            result.put("internalAvgBlock", edt_meminfoget.getMemInfoGet().getInternalAvgBlock());
                            result.put("cumBytesAllocated", edt_meminfoget.getMemInfoGet().getCumBytesAllocated());
                            result.put("cumBlocksAllocated", edt_meminfoget.getMemInfoGet().getCumBlocksAllocated());
                            result.put("cumAvgBlock", edt_meminfoget.getMemInfoGet().getCumAvgBlock()).put("type", "memPartCreate");


                            break;
                        case 102:
                            TdrMsg.EDT_INTENTER_INFO intenter_info = TdrMsg.EDT_INTENTER_INFO.parseFrom(TdrMsgBuffer);
                            result.put("evtId", intenter_info.getEvtId());
                            result.put("timeStamp", intenter_info.getTimeStamp());
                            result.put("pdId", intenter_info.getPdId());
                            result.put("portId", intenter_info.getPortId());
                            result.put("interruptId", intenter_info.getEdtIntEnterInfo().getInterruptId()).put("type", "中断进入");


                            break;
                        case 101:
                            TdrMsg.EDT_INTEXIT_INFO intexit_info = TdrMsg.EDT_INTEXIT_INFO.parseFrom(TdrMsgBuffer);
                            result.put("evtId", intexit_info.getEvtId());
                            result.put("timeStamp", intexit_info.getTimeStamp());
                            result.put("pdId", intexit_info.getPdId());
                            result.put("portId", intexit_info.getPortId()).put("type", "中断退出（内核队列空）");

                            break;
                        case 100:
                            break;
                        case 10082:
                            TdrMsg.EDT_EDRINJECT_INFO edrinject_info = TdrMsg.EDT_EDRINJECT_INFO.parseFrom(TdrMsgBuffer);
                            result.put("evtId", edrinject_info.getEvtId());
                            result.put("timeStamp", edrinject_info.getTimeStamp());
                            result.put("pdId", edrinject_info.getPdId());
                            result.put("portId", edrinject_info.getPortId());

                            result.put("kind", edrinject_info.getEdrInjectInfo().getKind());
                            result.put("fileName", edrinject_info.getEdrInjectInfo().getFileName());
                            result.put("lineNumber", edrinject_info.getEdrInjectInfo().getLineNumber());
                            result.put("address", edrinject_info.getEdrInjectInfo().getAddress());
                            result.put("message", edrinject_info.getEdrInjectInfo().getMessage()).put("type", "ED&R注入事件");


                            break;
                        case 40015:
                            TdrMsg.EDT_EDR_HEAD_INFO edt_edr_head_info = TdrMsg.EDT_EDR_HEAD_INFO.parseFrom(TdrMsgBuffer);
                            result.put("evtId", edt_edr_head_info.getEvtId());
                            result.put("timeStamp", edt_edr_head_info.getTimeStamp());
                            result.put("pdId", edt_edr_head_info.getPdId());
                            result.put("portId", edt_edr_head_info.getPortId());

                            result.put("header", edt_edr_head_info.getHeadInfo().getHeader());
                            result.put("checksum", edt_edr_head_info.getHeadInfo().getChecksum());
                            result.put("modCount", edt_edr_head_info.getHeadInfo().getModCount());
                            result.put("genCount", edt_edr_head_info.getHeadInfo().getGenCount());
                            result.put("bootCount", edt_edr_head_info.getHeadInfo().getBootCount());
                            result.put("missedErrors", edt_edr_head_info.getHeadInfo().getMissedErrors()).put("type", "ED&R头部信息立即获取");
                            for (int i = 0; i < edt_edr_head_info.getHeadInfo().getReservedCount(); i++) {
                                JsonObject recv_queuing_msg_res = new JsonObject();
                                recv_queuing_msg_res.put("reserved", edt_edr_head_info.getHeadInfo().getReserved(i));
                                result.put("reserved" + i, recv_queuing_msg_res);
                            }
                            result.put("maxNodeCount", edt_edr_head_info.getHeadInfo().getNodeList().getMaxNodeCount());
                            result.put("oldestNode", edt_edr_head_info.getHeadInfo().getNodeList().getOldestNode());
                            result.put("nextNode", edt_edr_head_info.getHeadInfo().getNodeList().getNextNode());
                            result.put("nodeCount", edt_edr_head_info.getHeadInfo().getNodeList().getNodeCount());
                            for (int i = 0; i < edt_edr_head_info.getHeadInfo().getNodeList().getReservedCount(); i++) {
                                JsonObject recv_queuing_msg_res = new JsonObject();
                                recv_queuing_msg_res.put("reserved", edt_edr_head_info.getHeadInfo().getNodeList().getReserved(i));
                                result.put("nodeList-reserved" + i, recv_queuing_msg_res);////////////////////////////////////////////////////////////////////////////////////////
                            }
                            result.put("pNodeFirst", edt_edr_head_info.getHeadInfo().getNodeList().getPNodeFirst());

                            break;
                        case 40014:
                        case 10061:
                            TdrMsg.EDT_HMINJECT_INFO edt_hminject_info = TdrMsg.EDT_HMINJECT_INFO.parseFrom(TdrMsgBuffer);
                            result.put("evtId", edt_hminject_info.getEvtId());
                            result.put("timeStamp", edt_hminject_info.getTimeStamp());
                            result.put("pdId", edt_hminject_info.getPdId());
                            result.put("portId", edt_hminject_info.getPortId());
                            result.put("code", edt_hminject_info.getHmInjectInfo().getCode());
                            result.put("partNumber", edt_hminject_info.getHmInjectInfo().getPartNumber());
                            result.put("evtTag", edt_hminject_info.getHmInjectInfo().getEvtTag());
                            result.put("sysStatus", edt_hminject_info.getHmInjectInfo().getSysStatus());
                            result.put("subCode", edt_hminject_info.getHmInjectInfo().getSubCode());
                            result.put("addr", edt_hminject_info.getHmInjectInfo().getAddr());
                            result.put("taskId", edt_hminject_info.getHmInjectInfo().getTaskId()).put("type", "HM注入事件");

                            break;
                        case 40020:
                            TdrMsg.EDT_HM_HEAD_INFO edt_hm_head_info = TdrMsg.EDT_HM_HEAD_INFO.parseFrom(TdrMsgBuffer);
                            result.put("evtId", edt_hm_head_info.getEvtId());
                            result.put("timeStamp", edt_hm_head_info.getTimeStamp());
                            result.put("pdId", edt_hm_head_info.getPdId());
                            result.put("portId", edt_hm_head_info.getPortId());

                            result.put("pdId", edt_hm_head_info.getHeadInfo().getPdId());
                            result.put("partitionNumber", edt_hm_head_info.getHeadInfo().getPartitionNumber());
                            result.put("hmQueueId", edt_hm_head_info.getHeadInfo().getHmQueueId());
                            result.put("hmStackSize", edt_hm_head_info.getHeadInfo().getHmStackSize());
                            result.put("hmMaxQueueDepth", edt_hm_head_info.getHeadInfo().getHmMaxQueueDepth());
                            result.put("queueThreshold", edt_hm_head_info.getHeadInfo().getQueueThreshold());
                            result.put("hmTaskId", edt_hm_head_info.getHeadInfo().getHmTaskId());
                            result.put("semId", edt_hm_head_info.getHeadInfo().getSemId());
                            result.put("hmCallback", edt_hm_head_info.getHeadInfo().getHmCallback());
                            result.put("notificationHandler", edt_hm_head_info.getHeadInfo().getNotificationHandler());
                            result.put("notifQueueId", edt_hm_head_info.getHeadInfo().getNotifQueueId());
                            result.put("notifMaxQueueDepth", edt_hm_head_info.getHeadInfo().getNotifMaxQueueDepth());
                            result.put("eventFilterMask", edt_hm_head_info.getHeadInfo().getEventFilterMask());
                            result.put("trustedPartitionsMask", edt_hm_head_info.getHeadInfo().getTrustedPartitionsMask());
                            result.put("pLog", edt_hm_head_info.getHeadInfo().getPLog());
                            result.put("logHead", edt_hm_head_info.getHeadInfo().getLogHead());
                            result.put("logTail", edt_hm_head_info.getHeadInfo().getLogTail());
                            result.put("maxLogEntries", edt_hm_head_info.getHeadInfo().getMaxLogEntries());
                            result.put("pCfgRecord", edt_hm_head_info.getHeadInfo().getPCfgRecord());
                            result.put("errorCode", edt_hm_head_info.getHeadInfo().getErrorCode());
                            result.put("attributes", edt_hm_head_info.getHeadInfo().getAttributes());
                            result.put("hmInitialized", edt_hm_head_info.getHeadInfo().getVThreadsCtx().getHmInitialized());
                            result.put("ehMsgQId", edt_hm_head_info.getHeadInfo().getVThreadsCtx().getEhMsgQId());
                            result.put("ehTaskId", edt_hm_head_info.getHeadInfo().getVThreadsCtx().getEhTaskId()).put("type", "HM头部信息立即获取");
                            result.put("vThreads_attributes", edt_hm_head_info.getHeadInfo().getVThreadsCtx().getVThreadsAttributes());
                            for (int i = 0; i < edt_hm_head_info.getHeadInfo().getPHmTblCount(); i++) {
                                JsonObject recv_queuing_msg_res = new JsonObject();
                                recv_queuing_msg_res.put("pHmTbl", edt_hm_head_info.getHeadInfo().getPHmTbl(i));
                                result.put("pHmTbl" + i, recv_queuing_msg_res);
                            }

                            break;
                        case 40021:
                            TdrMsg.EDT_HM_LOG_INFO edt_hm_log_info = TdrMsg.EDT_HM_LOG_INFO.parseFrom(TdrMsgBuffer);
                            result.put("evtId", edt_hm_log_info.getEvtId());
                            result.put("timeStamp", edt_hm_log_info.getTimeStamp());
                            result.put("pdId", edt_hm_log_info.getPdId());
                            result.put("portId", edt_hm_log_info.getPortId());
                            result.put("start", edt_hm_log_info.getLogInfo().getStart());
                            result.put("start", edt_hm_log_info.getLogInfo().getStart());
                            result.put("count", edt_hm_log_info.getLogInfo().getCount());
                            result.put("evtTag", edt_hm_log_info.getLogInfo().getEvtTag());
                            result.put("partNumber", edt_hm_log_info.getLogInfo().getPartNumber());
                            result.put("logInfo-timeStamp", edt_hm_log_info.getLogInfo().getTimeStamp());
                            result.put("sysStatus", edt_hm_log_info.getLogInfo().getSysStatus());
                            result.put("historicalCode", edt_hm_log_info.getLogInfo().getHistoricalCode());
                            result.put("code", edt_hm_log_info.getLogInfo().getCode());
                            result.put("subCode", edt_hm_log_info.getLogInfo().getSubCode());
                            result.put("addInfo", edt_hm_log_info.getLogInfo().getAddInfo());
                            result.put("addr", edt_hm_log_info.getLogInfo().getAddr());
                            result.put("taskId", edt_hm_log_info.getLogInfo().getTaskId());
                            result.put("taskName", edt_hm_log_info.getLogInfo().getTaskName());
                            result.put("msgLen", edt_hm_log_info.getLogInfo().getMsgLen());
                            result.put("msg", edt_hm_log_info.getLogInfo().getMsg()).put("type", "HM日志信息立即获取");


                            break;
                        case 10066:
                            TdrMsg.EDT_PSEUDOINT_INFO edt_pseudoint = TdrMsg.EDT_PSEUDOINT_INFO.parseFrom(TdrMsgBuffer);
                            result.put("evtId", edt_pseudoint.getEvtId());
                            result.put("timeStamp", edt_pseudoint.getTimeStamp());
                            result.put("pdId", edt_pseudoint.getPdId());
                            result.put("portId", edt_pseudoint.getPortId());
                            result.put("partitionNumber", edt_pseudoint.getEdtPseudoIntInfo().getPartitionNumber());
                            result.put("eventType", edt_pseudoint.getEdtPseudoIntInfo().getEventType());
                            result.put("arg1", edt_pseudoint.getEdtPseudoIntInfo().getArg1());
                            result.put("arg2", edt_pseudoint.getEdtPseudoIntInfo().getArg2());
                            result.put("arg3", edt_pseudoint.getEdtPseudoIntInfo().getArg3());
                            result.put("arg4", edt_pseudoint.getEdtPseudoIntInfo().getArg4()).put("type", "虚中断事件采集");

                            break;
                        case 60:
                            TdrMsg.EDT_EXC_INFO exc_info = TdrMsg.EDT_EXC_INFO.parseFrom(TdrMsgBuffer);
                            result.put("evtId", exc_info.getEvtId());
                            result.put("timeStamp", exc_info.getTimeStamp());
                            result.put("pdId", exc_info.getPdId());
                            result.put("portId", exc_info.getPortId());
                            result.put("exception", exc_info.getEdtExcInfo().getException()).put("type", "异常事件");

                            break;
                        case 40011:
                            TdrMsg.EDT_TASKSTACK_INFO edt_taskstack = TdrMsg.EDT_TASKSTACK_INFO.parseFrom(TdrMsgBuffer);
                            result.put("evtId", edt_taskstack.getEvtId());
                            result.put("timeStamp", edt_taskstack.getTimeStamp());
                            result.put("pdId", edt_taskstack.getPdId());
                            result.put("portId", edt_taskstack.getPortId());
                            result.put("taskname", edt_taskstack.getTaskInfoGet().getTaskname());
                            result.put("entry", edt_taskstack.getTaskInfoGet().getEntry());
                            result.put("taskID", edt_taskstack.getTaskInfoGet().getTaskID());
                            result.put("stackSize", edt_taskstack.getTaskInfoGet().getStackSize());
                            result.put("sp", edt_taskstack.getTaskInfoGet().getSp());
                            result.put("stackBase", edt_taskstack.getTaskInfoGet().getStackBase());
                            result.put("stackEnd", edt_taskstack.getTaskInfoGet().getStackEnd());
                            result.put("highSize", edt_taskstack.getTaskInfoGet().getHighSize());
                            result.put("margin", edt_taskstack.getTaskInfoGet().getMargin()).put("type", "任务堆栈获取");


                            break;
                        case 40012:
                            TdrMsg.EDT_MEMINFOGET_INFO edt_meminfoget_info = TdrMsg.EDT_MEMINFOGET_INFO.parseFrom(TdrMsgBuffer);
                            result.put("evtId", edt_meminfoget_info.getEvtId());
                            result.put("timeStamp", edt_meminfoget_info.getTimeStamp());
                            result.put("pdId", edt_meminfoget_info.getPdId());
                            result.put("portId", edt_meminfoget_info.getPortId());

                            result.put("freeBytes", edt_meminfoget_info.getMemInfoGet().getFreeBytes());
                            result.put("freeBlocks", edt_meminfoget_info.getMemInfoGet().getFreeBlocks());
                            result.put("freeAvgBlock", edt_meminfoget_info.getMemInfoGet().getFreeAvgBlock());

                            result.put("freeMaxBlock", edt_meminfoget_info.getMemInfoGet().getFreeMaxBlock());
                            result.put("allocBytes", edt_meminfoget_info.getMemInfoGet().getAllocBytes());
                            result.put("allocBlocks", edt_meminfoget_info.getMemInfoGet().getAllocBlocks());
                            result.put("allocAvgBlock", edt_meminfoget_info.getMemInfoGet().getAllocAvgBlock());
                            result.put("allocMaxBytes", edt_meminfoget_info.getMemInfoGet().getAllocMaxBytes());////////////////////////////////////////////////
                            result.put("allocMaxBytes", edt_meminfoget_info.getMemInfoGet().getAllocMaxBytes());/////////////////////////////////////////////////////
                            result.put("internalBlocks", edt_meminfoget_info.getMemInfoGet().getInternalBlocks());
                            result.put("internalAvgBlock", edt_meminfoget_info.getMemInfoGet().getInternalAvgBlock());
                            result.put("cumBytesAllocated", edt_meminfoget_info.getMemInfoGet().getCumBytesAllocated());
                            result.put("cumBlocksAllocated", edt_meminfoget_info.getMemInfoGet().getCumBlocksAllocated());
                            result.put("cumAvgBlock", edt_meminfoget_info.getMemInfoGet().getCumAvgBlock()).put("type", "内存信息获取");


                            break;
                        case 40023:
                            TdrMsg.EDT_PORTNAME_INFO edt_portname = TdrMsg.EDT_PORTNAME_INFO.parseFrom(TdrMsgBuffer);
                            result.put("evtId", edt_portname.getEvtId());
                            result.put("timeStamp", edt_portname.getTimeStamp());
                            result.put("pdId", edt_portname.getPdId());
                            result.put("portId", edt_portname.getPortId());
                            result.put("pdId2", edt_portname.getPdId2());
                            result.put("objId", edt_portname.getObjId());
                            result.put("objName", edt_portname.getObjName()).put("type", "APEX对象name和id对应关系");

                            break;
                        case 40001:
                            TdrMsg.EDT_EVENT_TASKTABLE edt_event_tasktable = TdrMsg.EDT_EVENT_TASKTABLE.parseFrom(TdrMsgBuffer);
                            result.put("evtId", edt_event_tasktable.getEvtId());
                            result.put("timeStamp", edt_event_tasktable.getTimeStamp());
                            result.put("pdId", edt_event_tasktable.getPdId());
                            result.put("portId", edt_event_tasktable.getPortId()).put("type", "获取系统任务列表");
                            for (int i = 0; i < edt_event_tasktable.getSysTaskTableCount(); i++) {
                                JsonObject recv_queuing_msg_res = new JsonObject();
                                recv_queuing_msg_res.put("state", edt_event_tasktable.getSysTaskTable(i).getState())
                                        .put("priority", edt_event_tasktable.getSysTaskTable(i).getPriority())
                                        .put("lockCnt", edt_event_tasktable.getSysTaskTable(i).getLockCnt())
                                        .put("tid", edt_event_tasktable.getSysTaskTable(i).getTid())
                                        .put("name", edt_event_tasktable.getSysTaskTable(i).getName());
                                result.put("sysTaskTable" + i, recv_queuing_msg_res);
                            }


                            break;
                        case 40002:
                            TdrMsg.EDT_EVENT_TASKSWINFO edt_event_taskswinfo = TdrMsg.EDT_EVENT_TASKSWINFO.parseFrom(TdrMsgBuffer);
                            result.put("evtId", edt_event_taskswinfo.getEvtId());
                            result.put("timeStamp", edt_event_taskswinfo.getTimeStamp());
                            result.put("pdId", edt_event_taskswinfo.getPdId());
                            result.put("portId", edt_event_taskswinfo.getPortId());
                            result.put("tOldId", edt_event_taskswinfo.getTOldId());
                            result.put("tNewId", edt_event_taskswinfo.getTNewId()).put("type", "任务切换监控事件");


                            break;
                        case 3:
                            TdrMsg.EDT_EVENT_TASKNAME event_taskname = TdrMsg.EDT_EVENT_TASKNAME.parseFrom(TdrMsgBuffer);
                            result.put("evtId", event_taskname.getEvtId());
                            result.put("timeStamp", event_taskname.getTimeStamp());
                            result.put("pdId", event_taskname.getPdId());
                            result.put("portId", event_taskname.getPortId());
                            result.put("state", event_taskname.getTasknameInfo().getState());
                            result.put("priority", event_taskname.getTasknameInfo().getPriority());
                            result.put("lockCnt", event_taskname.getTasknameInfo().getLockCnt());
                            result.put("tid", event_taskname.getTasknameInfo().getTid());
//                        result.put("entryPt",event_taskname.getTasknameInfo().get());
//                        result.put("rtp",event_taskname.getTasknameInfo().getRtp());
//                        result.put("affinity",event_taskname.getTasknameInfo().getAffinity());
                            result.put("name", event_taskname.getTasknameInfo().getName()).put("type", "获取创建的任务的名称");


                            break;
                        case 40003:
                        case 52:
                            TdrMsg.EDT_EXIT_DISPATCH edt_exit_dispatch = TdrMsg.EDT_EXIT_DISPATCH.parseFrom(TdrMsgBuffer);
                            result.put("evtId", edt_exit_dispatch.getEvtId());
                            result.put("timeStamp", edt_exit_dispatch.getTimeStamp());
                            result.put("pdId", edt_exit_dispatch.getPdId());
                            result.put("portId", edt_exit_dispatch.getPortId());
                            result.put("taskId", edt_exit_dispatch.getTaskId());
                            result.put("priority", edt_exit_dispatch.getPriority()).put("type", "分区中退出任务调度分配");

                            break;
                        case 62:
                            TdrMsg.EDT_PARTITION_EXITIDLE edt_partition_exitidle = TdrMsg.EDT_PARTITION_EXITIDLE.parseFrom(TdrMsgBuffer);
                            result.put("evtId", edt_partition_exitidle.getEvtId());
                            result.put("timeStamp", edt_partition_exitidle.getTimeStamp());
                            result.put("pdId", edt_partition_exitidle.getPdId());
                            result.put("portId", edt_partition_exitidle.getPortId()).put("type", "分区退出IDLE");


                            break;
                        case 63:
                            TdrMsg.EDT_PARTITION_IDLE edt_partition_idle = TdrMsg.EDT_PARTITION_IDLE.parseFrom(TdrMsgBuffer);
                            result.put("evtId", edt_partition_idle.getEvtId());
                            result.put("timeStamp", edt_partition_idle.getTimeStamp());
                            result.put("pdId", edt_partition_idle.getPdId());
                            result.put("portId", edt_partition_idle.getPortId()).put("type", "分区进入IDLE");


                            break;
                        case 40006:
                            TdrMsg.EDT_EVENT_MFTABLE edt_evebt_mftable = TdrMsg.EDT_EVENT_MFTABLE.parseFrom(TdrMsgBuffer);
                            result.put("evtId", edt_evebt_mftable.getEvtId());
                            result.put("timeStamp", edt_evebt_mftable.getTimeStamp());
                            result.put("pdId", edt_evebt_mftable.getPdId());
                            result.put("portId", edt_evebt_mftable.getPortId()).put("type", "获取系统中MF表");
                            for (int i = 0; i < edt_evebt_mftable.getMfTableCount(); i++) {
                                JsonObject recv_queuing_msg_res = new JsonObject();
                                recv_queuing_msg_res.put("schedId", edt_evebt_mftable.getMfTable(i).getSchedId());
                                for (int j = 0; j < edt_evebt_mftable.getMfTable(i).getPartInfoCount(); j++) {
                                    JsonObject partInfo = new JsonObject().put("partId", edt_evebt_mftable.getMfTable(i).getPartInfo(j).getPartId())
                                            .put("partName", edt_evebt_mftable.getMfTable(i).getPartInfo(j).getPartName())
                                            .put("duration", edt_evebt_mftable.getMfTable(i).getPartInfo(j).getDuration());


                                }
                                result.put("pHmTbl" + i, recv_queuing_msg_res);
                            }

                            //////////////////////////////////////////////////////////////////////////////////////////////////////////

                            break;
                        case 40008:
                            TdrMsg.EDT_EVENT_MFSW_INFO edt_event_mfsw_info = TdrMsg.EDT_EVENT_MFSW_INFO.parseFrom(TdrMsgBuffer);
                            result.put("evtId", edt_event_mfsw_info.getEvtId());
                            result.put("timeStamp", edt_event_mfsw_info.getTimeStamp());
                            result.put("pdId", edt_event_mfsw_info.getPdId());
                            result.put("portId", edt_event_mfsw_info.getPortId());
                            result.put("schedId", edt_event_mfsw_info.getSchedId()).put("type", "MF切换的信息");


                            break;
                        case 40009:
                            TdrMsg.EDT_EVENT_SWPARTATION edt_event_swpartation = TdrMsg.EDT_EVENT_SWPARTATION.parseFrom(TdrMsgBuffer);
                            result.put("evtId", edt_event_swpartation.getEvtId());
                            result.put("timeStamp", edt_event_swpartation.getTimeStamp());
                            result.put("pdId", edt_event_swpartation.getPdId());
                            result.put("portId", edt_event_swpartation.getPortId());
                            result.put("prevPartId", edt_event_swpartation.getPrevPartId());
                            result.put("currPartId", edt_event_swpartation.getCurrPartId()).put("type", "分区切换信息");

                            break;
                        case 40027:
                            TdrMsg.EDT_EVENT_PDLIST_INFO edt_event_pdlist_info = TdrMsg.EDT_EVENT_PDLIST_INFO.parseFrom(TdrMsgBuffer);
                            result.put("evtId", edt_event_pdlist_info.getEvtId());
                            result.put("timeStamp", edt_event_pdlist_info.getTimeStamp());
                            result.put("pdId", edt_event_pdlist_info.getPdId());
                            result.put("portId", edt_event_pdlist_info.getPortId()).put("type", "系统中分区列表");
                            for (int i = 0; i < edt_event_pdlist_info.getPdListCount(); i++) {
                                JsonObject recv_queuing_msg_res = new JsonObject();
                                recv_queuing_msg_res.put("pdId", edt_event_pdlist_info.getPdList(i).getPdId())
                                        .put("pdName", edt_event_pdlist_info.getPdList(i).getPdName());
                                result.put("pdList" + i, recv_queuing_msg_res);
                            }

                            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

                            break;

                    }
                    result.put("sid",Tools.generateKey());
                    System.out.println(result);
                    mongoClient.insert("communication_record",result , re -> {
                        if (re.failed()) {
                            logger.error(String.format("insert: %s, exception: %s", result, Tools.getTrace(re.cause())));
                            response.put("type", "errorData")
                                    .put("code", INSERT_FAILURE.getCode())
                                    .put("message", INSERT_FAILURE.getMsg());
                        } else {
                            response.put("type", "success").put("result", result);

                        }
                        future.complete(response);

                    });


                } catch (InvalidProtocolBufferException e) {
                    e.printStackTrace();

                }
            }else {
                logger.error(String.format("exception: %s", "数据库中不存在数据"));
            }
//            if (res.failed()) {
//                logger.error(String.format("mySQL find tdrMsg data error: %s", Tools.getTrace(res.cause())));
//                response.put("type","error")
//                        .put("code",QUERY_FAILURE.getCode())
//                        .put("message",QUERY_FAILURE.getMsg());
//                return
//
//
//////                System.out.println(sql);
////                response.error(ResponseError.SERVER_ERROR.getCode(), ResponseError.SERVER_ERROR.getMsg());
////                return;
//            }else {
//
//            }




//        String sql="SELECT * FROM detrcd_latest_data";
//        System.out.println(sql);
//        mySQLClient.query(sql,res->{
//            if(res.failed()){
//                logger.error(String.format("Select failed: %s", Tools.getTrace(res.cause())));
////                System.out.println(sql);FF
//                response.error(ResponseError.SERVER_ERROR.getCode(), ResponseError.SERVER_ERROR.getMsg());
//                return;
//            }




//            System.out.println(res.result().getRows().get(0));
//            int eventtype=res.result().getRows().get(0).getInteger("drt_eventtype_latest");//获取事件类型
//            byte[] resultBufferL=res.result().getRows().get(0).getBinary("drt_data_latest");//获取数据
//            System.out.println(eventtype);
//            System.out.println(resultBufferL);
//            System.out.println(eventtype);
//            System.out.println(resultBuffer);

//            try {
//                switch (eventtype){
//                    case 3:
//                        TdrMsg.EDT_EVENT_TASKNAME event_taskname=TdrMsg.EDT_EVENT_TASKNAME.parseFrom(resultBufferL);
//                        result.put("evtId",event_taskname.getEvtId());
//                        result.put("timeStamp",event_taskname.getTimeStamp());
//                        result.put("pdId",event_taskname.getPdId());
//                        result.put("portId",event_taskname.getPortId());
//                        result.put("state",event_taskname.getTasknameInfo().getState());
//                        result.put("priority",event_taskname.getTasknameInfo().getPriority());
//                        result.put("lockCnt",event_taskname.getTasknameInfo().getLockCnt());
//                        result.put("tid",event_taskname.getTasknameInfo().getTid());
////                        result.put("entryPt",event_taskname.getTasknameInfo().get());
////                        result.put("rtp",event_taskname.getTasknameInfo().getRtp());
////                        result.put("affinity",event_taskname.getTasknameInfo().getAffinity());
//                        result.put("name",event_taskname.getTasknameInfo().getName());
//
//                        break;
//                    case 60:
//                        TdrMsg.EDT_EXC_INFO exc_info=TdrMsg.EDT_EXC_INFO.parseFrom(resultBufferL);
//                        result.put("evtId",exc_info.getEvtId());
//                        result.put("timeStamp",exc_info.getTimeStamp());
//                        result.put("pdId",exc_info.getPdId());
//                        result.put("portId",exc_info.getPortId());
//                        result.put("interruptId",exc_info.getEdtExcInfo().getInterruptId());
//
//                        break;
//                    case 100:
//                    case 101:
//                        TdrMsg.EDT_INTEXIT_INFO intexit_info=TdrMsg.EDT_INTEXIT_INFO.parseFrom(resultBufferL);
//                        result.put("evtId",intexit_info.getEvtId());
//                        result.put("timeStamp",intexit_info.getTimeStamp());
//                        result.put("pdId",intexit_info.getPdId());
//                        result.put("portId",intexit_info.getPortId());
//
//                        break;
//                    case 102:
//                        TdrMsg.EDT_INTENTER_INFO intenter_info=TdrMsg.EDT_INTENTER_INFO.parseFrom(resultBufferL);
//                        result.put("evtId",intenter_info.getEvtId());
//                        result.put("timeStamp",intenter_info.getTimeStamp());
//                        result.put("pdId",intenter_info.getPdId());
//                        result.put("portId",intenter_info.getPortId());
//                        result.put("interruptId",intenter_info.getEdtIntEnterInfo().getInterruptId());
//
//                        break;
//                    case 10000:
//                        TdrMsg.EDT_TASKSPAWN_INFO taskspawnResult=TdrMsg.EDT_TASKSPAWN_INFO.parseFrom(resultBufferL);
//                        result.put("evtId",taskspawnResult.getEvtId());
//                        result.put("timeStamp",taskspawnResult.getTimeStamp());
//                        result.put("pdId",taskspawnResult.getPdId());
//                        result.put("portId",taskspawnResult.getPortId());
//                        result.put("taskId",taskspawnResult.getTskSpawn().getTaskId());
//                        result.put("priority",taskspawnResult.getTskSpawn().getPriority());
//                        result.put("stackSize",taskspawnResult.getTskSpawn().getStackSize());
//                        result.put("entryPoint",taskspawnResult.getTskSpawn().getEntryPoint());
//                        result.put("options",taskspawnResult.getTskSpawn().getOptions());
//
//                        break;
//                    case 10001:
//                        TdrMsg.EDT_TASKDESTROY_INFO taskdestroy=TdrMsg.EDT_TASKDESTROY_INFO.parseFrom(resultBufferL);
//                        result.put("evtId",taskdestroy.getEvtId());
//                        result.put("timeStamp",taskdestroy.getTimeStamp());
//                        result.put("pdId",taskdestroy.getPdId());
//                        result.put("portId",taskdestroy.getPortId());
//                        result.put("taskId",taskdestroy.getTskDestroy().getTaskId());
//                        result.put("taskClassId",taskdestroy.getTskDestroy().getTaskClassId());
//                        result.put("taskId_1",taskdestroy.getTskDestroy().getTaskId1());
//                        result.put("safeCnt",taskdestroy.getTskDestroy().getSafeCnt());
//
//                        break;
//                    case 10008:
//                        TdrMsg.EDT_SEMBCREATE_INFO sembcreate=TdrMsg.EDT_SEMBCREATE_INFO.parseFrom(resultBufferL);
//                        result.put("evtId",sembcreate.getEvtId());
//                        result.put("timeStamp",sembcreate.getTimeStamp());
//                        result.put("pdId",sembcreate.getPdId());
//                        result.put("portId",sembcreate.getPortId());
//                        result.put("semId",sembcreate.getSemBCreat().getSemId());
//                        result.put("classId",sembcreate.getSemBCreat().getClassId());
//                        result.put("semId_1",sembcreate.getSemBCreat().getSemId1());
//                        result.put("options",sembcreate.getSemBCreat().getOptions());
//                        result.put("owner",sembcreate.getSemBCreat().getOwner());
//
//                        break;
//                    case 10009:
//                        TdrMsg.EDT_SEMCCREATE_INFO EDT_SEMCCREATE_INFO=TdrMsg.EDT_SEMCCREATE_INFO.parseFrom(resultBufferL);
//                        result.put("evtId",EDT_SEMCCREATE_INFO.getEvtId());
//                        result.put("timeStamp",EDT_SEMCCREATE_INFO.getTimeStamp());
//                        result.put("pdId",EDT_SEMCCREATE_INFO.getPdId());
//                        result.put("portId",EDT_SEMCCREATE_INFO.getPortId());
//                        result.put("semId",EDT_SEMCCREATE_INFO.getSemCCreat().getSemId());
//                        result.put("classId",EDT_SEMCCREATE_INFO.getSemCCreat().getClassId());
//                        result.put("semId_1",EDT_SEMCCREATE_INFO.getSemCCreat().getSemId1());
//                        result.put("options",EDT_SEMCCREATE_INFO.getSemCCreat().getOptions());
//                        result.put("initCount",EDT_SEMCCREATE_INFO.getSemCCreat().getInitCount());
//
//                        break;
//                    case 10010:
//                        TdrMsg.EDT_SEMDELETE_INFO semdelete_info=TdrMsg.EDT_SEMDELETE_INFO.parseFrom(resultBufferL);
//                        result.put("evtId",semdelete_info.getEvtId());
//                        result.put("timeStamp",semdelete_info.getTimeStamp());
//                        result.put("pdId",semdelete_info.getPdId());
//                        result.put("portId",semdelete_info.getPortId());
//                        result.put("semId",semdelete_info.getSemDelete().getSemId());
//                        result.put("classId",semdelete_info.getSemDelete().getClassId());
//                        result.put("semId_1",semdelete_info.getSemDelete().getSemId1());
//                        result.put("owner",semdelete_info.getSemDelete().getOwner());
//                        result.put("recurse",semdelete_info.getSemDelete().getRecurse());
//                        result.put("qHeadAddr",semdelete_info.getSemDelete().getQHeadAddr());
//
//                        break;
//                    case 10013:
//                        TdrMsg.EDT_SEMMCREATE_INFO semmcreate=TdrMsg.EDT_SEMMCREATE_INFO.parseFrom(resultBufferL);
//                        result.put("evtId",semmcreate.getEvtId());
//                        result.put("timeStamp",semmcreate.getTimeStamp());
//                        result.put("pdId",semmcreate.getPdId());
//                        result.put("portId",semmcreate.getPortId());
//                        result.put("semId",semmcreate.getSemMCreat().getSemId());
//                        result.put("classId",semmcreate.getSemMCreat().getClassId());
//                        result.put("semId_1",semmcreate.getSemMCreat().getSemId1());
//                        result.put("options",semmcreate.getSemMCreat().getOptions());
//                        result.put("owner",semmcreate.getSemMCreat().getOwner());
//
//                        break;
//                    case 10016:
//                        TdrMsg.EDT_WDCREATE_INFO wdcreate=TdrMsg.EDT_WDCREATE_INFO.parseFrom(resultBufferL);
//                        result.put("evtId",wdcreate.getEvtId());
//                        result.put("timeStamp",wdcreate.getTimeStamp());
//                        result.put("pdId",wdcreate.getPdId());
//                        result.put("portId",wdcreate.getPortId());
//                        result.put("wdId",wdcreate.getWdCreat().getWdId());
//                        result.put("classId",wdcreate.getWdCreat().getClassId());
//                        result.put("wdId_1",wdcreate.getWdCreat().getWdId1());
//
//                        break;
//                    case 10017:
//                        TdrMsg.EDT_WDDELETE_INFO wddelete_info=TdrMsg.EDT_WDDELETE_INFO.parseFrom(resultBufferL);
//                        result.put("evtId",wddelete_info.getEvtId());
//                        result.put("timeStamp",wddelete_info.getTimeStamp());
//                        result.put("pdId",wddelete_info.getPdId());
//                        result.put("portId",wddelete_info.getPortId());
//                        result.put("wdId",wddelete_info.getWdDelete().getWdId());
//                        result.put("classId",wddelete_info.getWdDelete().getClassId());
//                        result.put("wdId_1",wddelete_info.getWdDelete().getWdId1());
//
//                        break;
//                    case 10020:
//                        TdrMsg.EDT_MSGQCREATE_INFO msgqcreate_info=TdrMsg.EDT_MSGQCREATE_INFO.parseFrom(resultBufferL);
//                        result.put("evtId",msgqcreate_info.getEvtId());
//                        result.put("timeStamp",msgqcreate_info.getTimeStamp());
//                        result.put("pdId",msgqcreate_info.getPdId());
//                        result.put("portId",msgqcreate_info.getPortId());
//                        result.put("msgQId",msgqcreate_info.getMsgQCreat().getMsgQId());
//                        result.put("classId",msgqcreate_info.getMsgQCreat().getClassId());
//                        result.put("msgQId_1",msgqcreate_info.getMsgQCreat().getMsgQId1());
//                        result.put("maxMsgs",msgqcreate_info.getMsgQCreat().getMaxMsgs());
//                        result.put("maxMsgLen",msgqcreate_info.getMsgQCreat().getMaxMsgLen());
//                        result.put("options",msgqcreate_info.getMsgQCreat().getOptions());
//
//                        break;
//                    case 10021:
//                        TdrMsg.EDT_MSGQDELETE_INFO msgqdelete_info=TdrMsg.EDT_MSGQDELETE_INFO.parseFrom(resultBufferL);
//                        result.put("evtId",msgqdelete_info.getEvtId());
//                        result.put("timeStamp",msgqdelete_info.getTimeStamp());
//                        result.put("pdId",msgqdelete_info.getPdId());
//                        result.put("portId",msgqdelete_info.getPortId());
//                        result.put("msgQId",msgqdelete_info.getMsgQDelete().getMsgQId());
//                        result.put("classId",msgqdelete_info.getMsgQDelete().getClassId());
//                        result.put("msgQId_1",msgqdelete_info.getMsgQDelete().getMsgQId1());
//
//                        break;
//                    case 10022:
//                        TdrMsg.EDT_MSGQRECV_INFO msgqrecv_info=TdrMsg.EDT_MSGQRECV_INFO.parseFrom(resultBufferL);
//                        result.put("evtId",msgqrecv_info.getEvtId());
//                        result.put("timeStamp",msgqrecv_info.getTimeStamp());
//                        result.put("pdId",msgqrecv_info.getPdId());
//                        result.put("portId",msgqrecv_info.getPortId());
//                        result.put("msgQId",msgqrecv_info.getMsgQId());
//                        result.put("objType",msgqrecv_info.getObjType());
//                        result.put("timeout",msgqrecv_info.getTimeout());
//                        result.put("recvMaxLen",msgqrecv_info.getRecvMaxLen());
//                        for(int i=0;i<msgqrecv_info.getMsgQBufCount()-1;i++){
//                            JsonObject msgqrecv_res=new JsonObject();
//                            msgqrecv_res.put("msgQBuf",msgqrecv_info.getMsgQBuf(i));
//                            result.put("msgQBuf"+i,msgqrecv_res);
//                        }
//
//                        break;
//                    case 10023:
//                        TdrMsg.EDT_MSGQSEND_INFO msgqsend_info=TdrMsg.EDT_MSGQSEND_INFO.parseFrom(resultBufferL);
//                        result.put("evtId",msgqsend_info.getEvtId());
//                        result.put("timeStamp",msgqsend_info.getTimeStamp());
//                        result.put("pdId",msgqsend_info.getPdId());
//                        result.put("portId",msgqsend_info.getPortId());
//                        result.put("msgQId",msgqsend_info.getMsgQId());
//                        result.put("objType",msgqsend_info.getObjType());
//                        result.put("timeout",msgqsend_info.getTimeout());
//                        result.put("priority",msgqsend_info.getPriority());
//                        result.put("sendLen",msgqsend_info.getSendLen());
//                        for(int i=0;i<msgqsend_info.getMsgQBufCount()-1;i++){
//                            JsonObject msgqsend_res=new JsonObject();
//                            msgqsend_res.put("msgQBuf"+i,msgqsend_info.getMsgQBuf(i));
//                            result.put("msgQBuf"+i,msgqsend_res);
//                        }
//
//                        break;
//                    case 10030:
//                        TdrMsg.EDT_MEMALLOC_INFO memalloc_info=TdrMsg.EDT_MEMALLOC_INFO.parseFrom(resultBufferL);
//                        result.put("evtId",memalloc_info.getEvtId());
//                        result.put("timeStamp",memalloc_info.getTimeStamp());
//                        result.put("partId",memalloc_info.getMemallocInfo().getPartId());
//                        result.put("alignAddr",memalloc_info.getMemallocInfo().getAlignAddr());
//                        result.put("chunkSize",memalloc_info.getMemallocInfo().getChunkSize());
//                        result.put("nBytes",memalloc_info.getMemallocInfo().getNBytes());
//
//                        break;
//                    case 10031:
//                        TdrMsg.EDT_MEMFREE_INFO memfree_info=TdrMsg.EDT_MEMFREE_INFO.parseFrom(resultBufferL);
//                        result.put("evtId",memfree_info.getEvtId());
//                        result.put("timeStamp",memfree_info.getTimeStamp());
//                        result.put("partId",memfree_info.getMemFreeInfo().getPartId());
//                        result.put("pBlock",memfree_info.getMemFreeInfo().getPBlock());
//                        result.put("Size",memfree_info.getMemFreeInfo().getSize());
//
//                        break;
//                    case 10032:
//                    case 80012:
//                        TdrMsg.EDT_MEMINFOGET_INFO meminfoget_info=TdrMsg.EDT_MEMINFOGET_INFO.parseFrom(resultBufferL);
//                        result.put("evtId",meminfoget_info.getEvtId());
//                        result.put("timeStamp",meminfoget_info.getTimeStamp());
//                        result.put("freeBytes",meminfoget_info.getMemInfoGet().getFreeBytes());
//                        result.put("freeBlocks",meminfoget_info.getMemInfoGet().getFreeBlocks());
//                        result.put("freeAvgBlock",meminfoget_info.getMemInfoGet().getFreeAvgBlock());
//                        result.put("freeMaxBlock",meminfoget_info.getMemInfoGet().getFreeMaxBlock());
//                        result.put("allocBytes",meminfoget_info.getMemInfoGet().getAllocBytes());
//                        result.put("allocBlocks",meminfoget_info.getMemInfoGet().getAllocBlocks());
//                        result.put("allocAvgBlock",meminfoget_info.getMemInfoGet().getAllocAvgBlock());
//                        result.put("allocMaxBytes",meminfoget_info.getMemInfoGet().getAllocMaxBytes());
//                        result.put("internalBytes",meminfoget_info.getMemInfoGet().getInternalBytes());
//                        result.put("internalBlocks",meminfoget_info.getMemInfoGet().getInternalBlocks());
//                        result.put("internalAvgBlock",meminfoget_info.getMemInfoGet().getInternalAvgBlock());
//                        result.put("cumBytesAllocated",meminfoget_info.getMemInfoGet().getCumBytesAllocated());
//                        result.put("cumBlocksAllocated",meminfoget_info.getMemInfoGet().getCumBlocksAllocated());
//                        result.put("cumAvgBlock",meminfoget_info.getMemInfoGet().getCumAvgBlock());
//
//                        break;
//                    case 10048:
//                    case 10053:
//                        TdrMsg.EDT_IOCREATE_OPEN_INFO iocreate_open_info=TdrMsg.EDT_IOCREATE_OPEN_INFO.parseFrom(resultBufferL);
//                        result.put("evtId",iocreate_open_info.getEvtId());
//                        result.put("timeStamp",iocreate_open_info.getTimeStamp());
//                        result.put("objId",iocreate_open_info.getIoCOpen().getObjId());
//                        result.put("classId",iocreate_open_info.getIoCOpen().getClassId());
//                        result.put("nameLen",iocreate_open_info.getIoCOpen().getNameLen());
//                        result.put("name",iocreate_open_info.getIoCOpen().getName());
//                        result.put("fd",iocreate_open_info.getIoCOpen().getFd());
//                        result.put("flag",iocreate_open_info.getIoCOpen().getFlag());
//
//                        break;
//                    case 10052:
//                        TdrMsg.EDT_IOCLOSE_INFO ioclose_info=TdrMsg.EDT_IOCLOSE_INFO.parseFrom(resultBufferL);
//                        result.put("evtId",ioclose_info.getEvtId());
//                        result.put("timeStamp",ioclose_info.getTimeStamp());
//                        result.put("objId",ioclose_info.getIoClose().getObjId());
//                        result.put("classId",ioclose_info.getIoClose().getClassId());
//                        result.put("fd",ioclose_info.getIoClose().getFd());
//
//                        break;
//                    case 10054:
//                        TdrMsg.EDT_IODELETE_INFO iodelete_info=TdrMsg.EDT_IODELETE_INFO.parseFrom(resultBufferL);
//                        result.put("evtId",iodelete_info.getEvtId());
//                        result.put("timeStamp",iodelete_info.getTimeStamp());
//                        result.put("length",iodelete_info.getIoDelete().getLength());
//                        result.put("classId",iodelete_info.getIoDelete().getClassId());
//                        result.put("name",iodelete_info.getIoDelete().getName());
//
//                        break;
//                    case 10082:
//                        TdrMsg.EDT_EDRINJECT_INFO edrinject_info=TdrMsg.EDT_EDRINJECT_INFO.parseFrom(resultBufferL);
//                        result.put("evtId",edrinject_info.getEvtId());
//                        result.put("timeStamp",edrinject_info.getTimeStamp());
//                        result.put("kind",edrinject_info.getEdrInjectInfo().getKind());
//                        result.put("fileName",edrinject_info.getEdrInjectInfo().getFileName());
//                        result.put("lineNumber",edrinject_info.getEdrInjectInfo().getLineNumber());
//                        result.put("address",edrinject_info.getEdrInjectInfo().getAddress());
//                        result.put("message",edrinject_info.getEdrInjectInfo().getMessage());
//
//                        break;
//                    case 10504:
//                        TdrMsg.EDT_SEND_BUFFER_INFO send_buffer_info=TdrMsg.EDT_SEND_BUFFER_INFO.parseFrom(resultBufferL);
//                        result.put("evtId",send_buffer_info.getEvtId());
//                        result.put("timeStamp",send_buffer_info.getTimeStamp());
//                        result.put("pdId",send_buffer_info.getPdId());
//                        result.put("portId",send_buffer_info.getPortId());
//                        result.put("bufferId",send_buffer_info.getBufferId());
//                        result.put("timeOut",send_buffer_info.getTimeOut());
//                        result.put("length",send_buffer_info.getLength());
//                        result.put("objType",send_buffer_info.getObjType());
//                        result.put("name",send_buffer_info.getName());
//
//                        break;
//                    case 10505:
//                        TdrMsg.EDT_RECV_BUFFER_INFO recv_buffer_info=TdrMsg.EDT_RECV_BUFFER_INFO.parseFrom(resultBufferL);
//                        result.put("evtId",recv_buffer_info.getEvtId());
//                        result.put("timeStamp",recv_buffer_info.getTimeStamp());
//                        result.put("pdId",recv_buffer_info.getPdId());
//                        result.put("portId",recv_buffer_info.getPortId());
//                        result.put("bufferId",recv_buffer_info.getBufferId());
//                        result.put("timeOut",recv_buffer_info.getTimeOut());
//                        result.put("objType",recv_buffer_info.getObjType());
//                        result.put("name",recv_buffer_info.getName());
//
//                        break;
//                    case 10507:
//                        TdrMsg.EDT_SET_EVENT_INFO set_event_info=TdrMsg.EDT_SET_EVENT_INFO.parseFrom(resultBufferL);
//                        result.put("evtId",set_event_info.getEvtId());
//                        result.put("timeStamp",set_event_info.getTimeStamp());
//                        result.put("pdId",set_event_info.getPdId());
//                        result.put("portId",set_event_info.getPortId());
//                        result.put("eventId",set_event_info.getEventId());
//                        result.put("state",set_event_info.getState());
//                        result.put("objType",set_event_info.getObjType());
//                        result.put("name",set_event_info.getName());
//
//                        break;
//                    case 10508:
//                        TdrMsg.EDT_RESET_EVENT_INFO reset_event_info=TdrMsg.EDT_RESET_EVENT_INFO.parseFrom(resultBufferL);
//                        result.put("evtId",reset_event_info.getEvtId());
//                        result.put("timeStamp",reset_event_info.getTimeStamp());
//                        result.put("pdId",reset_event_info.getPdId());
//                        result.put("portId",reset_event_info.getPortId());
//                        result.put("eventId",reset_event_info.getEventId());
//                        result.put("state",reset_event_info.getState());
//                        result.put("objType",reset_event_info.getObjType());
//                        result.put("name",reset_event_info.getName());
//
//                        break;
//                    case 10509:
//                        TdrMsg.EDT_WAIT_EVENT_INFO wait_event_info=TdrMsg.EDT_WAIT_EVENT_INFO.parseFrom(resultBufferL);
//                        result.put("evtId",wait_event_info.getEvtId());
//                        result.put("timeStamp",wait_event_info.getTimeStamp());
//                        result.put("pdId",wait_event_info.getPdId());
//                        result.put("portId",wait_event_info.getPortId());
//                        result.put("eventId",wait_event_info.getEventId());
//                        result.put("state",wait_event_info.getState());
//                        result.put("objType",wait_event_info.getObjType());
//                        result.put("name",wait_event_info.getName());
//
//                        break;
//                    case 10511:
//                        TdrMsg.EDT_DIS_BLACKBOARD_INFO dis_blackboard_info=TdrMsg.EDT_DIS_BLACKBOARD_INFO.parseFrom(resultBufferL);
//                        result.put("evtId",dis_blackboard_info.getEvtId());
//                        result.put("timeStamp",dis_blackboard_info.getTimeStamp());
//                        result.put("pdId",dis_blackboard_info.getPdId());
//                        result.put("portId",dis_blackboard_info.getPortId());
//
//                        result.put("balckBoardId",dis_blackboard_info.getBalckBoardId());
//                        result.put("displayLength",dis_blackboard_info.getDisplayLength());
//                        result.put("name",dis_blackboard_info.getName());
//                        result.put("objType",dis_blackboard_info.getObjType());
//                        result.put("msgLength",dis_blackboard_info.getMsgLength());
//                        for(int i=0;i<dis_blackboard_info.getMessageCount()-1;i++){
//                            JsonObject dis_blackboard_res=new JsonObject();
//                            dis_blackboard_res.put("message",dis_blackboard_info.getMessage(i));
//                            result.put("message"+i,dis_blackboard_res);
//                        }
//
//                        break;
//                    case 10512:
//                        TdrMsg.EDT_READ_BLACKBOARD_INFO read_blackboard_info=TdrMsg.EDT_READ_BLACKBOARD_INFO.parseFrom(resultBufferL);
////                        if(read_blackboard_info.hasBalckBoardId()){
////
////                        }
//                        result.put("evtId",read_blackboard_info.getEvtId());
//                        result.put("timeStamp",read_blackboard_info.getTimeStamp());
//                        result.put("pdId",read_blackboard_info.getPdId());
//                        result.put("portId",read_blackboard_info.getPortId());
//                        result.put("balckBoardId",read_blackboard_info.getBalckBoardId());
//                        result.put("timeout",read_blackboard_info.getTimeOut());
//                        result.put("state",read_blackboard_info.getState());
//                        result.put("objType",read_blackboard_info.getObjType());
//                        result.put("name",read_blackboard_info.getName());
//                        result.put("msgLength",read_blackboard_info.getMsgLength());
//                        for(int i=0;i<read_blackboard_info.getMessageCount()-1;i++){
//                            JsonObject read_blackboard_res=new JsonObject();
//                            read_blackboard_res.put("message",read_blackboard_info.getMessage(i));
//                            result.put("message"+i,read_blackboard_res);
//                        }
//
//                        break;
//                    case 10513:
//                        TdrMsg.EDT_CLR_BLACKBOARD_INFO clr_blackboard_info=TdrMsg.EDT_CLR_BLACKBOARD_INFO.parseFrom(resultBufferL);
//                        result.put("evtId",clr_blackboard_info.getEvtId());
//                        result.put("timeStamp",clr_blackboard_info.getTimeStamp());
//                        result.put("pdId",clr_blackboard_info.getPdId());
//                        result.put("portId",clr_blackboard_info.getPortId());
//                        result.put("balckBoardId",clr_blackboard_info.getBalckBoardId());
//                        result.put("state",clr_blackboard_info.getState());
//                        result.put("objType",clr_blackboard_info.getObjType());
//                        result.put("name",clr_blackboard_info.getName());
////                        result.put("msgLength",clr_blackboard_info.getMsgLength());
////                        for(int i=1;i<clr_blackboard_info.getMessageCount();i++){
////                            JsonObject clr_blackboard_res=new JsonObject();
////                            clr_blackboard_res.put("message",clr_blackboard_info.getMessage(i));
////                            result.put("message"+i,clr_blackboard_res);
////                        }
//
//                        break;
//                    case 10528:
//                        TdrMsg.EDT_WRITE_SAMPLING_MSG_INFO write_sampling_msg_info=TdrMsg.EDT_WRITE_SAMPLING_MSG_INFO.parseFrom(resultBufferL);
//                        result.put("evtId",write_sampling_msg_info.getEvtId());
//                        result.put("timeStamp",write_sampling_msg_info.getTimeStamp());
//                        result.put("pdId",write_sampling_msg_info.getPdId());
//                        result.put("portId",write_sampling_msg_info.getPortId());
//                        result.put("sPortId",write_sampling_msg_info.getSPortId());
//                        result.put("length",write_sampling_msg_info.getLength());
//                        result.put("objType",write_sampling_msg_info.getObjType());
//                        result.put("name",write_sampling_msg_info.getName());
//                        for(int i=0;i<write_sampling_msg_info.getMessageCount()-1;i++){
//                            JsonObject write_sampling_msg_res=new JsonObject();
//                            write_sampling_msg_res.put("message",write_sampling_msg_info.getMessage(i));
//                            result.put("message"+i,write_sampling_msg_res);
//                        }
//
//                        break;
//                    case 10529:
//                        TdrMsg.EDT_READ_SAMPLING_MSG_INFO read_sampling_msg_info=TdrMsg.EDT_READ_SAMPLING_MSG_INFO.parseFrom(resultBufferL);
//                        result.put("evtId",read_sampling_msg_info.getEvtId());
//                        result.put("timeStamp",read_sampling_msg_info.getTimeStamp());
//                        result.put("pdId",read_sampling_msg_info.getPdId());
//                        result.put("portId",read_sampling_msg_info.getPortId());
//                        result.put("sPortId",read_sampling_msg_info.getSPortId());
////                        result.put("length",read_sampling_msg_info.getLength());
//                        result.put("objType",read_sampling_msg_info.getObjType());
//                        result.put("name",read_sampling_msg_info.getName());
////                        for(int i=1;i<read_sampling_msg_info.getMessageCount();i++){
////                            JsonObject read_sampling_msg_res=new JsonObject();
////                            read_sampling_msg_res.put("message",read_sampling_msg_info.getMessage(i));
////                            result.put("message"+i,read_sampling_msg_res);
////                        }
//
//                        break;
//                    case 10531:
//                        TdrMsg.EDT_SEND_QUEUING_MSG_INFO send_queuing_msg_info=TdrMsg.EDT_SEND_QUEUING_MSG_INFO.parseFrom(resultBufferL);
//                        result.put("evtId",send_queuing_msg_info.getEvtId());
//                        result.put("timeStamp",send_queuing_msg_info.getTimeStamp());
//                        result.put("pdId",send_queuing_msg_info.getPdId());
//                        result.put("portId",send_queuing_msg_info.getPortId());
//                        result.put("qPortId",send_queuing_msg_info.getQPortId());
//                        result.put("timeOut",send_queuing_msg_info.getTimeOut());
//                        result.put("length",send_queuing_msg_info.getLength());
//                        result.put("objType",send_queuing_msg_info.getObjType());
//                        result.put("name",send_queuing_msg_info.getName());
//                        for(int i=0;i<send_queuing_msg_info.getMessageCount()-1;i++){
//                            JsonObject send_queuing_msg_res=new JsonObject();
//                            send_queuing_msg_res.put("message",send_queuing_msg_info.getMessage(i));
//                            result.put("message"+i,send_queuing_msg_res);
//                        }
//
//                        break;
//                    case 10532:
//                        TdrMsg.EDT_RECV_QUEUING_MSG_INFO recv_queuing_msg_info=TdrMsg.EDT_RECV_QUEUING_MSG_INFO.parseFrom(resultBufferL);
//                        result.put("evtId",recv_queuing_msg_info.getEvtId());
//                        result.put("timeStamp",recv_queuing_msg_info.getTimeStamp());
//                        result.put("pdId",recv_queuing_msg_info.getPdId());
//                        result.put("portId",recv_queuing_msg_info.getPortId());
//                        result.put("qPortId",recv_queuing_msg_info.getQPortId());
//                        result.put("timeOut",recv_queuing_msg_info.getTimeOut());
//                        result.put("maxMsgSize",recv_queuing_msg_info.getMaxMsgSize());
//                        result.put("objType",recv_queuing_msg_info.getObjType());
//                        result.put("name",recv_queuing_msg_info.getName());
//                        for(int i=0;i<recv_queuing_msg_info.getMessageCount()-1;i++){
//                            JsonObject recv_queuing_msg_res=new JsonObject();
//                            recv_queuing_msg_res.put("message",recv_queuing_msg_info.getMessage(i));
//                            result.put("message"+i,recv_queuing_msg_res);
//                        }
//
//                        break;
//                    case 80001:
//                        TdrMsg.EDT_EVENT_TASKTABLE event_tasktable=TdrMsg.EDT_EVENT_TASKTABLE.parseFrom(resultBufferL);
//                        result.put("evtId",event_tasktable.getEvtId());
//                        result.put("timeStamp",event_tasktable.getTimeStamp());
//                        result.put("pdId",event_tasktable.getPdId());
//                        result.put("portId",event_tasktable.getPortId());
//                        for(int i=0;i<event_tasktable.getSysTaskTableCount()-1;i++){
//                            JsonObject event_tasktable_res=new JsonObject();
//                            event_tasktable_res.put("state",event_tasktable.getSysTaskTable(i).getState());
//                            event_tasktable_res.put("priority",event_tasktable.getSysTaskTable(i).getPriority());
//                            event_tasktable_res.put("lockCnt",event_tasktable.getSysTaskTable(i).getLockCnt());
//                            event_tasktable_res.put("tid",event_tasktable.getSysTaskTable(i).getTid());
////                            event_tasktable_res.put("entryPt",event_tasktable.getSysTaskTable(i).getEntryPt());
////                            event_tasktable_res.put("rtp",event_tasktable.getSysTaskTable(i).getRtp());
////                            event_tasktable_res.put("affinity",event_tasktable.getSysTaskTable(i).getAffinity());
//                            event_tasktable_res.put("name",event_tasktable.getSysTaskTable(i).getName());
//                            result.put("task"+i,event_tasktable_res);
//                        }
//
//                        break;
//                    case 80002:
//                        TdrMsg.EDT_EVENT_TASKSWINFO event_taskswinfo=TdrMsg.EDT_EVENT_TASKSWINFO.parseFrom(resultBufferL);
//                        result.put("evtId",event_taskswinfo.getEvtId());
//                        result.put("timeStamp",event_taskswinfo.getTimeStamp());
//                        result.put("pdId",event_taskswinfo.getPdId());
//                        result.put("portId",event_taskswinfo.getPortId());
//                        result.put("tOldId",event_taskswinfo.getTOldId());
//                        result.put("tNewId",event_taskswinfo.getTNewId());
//
//                        break;
//                        //8003无定义
////                    case 80004:
////                        TdrMsg.java.EDT_EVENT_TASKTABLE_CORE_INFO event_tasktable_core_info=TdrMsg.java.EDT_EVENT_TASKTABLE_CORE_INFO.parseFrom(resultBuffer);
////                        result.put("evtId",event_tasktable_core_info.getEvtId());
////                        result.put("timeStamp",event_tasktable_core_info.getTimeStamp());
////                        result.put("pdId",read_sampling_msg_info.getPdId());
////                        result.put("portId",read_sampling_msg_info.getPortId());
////                        for(int i=1;i<event_tasktable_core_info.getSysTaskTableCount();i++){
////                            JsonObject event_tasktable_core_res=new JsonObject();
////                            event_tasktable_core_res.put("state",event_tasktable_core_info.getSysTaskTable(i).getState());
////                            event_tasktable_core_res.put("priority",event_tasktable_core_info.getSysTaskTable(i).getPriority());
////                            event_tasktable_core_res.put("lockCnt",event_tasktable_core_info.getSysTaskTable(i).getLockCnt());
////                            event_tasktable_core_res.put("tid",event_tasktable_core_info.getSysTaskTable(i).getTid());
////                            event_tasktable_core_res.put("ownerId",event_tasktable_core_info.getSysTaskTable(i).getOwnerId());
////                            event_tasktable_core_res.put("name",event_tasktable_core_info.getSysTaskTable(i).getName());
////                            result.put("taskTable"+i,event_tasktable_core_res);
////                        }
////
////                        break;
////                    case 80005:
////                        TdrMsg.java.EDT_EVENT_TASKNAME_CORE_INFO taskname_core_info=TdrMsg.java.EDT_EVENT_TASKNAME_CORE_INFO.parseFrom(resultBuffer);
////                        result.put("evtId",taskname_core_info.getEvtId());
////                        result.put("timeStamp",taskname_core_info.getTimeStamp());
////                        result.put("pdId",read_sampling_msg_info.getPdId());
////                        result.put("portId",read_sampling_msg_info.getPortId());
////                        result.put("state",taskname_core_info.getTasknameInfo().getState());
////                        result.put("priority",taskname_core_info.getTasknameInfo().getPriority());
////                        result.put("lockCnt",taskname_core_info.getTasknameInfo().getLockCnt());
////                        result.put("ownerId",taskname_core_info.getTasknameInfo().getOwnerId());
////                        result.put("tid",taskname_core_info.getTasknameInfo().getTid());
////                        result.put("name",taskname_core_info.getTasknameInfo().getName());
////
////                        break;
//                    case 80008:
//                        TdrMsg.EDT_EVENT_MFSW_INFO mfsw_info=TdrMsg.EDT_EVENT_MFSW_INFO.parseFrom(resultBufferL);
//                        result.put("evtId",mfsw_info.getEvtId());
//                        result.put("timeStamp",mfsw_info.getTimeStamp());
//                        result.put("pdId",mfsw_info.getPdId());
//                        result.put("portId",mfsw_info.getPortId());
//                        result.put("schedId",mfsw_info.getSchedId());
//
//                        break;
//                    case 80009:
//                        TdrMsg.EDT_EVENT_SWPARTATION swpartation=TdrMsg.EDT_EVENT_SWPARTATION.parseFrom(resultBufferL);
//                        result.put("evtId",swpartation.getEvtId());
//                        result.put("timeStamp",swpartation.getTimeStamp());
//                        result.put("pdId",swpartation.getPdId());
//                        result.put("portId",swpartation.getPortId());
//                        result.put("prevPartId",swpartation.getPrevPartId());
//                        result.put("currPartId",swpartation.getCurrPartId());
//
//                        break;
////                    case 80010:
////                        TdrMsg.java.EDT_OSAC_INFO osac_info=TdrMsg.java.EDT_OSAC_INFO.parseFrom(resultBuffer);
////                        result.put("evtId",osac_info.getEvtId());
////                        result.put("timeStamp",osac_info.getTimeStamp());
////                        result.put("pdId",read_sampling_msg_info.getPdId());
////                        result.put("portId",read_sampling_msg_info.getPortId());
////                        result.put("portNum",osac_info.getOsacT().getPortNum());
////                        result.put("portType",osac_info.getOsacT().getPortType());
////                        result.put("length",osac_info.getOsacT().getLength());
////                        result.put("name",osac_info.getOsacT().getName());
////                        for(int i=1;i<osac_info.getOsacT().getMsgBufCount();i++){
////                            JsonObject osac_res=new JsonObject();
////                            osac_res.put("message",osac_info.getOsacT().getMsgBuf(i));
////                            result.put("message"+i,osac_res);
////                        }
////
////                        break;
//                    case 80011:
//                        TdrMsg.EDT_TASKSTACK_INFO taskstack_info=TdrMsg.EDT_TASKSTACK_INFO.parseFrom(resultBufferL);
//                        result.put("evtId",taskstack_info.getEvtId());
//                        result.put("timeStamp",taskstack_info.getTimeStamp());
//                        result.put("pdId",taskstack_info.getPdId());
//                        result.put("portId",taskstack_info.getPortId());
//                        result.put("taskname",taskstack_info.getTaskInfoGet().getTaskname());
//                        result.put("entry",taskstack_info.getTaskInfoGet().getEntry());
//                        result.put("taskID",taskstack_info.getTaskInfoGet().getTaskID());
//                        result.put("stackSize",taskstack_info.getTaskInfoGet().getStackSize());
//                        result.put("sp",taskstack_info.getTaskInfoGet().getSp());
//                        result.put("stackBase",taskstack_info.getTaskInfoGet().getStackBase());
//                        result.put("stackEnd",taskstack_info.getTaskInfoGet().getStackEnd());
//                        result.put("highSize",taskstack_info.getTaskInfoGet().getHighSize());
//                        result.put("margin",taskstack_info.getTaskInfoGet().getMargin());
//                        System.out.println(resultBufferL);
//                        System.out.println(result);
//
//                        break;
//                    case 80013:
//                        TdrMsg.EDT_FUNC_TIME_USE_INFO func_time_use_info=TdrMsg.EDT_FUNC_TIME_USE_INFO.parseFrom(resultBufferL);
//                        result.put("evtId",func_time_use_info.getEvtId());
//                        result.put("timeStamp",func_time_use_info.getTimeStamp());
//                        result.put("pdId",func_time_use_info.getPdId());
//                        result.put("portId",func_time_use_info.getPortId());
//                        result.put("evtflag",func_time_use_info.getEvtflag());
//                        result.put("tmpCnt",func_time_use_info.getTmpCnt());
//                        result.put("funcId",func_time_use_info.getFuncId());
//
//                        break;
//                    case 120006:
//                        TdrMsg.EDT_EVENT_MFTABLE event_mftable=TdrMsg.EDT_EVENT_MFTABLE.parseFrom(resultBufferL);
//                        result.put("evtId",event_mftable.getEvtId());
//                        result.put("timeStamp",event_mftable.getTimeStamp());
//                        result.put("pdId",event_mftable.getPdId());
//                        result.put("portId",event_mftable.getPortId());
//                        for(int i=0;i<event_mftable.getMfTableCount()-1;i++){
//                            JsonObject event_mftable_res=new JsonObject();
//                            event_mftable_res.put("schedId",event_mftable.getMfTable(i).getSchedId());
//                            for(int j=0;i<event_mftable.getMfTable(i).getPartInfoCount();i++){
//                                JsonObject partInfo_res=new JsonObject();
//                                partInfo_res.put("partId",event_mftable.getMfTable(i).getPartInfo(j).getPartId());
//                                partInfo_res.put("partName",event_mftable.getMfTable(i).getPartInfo(j).getPartName());
//                                partInfo_res.put("duration",event_mftable.getMfTable(i).getPartInfo(j).getDuration());
//                                event_mftable_res.put("partInfo"+j,partInfo_res);
//                            }
////                            event_mftable_res.put("duration",event_mftable.getMfTable(i).getDuration());
//                            result.put("mftable"+i,event_mftable_res);
//                        }
//
//                        break;
//                    default:
//                        result.put("数据为空","111");
//
////                    case 120007:
////                        TdrMsg.java.EDT_EVENT_TASKTABLE_PART_INFO tasktable_part_info=TdrMsg.java.EDT_EVENT_TASKTABLE_PART_INFO.parseFrom(resultBuffer);
////                        result.put("evtId",tasktable_part_info.getEvtId());
////                        result.put("timeStamp",tasktable_part_info.getTimeStamp());
////                        result.put("pdId",read_sampling_msg_info.getPdId());
////                        result.put("portId",read_sampling_msg_info.getPortId());
////                        result.put("pd",tasktable_part_info.getPd());
////                        for(int i=1;i<tasktable_part_info.getPartTaskTableCount();i++){
////                            JsonObject tasktable_part_res=new JsonObject();
////                            tasktable_part_res.put("state",tasktable_part_info.getPartTaskTable(i).getState());
////                            tasktable_part_res.put("priority",tasktable_part_info.getPartTaskTable(i).getPriority());
////                            tasktable_part_res.put("lockCnt",tasktable_part_info.getPartTaskTable(i).getLockCnt());
////                            tasktable_part_res.put("tid",tasktable_part_info.getPartTaskTable(i).getTid());
////                            tasktable_part_res.put("ownerId",tasktable_part_info.getPartTaskTable(i).getOwnerId());
////                            tasktable_part_res.put("name",tasktable_part_info.getPartTaskTable(i).getName());
////                            result.put("tasktable_part"+i,tasktable_part_res);
////                        }
////
////                        break;
//                }
//            } catch (InvalidProtocolBufferException e) {
//                e.printStackTrace();
//            }
        });
        return future;







    }

    public JsonObject communicationParse(Object contentObject, Object flagObject, Object targetIPObject, Logger logger) {

        JsonObject response = new JsonObject();

        if (flagObject == null) {
            logger.error(String.format("exception: %s", "flag必填参数"));
            response.put("type", "error").put("code", PARAM_REQUIRED.getCode()).put("message", PARAM_REQUIRED.getMsg());
            return response;
//            response.error(PARAM_REQUIRED.getCode(), PARAM_REQUIRED.getMsg());

        } else if (!FormValidator.isString(flagObject.toString())) {
            logger.error(String.format("exception: %s", "flag格式错误"));
            response.put("type", "error").put("code", FORMAT_ERROR.getCode()).put("message", FORMAT_ERROR.getMsg());
//            response.error(FORMAT_ERROR.getCode(), FORMAT_ERROR.getMsg());
            return response;
        }


        if (targetIPObject == null) {
            logger.error(String.format("exception: %s", "目标机名称为必填参数"));

            response.put("type", "error").put("code", PARAM_REQUIRED.getCode()).put("message", PARAM_REQUIRED.getMsg());
            return response;
        } else if (!FormValidator.isString(targetIPObject)) {
            logger.error(String.format("exception: %s", "目标机名称格式错误"));
            response.put("type", "error").put("code", FORMAT_ERROR.getCode()).put("message", FORMAT_ERROR.getMsg());
//            response.error(FORMAT_ERROR.getCode(), FORMAT_ERROR.getMsg());
            return response;
        }
        targetIP = targetIPObject.toString();


        if (contentObject == null) {
            logger.error(String.format("exception: %s", "通信配置为必填参数"));
            response.put("type", "error").put("code", PARAM_REQUIRED.getCode()).put("message", PARAM_REQUIRED.getMsg());
            return response;
        } else if (!FormValidator.isJsonObject(contentObject)) {
            logger.error(String.format("exception: %s", "通信配置格式错误"));
            response.put("type", "error").put("code", FORMAT_ERROR.getCode()).put("message", FORMAT_ERROR.getMsg());
//            response.error(FORMAT_ERROR.getCode(), FORMAT_ERROR.getMsg());
            return response;
        }
        JsonObject content = (JsonObject) contentObject;
        if (!content.containsKey("EDT_TDR_CFGCTR_MSG")) {
            logger.error(String.format("exception: %s", "通信配置为必填参数"));
            response.put("type", "error").put("code", PARAM_REQUIRED.getCode()).put("message", PARAM_REQUIRED.getMsg());
            return response;
        }

        JsonObject EDT_TDR_CFGCTR_MSG = content.getJsonObject("EDT_TDR_CFGCTR_MSG");


//        System.out.println(flag.toString());
        if (flagObject.toString().equals("false")) {//与上次请求一致，无须特殊处理

//           if(content.containsKey("EDT_DATA_ANALYSIS_CFG")){
//               targetGuid = content.getJsonObject("EDT_DATA_ANALYSIS_CFG").getString("targetGuid");
//               EDT_DATA = EDT_DATA_ANALYSIS_CFG(targetGuid);
//           }

            Future<JsonObject> future  = EDT_TDR_CFGCTR_MSG(targetIP, logger);
            List<JsonObject> list = new ArrayList();
            future.setHandler(r->{
                response.put("type","success").put("result",future.result());
                list.add(response);
            });
            while (list.size() != 1){
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            return list.get(0);


        } else if (flagObject.toString().equals("true")) {
//            Object contentObject =request.getParamWithKey(GlobalConsts.CONTENT);
//
//            if(contentObject == null){
//                logger.error(String.format("exception: %s","模板配置为必填参数"));
//                response.error(PARAM_REQUIRED.getCode(),PARAM_REQUIRED.getMsg());
//                return;
//            }else if(!FormValidator.isJsonObject(contentObject)){
//                logger.error(String.format("exception: %s","模板配置格式错误"));
//                response.error(FORMAT_ERROR.getCode(),FORMAT_ERROR.getMsg());
//                return;
//            }
//            JsonObject content =(JsonObject) contentObject;

            UdpPacket.UDP_COMMUNCATE_PACKET.Builder udpPacket = UdpPacket.UDP_COMMUNCATE_PACKET.newBuilder();//构建udp数据包
            udpPacket.setTimeStamp(System.currentTimeMillis()) //时间戳
                    .setMsgType(1)//消息类型
                    .setIpAddr(targetIP);//ip地址

//            UdpPacket.UDP_COMMUNCATE_PACKET.Builder udpPacket_analysis=UdpPacket.UDP_COMMUNCATE_PACKET.newBuilder();//构建udp数据包
//            udpPacket_analysis.setTimeStamp(System.currentTimeMillis()) //时间戳
//                    .setMsgType(1)//消息类型
//                    .setIpAddr((String)content .getValue(GlobalConsts.TARGET_IP)) ;//ip地址
//                    .setPort(Integer.parseInt((String) content.getValue(GlobalConsts.PORT_STR))); //端口号
//            ByteConversion byteConversion=new ByteConversion();
//            byte[] buf=byteConversion.intToByteArray(1);
//            System.out.println(buf.toString());

            HdpMsg.EDT_TDR_CFGCTR_MSG.Builder edt_tdr_cfgctr_msg = HdpMsg.EDT_TDR_CFGCTR_MSG.newBuilder();//整体命令构建
            HdpMsg.GET_CMD_ST.Builder getCmd = HdpMsg.GET_CMD_ST.newBuilder();
            HdpMsg.CFG_CMD_ST.Builder cfg_cmd_st = HdpMsg.CFG_CMD_ST.newBuilder();
            HdpMsg.CTR_CMD_ST.Builder ctr_cmd_st = HdpMsg.CTR_CMD_ST.newBuilder();

            if (content.containsKey("EDT_TDR_CFGCTR_MSG")) {
//                JsonObject EDT_TDR_CFGCTR_MSG = content.getJsonObject("EDT_TDR_CFGCTR_MSG");
                if (EDT_TDR_CFGCTR_MSG.containsKey("GET_CMD_ST")) {//获取命令格式

                    JsonObject getCMDParams = EDT_TDR_CFGCTR_MSG.getJsonObject("GET_CMD_ST");
                    if (getCMDParams.containsKey("ERR_GET_INFO")) {//故障记录获取配置信息
                        HdpMsg.ERR_GET_INFO.Builder err_get_Info = HdpMsg.ERR_GET_INFO.newBuilder();//故障记录获取配置信息
                        JsonObject ERR_GET_INFO = getCMDParams.getJsonObject("ERR_GET_INFO");
                        err_get_Info.setErrType(Integer.parseInt((String) ERR_GET_INFO.getValue(GlobalConsts.ERRTYPE)));
                        if (ERR_GET_INFO.containsKey("ERR_HEAD_GET")) {
                            HdpMsg.ERR_HEAD_GET.Builder head = HdpMsg.ERR_HEAD_GET.newBuilder();//获取故障记录信息头部
                            JsonObject ERR_HEAD_GET = ERR_GET_INFO.getJsonObject("ERR_HEAD_GET");
                            head.setPartId(Integer.parseInt((String) ERR_HEAD_GET.getValue(GlobalConsts.PART_ID)));//头部信息所在分区
                            err_get_Info.setHead(head);
                        }
                        if (ERR_GET_INFO.containsKey("ERR_ENTRY_GET")) {
                            HdpMsg.ERR_ENTRY_GET.Builder entries = HdpMsg.ERR_ENTRY_GET.newBuilder();//故障记录信息条目
                            JsonObject ERR_ENTRY_GET = ERR_GET_INFO.getJsonObject("ERR_ENTRY_GET");
                            entries.setStartEntry(Integer.parseInt((String) ERR_ENTRY_GET.getValue(GlobalConsts.START_ENTRY)));
                            entries.setReqNum(Integer.parseInt((String) ERR_ENTRY_GET.getValue(GlobalConsts.REQ_NUM)));
                            entries.setPartId(Integer.parseInt((String) ERR_ENTRY_GET.getValue(GlobalConsts.PART_ID)));
                            err_get_Info.setEntries(entries);
                        }
                        getCmd.setErrGetInfo(err_get_Info);
                    }
                    if (getCMDParams.containsKey("IMG_GET_INFO")) {
                        HdpMsg.IMG_GET_INFO.Builder img_getInfo = HdpMsg.IMG_GET_INFO.newBuilder();//获取空间数据信息
                        JsonObject IMG_GET_INFO = getCMDParams.getJsonObject("IMG_GET_INFO");
                        img_getInfo.setTid(Integer.parseInt((String) IMG_GET_INFO.getValue(GlobalConsts.TID)));
                        getCmd.setImgGetInfo(img_getInfo);


                    }
                    if (getCMDParams.containsKey("TIMESYN_GET_INFO")) {
                        HdpMsg.TIMESYN_GET_INFO.Builder timesyn_get_info = HdpMsg.TIMESYN_GET_INFO.newBuilder();//获取同步信息
                        JsonObject TIMESYN_GET_INFO = getCMDParams.getJsonObject("TIMESYN_GET_INFO");
                        String TIME_TAG = TIMESYN_GET_INFO.getString(GlobalConsts.TIME_TAG).substring(2);
                        System.out.println(TIME_TAG);
                        timesyn_get_info.setTimeTag(Integer.parseInt(TIME_TAG, 16));
                        getCmd.setTimeGetInfo(timesyn_get_info);
                    }
                    edt_tdr_cfgctr_msg.setGetCmd(getCmd);
                }

                if (EDT_TDR_CFGCTR_MSG.containsKey("CFG_CMD_ST")) { //配置命令格式
                    //系统检测事件配置
                    HdpMsg.SYS_DET_CFG.Builder sys_det_cfg = HdpMsg.SYS_DET_CFG.newBuilder();//时间资源
                    //时间资源
                    HdpMsg.TIMESRC_CFG.Builder timeSrc_cfg = HdpMsg.TIMESRC_CFG.newBuilder();//时间资源
                    HdpMsg.CFG_TSKSWT.Builder cfg_tskswt = HdpMsg.CFG_TSKSWT.newBuilder();//任务切换
                    HdpMsg.CFG_PARTSW.Builder cfg_partsw = HdpMsg.CFG_PARTSW.newBuilder();//分区切换
                    HdpMsg.CFG_MFSW.Builder cfg_mfsw = HdpMsg.CFG_MFSW.newBuilder();//分区切换


                    //空间资源
                    HdpMsg.SPACESRC_CFG.Builder spacesrc_cfg = HdpMsg.SPACESRC_CFG.newBuilder();//空间信息
                    HdpMsg.CFG_TASKSPAWN.Builder cfg_taskspawn = HdpMsg.CFG_TASKSPAWN.newBuilder();//任务创建事件ID
                    HdpMsg.CFG_TASKDELETE.Builder cfg_taskdelete = HdpMsg.CFG_TASKDELETE.newBuilder();////任务删除事件ID
                    HdpMsg.CFG_IOCREATE.Builder cfg_iocreate = HdpMsg.CFG_IOCREATE.newBuilder();//IO创建事件ID
                    HdpMsg.CFG_IODELETE.Builder cfg_iodelete = HdpMsg.CFG_IODELETE.newBuilder();////IO删除事件ID
                    HdpMsg.CFG_SEMCREATE.Builder cfg_semcreate = HdpMsg.CFG_SEMCREATE.newBuilder();//信号量创建事件ID
                    HdpMsg.CFG_SEMDELETE.Builder cfg_semdelete = HdpMsg.CFG_SEMDELETE.newBuilder();//信号量删除事件ID
                    HdpMsg.CFG_WDCREATE.Builder cfg_wdcreate = HdpMsg.CFG_WDCREATE.newBuilder();//看门狗创建事件ID
                    HdpMsg.CFG_WDDELETE.Builder cfg_wddelete = HdpMsg.CFG_WDDELETE.newBuilder();//看门狗删除事件ID
                    HdpMsg.CFG_MSGQCREATE.Builder cfg_msgqcreate = HdpMsg.CFG_MSGQCREATE.newBuilder();//消息队列创建事件ID
                    HdpMsg.CFG_MSGQDELETE.Builder cfg_msgqdelete = HdpMsg.CFG_MSGQDELETE.newBuilder();//消息队列删除事件ID
                    HdpMsg.CFG_MALLOC.Builder cfg_malloc = HdpMsg.CFG_MALLOC.newBuilder();//malloc事件ID
                    HdpMsg.CFG_MEMPARTCREATE.Builder cfg_mempartcreate = HdpMsg.CFG_MEMPARTCREATE.newBuilder();//memPartCreate事件ID
                    HdpMsg.CFG_FREE.Builder cfg_free = HdpMsg.CFG_FREE.newBuilder();//free事件ID
                    HdpMsg.CFG_STACK.Builder cfg_stack = HdpMsg.CFG_STACK.newBuilder();//stack事件ID
                    HdpMsg.CFG_PDMEM.Builder cfg_pdmem = HdpMsg.CFG_PDMEM.newBuilder();//周期性内存资源监控
                    HdpMsg.CFG_APEXSEMCRE.Builder cfg_apexsemcre = HdpMsg.CFG_APEXSEMCRE.newBuilder();//apex信号量创建
                    HdpMsg.CFG_APEXBLACRE.Builder cfg_apexblacre = HdpMsg.CFG_APEXBLACRE.newBuilder();    //apex黑板创建
                    HdpMsg.CFG_APEXBUFCRE.Builder cfg_apexbufcre = HdpMsg.CFG_APEXBUFCRE.newBuilder();//apexbuffer创建
                    HdpMsg.CFG_APEXPROCCRE.Builder cfg_apexproccre = HdpMsg.CFG_APEXPROCCRE.newBuilder();//apex进程创建
                    HdpMsg.CFG_APEXEVENTCRE.Builder cfg_apexeventcre = HdpMsg.CFG_APEXEVENTCRE.newBuilder();//apex事件创建
                    HdpMsg.CFG_APEXSPORTCRE.Builder cfg_apexsportcre = HdpMsg.CFG_APEXSPORTCRE.newBuilder();    //apex sPort创建
                    HdpMsg.CFG_APEXQPORTCRE.Builder cfg_apexqportcre = HdpMsg.CFG_APEXQPORTCRE.newBuilder();//apex qPort创建
                    HdpMsg.CFG_PTIMERCREATE.Builder cfg_ptimercrete = HdpMsg.CFG_PTIMERCREATE.newBuilder();//POSIX_TIMER创建
                    HdpMsg.CFG_PTIMERDELETE.Builder cfg_ptimerdelete = HdpMsg.CFG_PTIMERDELETE.newBuilder();//POSIX_TIMER删除


                    //系统事件
                    HdpMsg.SYSEVT_CFG.Builder sysevt_cfg = HdpMsg.SYSEVT_CFG.newBuilder();//系统事件
                    HdpMsg.CFG_ISR.Builder cfg_isr = HdpMsg.CFG_ISR.newBuilder();//中断事件ID
                    HdpMsg.CFG_EPT.Builder cfg_ept = HdpMsg.CFG_EPT.newBuilder();//异常事件ID
                    HdpMsg.CFG_EDR.Builder cfg_edr = HdpMsg.CFG_EDR.newBuilder();//edr事件ID

                    //数据通信
                    HdpMsg.DATACOM_CFG.Builder datacom_cfg = HdpMsg.DATACOM_CFG.newBuilder();//数据通信总结构体
                    HdpMsg.CFG_OSACSEND.Builder cfg_osac_send = HdpMsg.CFG_OSACSEND.newBuilder();// //OSAC事件
                    HdpMsg.CFG_OSACRECV.Builder cfg_osac_recv = HdpMsg.CFG_OSACRECV.newBuilder();// //OSAC事件
                    HdpMsg.CFG_MSGQSND.Builder cfg_msgqsnd = HdpMsg.CFG_MSGQSND.newBuilder();//msgQSend事件
                    HdpMsg.CFG_APEXRDBLA.Builder cfg_apexrdbla = HdpMsg.CFG_APEXRDBLA.newBuilder();//msgQReceive事件
                    HdpMsg.CFG_MSGQREC.Builder cfg_msgqrec = HdpMsg.CFG_MSGQREC.newBuilder();//msgQReceive事件
                    HdpMsg.CFG_APEXCLRBLA.Builder cfg_apexclrbla = HdpMsg.CFG_APEXCLRBLA.newBuilder();//msgQReceive事件
                    HdpMsg.CFG_APEXDISBLA.Builder cfg_apexdisbla = HdpMsg.CFG_APEXDISBLA.newBuilder();//msgQReceive事件
                    HdpMsg.CFG_APEXSENDBUF.Builder cfg_apexsendbuf = HdpMsg.CFG_APEXSENDBUF.newBuilder();//msgQReceive事件
                    HdpMsg.CFG_APEXRECVBUF.Builder cfg_apexrecvbuf = HdpMsg.CFG_APEXRECVBUF.newBuilder();//msgQReceive事件
                    HdpMsg.CFG_APEXSETEVENT.Builder cfg_apexsetevent = HdpMsg.CFG_APEXSETEVENT.newBuilder();//msgQReceive事件
                    HdpMsg.CFG_APEXWAITEVENT.Builder cfg_apexwaitevent = HdpMsg.CFG_APEXWAITEVENT.newBuilder();//msgQReceive事件
                    HdpMsg.CFG_APEXWRSPORT.Builder cfg_apexwrsport = HdpMsg.CFG_APEXWRSPORT.newBuilder();//msgQReceive事件
                    HdpMsg.CFG_APEXRDSPORT.Builder cfg_apexrdsport = HdpMsg.CFG_APEXRDSPORT.newBuilder();//msgQReceive事件
                    HdpMsg.CFG_APEXSENDQPORT.Builder cfg_apexsendqport = HdpMsg.CFG_APEXSENDQPORT.newBuilder();//msgQReceive事件
                    HdpMsg.CFG_APEXRECVQPORT.Builder cfg_apexrecvqport = HdpMsg.CFG_APEXRECVQPORT.newBuilder();//msgQReceive事件

                    JsonObject CFG_CMD_ST = EDT_TDR_CFGCTR_MSG.getJsonObject("CFG_CMD_ST");
                    if (CFG_CMD_ST.containsKey("SYS_DET_CFG")) {
                        JsonObject SYS_DET_CFG = CFG_CMD_ST.getJsonObject("SYS_DET_CFG"); //系统检测事件配置
                        if (SYS_DET_CFG.containsKey("TIMESRC_CFG")) {//时间资源
                            JsonObject TIMESRC_CFG = SYS_DET_CFG.getJsonObject("TIMESRC_CFG");
                            if (TIMESRC_CFG.containsKey("CFG_TSKSWT")) {
                                JsonObject CFG_TSKSWT = TIMESRC_CFG.getJsonObject("CFG_TSKSWT");
                                cfg_tskswt.setTskSwId(Integer.parseInt((String) CFG_TSKSWT.getValue(GlobalConsts.TSKSWID)));
                                timeSrc_cfg.setTskSwtInfo(cfg_tskswt);
                            }
                            if (TIMESRC_CFG.containsKey("CFG_PARTSW")) {
                                JsonObject CFG_PARTSW = TIMESRC_CFG.getJsonObject("CFG_PARTSW");
                                cfg_partsw.setPartSwId(Integer.parseInt((String) CFG_PARTSW.getValue(GlobalConsts.PARTSSWID)));
                                timeSrc_cfg.setPartSwInfo(cfg_partsw);
                            }
                            if (TIMESRC_CFG.containsKey("CFG_MFSW")) {
                                JsonObject CFG_MFSW = TIMESRC_CFG.getJsonObject("CFG_MFSW");
                                cfg_mfsw.setMfSwId(Integer.parseInt((String) CFG_MFSW.getValue(GlobalConsts.MFSWID)));
                                timeSrc_cfg.setMfswInfo(cfg_mfsw);
                            }
                            sys_det_cfg.setTimeSrcCfg(timeSrc_cfg);

                        }

                        if (SYS_DET_CFG.containsKey("SPACESRC_CFG")) {//空间资源
                            JsonObject SPACESRC_CFG = SYS_DET_CFG.getJsonObject("SPACESRC_CFG");
                            if (SPACESRC_CFG.containsKey("CFG_TASKSPAWN")) {//任务创建（内核+分区）
                                JsonObject CFG_TASKSPAWN = SPACESRC_CFG.getJsonObject("CFG_TASKSPAWN");
                                cfg_taskspawn.setTskSpId(Integer.parseInt((String) CFG_TASKSPAWN.getValue(GlobalConsts.TSKSPID)));
                                spacesrc_cfg.setTaskSpawnInfo(cfg_taskspawn);
                            }
                            if (SPACESRC_CFG.containsKey("CFG_TASKDELETE")) {   //任务删除（内核+分区）
                                JsonObject CFG_TASKDELETE = SPACESRC_CFG.getJsonObject("CFG_TASKDELETE");
                                cfg_taskdelete.setTskDtId(Integer.parseInt((String) CFG_TASKDELETE.getValue(GlobalConsts.TSKDTID)));
                                spacesrc_cfg.setTaskDeleteInfo(cfg_taskdelete);
                            }
                            if (SPACESRC_CFG.containsKey("CFG_IOCREATE")) {   //IO创建（内核）
                                JsonObject CFG_IOCREATE = SPACESRC_CFG.getJsonObject("CFG_IOCREATE");
                                cfg_iocreate.setIoCtId(Integer.parseInt((String) CFG_IOCREATE.getValue(GlobalConsts.IOCTID)));
                                spacesrc_cfg.setIoCreateInfo(cfg_iocreate);
                            }
                            if (SPACESRC_CFG.containsKey("CFG_IODELETE")) {//IO删除（内核）
                                JsonObject CFG_IODELETE = SPACESRC_CFG.getJsonObject("CFG_IODELETE");
                                cfg_iodelete.setIoDtId(Integer.parseInt((String) CFG_IODELETE.getValue(GlobalConsts.IODTID)));
                                spacesrc_cfg.setIoDeleteInfo(cfg_iodelete);
                            }
                            if (SPACESRC_CFG.containsKey("CFG_SEMCREATE")) { //信号量创建（内核+分区）
                                JsonObject CFG_SEMCREATE = SPACESRC_CFG.getJsonObject("CFG_SEMCREATE");
                                cfg_semcreate.setSemCtId(Integer.parseInt((String) CFG_SEMCREATE.getValue(GlobalConsts.SEMCTID)));
                                spacesrc_cfg.setSemCreateInfo(cfg_semcreate);
                            }
                            if (SPACESRC_CFG.containsKey("CFG_SEMDELETE")) {  //信号量删除（内核+分区）
                                JsonObject CFG_SEMDELETE = SPACESRC_CFG.getJsonObject("CFG_SEMDELETE");
                                cfg_semdelete.setSemDtId(Integer.parseInt((String) CFG_SEMDELETE.getValue(GlobalConsts.SEMDTID)));
                                spacesrc_cfg.setSemDeleteInfo(cfg_semdelete);
                            }
                            if (SPACESRC_CFG.containsKey("CFG_WDCREATE")) { //看门狗定时器创建（内核+分区）
                                JsonObject CFG_WDCREATE = SPACESRC_CFG.getJsonObject("CFG_WDCREATE");
                                cfg_wdcreate.setWdCtId(Integer.parseInt((String) CFG_WDCREATE.getValue(GlobalConsts.WDCTID)));
                                spacesrc_cfg.setWdCreateInfo(cfg_wdcreate);
                            }
                            if (SPACESRC_CFG.containsKey("CFG_WDDELETE")) {//看门狗定时器删除（内核+分区）
                                JsonObject CFG_WDDELETE = SPACESRC_CFG.getJsonObject("CFG_WDDELETE");
                                cfg_wddelete.setWdDtId(Integer.parseInt((String) CFG_WDDELETE.getValue(GlobalConsts.WDDTID)));
                                spacesrc_cfg.setWdDeleteInfo(cfg_wddelete);
                            }
                            if (SPACESRC_CFG.containsKey("CFG_MSGQCREATE")) { //消息队列创建（内核+分区）
                                JsonObject CFG_MSGQCREATE = SPACESRC_CFG.getJsonObject("CFG_MSGQCREATE");
                                cfg_msgqcreate.setMsgQCtId(Integer.parseInt((String) CFG_MSGQCREATE.getValue(GlobalConsts.MSGQCTID)));
                                spacesrc_cfg.setMsgQCreateInfo(cfg_msgqcreate);
                            }
                            if (SPACESRC_CFG.containsKey("CFG_MSGQDELETE")) { //消息队列删除（内核+分区）
                                JsonObject CFG_MSGQDELETE = SPACESRC_CFG.getJsonObject("CFG_MSGQDELETE");
                                cfg_msgqdelete.setMsgQDtId(Integer.parseInt((String) CFG_MSGQDELETE.getValue(GlobalConsts.MSGQDTID)));
                                spacesrc_cfg.setMsgQDeleteInfo(cfg_msgqdelete);
                            }
                            if (SPACESRC_CFG.containsKey("CFG_MALLOC")) { //malloc函数内存影响（内核+分区）
                                JsonObject CFG_MALLOC = SPACESRC_CFG.getJsonObject("CFG_MALLOC");
                                cfg_malloc.setMallocId(Integer.parseInt((String) CFG_MALLOC.getValue(GlobalConsts.MALLOCID)));
                                spacesrc_cfg.setMallocInfo(cfg_malloc);
                            }
                            if (SPACESRC_CFG.containsKey("CFG_MEMPARTCREATE")) {//memPartCreate函数内存影响（内核+分区）
                                JsonObject CFG_MEMPARTCREATE = SPACESRC_CFG.getJsonObject("CFG_MEMPARTCREATE");
                                cfg_mempartcreate.setMemPCreateId(Integer.parseInt((String) CFG_MEMPARTCREATE.getValue(GlobalConsts.MEMPCREATEID)));
                                spacesrc_cfg.setMemPartCreateInfo(cfg_mempartcreate);
                            }
                            if (SPACESRC_CFG.containsKey("CFG_FREE")) {
                                JsonObject CFG_FREE = SPACESRC_CFG.getJsonObject("CFG_FREE");
                                cfg_free.setFreeId(Integer.parseInt((String) CFG_FREE.getValue(GlobalConsts.FREEID)));
                                spacesrc_cfg.setFreeInfo(cfg_free);

                            }
                            if (SPACESRC_CFG.containsKey("CFG_STACK")) {
                                JsonObject CFG_STACK = SPACESRC_CFG.getJsonObject("CFG_STACK");
                                cfg_stack.setStackId(Integer.parseInt((String) CFG_STACK.getValue(GlobalConsts.STACKID)));
                                spacesrc_cfg.setStackInfo(cfg_stack);
                            }
                            if (SPACESRC_CFG.containsKey("CFG_PDMEM")) {
                                JsonObject CFG_PDMEM = SPACESRC_CFG.getJsonObject("CFG_PDMEM");
                                cfg_pdmem.setPdMemId(Integer.parseInt((String) CFG_PDMEM.getValue(GlobalConsts.PDMEMID)));
                                System.out.println("period:" + CFG_PDMEM.getValue(GlobalConsts.PERIOD));
                                cfg_pdmem.setPeriod(Long.parseLong((String) CFG_PDMEM.getValue(GlobalConsts.PERIOD)));
                                spacesrc_cfg.setPdMemInfo(cfg_pdmem);
                            }
                            if (SPACESRC_CFG.containsKey("CFG_APEXSEMCRE")) {
                                JsonObject CFG_APEXSEMCRE = SPACESRC_CFG.getJsonObject("CFG_APEXSEMCRE");
                                cfg_apexsemcre.setSemaId(Integer.parseInt((String) CFG_APEXSEMCRE.getValue(GlobalConsts.SEMAID)));
                                spacesrc_cfg.setSemaCreInfo(cfg_apexsemcre);
                            }
                            if (SPACESRC_CFG.containsKey("CFG_APEXBLACRE")) {
                                JsonObject CFG_APEXBLACRE = SPACESRC_CFG.getJsonObject("CFG_APEXBLACRE");
                                cfg_apexblacre.setBlackBoardId(Integer.parseInt((String) CFG_APEXBLACRE.getValue(GlobalConsts.BLACKBOARDID)));
                                spacesrc_cfg.setBlackBoardCreInfo(cfg_apexblacre);
                            }
                            if (SPACESRC_CFG.containsKey("CFG_APEXBUFCRE")) {
                                JsonObject CFG_APEXBUFCRE = SPACESRC_CFG.getJsonObject("CFG_APEXBUFCRE");
                                cfg_apexbufcre.setBufferId(Integer.parseInt((String) CFG_APEXBUFCRE.getValue(GlobalConsts.BUFFERID)));
                                spacesrc_cfg.setBufferCreInfo(cfg_apexbufcre);
                            }
                            if (SPACESRC_CFG.containsKey("CFG_APEXPROCCRE")) {
                                JsonObject CFG_APEXPROCCRE = SPACESRC_CFG.getJsonObject("CFG_APEXPROCCRE");
                                cfg_apexproccre.setProcId(Integer.parseInt((String) CFG_APEXPROCCRE.getValue(GlobalConsts.PROCID)));
                                spacesrc_cfg.setProcCreInfo(cfg_apexproccre);
                            }
                            if (SPACESRC_CFG.containsKey("CFG_APEXEVENTCRE")) {
                                JsonObject CFG_APEXEVENTCRE = SPACESRC_CFG.getJsonObject("CFG_APEXEVENTCRE");
                                cfg_apexeventcre.setEventId(Integer.parseInt((String) CFG_APEXEVENTCRE.getValue(GlobalConsts.EVENTID)));
                                spacesrc_cfg.setEventCreInfo(cfg_apexeventcre);
                            }
                            if (SPACESRC_CFG.containsKey("CFG_APEXSPORTCRE")) {
                                JsonObject CFG_APEXSPORTCRE = SPACESRC_CFG.getJsonObject("CFG_APEXSPORTCRE");
                                cfg_apexsportcre.setSPortId(Integer.parseInt((String) CFG_APEXSPORTCRE.getValue(GlobalConsts.SPORTID)));
                                spacesrc_cfg.setSPortCreInfo(cfg_apexsportcre);
                            }
                            if (SPACESRC_CFG.containsKey("CFG_APEXQPORTCRE")) {
                                JsonObject CFG_APEXQPORTCRE = SPACESRC_CFG.getJsonObject("CFG_APEXQPORTCRE");
                                cfg_apexqportcre.setQPortId(Integer.parseInt((String) CFG_APEXQPORTCRE.getValue(GlobalConsts.QPORTID)));
                                spacesrc_cfg.setQPortCreInfo(cfg_apexqportcre);
                            }
                            if (SPACESRC_CFG.containsKey("CFG_PTIMERCREATE")) {
                                JsonObject CFG_PTIMERCREATE = SPACESRC_CFG.getJsonObject("CFG_PTIMERCREATE");
                                cfg_ptimercrete.setPTmCrtId(Integer.parseInt((String) CFG_PTIMERCREATE.getValue(GlobalConsts.PTMCRTID)));
                                spacesrc_cfg.setPTimerCreateInfo(cfg_ptimercrete);
                            }
                            if (SPACESRC_CFG.containsKey("CFG_PTIMERDELETE")) {
                                JsonObject CFG_PTIMERDELETE = SPACESRC_CFG.getJsonObject("CFG_PTIMERDELETE");
                                cfg_ptimerdelete.setPTmDetId(Integer.parseInt((String) CFG_PTIMERDELETE.getValue(GlobalConsts.PTMDETID)));
                                spacesrc_cfg.setPTimerDeleteInfo(cfg_ptimerdelete);
                            }
                            sys_det_cfg.setSpaceSrcCfg(spacesrc_cfg);

                        }
                        if (SYS_DET_CFG.containsKey("SYSEVT_CFG")) {//系统事件
                            JsonObject SYSEVT_CFG = SYS_DET_CFG.getJsonObject("SYSEVT_CFG");
                            if (SYSEVT_CFG.containsKey("CFG_ISR")) {//中断事件
                                JsonObject CFG_ISR = SYSEVT_CFG.getJsonObject("CFG_ISR");
                                System.out.println("ISRID: " + (String) CFG_ISR.getValue(GlobalConsts.ISRID));
                                cfg_isr.setIsrId(Integer.parseInt((String) CFG_ISR.getValue(GlobalConsts.ISRID)));
                                sysevt_cfg.setIsrInfo(cfg_isr);
                            }
                            if (SYSEVT_CFG.containsKey("CFG_EPT")) {//异常事件
                                JsonObject CFG_EPT = SYSEVT_CFG.getJsonObject("CFG_EPT");

                                cfg_ept.setEptId(Integer.parseInt((String) CFG_EPT.getValue(GlobalConsts.EPTID)));///////////////////////////////////////////
                                sysevt_cfg.setEptInfo(cfg_ept);
                            }
                            if (SYSEVT_CFG.containsKey("CFG_EDR")) {//异常事件
                                JsonObject CFG_EDR = SYSEVT_CFG.getJsonObject("CFG_EDR");
                                cfg_edr.setEdrId(Integer.parseInt((String) CFG_EDR.getValue(GlobalConsts.EDRID)));
                                sysevt_cfg.setEdrInfo(cfg_edr);
                            }
                            sys_det_cfg.setSysEvtCfg(sysevt_cfg);


                        }
                        if (SYS_DET_CFG.containsKey("DATACOM_CFG")) {
                            JsonObject DATACOM_CFG = SYS_DET_CFG.getJsonObject("DATACOM_CFG");
                            if (DATACOM_CFG.containsKey("CFG_OSACSEND")) {
                                JsonObject CFG_OSACSEND = DATACOM_CFG.getJsonObject("CFG_OSACSEND");
                                cfg_osac_send.setOSACSndId(Integer.parseInt((String) CFG_OSACSEND.getValue(GlobalConsts.OSACSNDID)));
                                datacom_cfg.setOsacSendInfo(cfg_osac_send);
                            }
                            if (DATACOM_CFG.containsKey("CFG_OSACRECV")) {
                                JsonObject CFG_OSACRECV = DATACOM_CFG.getJsonObject("CFG_OSACRECV");
                                cfg_osac_recv.setOSACRcvId(Integer.parseInt((String) CFG_OSACRECV.getValue(GlobalConsts.OSACRCVID)));
                                datacom_cfg.setOsacRecvInfo(cfg_osac_recv);
                            }
                            if (DATACOM_CFG.containsKey("CFG_MSGQSND")) {
                                JsonObject CFG_MSGQSND = DATACOM_CFG.getJsonObject("CFG_MSGQSND");
                                cfg_msgqsnd.setMsgQSndId(Integer.parseInt((String) CFG_MSGQSND.getValue(GlobalConsts.MSGQSNDID)));
                                datacom_cfg.setMsgQSndInfo(cfg_msgqsnd);
                            }
                            if (DATACOM_CFG.containsKey("CFG_MSGQREC")) {
                                JsonObject CFG_MSGQREC = DATACOM_CFG.getJsonObject("CFG_MSGQREC");
                                cfg_msgqrec.setMsgQRecId(Integer.parseInt((String) CFG_MSGQREC.getValue(GlobalConsts.MSGQRECID)));
                                datacom_cfg.setMsgQRecInfo(cfg_msgqrec);
                            }
                            if (DATACOM_CFG.containsKey("CFG_APEXRDBLA")) {
                                JsonObject CFG_APEXRDBLA = DATACOM_CFG.getJsonObject("CFG_APEXRDBLA");
                                cfg_apexrdbla.setRdBlaId(Integer.parseInt((String) CFG_APEXRDBLA.getValue(GlobalConsts.RDBLAID)));
                                datacom_cfg.setClrBlaInfo(cfg_apexclrbla);
                            }
                            if (DATACOM_CFG.containsKey("CFG_APEXDISBLA")) {
                                JsonObject CFG_APEXDISBLA = DATACOM_CFG.getJsonObject("CFG_APEXDISBLA");
                                cfg_apexdisbla.setDisBlaId(Integer.parseInt((String) CFG_APEXDISBLA.getValue(GlobalConsts.DISBLAID)));
                                datacom_cfg.setDisBlaInfo(cfg_apexdisbla);
                            }
                            if (DATACOM_CFG.containsKey("CFG_APEXSENDBUF")) {
                                JsonObject CFG_APEXSENDBUF = DATACOM_CFG.getJsonObject("CFG_APEXSENDBUF");
                                cfg_apexsendbuf.setSendBufId(Integer.parseInt((String) CFG_APEXSENDBUF.getValue(GlobalConsts.SENDBUFID)));
                                datacom_cfg.setSendBufInfo(cfg_apexsendbuf);
                            }
                            if (DATACOM_CFG.containsKey("CFG_APEXRECVBUF")) {
                                JsonObject CFG_APEXRECVBUF = DATACOM_CFG.getJsonObject("CFG_APEXRECVBUF");
                                cfg_apexrecvbuf.setRecvBufId(Integer.parseInt((String) CFG_APEXRECVBUF.getValue(GlobalConsts.RECVBUFID)));
                                datacom_cfg.setRecvBufInfo(cfg_apexrecvbuf);
                            }
                            if (DATACOM_CFG.containsKey("CFG_APEXSETEVENT")) {
                                JsonObject CFG_APEXSETEVENT = DATACOM_CFG.getJsonObject("CFG_APEXSETEVENT");
                                cfg_apexsetevent.setSetEventId(Integer.parseInt((String) CFG_APEXSETEVENT.getValue(GlobalConsts.SETEVENTID)));
                                datacom_cfg.setSetEventInfo(cfg_apexsetevent);
                            }
                            if (DATACOM_CFG.containsKey("CFG_APEXWAITEVENT")) {
                                JsonObject CFG_APEXWAITEVENT = DATACOM_CFG.getJsonObject("CFG_APEXWAITEVENT");
                                cfg_apexwaitevent.setWaitEventId(Integer.parseInt((String) CFG_APEXWAITEVENT.getValue(GlobalConsts.WAITEVENTID)));
                                datacom_cfg.setWaitEventInfo(cfg_apexwaitevent);
                            }
                            if (DATACOM_CFG.containsKey("CFG_APEXWRSPORT")) {
                                JsonObject CFG_APEXWRSPORT = DATACOM_CFG.getJsonObject("CFG_APEXWRSPORT");
                                cfg_apexwrsport.setWrSportId(Integer.parseInt((String) CFG_APEXWRSPORT.getValue(GlobalConsts.WRSPORTID)));
                                datacom_cfg.setWrSportInfo(cfg_apexwrsport);
                            }
                            if (DATACOM_CFG.containsKey("CFG_APEXRDSPORT")) {
                                JsonObject CFG_APEXRDSPORT = DATACOM_CFG.getJsonObject("CFG_APEXRDSPORT");
                                cfg_apexrdsport.setRdSportId(Integer.parseInt((String) CFG_APEXRDSPORT.getValue(GlobalConsts.RDSPORTID)));
                                datacom_cfg.setRdSportInfo(cfg_apexrdsport);
                            }
                            if (DATACOM_CFG.containsKey("CFG_APEXSENDQPORT")) {
                                JsonObject CFG_APEXSENDQPORT = DATACOM_CFG.getJsonObject("CFG_APEXSENDQPORT");
                                cfg_apexsendqport.setSendQportId(Integer.parseInt((String) CFG_APEXSENDQPORT.getValue(GlobalConsts.SENDQPORTID)));
                                datacom_cfg.setSendQportInfo(cfg_apexsendqport);
                            }
                            if (DATACOM_CFG.containsKey("CFG_APEXRECVQPORT")) {
                                JsonObject CFG_APEXRECVQPORT = DATACOM_CFG.getJsonObject("CFG_APEXRECVQPORT");
                                cfg_apexrecvqport.setRecvQportId(Integer.parseInt((String) CFG_APEXRECVQPORT.getValue(GlobalConsts.RECVQPORTID)));
                                datacom_cfg.setRecvQportInfo(cfg_apexrecvqport);
                            }
                            sys_det_cfg.setDataComCfg(datacom_cfg);
                        }
                        cfg_cmd_st.setSysDetCfg(sys_det_cfg);

                    }
                    if (CFG_CMD_ST.containsKey("RCD_MGR_CFG")) {
                        //记录信息管理模块配置信息
                        //记录信息管理模块配置信息
                        HdpMsg.RCD_MGR_CFG.Builder rcd_mgr_cfg = HdpMsg.RCD_MGR_CFG.newBuilder();
                        HdpMsg.RCD_MGR_CFG_SUB.Builder rcd_mgr_cfg_sub = HdpMsg.RCD_MGR_CFG_SUB.newBuilder();
                        JsonObject RCD_MGR_CFG = CFG_CMD_ST.getJsonObject("RCD_MGR_CFG");
                        if (RCD_MGR_CFG.containsKey("rcdMgrId")) {

                            rcd_mgr_cfg.setRcdMgrId(Integer.parseInt((String) RCD_MGR_CFG.getValue(GlobalConsts.RCDMGRID)));

                        }
                        if (RCD_MGR_CFG.containsKey("RCD_MGR_CFG_SUB")) {
                            JsonObject RCD_MGR_CFG_SUB = RCD_MGR_CFG.getJsonObject("RCD_MGR_CFG_SUB");
                            if (RCD_MGR_CFG_SUB.containsKey("min")) {
                                rcd_mgr_cfg_sub.setMin(Integer.parseInt((String) RCD_MGR_CFG_SUB.getValue(GlobalConsts.MIN)));
                            }
                            if (RCD_MGR_CFG_SUB.containsKey("max")) {
                                rcd_mgr_cfg_sub.setMax(Integer.parseInt((String) RCD_MGR_CFG_SUB.getValue(GlobalConsts.MAX)));
                            }
                            if (RCD_MGR_CFG_SUB.containsKey("buffSize")) {
                                rcd_mgr_cfg_sub.setBuffSize(Integer.parseInt((String) RCD_MGR_CFG_SUB.getValue(GlobalConsts.BUFFSIZE)));
                            }
                            if (RCD_MGR_CFG_SUB.containsKey("mode")) {
                                rcd_mgr_cfg_sub.setMode(Integer.parseInt((String) RCD_MGR_CFG_SUB.getValue(GlobalConsts.MODE)));
                            }
                            if (RCD_MGR_CFG_SUB.containsKey("option")) {
                                rcd_mgr_cfg_sub.setOption(Integer.parseInt((String) RCD_MGR_CFG_SUB.getValue(GlobalConsts.OPTION)));
                            }
                            if (RCD_MGR_CFG_SUB.containsKey("buffMgrPri")) {
                                rcd_mgr_cfg_sub.setBuffMgrPri(Integer.parseInt((String) RCD_MGR_CFG_SUB.getValue(GlobalConsts.BUFFMGRPRI)));
                            }
                            if (RCD_MGR_CFG_SUB.containsKey("buffUpPri")) {
                                rcd_mgr_cfg_sub.setBuffUpPri(Integer.parseInt((String) RCD_MGR_CFG_SUB.getValue(GlobalConsts.BUFFUPPRI)));
                            }
                            rcd_mgr_cfg.setSubInfo(rcd_mgr_cfg_sub);
                        }
                        cfg_cmd_st.setRcdMgrCfg(rcd_mgr_cfg);
                    }
                    if (CFG_CMD_ST.containsKey("PART_CFG")) {
                        //分区采集配置信息
                        HdpMsg.PART_CFG.Builder part_cfg = HdpMsg.PART_CFG.newBuilder();
                        JsonObject PART_CFG = CFG_CMD_ST.getJsonObject("PART_CFG");
                        if (PART_CFG.containsKey("evtActionAddr")) {
                            part_cfg.setEvtActionAddr(Integer.parseInt((String) PART_CFG.getValue(GlobalConsts.EVTACTIONADDR)));
                        }
                        if (PART_CFG.containsKey("wvEvtClassAddr")) {
                            part_cfg.setWvEvtClassAddr(Integer.parseInt((String) PART_CFG.getValue(GlobalConsts.WVEVTCLASSADDR)));
                        }
                        if (PART_CFG.containsKey("wvInstIsOnAddr")) {
                            part_cfg.setWvInstIsOnAddr(Integer.parseInt((String) PART_CFG.getValue(GlobalConsts.WVINSTISONADR)));
                        }
                        if (PART_CFG.containsKey("wvObjIsEnabledAddr")) {
                            part_cfg.setWvObjIsEnabledAddr(Integer.parseInt((String) PART_CFG.getValue(GlobalConsts.WVOBJISENABLEADDR)));
                        }
                        cfg_cmd_st.setPartCfg(part_cfg);
                    }
                    edt_tdr_cfgctr_msg.setCfgCmd(cfg_cmd_st);

                }
                if (EDT_TDR_CFGCTR_MSG.containsKey("CTR_CMD_ST")) {//控制命令格式
                    JsonObject CTR_CMD_ST = EDT_TDR_CFGCTR_MSG.getJsonObject("CTR_CMD_ST");
                    if (CTR_CMD_ST.containsKey("cfgCtrId")) {//配置控制事件ID
                        ctr_cmd_st.setCfgCtrId(Integer.parseInt((String) CTR_CMD_ST.getValue(GlobalConsts.CFGCTRID)));
                    }
                    if (CTR_CMD_ST.containsKey("runFlag")) { //0表示停止，1表示启动
                        ctr_cmd_st.setRunFlag(Integer.parseInt((String) CTR_CMD_ST.getValue(GlobalConsts.RUNFLAG)));
                    }
                    if (CTR_CMD_ST.containsKey("apexFlag")) {//0表示不采集，1表示采集 分区apex信息采集
                        ctr_cmd_st.setApexFlag(Integer.parseInt((String) CTR_CMD_ST.getValue(GlobalConsts.APEXFLAG)));
                    }
                    if (CTR_CMD_ST.containsKey("posixFlag")) { //0表示不采集，1表示采集 分区posix信息采集
                        ctr_cmd_st.setPosixFlag(Integer.parseInt((String) CTR_CMD_ST.getValue(GlobalConsts.POSIXFLAG)));
                    }
                    edt_tdr_cfgctr_msg.setCtrCmd(ctr_cmd_st);
                }
                HdpMsg.EDT_TDR_CFGCTR_MSG edttdrcfgctrmsg = edt_tdr_cfgctr_msg.build();
//            AnalysisCfg.EDT_DATA_ANALYSIS_CFG edtdataanalysiscfg = edt_data_analysis_cfg.build();
                System.out.println(edttdrcfgctrmsg);
//            System.out.println(edtdataanalysiscfg);
//            byte[] udpBuffers=cmd.toByteArray();
                udpPacket.setCurrLength(edttdrcfgctrmsg.toByteString().size()).setTotalLength(edttdrcfgctrmsg.toByteString().size()).setBuffers(edttdrcfgctrmsg.toByteString());
//            udpPacket_analysis.setCurrLength(edtdataanalysiscfg.toByteString().size()).setTotalLength(edtdataanalysiscfg.toByteString().size()).setBuffers(edtdataanalysiscfg.toByteString());

                ByteString test = udpPacket.build().getBuffers();
                System.out.println(udpPacket.build().getBuffers());
//            System.out.println(HdpMsg.EDT_TDR_CFGCTR_MSG.parseFrom(cmd.toByteString()));

                DatagramSocket commandSendsocket = vertx.createDatagramSocket();
                Future sendCommandFuture = Future.future();

//            System.out.println(Buffer.buffer(udpPacket.build().toByteArray()));
                byte[] buffer = udpPacket.build().toByteArray();
//            udpPacket.
                commandSendsocket.send(Buffer.buffer(udpPacket.build().toByteArray()), port, host, asyncResult -> {
                    if (asyncResult.succeeded()) {
                        sendCommandFuture.complete();
                        logger.info("Command send success!");
                    }
                });

                List<Future<JsonObject>> futureList = new ArrayList<>();
                List<JsonObject> list1 = new ArrayList<>();
                sendCommandFuture.setHandler(asyn -> {

                   futureList.add(EDT_TDR_CFGCTR_MSG(targetIP, logger));
                   futureList.get(0).setHandler(r->{
                        response.put("type","success").put("result", futureList.get(0).result());
                        list1.add(response);
                   });


                });

                while (list1.size() != 1){
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
                return list1.get(0);

            }

        }
        return null;
    }


}
