package com.alipay.update;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Created by xianyu.hxy on 2015/6/11.
 */
public class AppVersion {
    /**
     * 获取软件版本号
     *
     * @param context
     * @return
     */
    private int getVersionCode(Context context)
    {
        int versionCode = 0;
        try
        {
            // 获取软件版本号，
            versionCode = context.getPackageManager().getPackageInfo("com.alipay", 0).versionCode;
        } catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }
        return versionCode;
    }
}
