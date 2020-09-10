package org.nit.monitorserver.handler.data;

import com.google.protobuf.InvalidProtocolBufferException;
import com.sun.org.apache.bcel.internal.generic.RETURN;
import io.vertx.core.json.JsonObject;
import netscape.javascript.JSObject;
import org.nit.monitorserver.ICDParse.CollectingICDParse;
import org.nit.monitorserver.handler.task.EDT_SEMCCREATE_INFO;
import org.nit.monitorserver.proto.TdrMsg;

/**
 * @author 20817
 * @version 1.0
 * @className DataParse
 * @description
 * @date 2020/9/6 10:36
 */
public class DataParse {
    public JsonObject communicationDataParse(int eventType,byte[] TdrMsgBuffer,String portName){

        JsonObject result = new JsonObject();
        CollectingICDParse collectingICDParse = new CollectingICDParse();
        try{
            switch (eventType) {
                case 10000:
                    System.out.println("开始执行10000");

                    TdrMsg.EDT_TASKSPAWN_INFO taskspawnResult=TdrMsg.EDT_TASKSPAWN_INFO.parseFrom(TdrMsgBuffer);
                    result.put("evtId",taskspawnResult.getEvtId());
                    result.put("timeStamp",taskspawnResult.getTimeStamp());
                    result.put("pdId",taskspawnResult.getPdId());
                    result.put("portId",taskspawnResult.getPortId());
                    result.put("taskId",taskspawnResult.getTskSpawn().getTaskId());
                    result.put("priority",taskspawnResult.getTskSpawn().getPriority());
                    result.put("stackSize",taskspawnResult.getTskSpawn().getStackSize());
                    result.put("entryPoint",taskspawnResult.getTskSpawn().getEntryPoint());
                    result.put("options",taskspawnResult.getTskSpawn().getOptions());
                    result.put("objOwnerId",taskspawnResult.getTskSpawn().getObjOwnerId());

//                int a = ((int) (Math.random() * (100 - 0))) + 0;
//                int b = ((int) (Math.random() * (100 - 0))) + 0;
//                int c = ((int) (Math.random() * (100 - 0))) + 0;


//                EDT_SEMCCREATE_INFO edt_taskspawn_info = new EDT_SEMCCREATE_INFO(10000, a, b, c);
//                EDT_SEMCCREATE_INFO.EDT_TASKSPAWN_T edt_taskspawn_t = edt_taskspawn_info.getEDT_TASKSPAWN_T();
//
//                result.put("evtId", edt_taskspawn_info.getEvtId());
//                result.put("timeStamp", edt_taskspawn_info.getTimeStamp());
//                result.put("pdId", edt_taskspawn_info.getPdId());
//                result.put("portId", edt_taskspawn_info.getPortId());
//                result.put("taskId", edt_taskspawn_t.getTaskId());
//                result.put("priority", edt_taskspawn_t.getPriority());
//                result.put("stackSize", edt_taskspawn_t.getStackSize());
//                result.put("entryPoint", edt_taskspawn_t.getEntryPoint());
//                result.put("options", edt_taskspawn_t.getOptions());
//                result.put("objOwnerId", edt_taskspawn_t.getObjOwnerId()).put("type", "任务创建");
//                System.out.println("10000中的结果为："+result);



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

        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }

            return result;

    }




}
