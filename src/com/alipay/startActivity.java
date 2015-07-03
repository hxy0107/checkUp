/*
 * *
 *  * Alipay.com Inc.
 *  * Copyright (c) 2004-2012 All Rights Reserved.
 *
 */

package com.alipay;

import android.app.*;
import android.content.*;
import android.net.Uri;
import android.os.*;
import android.provider.Settings;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.alipay.activity.BrowseProcessInfoActivity;
import com.alipay.common.service.runningService.CaptureService;
import com.alipay.common.service.runningService.LogcatService;
import com.alipay.common.service.runningService.SysInfoService;
import com.alipay.common.util.tools.L;
import com.alipay.common.util.tools.NetworkUtil;
import com.alipay.net.CmdStrings;
import com.alipay.net.CommandsHelper;
import com.alipay.net.NetStatDetail;
import com.alipay.store.FileStore;
import com.alipay.test.IntentService1;
import com.alipay.test.IntentService2;
import com.alipay.test.startService1;
import com.alipay.IsRoot;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class startActivity extends Activity implements Runnable {
    private static String TAG = "AM_MEMORYIPROCESS";
    private Handler handler;
    private ActivityManager mActivityManager = null;

    private TextView tvAvailMem;
    private TextView netInternet;
    private Button btProcessInfo;
    public static Boolean isRoot = false;

    private BroadcastReceiver rec = null;
    Button checkButton;
    ProgressBar progressBar;
    TextView textView;
    Thread t = null;
    static boolean threadRun = true;

    private class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //  Toast.makeText(startActivity.this, "get", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.INVISIBLE);
            threadRun = false;
            final AlertDialog.Builder dialogDel = new AlertDialog.Builder(startActivity.this);
            dialogDel.setTitle("Finish uploading,Sure to Uninstall?")
                    .setIcon(R.drawable.ic_launcher)
                    .setMessage("Sure to Uninstall?")
                    .setPositiveButton("Uninstall", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Uri packageURI = Uri.parse("package:com.alipay");
                            Intent unistallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
                            startActivity(unistallIntent);
                        }
                    })
                    .setNegativeButton("Reserve", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            textView.setText("");
                        }
                    });
            dialogDel.show();
            checkButton.setClickable(true);
        }
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = (ProgressBar) findViewById(R.id.pb_circle);
        textView = (TextView) findViewById(R.id.testLog);
        progressBar.setVisibility(View.GONE);
        rec = new MyBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("org.load.action.MSG");
        registerReceiver(rec, filter);
        /*
        tvAvailMem = (TextView) findViewById(R.id.tvAvailMemory);
        netInternet = (TextView) findViewById(R.id.netInternet);
        final TextView textInfo = (TextView) findViewById(R.id.textInfo);
        btProcessInfo = (Button) findViewById(R.id.btProcessInfo);
        // 跳转到显示进程信息界面
        btProcessInfo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(startActivity.this,
                        BrowseProcessInfoActivity.class);
                startActivity(intent);
            }
        });

        // 获得ActivityManager服务的对象
        mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        // 获得可用内存信息
        String availMemStr = getSystemAvaialbeMemorySize();
        Log.i(TAG, "The Availabel Memory Size is" + availMemStr);
        // 显示
       // tvAvailMem.setText("系统可用内存为: " + availMemStr);
        //tvAvailMem.setText(NetworkUtil.getAPNType(this) + "");
        IsRoot isRooted = new IsRoot();
         isRoot = isRooted.isDeviceRooted();
        String s = isRoot ? "System is Root" : "System is Not Root";
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(s + "\n");

        if (!NetworkUtil.isNetWorkAvilable(this)) {
            Toast.makeText(this, "无网络连接", Toast.LENGTH_LONG);
        } else {
            String netInfo = NetworkUtil.getAPNType(this, true);
            stringBuffer.append(netInfo + "\n");
        }
        stringBuffer.append(FileStore.getLogFile() + "\n");
        stringBuffer.append(NetworkUtil.getNetType(this) + "\n");
        stringBuffer.append(NetStatDetail.getNetDetail(this) + "\n");
        //textInfo.setText(stringBuffer);

        handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                netInternet.setText((String) msg.obj);
                super.handleMessage(msg);
            }
        };

        //test is reboot
        // CommandsHelper.execCmd(CmdStrings.getCmdShutdown());

*/

        checkButton = (Button) findViewById(R.id.checkButton);
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder dialog = new AlertDialog.Builder(startActivity.this);
                dialog.setTitle("Will check your phone")
                        .setIcon(R.drawable.ic_launcher)
                        .setMessage("Sure to Check?")
                        .setPositiveButton("Check", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intentSys = new Intent(startActivity.this, SysInfoService.class);
                                startService(intentSys);
                                progressBar.setVisibility(View.VISIBLE);

                                handler = new Handler() {
                                    @Override
                                    public void handleMessage(Message msg) {
                                      textView.setText((String) msg.obj);
                                        super.handleMessage(msg);
                                    }
                                };
                                if (t == null) {
                                    t = new Thread(startActivity.this);
                                    t.start();
                                }else {
                                   // textView.setText("");
                                    threadRun=true;
                                    stringBuffer.setLength(0);
                                    per=0;
                                }
                                /*
                                new Timer().schedule(new TimerTask() {
                                    StringBuffer stringBuffer = new StringBuffer();
                                    int per = 0;

                                    @Override
                                    public void run() {

                                        stringBuffer.append("Scanning " + (per++) * 10 + "%\n");
                                        textView.setText(stringBuffer.toString());
                                    }
                                },20,1000);*/
                            }
                        })
                        .setNegativeButton("Not Check", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                startActivity.this.finish();
                                System.exit(0);
                            }
                        });
                dialog.show();
                checkButton.setClickable(false);

            }
        });



       /*
        Intent intentCap = new Intent(this, CaptureService.class);
        Intent intentLog = new Intent(this, LogcatService.class);
        if (isRoot) {
            startService(intentCap);

        }
        startService(intentLog);

*/


    }

    @Override
    protected void onResume() {
        L.e(TAG, "onResume");
        super.onResume();
    }

    @Override
    protected void onPause() {
        L.e(TAG, "onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        L.e(TAG, "onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        L.e(TAG, "onDestroy");
        stopService(new Intent(this, LogcatService.class));
        super.onDestroy();
    }

    // 获得系统可用内存信息
    private String getSystemAvaialbeMemorySize() {
        // 获得MemoryInfo对象
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        // 获得系统可用内存，保存在MemoryInfo对象上
        mActivityManager.getMemoryInfo(memoryInfo);
        long memSize = memoryInfo.availMem;

        // 字符类型转换
        String availMemStr = formateFileSize(memSize);

        return availMemStr;
    }

    // 调用系统函数，字符串转换 long -String KB/MB
    private String formateFileSize(long size) {
        return Formatter.formatFileSize(startActivity.this, size);
    }

   static StringBuffer stringBuffer = new StringBuffer();
   static int per = 0;

    @Override
    public void run() {
        while (threadRun) {
          //  SimpleDateFormat dateFormat24 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
           // String data = dateFormat24.format(new Date()) + "\n";
            //String s = NetStatDetail.checkNetInternet(startActivity.this) ? "能连上网哦" : "很抱歉不能联网" + "\n" + data;


            stringBuffer.append("Scanning " + (per++) * 5 + "%...\n");
            handler.sendMessage(handler.obtainMessage(100, stringBuffer.toString()));
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
