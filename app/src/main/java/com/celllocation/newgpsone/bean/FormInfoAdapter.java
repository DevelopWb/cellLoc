package com.celllocation.newgpsone.bean;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.celllocation.R;
import com.celllocation.newgpsone.FormDataLocationMapActivity;

import java.util.List;

/**
 * Created by Administrator on 2016/11/21.
 */

public class FormInfoAdapter extends BaseAdapter {


    private Context context;
    private List<FormInfo> arrays;
    private ViewHolder holder;

    public FormInfoAdapter(Context context, List<FormInfo> arrays) {
        this.context = context;
        this.arrays = arrays;
    }


    @Override
    public int getCount() {
        return arrays == null ? 0 : arrays.size();
    }

    @Override
    public Object getItem(int position) {
        return arrays.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.form_info, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final FormInfo bean = arrays.get(position);
        holder.phone_name.setText("姓名：" + bean.getName());
        holder.phone_num.setText("电话：" + bean.getPhone());
        holder.startTime.setText("起始：" + bean.getStartTime());
        holder.endTime.setText("截止：" + bean.getEndTime());
        holder.phone_detail.setText("备注：" + bean.getDetail());
        holder.display_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FormDataLocationMapActivity.class);
                intent.putExtra("ImportTime", bean.getImportTime());
                intent.putExtra("PhoneNum", bean.getPhone());
                context.startActivity(intent);
            }
        });


        return convertView;
    }


    class ViewHolder {
        private TextView phone_name;
        private TextView phone_num;
        private TextView startTime;
        private TextView endTime;
        private TextView phone_detail;
        private TextView display_tv;

        public ViewHolder(View v) {
            phone_name = (TextView) v.findViewById(R.id.phone_name_tv);
            phone_num = (TextView) v.findViewById(R.id.phone_num_tv);
            startTime = (TextView) v.findViewById(R.id.startTime_tv);
            endTime = (TextView) v.findViewById(R.id.endTime_tv);
            phone_detail = (TextView) v.findViewById(R.id.phone_detail_tv);
            display_tv = (TextView) v.findViewById(R.id.display_tv);


        }
    }
}
