package org.nit.monitorserver.constant;

//import org.omg.CORBA.PUBLIC_MEMBER;

/**
 * 全局常量
 *
 * @author eda
 * @date 2018/7/4
 */

import org.nit.monitorserver.proto.*;

public class GlobalConsts {


    public static final String YZ_HOME = "YZ_HOME";
    public static final String HBASE_ZOOKEEPER_QUORUM = "hbase.zookeeper.quorum";
    public static final String HBASE_CLIENT_PORT = "hbase.zookeeper.property.clientPort";

    public static final String NULL_CHARACTER = "";

    public static final String COMMA = ",";
    public static final String NULL_STR = "NULL";
    public static final String TRUE_STR = "true";
    public static final String FALSE_STR = "false";

    public static final String USERNAME_STR = "username";
    public static final String PASSWORD_STR = "password";
    public static final String TOKEN_STR = "token";




    public static final String SERVER_STR = "server";
    public static final String HTTP_STR = "http";
    public static final String HOST_STR = "host";
    public static final String PORT_STR = "port"; //端口
    public static final String REDIS_STR = "redis";
    public static final String COMMANDPORT_STR = "commandPort";
    public static final String CFGCMDPORT_STR = "cfgcmdPort";
    public static final String DACMDPORT_STR = "dacmdPort";

    public static final String PAGE_STR = "page";
    public static final String ROWS_STR = "rows";


    public static final String ID_STR = "id";
    public static final String CREATED_AT_STR = "createdAt";

    public static final String ID_UNDERLINE = "id";
    public static final String CREATED_AT_UNDERLINE = "created_at";
    public static final String ROLE_UNDERLINE = "role";
    public static final String TASK_INDEX="taskIndex";
    public static final String TASK_DATE="taskDate";
    public static final String START_TIME="startTime"; //采集开始时间
    public static final String END_TIME="endTime"; //采集结束时间
    public static final String PAGE_NUMBER="pageNumber";
    public static final String TASK_TYPE="taskType";
    public static final String PLANE_NAME="planeName";

    //目标机控制常量
    public static final String COMMAND_SET_CONTENT="CommandSetContent";

    //数据通信协议常量
    public static final String FLAG="flag";//是否需要重新发送命令
    public static final String TARGET_IP="targetIP";//目标机ip地址
    public static final String CONTENT="content";//是否需要重新发送命令

    //获取命令格式
    public static final String SWITCH="switch";//是否选择此模块内容
    public static final String ERRTYPE="errType";//故障记录类型
    public static final String PART_ID="partId";//所在分区
    public static final String START_ENTRY="startEntry";//起始条目数
    public static final String REQ_NUM="reqNum";//请求获取的条目数
    public static final String TID="tid";// 获取任务的TCB块的任务Id
    public static final String TIME_TAG="timeTag";// 时间戳获取标志，取值0x74696D65

    //配置命令格式
    //时间资源配置
    public static final String TSKSWID="tskSwId";//任务切换事件ID
    public static final String TSKSPID="tskSpId";//任务创建事件ID
    public static final String TSKDTID="tskDtId";//任务删除事件ID
    public static final String IOCTID="ioCtId";//IO创建事件ID
    public static final String IODTID="ioDtId";//IO删除事件ID
    public static final String SEMCTID="semCtId";//信号量创建事件ID
    public static final String SEMDTID="semDtId";//信号量删除事件ID
    public static final String WDCTID="wdCtId";//看门狗创建事件ID
    public static final String WDDTID="wdDtId";//看门狗删除事件ID
    public static final String MSGQCTID="msgQCtId";//消息队列创建事件ID
    public static final String MSGQDTID="msgQDtId";//消息队列删除事件ID
    public static final String MALLOCID="mallocId";//malloc事件ID
    public static final String MEMPCREATEID="memPCreateId";//memPartCreateId事件ID
    public static final String FREEID="freeId";//free事件ID
    public static final String STACKID="stackId";//stackId事件ID
    public static final String PDMEMID="pdMemId";//周期性内存资源监控事件ID
    public static final String PERIOD="period";//周期性内存资源监控事件采集周期
    public static final String PARTSSWID="partSwId";//时间资源分区切换事件ID
    public static final String MFSWID="mfSwId";//时间资源MF切换major frame切换事件ID
    public static final String SEMAID="semaId";//时间资源MF切换major frame切换事件ID
    public static final String BLACKBOARDID="blackBoardId";//时间资源MF切换major frame切换事件ID
    public static final String BUFFERID="bufferId";//时间资源MF切换major frame切换事件ID
    public static final String PROCID="procId";//时间资源MF切换major frame切换事件ID
    public static final String EVENTID="eventId";//时间资源MF切换major frame切换事件ID
    public static final String SPORTID="sPortId";//时间资源MF切换major frame切换事件ID
    public static final String QPORTID="qPortId";//时间资源MF切换major frame切换事件ID
    public static final String PTMDETID="pTmDetId";//时间资源MF切换major frame切换事件ID
    public static final String PTMCRTID="pTmCrtId";//POSIX_TIMER创建事件ID
    public static final String OSACSNDID="OSACSndId";//POSIX_TIMER创建事件ID
    public static final String OSACRCVID="OSACRcvId";//POSIX_TIMER创建事件ID
    public static final String MSGQSNDID="msgQSndId";//POSIX_TIMER创建事件ID
    public static final String MSGQRECID="msgQRecId";//POSIX_TIMER创建事件ID
    public static final String RDBLAID="rdBlaId";//POSIX_TIMER创建事件ID
    public static final String CLRBLAID="clrBlaId";//POSIX_TIMER创建事件ID
    public static final String DISBLAID="disBlaId";//POSIX_TIMER创建事件ID
    public static final String SENDBUFID="sendBufId";//POSIX_TIMER创建事件ID
    public static final String RECVBUFID="recvBufId";//POSIX_TIMER创建事件ID
    public static final String SETEVENTID="setEventId";//POSIX_TIMER创建事件ID
    public static final String RESETEVENTID="resetEventId";//POSIX_TIMER创建事件ID
    public static final String WAITEVENTID="waitEventId";//POSIX_TIMER创建事件ID
    public static final String WRSPORTID="wrSportId";//POSIX_TIMER创建事件ID
    public static final String RDSPORTID="rdSportId";//POSIX_TIMER创建事件ID
    public static final String SENDQPORTID="sendQportId";//POSIX_TIMER创建事件ID
    public static final String RECVQPORTID="recvQportId";//POSIX_TIMER创建事件ID




    //系统事件配置
    public static final String ISRID="isrId";//中断事件ID
    public static final String EPTID="eptId";//异常事件ID
    public static final String EDRID="edrId";//edr事件ID
    //数据通信
    public static final String OSACID="OSACId";//OSAC事件ID
//    public static final String MSGQSNDID="msgQSndId";//msgQSend事件ID
//    public static final String MSGQRECID="msgQRecId";//msgQReceive事件ID
    //记录信息管理模型配置
    public static final String RCDMGRID="rcdMgrId";//记录信息配置事件ID
    public static final String MIN="min";//最小buffer数量
    public static final String MAX="max";//最大buffer数量
    public static final String BUFFSIZE="buffSize";//每个buffer大小
    public static final String MODE="mode";//缓冲区上传模式
    public static final String OPTION="option";//缓冲区创建选项
    public static final String BUFFMGRPRI="buffMgrPri";//缓冲区管理任务优先级
    public static final String BUFFUPPRI="buffUpPri";//缓冲区上传任务优先级

    //分区采集配置信息
    public static final String EVTACTIONADDR="evtActionAddr";//evtAction地址
    public static final String WVEVTCLASSADDR="wvEvtClassAddr";//wvEvtClass地址
    public static final String WVINSTISONADR="wvInstIsOnAddr";//wvInstIsOn地址
    public static final String WVOBJISENABLEADDR="wvObjIsEnabledAddr";//wvInstIsOn地址



    //控制命令格式
    public static final String CFGCTRID="cfgCtrId";////配置控制事件ID
    public static final String RUNFLAG="runFlag";//0表示停止，1表示启动
    public static final String APEXFLAG="apexFlag";//wvObjIsEnabled地址
    public static final String POSIXFLAG="posixFlag";//wvObjIsEnabled地址

    //分析命令
    public static final String TARGERGUID="targetGuid";
    public static final String OPTID="optId";
    public static final String EVTID="evtId";

    //特定空间操作对内存资源的影响
    public static final String PDID="pdId";//待分析事件所在分区pd

    //特定函数执行用时
    public static final String FUNCID="funcId";  //函数ID，该ID为用户使用时指定






}
