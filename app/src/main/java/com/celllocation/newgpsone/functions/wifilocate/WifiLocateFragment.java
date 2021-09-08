package com.celllocation.newgpsone.functions.wifilocate;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.Marker;
import com.celllocation.R;
import com.celllocation.newgpsone.Utils.RegOperateTool;
import com.celllocation.newgpsone.base.BaseAppFragment;
import com.celllocation.newgpsone.database.DataHelper;
import com.celllocation.newgpsone.functions.MainPageContract;
import com.celllocation.newgpsone.functions.MainPagePresent;
import com.juntai.disabled.basecomponent.utils.DisplayUtil;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2021-09-06 10:06
 * @UpdateUser: 更新者
 * @UpdateDate: 2021-09-06 10:06
 */
public class WifiLocateFragment extends BaseAppFragment<MainPagePresent> implements MainPageContract.IMainPageView, View.OnClickListener, AMap.OnMarkerClickListener,
        AMap.InfoWindowAdapter {
    private MapView mapView;
    private AMap aMap;
    private TextView address_tv;
    private View infoWindow;
    private DataHelper helper;
    private String phoneNum;

    private RegOperateTool regOperateTool;
    @Override
    public MainPagePresent createPresenter() {
        return null;
    }

    @Override
    public void lazyLoad() {
    }

    @Override
    public int getLayoutRes() {
        return R.layout.cell_locate_oneselfe_layout;
    }

    @Override
    public void initView() {
        mapView = (MapView) getView(R.id.map);
//        mapView.onCreate(savedInstanceState);// 此方法必须重写
        if (aMap == null) {
            aMap = mapView.getMap();
            aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
            aMap.setInfoWindowAdapter(this);
            UiSettings uiSettings = aMap.getUiSettings();
            uiSettings.setZoomControlsEnabled(true);
            uiSettings.setZoomPosition(AMapOptions.ZOOM_POSITION_RIGHT_CENTER);

        }
    }

    @Override
    protected void initData() {

    }


    /**
     * 方法必须重写
     */
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();


    }


    /**
     * 方法必须重写
     */
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onDestroy() {

        super.onDestroy();
        mapView.onDestroy();
    }


    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        infoWindow = getLayoutInflater().inflate(R.layout.newcell_selfpospopup,
                null);

        render(marker, infoWindow);
        return infoWindow;
    }

    @Override
    public boolean onMarkerClick(Marker arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub

    }


    public void render(final Marker marker, View view) {
        String col4 = "", col5 = "";
        String snippet = marker.getSnippet();
        String[] snippets = snippet.split("\n");
        String lat = snippets[1];
        String lng = snippets[2];
        String lac = snippets[3];
        String cid = snippets[4];
        String nid = snippets[5];
        String addr = snippets[6];
        String time = snippets[8];
        if (Integer.parseInt(cid) == -1) {
            col4 = "基站号： 未知";
        } else {
            col4 = "基站号： " + cid;
        }
        if (nid!=null&&!TextUtils.isEmpty(nid)) {

            if (Integer.parseInt(cid) == -1) {
                col5 = "网络识别码： 未知";
            } else {
                col5 = "系统识别码(SID): " + String.valueOf(lac);
            }
            String col6 = "网络识别码(NID)：" + nid;
            if (col6 != null || !col6.equals("")) {
                TextView nid_detail_tv = (TextView) view
                        .findViewById(R.id.nid_detail_tv);
                nid_detail_tv.setVisibility(view.VISIBLE);
                nid_detail_tv.setText(col6);
            }
        } else {
            if (Integer.parseInt(cid) == -1) {
                col5 = "扇区号： 未知";
            } else {
                col5 = "扇区号： " + String.valueOf(lac);
            }
        }
        String col3 = time;

        address_tv = (TextView) view.findViewById(R.id.new_address_tv);
        address_tv.setText(addr);

        TextView time_tv = (TextView) view.findViewById(R.id.new_time_tv);
        time_tv.setText(col3);

        TextView jizhan = (TextView) view.findViewById(R.id.new_jizhan);
        jizhan.setText(col4);

        TextView shanqu_tv = (TextView) view.findViewById(R.id.new_shanqu_tv);
        shanqu_tv.setText(col5);

        TextView lo_tv = (TextView) view.findViewById(R.id.new_lo_tv);
        lo_tv.setText("经度：" + lng);

        TextView la_tv = (TextView) view.findViewById(R.id.new_la_tv);
        la_tv.setText("纬度：" + lat);
        ImageView selfpopupclose = (ImageView) view
                .findViewById(R.id.new_selfpopupclose);
        selfpopupclose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                marker.hideInfoWindow();
            }
        });

        address_tv.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                TextView address_tv = (TextView) v;

                if (address_tv.getText().toString() != null
                        && !TextUtils.isEmpty(address_tv.getText().toString())) {
                    showLoginDialog();

                }

                return false;
            }
        });
    }

    private void showLoginDialog() {
        View v = LayoutInflater.from(mContext).inflate(R.layout.position_popup,
                null);
        final Dialog dialog_c = new Dialog(mContext, R.style.DialogStyle);
        dialog_c.setCanceledOnTouchOutside(true);
        dialog_c.show();
        Window window = dialog_c.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        window.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        lp.width = DisplayUtil.dp2px(mContext, 290); // 宽度
        lp.height = DisplayUtil.dp2px(mContext, 160); // 高度
        lp.alpha = 0.7f; // 透明度
        window.setAttributes(lp);
        window.setContentView(v);
        TextView bigtext = (TextView) v.findViewById(R.id.bigtext_tv);
        bigtext.setText(address_tv.getText().toString());

    }

    @Override
    public void onSuccess(String tag, Object o) {

    }
}
