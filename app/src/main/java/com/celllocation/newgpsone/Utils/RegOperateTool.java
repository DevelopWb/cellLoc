package com.celllocation.newgpsone.Utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.model.LatLng;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.celllocation.R;
import com.celllocation.newgpsone.older.CommonProgressDialog;
import com.celllocation.newgpsone.older.DialogAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * ?????????????????????
 * Created by Administrator on 2017/3/30.
 */

public class RegOperateTool {
    private RequestQueue myRequestQueue;
    private SharedPreferences sp;
    public static boolean istoolTip = false;//????????????????????????????????????????????????????????????????????????
    public static boolean isNumberLimit = false;//??????????????????
    public static boolean isForbidden = false;//????????????
    public static int REGSIZE = 0;
    public static String URL_Reg_Center = "http://zc.xun365.net";//?????????????????????
    public static String URL_CELL_LOCATION = "http://218.246.35.74:5000";//??????????????????
    public static String APP_MARK = "JZDW";//????????????
    private CommonProgressDialog mProgressDialog;
    private String nearestVersion;
    private Context context;
    public static String strreg;
    private AMapLocationClient locationClient = null;
    private String Lat;
    private String Lng;
    private String Addr;
    private Dialog dialog_Reg;
    private ProgressDialog progressDialog;
    private CancelCallBack cancelCallBack;

    public RegOperateTool(Context context) {
        this.context = context;
        sp = context.getSharedPreferences("REG", MODE_PRIVATE);
        strreg = sp.getString("OBJREG", "");
    }

    public RegOperateTool(Context context, String str) {
        this.context = context;
        sp = context.getSharedPreferences("REG", MODE_PRIVATE);
        strreg = sp.getString("OBJREG", "");
        initLocation();
        if (strreg == null || TextUtils.isEmpty(strreg)) {
            showRegDialog();
        } else {
            checkRegStatus();
        }
    }

    /**
     * ??????????????????
     *
     * @param size ??????????????????
     */
    public void SetRegisCodeNumber(final int size) {
        getRequestQueue();
        String url = URL_Reg_Center + "/WebService/RegisCode.asmx/SetRegisCodeNumber";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String str) {
                if (str == null) {
                    Toast.makeText(context, "??????????????????????????????????????????", Toast.LENGTH_SHORT).show();
                    return;
                }
                sp.edit().putInt("MINUSTIMES", 0).commit();
                String json = getStr(str);
                try {
                    JSONObject obj = new JSONObject(json);
                    String model = obj.getString("Model");
                    //  "?????????????????????"
                    if (model.equals("?????????????????????")) {
                        isForbidden = true;
                        SaveRegStatus("??????????????????");
                        if (istoolTip) {
                            Toast.makeText(context, "????????????????????????????????????", Toast.LENGTH_SHORT).show();
                        }

                        return;
                    } else if (model.equals("????????????????????????")) {
                        SaveRegStatus("????????????????????????");
                        REGSIZE++;
                        if (istoolTip) {
                            Toast.makeText(context, "?????????????????????????????????????????????", Toast.LENGTH_SHORT).show();
                        }
                        return;
                    } else if (model.equals("???????????????????????????")) {
                        SaveRegStatus("??????????????????");
                        if (istoolTip) {
                            Toast.makeText(context, "????????????????????????????????????????????????", Toast.LENGTH_SHORT).show();
                        }
                        return;
                    } else if (model.equals("??????????????????")) {
                        SaveRegStatus("??????????????????");
                        if (istoolTip) {
                            Toast.makeText(context, "???????????????????????????????????????", Toast.LENGTH_SHORT).show();
                        }
                        return;
                    } else {
                        SaveRegStatus("???????????????");
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<String, String>();
                map.put("softType", "mb");
                map.put("regisCode", strreg);
                map.put("number", size + "");

                return map;
            }
        };
        myRequestQueue.add(stringRequest);

    }

    private RequestQueue getRequestQueue() {
        if (myRequestQueue == null) {
            myRequestQueue = Volley
                    .newRequestQueue(context.getApplicationContext());
        }
        return myRequestQueue;
    }

    private String getStr(String str) {
        int ii = 0;
        int j = 0;
        ii = str.indexOf("{");
        j = str.lastIndexOf("}");
        return str.substring(ii, j + 1);
    }

    /**
     * ?????????????????????
     *
     * @param str ????????????
     */
    private void SaveRegStatus(String str) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("REGSTATUS", str);
        editor.commit();
    }

    /**
     * ??????????????????????????????????????????
     */
    public void CheckUnMinusedRegSizeToMinus() {
        if (isTheRegStatusOkNoToast(context)) {
            if (sp.getInt("UNMINUSEDSIZE", 0) > 0) {
                if (isNumberLimit) {
                    SetRegisCodeNumber(sp.getInt("UNMINUSEDSIZE", 0));
                    sp.edit().putInt("UNMINUSEDSIZE", 0).commit();
                }

            }
        }

    }

    //??????????????????????????????
    public void UnMinusedRegSizeToCommit() {
        if (REGSIZE > 1) {
            SharedPreferences.Editor et = sp.edit();
            et.putInt("UNMINUSEDSIZE", REGSIZE - 1);
            et.commit();
        }
    }

    /**
     * ?????????????????????????????????
     *
     * @return
     */
    public boolean isTheRegStatusOk(Context context) {
        SharedPreferences sp = context.getSharedPreferences("REG", MODE_PRIVATE);
        String reg_status = sp.getString("REGSTATUS", "???????????????");
        //  "?????????????????????"
        if (reg_status.equals("??????????????????")) {
            Toast.makeText(context, "????????????????????????????????????", Toast.LENGTH_SHORT).show();
            return false;
        } else if (reg_status.equals("????????????????????????")) {
            Toast.makeText(context, "?????????????????????????????????????????????", Toast.LENGTH_SHORT).show();

            return false;
        } else if (reg_status.equals("??????????????????")) {
            Toast.makeText(context, "????????????????????????????????????????????????", Toast.LENGTH_SHORT).show();
            return false;
        } else if (reg_status.equals("??????????????????")) {
            Toast.makeText(context, "???????????????????????????????????????", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }


    /**
     * ?????????????????????????????????
     *
     * @return
     */
    public boolean isTheRegStatusOkNoToast(Context context) {
        SharedPreferences sp = context.getSharedPreferences("REG", MODE_PRIVATE);
        String reg_status = sp.getString("REGSTATUS", "???????????????");
        //  "?????????????????????"
        if (reg_status.equals("??????????????????")) {
            return false;
        } else if (reg_status.equals("????????????????????????")) {
            return false;
        } else if (reg_status.equals("??????????????????")) {
            return false;
        } else if (reg_status.equals("??????????????????")) {
            return false;
        } else {
            return true;
        }
    }
// ????????????????????????

    public static boolean isConnected(Context context) {
        boolean isOk = true;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mobNetInfo = connectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo wifiNetInfo = connectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (wifiNetInfo != null && !wifiNetInfo.isConnectedOrConnecting()) {
                if (mobNetInfo != null && !mobNetInfo.isConnectedOrConnecting()) {
                    NetworkInfo info = connectivityManager
                            .getActiveNetworkInfo();
                    if (info == null) {
                        isOk = false;
                    }
                }
            }
            mobNetInfo = null;
            wifiNetInfo = null;
            connectivityManager = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isOk;
    }


    /**
     * ????????????????????????
     */
    public void checkRegStatus() {
        if (!isConnected(context.getApplicationContext())) {
            Toast.makeText(context, "????????????????????????????????????", Toast.LENGTH_LONG)
                    .show();
            return;
        }

        myRequestQueue = getRequestQueue();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, URL_Reg_Center + "/WebService/SoftWare.asmx/GetRegisCodeInfo_NoPhoneMessage", new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                //???????????????????????????
                if (!TextUtils.isEmpty(response)) {
                    String json = Getstr(response);
                    try {
                        JSONObject obj = new JSONObject(json);
                        String result = obj.getString("Result");
                        JSONArray mArray = obj.getJSONArray("Model");
                        if (!TextUtils.isEmpty(result) && result.equals("ok")) {
                            if (mArray.length() == 0) {
                               boolean warn =  sp.getBoolean("ISTOOLTIP",false);//0???????????????
                                if (warn) {//??????
                                    SaveRegStatus("??????????????????");
                                    WarnRegStatus("????????????????????????????????????","");
                                }
                            } else {
                                JSONObject obj_ = (JSONObject) mArray.get(0);
                                String Imei = obj_.getString("Imei").trim();
                                String MAC = obj_.getString("MAC").trim();
                                String isValid = obj_.getString("isValid");
                                String isNumber = obj_.getString("isNumber");
                                String isDisabled = obj_.getString("isDisabled");
                                String isAutoUpdate = obj_.getString("isAutoUpdate");
                                String isToolTip = obj_.getString("isToolTip").trim();
//???????????????????????????
                                String RegisCodeState = obj_.getString("RegisCodeState");
                                RegSuccess(null, null, isToolTip, isNumber);
                                if (RegisCodeState.equals("??????")) {
                                    SaveRegStatus("???????????????");
                                } else if (RegisCodeState.equals("?????????")) {
                                    SaveRegStatus("??????????????????");
                                } else if (RegisCodeState.equals("????????????")) {
                                    SaveRegStatus("????????????????????????");
                                } else if (RegisCodeState.equals("?????????")) {
                                    SaveRegStatus("??????????????????");
                                }
                                if (isDisabled != null && !TextUtils.isEmpty(isDisabled)) {
                                    if (isDisabled.equals("0")) {//??????????????????
                                        isForbidden = true;
                                        WarnRegStatus("????????????????????????????????????", "disable");
                                        return;
                                    } else {
                                        isForbidden = false;
                                    }
                                }

                                if (isAutoUpdate != null && !TextUtils.isEmpty(isAutoUpdate)) {
                                    if (isAutoUpdate.equals("1")) {//??????????????????
                                        GetNearestVersionFromService();
                                    }
                                }
                                if (istoolTip) {
                                    if (isValid != null && !TextUtils.isEmpty(isValid)) {
                                        if (isValid.equals("0")) {//?????????????????????
                                            String ValidEnd = obj_.getString("ValidEnd");
                                            String time = ValidEnd.split(" ")[0];
                                            if (TheDayToNextDay(time) > 0 && TheDayToNextDay(time) < 8) {

                                                if (IsTheRegStatusTime("isValid")) {
                                                    WarnRegStatus("????????????????????????" + TheDayToNextDay(time) + "????????????????????????", "isValid");
                                                }

                                            } else if (TheDayToNextDay(time) < 0) {
                                                WarnRegStatus("???????????????????????????????????????", "isValid");
                                                resetNextWarnTime("isValid");
                                                return;
                                            } else {//???????????????????????????
                                                resetNextWarnTime("isValid");
                                            }
                                        }
                                    }
                                    if (!TextUtils.isEmpty(MAC)) {
                                        if (!macAddress().equals(MAC)) {
                                            //TOdo ????????????
                                            WarnRegStatus("???????????????MAC??????????????????????????????", "disable");
                                        }

                                    }
                                    if (!TextUtils.isEmpty(Imei)) {//??????????????????????????????IMEI
                                        if (!GetImei().equals(Imei)) {
                                            //todo ????????????
                                            WarnRegStatus("???????????????IMEI??????????????????????????????", "disable");
                                        }

                                    }
                                    if (isNumber != null && !TextUtils.isEmpty(isNumber)) {
                                        if (isNumber.equals("0")) {//????????????????????????
                                            String NumberTotal = obj_.getString("Number");
                                            String NumberUsed = obj_.getString("NumberNow");
                                            int NumberNow = Integer.parseInt(NumberTotal) - Integer.parseInt(NumberUsed);
                                            if (NumberNow >0&&NumberNow < 100) {
                                                if (IsTheRegStatusTime("isNumber")) {
                                                    WarnRegStatus("?????????????????????" + NumberNow + "????????????????????????", "isNumber");
                                                }

                                            } else if (NumberNow < 0) {
                                                WarnRegStatus("?????????????????????????????????????????????", "isNumber");

                                                resetNextWarnTime("isNumber");
                                                return;
                                            } else {//???????????????????????????
                                                resetNextWarnTime("isNumber");
                                            }
                                        }
                                    }

                                }

                            }
                        } else {
                            if (IsTheRegStatusTime("isWrong")) {
                                WarnRegStatus("?????????????????????", "isWrong");
                            }

                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }


                }


            }


        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(
                    VolleyError error) {
                Toast.makeText(context, "????????????????????????????????????", Toast.LENGTH_SHORT).show();


            }
        }) {
            @Override
            protected Map<String, String> getParams()
                    throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("regisCode", strreg);
                map.put("softwareId", APP_MARK);
                map.put("softwareType", "mb");
                return map;
            }


        };

        myRequestQueue.add(stringRequest);


    }

    public static String Getstr(String response) {

        int i = 0;
        int y = 0;
        i = response.indexOf("{");
        y = response.lastIndexOf("}");
        String str = response.substring(i, y + 1);
        return str;
    }

    /**
     * ???????????????????????????
     *
     * @param status
     */
    private void resetNextWarnTime(String status) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("NEXTWARNTIME", MODE_PRIVATE);
        SharedPreferences.Editor et = sharedPreferences.edit();
        et.putString("nextRegStatusTime" + status, "");
        et.commit();
    }

    /**
     * ?????????????????????????????????
     */
    private boolean IsTheRegStatusTime(String status) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("NEXTWARNTIME", MODE_PRIVATE);
        final String time = sharedPreferences.getString("nextRegStatusTime" + status, "");
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        String time2 = new SimpleDateFormat("yyyy-MM-dd").format(date);
        if (TextUtils.isEmpty(time)) {
            return true;
        } else {
            if (compareTime(time2, time)) {
                return true;
            } else {
                return false;
            }
        }

    }

    /**
     * ??????????????????????????????
     *
     * @param startTime ????????????
     * @param endTime   ????????????
     * @return
     */
    public static boolean compareTime(String startTime, String endTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Long a = sdf.parse(startTime).getTime();
            Long b = sdf.parse(endTime).getTime();
            if (a > b || a == b) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void WarnRegStatus(final String text, final String status) {

        View v = LayoutInflater.from(context).inflate(R.layout.warn_reg_layout
                , null);
        final Dialog dialog_toWarn = new Dialog(context, R.style.DialogStyle);
        dialog_toWarn.setCanceledOnTouchOutside(false);
        dialog_toWarn.setCancelable(false);
        dialog_toWarn.show();
        Window window = dialog_toWarn.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        window.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
//        lp.width = dip2px(this, 300); // ??????
//        lp.height = dip2px(this, 160); // ??????
        // lp.alpha = 0.7f; // ?????????
        window.setAttributes(lp);
        window.setContentView(v);
//        dialog_toSet.setOnKeyListener(new DialogInterface.OnKeyListener() {
//            @Override
//            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//
//                if (keyCode == event.KEYCODE_BACK) {
//                    if (event.getAction() == KeyEvent.ACTION_DOWN) {
//                        finish();
//                    }
//                }
//                return false;
//            }
//        });
        final TextView nfs_set_no_tv = (TextView) v.findViewById(R.id.warn_reg_tv);
        final TextView warn_reg_textViewreg = (TextView) v.findViewById(R.id.warn_reg_textViewreg);
        nfs_set_no_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (text != null && !TextUtils.isEmpty(text)) {
                    if (text.equals("????????????????????????????????????")) {
                        if (cancelCallBack != null) {
                            cancelCallBack.toFinishActivity();
                        }
                    } else if (text.equals("???????????????MAC??????????????????????????????")) {
                        if (cancelCallBack != null) {
                            cancelCallBack.toFinishActivity();
                        }
                    } else if (text.equals("???????????????IMEI??????????????????????????????")) {
                        if (cancelCallBack != null) {
                            cancelCallBack.toFinishActivity();
                        }
                    }  else if (text.equals("???????????????????????????????????????")) {
                        if (cancelCallBack != null) {
                            cancelCallBack.toFinishActivity();
                        }
                    } else if (text.equals("?????????????????????????????????????????????")) {
                        if (cancelCallBack != null) {
                            cancelCallBack.toFinishActivity();
                        }
                    }else {
                        String nextTime = GetNextWarnTime(1);
                        SharedPreferences sharedPreferences = context.getSharedPreferences("NEXTWARNTIME", MODE_PRIVATE);
                        SharedPreferences.Editor et = sharedPreferences.edit();
                        et.putString("nextRegStatusTime" + status, nextTime);
                        et.commit();
                        dialog_toWarn.dismiss();
                    }

                }
            }

        });
        warn_reg_textViewreg.setText(text);
    }


    /**
     * ???????????????????????????,day??????
     */

    private String GetNextWarnTime(int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, day);
        Date date = calendar.getTime();
        String time = new SimpleDateFormat("yyyy-MM-dd").format(date);
        return time;
    }

    /**
     * ?????????????????????????????????
     *
     * @return
     */
    private int TheDayToNextDay(String time) {
        int day = 0;
        try {
            Calendar mCalendar = Calendar.getInstance();
            Date nowDate = mCalendar.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            String time2 = sdf.format(nowDate);
            Date nowDate_ = sdf.parse(time2);
            Date nextDate = sdf.parse(time);
            day = (int) ((nextDate.getTime() - nowDate_.getTime()) / (24 * 60 * 60 * 1000));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return day;
    }

    /**
     * ?????????????????????????????????
     */
    private void GetNearestVersionFromService() {

        if (isConnected(context)) {
            getRequestQueue();
            StringRequest mStringRequest = new StringRequest(Request.Method.POST, URL_Reg_Center + "/WebService/SoftWare.asmx/GetAllSoftWareInfo", new Response.Listener<String>() {
                @Override
                public void onResponse(String str) {
                    if (str != null && !TextUtils.isEmpty(str)) {

                        try {
                            JSONObject obj = new JSONObject(str);
                            JSONArray infos = obj.getJSONArray("Model");
                            if (infos.length() > 0) {
                                JSONObject obj_ = (JSONObject) infos.get(0);
                                nearestVersion = obj_.getString("SoftwareVersion").trim();
                                String down_url = obj_.getString("softDownloadUrl");
                                String appDescription = obj_.getString("softDescription");
                                if (updateableSoftVersion(getAPPVersion(), nearestVersion)) {
                                    if (IsTheTime()) {
                                        WarnUpgradeDialog(down_url, appDescription);
                                    }

                                } else {//???
                                    SharedPreferences sharedPreferences = context.getSharedPreferences("NEXTWARNTIME", MODE_PRIVATE);
                                    SharedPreferences.Editor et = sharedPreferences.edit();
                                    et.putString("nextTime", "");
                                    et.commit();
                                }
                            } else {
                                Toast.makeText(context, "??????????????????????????????", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("softwareId", APP_MARK);
                    map.put("softwareType", "mb");
                    return map;
                }
            };
            myRequestQueue.add(mStringRequest);
        }

    }

    /**
     * ?????????????????????????????????????????????
     *
     * @param localVersionName  ???????????????????????????
     * @param serverVersionName ??????????????????????????????
     * @return
     */
    private boolean updateableSoftVersion(String localVersionName, String serverVersionName) {
        if (TextUtils.isEmpty(localVersionName) || TextUtils.isEmpty(serverVersionName)) {
            return false;
        }
        String local3 = "0";
        String server3 = "0";
        String[] localVersion = localVersionName.split("\\.");
        String[] serverVersion = serverVersionName.split("\\.");
        String local1 = localVersion[0];
        String local2 = localVersion[1];
        if (localVersion.length == 3) {
            local3 = localVersion[2];
        }
        String server1 = serverVersion[0];
        String server2 = serverVersion[1];
        if (serverVersion.length == 3) {
            server3 = serverVersion[2];
        }
        if (Integer.parseInt(server1) > Integer.parseInt(local1)) {
            return true;
        }
        if (Integer.parseInt(server2) > Integer.parseInt(local2)) {
            return true;
        }
        if (Integer.parseInt(server3) > Integer.parseInt(local3)) {
            return true;
        }
        return false;
    }

    /**
     * ????????????????????????
     */
    private boolean IsTheTime() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("NEXTWARNTIME", MODE_PRIVATE);
        final String time = sharedPreferences.getString("nextTime", "");
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        String time2 = new SimpleDateFormat("yyyy-MM-dd").format(date);
        if (TextUtils.isEmpty(time)) {
            return true;
        } else {
            if (compareTime(time2, time)) {
                return true;
            } else {
                return false;
            }
        }


    }

    private void WarnUpgradeDialog(final String url, String description) {

        View v = LayoutInflater.from(context).inflate(R.layout.to_nfc_set, null);
        final Dialog dialog_toSet = new Dialog(context, R.style.DialogStyle);
        dialog_toSet.setCanceledOnTouchOutside(false);
        dialog_toSet.setCancelable(false);
        dialog_toSet.show();
        Window window = dialog_toSet.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        window.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        lp.width = dip2px(context, 300); // ??????
        lp.height = dip2px(context, 260); // ??????
        // lp.alpha = 0.7f; // ?????????
        window.setAttributes(lp);
        window.setContentView(v);
        ListView feature_lv = (ListView) v.findViewById(R.id.feature_lv);
        feature_lv.setDivider(null);
        feature_lv.setAdapter(new DialogAdapter(context, description));
        dialog_toSet.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_BACK) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN) {
                        dialog_toSet.dismiss();
                    }
                }
                return false;
            }
        });
        final TextView nfs_set_sure_tv = (TextView) v.findViewById(R.id.nfs_set_sure_tv);
        final TextView nfs_set_no_tv = (TextView) v.findViewById(R.id.nfs_set_no_tv);
        nfs_set_sure_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_toSet.dismiss();
                DownAPKfromService(url);
            }
        });
        nfs_set_no_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_toSet.dismiss();
                String nextTime = GetNextWarnTime(7);
                SharedPreferences sharedPreferences = context.getSharedPreferences("NEXTWARNTIME", MODE_PRIVATE);
                SharedPreferences.Editor et = sharedPreferences.edit();
                et.putString("nextTime", nextTime);
                et.commit();
            }
        });
    }


    private void DownAPKfromService(String url) {

        if (isConnected(context)) {
            mProgressDialog = new CommonProgressDialog(context);
            mProgressDialog.setMessage("????????????");
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.setCancelable(true);
            mProgressDialog.setCanceledOnTouchOutside(false);
            final DownloadTask downloadTask = new DownloadTask(context);
            downloadTask.execute(url);
            mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    downloadTask.cancel(true);
                }
            });
            //downFile(url);
        }

    }

    private class DownloadTask extends AsyncTask<String, Integer, String> {

        private Context context;

        DownloadTask(Context context) {
            this.context = context;
        }

        //?????????????????????doInBackground???????????????????????????ui???????????????
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //???????????? ????????????????????????
            mProgressDialog.show();
            mProgressDialog.setProgress(0);
        }

        @Override
        protected String doInBackground(String... params) {
            int i = 0;
            String uri = URL_Reg_Center + params[0];
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(uri);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                // expect HTTP 200 OK, so we don't mistakenly save error report
                // instead of the file


                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                }
                // this will be useful to display download percentage
                // might be -1: server did not report the length
                int fileLength = connection.getContentLength();
                mProgressDialog.setMax(fileLength);
                // download the file
                input = connection.getInputStream();
                output = new FileOutputStream(GetAPKPath());
                byte data[] = new byte[4096];
                int total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
//					if (fileLength > 0) // only if total length is known
//					{
//						i = (int) (total * 100 / fileLength);
//					}
                    mProgressDialog.setProgress(total);
//					publishProgress(i);
                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                return e.toString();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }
                if (connection != null)
                    connection.disconnect();
            }
            return "right";
        }

        //???ui??????????????? ????????????ui
        @Override
        protected void onPostExecute(String string) {
            // TODO Auto-generated method stub
            super.onPostExecute(string);
            //???????????? ????????????????????????
            if (string.equals("right")) {
                mProgressDialog.cancel();
                Toast.makeText(context, "????????????", Toast.LENGTH_SHORT).show();
                installApk();
            } else {
                mProgressDialog.cancel();
                Toast.makeText(context, "????????????", Toast.LENGTH_LONG).show();
            }


        }

        /*
         * ???doInBackground?????????????????????publishProgress?????? ??????????????????????????????
         * ?????????????????? ????????????????????????
         * */
        @Override
        protected void onProgressUpdate(Integer... values) {
            // TODO Auto-generated method stub
            super.onProgressUpdate(values);

        }
    }

    /**
     * ??????APK
     */
    private void installApk() {
        File file = new File(GetAPKPath());
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * ???????????????????????????
     */
    private String GetAPKPath() {
        File file = new File("/mnt/sdcard/.toInstallPG");
        if (!file.exists()) {
            file.mkdir();
        }
        String path = "/mnt/sdcard/.toInstallPG" + "/" + getAPPName() + nearestVersion + ".apk";
        return path;
    }

    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * ??????????????????
     */
    public String getAPPName() {
        String appName = "";
        PackageManager pm = context.getPackageManager();//??????PackageManager??????
        try {
            ApplicationInfo applicationInfo = pm.getApplicationInfo(context.getPackageName(), 0);
            appName = (String) pm.getApplicationLabel(applicationInfo);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appName;
    }

    // ???????????????????????????
    public static String getDateToString(long time) {
        Date d = new Date(time);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return sf.format(d);
    }

    /**
     * ??????????????????
     */
    public void showRegDialog() {

        View v = LayoutInflater.from(context).inflate(R.layout.reg_dialog, null);
        dialog_Reg = new Dialog(context, R.style.DialogStyle);
        dialog_Reg.setCanceledOnTouchOutside(false);
        dialog_Reg.show();
        dialog_Reg.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                dialog_Reg.dismiss();
                //????????????
                if (cancelCallBack != null) {
                    cancelCallBack.toFinishActivity();
                }
            }
        });
        Window window = dialog_Reg.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        window.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        lp.width = dip2px(context, 290); // ??????
        lp.height = dip2px(context, 200); // ??????
        lp.alpha = 0.7f; // ?????????
        window.setAttributes(lp);
        window.setContentView(v);
        final TextView reg = (TextView) v.findViewById(R.id.editTextReg);
        ImageButton ib = (ImageButton) v.findViewById(R.id.imageButtonReg);
        ImageButton.OnClickListener listener = new ImageButton.OnClickListener() {

            public void onClick(View v) {
                if (!RegOperateTool.isConnected(context.getApplicationContext())) {
                    Toast.makeText(context, "????????????????????????????????????", Toast.LENGTH_LONG)
                            .show();
                    return;
                }

                final String input = reg.getText().toString().trim();
                if (input == null || TextUtils.isEmpty(input)) {
                    Toast.makeText(context, "??????????????????",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                // ???????????????
                progressDialog = ProgressDialog.show(context, "?????????",
                        "?????????????????????????????????????????????", true);
                progressDialog.setCancelable(true);

                getRequestQueue();

                StringRequest stringRequest = new StringRequest(
                        Request.Method.POST, RegOperateTool.URL_Reg_Center + "/WebService/SoftWare.asmx/GetRegisCodeInfo", new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        //???????????????????????????
                        if (!TextUtils.isEmpty(response)) {
                            String json = Getstr(response);
                            try {
                                JSONObject obj = new JSONObject(json);
                                String result = obj.getString("Result");
                                JSONArray mArray = obj.getJSONArray("Model");
                                if (!TextUtils.isEmpty(result) && result.equals("ok")) {
                                    if (mArray.length() == 0) {
                                        progressDialog.dismiss();
                                        Toast.makeText(context, "??????????????????",
                                                Toast.LENGTH_LONG).show();
                                    } else {
                                        JSONObject obj_ = (JSONObject) mArray.get(0);
                                        String regStatus = obj_.getString("RegisCodeState").trim();
                                        String guestName = obj_.getString("Customer").trim();
                                        String Imei = obj_.getString("Imei").trim();
                                        String MAC = obj_.getString("MAC").trim();
                                        String isToolTip = obj_.getString("isToolTip").trim();
                                        String isNumber = obj_.getString("isNumber").trim();
                                        String Version = obj_.getString("Version").trim();
                                        if (!getAPPVersion().equals(Version)) {
                                            progressDialog.dismiss();
                                            Toast.makeText(context, "??????????????????????????????????????????????????????",
                                                    Toast.LENGTH_LONG).show();
                                            return;
                                        }
                                        if (regStatus.equals("??????")) {
                                            if (TextUtils.isEmpty(Imei)) {//??????????????????????????????IMEI
                                                if (TextUtils.isEmpty(MAC)) {//???????????????MAC
                                                    RegSuccess(input, guestName, isToolTip, isNumber);
                                                } else {
                                                    //TODO ??????Mac????????????
                                                    if (macAddress().equals(MAC)) {
                                                        RegSuccess(input, guestName, isToolTip, isNumber);
                                                    } else {
                                                        progressDialog.dismiss();
                                                        Toast.makeText(context, "?????????????????????????????????(MAC)????????????",
                                                                Toast.LENGTH_LONG).show();
                                                    }

                                                }

                                            } else {//???????????????????????????
                                                if (GetImei().equals(Imei)) {
                                                    if (TextUtils.isEmpty(MAC)) {
                                                        RegSuccess(input, guestName, isToolTip, isNumber);
                                                    } else {
                                                        //TODO ??????Mac????????????
                                                        if (macAddress().equals(MAC)) {
                                                            RegSuccess(input, guestName, isToolTip, isNumber);
                                                        } else {
                                                            progressDialog.dismiss();
                                                            Toast.makeText(context, "?????????????????????????????????(MAC)????????????",
                                                                    Toast.LENGTH_LONG).show();
                                                        }
                                                    }

                                                } else {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(context, "?????????????????????????????????(IMEI)????????????",
                                                            Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        } else if (regStatus.equals("?????????")) {
                                            progressDialog.dismiss();
                                            Toast.makeText(context, "???????????????????????????????????????", Toast.LENGTH_LONG).show();
                                        } else if (regStatus.equals("????????????")) {
                                            progressDialog.dismiss();
                                            Toast.makeText(context, "???????????????????????????????????????????????????", Toast.LENGTH_LONG).show();
                                        } else if (regStatus.equals("?????????")) {
                                            progressDialog.dismiss();
                                            Toast.makeText(context, "????????????????????????????????????", Toast.LENGTH_LONG).show();
                                        }
                                    }


                                } else {
                                    Toast.makeText(context, "?????????????????????", Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }


                        }


                    }


                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(
                            VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(context, "????????????????????????????????????", Toast.LENGTH_SHORT).show();


                    }
                }) {
                    @Override
                    protected Map<String, String> getParams()
                            throws AuthFailureError {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("regisCode", input);
                        map.put("softwareId", RegOperateTool.APP_MARK);
                        map.put("softwareType", "mb");
                        map.put("phoneMessage", getPhoneMessage());
                        return map;
                    }


                };

                myRequestQueue.add(stringRequest);


            }
        };

        ib.setOnClickListener(listener);

    }


    /**
     * ???????????????
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void initLocation() {
        //?????????client
        locationClient = new AMapLocationClient(context.getApplicationContext());
        // ??????????????????
        locationClient.setLocationListener(locationListener);
        startLocation();
    }

    /**
     * ?????????????????????
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//????????????????????????????????????????????????????????????????????????????????????????????????????????????
        mOption.setGpsFirst(false);//?????????????????????gps??????????????????????????????????????????????????????
        mOption.setHttpTimeOut(30000);//???????????????????????????????????????????????????30?????????????????????????????????
        mOption.setInterval(2000);//???????????????????????????????????????2???
        mOption.setNeedAddress(true);//????????????????????????????????????????????????????????????true
        mOption.setOnceLocation(true);//?????????????????????????????????????????????false
//		mOption.setOnceLocationLatest(false);//???????????????????????????wifi??????????????????false.???????????????true,?????????????????????????????????????????????????????????
//		AMapLocationClientOption.setLocationProtocol(AMapLocationProtocol.HTTP);//????????? ????????????????????????????????????HTTP??????HTTPS????????????HTTP
//		mOption.setSensorEnable(false);//????????????????????????????????????????????????false
        return mOption;
    }

    /**
     * ????????????
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void startLocation() {
//		//????????????????????????????????????????????????
//		resetOption();
        // ??????????????????
        locationClient.setLocationOption(getDefaultOption());
        // ????????????
        locationClient.startLocation();
    }

    /**
     * ????????????
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void stopLocation() {
        // ????????????
        locationClient.stopLocation();
    }

    /**
     * ????????????
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void destroyLocation() {
        if (null != locationClient) {
            /**
             * ??????AMapLocationClient????????????Activity???????????????
             * ???Activity???onDestroy??????????????????AMapLocationClient???onDestroy
             */
            stopLocation();
            locationClient.onDestroy();
            locationClient = null;
        }
    }

    /**
     * ????????????
     */
    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation loc) {
            if (null != loc) {
                //??????????????????
                Lat = loc.getLatitude() + "";
                Lng = loc.getLongitude() + "";
                Addr = loc.getAddress();
                CheckSavedVersion();
            }
        }
    };

    private String getPhoneMessage() {
        String lac = "";
        String cid = "";
        String nid = "";
        String imei = "";
        String phoneNo = "";
        String imsi = "";
        String mac = "";
        StringBuffer phoneInfo_sb = new StringBuffer();
        TelephonyManager mTManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (mTManager != null) {
            imei = mTManager.getDeviceId();
            phoneNo = mTManager.getLine1Number();
            imsi = mTManager.getSubscriberId();
        }
        mac = macAddress();
        phoneInfo_sb.append("PhoneNo:" + phoneNo + "," + "Imei:" + imei + "," + "Imsi:" + imsi + "," + "Mac:" + mac + ",");

        if (mTManager != null) {
            int phonetype = mTManager.getPhoneType();
            if (phonetype == TelephonyManager.PHONE_TYPE_GSM) {
                GsmCellLocation gcl = (GsmCellLocation) mTManager.getCellLocation();
                cid = gcl.getCid() + "";
                lac = gcl.getLac() + "";
            } else if (phonetype == TelephonyManager.PHONE_TYPE_CDMA) {
                CdmaCellLocation gcl = (CdmaCellLocation) mTManager
                        .getCellLocation();
                if (gcl != null) {
                    nid = gcl.getNetworkId() + "";// nid
                    cid = gcl.getBaseStationId() + "";// cellid
                    lac = gcl.getSystemId() + ""; // sid
                }

            }
            phoneInfo_sb.append("Lat:" + Lat + "," + "Log:" + Lng + "," + "Lac:" + lac + "," + "Cid:" + cid + "," + "Nid:" + nid + "," + "Addr:" + Addr);
        }
        return phoneInfo_sb.toString();
    }

    /**
     * ?????????????????????
     */
    private String getAPPVersion() {
        PackageManager pm = context.getPackageManager();//??????PackageManager??????
        String version_app = "";
        try {
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);//??????PackageInfo???????????????????????????????????????????????????
            version_app = pi.versionName;//?????????????????????versionCode????????????
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version_app;
    }


    public String macAddress() {
        String address = null;
        try {
            // ???????????????????????????????????????????????? Enumeration?????????
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface netWork = interfaces.nextElement();
                // ????????????????????????????????????????????????????????????????????????????????????????????????????????? MAC??????
                byte[] by = netWork.getHardwareAddress();
                if (by == null || by.length == 0) {
                    continue;
                }
                StringBuilder builder = new StringBuilder();
                for (byte b : by) {
                    builder.append(String.format("%02X:", b));
                }
                if (builder.length() > 0) {
                    builder.deleteCharAt(builder.length() - 1);
                }
                String mac = builder.toString();
                // ??????????????????????????????MAC?????????????????????????????????Wifi??? name ??? wlan0
                if (netWork.getName().equals("wlan0")) {
                    address = mac;
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return address;
    }


    /**
     * ????????????????????????
     */
    private void RegSuccess(String input, String guestName, String isToolTip, String isNumber) {
        SharedPreferences.Editor editor = sp.edit();
        if (input != null && guestName != null) {
            Toast.makeText(context, "?????????????????????",
                    Toast.LENGTH_LONG).show();
            strreg = input;
            editor.putString("OBJREG", input);
            editor.putString("GUESTNAME", guestName);
        }

        if (isToolTip.equals("0")) {//0???????????????
            istoolTip = false;
            editor.putBoolean("ISTOOLTIP", false);
        } else {
            istoolTip = true;
            editor.putBoolean("ISTOOLTIP", true);
        }
        if (isNumber.equals("0")) {//0?????????????????????
            isNumberLimit = true;
            editor.putBoolean("ISNUMBER", true);
        } else {
            isNumberLimit = false;
            editor.putBoolean("ISNUMBER", false);
        }
        editor.commit();
        if (dialog_Reg != null && dialog_Reg.isShowing()) {
            dialog_Reg.dismiss();
        }
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
//        BugPublicUtils.checkToUploadBugInfos(context,"JZDW_crash","http://zc.xun365.net/WebService/SoftWare.asmx/SetBugInfo",strreg,"JZDW");

    }

    private String GetImei() {

        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imei = tm.getDeviceId();
        return imei;
    }

    private void CheckSavedVersion() {
        String savedVersion = sp.getString("SavedVersion", "");
//        String savedVersion = "1.0";
        String nowVersion = getAPPVersion();
        if (savedVersion.equals("")) {
            SharedPreferences.Editor et = sp.edit();
            et.putString("SavedVersion", nowVersion);
            et.commit();
        } else {
            if (updateableSoftVersion(savedVersion,nowVersion)) {
                //??????????????????
                String info = GetInfoWhenVersionChanged(savedVersion, nowVersion);
                UploadVersionInfo(info);
            }

        }

    }

    private String GetInfoWhenVersionChanged(String originalVersion, String newestVersion) {
        String PhoneNo = "";
        String Imei = "";
        String time = "";
        time = RegOperateTool.getDateToString(System.currentTimeMillis());
        StringBuffer Info_sb = new StringBuffer();
        TelephonyManager mTManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (mTManager != null) {
            Imei = mTManager.getDeviceId();
            PhoneNo = mTManager.getLine1Number();
        }
        Info_sb.append("SoftName:" + getAPPName() + "," + "GuestName:" + sp.getString("GUESTNAME", "") + "," + "RegCode:" + strreg + "," + "PhoneNo:" + PhoneNo + "," + "Imei:" + Imei + "," + "Mac:" + macAddress() + "," + "Lat:" + Lat + "," + "Lng:" + Lng + "," + "Addr:" + Addr + "," + "OriginalVersion:" + originalVersion + "," + "NewestVersion:" + newestVersion + "," + "Time:" + time);

        return Info_sb.toString();
    }


    private void UploadVersionInfo(final String info) {
        getRequestQueue();
        String url = RegOperateTool.URL_Reg_Center + "/WebService/SoftWare.asmx/SetVersionInfo";
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (s != null && !TextUtils.isEmpty(s)) {
                    SharedPreferences.Editor et = sp.edit();
                    et.putString("SavedVersion", getAPPVersion());
                    et.commit();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("regisCode", strreg);
                map.put("versionMsg", info);
                return map;
            }
        };
        myRequestQueue.add(mStringRequest);
    }

    public void SetCancelCallBack(CancelCallBack callBack) {
        this.cancelCallBack = callBack;
    }

    public interface CancelCallBack {
        void toFinishActivity();

    }

    /**
     * Gps??????
     *
     * @param lat gps????????????
     * @param lng gps????????????
     * @return
     */
    public static LatLng GpsCorrectToLatLng(double lat, double lng) {
        double[] latlng = {lat, lng};
        GpsCorrect.transform(lat, lng, latlng);
        double lat_ = latlng[0];
        double lng_ = latlng[1];
        return new LatLng(lat_, lng_);
    }

}
