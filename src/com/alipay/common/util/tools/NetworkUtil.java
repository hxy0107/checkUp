package com.alipay.common.util.tools;

/**
 * Created by xianyu.hxy on 2015/5/29.
 */

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.*;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import com.alipay.R;

/**
 * 网络信息
 *
 */
public class NetworkUtil {

    private static final String TAG = "NetworkUtil";
    ListView listShow;
    //创建一个tManager类的实例
   static TelephonyManager tManager;
    //声明一个表示Sim卡状态名的数组
    String []statusName=new String[]{};
    //声明一个表示Sim卡状态值得集合
    static  ArrayList<String> statusValue=new ArrayList<String>();
    /**
     * 判断网络是否可用 <br>
     *
     * @param context
     * @return
     */
    public static boolean haveInternet(Context context) {
        NetworkInfo info = (NetworkInfo) ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE))
                .getActiveNetworkInfo();

        if (info == null || !info.isConnected()) {
            return false;
        }
        if (info.isRoaming()) {
            // here is the roaming option you can change it if you want to
            // disable internet while roaming, just return false
            // 是否在漫游，可根据程序需求更改返回值
            return false;
        }
        return true;
    }

    /**
     * 判断网络是否可用
     * @param context
     * @return
     */
    public static boolean isNetWorkAvilable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager == null) {
            Log.e(TAG, "couldn't get connectivity manager");
        } else {
            NetworkInfo [] networkInfos = connectivityManager.getAllNetworkInfo();
            if(networkInfos != null){
                for (int i = 0, count = networkInfos.length; i < count; i++) {
                    if(networkInfos[i].getState() == NetworkInfo.State.CONNECTED){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * IP地址<br>
     *
     * @return 如果返回null，证明没有网络链接。 如果返回String，就是设备当前使用的IP地址，不管是WiFi还是3G
     */
    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("getLocalIpAddress", ex.toString());
        }
        return null;
    }

    /**
     * 获取MAC地址 <br>
     *
     * @param context
     * @return
     */
    public static String getLocalMacAddress(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }

    /**
     * WIFI 是否可用
     * @param context
     * @return
     */
    public static boolean isWiFiActive(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getTypeName().equals("WIFI") && info[i].isConnected()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 判断MOBILE网络是否可用
     * @param context
     * @return
     */
    public boolean isMobileConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mMobileNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mMobileNetworkInfo != null) {
                return mMobileNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 判断网络连接类型
     * @param context
     * @return
     */
    public static int getConnectedType(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
                return mNetworkInfo.getType();
            }
        }
        return -1;
    }
    /**
     * 获取当前的网络状态 -1：没有网络 1：WIFI网络2：wap网络3：net网络
     * @param context
     * @return
     */
    public static int getAPNType(Context context){
        int netType = -1;
        final int WIFI=1;
        final int CMWAP=2;
        final int CMNET=3;
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if(networkInfo==null){
            return netType;
        }
        int nType = networkInfo.getType();
        if(nType==ConnectivityManager.TYPE_MOBILE){
            if(networkInfo.getExtraInfo().toLowerCase().equals("cmnet")){
                netType = CMNET;
            }
            else{
                netType = CMWAP;
            }
        }
        else if(nType==ConnectivityManager.TYPE_WIFI){
            netType = WIFI;
        }
        return netType;
    }
    public static String getAPNType(Context context,Boolean b){
        String netType = "没有网络连接";
        final String WIFI="当前连接的是WIFI";
        final String CMWAP="当前连接的是CMWAP";
        final String CMNET="当前连接的是CMNET";
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if(networkInfo==null){
            return netType;
        }
        int nType = networkInfo.getType();
        if(nType==ConnectivityManager.TYPE_MOBILE){
            if(networkInfo.getExtraInfo().toLowerCase().equals("cmnet")){
                netType = CMNET;
            }
            else{
                netType = CMWAP;
            }
        }
        else if(nType==ConnectivityManager.TYPE_WIFI){
            netType = WIFI;
        }
        return netType;
    }

    /**
     * 存在多个连接点
     * @param context
     * @return
     */
    public static boolean hasMoreThanOneConnection(Context context){
        ConnectivityManager manager = (ConnectivityManager)context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if(manager==null){
            return false;
        }else{
            NetworkInfo [] info = manager.getAllNetworkInfo();
            int counter = 0;
            for(int i = 0 ;i<info.length;i++){
                if(info[i].isConnected()){
                    counter++;
                }
            }
            if(counter>1){
                return true;
            }
        }

        return false;
    }

    /*
 * HACKISH: These constants aren't yet available in my API level (7), but I need to handle these cases if they come up, on newer versions
 */
    public static final int NETWORK_TYPE_EHRPD=14; // Level 11
    public static final int NETWORK_TYPE_EVDO_B=12; // Level 9
    public static final int NETWORK_TYPE_HSPAP=15; // Level 13
    public static final int NETWORK_TYPE_IDEN=11; // Level 8
    public static final int NETWORK_TYPE_LTE=13; // Level 11

    /**
     * Check if there is any connectivity
     * @param context
     * @return
     */
    public static boolean isConnected(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return (info != null && info.isConnected());
    }
    /**
     * Check if there is fast connectivity
     * @param context
     * @return
     */
    public static boolean isConnectedFast(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return (info != null && info.isConnected() && isConnectionFast(info.getType(),info.getSubtype()));
    }

    /**
     * Check if the connection is fast
     * @param type
     * @param subType
     * @return
     */
    public static boolean isConnectionFast(int type, int subType){
        if(type==ConnectivityManager.TYPE_WIFI){
            System.out.println("CONNECTED VIA WIFI");
            return true;
        }else if(type==ConnectivityManager.TYPE_MOBILE){
            switch(subType){
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                    return false; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_CDMA:
                    return false; // ~ 14-64 kbps
                case TelephonyManager.NETWORK_TYPE_EDGE:
                    return false; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    return true; // ~ 400-1000 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    return true; // ~ 600-1400 kbps
                case TelephonyManager.NETWORK_TYPE_GPRS:
                    return false; // ~ 100 kbps
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                    return true; // ~ 2-14 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPA:
                    return true; // ~ 700-1700 kbps
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                    return true; // ~ 1-23 Mbps
                case TelephonyManager.NETWORK_TYPE_UMTS:
                    return true; // ~ 400-7000 kbps
                // NOT AVAILABLE YET IN API LEVEL 7
                case NETWORK_TYPE_EHRPD:
                    return true; // ~ 1-2 Mbps
                case NETWORK_TYPE_EVDO_B:
                    return true; // ~ 5 Mbps
                case NETWORK_TYPE_HSPAP:
                    return true; // ~ 10-20 Mbps
                case NETWORK_TYPE_IDEN:
                    return false; // ~25 kbps
                case NETWORK_TYPE_LTE:
                    return true; // ~ 10+ Mbps
                // Unknown
                case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                    return false;
                default:
                    return false;
            }
        }else{
            return false;
        }
    }

    /**
     * IP转整型
     * @param ip
     * @return
     */
    public static long ip2int(String ip) {
        String[] items = ip.split("\\.");
        return Long.valueOf(items[0]) << 24
                | Long.valueOf(items[1]) << 16
                | Long.valueOf(items[2]) << 8 | Long.valueOf(items[3]);
    }

    /**
     * 整型转IP
     * @param ipInt
     * @return
     */
    public static String int2ip(long ipInt) {
        StringBuilder sb = new StringBuilder();
        sb.append(ipInt & 0xFF).append(".");
        sb.append((ipInt >> 8) & 0xFF).append(".");
        sb.append((ipInt >> 16) & 0xFF).append(".");
        sb.append((ipInt >> 24) & 0xFF);
        return sb.toString();
    }
    public static String getNetType(Context context)
    {
        ConnectivityManager connectMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo info = connectMgr.getActiveNetworkInfo();
        if(info ==null){return "没有网络连接";}
        if(info.getType() == ConnectivityManager.TYPE_WIFI){return "当前网络连接类型是：WIFI";}
        if(info !=null && info.getType() ==  ConnectivityManager.TYPE_MOBILE)
        {
            int type=info.getSubtype();
            return getNetworkTypeDetail(type);
        }
        return null;

    }
    public static  String getNetworkType(int networkType) {
        // TODO Auto-generated method stub
        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return "1xRTT";
            case TelephonyManager.NETWORK_TYPE_CDMA:
                return "CDMA";
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return "EDGE";
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                return "EHRPD";
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                return "EVDO_0";
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                return "EVDO_A";
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                return "EVDO_B";
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return "GPRS";
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return "HSDPA";
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return "HSPA";
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return "HSPAP";
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return "HSUPA";
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return "IDEN";
            case TelephonyManager.NETWORK_TYPE_LTE:
                return "LTE";
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return "UMTS";
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                return "UNKNOWN";
            default:
                return "UNKNOWN";
        }
    }
    public static  String getNetworkTypeDetail(int networkType) {
        // TODO Auto-generated method stub
        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return "网络类型：1xRTT       2G CDMA2000 1xRTT (RTT - 无线电传输技术) 144kbps 2G的过渡\n( 2G )";
            case TelephonyManager.NETWORK_TYPE_CDMA:
                return "网络类型：CDMA        2G 电信 Code Division Multiple Access 码分多址\n(2G 电信)";
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return "网络类型：EDGE        2G(2.75G) Enhanced Data Rate for GSM Evolution 384kbps\n(2.75G)2.5G到3G的过渡    移动和联通";
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                return "网络类型：EHRPD       3G CDMA2000向LTE 4G的中间产物 Evolved High Rate Packet Data HRPD的升级";
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                return "网络类型：EVDO_0      3G (EVDO 全程 CDMA2000 1xEV-DO) Evolution - Data Only (Data Optimized) 153.6kps - 2.4mbps 属于3G\n( 3G )电信";
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                return "网络类型：EVDO_A      3G 1.8mbps - 3.1mbps 属于3G过渡，3.5G\n(3.5G) 属于3G过渡";
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                return "网络类型：EVDO_B      3G EV-DO Rev.B 14.7Mbps 下行 3.5G";
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return "网络类型：GPRS        2G(2.5) General Packet Radia Service 114kbps\n(2.5G）移动和联通";
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return "网络类型：HSDPA       3.5G 高速下行分组接入 3.5G WCDMA High Speed Downlink Packet Access 14.4mbps";
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return "网络类型：HSPA        3G (分HSDPA,HSUPA) High Speed Packet Access\n( 3G )联通\n(4G)";
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return "网络类型：HSPAP       3G HSPAP\n(3.5G )";
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return "网络类型：HSUPA       3.5G High Speed Uplink Packet Access 高速上行链路分组接入 1.4 - 5.8 mbps\n( 3.5G )";
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return "网络类型：IDEN        2G Integrated Dispatch Enhanced Networks 集成数字增强型网络";
            case TelephonyManager.NETWORK_TYPE_LTE:
                return "网络类型：LTE         4G Long Term Evolution FDD-LTE 和 TDD-LTE , 3G过渡，升级版 LTE ";
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return "网络类型：UMTS        3G WCDMA 联通3G Universal Mobile Telecommunication System 完整的3G移动通信技术标准\n(3G)联通";
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                return "网络类型：UNKNOWN";
            default:
                return "网络类型：UNKNOWN";
        }
    }

}
