package com.celllocation.newgpsone.functions.persionalLocate;

import com.celllocation.R;
import com.celllocation.newgpsone.bean.PeopleLocateUserBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.juntai.disabled.basecomponent.utils.ImageLoadUtil;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2021-09-07 15:16
 * @UpdateUser: 更新者
 * @UpdateDate: 2021-09-07 15:16
 */
public class PersionalLocUserListAdapter extends BaseQuickAdapter<PeopleLocateUserBean, BaseViewHolder> {
    public PersionalLocUserListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, PeopleLocateUserBean item) {
        helper.setText(R.id.people_name_tv, item.getPeopleName());
        helper.setText(R.id.people_mobile_tv, item.getPeopleMobile());
        helper.setText(R.id.people_loc_type_tv, item.getLocType()==0?"设备类型：手机":"设备类型：GPS设备");
        ImageLoadUtil.loadImage(mContext,item.getHeadPicPath(),R.mipmap.default_head_icon,R.mipmap.default_head_icon,
                helper.getView(R.id.head_pic_iv));
    }
}
