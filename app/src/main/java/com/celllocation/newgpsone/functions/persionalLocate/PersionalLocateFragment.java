package com.celllocation.newgpsone.functions.persionalLocate;

import android.view.View;

import com.celllocation.R;
import com.celllocation.newgpsone.Utils.ObjectBox;
import com.celllocation.newgpsone.base.BaseRecyclerviewFragment;
import com.celllocation.newgpsone.bean.PeopleLocateRecordBean;
import com.celllocation.newgpsone.bean.PeopleLocateUserBean;
import com.celllocation.newgpsone.functions.BaseFunctionActivity;
import com.celllocation.newgpsone.functions.MainPageContract;
import com.celllocation.newgpsone.functions.MainPagePresent;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.juntai.disabled.basecomponent.utils.CalendarUtil;

import java.util.Collections;
import java.util.List;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2021-09-06 10:06
 * @UpdateUser: 更新者
 * @UpdateDate: 2021-09-06 10:06
 */
public class PersionalLocateFragment extends BaseRecyclerviewFragment<MainPagePresent> implements MainPageContract.IMainPageView,
        View.OnClickListener {

    @Override
    public MainPagePresent createPresenter() {
        return null;
    }

    @Override
    public void lazyLoad() {
        ((BaseFunctionActivity) getBaseActivity()).setTitleName("人员列表");
        getBaseActivity(). getTitleRightTv().setText("添加");
    }


    @Override
    public void onResume() {
        super.onResume();

        List<PeopleLocateUserBean> arrays =
                ObjectBox.get().boxFor(PeopleLocateUserBean.class).getAll();
        Collections.reverse(arrays);
        adapter.setNewData(arrays);
    }

    @Override
    public void initView() {
        super.initView();

        mSmartrefreshlayout.setEnableLoadMore(false);
        mSmartrefreshlayout.setEnableRefresh(false);
        getBaseActivity().addDivider(true, mRecyclerview, false, false);
    }

    @Override
    protected void freshlayoutOnLoadMore() {

    }

    @Override
    protected void freshlayoutOnRefresh() {

    }

    @Override
    protected BaseQuickAdapter getAdapter() {
        return new PersionalLocUserListAdapter(R.layout.people_list_item);
    }

    @Override
    protected void initData() {
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                PeopleLocateUserBean userBean = (PeopleLocateUserBean) adapter.getData().get(position);
                PeopleLocateRecordBean recordBean = new PeopleLocateRecordBean();
                recordBean.setPeopleName(userBean.getPeopleName());
                recordBean.setPeopleMobile(userBean.getPeopleMobile());
                recordBean.setLat("35.12345465");
                recordBean.setLat("110.12345465");
                recordBean.setLocTime(CalendarUtil.getCurrentTime());
                ObjectBox.get().boxFor(PeopleLocateRecordBean.class).put(recordBean);
            }
        });
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onSuccess(String tag, Object o) {

    }
}
