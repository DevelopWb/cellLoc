package com.celllocation.newgpsone.functions.persionalLocate;

import com.celllocation.R;
import com.celllocation.newgpsone.bean.PeopleLocateRecordBean;
import com.celllocation.newgpsone.bean.PeopleLocateUserBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2021-09-07 15:16
 * @UpdateUser: 更新者
 * @UpdateDate: 2021-09-07 15:16
 */
public class PersionalLocHisAdapter extends BaseQuickAdapter<PeopleLocateRecordBean, BaseViewHolder> {
    public PersionalLocHisAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, PeopleLocateRecordBean item) {
        helper.setText(R.id.people_name_tv,"成员名称："+item.getPeopleName());
        helper.setText(R.id.people_mobile_tv,"手机号："+item.getPeopleMobile());
        helper.setText(R.id.lng_tv,"经度："+item.getLng());
        helper.setText(R.id.lat_tv,"纬度："+item.getLat());
        helper.setText(R.id.loc_time_tv,"定位时间："+item.getLocTime());
    }
}
