package com.celllocation.newgpsone.functions.celllocate;

import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;

import com.celllocation.R;
import com.celllocation.newgpsone.functions.BaseFunctionActivity;
import com.juntai.disabled.basecomponent.utils.DisplayUtil;

/**
 * @aouther tobato
 * @description 描述  基站查询定位
 *
 * @date 2021/9/6 22:06
 */
public class CellLocateActivity extends BaseFunctionActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mBaseRootCol.setFitsSystemWindows(false);
//        setMargin(getToolbar(),0,20,0,0);
    }
    /**
     * 配置view的margin属性
     */
    public void setMargin(View view, int left, int top, int right, int bottom) {
        left = DisplayUtil.dp2px(this, left);
        top = DisplayUtil.dp2px(this, top);
        right = DisplayUtil.dp2px(this, right);
        bottom = DisplayUtil.dp2px(this, bottom);
        AppBarLayout.LayoutParams layoutParams = new AppBarLayout.LayoutParams(view.getLayoutParams());
        layoutParams.setMargins(left, top, right, bottom);
        view.setLayoutParams(layoutParams);
    }
    @Override
    protected SparseArray<Fragment> getFragments() {
        SparseArray<Fragment> mFragments = new SparseArray<>();
        mFragments.append(0, new CellSearchFragment());//
        mFragments.append(1, new CellLocateRecordFragment());//
        return mFragments;
    }


    @Override
    protected int[] getTabDrawables() {
        return new int[]{R.drawable.cell_search, R.drawable.cell_search_record};
    }

    @Override
    protected String[] getTitleArrays() {
        return new String[]{"基站查询", "历史记录"};
    }
}
