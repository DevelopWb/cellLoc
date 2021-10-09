package com.celllocation.newgpsone.functions.LatlngTransform;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.celllocation.R;
import com.celllocation.newgpsone.base.BaseAppFragment;
import com.celllocation.newgpsone.base.customview.CustomViewPager;
import com.celllocation.newgpsone.base.customview.MainPagerAdapter;
import com.celllocation.newgpsone.functions.BaseFunctionActivity;
import com.juntai.disabled.basecomponent.mvp.IPresenter;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2021-10-07 15:19
 * @UpdateUser: 更新者
 * @UpdateDate: 2021-10-07 15:19
 */
public class LatLngTransformFragment extends BaseAppFragment implements View.OnClickListener {

    private TabLayout mTablayoutTb;
    private CustomViewPager mViewpagerVp;

    @Override
    protected IPresenter createPresenter() {
        return null;
    }

    @Override
    protected void lazyLoad() {
        ((BaseFunctionActivity) getBaseActivity()).setTitleName("经纬度查询");

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.latlng_transform_fg;
    }

    @Override
    protected void initView() {

        mTablayoutTb = (TabLayout) getView(R.id.tablayout_tb);
        mViewpagerVp = (CustomViewPager) getView(R.id.viewpager_vp);
        initTab();
    }
    public void initTab() {
        MainPagerAdapter  adapter = new MainPagerAdapter(getChildFragmentManager(), mContext, getTitleArrays(),
                getTabDrawables(),
                getFragments());
        mViewpagerVp.setAdapter(adapter);
        mViewpagerVp.setOffscreenPageLimit(3);
        mViewpagerVp.setScanScroll(false);
        for (int i = 0; i < getTitleArrays().length; i++) {
            TabLayout.Tab tab = mTablayoutTb.newTab();
            if (tab != null) {
                tab.setCustomView(adapter.getTabView(i));
                mTablayoutTb.addTab(tab);
            }
        }

        mTablayoutTb.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewpagerVp.setCurrentItem(tab.getPosition(), false);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        //        mTablayoutTb.newTab();
        /*viewpager切换默认第一个*/
        mViewpagerVp.setCurrentItem(0);
    }

    private SparseArray<Fragment> getFragments() {
        SparseArray<Fragment> arrays = new SparseArray<>();
        arrays.append(0, LatLngTransformChildFragment.getInstance(0));
        arrays.append(1, LatLngTransformChildFragment.getInstance(1));
        arrays.append(2, LatLngTransformChildFragment.getInstance(2));
        return arrays;
    }

    private int[] getTabDrawables() {
        return new int[]{R.drawable.latlng_gps, R.drawable.latlng_baidu,R.drawable.latlng_gaode};
    }

    private String[] getTitleArrays() {
        return new String[]{"GPS坐标", "百度坐标","高德坐标"};
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onSuccess(String tag, Object o) {

    }


    @Override
    public void onClick(View v) {
    }

}
