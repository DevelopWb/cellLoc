package com.celllocation.newgpsone.functions.wifilocate;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.celllocation.R;
import com.celllocation.newgpsone.Utils.ObjectBox;
import com.celllocation.newgpsone.base.BaseAppActivity;
import com.celllocation.newgpsone.Utils.DataUtil;
import com.celllocation.newgpsone.bean.WifiLocRecordBean;
import com.juntai.disabled.basecomponent.mvp.BasePresenter;

import java.text.DecimalFormat;

public class WifiLocAddrActivity extends BaseAppActivity implements
        View.OnClickListener, AMap.OnMarkerClickListener,
        AMap.InfoWindowAdapter {

    private MapView mapView;
    private AMap aMap;
    private View infoWindow;
    String time;
    private double lat;
    private double lng;

    public static String KEY_MAC = "key_loctype";
    /**
     * 位置
     */
    private TextView mInfoAddressTv;
    private ImageView mInfoCloseIv;
    /**
     * 经度：
     */
    private TextView mLngTv;
    /**
     * 纬度：
     */
    private TextView mLatTv;
    /**
     * 坐标类型：
     */
    private TextView mLatlngTypeTv;
    /**
     * 定位时间：
     */
    private TextView mTimeTv;
    private Marker mMarker;
    private String addressName;
    private WifiLocRecordBean recordBean;
    private String mac;

    public static void startWifiLocAddrActivity(Context mContext, Class cls, WifiLocRecordBean bean) {
        mContext.startActivity(new Intent(mContext, cls)
                .putExtra(PARCELABLE_KEY, bean)
        );
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    public int getLayoutView() {
        return R.layout.map_layout;
    }

    @Override
    public void initView() {
        if (getIntent() != null) {
            recordBean = getIntent().getParcelableExtra(PARCELABLE_KEY);
            mac = recordBean.getMac();
        }
        time = DataUtil.getDateToString(System
                .currentTimeMillis());
        setTitleName("wifi定位查询");
        mapView = (MapView) findViewById(R.id.search_map);
        init();
        lat = Double.parseDouble(new DecimalFormat("0.000000").format(recordBean.getLatitude()));
        lng = Double.parseDouble(new DecimalFormat("0.000000").format(recordBean.getLongitude()));
        ObjectBox.get().boxFor(WifiLocRecordBean.class).put(new WifiLocRecordBean(mac,lat,lng,time, recordBean.getAddr()));

        MarkerOptions markerOption1 = new MarkerOptions();
        markerOption1
                .anchor(0.5f, 0.5f)
                .position(new LatLng(lat,lng) )
                .snippet("")
                .icon(BitmapDescriptorFactory.fromBitmap(DataUtil
                        .toRoundBitmap(getBitmap(),mContext))).draggable(true)
                .period(50);
        mMarker = aMap.addMarker(markerOption1);
        mMarker.showInfoWindow();

    }


    @Override
    public void initData() {

    }


    /**
     * 初始化AMap对象
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
            aMap.setInfoWindowAdapter(this);
            UiSettings uiSettings = aMap.getUiSettings();
            uiSettings.setZoomControlsEnabled(true);
            uiSettings.setZoomPosition(AMapOptions.ZOOM_POSITION_RIGHT_CENTER);

        }
    }

    private Bitmap getBitmap() {
        return BitmapFactory.decodeResource(getResources(),
                R.drawable.singlepos_mapcenter);
    }


    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        infoWindow = getLayoutInflater().inflate(R.layout.wifi_addr_info,
                null);
        render(marker, infoWindow);
        return infoWindow;
    }

    @Override
    public boolean onMarkerClick(Marker arg0) {
        //  Auto-generated method stub
        return false;
    }


    public void render(final Marker marker, View view) {
        mInfoAddressTv = (TextView) view.findViewById(R.id.info_address_tv);
        mInfoAddressTv.setText(addressName);
        mInfoAddressTv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showAddrDialog(addressName);

                return true;
            }
        });
        mInfoCloseIv = (ImageView) view.findViewById(R.id.info_close_iv);
        mInfoCloseIv.setOnClickListener(this);
        mLngTv = (TextView) view.findViewById(R.id.lng_tv);
        mLngTv.append(String.valueOf(lng));
        mLatTv = (TextView) view.findViewById(R.id.lat_tv);
        mLatTv.append(String.valueOf(lat));

        mLatlngTypeTv = (TextView) view.findViewById(R.id.latlng_type_tv);
        mLatlngTypeTv.append(mac);
        mTimeTv = (TextView) view.findViewById(R.id.time_tv);
        mTimeTv.setText(time);
    }

    /**
     * 地址
     */
    private void showAddrDialog(String addr) {
        View v = LayoutInflater.from(this).inflate(R.layout.position_popup,
                null);
        final Dialog dialog_c = new Dialog(this, R.style.DialogStyle);
        dialog_c.setCanceledOnTouchOutside(true);
        dialog_c.show();
        Window window = dialog_c.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        window.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        lp.width = dip2px(this, 290); // 宽度
        lp.height = dip2px(this, 160); // 高度
        lp.alpha = 0.7f; // 透明度
        window.setAttributes(lp);
        window.setContentView(v);
        TextView bigtext = (TextView) v.findViewById(R.id.bigtext_tv);
        bigtext.setText(addr);

    }

    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    @Override
    public void onSuccess(String tag, Object o) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.info_close_iv:
                mMarker.hideInfoWindow();
                break;
            default:
                break;
        }
    }
}
