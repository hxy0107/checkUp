package com.alipay.test;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.IBinder;
import com.alipay.common.util.tools.L;

import java.io.FileDescriptor;
import java.io.PrintWriter;

/**
 * Created by xianyu.hxy on 2015/6/4.
 */
public class IntentService2 extends IntentService {
    public static final String  TAG="IntentService2";
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public IntentService2() {
        super("IntentService_1");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        L.e(TAG, "onHandleIntent");
        L.e(TAG, "IntentService1 id: "+Thread.currentThread().getId()+","+"Threadpriotiry: "+Thread.currentThread().getPriority());
        long t1=System.currentTimeMillis();
        L.e(TAG, "systime:" + t1);
        while(System.currentTimeMillis()-t1<100000){

        }
    }

    @Override
    public void onCreate() {
        L.e(TAG,"onCreate");
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {

        L.e(TAG,"onStart");



        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        L.e(TAG,"onStartCommand");
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
