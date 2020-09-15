package org.nit.monitorserver.constant;


//import com.sun.org.apache.bcel.internal.classfile.Code;
//import sun.applet.resources.MsgAppletViewer;

import javax.lang.model.type.ErrorType;

/**
 * 错误码常量
 *
 * @author sensordb
 * @date 2018/5/21
 */

public enum ResponseError {

    // 0.全局错误

    DECODE_ERROR(1, "请求解析错误"),
    METHOD_NOT_FOUND(2, "方法不存在或者不可见"),
    SERVER_ERROR(3, "服务器内部错误"),
    CLIENT_NOT_LOGIN(4, "客户端未登录"),
    LOGIN_TIMEOUT(5, "用户登录超时"),
    PAGE_FORMAT_ERROR(6, "页码格式非法"),
    CHECK_TYPE_IS_REQUIRED(7, "checkType为必填参数"),
    CHECK_TYPE_FORMAT_ERROR(8, "checkType格式非法"),
    START_TIME_H_IS_REQUIRED(9, "startTime为必填参数"),
    END_TIME_H_IS_REQUIRED(10, "endTime为必填参数"),
    START_TIME_H_FORMAT_ERROR(11, "startTime格式非法"),
    END_TIME_H_FORMAT_ERROR(12, "endTime格式非法"),
    FORMAT_ERROR(13,"存在格式非法的参数"),
    QUERY_FAILURE(14,"记录查询失败"),
    RECORD_NOT_EXISTED(15,"该记录不存在"),
    PARAM_REQUIRED(16, "存在未填的必填参数"),
    RECORD_EXISTED(17,"该记录已经存在"),
    INSERT_FAILURE(18,"记录插入失败"),
    DELETE_FAILURE(19,"记录删除失败"),
    UPDATE_FAILURE(20,"记录更新失败"),
    TASKISEXECUTING(21,"存在采集任务正在运行"),
    TASKNOEXECUTING(22,"当前没有采集任务正在运行"),
    PAGEINDEX(23,"页码为必填参数"),
    PAGESIZE(24,"每页记录数为必填参数"),
    PAGMSIZE_FORMAT_ERROR(25,"每页记录数格式错误"),
    TASKISSTIOP(26,"该采集任务处于停止状态"),

    // 1.用户模块
    USERNAME_IS_REQUIRED(10001, "username为必填参数"),
    PASSWORD_IS_REQUIRED(10002, "password为必填参数"),
    USERNAME_FORMAT_ERROR(10003, "username格式非法"),
    PASSWORD_FORMAT_ERROR(10004, "password格式非法"),
    USERNAME_PASSWORD_ERROR(10005, "用户名或密码错误"),
    USERNAME_EXISTED_ERROR(10006, "该用户已经存在"),
    USER_ID_IS_REQUIRED(10007, "userId为必填参数"),
    USER_ID_FORMAT_ERROR(10008, "userId格式非法"),
    NEW_PASSWORD_IS_REQUIRED(10009, "newPassword为必填参数"),
    NEW_PASSWORD_FORMAT_ERROR(10010, "newPassword格式非法"),


    //采集数据分析模块
    DATATYPE_IS_REQUIRED(10011, "dataType为必填参数"),
    STARTTIME_IS_REQUIRED(10012, "startTime为必填参数"),
    ENDTIME_IS_REQUIRED(10013, "endTime为必填参数"),
    INPUT_QUERY_WITHOUT_RESULT(10014, "查询到 0 条结果"),
    DATATYPE_FORMAT_ERROR(10015, "dataType格式非法"),
    DATATYPE(10016,"dataType为必填参数"),


    //目标机管理模块
    DATA_REQUIRED_ERROR(20000, "数据请求失败。"),
    NAME_IS_REQUIRED(20001, "目标机名称为必填参数。"),
    IP_IS_REQUIRED(20002, "目标机IP地址为必填参数。"),
    NEW_TARGET_ERROR(20003, "请检查IP地址是否已经存在。"),
    UPDATE_TARGET_ERROR(20004, "请检查IP地址是否已经存在。"),
    DELETE_TARGET_ERROR(20005, "删除失败。"),
    DATA_QUERY_ERROR(20006, "数据查询失败。"),
    IP_IS_ILLEGAL(20007, "IP不合法。"),
    COMMANDNAME_IS_REQUIRED(20008, "指令集名称为必填参数。"),
    COMMANDCONTENT_IS_REQUIRED(20009, "指令集内容为必填参数。"),
    NEW_COMMAND_ERROR(20010, "请检查指令集名称或指令集内容是否为空。"),
    COMMAND_SEND_ERROR(20011, "指令发送失败,请检查指令格式！"),
    ID_IS_REQUIRED(20012, "目标机id为必填参数"),
    ID_FORMAT_ERROR(20013,"目标机id格式错误"),
    TARGETMACHINENAME_FORMAT_ERROR(20014,"目标机名称格式错误"),
    TARGETMACHINEOS_FORMAT_ERROR(20015,"目标机OS格式错误"),
    TARGETMACHINEIP_FORMAT_ERROR(20016,"目标机IP格式错误"),

    //数据采集模块
    COMMAND_CONFIG_ERROR(30001,"采集配置参数错误，请检查参数！"),
    DATA_ACQUISITION_ERROR(30002,"数据获取失败！"),
    TARGETIPLIST(30003,"目标机列表为必填参数"),
    TARGETIPLIST_FORMAT_ERROR(30004,"目标机列表格式错误"),

    //ICD模块
    ICDNAME_IS_REQUIRED(40021,"ICD名称为必填参数"),
    ICDID_IS_REQUIRED(20012, "ICD的id为必填参数。"),
    ICD_IS_REQUIRED(40000, "ICD为必填参数。"),
    ICD_FORMAT_ERROR(10004, "ICD格式错误"),
    MESSAGENAME_IS_REQUIRED(40001,"消息名称为必填参数"),
    PORTNAME_IS_REQUIRED(40002,"portName为必填参数"),
    BUS_IS_REQUIRED(40003,"所属总线名称为必填参数"),
    MESSAGELENGTH_IS_REQUIRED(40004,"消息长度为必填参数"),
    MESSAGESOURCE_IS_REQUIRED(40005,"消息的发送端为必填参数"),
    MESSAGESOURCEPORT_IS_REQUIRED(40006,"消息发送端口号为必填参数"),
    MESSAGETARGET(40007,"消息的到达端为必填参数"),
    MESSAGETARGETPORT(40008,"消息的到达端口号为必填参数"),
    SIGNAL(40009,"信号为必填参数"),
    SIGNALNAME(40010,"信号名称为必填参数"),
    SIGNALLENGTH(40011,"信号长度为必填参数"),
    SIGNALSTARTBIT(40012,"信号起始位为必填参数"),
    SIGNALTYPE(40013,"信号类型为必填参数"),
    SIGNALUNIT(40014,"信号单元为必填参数"),
    PARAMETER(40015,"参数为必填参数"),
    PARAMETERNAME(40016,"参数名称为必填参数"),
    PARAMETERLENGTH(40017,"参数长度为必填参数"),
    PARAMETERSTARTBIT(40018,"参数起始位为必填参数"),
    PARAMETERTYPE(40019,"参数类型"),
    PARAMETERUNIT(40020,"参数单位为必填参数"),
    ICDNAME_FORMAT_ERROR(40021,"ICD名称格式错误"),
    DATAID_FORMAT_ERROR(40023,"数据id格式错误"),

    //激励任务管理
    INCENTIVENAME(50001,"激励任务名称为必填参数"),
    INCENTIVEDATA(50002,"激励数据为必填参数"),
    INCENTIVEPORT(50003,"数据据激励虚端口为必填参数"),
    INCENTIVEPORT_FORMAT_ERROR(50004,"数据据激励虚端口格式错误"),
    INCENTIVETARGETIP(50005,"激励的目标机IP为必填参数"),
    INCENTIVETARGETIP_FORMAT_ERROR(50006,"激励的目标机IP格式错误"),
    INCENTIVEID(50007,"激励任务ID为必填参数"),
    RUNFLAG(50008,"运行标识为必填参数"),
    RUNFLAG_FORMAT_ERROR(50008,"运行标识格式错误"),
    PERIOD_FORMAT_ERROR(50009,"周期格式错误"),
    DATAINCENTIVE_FAILURE(50010,"数据激励发送失败"),
    DATA_FORMAT_ERROR(50011,"激励数据格式错误"),
    DATAINCENTIVEISEXECUTING(50012,"存在数据激励任务正在执行"),
    INCENTIVENAME_FORMAT_ERROR(50013,"激励任务名称格式错误"),
    INCENTIVEID_FORMAT_ERROR(50014,"数据激励id格式错误"),

    //采集任务管理
    DEFAULTCHECKED(60000,"采集任务配置参数为必填参数"),
    ERRTYPE(60001,"故障消息类型为必填参数"),
    PARTID(60002,"头部信息所在分区为必填参数"),
    STARTENTRY(60003,"起始条目数为必填参数"),
    REQNUM(60004,"请求获取的条目数为必填参数"),
    TASKID(60005,"采集任务id为必填参数"),
    CFGCTRID(60004,"配置控制事件ID为必填参数"),
    CFGCTRID_FORMAT_ERROR(60005,"配置控制事件ID格式错误"),
    TASKRUNFLAG(60006,"运行标识为必填参数"),
    TASKRUNFLAG_FORMAT_ERROR(60007,"运行标识格式错误"),
    APEXFLAG(60008,"分区apex信息采集为必填参数"),
    APEXFLAG_FORMAT_ERROR(60009,"分区apex信息采集格式错误"),
    POSIXFLAG(60010,"分区posix信息采集为必填参数"),
    POSIXFLAG_FORMAT_ERROR(60011,"分区posix信息采集格式错误"),
    ANAFLAG(60012,"玖翼部分的分析数据为必填参数"),
    ANAFLAG_FORMAT_ERROR(60013,"玖翼部分的分析数据格式错误"),
    CFGCTRIDTDRCFG(60014,"配置控制事件与通信配置不匹配"),
    ANAFLAGANACFG(60015,"配置控制事件与分析配置不匹配"),
    COMMUNICATION_UDP_FAILURE(60016,"UDP发送通信配置失败"),
    ANALYSIS_UDP_FAILURE(60017,"UDP发送分析配置失败"),
    SEARCHDATATYPE(60018,"查看采集任务数据类型格式错误"),
    DEFAULTCHECKED_FORMAT_ERRR(60019,"defaultChecked格式错误"),
    TARGETEVTIDID(60020,"targetIPEvtIDID为必填参数"),
    TASKNAME(60021,"采集任务名称为必填参数"),
    TASKNAME_FORMAT_ERROR(60022,"采集任务名称格式错误"),
    TARGETIP_FORMAT_ERROR(60023,"目标机IP格式错误"),
    ICDID_FORMAT_ERROR(60024,"ICDId格式错误"),
    DEFAULTCHECKED_FORMAT_ERROR(60025,"采集任务配置参数格式错误"),
    TASKID_FORMAT_ERROR(60026,"采集任务ID格式错误"),
    TARGETEVTIDID_FORMAT_ERROR(60027,"targetIPEvtIDID为必填参数"),
    TDRCFG_FORMAT_ERROR(60028,"通信配置格式错误"),
    ANACFG_FORMAT_ERROR(60029,"分析配置格式错误"),
    DEFAULTEVTID(60030,"配置参数的evtId为必填参数"),
    DEFAULTEVTID_FORMAT_ERROR(60031,"配置参数的evtId格式错误"),


    //日志管理
    LEVEL(70000,"日志级别为必填参数"),
    LEVEL_FORMAT_ERROR(70001,"日志级别格式错误"),
    LOGID(70002,"日志名称为必填参数"),
    MODULENAME_FORMAT_ERROR(70003,"模块名称格式错误"),
    LOGID_FORMAT_ERROR(70004,"日志id格式错误"),


    //工程管理
    PROJECTNAME(80000,"工程名称为必填参数"),
    PROJECTID(80001,"工程id为必填参数"),
    EVENTID(80002,"事件ID为必填参数"),
    EVENTID_FORMAT_ERROR(80003,"事件ID为格式错误"),
    EVENTTYPE_FORMAT_ERROR(80004,"事件配置格式错误"),
    FIELD_FORMAT_ERROR(80005,"域格式错误"),
    PDID_FORMAT_ERROR(80006,"分区ID格式错误"),
    STARTTIME_FORMAT_ERROR(80007,"起始时间格式错误"),
    ENDTIME_FORMAT_ERROR(80008,"结束时间格式错误"),
    PORTID_FORMAT_ERROR(80010,"对象ID格式错误"),
    DATAID(80011,"采集数据ID为必填参数"),
    FORMAT_FORMAT_ERROR(80012,"导出格式格式错误"),
    PROJECTNAME_FORMAT_ERROR(80013,"工程名称格式错误"),
    CONTENT_FORMAT_ERROR(80014,"工程内容格式错误"),
    PROJECTID_FORMAT_ERROR(80015,"工程id格式错误");




    private String msg;
    private int code;

    ResponseError(int code, String msg) {
        this.msg = msg;
        this.code = code;
    }

    public String getMsg() {
        return this.msg;
    }

    public int getCode() {
        return this.code;
    }


}
