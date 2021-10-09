package com.celllocation.newgpsone.functions.celllocate;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.celllocation.R;
import com.celllocation.newgpsone.Utils.ObjectBox;
import com.celllocation.newgpsone.Utils.PublicUtill;
import com.celllocation.newgpsone.Utils.RegOperateTool;
import com.celllocation.newgpsone.base.BaseAppFragment;
import com.celllocation.newgpsone.bean.CellHisData;
import com.celllocation.newgpsone.bean.CellLocResultBean;
import com.celllocation.newgpsone.bean.DataUtil;
import com.celllocation.newgpsone.bean.Position;
import com.celllocation.newgpsone.database.DataHelper;
import com.celllocation.newgpsone.functions.BaseFunctionActivity;
import com.celllocation.newgpsone.functions.MainPageContract;
import com.celllocation.newgpsone.functions.MainPagePresent;
import com.celllocation.newgpsone.older.SearchMapActivity;
import com.juntai.disabled.basecomponent.utils.CalendarUtil;
import com.juntai.disabled.basecomponent.utils.ToastUtils;

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

    Position mGpsPos;
    AlertDialog.Builder posprogress = null;

    private RegOperateTool regOperateTool;
    private View view;
    private ImageView mCmccLogoIv;
    private ImageView mUnicomLogoIv;
    private ImageView mTelecomLogoIv;
    private boolean DianxinClicked = false;
    private DataHelper helper;

    @Override
    protected MainPagePresent createPresenter() {
        return new MainPagePresent();
    }

    @Override
    protected void lazyLoad() {
        ((BaseFunctionActivity) getBaseActivity()).setTitleName("基站定位");
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.cellsearch_activity;
    }

    @Override
    protected void initView() {
        helper = new DataHelper(mContext);
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
        switch (tag) {
            case MainPageContract.CELL_CDMA:
                CellLocResultBean cellLocResultBean = (CellLocResultBean) o;
                if (cellLocResultBean != null) {
                    if (0==cellLocResultBean.getErrCode()) {
                        mGpsPos = resolveResponse(mContext, cellLocResultBean, LAC, CID, NID);
                        if (mGpsPos != null) {
                            CellHisData cellHisData = new CellHisData();
                            cellHisData.setAddress(mGpsPos.address);
                            cellHisData.setLac(String.valueOf(mGpsPos.lac));
                            cellHisData.setCid(String.valueOf(mGpsPos.cid));
                            cellHisData.setNid(String.valueOf(mGpsPos.nid));
                            cellHisData.setLat(String.valueOf(cellLocResultBean.getLocation().getLatitude()));
                            cellHisData.setLng(String.valueOf(cellLocResultBean.getLocation().getLongitude()));
                            cellHisData.setTime(CalendarUtil.getCurrentTime());
                            cellHisData.setType(MNC);
                            //  保存基站查询历史数据
                            ObjectBox.get().boxFor(CellHisData.class).put(cellHisData);
                            Intent intent = new Intent();
                            intent.setClass(mContext, SearchMapActivity.class);
                            intent.putExtra("gpspos", mGpsPos);
                            startActivity(intent);
                        }
                    }else {
                        ToastUtils.toast(mContext,"无法获取基站信息位置,请确保基站信息输入正确");
                    }


                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.cell_search_tv:

                String lacstr = LAC_et.getText().toString();
                String cellstr = CELL_et.getText().toString();

                if (DianxinClicked) {
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
                        startCellLocate();
                    }
                } else {
                    if (RegOperateTool.isForbidden) {
                        Toast.makeText(mContext, "注册码无效，请联系管理员", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    startCellLocate();
                }


                break;
            case R.id.cmcc_ll://移动

                DianxinClicked = false;
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
                DianxinClicked = false;
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
                DianxinClicked = true;
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

    public void startCellLocate() {
        if (!DataUtil.isConnected(mContext)) {
            Toast.makeText(mContext, "网络异常，请检查手机网络", Toast.LENGTH_SHORT).show();
            return;
        }
        if (DianxinClicked) {
            mPresenter.cellLocateCDMA(LAC,CID,NID,PublicUtill.CDMA_CELL_LOC_KEY, MainPageContract.CELL_CDMA);
        }else {
            NID = "";
            mPresenter.cellLocateOther(LAC,CID,MNC,PublicUtill.OTHER_CELL_LOC_KEY, MainPageContract.CELL_CDMA);

        }


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
    private Position resolveResponse(Context context, CellLocResultBean cellPosition, String lac, String cid, String nid) {

        Position p = new Position();
            p.lac = Integer.parseInt(lac);
            p.cid = Integer.parseInt(cid);
            if (!TextUtils.isEmpty(nid)) {
                p.nid = Integer.parseInt(nid);
            }
            String lat = String.valueOf(cellPosition.getLocation().getLatitude());
            String lng = String.valueOf(cellPosition.getLocation().getLongitude());
            if (lat.isEmpty() || lng.isEmpty()) {
                p.x = 0.0;
                p.y = 0.0;
            } else {
                p.x = Double.parseDouble(lat);
                p.y = Double.parseDouble(lng);
            }
            p.address = cellPosition.getLocation().getAddressDescription();
            return p;
//        }
    }




}
