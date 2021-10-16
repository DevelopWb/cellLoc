package com.celllocation.newgpsone.functions.wifilocate;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.celllocation.R;
import com.celllocation.newgpsone.Utils.PublicUtill;
import com.celllocation.newgpsone.base.BaseAppFragment;
import com.celllocation.newgpsone.bean.WifiLocBean;
import com.celllocation.newgpsone.bean.WifiLocRequestJson;
import com.celllocation.newgpsone.functions.BaseFunctionActivity;
import com.celllocation.newgpsone.functions.MainPageContract;
import com.celllocation.newgpsone.functions.MainPagePresent;
import com.juntai.disabled.basecomponent.utils.GsonTools;
import com.juntai.disabled.basecomponent.utils.ToastUtils;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2021-09-06 10:06
 * @UpdateUser: 更新者
 * @UpdateDate: 2021-09-06 10:06
 */
public class WifiLocateFragment extends BaseAppFragment<MainPagePresent> implements MainPageContract.IMainPageView,
        View.OnClickListener {
    private View view;
    /**
     * 2
     */
    private EditText mMacEt1;
    /**
     * 查询
     */
    private TextView mCellSearchTv;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cell_search_tv:
                WifiLocRequestJson  wifiLocRequestJson = new WifiLocRequestJson("5c：d0：6e：c6：2e：48",0,8);
                mPresenter.wifiLoc(GsonTools.createGsonString(wifiLocRequestJson),0, PublicUtill.WIFI_LOC_KEY,"");
                break;
            default:
                break;
        }
    }


    @Override
    protected MainPagePresent createPresenter() {
        return new MainPagePresent();
    }

    @Override
    protected void lazyLoad() {
        ((BaseFunctionActivity) getBaseActivity()).setTitleName("wifi定位");
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.wifi_loc_fg;
    }

    @Override
    protected void initView() {

        mMacEt1 = (EditText) getView(R.id.mac_et1);
        mMacEt1 = (EditText) getView(R.id.mac_et1);
        mMacEt1 = (EditText) getView(R.id.mac_et1);
        mMacEt1 = (EditText) getView(R.id.mac_et1);
        mMacEt1 = (EditText) getView(R.id.mac_et1);
        mMacEt1 = (EditText) getView(R.id.mac_et1);
        mCellSearchTv = (TextView) getView(R.id.cell_search_tv);
        mCellSearchTv.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onSuccess(String tag, Object o) {
        WifiLocBean wifiLocBean = (WifiLocBean) o;
        if (wifiLocBean != null) {
            ToastUtils.toast(mContext,wifiLocBean.toString());
        }
    }

}
