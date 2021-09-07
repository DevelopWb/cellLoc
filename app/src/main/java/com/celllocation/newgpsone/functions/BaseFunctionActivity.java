package com.celllocation.newgpsone.functions;


import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.View;
import android.widget.LinearLayout;

import com.celllocation.R;
import com.celllocation.newgpsone.base.BaseAppActivity;
import com.celllocation.newgpsone.base.customview.CustomViewPager;
import com.celllocation.newgpsone.base.customview.MainPagerAdapter;

/**
 * @aouther tobato
 * @description 描述  业务的基类
 * @date  21:57
 */
public abstract class BaseFunctionActivity extends BaseAppActivity<MainPagePresent> implements ViewPager.OnPageChangeListener,
        View.OnClickListener, MainPageContract.IMainPageView {
    private MainPagerAdapter adapter;
    private LinearLayout mainLayout;
    private CustomViewPager mainViewpager;
    private TabLayout mainTablayout;

    //


    @Override
    public int getLayoutView() {
        return R.layout.activity_fuctions;
    }

    @Override
    public void initView() {
        mainViewpager = findViewById(R.id.main_viewpager);
        mainTablayout = findViewById(R.id.main_tablayout);
        mainLayout = findViewById(R.id.main_layout);
        mainViewpager.setScanScroll(false);
        mainViewpager.setOffscreenPageLimit(2);
        initTab();
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
