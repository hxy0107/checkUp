package com.alipay.common.util.tools;

/**
 * Created by xianyu.hxy on 2015/5/29.
 */


public class ProcessInfo {

    private int pid; // ����ID��
    private int uid; // �û�ID ������Linux��Ȩ�޲�ͬ��IDҲ�Ͳ�ͬ ���� root��
    private String processName; // ��������Ĭ���ǰ�������������android��process=""ָ��
    private int memSize; // ��ȡ����ռ�ڴ�����Ϣ kb��λ

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
