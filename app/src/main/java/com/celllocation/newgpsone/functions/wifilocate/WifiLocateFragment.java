package com.celllocation.newgpsone.functions.wifilocate;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.celllocation.R;
import com.celllocation.newgpsone.Utils.PublicUtill;
import com.celllocation.newgpsone.base.BaseAppFragment;
import com.celllocation.newgpsone.bean.WifiLocBean;
import com.celllocation.newgpsone.bean.WifiLocRecordBean;
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
    /**
     * 2
     */
    private EditText mMacEt1;
    /**
     * 查询
     */
    private TextView mCellSearchTv;
    /**
     * 2
     */
    private EditText mMacEt2;
    /**
     * 2
     */
    private EditText mMacEt3;
    /**
     * 2
     */
    private EditText mMacEt4;
    /**
     * 2
     */
    private EditText mMacEt5;
    /**
     * 2
     */
    private EditText mMacEt6;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cell_search_tv:
                String mac = getMacInfo();
                ToastUtils.toast(mContext,mac);
//                WifiLocRequestJson wifiLocRequestJson = new WifiLocRequestJson("5c：d0：6e：c6：2e：48", 0, 8);
//                mPresenter.wifiLoc(GsonTools.createGsonString(wifiLocRequestJson), 0, PublicUtill.WIFI_LOC_KEY, "");
                break;
            default:
                break;
        }
    }

    /**
     * 获取mac
     *
     * @return
     */
    private String getMacInfo() {
        return new StringBuilder().append(getBaseActivity().getTextViewValue(mMacEt1))
                .append(":")
                .append(getBaseActivity().getTextViewValue(mMacEt2))
                .append(":")
                .append(getBaseActivity().getTextViewValue(mMacEt3))
                .append(":")
                .append(getBaseActivity().getTextViewValue(mMacEt4))
                .append(":")
                .append(getBaseActivity().getTextViewValue(mMacEt5))
                .append(":")
                .append(getBaseActivity().getTextViewValue(mMacEt6)).toString();
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

        mCellSearchTv = (TextView) getView(R.id.cell_search_tv);

        mCellSearchTv.setOnClickListener(this);
        mMacEt1 = (EditText) getView(R.id.mac_et1);
        mMacEt2 = (EditText) getView(R.id.mac_et2);
        mMacEt3 = (EditText) getView(R.id.mac_et3);
        mMacEt4 = (EditText) getView(R.id.mac_et4);
        mMacEt5 = (EditText) getView(R.id.mac_et5);
        mMacEt6 = (EditText) getView(R.id.mac_et6);
        mMacEt1.addTextChangedListener(watcher);
        mMacEt2.addTextChangedListener(watcher);
        mMacEt3.addTextChangedListener(watcher);
        mMacEt4.addTextChangedListener(watcher);
        mMacEt5.addTextChangedListener(watcher);
        mMacEt6.addTextChangedListener(watcher);
        mCellSearchTv = (TextView) getView(R.id.cell_search_tv);
    }

    TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String content = s.toString();
            if (content.length() == 2) {
                if (mMacEt1.isFocused()) {
                    getBaseActivity().getViewFocus(mMacEt2, false);
                    return;
                }
                if (mMacEt2.isFocused()) {
                    getBaseActivity().getViewFocus(mMacEt3, false);
                    return;
                }
                if (mMacEt3.isFocused()) {
                    getBaseActivity().getViewFocus(mMacEt4, false);
                    return;
                }
                if (mMacEt4.isFocused()) {
                    getBaseActivity().getViewFocus(mMacEt5, false);
                    return;
                }
                if (mMacEt5.isFocused()) {
                    getBaseActivity().getViewFocus(mMacEt6, false);
                    return;
                }
                if (mMacEt6.isFocused()) {
                    getBaseActivity().hideKeyboardFromView(mMacEt6);
                    return;
                }
            }
        }
    };

    @Override
    protected void initData() {

    }

    @Override
    public void onSuccess(String tag, Object o) {
        WifiLocBean wifiLocBean = (WifiLocBean) o;
        if (wifiLocBean != null) {
            WifiLocBean.LocationBean locationBean = wifiLocBean.getLocation();
            ToastUtils.toast(mContext, wifiLocBean.toString());
            if (locationBean != null) {
                WifiLocRecordBean bean = new WifiLocRecordBean(getMacInfo(),locationBean.getLatitude(),locationBean.getLongitude(),locationBean.getAddressDescription());
                WifiLocAddrActivity.startWifiLocAddrActivity(mContext,WifiLocAddrActivity.class,bean);
            }
        }
    }

}
