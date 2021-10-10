package com.celllocation.newgpsone.functions.LatlngTransform;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.celllocation.R;
import com.celllocation.newgpsone.base.BaseAppFragment;
import com.juntai.disabled.basecomponent.mvp.IPresenter;
import com.juntai.disabled.basecomponent.utils.ToastUtils;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2021-10-07 15:19
 * @UpdateUser: 更新者
 * @UpdateDate: 2021-10-07 15:19
 */
public class LatLngTransformChildFragment extends BaseAppFragment implements View.OnClickListener {
    //坐标类型 0是gps  1是百度 2是国标
    public static String LATLNG_TYPE = "lng_type";
    private int latlngType;
    private View view;
    private EditText mLngValueEt;
    private EditText mLatValueEt;
    /**
     * 提交
     */
    private TextView mCellSearchTv;

    public static LatLngTransformChildFragment getInstance(int type) {
        LatLngTransformChildFragment latLngTransformFragment = new LatLngTransformChildFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(LATLNG_TYPE, type);
        latLngTransformFragment.setArguments(bundle);
        return latLngTransformFragment;
    }

    @Override
    protected IPresenter createPresenter() {
        return null;
    }

    @Override
    protected void lazyLoad() {
        latlngType = getArguments().getInt(LATLNG_TYPE);


    }

    @Override
    protected int getLayoutRes() {
        return R.layout.latlng_transform_child_fg;
    }

    @Override
    protected void initView() {

        mLngValueEt = (EditText) getView(R.id.lng_value_et);
        mLatValueEt = (EditText) getView(R.id.lat_value_et);
        mCellSearchTv = (TextView) getView(R.id.cell_search_tv);
        mCellSearchTv.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onSuccess(String tag, Object o) {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.cell_search_tv:

                if (TextUtils.isEmpty(getBaseActivity().getTextViewValue(mLngValueEt))) {
                    ToastUtils.toast(mContext,"请输入经度");
                    return;
                }
                if (TextUtils.isEmpty(getBaseActivity().getTextViewValue(mLatValueEt))) {
                    ToastUtils.toast(mContext,"请输入纬度");
                    return;
                }
                startActivity(new Intent(mContext,LatLngAddrActivity.class).putExtra(LatLngAddrActivity.KEY_LAT,
                        Double.parseDouble(getBaseActivity().getTextViewValue(mLatValueEt))).putExtra(LatLngAddrActivity.KEY_LNG,
                        Double.parseDouble(getBaseActivity().getTextViewValue(mLngValueEt))));
                break;
        }
    }
}
