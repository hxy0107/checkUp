package com.alipay.test;

import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import com.alipay.common.util.tools.L;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by xianyu.hxy on 2015/6/4.
 */
public class startService1 extends Service  {
    static final String TAG="startService1 :";

    public startService1() {
        super();
    }

    @Override
    public void onCreate() {
        L.e(TAG,"onCreate");
        super.onCreate();
    }
    final Handler myHandler=new Handler(){

        @Override
        public void handleMessage(Message msg) {
            if(msg.what==0x123){
                L.e(TAG, "handleMessage");
                L.e(TAG, ""+Thread.currentThread().getId());
            }
        }
    };
    final class MyHandlerThread extends HandlerThread implements Handler.Callback{

        public MyHandlerThread(String name) {
            super(name);
        }


        @Override
        public boolean handleMessage(Message msg) {
            L.e(TAG, "myHandler.handleMessage");
            return false;
        }
    }
    MyHandlerThread myHandlerThread;
    @Override
    public void onStart(Intent intent, int startId) {

        L.e(TAG,"onStart");
        /*
        long t1=System.currentTimeMillis();
        L.e(TAG, "systime:"+t1);
        while(System.currentTimeMillis()-t1<100000){

        }
        */
        myHandlerThread=new MyHandlerThread("myhandlerThread");
        myHandlerThread.start();
        final Handler handlerelse=new Handler(myHandlerThread.getLooper()){

            @Override
            public void handleMessage(Message msg) {
                if(msg.what==0x111) {
                    L.e(TAG, "handlerelse handleMessage");
                }
                super.handleMessage(msg);
            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                handlerelse.sendEmptyMessage(0x111);
            }
        }).start();
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        L.e(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        L.e(TAG, "onConfigurationChanged");
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        L.e(TAG, "onLowMemory");
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        L.e(TAG, "onTrimMemory");
        super.onTrimMemory(level);
    }

    @Override
    public IBinder onBind(Intent intent) {
        L.e(TAG, "onBind");
        L.e(TAG, "Thread:id "+Thread.currentThread().getId());
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        L.e(TAG,"onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        L.e(TAG, "onRebind");
        super.onRebind(intent);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        L.e(TAG, "onTaskRemoved");
        super.onTaskRemoved(rootIntent);
    }

    @Override
    protected void dump(FileDescriptor fd, PrintWriter writer, String[] args) {
        L.e(TAG, "dump");
        super.dump(fd, writer, args);
    }

}
