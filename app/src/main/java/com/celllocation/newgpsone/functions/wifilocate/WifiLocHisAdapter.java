package com.celllocation.newgpsone.functions.wifilocate;

import com.celllocation.R;
import com.celllocation.newgpsone.bean.WifiLocRecordBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.text.DecimalFormat;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2021-09-07 15:16
 * @UpdateUser: 更新者
 * @UpdateDate: 2021-09-07 15:16
 */
public class WifiLocHisAdapter extends BaseQuickAdapter<WifiLocRecordBean, BaseViewHolder> {
    public WifiLocHisAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, WifiLocRecordBean item) {
        helper.setText(R.id.lng_tv, "经度：" + new DecimalFormat("0.000000").format(item.getLongitude()));
        helper.setText(R.id.lat_tv, "纬度：" + new DecimalFormat("0.000000").format(item.getLatitude()));
        helper.setText(R.id.loc_type_tv,"MAC："+item.getMac());
        helper.setText(R.id.loc_time_tv,"定位时间："+item.getLocTime());
    }
}
