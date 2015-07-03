package com.alipay.activity;

/**
 * Created by xianyu.hxy on 2015/5/29.
 */


        import java.util.ArrayList;
        import java.util.List;

        import android.app.Activity;
        import android.app.ActivityManager;
        import android.app.AlertDialog;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.os.Bundle;
        import android.os.Debug;
        import android.util.Log;
        import android.view.ContextMenu;
        import android.view.ContextMenu.ContextMenuInfo;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.AdapterView.OnItemClickListener;
        import android.widget.ListView;
        import android.widget.TextView;
        import com.alipay.R;
        import com.alipay.common.util.tools.BrowseProcessInfoAdapter;
        import com.alipay.common.util.tools.ProcessInfo;

public class BrowseProcessInfoActivity extends Activity implements
        OnItemClickListener {

    private static String TAG = "ProcessInfo";
    private static final int KILL_PORCESS = 1;
    private static final int SEARCH_RUNNING_APP = 2;

    private ActivityManager mActivityManager = null;
    // ProcessInfo Model�� �����������н�����Ϣ
    private List<ProcessInfo> processInfoList = null;

    private ListView listviewProcess;
    private TextView tvTotalProcessNo;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.browse_process_info);

        listviewProcess = (ListView) findViewById(R.id.listviewProcess);
        listviewProcess.setOnItemClickListener(this);

        tvTotalProcessNo = (TextView) findViewById(R.id.tvTotalProcessNo);

        this.registerForContextMenu(listviewProcess);
        // ���ActivityManager����Ķ���
        mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        // ���ϵͳ������Ϣ
        getRunningAppProcessInfo();
        // ΪListView��������������
        BrowseProcessInfoAdapter mprocessInfoAdapter = new BrowseProcessInfoAdapter(
                this, processInfoList);
        listviewProcess.setAdapter(mprocessInfoAdapter);

        tvTotalProcessNo.setText("��ǰϵͳ���̹��У� " + processInfoList.size());
    }

    // ɱ���ý��̣�����ˢ��
    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, final int position,
                            long arg3) {
        // TODO Auto-generated method stub
        new AlertDialog.Builder(this)
                .setMessage("�Ƿ�ɱ���ý���")
                .setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        // ɱ���ý��̣��ͷŽ���ռ�õĿռ�
                        mActivityManager
                                .killBackgroundProcesses(processInfoList.get(
                                        position).getProcessName());
                        // ˢ�½���
                        getRunningAppProcessInfo();
                        BrowseProcessInfoAdapter mprocessInfoAdapter = new BrowseProcessInfoAdapter(
                                BrowseProcessInfoActivity.this, processInfoList);
                        listviewProcess.setAdapter(mprocessInfoAdapter);
                        tvTotalProcessNo.setText("��ǰϵͳ���̹��У�"
                                + processInfoList.size());

                    }
                })
                .setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        dialog.cancel();
                    }
                }).create().show();
    }

    // ���ϵͳ������Ϣ
    private void getRunningAppProcessInfo() {
        // ProcessInfo Model�� �����������н�����Ϣ
        processInfoList = new ArrayList<ProcessInfo>();

        // ͨ������ActivityManager��getRunningAppProcesses()�������ϵͳ�������������еĽ���
        List<ActivityManager.RunningAppProcessInfo> appProcessList = mActivityManager
                .getRunningAppProcesses();

        for (ActivityManager.RunningAppProcessInfo appProcessInfo : appProcessList) {
            // ����ID��
            int pid = appProcessInfo.pid;
            // �û�ID ������Linux��Ȩ�޲�ͬ��IDҲ�Ͳ�ͬ ���� root��
            int uid = appProcessInfo.uid;
            // ��������Ĭ���ǰ�������������android��process=""ָ��
            String processName = appProcessInfo.processName;
            // ��øý���ռ�õ��ڴ�
            int[] myMempid = new int[] { pid };
            // ��MemoryInfoλ��android.os.Debug.MemoryInfo���У�����ͳ�ƽ��̵��ڴ���Ϣ
            Debug.MemoryInfo[] memoryInfo = mActivityManager
                    .getProcessMemoryInfo(myMempid);
            // ��ȡ����ռ�ڴ�����Ϣ kb��λ
            int memSize = memoryInfo[0].dalvikPrivateDirty;

            Log.i(TAG, "processName: " + processName + "  pid: " + pid
                    + " uid:" + uid + " memorySize is -->" + memSize + "kb");
            // ����һ��ProcessInfo����
            ProcessInfo processInfo = new ProcessInfo();
            processInfo.setPid(pid);
            processInfo.setUid(uid);
            processInfo.setMemSize(memSize);
            processInfo.setProcessName(processName);
            processInfoList.add(processInfo);

            // ���ÿ�����������е�Ӧ�ó���(��),��ÿ��Ӧ�ó���İ���
            String[] packageList = appProcessInfo.pkgList;
            Log.i(TAG, "process id is " + pid + "has " + packageList.length);
            for (String pkg : packageList) {
                Log.i(TAG, "packageName " + pkg + " in process id is -->" + pid);
            }
        }
    }

    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        menu.add(0, KILL_PORCESS, KILL_PORCESS, "ɱ���ý���");
        menu.add(0, KILL_PORCESS, SEARCH_RUNNING_APP, "�����ڸý��̵�Ӧ�ó���");
        super.onCreateContextMenu(menu, v, menuInfo);

    }

    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case KILL_PORCESS: // ɱ���ý��� �� ���¼��ؽ���
                new AlertDialog.Builder(this)
                        .setMessage("�Ƿ�ɱ���ý���")
                        .setPositiveButton("ȷ��",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        // TODO Auto-generated method stub

                                    }
                                })
                        .setNegativeButton("ȡ��",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        // TODO Auto-generated method stub
                                        dialog.cancel();
                                    }
                                }).create().show();
                break;
            case SEARCH_RUNNING_APP: // �鿴�����ڸý��̵�Ӧ�ó�����Ϣ
                break;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }
}
