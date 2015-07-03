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
 * ������Ϣ
 *
 */
public class NetworkUtil {

    private static final String TAG = "NetworkUtil";
    ListView listShow;
    //����һ��tManager���ʵ��
   static TelephonyManager tManager;
    //����һ����ʾSim��״̬��������
    String []statusName=new String[]{};
    //����һ����ʾSim��״ֵ̬�ü���
    static  ArrayList<String> statusValue=new ArrayList<String>();
    /**
     * �ж������Ƿ���� <br>
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
            // �Ƿ������Σ��ɸ��ݳ���������ķ���ֵ
            return false;
        }
        return true;
    }

    /**
     * �ж������Ƿ����
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
     * IP��ַ<br>
     *
     * @return �������null��֤��û���������ӡ� �������String�������豸��ǰʹ�õ�IP��ַ��������WiFi����3G
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
     * ��ȡMAC��ַ <br>
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
     * WIFI �Ƿ����
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
     * �ж�MOBILE�����Ƿ����
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
     * �ж�������������
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
     * ��ȡ��ǰ������״̬ -1��û������ 1��WIFI����2��wap����3��net����
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
        String netType = "û����������";
        final String WIFI="��ǰ���ӵ���WIFI";
        final String CMWAP="��ǰ���ӵ���CMWAP";
        final String CMNET="��ǰ���ӵ���CMNET";
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
     * ���ڶ�����ӵ�
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
     * IPת����
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
     * ����תIP
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
        if(info ==null){return "û����������";}
        if(info.getType() == ConnectivityManager.TYPE_WIFI){return "��ǰ�������������ǣ�WIFI";}
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
                return "�������ͣ�1xRTT       2G CDMA2000 1xRTT (RTT - ���ߵ紫�似��) 144kbps 2G�Ĺ���\n( 2G )";
            case TelephonyManager.NETWORK_TYPE_CDMA:
                return "�������ͣ�CDMA        2G ���� Code Division Multiple Access ��ֶ�ַ\n(2G ����)";
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return "�������ͣ�EDGE        2G(2.75G) Enhanced Data Rate for GSM Evolution 384kbps\n(2.75G)2.5G��3G�Ĺ���    �ƶ�����ͨ";
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                return "�������ͣ�EHRPD       3G CDMA2000��LTE 4G���м���� Evolved High Rate Packet Data HRPD������";
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                return "�������ͣ�EVDO_0      3G (EVDO ȫ�� CDMA2000 1xEV-DO) Evolution - Data Only (Data Optimized) 153.6kps - 2.4mbps ����3G\n( 3G )����";
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                return "�������ͣ�EVDO_A      3G 1.8mbps - 3.1mbps ����3G���ɣ�3.5G\n(3.5G) ����3G����";
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                return "�������ͣ�EVDO_B      3G EV-DO Rev.B 14.7Mbps ���� 3.5G";
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return "�������ͣ�GPRS        2G(2.5) General Packet Radia Service 114kbps\n(2.5G���ƶ�����ͨ";
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return "�������ͣ�HSDPA       3.5G �������з������ 3.5G WCDMA High Speed Downlink Packet Access 14.4mbps";
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return "�������ͣ�HSPA        3G (��HSDPA,HSUPA) High Speed Packet Access\n( 3G )��ͨ\n(4G)";
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return "�������ͣ�HSPAP       3G HSPAP\n(3.5G )";
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return "�������ͣ�HSUPA       3.5G High Speed Uplink Packet Access ����������·������� 1.4 - 5.8 mbps\n( 3.5G )";
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return "�������ͣ�IDEN        2G Integrated Dispatch Enhanced Networks ����������ǿ������";
            case TelephonyManager.NETWORK_TYPE_LTE:
                return "�������ͣ�LTE         4G Long Term Evolution FDD-LTE �� TDD-LTE , 3G���ɣ������� LTE ";
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return "�������ͣ�UMTS        3G WCDMA ��ͨ3G Universal Mobile Telecommunication System ������3G�ƶ�ͨ�ż�����׼\n(3G)��ͨ";
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                return "�������ͣ�UNKNOWN";
            default:
                return "�������ͣ�UNKNOWN";
        }
    }

}
