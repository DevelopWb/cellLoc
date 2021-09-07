package com.celllocation.newgpsone.functions.celllocate;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.celllocation.R;
import com.celllocation.newgpsone.Utils.PubUtill;
import com.celllocation.newgpsone.Utils.RegOperateTool;
import com.celllocation.newgpsone.base.BaseAppFragment;
import com.celllocation.newgpsone.bean.CellPosition;
import com.celllocation.newgpsone.bean.DataUtil;
import com.celllocation.newgpsone.bean.Position;
import com.celllocation.newgpsone.cellInfos.CellPositionNetTask;
import com.celllocation.newgpsone.cellInfos.PositionCallBack;
import com.celllocation.newgpsone.functions.MainPageContract;
import com.celllocation.newgpsone.functions.MainPagePresent;
import com.celllocation.newgpsone.older.SearchMapActivity;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2021-09-06 10:06
 * @UpdateUser: 更新者
 * @UpdateDate: 2021-09-06 10:06
 */
public class CellSearchFragment extends BaseAppFragment<MainPagePresent> implements MainPageContract.IMainPageView, View.OnClickListener {


    private TextView cell_search_tv, qita_tv, shanqu_tv, jizhan_tv;

    private LinearLayout unicom_ll, telecom_ll, telecomNID_ll;
    private LinearLayout cmcc_ll;
    private EditText LAC_et;
    private EditText CELL_et;
    private EditText NID_et;

    private String LAC = "";
    private String CID = "";
    private String NID = "";
    private String MNC = "0";//0代表移动，1代表联通

    Position GpsPos;
    private String dlgmsg;
    AlertDialog dlg = null;
    AlertDialog.Builder posprogress = null;

    private RegOperateTool regOperateTool;
    private View view;
    private ImageView mCmccLogoIv;
    private ImageView mUnicomLogoIv;
    private ImageView mTelecomLogoIv;

    @Override
    protected MainPagePresent createPresenter() {
        return new MainPagePresent();
    }

    @Override
    protected void lazyLoad() {
        ((CellLocateActivity) getBaseActivity()).setTitleName("基站定位");
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.cellsearch_activity;
    }

    @Override
    protected void initView() {
        regOperateTool = new RegOperateTool(mContext);
        cell_search_tv = (TextView) getView(R.id.cell_search_tv);
        cmcc_ll = (LinearLayout) getView(R.id.cmcc_ll);
        unicom_ll = (LinearLayout) getView(R.id.unicom_ll);
        telecom_ll = (LinearLayout) getView(R.id.telecom_ll);
        telecomNID_ll = (LinearLayout) getView(R.id.telecomNID_ll);
        LAC_et = (EditText) getView(R.id.LAC_et);
        CELL_et = (EditText) getView(R.id.CELL_et);
        NID_et = (EditText) getView(R.id.NID_et);
        shanqu_tv = (TextView) getView(R.id.shanqu_tv);
        jizhan_tv = (TextView) getView(R.id.jizhan_tv);
        qita_tv = (TextView) getView(R.id.qita_tv);
        cell_search_tv.setOnClickListener(this);
        cmcc_ll.setOnClickListener(this);
        unicom_ll.setOnClickListener(this);
        telecom_ll.setOnClickListener(this);
        mCmccLogoIv = (ImageView) getView(R.id.cmcc_logo_iv);
        mCmccLogoIv.setImageResource(R.mipmap.cmcc_press_icon);
        mUnicomLogoIv = (ImageView) getView(R.id.unicom_logo_iv);
        mTelecomLogoIv = (ImageView) getView(R.id.telecom_logo_iv);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onSuccess(String tag, Object o) {

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.cell_search_tv:

                String lacstr = LAC_et.getText().toString();
                String cellstr = CELL_et.getText().toString();

                if (PubUtill.DianxinClicked) {
                    if (TextUtils.isEmpty(lacstr)) {
                        Toast.makeText(mContext, "请填系统识别码",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (TextUtils.isEmpty(cellstr)) {
                        Toast.makeText(mContext, "请填写基站号",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String nidstr = NID_et.getText().toString();
                    if (TextUtils.isEmpty(nidstr)) {
                        Toast.makeText(mContext, "请填写网络识别码",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    NID = nidstr;

                } else {
                    NID = "";
                    if (TextUtils.isEmpty(lacstr)) {
                        Toast.makeText(mContext, "请填写扇区号",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (TextUtils.isEmpty(cellstr)) {
                        Toast.makeText(mContext, "请填写基站号",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                }
                LAC = lacstr;
                CID = cellstr;

                if (RegOperateTool.istoolTip) {
                    if (regOperateTool.isTheRegStatusOk(mContext)) {
                        JizhanPos();
                    }
                } else {
                    if (RegOperateTool.isForbidden) {
                        Toast.makeText(mContext, "注册码无效，请联系管理员", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    JizhanPos();
                }


                break;
            case R.id.cmcc_ll://移动

                PubUtill.DianxinClicked = false;
                MNC = "0";
                LAC_et.setText("");
                CELL_et.setText("");
                mCmccLogoIv.setImageResource(R.mipmap.cmcc_press_icon);
                mUnicomLogoIv.setImageResource(R.mipmap.unicom_normal_icon);
                mTelecomLogoIv.setImageResource(R.mipmap.telecom_normal_icon);
                telecomNID_ll.setVisibility(View.GONE);
                shanqu_tv.setText("LAC(扇区号) ：");
                jizhan_tv.setText("CID( 基站号 )：");
                break;
            case R.id.unicom_ll:
                LAC_et.setText("");
                CELL_et.setText("");
                PubUtill.DianxinClicked = false;
                MNC = "1";
                mCmccLogoIv.setImageResource(R.mipmap.cmcc_normal_icon);
                mUnicomLogoIv.setImageResource(R.mipmap.unicom_press_icon);
                mTelecomLogoIv.setImageResource(R.mipmap.telecom_normal_icon);
                telecomNID_ll.setVisibility(View.GONE);
                shanqu_tv.setText("LAC(扇区号) ：");
                jizhan_tv.setText("CID( 基站号 )：");
                break;
            case R.id.telecom_ll:
                LAC_et.setText("");
                CELL_et.setText("");
                NID_et.setText("");
                PubUtill.DianxinClicked = true;
                MNC = "3";
                mCmccLogoIv.setImageResource(R.mipmap.cmcc_normal_icon);
                mUnicomLogoIv.setImageResource(R.mipmap.unicom_normal_icon);
                mTelecomLogoIv.setImageResource(R.mipmap.telecom_press_icon);
                telecomNID_ll.setVisibility(View.VISIBLE);
                shanqu_tv.setText("SID(系统识别码)：");
                jizhan_tv.setText("BID(   基 站 号   )：");
                break;

            default:
                break;
        }
    }

    public void JizhanPos() {
        if (!DataUtil.isConnected(mContext)) {
            Toast.makeText(mContext, "网络异常，请检查手机网络", Toast.LENGTH_SHORT).show();
            return;
        }
        dlgmsg = "正在进行基站查询...";
        ShowProgressDlg(dlgmsg);
        dlg.setMessage(dlgmsg);
        GetJizhanPosBySelf(LAC, CID, NID, MNC);

    }

    private void GetJizhanPosBySelf(final String lac, final String cid, final String nid, final String mnc) {

        CellPositionNetTask cellPositionNetTask = new CellPositionNetTask(new PositionCallBack() {
            @Override
            public void onSuccessed(CellPosition position) {
                String notice = position.getDesc();
                if ("未查询到数据!".equals(notice)) {
                    CellLocationFailed("未查询到位置信息，查询失败");
                } else {
                    GpsPos = resolveResponse(mContext, position, lac, cid, nid);
                    myMessageHandler.sendEmptyMessage(6);
                }

            }

            @Override
            public void onErro() {
                CellLocationFailed("未查询到位置信息，查询失败");
            }
        });
        cellPositionNetTask.getCellPosition(lac, cid, nid, mnc);
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
                p.x = Double.parseDouble(lat);
                p.y = Double.parseDouble(lng);
            }
            p.address = cellPosition.getModel().getAddress();
            return p;
        }
    }

    private void CellLocationFailed(String notice) {
        dlg.setMessage(notice);
        dlg.getButton(AlertDialog.BUTTON_POSITIVE).setText("返回");
    }


    Handler myMessageHandler = new Handler() {

        public void handleMessage(Message msg) {

            switch (msg.what) {

                case 6:// JiZhan position success

                    Log.d("debug", "jizhan pos succeed");

                    if (posprogress != null) {
                        dlg.dismiss();
                    }

//				Toast.makeText(mContext, "基站查询成功！",
//						Toast.LENGTH_SHORT).show();
                    if (GpsPos != null) {
                        Intent intent = new Intent();
                        intent.setClass(mContext, SearchMapActivity.class);
                        intent.putExtra("gpspos", GpsPos);
                        startActivity(intent);
                    }
                    // SetCenterPos(GpsPos);

                    break;

                case 7:// JiZhan position failed,self pos failed

                    Log.d("debug", "jizhan pos failed");

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

    public void ShowProgressDlg(String info) {
        // 创建ProgressDialog对象
        posprogress = new AlertDialog.Builder(mContext);

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

                        dialog.dismiss();
                        // android.os.Process.killProcess(android.os.Process.myPid());


                    }
                });

        // 让ProgressDialog显示
        dlg = posprogress.show();

    }

}
