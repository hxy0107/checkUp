package com.alipay.common.service.runningService;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.alipay.common.util.tools.NetworkUtil;

/**
 * Created by xianyu.hxy on 2015/6/9.
 */
public class NetStatService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                NetworkUtil.getNetType(NetStatService.this);

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
