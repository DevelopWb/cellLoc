package com.celllocation.newgpsone.functions.celllocate;

import android.app.Dialog;
import android.widget.ListView;

import com.celllocation.R;
import com.celllocation.newgpsone.Utils.PubUtill;
import com.celllocation.newgpsone.base.BaseAppFragment;
import com.celllocation.newgpsone.bean.CellHisData;
import com.celllocation.newgpsone.database.DataHelper;
import com.celllocation.newgpsone.functions.MainPageContract;
import com.celllocation.newgpsone.functions.MainPagePresent;
import com.celllocation.newgpsone.older.MyHistoryDataListAdapter;

import java.util.List;

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
    private Dialog dialog;
    private DataHelper helper;
    private String phone;
    @Override
    public MainPagePresent createPresenter() {
        return null;
    }

    @Override
    public void lazyLoad() {
        ((CellLocateActivity)getBaseActivity()).setTitleName("历史记录");

    }

    @Override
    public int getLayoutRes() {
        return R.layout.cell_historydata;
    }

    @Override
    public void initView() {
        helper = new DataHelper(mContext);
        dialog = new Dialog(mContext, R.style.DialogStyle);
        dialog.setContentView(R.layout.dialog);
        dialog.show();
        phone = PubUtill.getIMEIDeviceId(mContext);
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
        List<CellHisData> arrays = helper.GetCellHisDatas(phone);
        m_ListAdapter = new MyHistoryDataListAdapter(mContext,arrays);
        m_listHistoryData.setAdapter(m_ListAdapter);
        dialog.dismiss();
    }

    @Override
    public void onDestroy() {
        if (dialog!=null) {
            dialog.dismiss();
            dialog=null;
        }
        super.onDestroy();
    }
}
