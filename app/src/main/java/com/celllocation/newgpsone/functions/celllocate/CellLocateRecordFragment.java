package com.celllocation.newgpsone.functions.celllocate;


import android.widget.ListView;

import com.celllocation.R;
import com.celllocation.newgpsone.Utils.ObjectBox;
import com.celllocation.newgpsone.base.BaseAppFragment;
import com.celllocation.newgpsone.bean.CellHisData;
import com.celllocation.newgpsone.bean.CellHisData_;
import com.celllocation.newgpsone.functions.BaseFunctionActivity;
import com.celllocation.newgpsone.functions.MainPageContract;
import com.celllocation.newgpsone.functions.MainPagePresent;
import com.celllocation.newgpsone.older.MyHistoryDataListAdapter;

import java.util.List;

import io.objectbox.query.QueryBuilder;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2021-09-06 10:06
 * @UpdateUser: 更新者
 * @UpdateDate: 2021-09-06 10:06
 */
public class CellLocateRecordFragment extends BaseAppFragment<MainPagePresent> implements MainPageContract.IMainPageView {
    MyHistoryDataListAdapter m_ListAdapter;
    private ListView m_listHistoryData;
    @Override
    public MainPagePresent createPresenter() {
        return null;
    }

    @Override
    public void lazyLoad() {
        ((BaseFunctionActivity) getBaseActivity()).setTitleName("历史记录");
        List<CellHisData> arrays = ObjectBox.get().boxFor(CellHisData.class).query().order(CellHisData_.time,
                QueryBuilder.DESCENDING).build().find();
        m_ListAdapter = new MyHistoryDataListAdapter(mContext,arrays);
        m_listHistoryData.setAdapter(m_ListAdapter);

    }

    @Override
    public int getLayoutRes() {
        return R.layout.cell_historydata;
    }

    @Override
    public void initView() {
        m_listHistoryData = (ListView)getView(R.id.listHistoryData);

    }

    @Override
    public void initData() {

    }

    @Override
    public void onSuccess(String tag, Object o) {

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
