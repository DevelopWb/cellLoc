package com.celllocation.newgpsone.functions.LatlngTransform;

import android.support.v4.app.Fragment;
import android.util.SparseArray;

import com.celllocation.R;
import com.celllocation.newgpsone.functions.BaseFunctionActivity;
/**
 * @aouther tobato
 * @description 描述 经纬度查询
 * @date 2021/10/9 21:37
 */
public class LatLngTransformActivity extends BaseFunctionActivity {


    @Override
    protected SparseArray<Fragment> getFragments() {
        SparseArray<Fragment> arrays = new SparseArray<>();
        arrays.append(0, new LatLngTransformFragment());
        arrays.append(1, new LatLngTransformRecordFragment());
        return arrays;
    }

    @Override
    protected int[] getTabDrawables() {
        return new int[]{R.drawable.latlng_search, R.drawable.cell_search_record};
    }

    @Override
    protected String[] getTitleArrays() {
        return new String[]{"经纬度查询", "历史记录"};
    }
}
