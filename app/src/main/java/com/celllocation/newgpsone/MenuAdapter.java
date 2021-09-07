package com.celllocation.newgpsone;

import com.celllocation.R;
import com.celllocation.newgpsone.bean.HomePageMenuBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.juntai.disabled.basecomponent.utils.ImageLoadUtil;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2021/4/18 15:39
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/4/18 15:39
 */
public class MenuAdapter extends BaseQuickAdapter<HomePageMenuBean, BaseViewHolder> {
    public MenuAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, HomePageMenuBean item) {

        helper.setText(R.id.homepage_menu_title_tv,item.getMenuName());
        ImageLoadUtil.loadImage(mContext,item.getMenuPicId(),helper.getView(R.id.menu_icon_iv));
    }
}
