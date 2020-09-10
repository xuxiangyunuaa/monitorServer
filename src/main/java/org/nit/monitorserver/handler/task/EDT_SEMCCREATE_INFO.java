package org.nit.monitorserver.handler.task;

public class EDT_SEMCCREATE_INFO {
    int evtId ;
    int timeStamp;
    int pdId;
    int portId;

    public EDT_SEMCCREATE_INFO(int evtId, int timeStamp, int pdId, int portId) {



        this.evtId = evtId;
        this.timeStamp = timeStamp;
        this.pdId = pdId;
        this.portId = portId;
    }


    public int getEvtId() {
        return evtId;
    }

    public void setEvtId(int evtId) {
        this.evtId = evtId;
    }

    public int getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(int timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getPdId() {
        return pdId;
    }

    public void setPdId(int pdId) {
        this.pdId = pdId;
    }

    public int getPortId() {
        return portId;
    }

    public void setPortId(int portId) {
        this.portId = portId;
    }

    class EDT_TASKSPAWN_T{
        int taskId;
        int priority;
        int stackSize;
        int entryPoint;
        int options;
        int objOwnerId;

        public EDT_TASKSPAWN_T(int taskId, int priority, int stackSize, int entryPoint, int options, int objOwnerId) {

            this.taskId = taskId;
            this.priority = priority;
            this.stackSize = stackSize;
            this.entryPoint = entryPoint;
            this.options = options;
            this.objOwnerId = objOwnerId;
        }

        public int getTaskId() {
            return taskId;
        }

        public void setTaskId(int taskId) {
            this.taskId = taskId;
        }

        public int getPriority() {
            return priority;
        }

        public void setPriority(int priority) {
            this.priority = priority;
        }

        public int getStackSize() {
            return stackSize;
        }

        public void setStackSize(int stackSize) {
            this.stackSize = stackSize;
        }

        public int getEntryPoint() {
            return entryPoint;
        }

        public void setEntryPoint(int entryPoint) {
            this.entryPoint = entryPoint;
        }

        public int getOptions() {
            return options;
        }

        public void setOptions(int options) {
            this.options = options;
        }

        public int getObjOwnerId() {
            return objOwnerId;
        }

        public void setObjOwnerId(int objOwnerId) {
            this.objOwnerId = objOwnerId;
        }
    }
    public EDT_TASKSPAWN_T getEDT_TASKSPAWN_T(){

        int a = ((int) (Math.random() * (100 - 0))) + 0;
        int b = ((int) (Math.random() * (100 - 0))) + 0;
        int c = ((int) (Math.random() * (100 - 0))) + 0;
        int d = ((int) (Math.random() * (100 - 0))) + 0;
        int e = ((int) (Math.random() * (100 - 0))) + 0;
        int f  = ((int) (Math.random() * (100 - 0))) + 0;

        return new EDT_TASKSPAWN_T(a,b,c,d,e,f);
    }



}
