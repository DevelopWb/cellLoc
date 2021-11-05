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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.celllocation.R;
import com.celllocation.newgpsone.Utils.ObjectBox;
import com.celllocation.newgpsone.base.BaseAppActivity;
import com.celllocation.newgpsone.bean.CellHisData_;
import com.celllocation.newgpsone.bean.CellHisData;
import com.celllocation.newgpsone.Utils.DataUtil;
import com.juntai.disabled.basecomponent.mvp.BasePresenter;

import java.text.DecimalFormat;
import java.util.List;
/**
 * @aouther tobato
 * @description 描述  update 更新  基站查询历史记录
 * @date 2021-09-10 16:45
 */

public class NewHistoryMapActivity extends BaseAppActivity implements
        AMap.OnMarkerClickListener, AMap.OnInfoWindowClickListener, AMap.InfoWindowAdapter {

    private MapView mapView;
    private AMap aMap;
    private LatLonPoint latLonPoint;
    private TextView address_tv;
    GeocodeAddress address;
    private View infoWindow;
    private Button mbtnleft, mbtnright;
    private GeocodeSearch geocoderSearch;
    private double x, y;
    private List<CellHisData> arrays;
    private CellHisData bean;
    private DecimalFormat df;
    private CellHisData[] arrs;
    private int position;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    public int getLayoutView() {
        return R.layout.newlayouthistorymap;
    }

    @Override
    public void initView() {
        setTitleName("查询记录");
        getNeedDatas();
        findView();
        df = new DecimalFormat("0.000000");

        setListener();
        init();
        getpositions();
        getposition();
    }

    @Override
    public void initData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapView.onCreate(savedInstanceState);// 此方法必须重写


    }

    private void getNeedDatas() {
//        TelephonyManager tm = (TelephonyManager) this
//                .getSystemService(Context.TELEPHONY_SERVICE);
//        phone = tm.getDeviceId();
        arrays = ObjectBox.get().boxFor(CellHisData.class).getAll();
        arrs = new CellHisData[arrays.size()];
        for (int i = 0; i < arrays.size(); i++) {
            arrs[i] = arrays.get(i);
        }
        Intent intent = getIntent();
        String time = intent.getStringExtra("ClickedTime");
        position = intent.getIntExtra("Position", 0);
        bean = (CellHisData) ObjectBox.get().boxFor(CellHisData.class).query().equal(CellHisData_.time,time).build().findUnique();
    }

    private void getpositions() {
        // 划线
        PolylineOptions mPolylineOptions = new PolylineOptions();
        mPolylineOptions.width(10).setDottedLine(true).geodesic(true)
                .color(Color.argb(255, 1, 1, 1));

        for (int i = 0; i < arrays.size(); i++) {
            CellHisData array_ = arrays.get(i);
            String addr = array_.getAddress();
            String time = array_.getTime();
            String lac = array_.getLac();
            String cid = array_.getCid();
            String nid = array_.getNid();
            if (nid==null) {
                nid = "";
            }
            String lat = array_.getLat();
            String lng = array_.getLng();
            double x = Double.parseDouble(lat);
            double y = Double.parseDouble(lng);
            latLonPoint = new LatLonPoint(x, y);
            String xx = df.format(x);
            String yy = df.format(y);
            latLonPoint = new LatLonPoint(x, y);
            LatLng latlng = new LatLng(x, y);
            MarkerOptions markerOption = new MarkerOptions()
                    .anchor(0.5f, 0.5f)
                    .position(latlng)
                    .snippet(lac + "\n" + cid + "\n" + nid + "\n" + time + "\n" + addr + "\n" + xx + "\n" + yy)
                    .icon(BitmapDescriptorFactory.fromBitmap(DataUtil
                            .toRoundBitmap(getBitmap(),
                                    NewHistoryMapActivity.this)))
                    .draggable(true).period(50);
            aMap.addMarker(markerOption);
            mPolylineOptions.add(latlng);
            aMap.addPolyline(mPolylineOptions);
        }
    }

    private void getposition() {
        String addr = bean.getAddress();
        String time = bean.getTime();
        String lac = bean.getLac();
        String cid = bean.getCid();
        String nid = bean.getNid();
        if (nid==null) {
            nid = "";
        }
        String lat = bean.getLat();
        String lng = bean.getLng();
        x = Double.parseDouble(lat);
        y = Double.parseDouble(lng);
        String xx = df.format(x);
        String yy = df.format(y);
        LatLng latlng1 = new LatLng(x, y);
        MarkerOptions markerOption1 = new MarkerOptions()
                .anchor(0.5f, 0.5f)
                .position(latlng1)
                .snippet(lac + "\n" + cid + "\n" + nid + "\n" + time + "\n" + addr + "\n" + xx + "\n" + yy)
                .icon(BitmapDescriptorFactory.fromBitmap(DataUtil
                        .toRoundBitmap(getBitmap(), NewHistoryMapActivity.this)))
                .draggable(true).period(50);
        Marker regeoMarker = aMap.addMarker(markerOption1);
        regeoMarker.showInfoWindow();// 设置默认显示一个infowinfow
        aMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(latlng1, 16, 0, 0)));

    }

    private void setListener() {
        mbtnleft.setOnClickListener(new OnClickListener() {

                                        @Override
                                        public void onClick(View arg0) {
                                            if (position > 0) {
                                                position = position - 1;
                                                CellHisData bean_pos = arrs[position];
                                                String addr = bean_pos.getAddress();
                                                String time = bean_pos.getTime();
                                                String lac = bean_pos.getLac();
                                                String cid = bean_pos.getCid();
                                                String nid = bean_pos.getNid();
                                                if (nid==null) {
                                                    nid = "";
                                                }
                                                String lat = bean_pos.getLat();
                                                String lng = bean_pos.getLng();
                                                double a = Double.parseDouble(lat);
                                                double b = Double.parseDouble(lng);
                                                String aa = df.format(a);
                                                String bb = df.format(b);
                                                LatLng latlng1 = new LatLng(a, b);
                                                MarkerOptions markerOption1 = new MarkerOptions()
                                                        .anchor(0.5f, 0.5f)
                                                        .position(latlng1)
                                                        .snippet(lac + "\n" + cid + "\n" + nid + "\n" + time + "\n" + addr + "\n" + aa + "\n" + bb)
                                                        .icon(BitmapDescriptorFactory.fromBitmap(DataUtil
                                                                .toRoundBitmap(getBitmap(),
                                                                        NewHistoryMapActivity.this)))
                                                        .draggable(true).period(50);
                                                Marker regeoMarker = aMap.addMarker(markerOption1);
                                                regeoMarker.showInfoWindow();// 设置默认显示一个infowinfow
                                                aMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(latlng1, 16, 0, 0)));
                                                if (position == 0) {
                                                    mbtnleft.setBackgroundResource(R.drawable.bt7);
                                                    mbtnright.setBackgroundResource(R.drawable.bt3);
                                                } else {
                                                    mbtnright.setBackgroundResource(R.drawable.bt3);
                                                }

                                            } else if (position == 0)

                                            {
                                                return;
                                            }
                                        }
                                    }
        );
        mbtnright.setOnClickListener(new

                                             OnClickListener() {

                                                 @Override
                                                 public void onClick(View arg0) {
                                                     if (position < arrs.length - 1) {

                                                         position = position + 1;

                                                         CellHisData bean_next = arrs[position];
                                                         String addr = bean_next.getAddress();
                                                         String time = bean_next.getTime();
                                                         String lac = bean_next.getLac();
                                                         String cid = bean_next.getCid();
                                                         String nid = bean_next.getNid();
                                                         if (nid==null) {
                                                             nid = "";
                                                         }
                                                         String lat = bean_next.getLat();
                                                         String lng = bean_next.getLng();
                                                         double a = Double.parseDouble(lat);
                                                         double b = Double.parseDouble(lng);
                                                         String aa = df.format(a);
                                                         String bb = df.format(b);
                                                         LatLng latlng1 = new LatLng(a, b);
                                                         MarkerOptions markerOption1 = new MarkerOptions()
                                                                 .anchor(0.5f, 0.5f)
                                                                 .position(latlng1)
                                                                 .snippet(lac + "\n" + cid + "\n" + nid + "\n" + time + "\n" + addr + "\n" + aa + "\n" + bb)
                                                                 .icon(BitmapDescriptorFactory.fromBitmap(DataUtil
                                                                         .toRoundBitmap(getBitmap(),
                                                                                 NewHistoryMapActivity.this)))
                                                                 .draggable(true).period(50);
                                                         Marker regeoMarker = aMap.addMarker(markerOption1);
                                                         regeoMarker.showInfoWindow();// 设置默认显示一个infowinfow
                                                         aMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(latlng1, 16, 0, 0)));
                                                         if (position == arrs.length - 1) {
                                                             mbtnright.setBackgroundResource(R.drawable.bt4);
                                                             mbtnleft.setBackgroundResource(R.drawable.bt6);
                                                         } else {
                                                             mbtnleft.setBackgroundResource(R.drawable.bt6);
                                                         }
                                                     } else if (position == arrs.length - 1) {
                                                         return;
                                                     }
                                                 }
                                             }

        );
    }

    private void findView() {
        mbtnleft = (Button) findViewById(R.id.btnHistoryDataMapLeft);
        mbtnright = (Button) findViewById(R.id.btnHistoryDataMapRight);
        if (position == 0) {
            mbtnleft.setBackgroundResource(R.drawable.bt7);
            if (arrays.size() == 1) {
                mbtnright.setBackgroundResource(R.drawable.bt4);
            }
        } else if (position == arrays.size() - 1) {
            mbtnright.setBackgroundResource(R.drawable.bt4);
            mbtnleft.setBackgroundResource(R.drawable.bt6);
        }
        mapView = (MapView) findViewById(R.id.new_his_map);

    }

    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();

            setUpMap();
            UiSettings uiSettings = aMap.getUiSettings();
            uiSettings.setZoomControlsEnabled(true);
            uiSettings.setZoomPosition(AMapOptions.ZOOM_POSITION_RIGHT_CENTER);

        }
    }

    private void setUpMap() {

        aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
        aMap.setInfoWindowAdapter(this);

    }

    @Override
    public View getInfoContents(Marker arg0) {
        return null;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
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

    private Bitmap getBitmap() {
        Bitmap bitmap = null;

        Resources res = NewHistoryMapActivity.this.getResources();

        bitmap = BitmapFactory.decodeResource(res, R.drawable.new_ni);
        return bitmap;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        infoWindow = getLayoutInflater().inflate(R.layout.newcell_map_view,
                null);

        render(marker, infoWindow);
        return infoWindow;
    }

    public void render(final Marker marker, View view) {
        String snippet = marker.getSnippet();
        String[] snippets = snippet.split("\n");
        String nid = snippets[2];
        TextView cell_his_nid_tv = (TextView) view.findViewById(R.id.cell_his_nid_tv);
        TextView lac = (TextView) view.findViewById(R.id.cell_his_shanqu_tv);
        TextView cid = (TextView) view.findViewById(R.id.cell_his_jizhan_tv);
        if (!TextUtils.isEmpty(nid)) {
            cid.setText("基站号(BID)：" + snippets[1]);
            cell_his_nid_tv.setVisibility(View.VISIBLE);
            lac.setText("系统识别码(SID)：" + snippets[0]);
            cell_his_nid_tv.setText("网络识别码(NID)："+snippets[2]);
        }else{
            cid.setText("基站号(CELLID)：" + snippets[1]);
            lac.setText("扇区号(LAC)：" + snippets[0]);
            cell_his_nid_tv.setVisibility(View.GONE);
        }
        TextView lng = (TextView) view.findViewById(R.id.cell_his_lng);
        lng.setText("经度：" + snippets[6]);
        TextView lat = (TextView) view.findViewById(R.id.cell_his_lat);
        lat.setText("纬度：" + snippets[5]);




        address_tv = (TextView) view.findViewById(R.id.cell_his_address_tv);
        address_tv.setText("地址：" + snippets[4]);

        TextView time_tv = (TextView) view.findViewById(R.id.cell_his_time_tv);
        time_tv.setText(snippets[3]);

        ImageView selfpopupclose = (ImageView) view.findViewById(R.id.cell_his_close_iv);
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
                    showLoginDialog();

                }

                return false;
            }
        });
    }

    private void showLoginDialog() {
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
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public void onSuccess(String tag, Object o) {

    }
}
