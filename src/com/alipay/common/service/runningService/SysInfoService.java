package com.alipay.common.service.runningService;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import com.alipay.IsRoot;
import com.alipay.common.entity.HttpResponse;
import com.alipay.common.util.tools.L;
import com.alipay.info.AppInfo;
import com.alipay.info.PacInfo;
import com.alipay.info.RunningAppInfo;
import com.alipay.security.Base64;
import com.alipay.store.FileStore;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import javax.net.ssl.*;
import javax.security.cert.CertificateException;
import javax.security.cert.X509Certificate;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xianyu.hxy on 2015/6/8.
 */
public class SysInfoService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        new Thread(new Runnable() {

            @Override
            public void run() {

                Boolean isRoot = false;
                IsRoot isRooted = new IsRoot();
                isRoot = isRooted.isDeviceRooted();
                String sr = isRoot ? "root" : "no root";
                /**
                 * 所有安装的包信息
                 */
                PacInfo pacInfo = new PacInfo();
                ArrayList<AppInfo> appInfos = pacInfo.Get(SysInfoService.this);
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("\n\n" + "Log:" + FileStore.date());
                stringBuffer.append("\n" + sr);
                stringBuffer.append("\n" + "*************Install pack*************" + "\n");
                int i = 0,j=1;
                ArrayList<String> list=new ArrayList<String>();
                for (AppInfo appInfo : appInfos) {
                    String issys = appInfo.isSystemApp() ? ",sysApp" : ",3rdApp";
                    String s = ++i + ".Name:" + appInfo.getAppname() + issys +  ",ver:" + appInfo.getVersionName()
                            + ",md5:" + appInfo.getMD5()
                            + "\n";
                    stringBuffer.append(s);
                    if(j%90==0){
                        list.add(stringBuffer.toString());
                        stringBuffer.setLength(0);
                    }
                    j++;
                }
                stringBuffer.append("*************************************" + "\n");
                list.add(stringBuffer.toString());


                final String uri = "http://localhost:8080/";
                for(String it:list){
                    post(uri,it);
                }



               // Toast.makeText(SysInfoService.this, "上传完成",Toast.LENGTH_LONG).show();
                Intent it=new Intent("org.load.action.MSG");
                it.putExtra("back",true);
                SysInfoService.this.sendBroadcast(it);
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

    public static StringBuffer post(String urlString, String param) {
        StringBuffer sb = new StringBuffer();
        URL url = null;
        HttpURLConnection connection = null;
        DataOutputStream out = null;
        BufferedReader in = null;
        try {
            url = new URL(urlString);

            //关键代码
            //ignore https certificate validation |忽略 https 证书验证
            if (url.getProtocol().toUpperCase().equals("HTTPS")) {
                trustAllHosts();
                HttpsURLConnection https = (HttpsURLConnection) url
                        .openConnection();
                https.setHostnameVerifier(DO_NOT_VERIFY);
                connection = https;
            } else {
                connection = (HttpURLConnection) url.openConnection();
            }

            connection.setReadTimeout(10000);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            connection.connect();
            out = new DataOutputStream(
                    connection.getOutputStream());
            out.writeBytes(param);
            out.flush();

            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()), 512);
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        return sb;
    }
    public static void trustAllHosts() {
        // Create a trust manager that does not validate certificate chains
        // Android use X509 cert
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws java.security.cert.CertificateException {

            }

            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws java.security.cert.CertificateException {

            }

            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[] {};
            }
        } };

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection
                    .setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };
}
