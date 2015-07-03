package com.alipay.common.service.runningService;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.alipay.net.CommandsHelper;

/**
 * Created by xianyu.hxy on 2015/6/10.
 */
public class LogcatService extends Service {
    @Override
    public void onCreate() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final  boolean retval= CommandsHelper.startCaptureLogcat(LogcatService.this);
            }
        }).start();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
