package com.celllocation.newgpsone.functions.wifilocate;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;

import com.celllocation.R;
import com.celllocation.newgpsone.functions.BaseFunctionActivity;
import com.celllocation.newgpsone.functions.celllocate.CellLocateRecordFragment;
import com.celllocation.newgpsone.functions.celllocate.CellSearchFragment;

public class WifiLocateActivity extends BaseFunctionActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected SparseArray<Fragment> getFragments() {
        SparseArray<Fragment> mFragments = new SparseArray<>();
        mFragments.append(0, new WifiLocateFragment());//
        mFragments.append(1, new WifiLocateRecordFragment());//
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
