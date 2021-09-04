package com.celllocation.newgpsone;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.celllocation.R;
import com.celllocation.newgpsone.Utils.RegOperateTool;
import com.celllocation.newgpsone.bean.DataUtil;
import com.celllocation.newgpsone.cellInfos.CellPosition;
import com.celllocation.newgpsone.cellInfos.CellPositionNetTask;
import com.celllocation.newgpsone.cellInfos.PositionCallBack;

import java.util.Timer;
import java.util.TimerTask;

public class CellSearchActivity extends Activity implements OnClickListener {

    private TextView cell_search_tv, qita_tv, shanqu_tv, jizhan_tv;

    private LinearLayout unicom_ll, telecom_ll, telecomNID_ll;
    private LinearLayout cmcc_ll;
    private EditText LAC_et;
    private EditText CELL_et;
    private EditText NID_et;

    private RequestQueue mRequestQueue;
    private String LAC = "";
    private String CID = "";
    private String NID = "";
    private String MNC = "0";//0代表移动，1代表联通

    private Thread t;
    Position GpsPos;
    Timer mTimer;
    private String dlgmsg;
    AlertDialog dlg = null;
    AlertDialog.Builder posprogress = null;

    private SharedPreferences sp;
    private boolean istoolTip = false;//注册码状态改变是否提醒，例如：注册码到期，禁用等
    private RegOperateTool regOperateTool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.cellsearch_activity);
        sp = getSharedPreferences("REG", MODE_PRIVATE);
        istoolTip = sp.getBoolean("ISTOOLTIP", false);
        regOperateTool = new RegOperateTool(this);
        findView();
        setclick();
    }

    private void setclick() {
        cell_search_tv.setOnClickListener(this);
        cmcc_ll.setOnClickListener(this);
        unicom_ll.setOnClickListener(this);
        telecom_ll.setOnClickListener(this);
    }

    private void findView() {
        cell_search_tv = (TextView) findViewById(R.id.cell_search_tv);
        cmcc_ll = (LinearLayout) findViewById(R.id.cmcc_ll);
        unicom_ll = (LinearLayout) findViewById(R.id.unicom_ll);
        telecom_ll = (LinearLayout) findViewById(R.id.telecom_ll);
        telecomNID_ll = (LinearLayout) findViewById(R.id.telecomNID_ll);
        LAC_et = (EditText) findViewById(R.id.LAC_et);
        CELL_et = (EditText) findViewById(R.id.CELL_et);
        NID_et = (EditText) findViewById(R.id.NID_et);
        shanqu_tv = (TextView) findViewById(R.id.shanqu_tv);
        jizhan_tv = (TextView) findViewById(R.id.jizhan_tv);
        qita_tv = (TextView) findViewById(R.id.qita_tv);

        cmcc_ll.setBackgroundColor(android.graphics.Color.parseColor("#125E96"));
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.cell_search_tv:

                String lacstr = LAC_et.getText().toString();
                String cellstr = CELL_et.getText().toString();

                if (PubUtill.DianxinClicked) {
                    if (TextUtils.isEmpty(lacstr)) {
                        Toast.makeText(CellSearchActivity.this, "请填系统识别码",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (TextUtils.isEmpty(cellstr)) {
                        Toast.makeText(CellSearchActivity.this, "请填写基站号",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String nidstr = NID_et.getText().toString();
                    if (TextUtils.isEmpty(nidstr)) {
                        Toast.makeText(CellSearchActivity.this, "请填写网络识别码",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    NID = nidstr;

                } else {
                    NID = "";
                    if (TextUtils.isEmpty(lacstr)) {
                        Toast.makeText(CellSearchActivity.this, "请填写扇区号",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (TextUtils.isEmpty(cellstr)) {
                        Toast.makeText(CellSearchActivity.this, "请填写基站号",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                }
                LAC = lacstr;
                CID = cellstr;

                if (RegOperateTool.istoolTip) {
                    if (regOperateTool.isTheRegStatusOk(CellSearchActivity.this)) {
                        JizhanPos();
                    }
                } else {
                    if (RegOperateTool.isForbidden) {
                        Toast.makeText(CellSearchActivity.this, "注册码无效，请联系管理员", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    JizhanPos();
                }


                break;
            case R.id.cmcc_ll://移动

                PubUtill.DianxinClicked = false;
                MNC="0";
                LAC_et.setText("");
                CELL_et.setText("");
                cmcc_ll.setBackgroundColor(android.graphics.Color
                        .parseColor("#125E96"));
                unicom_ll.setBackgroundColor(android.graphics.Color
                        .parseColor("#0A8FCC"));
                telecom_ll.setBackgroundColor(android.graphics.Color
                        .parseColor("#0A8FCC"));
                telecomNID_ll.setVisibility(View.GONE);
                shanqu_tv.setText("LAC(扇区号) ：");
                jizhan_tv.setText("CID( 基站号 )：");
                break;
            case R.id.unicom_ll:
                LAC_et.setText("");
                CELL_et.setText("");
                PubUtill.DianxinClicked = false;
                MNC="1";
                unicom_ll.setBackgroundColor(android.graphics.Color
                        .parseColor("#125E96"));
                cmcc_ll.setBackgroundColor(android.graphics.Color
                        .parseColor("#0A8FCC"));
                telecom_ll.setBackgroundColor(android.graphics.Color
                        .parseColor("#0A8FCC"));
                telecomNID_ll.setVisibility(View.GONE);
                shanqu_tv.setText("LAC(扇区号) ：");
                jizhan_tv.setText("CID( 基站号 )：");
                break;
            case R.id.telecom_ll:
                LAC_et.setText("");
                CELL_et.setText("");
                NID_et.setText("");
                PubUtill.DianxinClicked = true;
                MNC="3";
                telecom_ll.setBackgroundColor(android.graphics.Color
                        .parseColor("#125E96"));
                cmcc_ll.setBackgroundColor(android.graphics.Color
                        .parseColor("#0A8FCC"));
                unicom_ll.setBackgroundColor(android.graphics.Color
                        .parseColor("#0A8FCC"));
                telecomNID_ll.setVisibility(View.VISIBLE);
                shanqu_tv.setText("SID(系统识别码)：");
                jizhan_tv.setText("BID(   基 站 号   )：");
                break;

            default:
                break;
        }
    }

    public void JizhanPos() {
        if (!DataUtil.isConnected(getApplicationContext())) {
            Toast.makeText(CellSearchActivity.this, "网络异常，请检查手机网络", Toast.LENGTH_SHORT).show();
            return;
        }
        dlgmsg = "正在进行基站查询...";
        ShowProgressDlg(dlgmsg);
        dlg.setMessage(dlgmsg);
        GetJizhanPosBySelf(LAC, CID, NID,MNC);

        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 0;
                myGPSMsgHandler.sendMessage(message);
            }
        }, 60 * 1000, 10 * 1000);
    }

    private void GetJizhanPosBySelf(final String lac, final String cid, final String nid,final String mnc) {

        CellPositionNetTask cellPositionNetTask = new CellPositionNetTask(new PositionCallBack() {
            @Override
            public void onSuccessed(CellPosition position) {
                String notice = position.getDesc();
                if ("未查询到数据!".equals(notice)) {
                    CellLocationFailed("未查询到位置信息，查询失败");
                }else{
                    GpsPos = resolveResponse(CellSearchActivity.this, position, lac, cid, nid);
                    myMessageHandler.sendEmptyMessage(6);
                }

            }

            @Override
            public void onErro() {
                CellLocationFailed("未查询到位置信息，查询失败");
            }
        });
        cellPositionNetTask.getCellPosition(lac, cid, nid,mnc);
    }

    /**
     * 解析数据
     *
     * @param context
     * @param cellPosition
     * @param lac
     * @param cid
     * @param nid
     * @return
     */
    private Position resolveResponse(Context context, CellPosition cellPosition, String lac, String cid, String nid) {

        Position p = new Position();


        Object model = cellPosition.getModel();
        //  "注册码已经禁用"
        if (model.equals("注册码已经禁用")) {
            Toast.makeText(context, "注册码已经禁用,请联系管理员", Toast.LENGTH_SHORT).show();
            return p;
        } else if (model.equals("注册码次数已用完")) {
            Toast.makeText(context, "注册码次数已用完,请联系管理员", Toast.LENGTH_SHORT).show();
            return p;
        } else if (model.equals("注册码使用时间过期")) {
            Toast.makeText(context, "注册码使用时间过期,请联系管理员", Toast.LENGTH_SHORT).show();
            return p;
        } else {
            p.lac = Integer.parseInt(lac);
            p.cid = Integer.parseInt(cid);
            if (!TextUtils.isEmpty(nid)) {
                p.nid = Integer.parseInt(nid);
            }
            String lat = cellPosition.getModel().getLatitude();
            String lng = cellPosition.getModel().getLongitude();
            if (lat.isEmpty() || lng.isEmpty()) {
                p.x = 0.0;
                p.y = 0.0;
            } else {
//                LatLng mLatLng = RegOperateTool.GpsCorrectToLatLng(Double.parseDouble(lat), Double.parseDouble(lng));
//                p.x = mLatLng.latitude;
//                p.y = mLatLng.longitude;
                p.x= Double.parseDouble(lat);
                p.y = Double.parseDouble(lng);
            }
            p.address = cellPosition.getModel().getAddress();
            return p;
        }
    }

    private void CellLocationFailed(String notice) {
        if (mTimer != null) {
            mTimer.cancel();
        }
        dlg.setMessage(notice);
        dlg.getButton(AlertDialog.BUTTON_POSITIVE).setText("返回");
    }


    Handler myMessageHandler = new Handler() {

        public void handleMessage(Message msg) {

            switch (msg.what) {

                case 6:// JiZhan position success

                    Log.d("debug", "jizhan pos succeed");

                    if (mTimer != null) {
                        mTimer.cancel();
                    }
                    if (posprogress != null) {
                        dlg.dismiss();
                    }

//				Toast.makeText(CellSearchActivity.this, "基站查询成功！",
//						Toast.LENGTH_SHORT).show();
                    if (GpsPos != null) {
                        Intent intent = new Intent();
                        intent.setClass(CellSearchActivity.this, SearchMapActivity.class);
                        intent.putExtra("gpspos", GpsPos);
                        startActivity(intent);
                    }
                    // SetCenterPos(GpsPos);

                    break;

                case 7:// JiZhan position failed,self pos failed

                    Log.d("debug", "jizhan pos failed");

                    if (mTimer != null) {
                        mTimer.cancel();
                    }

                    dlgmsg = dlgmsg + "\n" + "基站定位失败 ";
                    dlg.setMessage(dlgmsg);
                    dlg.getButton(AlertDialog.BUTTON_POSITIVE).setText("返回");

                    break;

                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
    Handler myGPSMsgHandler = new Handler() {

        public void handleMessage(Message msg) {

            switch (msg.what) {

                case 0:// JIzhan position timeout

                    if (mTimer != null) {
                        mTimer.cancel();
                    }

                    dlgmsg = dlgmsg + "\n" + "网络连接超时，基站查询失败 ";
                    dlg.setMessage(dlgmsg);

                    // android.os.Process.killProcess(android.os.Process.myPid());

                    // 设置ProgressDialog 的一个Button
                    dlg.getButton(AlertDialog.BUTTON_POSITIVE).setText("返回");
                    break;

                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public void ShowProgressDlg(String info) {
        // 创建ProgressDialog对象
        posprogress = new AlertDialog.Builder(this);

        // 设置进度条风格，风格为圆形，旋转的
        // posprogress.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        // 设置ProgressDialog 标题
        posprogress.setTitle("请稍候");

        posprogress.setMessage(info);

        posprogress.setCancelable(false);

        // 设置ProgressDialog 的一个Button
        posprogress.setPositiveButton("放弃定位",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int i) {

                        if (dlg != null
                                && dlg.getButton(AlertDialog.BUTTON_POSITIVE)
                                .getText().toString()
                                .equalsIgnoreCase("返回")) {
                            dialog.dismiss();
                            // finish();

                            return;
                        }
                        // 点击“确定按钮”取消对话框
                        if (mTimer != null) {
                            mTimer.cancel();
                        }

                        dialog.dismiss();
                        // android.os.Process.killProcess(android.os.Process.myPid());


                    }
                });

        // 让ProgressDialog显示
        dlg = posprogress.show();

    }
}
