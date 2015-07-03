package com.alipay.common.util.tools;

/**
 * Created by xianyu.hxy on 2015/5/29.
 */
        import java.io.File;
        import java.io.FileOutputStream;
        import java.io.PrintWriter;
        import java.io.StringWriter;
        import java.lang.Thread.UncaughtExceptionHandler;
        import java.lang.reflect.Field;
        import java.net.UnknownHostException;
        import java.text.DateFormat;
        import java.text.SimpleDateFormat;
        import java.util.Date;
        import java.util.HashMap;
        import java.util.Map;

        import android.annotation.SuppressLint;
        import android.content.Context;
        import android.content.pm.PackageInfo;
        import android.content.pm.PackageManager;
        import android.content.pm.PackageManager.NameNotFoundException;
        import android.os.Build;
        import android.os.Environment;
        import android.os.Looper;
        import android.telephony.TelephonyManager;
        import android.text.TextUtils;
        import android.util.Log;
        import android.widget.Toast;

/**
 * ����android���������־<br>
 * UncaughtException������,��������Uncaught�쳣��ʱ��,�и������ӹܳ���.
 *
 *
 *
 */
@SuppressLint("SimpleDateFormat")
public class CrashHandler implements UncaughtExceptionHandler
{

    public static final String TAG = CrashHandler.class.getCanonicalName();

    // ϵͳĬ�ϵ�UncaughtException������
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    // CrashHandlerʵ��
    private static CrashHandler INSTANCE = new CrashHandler();
    // �����Context����
    private Context mContext;
    // �����洢�豸��Ϣ���쳣��Ϣ
    private Map<String, String> infos = new HashMap<String, String>();

    // ���ڸ�ʽ������,��Ϊ��־�ļ�����һ����
    private DateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss");

    /**
     * ��ֻ֤��һ��ʵ��
     */
    private CrashHandler()
    {
    }

    /**
     * ��ȡʵ�� ������ģʽ
     */
    public static CrashHandler getInstance()
    {
        return INSTANCE;
    }

    /**
     * ��ʼ��
     *
     * @param context
     */
    public void init(Context context)
    {
        mContext = context;
        // ��ȡϵͳĬ�ϵ�UncaughtException������
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // ���ø�CrashHandlerΪ�����Ĭ�ϴ�����
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * ��UncaughtException����ʱ��ת��ú���������
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex)
    {
        if (!handleException(ex) && mDefaultHandler != null)
        {
            // ����û�û�д�������ϵͳĬ�ϵ��쳣������������
            mDefaultHandler.uncaughtException(thread, ex);
        }
        else
        {
            try
            {
                Thread.sleep(3000);
            }
            catch (InterruptedException e)
            {
                Log.e(TAG, "error : ", e);
            }

            // �˳�����
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }

    /**
     * �Զ��������,�ռ�������Ϣ ���ʹ��󱨸�Ȳ������ڴ����.
     *
     * @param ex
     * @return true:��������˸��쳣��Ϣ;���򷵻�false.
     */
    private boolean handleException(Throwable ex)
    {
        if (ex == null)
        {
            return false;
        }

        // ʹ��Toast����ʾ�쳣��Ϣ
        new Thread()
        {
            @Override
            public void run()
            {
                Looper.prepare();
                Toast.makeText(mContext, "�ܱ�Ǹ����������쳣��", Toast.LENGTH_LONG)
                        .show();
                Looper.loop();
            }
        }.start();

        // �ռ��豸������Ϣ
        collectDeviceInfo(mContext);

        // ������־�ļ�
        String str = saveCrashInfo2File(ex);
        Log.e(TAG, str);

        return false;
    }

    /**
     * �ռ��豸������Ϣ
     *
     * @param ctx
     */
    public void collectDeviceInfo(Context ctx)
    {
        try
        {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
                    PackageManager.GET_ACTIVITIES);
            if (pi != null)
            {
                String versionName = pi.versionName == null ? "null"
                        : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }

        }
        catch (NameNotFoundException e)
        {
            Log.e(TAG, "an error occured when collect package info", e);
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields)
        {
            try
            {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
                Log.d(TAG, field.getName() + " : " + field.get(null));
            }
            catch (Exception e)
            {
                Log.e(TAG, "an error occured when collect crash info", e);
            }
        }
    }

    /**
     * ���������Ϣ���ļ���
     *
     * @param ex
     * @return �����ļ�����,���ڽ��ļ����͵�������
     */
    private String saveCrashInfo2File(Throwable ex)
    {

        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : infos.entrySet())
        {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append("[" + key + ", " + value + "]\n");
        }

        sb.append("\n" + getStackTraceString(ex));

        try
        {
            String time = formatter.format(new Date());

            TelephonyManager mTelephonyMgr = (TelephonyManager) mContext
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String imei = mTelephonyMgr.getDeviceId();
            if (TextUtils.isEmpty(imei))
            {
                imei = "unknownimei";
            }

            String fileName = "CRS_" + time + "_" + imei + ".txt";

            File sdDir = null;

            if (Environment.getExternalStorageState().equals(
                    android.os.Environment.MEDIA_MOUNTED))
                sdDir = Environment.getExternalStorageDirectory();

            File cacheDir = new File(sdDir + File.separator + "Alipay");
            if (!cacheDir.exists())
                cacheDir.mkdir();

            File filePath = new File(cacheDir + File.separator + fileName);

            FileOutputStream fos = new FileOutputStream(filePath);
            fos.write(sb.toString().getBytes());
            fos.close();

            return fileName;
        }
        catch (Exception e)
        {
            Log.e(TAG, "an error occured while writing file...", e);
        }
        return null;
    }

    /**
     * ��ȡ��׽�����쳣���ַ���
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
}

