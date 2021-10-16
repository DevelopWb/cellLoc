package com.celllocation.newgpsone.functions.LatlngTransform;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.celllocation.R;
import com.celllocation.newgpsone.Utils.LatLngTransFormUtil;
import com.celllocation.newgpsone.Utils.ObjectBox;
import com.celllocation.newgpsone.base.BaseAppFragment;
import com.celllocation.newgpsone.base.BaseRecyclerviewFragment;
import com.celllocation.newgpsone.bean.LatLngBean;
import com.celllocation.newgpsone.bean.LatLngBean_;
import com.celllocation.newgpsone.functions.BaseFunctionActivity;
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
public class LatLngTransformRecordFragment extends BaseRecyclerviewFragment<MainPagePresent> implements MainPageContract.IMainPageView {

    @Override
    public MainPagePresent createPresenter() {
        return null;
    }

    @Override
    public void lazyLoad() {
        ((BaseFunctionActivity) getBaseActivity()).setTitleName("历史记录");
        List<LatLngBean> arrays = ObjectBox.get().boxFor(LatLngBean.class).query().order(LatLngBean_.time,
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
        return new LatLngLocAdapter(R.layout.latlng_loc_item);
    }

    @Override
    public void initData() {
        mSmartrefreshlayout.setEnableRefresh(false);
        mSmartrefreshlayout.setEnableLoadMore(false);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                LatLngBean bean = (LatLngBean) adapter.getData().get(position);
                LatLngBean latLngBean = null;
                switch (bean.getLocType()) {
                    case 0:
                        latLngBean = LatLngTransFormUtil.gps84_To_Gcj02(bean.getWgLat(), bean.getWgLon());
                        break;
                    case 1:
                        latLngBean = LatLngTransFormUtil.bd09_To_Gcj02(bean.getWgLat(), bean.getWgLon());
                        break;
                    case 2:
                        latLngBean = new LatLngBean(bean.getWgLat(), bean.getWgLon());
                        break;
                    default:
                        break;
                }


                startActivity(new Intent(mContext, LatLngAddrActivity.class)
                        .putExtra(LatLngAddrActivity.KEY_LOC_TYPE, bean.getLocType())
                        .putExtra(LatLngAddrActivity.KEY_LAT_LNG_BEAN, latLngBean));
            }
        });
    }


}
