package com.celllocation.newgpsone.functions.persionalLocate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.view.View;

import com.celllocation.R;
import com.celllocation.newgpsone.Utils.DataUtil;
import com.celllocation.newgpsone.Utils.ObjectBox;
import com.celllocation.newgpsone.base.BaseRecyclerviewFragment;
import com.celllocation.newgpsone.bean.LatLngBean;
import com.celllocation.newgpsone.bean.PeopleLocateRecordBean;
import com.celllocation.newgpsone.bean.PeopleLocateUserBean;
import com.celllocation.newgpsone.functions.BaseFunctionActivity;
import com.celllocation.newgpsone.functions.LatlngTransform.LatLngAddrActivity;
import com.celllocation.newgpsone.functions.MainPageContract;
import com.celllocation.newgpsone.functions.MainPagePresent;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.juntai.disabled.basecomponent.utils.CalendarUtil;
import com.juntai.disabled.basecomponent.utils.LogUtil;

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
    private DynamicReceiver receiverSMS = new DynamicReceiver();
    private PeopleLocateUserBean userBean;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter filterSMS = new IntentFilter(
                "android.provider.Telephony.SMS_RECEIVED");
        mContext.registerReceiver(receiverSMS, filterSMS);
    }

    // 对收到的短信内容进行提取
    public class DynamicReceiver extends BroadcastReceiver {
        public static final String SMS_ACTION = "android.provider.Telephony.SMS_RECEIVED";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (SMS_ACTION.equals(action)) {
                Bundle bundle = intent.getExtras();
                Object messages[] = (Object[]) bundle.get("pdus");
                final SmsMessage smsMessage[] = new SmsMessage[messages.length];

                for (int n = 0; n < messages.length; n++) {
                    smsMessage[n] = SmsMessage
                            .createFromPdu((byte[]) messages[n]);
                    String body = smsMessage[n].getMessageBody();
                    if (body.startsWith("DW")|| body.startsWith("RDW")) {
                        this.abortBroadcast();
                    }
                    if (body.startsWith("#RDW,")) {
                        LogUtil.d("收到短信内容8888888888");
                        String[] latlngs = body.split(",");
                        final String num = smsMessage[n].getOriginatingAddress();
                        //收到短信指令后  开始解析经纬度  然后跳转到定位页面
                        LatLngBean latLngBean = new LatLngBean(Double.parseDouble(latlngs[1]),
                                Double.parseDouble(latlngs[2]), 3,
                                DataUtil.getDateToString(System
                                        .currentTimeMillis()));
                        startActivity(new Intent(mContext, LatLngAddrActivity.class)
                                .putExtra(LatLngAddrActivity.KEY_LOC_TYPE, latLngBean.getLocType())
                                .putExtra(LatLngAddrActivity.KEY_LAT_LNG_BEAN, latLngBean));
                        PeopleLocateRecordBean recordBean = new PeopleLocateRecordBean();
                        recordBean.setPeopleName(userBean.getPeopleName());
                        recordBean.setPeopleMobile(userBean.getPeopleMobile());
                        recordBean.setLat(latlngs[1]);
                        recordBean.setLocType(3);
                        recordBean.setLng(latlngs[2]);
                        recordBean.setLocTime(CalendarUtil.getCurrentTime());
                        ObjectBox.get().boxFor(PeopleLocateRecordBean.class).put(recordBean);

                    }
                }

            }
        }

    }

    @Override
    public MainPagePresent createPresenter() {
        return null;
    }

    @Override
    public void lazyLoad() {
        ((BaseFunctionActivity) getBaseActivity()).setTitleName("人员列表");
        getBaseActivity().getTitleRightTv().setText("添加");
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

                userBean = (PeopleLocateUserBean) adapter.getData().get(position);

                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(userBean.getPeopleMobile(), null, "#DW", null,
                        null);

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
