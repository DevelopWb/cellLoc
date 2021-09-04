package com.celllocation.newgpsone;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
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
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.android.volley.RequestQueue;
import com.celllocation.R;
import com.celllocation.newgpsone.Utils.RegOperateTool;
import com.celllocation.newgpsone.bean.CellHisData;
import com.celllocation.newgpsone.bean.DataUtil;
import com.celllocation.newgpsone.cellInfos.CellPosition;
import com.celllocation.newgpsone.cellInfos.CellPositionNetTask;
import com.celllocation.newgpsone.cellInfos.PositionCallBack;

import java.text.DecimalFormat;


public class NewCellActivity extends Activity implements
        OnClickListener, OnMarkerClickListener,
        InfoWindowAdapter {
    private MapView mapView;
    private AMap aMap;
    private boolean isDianXinNo = false;
    private TextView address_tv;
    private Marker regeoMarker;
    AlertDialog.Builder posprogress = null;
    LocationManager locationManager;
    LocationListener locationListener;
    private View infoWindow;
    private GeocodeSearch geocoderSearch;
    private String addressName;
    private RequestQueue myRequestQueue;
    private DataHelper helper;
    private String phoneNum;
    private SharedPreferences sp;
    private boolean istoolTip = false;//注册码状态改变是否提醒，例如：注册码到期，禁用等



    Handler myHandler = new Handler() {

        public void handleMessage(Message msg) {

            switch (msg.what) {

                case 6:// JiZhan position success
                    CellHisData cell = (CellHisData) msg.obj;
                    if (cell != null) {
                        StringBuffer snippet_sb = new StringBuffer();
                        DecimalFormat df = new DecimalFormat("0.000000");
                        helper.SaveCellHisData(cell);
                        String lac = cell.getLac();
                        String cid = cell.getCid();
                        String nid = cell.getNid();
                        String addr = cell.getAddress();
                        String time = cell.getTime();
                        if (addr.equals("")) {
                            addr = "位置不详";
                        }
                        String accuracy = cell.getAccuracy();

                        Double la = Double.parseDouble(cell.getLat());
                        Double lo = Double.parseDouble(cell.getLng());
                        String la_ = df.format(la);
                        String lo_ = df.format(lo);
                        snippet_sb.append(phoneNum + "\n" + la_ + "\n" + lo_ + "\n" + lac + "\n" + cid + "\n" + nid + "\n" + addr + "\n" + accuracy + "\n" + time);
                        LatLng latlng = new LatLng(Double.parseDouble(la_), Double.parseDouble(lo_));
                        LatLonPoint latLonPoint = new LatLonPoint(Double.parseDouble(la_), Double.parseDouble(lo_));
                        aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                com.celllocation.newgpsone.AMapUtil.convertToLatLng(latLonPoint), 16));
                        aMap.clear();
                        Bitmap bitmap = DataUtil.toRoundBitmap(
                                getBitmap(),
                                NewCellActivity.this);
                        MarkerOptions markerOption = new MarkerOptions();
                        markerOption.anchor(0.5f, 0.5f).position(latlng).snippet(snippet_sb.toString())

                                .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                                .draggable(true).period(50);
                        aMap.addMarker(markerOption);
                        PubUtill.isDrawable = true;
                        Toast.makeText(NewCellActivity.this, "基站定位成功！",
                                Toast.LENGTH_SHORT).show();
                        // 在定位点画圆
                        aMap.addCircle(new CircleOptions()
                                .center(latlng).radius(100).strokeColor(Color.BLUE)
                                .fillColor(Color.parseColor("#3F003399"))
                                .strokeWidth(3));
                        if (RegOperateTool.isNumberLimit) {
                            rot.SetRegisCodeNumber(1);
                        }

                    }

                    break;

                case 7:// JiZhan position failed,self pos failed
                    Toast.makeText(NewCellActivity.this, "基站定位失败！",
                            Toast.LENGTH_SHORT).show();
                    PubUtill.isDrawable = true;
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
    private RegOperateTool rot;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.newcell_selfposlayout);
        rot = new RegOperateTool(this);
        sp = getSharedPreferences("REG", MODE_PRIVATE);
        istoolTip = sp.getBoolean("ISTOOLTIP", false);
        findView();
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        helper = new DataHelper(this);
        init();
        RegistBoradCast();
        TelephonyManager tm = (TelephonyManager) this
                .getSystemService(Context.TELEPHONY_SERVICE);
        phoneNum = tm.getDeviceId();
    }

    private void RegistBoradCast() {

        IntentFilter intentFilter = new IntentFilter("REFRESH");
        registerReceiver(mBroadcastReceiver, intentFilter);


    }

    //定义一个广播接受类
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("REFRESH")) {
                if (RegOperateTool.istoolTip) {
                    if (rot.isTheRegStatusOk(NewCellActivity.this)) {
                        PositionByJiZhan();
                    }else{
                        PubUtill.isDrawable = true;
                    }
                }else{
                    if (RegOperateTool.isForbidden) {
                        PubUtill.isDrawable = true;
                        Toast.makeText(NewCellActivity.this, "注册码无效，请联系管理员", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    PositionByJiZhan();
                }
            }
        }
    };

    private void findView() {
        mapView = (MapView) findViewById(R.id.map);
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
        aMap.setInfoWindowAdapter(this);

    }


    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        if (RegOperateTool.istoolTip) {
            if (rot.isTheRegStatusOk(NewCellActivity.this)) {
                PositionByJiZhan();
            }else{
                PubUtill.isDrawable = true;
            }
        }else{
            if (RegOperateTool.isForbidden) {
                Toast.makeText(NewCellActivity.this, "注册码无效，请联系管理员", Toast.LENGTH_SHORT).show();
                return;
            }
            PositionByJiZhan();
        }

    }

    private void PositionByJiZhan() {


        if (!DataUtil.isConnected(getApplicationContext())) {
            Toast.makeText(NewCellActivity.this, "网络异常，请检查手机网络", Toast.LENGTH_SHORT).show();
            return;
        }

        aMap.clear();
        PhoneLocation();
    }

    private void GetJizhanPosBySelf(final String lac, final String cid, final String nid,final String mnc) {

        CellPositionNetTask cellPositionNetTask = new CellPositionNetTask(new PositionCallBack() {
            @Override
            public void onSuccessed(CellPosition position) {
                sendMsg(resolveResponse(NewCellActivity.this,position,lac,cid,nid), 6);
            }

            @Override
            public void onErro() {
                Toast.makeText(getApplicationContext(), "定位失败", Toast.LENGTH_LONG).show();
            }
        });
        cellPositionNetTask.getCellPosition(lac,cid,nid,mnc);
    }
    /**
     * 解析数据
     * @param context
     * @param cellPosition
     * @param lac
     * @param cid
     * @param nid
     * @return
     */
    private CellHisData resolveResponse(Context context, CellPosition cellPosition, String lac, String cid, String nid) {
        String time = DataUtil.getDateToString(System.currentTimeMillis());
        CellHisData cell = new CellHisData();
        Object model = cellPosition.getModel();
        //  "注册码已经禁用"
        if (model.equals("注册码已经禁用")) {
            Toast.makeText(context, "注册码已经禁用,请联系管理员", Toast.LENGTH_SHORT).show();
            return cell;
        } else if (model.equals("注册码次数已用完")) {
            Toast.makeText(context, "注册码次数已用完,请联系管理员", Toast.LENGTH_SHORT).show();
            return cell;
        } else if (model.equals("注册码使用时间过期")) {
            Toast.makeText(context, "注册码使用时间过期,请联系管理员", Toast.LENGTH_SHORT).show();
            return cell;
        } else{
            cell.setCid(cid);
            cell.setLac(lac);
            cell.setNid(nid);

            String lat = cellPosition.getModel().getLatitude();
            String lng = cellPosition.getModel().getLongitude();
            if (lat.isEmpty()||lng.isEmpty()) {
                cell.setLat("0.0");
                cell.setLng("0.0");
            }else{
//                LatLng mLatLng = RegOperateTool.GpsCorrectToLatLng(Double.parseDouble(lat),Double.parseDouble(lng));
//                cell.setLat(mLatLng.latitude+"");
//                cell.setLng(mLatLng.longitude+"");
                cell.setLat(lat);
                cell.setLng(lng);
            }
            cell.setAccuracy(cellPosition.getModel().getPrecision());
            cell.setTime(time);
            cell.setPhone(phoneNum);
            cell.setAddress(cellPosition.getModel().getAddress());
            return cell;
        }
    }

    private void PhoneLocation() {
        String lac = "";
        String cid = "";
        String nid = "";
        String mnc = "0";
        TelephonyManager mTManager = (TelephonyManager) this
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (mTManager != null) {
            int phonetype = mTManager.getPhoneType();
            mnc = mTManager.getSubscriberId().substring(3, 5);
            //移动00、02、04、07
            //联通01、06、09
            //电信03、05、电信4G使用11

            if ("00".equals(mnc)||"02".equals(mnc)||"04".equals(mnc)||"07".equals(mnc)) {
                mnc = "0";
            }else if ("01".equals(mnc)||"06".equals(mnc)||"09".equals(mnc)) {
                mnc = "1";
            }else{
                mnc = "3";
            }
            if (phonetype == TelephonyManager.PHONE_TYPE_GSM) {
                GsmCellLocation gcl = (GsmCellLocation) mTManager.getCellLocation();
                if (gcl!=null) {
                    cid = gcl.getCid() + "";
                    lac = gcl.getLac() + "";
                    GetJizhanPosBySelf(lac, cid, nid,mnc);
                }else{
                    Toast.makeText(this, "无法获取基站信息，请确定SIM卡安装正常", Toast.LENGTH_SHORT).show();
                }

            } else if (phonetype == TelephonyManager.PHONE_TYPE_CDMA) {
                CdmaCellLocation gcl = (CdmaCellLocation) mTManager
                        .getCellLocation();
                if (gcl != null) {
                    nid = gcl.getNetworkId() + "";// nid
                    cid = gcl.getBaseStationId() + "";// cellid
                    lac = gcl.getSystemId() + ""; // sid
                    GetJizhanPosBySelf(lac, cid, nid,mnc);
                }else{
                    Toast.makeText(this, "无法获取基站信息，请确定SIM卡安装正常", Toast.LENGTH_SHORT).show();
                }

            }
        }

    }


    private void sendMsg(CellHisData cell, int num) {
        Message msg = new Message();
        msg.obj = cell;
        msg.what = num;
        myHandler.sendMessage(msg);
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

        if (posprogress != null) {
            finish();
        }
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
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

    private Bitmap getBitmap() {
        Bitmap bitmap = null;

        Resources res = NewCellActivity.this.getResources();

        bitmap = BitmapFactory.decodeResource(res,
                R.drawable.new_ni);
        return bitmap;
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

}
