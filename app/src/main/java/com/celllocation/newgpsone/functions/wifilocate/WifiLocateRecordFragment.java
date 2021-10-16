package com.celllocation.newgpsone.functions.wifilocate;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.celllocation.R;
import com.celllocation.newgpsone.Utils.LatLngTransFormUtil;
import com.celllocation.newgpsone.Utils.ObjectBox;
import com.celllocation.newgpsone.base.BaseRecyclerviewFragment;
import com.celllocation.newgpsone.bean.LatLngBean;
import com.celllocation.newgpsone.bean.LatLngBean_;
import com.celllocation.newgpsone.bean.WifiLocRecordBean;
import com.celllocation.newgpsone.bean.WifiLocRecordBean_;
import com.celllocation.newgpsone.functions.BaseFunctionActivity;
import com.celllocation.newgpsone.functions.LatlngTransform.LatLngAddrActivity;
import com.celllocation.newgpsone.functions.LatlngTransform.LatLngLocAdapter;
import com.celllocation.newgpsone.functions.MainPageContract;
import com.celllocation.newgpsone.functions.MainPagePresent;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.List;

import io.objectbox.query.QueryBuilder;

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
        ((BaseFunctionActivity) getBaseActivity()).setTitleName("历史记录");
        List<WifiLocRecordBean> arrays = ObjectBox.get().boxFor(WifiLocRecordBean.class).query().order(WifiLocRecordBean_.locTime,
                QueryBuilder.DESCENDING).build().find();
        if (adapter != null) {
            adapter.setNewData(arrays);
        }
    }


    @Override
    protected void freshlayoutOnLoadMore() {

    }

    @Override
    protected void freshlayoutOnRefresh() {

    }

    @Override
    protected BaseQuickAdapter getAdapter() {
        return new WifiLocHisAdapter(R.layout.wifi_loc_item);
    }

    @Override
    public void initData() {
        mSmartrefreshlayout.setEnableRefresh(false);
        mSmartrefreshlayout.setEnableLoadMore(false);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                WifiLocRecordBean bean = (WifiLocRecordBean) adapter.getData().get(position);
                WifiLocAddrActivity.startWifiLocAddrActivity(mContext,WifiLocAddrActivity.class,bean);
            }
        });
    }
}
