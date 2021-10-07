package com.celllocation.newgpsone.functions.persionalLocate;

import com.celllocation.R;
import com.celllocation.newgpsone.base.BaseRecyclerviewFragment;
import com.celllocation.newgpsone.functions.BaseFunctionActivity;
import com.celllocation.newgpsone.functions.MainPageContract;
import com.celllocation.newgpsone.functions.MainPagePresent;
import com.celllocation.newgpsone.functions.wifilocate.WifiLocHisAdapter;
import com.celllocation.newgpsone.functions.wifilocate.WifiLocateActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;

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
        ((BaseFunctionActivity)getBaseActivity()).setTitleName("历史记录");

    }



    @Override
    protected void freshlayoutOnLoadMore() {

    }

    @Override
    protected void freshlayoutOnRefresh() {

    }

    @Override
    protected BaseQuickAdapter getAdapter() {
        return new PersionalLocHisAdapter(R.layout.new_layouthistorydatapos);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onSuccess(String tag, Object o) {

    }

}
