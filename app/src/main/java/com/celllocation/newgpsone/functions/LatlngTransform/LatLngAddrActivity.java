package com.celllocation.newgpsone.functions.LatlngTransform;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.celllocation.R;
import com.celllocation.newgpsone.Utils.AMapUtil;
import com.celllocation.newgpsone.Utils.PublicUtill;
import com.celllocation.newgpsone.base.BaseAppActivity;
import com.celllocation.newgpsone.bean.DataUtil;
import com.celllocation.newgpsone.bean.LatLngBean;
import com.juntai.disabled.basecomponent.mvp.BasePresenter;
import com.juntai.disabled.basecomponent.utils.ToastUtils;

import java.text.DecimalFormat;

/**
 * @aouther tobato
 * @description 描述
 * @date 2021/10/10 21:25
 */
public class LatLngAddrActivity extends BaseAppActivity implements
        OnGeocodeSearchListener, OnClickListener, OnMarkerClickListener,
        InfoWindowAdapter {

    private MapView mapView;
    private AMap aMap;
    private GeocodeSearch geocoderSearch;
    private View infoWindow;
    LatLonPoint latLonPoint = null;
    String time;
    private double lat;
    private double lng;

    public static String KEY_LOC_TYPE = "key_loctype";
    public static String KEY_LAT_LNG_BEAN = "key_latlng";
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
    private int locType;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    public int getLayoutView() {
        return R.layout.search_map;
    }

    @Override
    public void initView() {
        time = DataUtil.getDateToString(System
                .currentTimeMillis());
        setTitleName("经纬度查询");
        mapView = (MapView) findViewById(R.id.search_map);
        init();
        Intent intent = getIntent();
        locType = intent.getIntExtra(KEY_LOC_TYPE, 0);
        LatLngBean latLngBean = intent.getParcelableExtra(KEY_LAT_LNG_BEAN);
        lat = Double.parseDouble(new DecimalFormat("0.000000").format(latLngBean.getWgLat()));
        lng =Double.parseDouble(new DecimalFormat("0.000000").format(latLngBean.getWgLon()));
        if (lat > 0 && lng > 0) {
            getAddress(new LatLonPoint(lat, lng));
        }


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
        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);
    }

    private Bitmap getBitmap() {
        return BitmapFactory.decodeResource(getResources(),
                R.drawable.singlepos_mapcenter);
    }


    /**
     * 响应逆地理编码
     */
    public void getAddress(final LatLonPoint latLonPoint) {
        this.latLonPoint = latLonPoint;
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,
                GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        geocoderSearch.getFromLocationAsyn(query);// 设置同步逆地理编码请求
    }

    /**
     * 逆地理编码回调
     */
    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
        if (rCode == 0) {
            if (result != null && result.getRegeocodeAddress() != null
                    && result.getRegeocodeAddress().getFormatAddress() != null) {
                addressName = result.getRegeocodeAddress().getFormatAddress()
                        + "附近";
                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        AMapUtil.convertToLatLng(latLonPoint), 16));
                LatLng latlng1 = new LatLng(lat, lng);
                // 在定位点画圆
                Circle circle = aMap.addCircle(new CircleOptions()
                        .center(latlng1).radius(100)
                        .strokeColor(Color.BLUE)
                        .fillColor(Color.parseColor("#3F003399"))
                        .strokeWidth(3));
                time = DataUtil.getDateToString(System
                        .currentTimeMillis());
                MarkerOptions markerOption1 = new MarkerOptions();
                markerOption1
                        .anchor(0.5f, 0.5f)
                        .position(latlng1)
                        .snippet("")
                        .icon(BitmapDescriptorFactory.fromBitmap(DataUtil
                                .toRoundBitmap(getBitmap(),
                                        LatLngAddrActivity.this))).draggable(true)
                        .period(50);
                mMarker = aMap.addMarker(markerOption1);
                mMarker.setPosition(AMapUtil.convertToLatLng(latLonPoint));
            } else {
                ToastUtils.toast(LatLngAddrActivity.this, "对不起，没有搜索到相关数据！");
            }
        } else if (rCode == 27) {
            ToastUtils.toast(LatLngAddrActivity.this, "搜索失败,请检查网络连接！");
        } else if (rCode == 32) {
            ToastUtils.toast(LatLngAddrActivity.this, "key验证无效！");
        } else {
            ToastUtils.toast(LatLngAddrActivity.this, "未知错误，请稍后重试!错误码为 " + rCode);
        }
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
        infoWindow = getLayoutInflater().inflate(R.layout.lat_lng_addr_info,
                null);
        render(marker, infoWindow);
        return infoWindow;
    }

    @Override
    public boolean onMarkerClick(Marker arg0) {
        //  Auto-generated method stub
        return false;
    }


    @Override
    public void onGeocodeSearched(GeocodeResult arg0, int arg1) {
        //  Auto-generated method stub

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
        if (0 == locType) {
            mLatlngTypeTv.append("GPS坐标");
        } else if (1 == locType) {
            mLatlngTypeTv.append("百度坐标");
        } else {
            mLatlngTypeTv.append("高德坐标");
        }
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
