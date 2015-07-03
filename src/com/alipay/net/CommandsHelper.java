package com.alipay.net;

/**
 * Created by xianyu.hxy on 2015/6/9.
 */

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.text.TextUtils;

import java.io.*;

public class CommandsHelper {
    private static final String NAME = "tcpdump";
    private static final String TAG = "CommandsHelper";
    public static final String DEST_FILE = Environment.getExternalStorageDirectory() + File.separator+"Alipay"+File.separator+"capture.pcap";
    public static final String DEST_FILE1="/sdcard/Alipay/capture.pcap";
    /*
   public static final Process suProcess1 = getSuProcess();
    public static Process getSuProcess() {
        try {
            return  Runtime.getRuntime().exec("su\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }*/
    public static boolean startCapture(Context context) {
        InputStream is = null;
        OutputStream os = null;
        boolean retVal = false;
        try {
            AssetManager am = context.getAssets();
            is = am.open(NAME);
            File sdcardFile = Environment.getExternalStorageDirectory();
            File dstFile = new File(sdcardFile, NAME);
            os = new FileOutputStream(dstFile);

            copyStream(is, os);

            String[] commands = new String[7];
            commands[0] = "adb shell";
            commands[1] = "su";
            commands[2] = "cp -rf " + dstFile.toString() + " /data/local/tcpdump";
            commands[3] = "rm -r " + dstFile.toString();
            commands[4] = "chmod 777 /data/local/tcpdump";
            commands[5] ="cd /data/local";
            commands[6] = "./tcpdump -p -vv -s 0 -w " + DEST_FILE1;

            execCmd(commands);
        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            closeSafely(is);
            closeSafely(os);
        }

        return retVal;
    }
    public static boolean startCaptureLogcat(Context context) {
        boolean retVal = false;

            String[] commands = new String[2];
            commands[0] = "adb shell";
            commands[1] = "logcat -f  /sdcard/Alipay/logcat.txt";
           // commands[2] = "cp -rf " + dstFile.toString() + " /data/local/tcpdump";
            execCmdNorm(commands, true);

        return retVal;
    }

    public static void stopCapture(Context context) {
        // 找出所有的带有tcpdump的进程
        String[] commands = new String[2];
        commands[0] = "adb shell";
        commands[1] = "ps|grep tcpdump|grep root|awk '{print $2}'";
        Process process = execCmd(commands);
        String result = parseInputStream(process.getInputStream());
        if (!TextUtils.isEmpty(result)) {
            String[] pids = result.split("\n");
            if (null != pids) {
                String[] killCmds = new String[pids.length];
                for (int i = 0; i < pids.length; ++i) {
                    killCmds[i] = "kill -9 " + pids[i];
                }
                execCmd(killCmds);
            }
        }
    }

    public static Process execCmd(String command) {
        return execCmd(new String[] { command }, true);
    }

    public static Process execCmd(String[] commands) {
        return execCmd(commands, true);
    }
//可以传任意linux命令进来 然后执行操作。可以执行/system/bin,/system/xbin/下任意操作 ，实现定时关机等等功能.
    public static Process execCmd(String[] commands, boolean waitFor) {
        Process suProcess = null;
        try {

            suProcess = Runtime.getRuntime().exec("su\n");

            DataOutputStream os = new DataOutputStream(suProcess.getOutputStream());
            for (String cmd : commands) {
                if (!TextUtils.isEmpty(cmd)) {
                    os.writeBytes(cmd + "\n");
                }
            }
            os.flush();
            os.writeBytes("exit\n");
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (waitFor) {
            boolean retval = false;
            try {
                int suProcessRetval = suProcess.waitFor();
                if (255 != suProcessRetval) {
                    retval = true;
                } else {
                    retval = false;
                }
            } catch (Exception ex) {
                //  Log.w("Error ejecutando el comando Root", ex);
            }
        }

        return suProcess;
    }

    public static Process execCmdNorm(String[] commands, boolean waitFor) {
        Process suProcess = null;
        try {
            suProcess = Runtime.getRuntime().exec("su\n");
            DataOutputStream os = new DataOutputStream(suProcess.getOutputStream());
            for (String cmd : commands) {
                if (!TextUtils.isEmpty(cmd)) {
                    os.writeBytes(cmd + "\n");
                }
            }
            os.flush();
            os.writeBytes("exit\n");
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (waitFor) {
            boolean retval = false;
            try {
                int suProcessRetval = suProcess.waitFor();
                if (255 != suProcessRetval) {
                    retval = true;
                } else {
                    retval = false;
                }
            } catch (Exception ex) {
                //  Log.w("Error ejecutando el comando Root", ex);
            }
        }

        return suProcess;
    }
    //可用来文件拷贝
    private static void copyStream(InputStream is, OutputStream os) {
        final int BUFFER_SIZE = 1024;
        try {
            byte[] bytes = new byte[BUFFER_SIZE];
            for (;;) {
                int count = is.read(bytes, 0, BUFFER_SIZE);
                if (count == -1) {
                    break;
                }

                os.write(bytes, 0, count);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void closeSafely(Closeable is) {
        try {
            if (null != is) {
                is.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String parseInputStream(InputStream is) {
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String line = null;
        StringBuilder sb = new StringBuilder();
        try {
            while ( (line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }
}
