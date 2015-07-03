package com.alipay.common.service.runningService;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.alipay.IsRoot;
import com.alipay.net.CommandsHelper;

import java.io.File;
import java.io.IOException;

/**
 * Created by xianyu.hxy on 2015/6/9.
 */
public class CaptureService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(IsRoot.isDeviceRooted()) {
                    final boolean retval = CommandsHelper.startCapture(CaptureService.this);
                }else {
                    try {
                        Runtime.getRuntime().exec("logcat -f  /sdcard/Alipay/logcat.txt\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                File capFile=new File(CommandsHelper.DEST_FILE);
                if(capFile.exists()){
                    if(capFile.length()>5000000){}

                }

            }
        }).start();
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
