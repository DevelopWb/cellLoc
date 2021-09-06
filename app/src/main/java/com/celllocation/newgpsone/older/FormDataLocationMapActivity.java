package com.celllocation.newgpsone.older;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.celllocation.R;
import com.celllocation.newgpsone.database.DataHelper;
import com.celllocation.newgpsone.bean.PhoneNO;
import com.celllocation.newgpsone.bean.DataUtil;
import com.celllocation.newgpsone.bean.PeopleLocation;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2016/11/22.
 */

public class FormDataLocationMapActivity extends Activity implements
        AMap.OnMarkerClickListener,
        AMap.OnInfoWindowClickListener, AMap.InfoWindowAdapter {
    private MapView mapView;
    private AMap aMap;
    private DataHelper helper;
    private RequestQueue mRequestQueue;

    private String Tag = "FormData";
    private Dialog dialog;
    private int size;
    private String importTime;
    private String phoneNum;
    private DecimalFormat df;
    private Bitmap bitmap_onLine;
    private View infoWindow;
    private List<PeopleLocation> peopleLocations = new ArrayList<PeopleLocation>();
    private android.os.Handler mHandler = new android.os.Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (!peopleLocations.isEmpty()) {
                        MarkersToView(peopleLocations);
                    }

                    break;
                default:
                    break;
            }


            super.handleMessage(msg);
        }
    };
    private TextView address_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.form_location);
        mapView = (MapView) findViewById(R.id.form_map);
        bitmap_onLine = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.new_ni);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        init();
        helper = new DataHelper(this);
        dialog = new Dialog(this, R.style.DialogStyle);
        dialog.setContentView(R.layout.dialog);
        dialog.show();
        Intent intent = getIntent();
        importTime = intent.getStringExtra("ImportTime");
        phoneNum = intent.getStringExtra("PhoneNum");
        df = new DecimalFormat("0.000000");
        MarkersToMap();
    }

    private void MarkersToMap() {
        List<PeopleLocation> arrays = helper.GetPeopleLocation(importTime);
        if (arrays != null && arrays.size() > 0) {
            for (int i = 0; i < arrays.size(); i++) {
                PeopleLocation pl = arrays.get(i);
                String lat = pl.getLat();
                String lng = pl.getLng();
                String address = pl.getAddress();
                String lac = pl.getLac();
                String cid = pl.getCid();
                String nid = pl.getNid();
                String time = pl.getTime();
                double x = Double.parseDouble(lat);
                double y = Double.parseDouble(lng);
//                double[] latlng = {x, y};
//                GpsCorrect.transform(x, y, latlng);
//                double x1 = latlng[0];
//                double y1 = latlng[1];
                String xx = df.format(x);
                String yy = df.format(y);
                LatLng latlng_ = new LatLng(x, y);
                Bitmap bitmap = DataUtil.toRoundBitmap(
                        bitmap_onLine, FormDataLocationMapActivity.this);
                if (nid != null) {
                    MarkerOptions markerOption1 = new MarkerOptions()
                            .anchor(0.5f, 0.5f)
                            .position(latlng_)
                            .snippet(xx + "\n" + yy + "\n" + address + "\n" + lac + "\n" + cid + "\n" + time
                                    + "\n" + nid)
                            .icon(BitmapDescriptorFactory
                                    .fromBitmap(bitmap))
                            .draggable(true).period(50);
                    aMap.addMarker(markerOption1);
                } else {
                    MarkerOptions markerOption1 = new MarkerOptions()
                            .anchor(0.5f, 0.5f)
                            .position(latlng_)
                            .snippet(xx + "\n" + yy + "\n" + address + "\n" + lac + "\n" + cid + "\n" + time
                            )
                            .icon(BitmapDescriptorFactory
                                    .fromBitmap(bitmap))
                            .draggable(true).period(50);
                    aMap.addMarker(markerOption1);
                }
                peopleLocations.add(pl);

            }
            mHandler.sendEmptyMessageDelayed(0, 500);

        } else {
            GetCellInfos();
        }
    }

    private void MarkersToView(List<PeopleLocation> arrays) {
        List<LatLng> latlngs = new ArrayList<LatLng>();
        if (arrays.size() > 0) {
            for (int i = 0; i < arrays.size(); i++) {
                PeopleLocation bean = arrays.get(i);
                LatLng latlng = new LatLng(Double.parseDouble(bean.getLat()),
                        Double.parseDouble(bean.getLng()));
                latlngs.add(latlng);

            }
        }

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng latlng : latlngs) {
            builder.include(latlng);
        }
        LatLngBounds bounds_ = builder.build();
        if (latlngs.size() == 1) {

            // 设中心点和zoom
            aMap.moveCamera(CameraUpdateFactory
                    .newCameraPosition(new CameraPosition(latlngs.get(0), 15,
                            0, 0)));
        } else {
            aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds_, 300));
        }
        if (dialog!=null) {
            dialog.dismiss();
            dialog = null;
        }


    }

    public String matchesPhoneNumber(String phone_number) {

        String cm = "^((13[4-9])|(147)|(15[0-2,7-9])|(18[2-3,7-8]))\\d{8}$";
        String cu = "^((13[0-2])|(145)|(15[5-6])|(186))\\d{8}$";
        String ct = "^((133)|(153)|(18[0,9]))\\d{8}$";
        String vcm = "^1705\\d{7}$";
        String vcu = "^1709\\d{7}$";
        String vct = "^1700\\d{7}$";

        if (phone_number.matches(cm)) {
            return "中国移动";
        } else if (phone_number.matches(cu)) {
            return "中国联通";
        } else if (phone_number.matches(ct)) {
            return "中国电信";
        } else if (phone_number.matches(vcm)) {
            return "中国移动";
        } else if (phone_number.matches(vcu)) {
            return "中国联通";
        } else if (phone_number.matches(vct)) {
            return "中国电信";
        } else {
            return "";
        }

    }


    private void GetCellInfos() {
        size = 0;
        final StringBuffer latlng_sb = new StringBuffer();
        String mcc = "460";
        int type = 0; // 返回坐标类型默认值 0(google坐标),1( 百度坐标),2(gps坐标)
        final String key0 = "80bad67eb9bd4f3aa68b52f5d747c91b";//haoService移动联通接口
        String key1 = "6536fdab0b6ecb926485658af71f3a9d";// 聚合申请的电信接口的key
        StringBuffer sur_hao_ = null;
        StringBuffer Dianxin_sb = null;
        StringRequest stringRequest_noDianxin = null;
        StringRequest stringRequest_Dianxin = null;
        final List<PhoneNO> arrays = helper.GetCellInfos_Form(importTime);
        if (arrays.size() > 0 && arrays != null) {
            for (int i = 0; i < arrays.size(); i++) {
                PhoneNO bean = arrays.get(i);
                String tag = matchesPhoneNumber(bean.getPhoneNum().trim());
                final String cid = bean.getCid();
                final String lac = bean.getLac();
                final String nid = bean.getNid();
                final String time = bean.getTime();
                if (tag.equals("中国电信")) {
                    Dianxin_sb = new StringBuffer();
                    Dianxin_sb.append("http://v.juhe.cn/cdma/?");
                    Dianxin_sb.append("&sid=" + lac).append("&cellid=" + cid)
                            .append("&nid=" + nid).append("&key=" + key1);
                    getRequestQueue();
                    stringRequest_Dianxin = new StringRequest(Request.Method.GET, Dianxin_sb.toString(), new Response.Listener<String>() {


                        @Override
                        public void onResponse(String s) {
                            size++;

                            JSONObject obj;
                            try {
                                PeopleLocation pl = new PeopleLocation();
                                obj = new JSONObject(s);
                                String reason = obj.getString("reason");
                                if (!reason.equals("Successed!")) {
                                    getDataFromHaoServiceOfDianxin(lac,cid,nid,key0,time,arrays);
                                } else {
                                    JSONObject object = (JSONObject) obj.get("result");
                                    String la = (String) object.get("o_lat");
                                    String lo = (String) object.get("o_lon");
                                    String addr = object.getString("address");
                                    pl.setLat(la);
                                    pl.setLng(lo);
                                    pl.setAddress(addr);
                                    pl.setLac(lac);
                                    pl.setCid(cid);
                                    pl.setNid(nid);
                                    pl.setTime(time);
                                    pl.setImportTime(importTime);
                                    helper.SavePeopleLocation(pl);
                                }
                                if (size == arrays.size()) {
                                    MarkersToMap();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }




                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError volleyError) {

                        }
                    });
                    mRequestQueue.add(stringRequest_Dianxin);
                } else {
                    String sur_hao = "http://api.haoservice.com/api/getlbs";
                    sur_hao_ = new StringBuffer(sur_hao);
                    sur_hao_.append("?mcc=" + mcc);
                    if (tag.equals("中国移动")) {
                        sur_hao_.append("&mnc=" + "2");
                    } else if (tag.equals("中国联通")) {
                        sur_hao_.append("&mnc=" + "1");
                    }
                    sur_hao_.append("&cell_id=" + String.valueOf(cid) + "&lac="
                            + String.valueOf(lac));
                    sur_hao_.append("&type=" + String.valueOf(type));
                    sur_hao_.append("&key=" + key0);
                    getRequestQueue();
                    stringRequest_noDianxin = new StringRequest(Request.Method.GET, sur_hao_.toString(), new Response.Listener<String>() {


                        @Override
                        public void onResponse(String s) {
                            size++;
                            JSONObject obj;
                            try {
                                PeopleLocation pl = new PeopleLocation();
                                obj = new JSONObject(s);
                                String location = obj.getString("location");
                                if (location.equals("null")) {
                                    //TODO 转聚合接口
                                } else {
                                    JSONObject object_ = (JSONObject) obj.get("location");
                                    String la = object_.getString("latitude");
                                    String lo = object_.getString("longitude");
                                    String address = object_.getString("addressDescription");

                                    pl.setLat(la);
                                    pl.setLng(lo);
                                    pl.setAddress(address);
                                    pl.setLac(lac);
                                    pl.setCid(cid);
                                    pl.setTime(time);
                                    pl.setImportTime(importTime);
                                    helper.SavePeopleLocation(pl);
                                    Log.i(Tag, lac + "..." + cid + "..." + time + "...." + latlng_sb.toString());
                                    //   helper.UpdateFormInfo(latlng_sb.toString(), num);
                                    if (size == arrays.size()) {
                                        MarkersToMap();
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError volleyError) {

                        }
                    });
                    mRequestQueue.add(stringRequest_noDianxin);
                }


            }


        }

    }

    /**
     * 调用haoService的电信接口获取电信数据
     *  http://api.haoservice.com/api/getcdmalbs?sid=14175&bid=29282&nid=11&type=0&key=80bad67eb9bd4f3aa68b52f5d747c91b
     * @param lac
     * @param cid
     * @param nid
     * @param key0
     */
    private void getDataFromHaoServiceOfDianxin(final String lac, final String cid,final String nid, String key0,final String time,final List<PhoneNO> arrays) {
        int type = 0; // 返回坐标类型默认值 0(google坐标),1( 百度坐标),2(gps坐标)
        StringBuffer sbHSofDX = new StringBuffer(
                "http://api.haoservice.com/api/getcdmalbs");
        sbHSofDX.append("?sid=" + lac + "&bid=" + cid + "&nid=" + nid
                + "&type=" + type + "&key=" + key0);

        getRequestQueue();
        StringRequest  Request_DianxinByHaoservice = new StringRequest(Request.Method.GET, sbHSofDX.toString(), new Response.Listener<String>() {


            @Override
            public void onResponse(String s) {

                JSONObject obj;
                try {
                    if (size == arrays.size()) {
                      if (dialog!=null) {
                          dialog.dismiss();
                          dialog=null;
                      }
                    }
                    PeopleLocation pl = new PeopleLocation();
                    obj = new JSONObject(s);
                    String object = obj.getString("ErrCode");
                    if (object.equals("0")) {
                        JSONObject object_ = (JSONObject) obj.get("location");
                        String la = object_.getString("latitude");
                        String lo = object_.getString("longitude");
                        String addr = object_.getString("addressDescription");
                        pl.setLat(la);
                        pl.setLng(lo);
                        pl.setAddress(addr);
                        pl.setLac(lac);
                        pl.setCid(cid);
                        pl.setTime(time);
                        pl.setImportTime(importTime);
                        helper.SavePeopleLocation(pl);

                    }else{
                        Toast.makeText(FormDataLocationMapActivity.this, "CID/"+cid+"SID/"+lac+"NID/"+nid+"这组数据查不到位置信息", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }




            }


        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });

        mRequestQueue.add(Request_DianxinByHaoservice);

    }
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley
                    .newRequestQueue(this.getApplicationContext());
        }
        return mRequestQueue;
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
    }

    private void setUpMap() {
        aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
        aMap.setOnInfoWindowClickListener(this);// 设置点击infoWindow事件监听器
        aMap.setInfoWindowAdapter(this);
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
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
        if (bitmap_onLine != null) {
            bitmap_onLine.recycle();
            bitmap_onLine = null;

        }
    }

    @Override
    public View getInfoWindow(Marker marker) {
        infoWindow = getLayoutInflater().inflate(R.layout.newcell_selfpospopup,
                null);

        render(marker, infoWindow);
        return infoWindow;
    }

    public void render(final Marker marker, View view) {

        String info = marker.getSnippet();
        String[] infos = info.split("\n");
        int len = infos.length;
        TextView nid_detail_tv = (TextView) view.findViewById(R.id.nid_detail_tv);
        if (len == 7) {
            nid_detail_tv.setVisibility(View.VISIBLE);
            nid_detail_tv.setText("网络识别码(NID):" + infos[6]);
        } else {
            nid_detail_tv.setVisibility(View.GONE);
        }
        address_tv = (TextView) view.findViewById(R.id.new_address_tv);
        address_tv.setText(infos[2]);
        TextView time_tv = (TextView) view.findViewById(R.id.new_time_tv);
        time_tv.setText(infos[5]);
        TextView jizhan = (TextView) view.findViewById(R.id.new_jizhan);
        TextView shanqu_tv = (TextView) view.findViewById(R.id.new_shanqu_tv);
        if (len == 7) {
            jizhan.setText("基站号(BID)：" + infos[4]);
            shanqu_tv.setText("系统识别码(SID)" + infos[3]);
        } else {
            jizhan.setText("基站号：" + infos[4]);
            shanqu_tv.setText("扇区号：" + infos[3]);
        }
        TextView lo_tv = (TextView) view.findViewById(R.id.new_lo_tv);
        lo_tv.setText("经度：" + infos[1]);

        TextView la_tv = (TextView) view.findViewById(R.id.new_la_tv);
        la_tv.setText("纬度：" + infos[0]);
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
                    showAddressDialog();

                }

                return false;
            }
        });
    }

    private void showAddressDialog() {
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

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }


    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
