package com.alipay.net;

/**
 * Created by xianyu.hxy on 2015/6/10.
 */
public class CmdStrings {
    //ʵ�ֹػ�����
  public static String[] getCmdShutdown(){
       String[] commands = new String[7];
       commands[0] = "adb shell";
       commands[1] = "su";
       commands[2] = "cd /system/bin";
       commands[3] = "./reboot";
       return commands;
   }
    //kill ������
    public static String[] stopCmd(String cmd)
    {
        String[] commands = new String[2];
        commands[0] = "adb shell";
        commands[1] = "busybox pkill -SIGINT "+cmd;
        return commands;
    }

}
