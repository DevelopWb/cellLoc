package com.celllocation.newgpsone.homepage.fragments;

import com.celllocation.R;
import com.celllocation.newgpsone.base.BaseAppFragment;
import com.celllocation.newgpsone.homepage.MainPageContract;
import com.celllocation.newgpsone.homepage.MainPagePresent;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2021-09-06 10:06
 * @UpdateUser: 更新者
 * @UpdateDate: 2021-09-06 10:06
 */
public class CellSearchFragment extends BaseAppFragment<MainPagePresent> implements MainPageContract.IMainPageView {
    @Override
    protected MainPagePresent createPresenter() {
        return null;
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.cellsearch_activity;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    public void onSuccess(String tag, Object o) {

    }
}
