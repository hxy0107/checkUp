package com.alipay.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import com.alipay.common.util.tools.NetworkUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by xianyu.hxy on 2015/6/10.
 */
public class NetStatDetail {
    //检查是不是联网 和网络类型
    public static String getNetState(Context context) {
        return NetworkUtil.getNetType(context);
    }

    public static String getNetDetail(Context context) {
        StringBuffer detail = new StringBuffer("***************\n" + "网络详情\n" + "当前网络类型是：" + NetStatDetail.getNetworkClass(context) + "\n");
        ConnectivityManager connectMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo info = connectMgr.getActiveNetworkInfo();
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        detail.append("DeviceSoftwareVersion():" + telephonyManager.getDeviceSoftwareVersion() + "\n")
                .append("DeviceId:" + telephonyManager.getDeviceId() + "\n")
                .append("CellLocation():" + telephonyManager.getCellLocation() + "\n")
                .append(":" + telephonyManager.getNeighboringCellInfo() + "\n")
                .append(":" + telephonyManager.getNetworkOperatorName() + "\n")
                .append(":" + telephonyManager.isNetworkRoaming() + "\n")
                .append(":" + telephonyManager.getNetworkCountryIso() + "\n" + "***************" + "\n" + "SIM卡信息：\n");
        if (telephonyManager.getSimState() != TelephonyManager.SIM_STATE_READY) {
            detail.append("SIM卡没有正确设置" + "\n");
        } else {
            detail.append("SimOperator:" + telephonyManager.getSimOperator() + "\n")
                    .append("SimCountryIso:" + telephonyManager.getSimCountryIso() + "\n")
                    .append("SimSerialNumber:" + telephonyManager.getSimSerialNumber() + "\n")
                    .append("SubscriberId:" + telephonyManager.getSubscriberId() + "\n")
                    .append("CallState:" + telephonyManager.getCallState() + "\n")
                    .append("SimDataState:" + NetStatDetail.getSimDataState(context) + "\n");

        }


        return detail.toString();
    }

    public static String getNetworkClass(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        switch (telephonyManager.getNetworkType()) {

            case 1:
                return "NETWORK_CLASS_2_G";
            case 2:
                return "NETWORK_CLASS_3_G";
            case 3:
                return "NETWORK_CLASS_4_G";
            default:
                return "未知网络";
        }
    }

    public static String getSimDataState(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        switch (telephonyManager.getDataState()) {
            case TelephonyManager.DATA_DISCONNECTED:
                return "DATA_DISCONNECTED";
            case TelephonyManager.DATA_CONNECTED:
                return "DATA_CONNECTED";
            case TelephonyManager.DATA_CONNECTING:
                return "DATA_CONNECTING";
            case TelephonyManager.DATA_SUSPENDED:
                return "DATA_SUSPENDED";
            default:
                return "未知SIM卡数据状态";
        }
    }

    public static Boolean checkNetInternet(Context context) {
        String serverURL = "http://www.baidu.com";
        HttpGet httpRequest = new HttpGet(serverURL);// 建立http get联机
        HttpResponse httpResponse = null;// 发出http请求
        try {
            httpResponse = new DefaultHttpClient().execute(httpRequest);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                return true;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static int getInternelResponseCode(Context context) {
        String serverURL = "http://www.baidu.com";
        HttpGet httpRequest = new HttpGet(serverURL);// 建立http get联机
        HttpResponse httpResponse = null;// 发出http请求

        try {
            httpResponse = new DefaultHttpClient().execute(httpRequest);
            return httpResponse.getStatusLine().getStatusCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }


}
