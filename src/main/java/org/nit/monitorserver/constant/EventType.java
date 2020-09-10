package org.nit.monitorserver.constant;

public enum EventType {


    TASKSPAWN("10000","EDT_TASKSPAWN_INFO"),//任务创建
    ;



    private String msg;
    private String EvtID;

    EventType(String EvtID,String msg) {
        this.msg = msg;
        this.EvtID = EvtID;
    }

    public String getMsg() {
        return this.msg;
    }

    public String getEvtID() {
        return this.EvtID;
    }

}
