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
    //����Ӧ��
    public ArrayList<AppInfo> Get(Context context){
        ArrayList<AppInfo> appList = new ArrayList<AppInfo>();
        ArrayList<AppInfo> appList3rd=new ArrayList<AppInfo>();
        List<PackageInfo> packageInfos=context.getPackageManager().getInstalledPackages(PackageManager.GET_SIGNATURES);
        for (int i = 0; i < packageInfos.size(); i++) {
            PackageInfo pInfo=packageInfos.get(i);
            AppInfo allAppInfo=new AppInfo();
            allAppInfo.setAppname(pInfo.applicationInfo.loadLabel(context.getPackageManager()).toString());//Ӧ�ó��������
            allAppInfo.setPackagename(pInfo.packageName);//Ӧ�ó���İ�
            allAppInfo.setVersionCode(pInfo.versionCode);//�汾��
            allAppInfo.setLastInstal(pInfo.firstInstallTime);
            allAppInfo.setVersionName(pInfo.versionName);
            //ǩ��


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
    //����Ӧ��
    public ArrayList<AppInfo> GetOnly3rd(Context context){
        ArrayList<AppInfo> appList = new ArrayList<AppInfo>();
        List<PackageInfo> packageInfos=context.getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packageInfos.size(); i++) {
            PackageInfo pInfo=packageInfos.get(i);
            AppInfo allAppInfo=new AppInfo();
            allAppInfo.setAppname(pInfo.applicationInfo.loadLabel(context.getPackageManager()).toString());//Ӧ�ó��������
            allAppInfo.setPackagename(pInfo.packageName);//Ӧ�ó���İ�
            allAppInfo.setVersionCode(pInfo.versionCode);//�汾��
            allAppInfo.setLastInstal(pInfo.firstInstallTime);
            //allAppInfo.setProvider(pInfo.providers);
            allAppInfo.setInstalPath(pInfo.applicationInfo.sourceDir);
            allAppInfo.setAppicon(pInfo.applicationInfo.loadIcon(context.getPackageManager()));
            if((pInfo.applicationInfo.flags&ApplicationInfo.FLAG_SYSTEM)==0){
                appList.add(allAppInfo);//�����ϵͳӦ�ã��������appList
            }

        }
        return appList;
    }
    //����ϵͳӦ��
    public ArrayList<AppInfo> GetOnlysys(Context context){
        ArrayList<AppInfo> appList = new ArrayList<AppInfo>();
        List<PackageInfo> packageInfos=context.getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packageInfos.size(); i++) {
            PackageInfo pInfo=packageInfos.get(i);
            AppInfo allAppInfo=new AppInfo();
            allAppInfo.setAppname(pInfo.applicationInfo.loadLabel(context.getPackageManager()).toString());//Ӧ�ó��������
            allAppInfo.setPackagename(pInfo.packageName);//Ӧ�ó���İ�
            allAppInfo.setVersionCode(pInfo.versionCode);//�汾��
            allAppInfo.setLastInstal(pInfo.firstInstallTime);
            //allAppInfo.setProvider(pInfo.providers);
            allAppInfo.setInstalPath(pInfo.applicationInfo.sourceDir);
            allAppInfo.setAppicon(pInfo.applicationInfo.loadIcon(context.getPackageManager()));
            if(!((pInfo.applicationInfo.flags&ApplicationInfo.FLAG_SYSTEM)==0)){
                appList.add(allAppInfo);//�����ϵͳӦ�ã��������appList
            }

        }
        return appList;
    }
    public boolean filterApp(ApplicationInfo info) {
        if ((info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
        // �������ϵͳ��Ӧ��,���Ǳ��û�������. �û�Ӧ��
            return true;
        } else if ((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
            // ������û���Ӧ��
            return true;
        }
        return false;
    }


    public List<RunningAppInfo> queryAllRunningAppInfo(Context context) {
        pm = context.getPackageManager();
        // ��ѯ�����Ѿ���װ��Ӧ�ó���
        List<ApplicationInfo> listAppcations = pm.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
        Collections.sort(listAppcations, new ApplicationInfo.DisplayNameComparator(pm));// ����

        // ���������������еİ��� �Լ������ڵĽ�����Ϣ
        Map<String, ActivityManager.RunningAppProcessInfo> pgkProcessAppMap = new HashMap<String, ActivityManager.RunningAppProcessInfo>();

        ActivityManager mActivityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        // ͨ������ActivityManager��getRunningAppProcesses()�������ϵͳ�������������еĽ���
        List<ActivityManager.RunningAppProcessInfo> appProcessList = mActivityManager
                .getRunningAppProcesses();

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcessList) {
            int pid = appProcess.pid; // pid
            String processName = appProcess.processName; // ������

            String[] pkgNameList = appProcess.pkgList; // ��������ڸý����������Ӧ�ó����

            // �������Ӧ�ó���İ���
            for (int i = 0; i < pkgNameList.length; i++) {
                String pkgName = pkgNameList[i];
                // ������map������
                pgkProcessAppMap.put(pkgName, appProcess);
            }
        }
        // ���������������е�Ӧ�ó�����Ϣ
        List<RunningAppInfo> runningAppInfos = new ArrayList<RunningAppInfo>(); // ������˲鵽��AppInfo

        for (ApplicationInfo app : listAppcations) {
            // ����ð������� ����һ��RunningAppInfo����
            if (pgkProcessAppMap.containsKey(app.packageName)) {
                // ��ø�packageName�� pid �� processName
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

//���Ӧ��ǩ��
    private byte[] getSign(Context context,String pInfoName) {
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> apps = pm
                .getInstalledPackages(PackageManager.GET_SIGNATURES);
        Iterator<PackageInfo> iter = apps.iterator();

        while (iter.hasNext()) {
            PackageInfo info = iter.next();
            String packageName = info.packageName;
            //������ ȡǩ��
            if (packageName.equals(pInfoName)) {
                return info.signatures[0].toByteArray();

            }
        }
        return null;
    }
    //���Ӧ��ǩ��
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
