package com.celllocation.newgpsone.functions.persionalLocate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.celllocation.R;
import com.celllocation.newgpsone.base.BaseAppActivity;
import com.juntai.disabled.basecomponent.mvp.BasePresenter;

/**
 * @aouther tobato
 * @description 描述  添加成员
 * @date 2021/10/23 20:21
 */
public class AddPersionActivity extends BaseAppActivity {

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    public int getLayoutView() {
        return R.layout.activity_add_persion;
    }

    @Override
    public void initView() {
        setTitleName("成员添加");
    }

    @Override
    public void initData() {

    }


    @Override
    public void onSuccess(String tag, Object o) {

    }
}
