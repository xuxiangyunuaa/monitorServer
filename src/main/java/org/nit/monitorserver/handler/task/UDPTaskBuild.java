package org.nit.monitorserver.handler.task;

import com.google.protobuf.ByteString;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.datagram.DatagramSocket;
import io.vertx.core.json.JsonObject;
import org.nit.monitorserver.constant.GlobalConsts;
import org.nit.monitorserver.proto.AnalysisCfg;
import org.nit.monitorserver.proto.HdpMsg;
import org.nit.monitorserver.proto.UdpPacket;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 20817
 * @version 1.0
 * @className UDPTaskBuild
 * @description
 * @date 2020/9/5 21:32
 */
public class UDPTaskBuild {
    public static HdpMsg.EDT_TDR_CFGCTR_MSG.Builder communicationTaskParse(JsonObject tdrCfg) {

        HdpMsg.EDT_TDR_CFGCTR_MSG.Builder edt_tdr_cfgctr_msg = HdpMsg.EDT_TDR_CFGCTR_MSG.newBuilder();//整体命令构建
        HdpMsg.GET_CMD_ST.Builder get_cmd_st = HdpMsg.GET_CMD_ST.newBuilder();
        HdpMsg.CFG_CMD_ST.Builder cfg_cmd_st = HdpMsg.CFG_CMD_ST.newBuilder();

//                JsonObject EDT_TDR_CFGCTR_MSG = content.getJsonObject("EDT_TDR_CFGCTR_MSG");
        if (tdrCfg.containsKey("getCmd")) {//获取命令格式

            JsonObject getCMDParams = tdrCfg.getJsonObject("getCmd");
            if (getCMDParams.containsKey("ERR_GET_INFO")) {//故障记录获取配置信息
                HdpMsg.ERR_GET_INFO.Builder err_get_Info = HdpMsg.ERR_GET_INFO.newBuilder();//故障记录获取配置信息
                JsonObject ERR_GET_INFO = getCMDParams.getJsonObject("errorGet");
                err_get_Info.setErrType(Integer.parseInt((String) ERR_GET_INFO.getValue(GlobalConsts.ERRTYPE)));
                if (ERR_GET_INFO.containsKey("head")) {
                    HdpMsg.ERR_HEAD_GET.Builder head = HdpMsg.ERR_HEAD_GET.newBuilder();//获取故障记录信息头部
                    JsonObject ERR_HEAD_GET = ERR_GET_INFO.getJsonObject("head");
                    head.setPartId(Integer.parseInt((String) ERR_HEAD_GET.getValue(GlobalConsts.PART_ID)));//头部信息所在分区
                    err_get_Info.setHead(head);
                }
                if (ERR_GET_INFO.containsKey("entries")) {
                    HdpMsg.ERR_ENTRY_GET.Builder entries = HdpMsg.ERR_ENTRY_GET.newBuilder();//故障记录信息条目
                    JsonObject ERR_ENTRY_GET = ERR_GET_INFO.getJsonObject("entries");
                    entries.setStartEntry(Integer.parseInt((String) ERR_ENTRY_GET.getValue(GlobalConsts.START_ENTRY)));
                    entries.setReqNum(Integer.parseInt((String) ERR_ENTRY_GET.getValue(GlobalConsts.REQ_NUM)));
                    entries.setPartId(Integer.parseInt((String) ERR_ENTRY_GET.getValue(GlobalConsts.PART_ID)));
                    err_get_Info.setEntries(entries);
                }
                get_cmd_st.setErrGetInfo(err_get_Info);
            }
            if (getCMDParams.containsKey("imgGet")) {
                HdpMsg.IMG_GET_INFO.Builder img_getInfo = HdpMsg.IMG_GET_INFO.newBuilder();//获取空间数据信息
                JsonObject IMG_GET_INFO = getCMDParams.getJsonObject("imgGet");
                img_getInfo.setTid(Integer.parseInt((String) IMG_GET_INFO.getValue(GlobalConsts.TID)));
                get_cmd_st.setImgGetInfo(img_getInfo);


            }
            if (getCMDParams.containsKey("timeGet")) {
                HdpMsg.TIMESYN_GET_INFO.Builder timesyn_get_info = HdpMsg.TIMESYN_GET_INFO.newBuilder();//获取同步信息
                JsonObject TIMESYN_GET_INFO = getCMDParams.getJsonObject("timeGet");
                String TIME_TAG = TIMESYN_GET_INFO.getString(GlobalConsts.TIME_TAG).substring(2);
                System.out.println(TIME_TAG);
                timesyn_get_info.setTimeTag(Integer.parseInt(TIME_TAG, 16));
                get_cmd_st.setTimeGetInfo(timesyn_get_info);
            }
            edt_tdr_cfgctr_msg.setGetCmd(get_cmd_st);
        }

        if (tdrCfg.containsKey("cfgCmd")) { //配置命令格式
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
            HdpMsg.CFG_APEXRESETEVENT.Builder cfg_apexResetevent = HdpMsg.CFG_APEXRESETEVENT.newBuilder();//msgQReceive事件

            HdpMsg.CFG_APEXWAITEVENT.Builder cfg_apexwaitevent = HdpMsg.CFG_APEXWAITEVENT.newBuilder();//msgQReceive事件
            HdpMsg.CFG_APEXWRSPORT.Builder cfg_apexwrsport = HdpMsg.CFG_APEXWRSPORT.newBuilder();//msgQReceive事件
            HdpMsg.CFG_APEXRDSPORT.Builder cfg_apexrdsport = HdpMsg.CFG_APEXRDSPORT.newBuilder();//msgQReceive事件
            HdpMsg.CFG_APEXSENDQPORT.Builder cfg_apexsendqport = HdpMsg.CFG_APEXSENDQPORT.newBuilder();//msgQReceive事件
            HdpMsg.CFG_APEXRECVQPORT.Builder cfg_apexrecvqport = HdpMsg.CFG_APEXRECVQPORT.newBuilder();//msgQReceive事件

            JsonObject CFG_CMD_ST = tdrCfg.getJsonObject("cfgCmd");
            if (CFG_CMD_ST.containsKey("sysDet")) {
                JsonObject SYS_DET_CFG = CFG_CMD_ST.getJsonObject("sysDet"); //系统检测事件配置
                if (SYS_DET_CFG.containsKey("timeSrc")) {//时间资源
                    JsonObject TIMESRC_CFG = SYS_DET_CFG.getJsonObject("timeSrc");
                    if (TIMESRC_CFG.containsKey("tskSwtInfo")) {
                        JsonObject CFG_TSKSWT = TIMESRC_CFG.getJsonObject("tskSwtInfo");
                        cfg_tskswt.setTskSwId(Integer.parseInt((String) CFG_TSKSWT.getValue(GlobalConsts.TSKSWID)));
                        timeSrc_cfg.setTskSwtInfo(cfg_tskswt);
                    }
                    if (TIMESRC_CFG.containsKey("partSwInfo")) {
                        JsonObject CFG_PARTSW = TIMESRC_CFG.getJsonObject("partSwInfo");
                        cfg_partsw.setPartSwId(Integer.parseInt((String) CFG_PARTSW.getValue(GlobalConsts.PARTSSWID)));
                        timeSrc_cfg.setPartSwInfo(cfg_partsw);
                    }
                    if (TIMESRC_CFG.containsKey("mfswInfo")) {
                        JsonObject CFG_MFSW = TIMESRC_CFG.getJsonObject("mfswInfo");
                        cfg_mfsw.setMfSwId(Integer.parseInt((String) CFG_MFSW.getValue(GlobalConsts.MFSWID)));
                        timeSrc_cfg.setMfswInfo(cfg_mfsw);
                    }
                    sys_det_cfg.setTimeSrcCfg(timeSrc_cfg);

                }

                if (SYS_DET_CFG.containsKey("spaceSrc")) {//空间资源
                    JsonObject SPACESRC_CFG = SYS_DET_CFG.getJsonObject("spaceSrc");
                    if (SPACESRC_CFG.containsKey("taskSpawnInfo")) {//任务创建（内核+分区）
                        JsonObject CFG_TASKSPAWN = SPACESRC_CFG.getJsonObject("taskSpawnInfo");
                        cfg_taskspawn.setTskSpId(Integer.parseInt((String) CFG_TASKSPAWN.getValue(GlobalConsts.TSKSPID)));
                        spacesrc_cfg.setTaskSpawnInfo(cfg_taskspawn);
                    }
                    if (SPACESRC_CFG.containsKey("taskDeleteInfo")) {   //任务删除（内核+分区）
                        JsonObject CFG_TASKDELETE = SPACESRC_CFG.getJsonObject("taskDeleteInfo");
                        cfg_taskdelete.setTskDtId(Integer.parseInt((String) CFG_TASKDELETE.getValue(GlobalConsts.TSKDTID)));
                        spacesrc_cfg.setTaskDeleteInfo(cfg_taskdelete);
                    }
                    if (SPACESRC_CFG.containsKey("ioCreateInfo")) {   //IO创建（内核）
                        JsonObject CFG_IOCREATE = SPACESRC_CFG.getJsonObject("ioCreateInfo");
                        cfg_iocreate.setIoCtId(Integer.parseInt((String) CFG_IOCREATE.getValue(GlobalConsts.IOCTID)));
                        spacesrc_cfg.setIoCreateInfo(cfg_iocreate);
                    }
                    if (SPACESRC_CFG.containsKey("ioDeleteInfo")) {//IO删除（内核）
                        JsonObject CFG_IODELETE = SPACESRC_CFG.getJsonObject("ioDeleteInfo");
                        cfg_iodelete.setIoDtId(Integer.parseInt((String) CFG_IODELETE.getValue(GlobalConsts.IODTID)));
                        spacesrc_cfg.setIoDeleteInfo(cfg_iodelete);
                    }
                    if (SPACESRC_CFG.containsKey("semCreateInfo")) { //信号量创建（内核+分区）
                        JsonObject CFG_SEMCREATE = SPACESRC_CFG.getJsonObject("semCreateInfo");
                        cfg_semcreate.setSemCtId(Integer.parseInt((String) CFG_SEMCREATE.getValue(GlobalConsts.SEMCTID)));
                        spacesrc_cfg.setSemCreateInfo(cfg_semcreate);
                    }
                    if (SPACESRC_CFG.containsKey("semDeleteInfo")) {  //信号量删除（内核+分区）
                        JsonObject CFG_SEMDELETE = SPACESRC_CFG.getJsonObject("semDeleteInfo");
                        cfg_semdelete.setSemDtId(Integer.parseInt((String) CFG_SEMDELETE.getValue(GlobalConsts.SEMDTID)));
                        spacesrc_cfg.setSemDeleteInfo(cfg_semdelete);
                    }
                    if (SPACESRC_CFG.containsKey("wdCreateInfo")) { //看门狗定时器创建（内核+分区）
                        JsonObject CFG_WDCREATE = SPACESRC_CFG.getJsonObject("wdCreateInfo");
                        cfg_wdcreate.setWdCtId(Integer.parseInt((String) CFG_WDCREATE.getValue(GlobalConsts.WDCTID)));
                        spacesrc_cfg.setWdCreateInfo(cfg_wdcreate);
                    }
                    if (SPACESRC_CFG.containsKey("wdDeleteInfo")) {//看门狗定时器删除（内核+分区）
                        JsonObject CFG_WDDELETE = SPACESRC_CFG.getJsonObject("wdDeleteInfo");
                        cfg_wddelete.setWdDtId(Integer.parseInt((String) CFG_WDDELETE.getValue(GlobalConsts.WDDTID)));
                        spacesrc_cfg.setWdDeleteInfo(cfg_wddelete);
                    }
                    if (SPACESRC_CFG.containsKey("msgQCreateInfo")) { //消息队列创建（内核+分区）
                        JsonObject CFG_MSGQCREATE = SPACESRC_CFG.getJsonObject("msgQCreateInfo");
                        cfg_msgqcreate.setMsgQCtId(Integer.parseInt((String) CFG_MSGQCREATE.getValue(GlobalConsts.MSGQCTID)));
                        spacesrc_cfg.setMsgQCreateInfo(cfg_msgqcreate);
                    }
                    if (SPACESRC_CFG.containsKey("msgQDeleteInfo")) { //消息队列删除（内核+分区）
                        JsonObject CFG_MSGQDELETE = SPACESRC_CFG.getJsonObject("msgQDeleteInfo");
                        cfg_msgqdelete.setMsgQDtId(Integer.parseInt((String) CFG_MSGQDELETE.getValue(GlobalConsts.MSGQDTID)));
                        spacesrc_cfg.setMsgQDeleteInfo(cfg_msgqdelete);
                    }
                    if (SPACESRC_CFG.containsKey("mallocInfo")) { //malloc函数内存影响（内核+分区）
                        JsonObject CFG_MALLOC = SPACESRC_CFG.getJsonObject("mallocInfo");
                        cfg_malloc.setMallocId(Integer.parseInt((String) CFG_MALLOC.getValue(GlobalConsts.MALLOCID)));
                        spacesrc_cfg.setMallocInfo(cfg_malloc);
                    }
                    if (SPACESRC_CFG.containsKey("memPartCreateInfo")) {//memPartCreate函数内存影响（内核+分区）
                        JsonObject CFG_MEMPARTCREATE = SPACESRC_CFG.getJsonObject("memPartCreateInfo");
                        cfg_mempartcreate.setMemPCreateId(Integer.parseInt((String) CFG_MEMPARTCREATE.getValue(GlobalConsts.MEMPCREATEID)));
                        spacesrc_cfg.setMemPartCreateInfo(cfg_mempartcreate);
                    }
                    if (SPACESRC_CFG.containsKey("freeInfo")) {
                        JsonObject CFG_FREE = SPACESRC_CFG.getJsonObject("freeInfo");
                        cfg_free.setFreeId(Integer.parseInt((String) CFG_FREE.getValue(GlobalConsts.FREEID)));
                        spacesrc_cfg.setFreeInfo(cfg_free);

                    }
                    if (SPACESRC_CFG.containsKey("stackInfo")) {
                        JsonObject CFG_STACK = SPACESRC_CFG.getJsonObject("stackInfo");
                        cfg_stack.setStackId(Integer.parseInt((String) CFG_STACK.getValue(GlobalConsts.STACKID)));
                        spacesrc_cfg.setStackInfo(cfg_stack);
                    }
                    if (SPACESRC_CFG.containsKey("pdMemInfo")) {
                        JsonObject CFG_PDMEM = SPACESRC_CFG.getJsonObject("pdMemInfo");
                        cfg_pdmem.setPdMemId(Integer.parseInt((String) CFG_PDMEM.getValue(GlobalConsts.PDMEMID)));
                        System.out.println("period:" + CFG_PDMEM.getValue(GlobalConsts.PERIOD));
                        cfg_pdmem.setPeriod(Long.parseLong((String) CFG_PDMEM.getValue(GlobalConsts.PERIOD)));
                        spacesrc_cfg.setPdMemInfo(cfg_pdmem);
                    }
                    if (SPACESRC_CFG.containsKey("semaCreInfo")) {
                        JsonObject CFG_APEXSEMCRE = SPACESRC_CFG.getJsonObject("semaCreInfo");
                        cfg_apexsemcre.setSemaId(Integer.parseInt((String) CFG_APEXSEMCRE.getValue(GlobalConsts.SEMAID)));
                        spacesrc_cfg.setSemaCreInfo(cfg_apexsemcre);
                    }
                    if (SPACESRC_CFG.containsKey("blackBoardCreInfo")) {
                        JsonObject CFG_APEXBLACRE = SPACESRC_CFG.getJsonObject("blackBoardCreInfo");
                        cfg_apexblacre.setBlackBoardId(Integer.parseInt((String) CFG_APEXBLACRE.getValue(GlobalConsts.BLACKBOARDID)));
                        spacesrc_cfg.setBlackBoardCreInfo(cfg_apexblacre);
                    }
                    if (SPACESRC_CFG.containsKey("bufferCreInfo")) {
                        JsonObject CFG_APEXBUFCRE = SPACESRC_CFG.getJsonObject("bufferCreInfo");
                        cfg_apexbufcre.setBufferId(Integer.parseInt((String) CFG_APEXBUFCRE.getValue(GlobalConsts.BUFFERID)));
                        spacesrc_cfg.setBufferCreInfo(cfg_apexbufcre);
                    }
                    if (SPACESRC_CFG.containsKey("procCreInfo")) {
                        JsonObject CFG_APEXPROCCRE = SPACESRC_CFG.getJsonObject("procCreInfo");
                        cfg_apexproccre.setProcId(Integer.parseInt((String) CFG_APEXPROCCRE.getValue(GlobalConsts.PROCID)));
                        spacesrc_cfg.setProcCreInfo(cfg_apexproccre);
                    }
                    if (SPACESRC_CFG.containsKey("eventCreInfo")) {
                        JsonObject CFG_APEXEVENTCRE = SPACESRC_CFG.getJsonObject("eventCreInfo");
                        cfg_apexeventcre.setEventId(Integer.parseInt((String) CFG_APEXEVENTCRE.getValue(GlobalConsts.EVENTID)));
                        spacesrc_cfg.setEventCreInfo(cfg_apexeventcre);
                    }
                    if (SPACESRC_CFG.containsKey("sPortCreInfo")) {
                        JsonObject CFG_APEXSPORTCRE = SPACESRC_CFG.getJsonObject("sPortCreInfo");
                        cfg_apexsportcre.setSPortId(Integer.parseInt((String) CFG_APEXSPORTCRE.getValue(GlobalConsts.SPORTID)));
                        spacesrc_cfg.setSPortCreInfo(cfg_apexsportcre);
                    }
                    if (SPACESRC_CFG.containsKey("qPortCreInfo")) {
                        JsonObject CFG_APEXQPORTCRE = SPACESRC_CFG.getJsonObject("qPortCreInfo");
                        cfg_apexqportcre.setQPortId(Integer.parseInt((String) CFG_APEXQPORTCRE.getValue(GlobalConsts.QPORTID)));
                        spacesrc_cfg.setQPortCreInfo(cfg_apexqportcre);
                    }
                    if (SPACESRC_CFG.containsKey("pTimerCreateInfo")) {
                        JsonObject CFG_PTIMERCREATE = SPACESRC_CFG.getJsonObject("pTimerCreateInfo");
                        cfg_ptimercrete.setPTmCrtId(Integer.parseInt((String) CFG_PTIMERCREATE.getValue(GlobalConsts.PTMCRTID)));
                        spacesrc_cfg.setPTimerCreateInfo(cfg_ptimercrete);
                    }
                    if (SPACESRC_CFG.containsKey("pTimerDeleteInfo")) {
                        JsonObject CFG_PTIMERDELETE = SPACESRC_CFG.getJsonObject("pTimerDeleteInfo");
                        cfg_ptimerdelete.setPTmDetId(Integer.parseInt((String) CFG_PTIMERDELETE.getValue(GlobalConsts.PTMDETID)));
                        spacesrc_cfg.setPTimerDeleteInfo(cfg_ptimerdelete);
                    }
                    sys_det_cfg.setSpaceSrcCfg(spacesrc_cfg);

                }
                if (SYS_DET_CFG.containsKey("sysEvt")) {//系统事件
                    JsonObject SYSEVT_CFG = SYS_DET_CFG.getJsonObject("sysEvt");
                    if (SYSEVT_CFG.containsKey("isrInfo")) {//中断事件
                        JsonObject CFG_ISR = SYSEVT_CFG.getJsonObject("isrInfo");
                        System.out.println("ISRID: " + (String) CFG_ISR.getValue(GlobalConsts.ISRID));
                        cfg_isr.setIsrId(Integer.parseInt((String) CFG_ISR.getValue(GlobalConsts.ISRID)));
                        sysevt_cfg.setIsrInfo(cfg_isr);
                    }
                    if (SYSEVT_CFG.containsKey("eptInfo")) {//异常事件
                        JsonObject CFG_EPT = SYSEVT_CFG.getJsonObject("eptInfo");

                        cfg_ept.setEptId(Integer.parseInt((String) CFG_EPT.getValue(GlobalConsts.EPTID)));///////////////////////////////////////////
                        sysevt_cfg.setEptInfo(cfg_ept);
                    }
                    if (SYSEVT_CFG.containsKey("edrInfo")) {//异常事件
                        JsonObject CFG_EDR = SYSEVT_CFG.getJsonObject("CFG_EDR");
                        cfg_edr.setEdrId(Integer.parseInt((String) CFG_EDR.getValue(GlobalConsts.EDRID)));
                        sysevt_cfg.setEdrInfo(cfg_edr);
                    }
                    sys_det_cfg.setSysEvtCfg(sysevt_cfg);


                }
                if (SYS_DET_CFG.containsKey("dataCom")) {
                    JsonObject DATACOM_CFG = SYS_DET_CFG.getJsonObject("dataCom");
                    if (DATACOM_CFG.containsKey("osacSendInfo")) {
                        JsonObject CFG_OSACSEND = DATACOM_CFG.getJsonObject("osacSendInfo");
                        cfg_osac_send.setOSACSndId(Integer.parseInt((String) CFG_OSACSEND.getValue(GlobalConsts.OSACSNDID)));
                        datacom_cfg.setOsacSendInfo(cfg_osac_send);
                    }
                    if (DATACOM_CFG.containsKey("osacRecvInfo")) {
                        JsonObject CFG_OSACRECV = DATACOM_CFG.getJsonObject("osacRecvInfo");
                        cfg_osac_recv.setOSACRcvId(Integer.parseInt((String) CFG_OSACRECV.getValue(GlobalConsts.OSACRCVID)));
                        datacom_cfg.setOsacRecvInfo(cfg_osac_recv);
                    }
                    if (DATACOM_CFG.containsKey("msgQSndInfo")) {
                        JsonObject CFG_MSGQSND = DATACOM_CFG.getJsonObject("msgQSndInfo");
                        cfg_msgqsnd.setMsgQSndId(Integer.parseInt((String) CFG_MSGQSND.getValue(GlobalConsts.MSGQSNDID)));
                        datacom_cfg.setMsgQSndInfo(cfg_msgqsnd);
                    }
                    if (DATACOM_CFG.containsKey("msgQRecInfo")) {
                        JsonObject CFG_MSGQREC = DATACOM_CFG.getJsonObject("msgQRecInfo");
                        cfg_msgqrec.setMsgQRecId(Integer.parseInt((String) CFG_MSGQREC.getValue(GlobalConsts.MSGQRECID)));
                        datacom_cfg.setMsgQRecInfo(cfg_msgqrec);
                    }
                    if (DATACOM_CFG.containsKey("rdBlaInfo")) {
                        JsonObject CFG_APEXRDBLA = DATACOM_CFG.getJsonObject("rdBlaInfo");
                        cfg_apexrdbla.setRdBlaId(Integer.parseInt((String) CFG_APEXRDBLA.getValue(GlobalConsts.RDBLAID)));
                        datacom_cfg.setClrBlaInfo(cfg_apexclrbla);
                    }
                    if (DATACOM_CFG.containsKey("clrBlaInfo")) {
                        JsonObject CFG_APEXDISBLA = DATACOM_CFG.getJsonObject("clrBlaInfo");
                        cfg_apexdisbla.setDisBlaId(Integer.parseInt((String) CFG_APEXDISBLA.getValue(GlobalConsts.DISBLAID)));
                        datacom_cfg.setDisBlaInfo(cfg_apexdisbla);
                    }
                    if (DATACOM_CFG.containsKey("sendBufInfo")) {
                        JsonObject CFG_APEXSENDBUF = DATACOM_CFG.getJsonObject("sendBufInfo");
                        cfg_apexsendbuf.setSendBufId(Integer.parseInt((String) CFG_APEXSENDBUF.getValue(GlobalConsts.SENDBUFID)));
                        datacom_cfg.setSendBufInfo(cfg_apexsendbuf);
                    }
                    if (DATACOM_CFG.containsKey("recvBufInfo")) {
                        JsonObject CFG_APEXRECVBUF = DATACOM_CFG.getJsonObject("recvBufInfo");
                        cfg_apexrecvbuf.setRecvBufId(Integer.parseInt((String) CFG_APEXRECVBUF.getValue(GlobalConsts.RECVBUFID)));
                        datacom_cfg.setRecvBufInfo(cfg_apexrecvbuf);
                    }
                    if (DATACOM_CFG.containsKey("setEventInfo")) {
                        JsonObject CFG_APEXSETEVENT = DATACOM_CFG.getJsonObject("setEventInfo");
                        cfg_apexsetevent.setSetEventId(Integer.parseInt((String) CFG_APEXSETEVENT.getValue(GlobalConsts.SETEVENTID)));
                        datacom_cfg.setSetEventInfo(cfg_apexsetevent);
                    }
                    if (DATACOM_CFG.containsKey("resetEventInfo")) {
                        JsonObject CFG_APEXRESETEVENT = DATACOM_CFG.getJsonObject("resetEventInfo");
                        cfg_apexResetevent.setResetEventId(Integer.parseInt((String) CFG_APEXRESETEVENT.getValue(GlobalConsts.RESETEVENTID)));
                        datacom_cfg.setResetEventInfo(cfg_apexResetevent);
                    }

                    if (DATACOM_CFG.containsKey("waitEventInfo")) {
                        JsonObject CFG_APEXWAITEVENT = DATACOM_CFG.getJsonObject("waitEventInfo");
                        cfg_apexwaitevent.setWaitEventId(Integer.parseInt((String) CFG_APEXWAITEVENT.getValue(GlobalConsts.WAITEVENTID)));
                        datacom_cfg.setWaitEventInfo(cfg_apexwaitevent);
                    }
                    if (DATACOM_CFG.containsKey("wrSportInfo")) {
                        JsonObject CFG_APEXWRSPORT = DATACOM_CFG.getJsonObject("wrSportInfo");
                        cfg_apexwrsport.setWrSportId(Integer.parseInt((String) CFG_APEXWRSPORT.getValue(GlobalConsts.WRSPORTID)));
                        datacom_cfg.setWrSportInfo(cfg_apexwrsport);
                    }
                    if (DATACOM_CFG.containsKey("rdSportInfo")) {
                        JsonObject CFG_APEXRDSPORT = DATACOM_CFG.getJsonObject("rdSportInfo");
                        cfg_apexrdsport.setRdSportId(Integer.parseInt((String) CFG_APEXRDSPORT.getValue(GlobalConsts.RDSPORTID)));
                        datacom_cfg.setRdSportInfo(cfg_apexrdsport);
                    }
                    if (DATACOM_CFG.containsKey("sendQportInfo")) {
                        JsonObject CFG_APEXSENDQPORT = DATACOM_CFG.getJsonObject("sendQportInfo");
                        cfg_apexsendqport.setSendQportId(Integer.parseInt((String) CFG_APEXSENDQPORT.getValue(GlobalConsts.SENDQPORTID)));
                        datacom_cfg.setSendQportInfo(cfg_apexsendqport);
                    }
                    if (DATACOM_CFG.containsKey("recvQportInfo")) {
                        JsonObject CFG_APEXRECVQPORT = DATACOM_CFG.getJsonObject("recvQportInfo");
                        cfg_apexrecvqport.setRecvQportId(Integer.parseInt((String) CFG_APEXRECVQPORT.getValue(GlobalConsts.RECVQPORTID)));
                        datacom_cfg.setRecvQportInfo(cfg_apexrecvqport);
                    }
                    sys_det_cfg.setDataComCfg(datacom_cfg);
                }
                cfg_cmd_st.setSysDetCfg(sys_det_cfg);

            }
            if (CFG_CMD_ST.containsKey("rcdMgr")) {
                //记录信息管理模块配置信息
                //记录信息管理模块配置信息
                HdpMsg.RCD_MGR_CFG.Builder rcd_mgr_cfg = HdpMsg.RCD_MGR_CFG.newBuilder();
                HdpMsg.RCD_MGR_CFG_SUB.Builder rcd_mgr_cfg_sub = HdpMsg.RCD_MGR_CFG_SUB.newBuilder();
                JsonObject RCD_MGR_CFG = CFG_CMD_ST.getJsonObject("rcdMgr");
                if (RCD_MGR_CFG.containsKey("rcdMgrId")) {

                    rcd_mgr_cfg.setRcdMgrId(Integer.parseInt((String) RCD_MGR_CFG.getValue(GlobalConsts.RCDMGRID)));

                }
                if (RCD_MGR_CFG.containsKey("subInfo")) {
                    JsonObject RCD_MGR_CFG_SUB = RCD_MGR_CFG.getJsonObject("subInfo");
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
            if (CFG_CMD_ST.containsKey("part")) {
                //分区采集配置信息
                HdpMsg.PART_CFG.Builder part_cfg = HdpMsg.PART_CFG.newBuilder();
                JsonObject PART_CFG = CFG_CMD_ST.getJsonObject("part");
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
//        if (tdrCfg.containsKey("ctr_cmd")) {//控制命令格式
//            JsonObject CTR_CMD_ST = tdrCfg.getJsonObject("ctr_cmd");
//            if (CTR_CMD_ST.containsKey("cfgCtrId")) {//配置控制事件ID
//                ctr_cmd_st.setCfgCtrId(Integer.parseInt((String) CTR_CMD_ST.getValue(GlobalConsts.CFGCTRID)));
//            }
//            if (CTR_CMD_ST.containsKey("runFlag")) { //0表示停止，1表示启动
//                ctr_cmd_st.setRunFlag(Integer.parseInt((String) CTR_CMD_ST.getValue(GlobalConsts.RUNFLAG)));
//            }
//            if (CTR_CMD_ST.containsKey("apexFlag")) {//0表示不采集，1表示采集 分区apex信息采集
//                ctr_cmd_st.setApexFlag(Integer.parseInt((String) CTR_CMD_ST.getValue(GlobalConsts.APEXFLAG)));
//            }
//            if (CTR_CMD_ST.containsKey("posixFlag")) { //0表示不采集，1表示采集 分区posix信息采集
//                ctr_cmd_st.setPosixFlag(Integer.parseInt((String) CTR_CMD_ST.getValue(GlobalConsts.POSIXFLAG)));
//            }
//            edt_tdr_cfgctr_msg.setCtrCmd(ctr_cmd_st);
//        }
        return edt_tdr_cfgctr_msg;

    }

    public static AnalysisCfg.EDT_DATA_ANALYSIS_CFG.Builder analysisTaskPaese(JsonObject anaCfg){




        AnalysisCfg.EDT_DATA_ANALYSIS_CFG.Builder edt_data_analysis_cfg = AnalysisCfg.EDT_DATA_ANALYSIS_CFG.newBuilder();
            edt_data_analysis_cfg.setTargetGuid(anaCfg.getString(GlobalConsts.TARGERGUID));
            if(anaCfg.containsKey("evtMem")){//特定空间操作对内存资源的影响
                AnalysisCfg.EVT_MEM_EFFECT_CFG.Builder evt_mem_effet_cgf =AnalysisCfg.EVT_MEM_EFFECT_CFG.newBuilder();//特定空间操作对内存资源的影响
                JsonObject EVT_MEM_EFFECT_CFG = anaCfg.getJsonObject("evtMem");
                if(EVT_MEM_EFFECT_CFG.containsKey("optId")){//操作编号
                    evt_mem_effet_cgf.setOptId(Integer.parseInt((String)EVT_MEM_EFFECT_CFG.getValue(GlobalConsts.OPTID)));
                }
                if(EVT_MEM_EFFECT_CFG.containsKey("evtId")){//待分析事件的ID
                    evt_mem_effet_cgf.setEvtId(Integer.parseInt((String)EVT_MEM_EFFECT_CFG.getValue(GlobalConsts.EVTID)));
                }
                if(EVT_MEM_EFFECT_CFG.containsKey("pdId")){ //待分析事件所在分区pd
                    evt_mem_effet_cgf.setPdId(Integer.parseInt((String)EVT_MEM_EFFECT_CFG.getValue(GlobalConsts.PDMEMID)));
                }
                if(EVT_MEM_EFFECT_CFG.containsKey("startTime")){//分析的起始时间
                    evt_mem_effet_cgf.setStartTime(Integer.parseInt((String)EVT_MEM_EFFECT_CFG.getValue(GlobalConsts.START_TIME)));
                }
                if(EVT_MEM_EFFECT_CFG.containsKey("endTime")){  //*分析的结束时间
                    evt_mem_effet_cgf.setEndTime(Integer.parseInt((String)EVT_MEM_EFFECT_CFG.getValue(GlobalConsts.END_TIME)));
                }
                edt_data_analysis_cfg.setEvtMemEffectCfg(evt_mem_effet_cgf);
            }
            if(anaCfg.containsKey("tskMem")){//任务及各种空间操作对内存资源的影响
                JsonObject TSK_MEM_EFFECT_CFG = anaCfg.getJsonObject("tskMem");
                AnalysisCfg.TSK_MEM_EFFECT_CFG.Builder tsk_mem_effet_cfg =AnalysisCfg.TSK_MEM_EFFECT_CFG.newBuilder();//任务及各种空间操作对内存资源的影响
                if(TSK_MEM_EFFECT_CFG.containsKey("optId")){
                    tsk_mem_effet_cfg.setOptId(Integer.parseInt((String)TSK_MEM_EFFECT_CFG.getValue(GlobalConsts.OPTID)));
                }
                if(TSK_MEM_EFFECT_CFG.containsKey("pdId")){
                    tsk_mem_effet_cfg.setPdId(Integer.parseInt((String)TSK_MEM_EFFECT_CFG.getValue(GlobalConsts.PDMEMID)));
                }
                if(TSK_MEM_EFFECT_CFG.containsKey("startTime")){
                    tsk_mem_effet_cfg.setStartTime(Integer.parseInt((String)TSK_MEM_EFFECT_CFG.getValue(GlobalConsts.START_TIME)));
                }
                if(TSK_MEM_EFFECT_CFG.containsKey("endTime")){
                    tsk_mem_effet_cfg.setEndTime(Integer.parseInt((String)TSK_MEM_EFFECT_CFG.getValue(GlobalConsts.END_TIME)));
                }
                edt_data_analysis_cfg.setTskMemEffectCfg(tsk_mem_effet_cfg);
            }
            if(anaCfg.containsKey("funcTimeUse")){ //特定函数执行用时
                JsonObject FUNC_TIMEUSE_STATIC = anaCfg.getJsonObject("funcTimeUse");
                AnalysisCfg.FUNC_TIMEUSE_STATIC.Builder func_imeuse_static =AnalysisCfg.FUNC_TIMEUSE_STATIC.newBuilder();//任务及各种空间操作对内存资源的影响
                if(FUNC_TIMEUSE_STATIC.containsKey("optId")){
                    func_imeuse_static.setOptId(Integer.parseInt((String)FUNC_TIMEUSE_STATIC.getValue(GlobalConsts.OPTID)));
                }
                if(FUNC_TIMEUSE_STATIC.containsKey("pdId")){
                    func_imeuse_static.setPdId(Integer.parseInt((String)FUNC_TIMEUSE_STATIC.getValue(GlobalConsts.PDMEMID)));
                }
                if(FUNC_TIMEUSE_STATIC.containsKey("funcId")){
                    func_imeuse_static.setFuncId(Integer.parseInt((String)FUNC_TIMEUSE_STATIC.getValue(GlobalConsts.FUNCID)));
                }
                if(FUNC_TIMEUSE_STATIC.containsKey("startTime")){
                    func_imeuse_static.setStartTime(Integer.parseInt((String)FUNC_TIMEUSE_STATIC.getValue(GlobalConsts.START_TIME)));
                }
                if(FUNC_TIMEUSE_STATIC.containsKey("endTime")){
                    func_imeuse_static.setEndTime(Integer.parseInt((String)FUNC_TIMEUSE_STATIC.getValue(GlobalConsts.END_TIME)));
                }
                edt_data_analysis_cfg.setFuncTimeuseStatic(func_imeuse_static);
            }
            if(anaCfg.containsKey("partRunTime")){//分区运行分析请求
                JsonObject PART_RUNTIME_ANALYSIS_CFG = anaCfg.getJsonObject("partRunTime");
                AnalysisCfg.PART_RUNTIME_ANALYSIS_CFG.Builder part_runtim_analysis_cfg =AnalysisCfg.PART_RUNTIME_ANALYSIS_CFG.newBuilder();//任务及各种空间操作对内存资源的影响
                if(PART_RUNTIME_ANALYSIS_CFG.containsKey("optId")){
                    part_runtim_analysis_cfg.setOptId(Integer.parseInt((String)PART_RUNTIME_ANALYSIS_CFG.getValue(GlobalConsts.OPTID)));
                }
                if(PART_RUNTIME_ANALYSIS_CFG.containsKey("startTime")){
                    part_runtim_analysis_cfg.setStartTime(Integer.parseInt((String)PART_RUNTIME_ANALYSIS_CFG.getValue(GlobalConsts.START_TIME)));
                }
                if(PART_RUNTIME_ANALYSIS_CFG.containsKey("endTime")){
                    part_runtim_analysis_cfg.setEndTime(Integer.parseInt((String)PART_RUNTIME_ANALYSIS_CFG.getValue(GlobalConsts.END_TIME)));
                }
                edt_data_analysis_cfg.setPartRuntimeCfg(part_runtim_analysis_cfg);

            }
            if(anaCfg.containsKey("taskRunTime")){//任务运行分析请求
                JsonObject TASK_RUNTIME_ANALYSIS_CFG = anaCfg.getJsonObject("taskRunTime");
                AnalysisCfg.TASK_RUNTIME_ANALYSIS_CFG.Builder task_runtime_analysis_cfg =AnalysisCfg.TASK_RUNTIME_ANALYSIS_CFG.newBuilder();//任务及各种空间操作对内存资源的影响
                if(TASK_RUNTIME_ANALYSIS_CFG.containsKey("optId")){
                    task_runtime_analysis_cfg.setOptId(Integer.parseInt((String)TASK_RUNTIME_ANALYSIS_CFG.getValue(GlobalConsts.OPTID)));
                }
                if(TASK_RUNTIME_ANALYSIS_CFG.containsKey("startTime")){
                    task_runtime_analysis_cfg.setStartTime(Integer.parseInt((String)TASK_RUNTIME_ANALYSIS_CFG.getValue(GlobalConsts.START_TIME)));
                }
                if(TASK_RUNTIME_ANALYSIS_CFG.containsKey("endTime")){
                    task_runtime_analysis_cfg.setEndTime(Integer.parseInt((String)TASK_RUNTIME_ANALYSIS_CFG.getValue(GlobalConsts.END_TIME)));
                }
                edt_data_analysis_cfg.setTaskRuntimeCfg(task_runtime_analysis_cfg);

            }
            return edt_data_analysis_cfg;


    }







}
