package com.alipay.common.util.tools;

/**
 * Created by xianyu.hxy on 2015/5/29.
 */
        import java.io.File;
        import java.io.FileWriter;
        import java.io.IOException;
        import java.io.PrintWriter;
        import java.io.StringWriter;
        import java.net.UnknownHostException;
        import java.text.SimpleDateFormat;
        import java.util.Date;

        import android.annotation.SuppressLint;
        import android.os.Build;
        import android.os.Environment;
        import android.util.Log;

/**
 * Android����������־������[֧�ֱ��浽SD��]<br>
 * <br>
 *
 * ��ҪһЩȨ��: <br>
 * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" /> <br>
 * <uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS" /><br>
 *
 * @author PMTOAM
 *
 */
@SuppressLint("SimpleDateFormat")
public class L
{

    public static final String CACHE_DIR_NAME = "Alipay";

    public static boolean isDebugModel = true;// �Ƿ������־
    public static boolean isSaveDebugInfo = true;// �Ƿ񱣴������־
    public static boolean isSaveCrashInfo = true;// �Ƿ񱣴汨����־

    public static void v(final String tag, final String msg)
    {
        if (isDebugModel)
        {
            Log.v(tag, "--> " + msg);
        }
    }

    public static void d(final String tag, final String msg)
    {
        if (isDebugModel)
        {
            Log.d(tag, "--> " + msg);
        }
    }

    public static void i(final String tag, final String msg)
    {
        if (isDebugModel)
        {
            Log.i(tag, "--> " + msg);
        }
    }

    public static void w(final String tag, final String msg)
    {
        if (isDebugModel)
        {
            Log.w(tag, "--> " + msg);
        }
    }

    /**
     * ������־�����ڿ������١�
     * @param tag
     * @param msg
     */
    public static void e(final String tag, final String msg)
    {
        if (isDebugModel)
        {
            Log.e(tag, "--> " + msg);
        }

        if (isSaveDebugInfo)
        {
            new Thread()
            {
                public void run()
                {
                    write(time() + tag + " --> " + msg + "\n");
                };
            }.start();
        }
    }

    /**
     * try catch ʱʹ�ã����߲�Ʒ���ϴ�������
     * @param tag
     * @param tr
     */
    public static void e(final String tag, final Throwable tr)
    {
        if (isSaveCrashInfo)
        {
            new Thread()
            {
                public void run()
                {
                    write(time() + tag + " [CRASH] --> "
                            + getStackTraceString(tr) + "\n");
                };
            }.start();
        }
    }

    /**
     * ��ȡ��׽�����쳣���ַ���
     * @param tr
     * @return
     */
    public static String getStackTraceString(Throwable tr)
    {
        if (tr == null)
        {
            return "";
        }

        Throwable t = tr;
        while (t != null)
        {
            if (t instanceof UnknownHostException)
            {
                return "";
            }
            t = t.getCause();
        }

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        tr.printStackTrace(pw);
        return sw.toString();
    }

    /**
     * ��ʶÿ����־������ʱ��
     * @return
     */
    private static String time()
    {
        return "["
                + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(
                System.currentTimeMillis())) + "] ";
    }

    /**
     * ����������Ϊ��־�ļ�����
     * @return
     */
    private static String date()
    {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date(System
                .currentTimeMillis()));
    }

    /**
     * ���浽��־�ļ�
     * @param content
     */
    public static synchronized void write(String content)
    {
        try
        {
            FileWriter writer = new FileWriter(getFile(), true);
            writer.write(content+"\n");
            writer.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    public static synchronized void write(String content,String path)
    {
        try
        {
            FileWriter writer = new FileWriter(getFile(path), true);
            writer.write(content+"\n");
            writer.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * ��ȡ��־�ļ�·��
     * @return
     */
    public static String getFile()
    {
        File sdDir = null;

        if (Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED))
            sdDir = Environment.getExternalStorageDirectory();

        File cacheDir = new File(sdDir + File.separator + CACHE_DIR_NAME);

        if (!cacheDir.exists())
            cacheDir.mkdir();

        File filePath = new File(cacheDir + File.separator + date() + ".txt");

        return filePath.toString();
    }
    public static String getFile(String Path)
    {
        File sdDir = null;

        if (Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED))
            sdDir = Environment.getExternalStorageDirectory();

        File cacheDir = new File(sdDir + File.separator + CACHE_DIR_NAME+File.separator+Path);

        if (!cacheDir.exists())
            cacheDir.mkdir();

        File filePath = new File(cacheDir + File.separator + date() + ".txt");

        return filePath.toString();
    }

    public static String getOsBuild()
    {
        String phoneInfo = "                     OsBuild\n/***********************************";
        phoneInfo += "\n TIME: " + Build.TIME;
        phoneInfo += "\n PRODUCT: " + Build.PRODUCT;
        phoneInfo += "\n CPU_ABI: " + android.os.Build.CPU_ABI;
        phoneInfo += "\n CPU_ABI2: " + Build.CPU_ABI2;
        phoneInfo += "\n TAGS: " + android.os.Build.TAGS;
        phoneInfo += "\n VERSION_CODES.BASE: " + android.os.Build.VERSION_CODES.BASE;
        phoneInfo += "\n MODEL: " + android.os.Build.MODEL;
        phoneInfo += "\n SDK: " + android.os.Build.VERSION.SDK;
        phoneInfo += "\n VERSION.RELEASE: " + android.os.Build.VERSION.RELEASE;
        phoneInfo += "\n DEVICE: " + android.os.Build.DEVICE;
        phoneInfo += "\n DISPLAY: " + android.os.Build.DISPLAY;
        phoneInfo += "\n BRAND: " + android.os.Build.BRAND;
        phoneInfo += "\n BOARD: " + android.os.Build.BOARD;
        phoneInfo += "\n BOOTLOADER: " + Build.BOOTLOADER;
        phoneInfo += "\n HARDWARE: " + Build.HARDWARE;
        phoneInfo += "\n HOST: " + Build.HOST;
        phoneInfo += "\n RADIO: " + Build.RADIO;
        phoneInfo += "\n SERIAL: " + Build.SERIAL;
        phoneInfo += "\n TYPE: " + Build.TYPE;
        phoneInfo += "\n MANUFACTURE: " + Build.MANUFACTURER;
        phoneInfo += "\n FINGERPRINT: " + android.os.Build.FINGERPRINT;
        phoneInfo += "\n ID: " + android.os.Build.ID;
        phoneInfo += "\n MANUFACTURER: " + android.os.Build.MANUFACTURER;
        phoneInfo += "\n USER: " + android.os.Build.USER;
        phoneInfo += "\n***********************************/";
       return phoneInfo;
    }

}
