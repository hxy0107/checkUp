package com.alipay.store;

import android.os.Environment;
import android.util.Base64;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by xianyu.hxy on 2015/6/8.
 */
public class FileStore {
    public static final String CACHE_DIR_NAME = "Alipay";
    /**
     * 以年月日作为日志文件名称
     * @return
     */
    public static String date()
    {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System
                .currentTimeMillis()));
    }

    /**
     * 保存到日志文件
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
    public static synchronized void writeBase64(String content)
    {
        try
        {
            FileWriter writer = new FileWriter(getFile(), true);
            String base64=new String(Base64.encode(content.getBytes(),Base64.DEFAULT ));
            writer.write(base64+"\n");
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
    public static synchronized void writeBase64(String content,String path)
    {
        try
        {
            FileWriter writer = new FileWriter(getFile(path), true);
            String base64=new String(Base64.encode(content.getBytes(),Base64.DEFAULT ));
            writer.write(base64+"\n");
            writer.write(content+"\n");
            writer.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 获取日志文件路径 默认以时间为文件名
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
    // path :/storage/emulated/0/Alipay/log.txt
    public static String getLogFile()
    {
        File sdDir = null;

        if (Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED))
            sdDir = Environment.getExternalStorageDirectory();

        File cacheDir = new File(sdDir + File.separator + CACHE_DIR_NAME);

        if (!cacheDir.exists())
            cacheDir.mkdir();

        File filePath = new File(cacheDir + File.separator  + "log.txt");


        return filePath.toString();
    }

    public static File getLogFile(Boolean b)
    {
        File sdDir = null;

        if (Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED))
            sdDir = Environment.getExternalStorageDirectory();

        File cacheDir = new File(sdDir + File.separator + CACHE_DIR_NAME);

        if (!cacheDir.exists())
            cacheDir.mkdir();

        File filePath = new File(cacheDir + File.separator  + "log.txt", String.valueOf(true));


        return filePath;
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

}
