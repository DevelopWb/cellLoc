package com.celllocation.newgpsone.functions.persionalLocate;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.Marker;
import com.celllocation.R;
import com.celllocation.newgpsone.Utils.RegOperateTool;
import com.celllocation.newgpsone.base.BaseAppFragment;
import com.celllocation.newgpsone.database.DataHelper;
import com.celllocation.newgpsone.functions.BaseFunctionActivity;
import com.celllocation.newgpsone.functions.MainPageContract;
import com.celllocation.newgpsone.functions.MainPagePresent;
import com.juntai.disabled.basecomponent.utils.DisplayUtil;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2021-09-06 10:06
 * @UpdateUser: 更新者
 * @UpdateDate: 2021-09-06 10:06
 */
public class PersionalLocateFragment extends BaseAppFragment<MainPagePresent> implements MainPageContract.IMainPageView, View.OnClickListener {

    @Override
    public MainPagePresent createPresenter() {
        return null;
    }

    @Override
    public void lazyLoad() {
        ((BaseFunctionActivity) getBaseActivity()).setTitleName("人员列表");
    }

    @Override
    public int getLayoutRes() {
        return R.layout.cell_locate_oneselfe_layout;
    }

    @Override
    public void initView() {
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onSuccess(String tag, Object o) {

    }
}
