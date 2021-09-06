package com.celllocation.newgpsone.homepage;


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
import com.celllocation.newgpsone.homepage.fragments.CellLocateFragment;
import com.celllocation.newgpsone.homepage.fragments.CellSearchFragment;

public class MainActivity extends BaseAppActivity<MainPagePresent> implements ViewPager.OnPageChangeListener,
        View.OnClickListener, MainPageContract.IMainPageView {
    private MainPagerAdapter adapter;
    private LinearLayout mainLayout;
    private CustomViewPager mainViewpager;

    private TabLayout mainTablayout;
    private String[] title = new String[]{"首页", "添加", "我的"};
    private int[] tabDrawables = new int[]{R.drawable.home_index, R.drawable.home_index, R.drawable.home_index};
    private SparseArray<Fragment> mFragments = new SparseArray<>();
    //


    @Override
    public int getLayoutView() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        mainViewpager = findViewById(R.id.main_viewpager);
        mainTablayout = findViewById(R.id.main_tablayout);
        mainLayout = findViewById(R.id.main_layout);
        mainViewpager.setScanScroll(false);
        mFragments.append(0, new CellSearchFragment());//
        mFragments.append(1, new CellLocateFragment());//
        mFragments.append(2, new CellLocateFragment());//
        getToolbar().setVisibility(View.GONE);
        mBaseRootCol.setFitsSystemWindows(false);
        mainViewpager.setOffscreenPageLimit(3);
        initTab();
    }

    @Override
    public void initData() {
    }


    public void initTab() {
        adapter = new MainPagerAdapter(getSupportFragmentManager(), getApplicationContext(), title, tabDrawables,
                mFragments);
        mainViewpager.setAdapter(adapter);
        mainViewpager.setOffscreenPageLimit(title.length);
        /*viewpager切换监听，包含滑动点击两种*/
        mainViewpager.addOnPageChangeListener(this);
        for (int i = 0; i < title.length; i++) {
            TabLayout.Tab tab = mainTablayout.newTab();
            if (tab != null) {
                if (i == title.length - 1) {
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
        switch (tag) {
            case MainPageContract.UPLOAD_HISTORY:
                break;
            default:
                break;
        }
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
    public void onBackPressed() {
    }

    @Override
    public void onPause() {
        super.onPause();
    }


}
