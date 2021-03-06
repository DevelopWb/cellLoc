package com.celllocation.newgpsone.functions.persionalLocate;

import android.content.Intent;
import android.view.View;

import com.celllocation.R;
import com.celllocation.newgpsone.Utils.DataUtil;
import com.celllocation.newgpsone.Utils.ObjectBox;
import com.celllocation.newgpsone.base.BaseRecyclerviewFragment;
import com.celllocation.newgpsone.bean.LatLngBean;
import com.celllocation.newgpsone.bean.PeopleLocateRecordBean;
import com.celllocation.newgpsone.bean.PeopleLocateRecordBean_;
import com.celllocation.newgpsone.bean.WifiLocRecordBean;
import com.celllocation.newgpsone.bean.WifiLocRecordBean_;
import com.celllocation.newgpsone.functions.BaseFunctionActivity;
import com.celllocation.newgpsone.functions.LatlngTransform.LatLngAddrActivity;
import com.celllocation.newgpsone.functions.MainPageContract;
import com.celllocation.newgpsone.functions.MainPagePresent;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import io.objectbox.query.QueryBuilder;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2021-09-06 10:06
 * @UpdateUser: 更新者
 * @UpdateDate: 2021-09-06 10:06
 */
public class PersionalLocateRecordFragment extends BaseRecyclerviewFragment<MainPagePresent> implements MainPageContract.IMainPageView {
    @Override
    public MainPagePresent createPresenter() {
        return null;
    }

    @Override
    public void lazyLoad() {
        ((BaseFunctionActivity) getBaseActivity()).setTitleName("历史记录");
        getBaseActivity().getTitleRightTv().setText("");
        List<PeopleLocateRecordBean> arrays =
                ObjectBox.get().boxFor(PeopleLocateRecordBean.class).query().order(PeopleLocateRecordBean_.locTime,
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
        return new PersionalLocHisAdapter(R.layout.people_loc_record_item);
    }
    @Override
    public void initView() {
        super.initView();

        mSmartrefreshlayout.setEnableLoadMore(false);
        mSmartrefreshlayout.setEnableRefresh(false);
    }
    @Override
    public void initData() {
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                PeopleLocateRecordBean recordBean = (PeopleLocateRecordBean) adapter.getData().get(position);
                LatLngBean latLngBean = new LatLngBean(Double.parseDouble(recordBean.getLat()),
                        Double.parseDouble(recordBean.getLng()), recordBean.getLocType(),
                        recordBean.getLocTime());
                startActivity(new Intent(mContext, LatLngAddrActivity.class)
                        .putExtra(LatLngAddrActivity.KEY_LOC_TYPE, latLngBean.getLocType())
                        .putExtra(LatLngAddrActivity.KEY_LAT_LNG_BEAN, latLngBean));
            }
        });
    }

    @Override
    public void onSuccess(String tag, Object o) {

    }

}
