package com.celllocation.newgpsone.functions;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.celllocation.R;
import com.celllocation.newgpsone.base.BaseAppActivity;
import com.celllocation.newgpsone.base.customview.CustomViewPager;
import com.celllocation.newgpsone.base.customview.MainPagerAdapter;
import com.juntai.disabled.basecomponent.utils.DisplayUtil;

/**
 * @aouther tobato
 * @description 描述  业务的基类
 * @date 21:57
 */
public abstract class BaseFunctionActivity extends BaseAppActivity<MainPagePresent> implements ViewPager.OnPageChangeListener,
        View.OnClickListener, MainPageContract.IMainPageView {
    private MainPagerAdapter adapter;
    private CustomViewPager mainViewpager;
    private TabLayout mainTablayout;
    private TextView mBackTv;
    public TextView mTitleName;
    private Toolbar mTopToolbar;
    private LinearLayout mMainLayout;

    //


    @Override
    public int getLayoutView() {
        return R.layout.activity_fuctions;
    }

    @Override
    public void initView() {
        initToolbarAndStatusBar(false);
        mImmersionBar.statusBarColor(R.color.colorAccent)
                .statusBarDarkFont(false)
                .init();
        mainViewpager = findViewById(R.id.main_viewpager);
        mainTablayout = findViewById(R.id.main_tablayout);
        mainViewpager.setScanScroll(false);
        mainViewpager.setOffscreenPageLimit(2);
        initTab();
        mBackTv = (TextView) findViewById(R.id.basefuction_back_tv);
        mBackTv.setOnClickListener(this);
        Drawable drawable = mContext.getResources().getDrawable(R.drawable.app_back);
        // 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        mBackTv.setCompoundDrawables(drawable, null, null, null);
        //            mBackTv.setText("返回");
        mBackTv.setCompoundDrawablePadding(-DisplayUtil.dp2px(this, 3));
        mTitleName = (TextView) findViewById(R.id.basefuction_title_name);
        mTopToolbar = (Toolbar) findViewById(R.id.top_toolbar);
        mMainLayout = (LinearLayout) findViewById(R.id.main_layout);
        mTopToolbar.setVisibility(View.VISIBLE);
        mTopToolbar.setNavigationIcon(null);
        mTopToolbar.setBackgroundResource(com.juntai.disabled.basecomponent.R.color.colorAccent);
    }

    @Override
    public void initData() {
    }


    public void initTab() {
        adapter = new MainPagerAdapter(getSupportFragmentManager(), getApplicationContext(), getTitleArrays(),
                getTabDrawables(),
                getFragments());
        mainViewpager.setAdapter(adapter);
        mainViewpager.setOffscreenPageLimit(getTitleArrays().length);
        /*viewpager切换监听，包含滑动点击两种*/
        mainViewpager.addOnPageChangeListener(this);
        for (int i = 0; i < getTitleArrays().length; i++) {
            TabLayout.Tab tab = mainTablayout.newTab();
            if (tab != null) {
                if (i == getTitleArrays().length - 1) {
                    tab.setCustomView(adapter.getTabView(i, true));
                } else {
                    tab.setCustomView(adapter.getTabView(i, false));
                }
                mainTablayout.addTab(tab);
            }
        }

        mainTablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mainViewpager.setCurrentItem(tab.getPosition(), false);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        //        mainTablayout.newTab();
        /*viewpager切换默认第一个*/
        mainViewpager.setCurrentItem(0);
    }

    protected abstract SparseArray<Fragment> getFragments();

    protected abstract int[] getTabDrawables();

    protected abstract String[] getTitleArrays();

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    @Override
    public void onSuccess(String tag, Object o) {
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.basefuction_back_tv:
                finish();
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
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onPause() {
        super.onPause();
    }


}
