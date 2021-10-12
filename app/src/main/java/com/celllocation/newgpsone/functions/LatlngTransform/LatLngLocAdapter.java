package com.celllocation.newgpsone.functions.LatlngTransform;

import com.celllocation.R;
import com.celllocation.newgpsone.bean.LatLngBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.text.DecimalFormat;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2021/10/12 21:44
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/10/12 21:44
 */
public class LatLngLocAdapter extends BaseQuickAdapter<LatLngBean, BaseViewHolder> {
    public LatLngLocAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, LatLngBean item) {
        helper.setText(R.id.lng_tv, "经度：" + new DecimalFormat("0.000000").format(item.getWgLon()));
        helper.setText(R.id.lat_tv, "纬度：" + new DecimalFormat("0.000000").format(item.getWgLat()));
        String loctype = null;
        switch (item.getLocType()) {
            case 0:
                loctype ="GPS坐标";
                break;
            case 1:
                loctype ="百度坐标";
                break;
            case 2:
                loctype ="高德坐标";
                break;
            default:
                break;
        }
        helper.setText(R.id.loc_type_tv,"坐标类型："+loctype);
        helper.setText(R.id.loc_time_tv,"定位时间："+item.getTime());

    }
}
