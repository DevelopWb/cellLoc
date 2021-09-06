package com.celllocation.newgpsone.older;

import android.app.Dialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.celllocation.R;
import com.celllocation.newgpsone.Utils.PubUtill;
import com.celllocation.newgpsone.Utils.RegOperateTool;

import static com.celllocation.R.layout.tab;
import static com.celllocation.newgpsone.bean.DataUtil.dip2px;


/**
 * Created by Administrator on 2016/11/9.
 */

public class CellPositionActivity extends TabActivity {


    private RegOperateTool rot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.cell_position);
        initHost();
//        initReg();
    }

    private void initReg() {
        rot = new RegOperateTool(this, "");
        rot.SetCancelCallBack(new RegOperateTool.CancelCallBack() {
            @Override
            public void toFinishActivity() {
                finish();
            }
        });
    }

    private void initHost() {
        final TabHost host = getTabHost();
        TabHost.TabSpec tab1 = host.newTabSpec("tab1");
        TabHost.TabSpec tab2 = host.newTabSpec("tab2");
        TabHost.TabSpec tab3 = host.newTabSpec("tab3");

        View view1 = LayoutInflater.from(this).inflate(R.layout.tab, null);
        final ImageView iv1 = (ImageView) view1.findViewById(R.id.image_iv);
        iv1.setBackgroundResource(R.drawable.cell_search_press);
        final TextView tv1 = (TextView) view1.findViewById(R.id.textview_iv);
        tv1.setText("基站查询");
        tv1.setTextColor(getResources().getColor(R.color.blue_text));
        Intent intent1 = new Intent(this, CellSearchActivity.class);
        tab1.setIndicator(view1).setContent(intent1);
        host.addTab(tab1);


        View view2 = LayoutInflater.from(this).inflate(tab, null);
        final ImageView iv2 = (ImageView) view2.findViewById(R.id.image_iv);
        iv2.setBackgroundResource(R.drawable.cell_location_normal);
        final TextView tv2 = (TextView) view2.findViewById(R.id.textview_iv);
        tv2.setText("本机定位");
        tv2.setTextColor(getResources().getColor(R.color.white));
        final Intent intent2 = new Intent(this, NewCellActivity.class);
        tab2.setIndicator(view2).setContent(intent2);
        host.addTab(tab2);


        View view3 = LayoutInflater.from(this).inflate(tab, null);
        final ImageView iv3 = (ImageView) view3.findViewById(R.id.image_iv);
        iv3.setBackgroundResource(R.drawable.cell_his_search_normal);
        final TextView tv3 = (TextView) view3.findViewById(R.id.textview_iv);
        tv3.setText("基站记录");
        tv3.setTextColor(getResources().getColor(R.color.white));
        Intent intent3 = new Intent(this, NewHistoryDataActivity.class);
        tab3.setIndicator(view3).setContent(intent3);
        host.addTab(tab3);

        host.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (tabId.equals("tab1")) {
                    iv1.setTag("press");
                    setBackground(iv1, iv2, iv3, tv1, tv2, tv3);
                } else if (tabId.equals("tab2")) {
                    iv2.setTag("press");
                    setBackground(iv1, iv2, iv3, tv1, tv2, tv3);
                } else if (tabId.equals("tab3")) {
                    iv3.setTag("press");
                    setBackground(iv1, iv2, iv3, tv1, tv2, tv3);
                }
            }
        });

        host.getTabWidget().getChildTabViewAt(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCanUseSim(CellPositionActivity.this)) {
                    if (host.getCurrentTab() == 1 && PubUtill.isDrawable) {
                        PubUtill.isDrawable = false;
                        Intent intent = new Intent("REFRESH");
                        sendBroadcast(intent);
                    } else {
                        host.setCurrentTab(1);
                    }

                } else {
                    Toast.makeText(CellPositionActivity.this, "您当前sim卡无效,请检查sim卡是否安装",
                            Toast.LENGTH_LONG).show();
                }


            }
        });
    }

    /**
     * 检测sim卡是否可读
     *
     * @return
     */
    // sim卡是否可读
    public static boolean isCanUseSim(Context context) {
        try {
            TelephonyManager mgr = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);

            return TelephonyManager.SIM_STATE_READY == mgr.getSimState();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void setBackground(ImageView iv1, ImageView iv2, ImageView iv3, TextView tv1, TextView tv2, TextView tv3) {
        if (iv1.getTag() != null) {
            iv1.setBackgroundResource(R.drawable.cell_search_press);
            tv1.setTextColor(getResources().getColor(R.color.blue_text));
            iv1.setTag(null);
        } else {
            tv1.setTextColor(getResources().getColor(R.color.white));
            iv1.setBackgroundResource(R.drawable.cell_search_normal);
        }
        if (iv2.getTag() != null) {
            iv2.setBackgroundResource(R.drawable.cell_location_press);
            tv2.setTextColor(getResources().getColor(R.color.blue_text));
            iv2.setTag(null);
        } else {
            tv2.setTextColor(getResources().getColor(R.color.white));
            iv2.setBackgroundResource(R.drawable.cell_location_normal);
        }

        if (iv3.getTag() != null) {
            tv3.setTextColor(getResources().getColor(R.color.blue_text));
            iv3.setBackgroundResource(R.drawable.cell_his_search_press);
            iv3.setTag(null);
        } else {
            tv3.setTextColor(getResources().getColor(R.color.white));
            iv3.setBackgroundResource(R.drawable.cell_his_search_normal);

        }

    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                showQuitDialog();
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    private void showQuitDialog() {

        View v = LayoutInflater.from(this).inflate(R.layout.singlepos_exitconfirm, null);
        final Dialog dialog_edit = new Dialog(this, R.style.DialogStyle);
        dialog_edit.setCanceledOnTouchOutside(false);
        dialog_edit.show();
        Window window = dialog_edit.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        window.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        lp.width = dip2px(this, 220); // 宽度
        lp.height = dip2px(this, 170); // 高度
        //  lp.alpha = 0.7f; // 透明度
        window.setAttributes(lp);
        window.setContentView(v);
        ImageButton sure_ib = (ImageButton) v.findViewById(R.id.imagebuttonexitok);
        ImageButton cancel_ib = (ImageButton) v.findViewById(R.id.imageButtonexitcancel);
        cancel_ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_edit.dismiss();

            }
        });
        sure_ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_edit.dismiss();
                finish();
            }
        });
    }


}
