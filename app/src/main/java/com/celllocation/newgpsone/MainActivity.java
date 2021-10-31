package com.celllocation.newgpsone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.View;

import com.celllocation.R;
import com.celllocation.newgpsone.base.BaseAppActivity;
import com.celllocation.newgpsone.bean.HomePageMenuBean;
import com.celllocation.newgpsone.functions.LatlngTransform.LatLngTransformActivity;
import com.celllocation.newgpsone.functions.celllocate.CellLocateActivity;
import com.celllocation.newgpsone.functions.persionalLocate.PersionalLocateActivity;
import com.celllocation.newgpsone.functions.wifilocate.WifiLocateActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.juntai.disabled.basecomponent.mvp.BasePresenter;
import com.juntai.disabled.basecomponent.utils.LogUtil;
import com.juntai.disabled.basecomponent.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseAppActivity {

    private RecyclerView mRecyclerview;
    private MenuAdapter menuAdapter;
    public final static String HOMEPAGE_MENU_CELL_LOC = "基站定位";
    public final static String HOMEPAGE_MENU_WIFI_LOC = "WIFI定位";
    public final static String HOMEPAGE_MENU_PEOPLE_LOC = "人员定位";
    public final static String HOMEPAGE_MENU_LAT_LNG = "经纬度查询";
    private DynamicReceiver receiverSMS = new DynamicReceiver();
    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    public int getLayoutView() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        setTitleName("智能定位");
        mRecyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        menuAdapter = new MenuAdapter(R.layout.homepage_menu_item);
        GridLayoutManager manager = new GridLayoutManager(mContext, 2);
        mRecyclerview.setLayoutManager(manager);
        mRecyclerview.setAdapter(menuAdapter);
        menuAdapter.setNewData(getMenuList());
        menuAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                HomePageMenuBean menuBean = (HomePageMenuBean) adapter.getData().get(position);
                String menuName = menuBean.getMenuName();

                Intent intent = new Intent();
                switch (menuName) {
                    case HOMEPAGE_MENU_CELL_LOC:
                        intent.setClass(mContext, CellLocateActivity.class);
                        startActivity(intent);
                        break;
                    case HOMEPAGE_MENU_WIFI_LOC:
                        intent.setClass(mContext, WifiLocateActivity.class);
                        startActivity(intent);
                        break;
                    case HOMEPAGE_MENU_PEOPLE_LOC:
                        intent.setClass(mContext, PersionalLocateActivity.class);
                        startActivity(intent);
                        break;
                    case HOMEPAGE_MENU_LAT_LNG:
                        intent.setClass(mContext, LatLngTransformActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });

        IntentFilter filterSMS = new IntentFilter(
                "android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(receiverSMS, filterSMS);
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
                    if (smsMessage[n].getMessageBody().startsWith("#RDW")) {
                        LogUtil.d("收到短信内容8888888888");
                        final String num = smsMessage[n].getOriginatingAddress();
                        //收到短信指令后  开始解析经纬度  然后跳转到定位页面

                    }
                }

            }
        }

    }
    /**
     * 获取菜单列表
     *
     * @return
     */
    protected List<HomePageMenuBean> getMenuList() {

        List<HomePageMenuBean> arrays = new ArrayList<>();

        arrays.add(new HomePageMenuBean(HOMEPAGE_MENU_CELL_LOC,
                R.mipmap.home_menu_cell));
        arrays.add(new HomePageMenuBean(HOMEPAGE_MENU_WIFI_LOC,
                R.mipmap.home_menu_wifi));

        arrays.add(new HomePageMenuBean(HOMEPAGE_MENU_PEOPLE_LOC, R.mipmap.home_menu_people));
        arrays.add(new HomePageMenuBean(HOMEPAGE_MENU_LAT_LNG, R.mipmap.home_menu_latlng));

        return arrays;
    }

    @Override
    public void initData() {

    }


    @Override
    public void onSuccess(String tag, Object o) {

    }

}
