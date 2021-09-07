package com.celllocation.newgpsone.functions.wifilocate;

import android.app.Dialog;
import android.widget.ListView;

import com.celllocation.R;
import com.celllocation.newgpsone.Utils.PubUtill;
import com.celllocation.newgpsone.base.BaseAppFragment;
import com.celllocation.newgpsone.base.BaseRecyclerviewFragment;
import com.celllocation.newgpsone.bean.CellHisData;
import com.celllocation.newgpsone.database.DataHelper;
import com.celllocation.newgpsone.functions.MainPageContract;
import com.celllocation.newgpsone.functions.MainPagePresent;
import com.celllocation.newgpsone.functions.celllocate.CellLocateActivity;
import com.celllocation.newgpsone.older.MyHistoryDataListAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2021-09-06 10:06
 * @UpdateUser: 更新者
 * @UpdateDate: 2021-09-06 10:06
 */
public class WifiLocateRecordFragment extends BaseRecyclerviewFragment<MainPagePresent> implements MainPageContract.IMainPageView {
    @Override
    public MainPagePresent createPresenter() {
        return null;
    }

    @Override
    public void lazyLoad() {
        ((WifiLocateActivity)getBaseActivity()).setTitleName("历史记录");

    }



    @Override
    protected void freshlayoutOnLoadMore() {

    }

    @Override
    protected void freshlayoutOnRefresh() {

    }

    @Override
    protected BaseQuickAdapter getAdapter() {
        return new WifiLocHisAdapter(R.layout.new_layouthistorydatapos);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onSuccess(String tag, Object o) {

    }

}
