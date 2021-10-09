package com.celllocation.newgpsone.older;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
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
import com.celllocation.newgpsone.Utils.RegOperateTool;
import com.celllocation.newgpsone.base.BaseAppActivity;
import com.celllocation.newgpsone.bean.DataUtil;
import com.celllocation.newgpsone.bean.Position;
import com.juntai.disabled.basecomponent.mvp.BasePresenter;
import com.juntai.disabled.basecomponent.utils.ToastUtils;

import java.text.DecimalFormat;

public class SearchMapActivity extends BaseAppActivity implements
        OnGeocodeSearchListener, OnClickListener, OnMarkerClickListener,
        InfoWindowAdapter {

    private TextView address_tv;
    private MapView mapView;
    private AMap aMap;
    private GeocodeSearch geocoderSearch;
    private String addressName;
    private Marker regeoMarker;
    private LatLonPoint latLonPoint;
    private View infoWindow;
    private Position gpspos;
    String time;
    String la_, lo_;
    double la, lo;
    private RegOperateTool rot;

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
        setTitleName("基站位置");
        rot = new RegOperateTool(this);
        mapView = (MapView) findViewById(R.id.search_map);
        init();
        Intent intent = getIntent();
        gpspos = (Position) intent.getSerializableExtra("gpspos");
        getPosition();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
    }

    @Override
    public void initData() {

    }


    private void getPosition() {
        DecimalFormat df = new DecimalFormat("0.000000");

        la = gpspos.x;
        lo = gpspos.y;
        la_ = df.format(la);
        lo_ = df.format(lo);
        latLonPoint = new LatLonPoint(la, lo);
        getAddress(latLonPoint);

    }

    /**
     * 初始化AMap对象
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
            UiSettings uiSettings = aMap.getUiSettings();
            uiSettings.setZoomControlsEnabled(true);
            uiSettings.setZoomPosition(AMapOptions.ZOOM_POSITION_RIGHT_CENTER);

        }
        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);
    }

    private Bitmap getBitmap() {
        Bitmap bitmap = null;

        Resources res = SearchMapActivity.this.getResources();

        bitmap = BitmapFactory.decodeResource(res,
                R.drawable.singlepos_mapcenter);
        return bitmap;
    }

    private void setUpMap() {

        aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
        aMap.setInfoWindowAdapter(this);

    }

    /**
     * 响应逆地理编码
     */
    public void getAddress(final LatLonPoint latLonPoint) {
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
                LatLng latlng1 = new LatLng(la, lo);
                gpspos.address = addressName;
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
                        .snippet(
                                gpspos.address + "\n"
                                        + time + "\n" + gpspos.cid + "\n"
                                        + gpspos.lac + "\n" + la_ + "\n"
                                        + lo_ + "\n" + gpspos.nid + "")

                        .icon(BitmapDescriptorFactory.fromBitmap(DataUtil
                                .toRoundBitmap(getBitmap(),
                                        SearchMapActivity.this))).draggable(true)
                        .period(50);
                regeoMarker = aMap.addMarker(markerOption1);
                regeoMarker.setPosition(AMapUtil.convertToLatLng(latLonPoint));
                //减次
                if (RegOperateTool.isNumberLimit) {
                    rot.SetRegisCodeNumber(1);
                }
            } else {
                ToastUtils.toast(SearchMapActivity.this, "对不起，没有搜索到相关数据！");
            }
        } else if (rCode == 27) {
            ToastUtils.toast(SearchMapActivity.this, "搜索失败,请检查网络连接！");
        } else if (rCode == 32) {
            ToastUtils.toast(SearchMapActivity.this, "key验证无效！");
        } else {
            ToastUtils.toast(SearchMapActivity.this, "未知错误，请稍后重试!错误码为 " + rCode);
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
        infoWindow = getLayoutInflater().inflate(R.layout.newcell_selfpospopup,
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
    public void onClick(View arg0) {
        //  Auto-generated method stub

    }

    @Override
    public void onGeocodeSearched(GeocodeResult arg0, int arg1) {
        //  Auto-generated method stub

    }

    @Override
    public void onBackPressed() {
        PublicUtill.dianxin_mar = 0;
        super.onBackPressed();
    }

    public void render(final Marker marker, View view) {
        String col2 = gpspos.address;

        String col4;
        int cellid = gpspos.cid;
        if (cellid == -1) {
            col4 = "基站号： 未知";
        } else {
            if (PublicUtill.dianxin_mar == 100) {
                col4 = "基站号(BID)： " + String.valueOf(cellid);
            } else {
                col4 = "基站号： " + String.valueOf(cellid);
            }
        }

        String col5;
        int lac = gpspos.lac;
        if (PublicUtill.dianxin_mar == 100) {

            if (lac == -1) {
                col5 = "网络识别码： 未知";
            } else {
                col5 = "系统识别码(SID): " + String.valueOf(lac);
            }
            String col6 = "网络识别码(NID)：" + gpspos.nid + "";
            if (col6 != null || !col6.equals("")) {

                TextView nid_detail_tv = (TextView) view
                        .findViewById(R.id.nid_detail_tv);
                nid_detail_tv.setVisibility(view.VISIBLE);
                nid_detail_tv.setText(col6);
            }
        } else {
            if (lac == -1) {
                col5 = "扇区号： 未知";
            } else {
                col5 = "扇区号： " + String.valueOf(lac);
            }
        }
        String col3 = time;


        address_tv = (TextView) view.findViewById(R.id.new_address_tv);
        address_tv.setText(col2);

        TextView time_tv = (TextView) view.findViewById(R.id.new_time_tv);
        time_tv.setText(col3);

        TextView jizhan = (TextView) view.findViewById(R.id.new_jizhan);
        jizhan.setText(col4);

        TextView shanqu_tv = (TextView) view.findViewById(R.id.new_shanqu_tv);
        shanqu_tv.setText(col5);

        TextView lo_tv = (TextView) view.findViewById(R.id.new_lo_tv);
        lo_tv.setText("经度：" + lo_);

        TextView la_tv = (TextView) view.findViewById(R.id.new_la_tv);
        la_tv.setText("纬度：" + la_);

        ImageView selfpopupclose = (ImageView) view
                .findViewById(R.id.new_selfpopupclose);
        selfpopupclose.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                marker.hideInfoWindow();
            }
        });

        address_tv.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                TextView address_tv = (TextView) v;

                if (address_tv.getText().toString() != null
                        && !TextUtils.isEmpty(address_tv.getText().toString())) {
                    showAddrDialog();

                }

                return false;
            }
        });
    }

    /**
     * 地址
     */
    private void showAddrDialog() {
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
        bigtext.setText(address_tv.getText().toString());

    }

    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }



    @Override
    public void onSuccess(String tag, Object o) {

    }
}
