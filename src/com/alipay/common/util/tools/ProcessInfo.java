package com.alipay.common.util.tools;

/**
 * Created by xianyu.hxy on 2015/5/29.
 */


public class ProcessInfo {

    private int pid; // 进程ID号
    private int uid; // 用户ID 类似于Linux的权限不同，ID也就不同 比如 root等
    private String processName; // 进程名，默认是包名或者由属性android：process=""指定
    private int memSize; // 获取进程占内存用信息 kb单位

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public int getMemSize() {
        return memSize;
    }

    public void setMemSize(int memSize) {
        this.memSize = memSize;
    }

}
