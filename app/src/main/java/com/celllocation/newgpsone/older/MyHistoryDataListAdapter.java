package com.celllocation.newgpsone.older;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.celllocation.R;
import com.celllocation.newgpsone.bean.CellHisData;

import java.util.List;

public class MyHistoryDataListAdapter extends BaseAdapter {
    LayoutInflater Inflater;
    private List<CellHisData> arrays;
    int layout;
    Context context;
    private ViewHolder holder;

    public MyHistoryDataListAdapter(Context context, List<CellHisData> arrays) {
        Inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.arrays = arrays;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return arrays == null ? 0 : arrays.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return arrays.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final CellHisData bean = arrays.get(position);

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = Inflater.inflate(R.layout.new_layouthistorydatapos, parent, false);
            holder.cell_his_jizhan = (TextView) convertView.findViewById(R.id.cell_his_jizhan);
            holder.cell_his_shanqu = (TextView) convertView.findViewById(R.id.cell_his_shanqu);
            holder.cell_his_nid = (TextView) convertView.findViewById(R.id.cell_his_nid);
            holder.cell_his_address = (TextView) convertView.findViewById(R.id.cell_his_address);
            holder.cell_his_time = (TextView) convertView.findViewById(R.id.cell_his_time);
            holder.cell_loc_type = (TextView) convertView.findViewById(R.id.cell_his_loc_type_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NewHistoryMapActivity.class);
                intent.putExtra("ClickedTime",bean.getTime());
                intent.putExtra("Position",position);
                context.startActivity(intent);
            }
        });
        holder.cell_his_jizhan.setText(("3".equals(bean.getType())?"SID：":"LAC：" )+ bean.getLac());
        holder.cell_his_shanqu.setText(("3".equals(bean.getType())?"BID：":"CID：" )+ bean.getCid());
        holder.cell_his_address.setText("地址：" + bean.getAddress());
        holder.cell_his_time.setText("定位时间：" + bean.getTime());
        holder.cell_loc_type.setText("基站类型：" +getCellType(bean.getType()));
        if (bean.getNid() != null&& !TextUtils.isEmpty(bean.getNid())) {
            if (Integer.parseInt(bean.getNid()) > -1) {
                holder.cell_his_nid.setText("NID：" + bean.getNid());
            }else {
                holder.cell_his_nid.setVisibility(View.GONE);
            }
        } else {
            holder.cell_his_nid.setVisibility(View.GONE);
        }

        return convertView;
    }

    /**
     * 获取基站类型
     * @param type
     * @return
     */
    private String  getCellType(String  type){
        String typeName = null;
        switch (type) {
            case "0":
                typeName = "中国移动";
                break;
            case "1":
                typeName = "中国联通";
                break;
            case "3":
                typeName = "中国电信";
                break;
            default:
                break;
        }

        return typeName;
    }


    class ViewHolder {
        TextView cell_his_jizhan;
        TextView cell_his_shanqu;
        TextView cell_his_nid;
        TextView cell_his_address;
        TextView cell_his_time;
        TextView cell_loc_type;
    }

}
