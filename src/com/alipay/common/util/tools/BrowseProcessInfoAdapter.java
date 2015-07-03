package com.alipay.common.util.tools;

/**
 * Created by xianyu.hxy on 2015/5/29.
 */

        import java.util.List;

        import android.content.Context;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.BaseAdapter;
        import android.widget.TextView;
        import com.alipay.R;

public class BrowseProcessInfoAdapter extends BaseAdapter {

    private Context context;
    private List<ProcessInfo> processInfoList;

    public BrowseProcessInfoAdapter(Context context,
                                    List<ProcessInfo> processInfoList) {
        this.context = context;
        this.processInfoList = processInfoList;
    }

    @Override
    public int getCount() {
        // TODO �Զ����ɵķ������
        return processInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO �Զ����ɵķ������
        return processInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO �Զ����ɵķ������
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        // TODO �Զ����ɵķ������
        if (convertView == null) {
            view = View.inflate(context, R.layout.process_info_item, null);
            ProcessInfo info = processInfoList.get(position);

            ViewHolder holder = new ViewHolder();
            holder.tv_pid = (TextView) view.findViewById(R.id.tv_pid);
            holder.tv_uid = (TextView) view.findViewById(R.id.tv_uid);
            holder.tv_memSize = (TextView) view.findViewById(R.id.tv_memSize);
            holder.tv_processName = (TextView) view
                    .findViewById(R.id.tv_processName);

            holder.tv_pid.setText("����id: "+info.getPid());
            holder.tv_uid.setText("�û�id:"+info.getUid());
            holder.tv_memSize.setText("����ռ���ڴ�:"+info.getMemSize()+"KB");
            holder.tv_processName.setText("������:"+info.getProcessName());
        } else{
            view = convertView;
        }
        return view;
    }

    private static class ViewHolder {
        private TextView tv_pid;
        private TextView tv_uid;
        private TextView tv_memSize;
        private TextView tv_processName;
    }

}

