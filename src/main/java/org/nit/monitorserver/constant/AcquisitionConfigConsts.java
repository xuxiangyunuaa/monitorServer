package org.nit.monitorserver.constant;

public class AcquisitionConfigConsts {
    public static String NETADDRESS = "39.104.87.140";//数据收集请求服务器地址
    public static int PORT = 9999;//端口

    public static final String TARGETID="targetId"; //目标机ID

    //数据配置协议指令参数
    public static final String INSTRUCTION = "instruction";
    public static final String TIMERANGE = "timeRange";
    public static final String ERRORRCDGET = "errorRcdGet"; //故障记录信息
    public static final String ERRORTYPE = "errorType";
    public static final String LOCATEFLAG = "locateFlag";
    public static final String PARTITION = "partition";
    public static final String STARTENTRY = "startEntry";
    public static final String GETNUM = "getNum";
    public static final String OPTFLAG = "optFlag";

    public static final String SPCDATAGET = "spcDataGet"; //空间数据
    public static final String STARTADDR = "startAddr";
    public static final String MEMSIZE = "memSize";

    public static final String TIMESTAMPGET = "timeStampGet"; //目标机时间戳
    public static final String PLANETIMESTAMPCONFIRM = "planeTimeStampConfirm";
    public static final String LOADTIMESTAMPCONFIRM = "loadTimeStampConfirm";

    public static final String SYSDETECT = "sysDetect"; //系统检测
    public static final String ITEM = "item";
    public static final String SPCSRC_SMPRATE = "spcSrc_smpRate";

    public static final String RECORDMGR = "recordMgr"; //记录信息管理
    public static final String OPERATEMODE = "operateMode";
    public static final String TASKPRI = "taskPri";
    public static final String BUFTYPE = "bufType";
    public static final String BUFSIZE = "bufSize";
    public static final String OVERFLAG = "overFlag";

    //数据分析协议指令参数
    public static final String OBJECTTYPE="type"; //采集数据类型
    public static final String STARTTIME="startTime"; //开始时间
    public static final String ENDTIME="endTime"; //结束时间
    public static final String JOBID="jobId"; //结束时间

    public static final String PARTITIONINFO="partitionInfo";//分区信息
    public static final String SPECIFICOPTTYPE="specificOptType";//特定操作类型

}
