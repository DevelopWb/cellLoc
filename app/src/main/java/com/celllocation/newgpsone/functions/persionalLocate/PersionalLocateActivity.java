package com.celllocation.newgpsone.functions.persionalLocate;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;

import com.celllocation.R;
import com.celllocation.newgpsone.functions.BaseFunctionActivity;
import com.celllocation.newgpsone.functions.wifilocate.WifiLocateFragment;
import com.celllocation.newgpsone.functions.wifilocate.WifiLocateRecordFragment;
/**
 * @aouther tobato
 * @description 描述  人员定位
 * @date 2021/10/23 20:15
 */


public class PersionalLocateActivity extends BaseFunctionActivity {


    @Override
    public void initData() {
        super.initData();

        getTitleRightTv().setTextColor(ContextCompat.getColor(mContext,R.color.white));
        getTitleRightTv().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(getTextViewValue(getTitleRightTv()))) {
                    startActivity(new Intent(mContext,AddPersionActivity.class));
                }
            }
        });
    }

    @Override
    protected SparseArray<Fragment> getFragments() {
        SparseArray<Fragment> mFragments = new SparseArray<>();
        mFragments.append(0, new PersionalLocateFragment());//
        mFragments.append(1, new PersionalLocateRecordFragment());//
        return mFragments;
    }

    @Override
    protected int[] getTabDrawables() {
        return new int[]{R.drawable.cell_search, R.drawable.cell_search_record};
    }

    @Override
    protected String[] getTitleArrays() {
        return new String[]{"人员列表", "历史记录"};
    }
}
