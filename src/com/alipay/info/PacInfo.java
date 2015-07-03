package com.alipay.info;


import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import com.alipay.common.service.runningService.SysInfoService;
import com.alipay.security.MD5;

import java.io.ByteArrayInputStream;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.*;

/**
 * Created by xianyu.hxy on 2015/6/8.
 */
public class PacInfo {
    private PackageManager pm;
    //所有应用
    public ArrayList<AppInfo> Get(Context context){
        ArrayList<AppInfo> appList = new ArrayList<AppInfo>();
        ArrayList<AppInfo> appList3rd=new ArrayList<AppInfo>();
        List<PackageInfo> packageInfos=context.getPackageManager().getInstalledPackages(PackageManager.GET_SIGNATURES);
        for (int i = 0; i < packageInfos.size(); i++) {
            PackageInfo pInfo=packageInfos.get(i);
            AppInfo allAppInfo=new AppInfo();
            allAppInfo.setAppname(pInfo.applicationInfo.loadLabel(context.getPackageManager()).toString());//应用程序的名称
            allAppInfo.setPackagename(pInfo.packageName);//应用程序的包
            allAppInfo.setVersionCode(pInfo.versionCode);//版本号
            allAppInfo.setLastInstal(pInfo.firstInstallTime);
            allAppInfo.setVersionName(pInfo.versionName);
            //签名


            /*
           android.content.pm.Signature[] signatures= pInfo.signatures;
            StringBuilder builder=new StringBuilder();
            for(android.content.pm.Signature signature:signatures){
                builder.append(signature.toCharsString());
            }
            byte[] bytes=builder.toString().getBytes();
            allAppInfo.setSign(builder.toString());
            allAppInfo.setPublicKey(PacInfo.getPublicKey(bytes));
            */

            android.content.pm.Signature signature=pInfo.signatures[0];
            allAppInfo.setSign(signature.toString());
            allAppInfo.setPublicKey(getPublicKey(signature.toByteArray()));
            try {
               String md5= MD5.getMD5(allAppInfo.getPublicKey());
                allAppInfo.setMD5(md5);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

            //allAppInfo.setProvider(pInfo.providers);
            allAppInfo.setInstalPath(pInfo.applicationInfo.sourceDir);
            allAppInfo.setAppicon(pInfo.applicationInfo.loadIcon(context.getPackageManager()));
            if((pInfo.applicationInfo.flags&ApplicationInfo.FLAG_SYSTEM)==0){
                allAppInfo.setIsSystemApp(false);
                appList3rd.add(allAppInfo);
            }else {
                allAppInfo.setIsSystemApp(true);
                appList.add(allAppInfo);
            }

        }
        Log.e("test",appList.get(2).getAppname());
        appList3rd.addAll(appList);
        return appList3rd;
    }
    //三方应用
    public ArrayList<AppInfo> GetOnly3rd(Context context){
        ArrayList<AppInfo> appList = new ArrayList<AppInfo>();
        List<PackageInfo> packageInfos=context.getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packageInfos.size(); i++) {
            PackageInfo pInfo=packageInfos.get(i);
            AppInfo allAppInfo=new AppInfo();
            allAppInfo.setAppname(pInfo.applicationInfo.loadLabel(context.getPackageManager()).toString());//应用程序的名称
            allAppInfo.setPackagename(pInfo.packageName);//应用程序的包
            allAppInfo.setVersionCode(pInfo.versionCode);//版本号
            allAppInfo.setLastInstal(pInfo.firstInstallTime);
            //allAppInfo.setProvider(pInfo.providers);
            allAppInfo.setInstalPath(pInfo.applicationInfo.sourceDir);
            allAppInfo.setAppicon(pInfo.applicationInfo.loadIcon(context.getPackageManager()));
            if((pInfo.applicationInfo.flags&ApplicationInfo.FLAG_SYSTEM)==0){
                appList.add(allAppInfo);//如果非系统应用，则添加至appList
            }

        }
        return appList;
    }
    //所有系统应用
    public ArrayList<AppInfo> GetOnlysys(Context context){
        ArrayList<AppInfo> appList = new ArrayList<AppInfo>();
        List<PackageInfo> packageInfos=context.getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packageInfos.size(); i++) {
            PackageInfo pInfo=packageInfos.get(i);
            AppInfo allAppInfo=new AppInfo();
            allAppInfo.setAppname(pInfo.applicationInfo.loadLabel(context.getPackageManager()).toString());//应用程序的名称
            allAppInfo.setPackagename(pInfo.packageName);//应用程序的包
            allAppInfo.setVersionCode(pInfo.versionCode);//版本号
            allAppInfo.setLastInstal(pInfo.firstInstallTime);
            //allAppInfo.setProvider(pInfo.providers);
            allAppInfo.setInstalPath(pInfo.applicationInfo.sourceDir);
            allAppInfo.setAppicon(pInfo.applicationInfo.loadIcon(context.getPackageManager()));
            if(!((pInfo.applicationInfo.flags&ApplicationInfo.FLAG_SYSTEM)==0)){
                appList.add(allAppInfo);//如果非系统应用，则添加至appList
            }

        }
        return appList;
    }
    public boolean filterApp(ApplicationInfo info) {
        if ((info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
        // 代表的是系统的应用,但是被用户升级了. 用户应用
            return true;
        } else if ((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
            // 代表的用户的应用
            return true;
        }
        return false;
    }


    public List<RunningAppInfo> queryAllRunningAppInfo(Context context) {
        pm = context.getPackageManager();
        // 查询所有已经安装的应用程序
        List<ApplicationInfo> listAppcations = pm.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
        Collections.sort(listAppcations, new ApplicationInfo.DisplayNameComparator(pm));// 排序

        // 保存所有正在运行的包名 以及它所在的进程信息
        Map<String, ActivityManager.RunningAppProcessInfo> pgkProcessAppMap = new HashMap<String, ActivityManager.RunningAppProcessInfo>();

        ActivityManager mActivityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        // 通过调用ActivityManager的getRunningAppProcesses()方法获得系统里所有正在运行的进程
        List<ActivityManager.RunningAppProcessInfo> appProcessList = mActivityManager
                .getRunningAppProcesses();

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcessList) {
            int pid = appProcess.pid; // pid
            String processName = appProcess.processName; // 进程名

            String[] pkgNameList = appProcess.pkgList; // 获得运行在该进程里的所有应用程序包

            // 输出所有应用程序的包名
            for (int i = 0; i < pkgNameList.length; i++) {
                String pkgName = pkgNameList[i];
                // 加入至map对象里
                pgkProcessAppMap.put(pkgName, appProcess);
            }
        }
        // 保存所有正在运行的应用程序信息
        List<RunningAppInfo> runningAppInfos = new ArrayList<RunningAppInfo>(); // 保存过滤查到的AppInfo

        for (ApplicationInfo app : listAppcations) {
            // 如果该包名存在 则构造一个RunningAppInfo对象
            if (pgkProcessAppMap.containsKey(app.packageName)) {
                // 获得该packageName的 pid 和 processName
                int pid = pgkProcessAppMap.get(app.packageName).pid;
                String processName = pgkProcessAppMap.get(app.packageName).processName;
                runningAppInfos.add(getAppInfo(app, pid, processName));
            }
        }

        return runningAppInfos;

    }
    private RunningAppInfo getAppInfo(ApplicationInfo app, int pid, String processName) {
        RunningAppInfo appInfo = new RunningAppInfo();
        appInfo.setAppLabel((String) app.loadLabel(pm));
        appInfo.setAppIcon(app.loadIcon(pm));
        appInfo.setPkgName(app.packageName);

        appInfo.setPid(pid);
        appInfo.setProcessName(processName);

        return appInfo;
    }

//获得应用签名
    private byte[] getSign(Context context,String pInfoName) {
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> apps = pm
                .getInstalledPackages(PackageManager.GET_SIGNATURES);
        Iterator<PackageInfo> iter = apps.iterator();

        while (iter.hasNext()) {
            PackageInfo info = iter.next();
            String packageName = info.packageName;
            //按包名 取签名
            if (packageName.equals(pInfoName)) {
                return info.signatures[0].toByteArray();

            }
        }
        return null;
    }
    //获得应用签名
    public static String getPublicKey(byte[] signature) {
        try {

            CertificateFactory certFactory = CertificateFactory
                    .getInstance("X.509");
            X509Certificate cert = (X509Certificate) certFactory
                    .generateCertificate(new ByteArrayInputStream(signature));

            String publickey = cert.getPublicKey().toString();
          //  publickey = publickey.substring(publickey.indexOf("modulus: ") + 9, publickey.indexOf("\n", publickey.indexOf("modulus:")));
                publickey=publickey.substring(publickey.indexOf("=")+1,publickey.indexOf(","));

            return publickey;
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        return null;
    }

}
