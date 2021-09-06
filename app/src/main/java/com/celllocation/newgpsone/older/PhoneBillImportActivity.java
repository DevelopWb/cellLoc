package com.celllocation.newgpsone.older;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.celllocation.R;
import com.celllocation.newgpsone.database.DataHelper;
import com.celllocation.newgpsone.Utils.PubUtill;
import com.celllocation.newgpsone.bean.ExcelUtils;
import com.celllocation.newgpsone.bean.FormInfo;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by Administrator on 2016/11/16.
 */

public class PhoneBillImportActivity extends Activity implements View.OnClickListener {


    private TextView num_path;

    String[] title = {"PHONE", "LAC", "CID", "日期"};
    String[] title_dianxin = {"PHONE", "SID", "BID", "NID", "日期"};
    private EditText phone_name;
    private EditText phone_num;
    private EditText phone_detail;
    private DataHelper helper;
    private PopupWindow pop_export;
    private PopupWindow pop_import;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.phone_import);
        helper = new DataHelper(this);
        num_path = (TextView) findViewById(R.id.Num_path);
        phone_name = (EditText) findViewById(R.id.phone_name);
        phone_num = (EditText) findViewById(R.id.phone_num);
        phone_detail = (EditText) findViewById(R.id.phone_detail);
        pop_import = pop_import();
        pop_export = pop_export();

    }

    private PopupWindow pop_import() {
        View view = LayoutInflater.from(this)
                .inflate(R.layout.pop_item, null);
        LinearLayout yidong_form_ll = (LinearLayout) view
                .findViewById(R.id.yidong_form_ll);
        LinearLayout liantong_form_ll = (LinearLayout) view
                .findViewById(R.id.liantong_form_ll);
        LinearLayout dianxin_form_ll = (LinearLayout) view
                .findViewById(R.id.dianxin_form_ll);
        yidong_form_ll.setOnClickListener(this);
        liantong_form_ll.setOnClickListener(this);
        dianxin_form_ll.setOnClickListener(this);
        PopupWindow pop_import = new PopupWindow(view, dip2px(this, 90), dip2px(this, 110), false);
        pop_import.setBackgroundDrawable(new BitmapDrawable());
        pop_import.setOutsideTouchable(true); // 设置点击窗口外边窗口消失
        pop_import.setFocusable(true);// 设置此参数获得焦点，否则无法点击

        return pop_import;
    }

    private PopupWindow pop_export() {
        View view = LayoutInflater.from(this)
                .inflate(R.layout.pop_item2, null);
        LinearLayout yidong_model_ll = (LinearLayout) view
                .findViewById(R.id.yidong_model_ll);
        LinearLayout liantong_model_ll = (LinearLayout) view
                .findViewById(R.id.liantong_model_ll);
        LinearLayout dianxin_model_ll = (LinearLayout) view
                .findViewById(R.id.dianxin_model_ll);
        yidong_model_ll.setOnClickListener(this);
        liantong_model_ll.setOnClickListener(this);
        dianxin_model_ll.setOnClickListener(this);
        PopupWindow pop_export = new PopupWindow(view, dip2px(this, 90), dip2px(this, 110), false);
        pop_export.setBackgroundDrawable(new BitmapDrawable());
        pop_export.setOutsideTouchable(true); // 设置点击窗口外边窗口消失
        pop_export.setFocusable(true);// 设置此参数获得焦点，否则无法点击

        return pop_export;
    }

    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    /**
     * 验证手机格式
     */
    public boolean isMobileNO(String mobiles) {
        /*
         * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		 * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
		 * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		 */
        String telRegex = "[1][3758]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles))
            return false;
        else
            return mobiles.matches(telRegex);
    }


// 将字符串转为时间戳


    public String getTime(String user_time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = null;
        Date d;
        try {
            d = sdf.parse(user_time);
            long l = d.getTime();
            str = String.valueOf(l);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 100) {
            if (data != null) {
                Uri uri = data.getData();
                if (!TextUtils.isEmpty(uri.toString())) {
                    String[] data_ = uri.toString().split(",");
                    num_path.setText("话单路径：" + data_[0]);
                    Long long1 = Long.parseLong(getTime(data_[1]));
                    Long long2 = Long.parseLong(getTime(data_[2]));
                    String importTime = data_[3];
                    // 将话单人信息存入本地数据库
                    String name = phone_name.getText().toString().trim();
                    String num = phone_num.getText().toString().trim();
                    String detail = phone_detail.getText().toString().trim();
                    String startTime = null;
                    String endTime = null;

                    if (long1 > long2) {
                        startTime = data_[2];
                        endTime = data_[1];
                    } else {
                        startTime = data_[1];
                        endTime = data_[2];
                    }
                    String startTime_ = startTime.substring(0, 10);
                    String endTime_ = endTime.substring(0, 10);
                    FormInfo fi = new FormInfo();
                    fi.setName(name);
                    fi.setPhone(num);
                    fi.setStartTime(startTime_);
                    fi.setEndTime(endTime_);
                    fi.setDetail(detail);
                    fi.setImportTime(importTime);
                    helper.SaveFormInfo(fi);
                    if (pop_import.isShowing()) {
                        pop_import.dismiss();
                    }
                    Toast.makeText(this, "导入完成", Toast.LENGTH_SHORT).show();
                }


            }

        }
        if (requestCode == 81 || requestCode == 82) {//移动联通话单模板导出
            if (data != null) {
                File file;
                Uri uri = data.getData();
                String exportPath = uri.toString();
                // 导出话单模板
                if (requestCode == 81) {
                    file = new File(exportPath + "/移动话单模板");
                    makeDir(file);
                    ExcelUtils.initExcel(file.toString() + "/移动话单模板.xls", title);
                } else {
                    file = new File(exportPath + "/联通话单模板");
                    makeDir(file);
                    ExcelUtils.initExcel(file.toString() + "/联通话单模板.xls", title);
                }

                if (pop_export.isShowing()) {
                    pop_export.dismiss();
                }
                Toast.makeText(this, "模板已导出", Toast.LENGTH_SHORT).show();
            }

        }
        if (requestCode == 83) {//电信话单模板导出
            if (data != null) {
                Uri uri = data.getData();
                String exportPath = uri.toString();
                // 导出话单模板
                File file = new File(exportPath + "/电信话单模板");
                makeDir(file);
                ExcelUtils.initExcel(file.toString() + "/电信话单模板.xls", title_dianxin);
                if (pop_export.isShowing()) {
                    pop_export.dismiss();
                }
                Toast.makeText(this, "模板已导出", Toast.LENGTH_SHORT).show();
            }

        }

    }
    public static void makeDir(File dir) {
        if (!dir.getParentFile().exists()) {
            makeDir(dir.getParentFile());
        }
        dir.mkdir();
    }
    /**
     * 导入话单数据
     *
     * @param view
     */
    public void ImportPhoneNum(View view) {
        String name = phone_name.getText().toString().trim();
        String num = phone_num.getText().toString().trim();
        String detail = phone_detail.getText().toString().trim();
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(num) || TextUtils.isEmpty(detail)) {
            Toast.makeText(this, "请将上面的信息填写完整", Toast.LENGTH_SHORT).show();
            return;
        } else {
            if (num.length() != 11) {
                Toast.makeText(this, "手机格式错误", Toast.LENGTH_SHORT).show();
                return;
            } else {
                if (!isMobileNO(num)) {
                    Toast.makeText(this, "手机格式错误", Toast.LENGTH_SHORT).show();
                    return;
                }

            }


            pop_import.showAsDropDown(view, 65, 10);


        }

    }

    /**
     * 导出话单模板
     *
     * @param view
     */
    public void ExportPhoneModel(View view) {

        pop_export.showAsDropDown(view, 65, 10);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            /**
             * 导入移动表格
             */
            case R.id.yidong_form_ll:
                PubUtill.isDianxin = false;
                Intent intent_yidong = new Intent(PhoneBillImportActivity.this, FileManagerActivity.class);
                startActivityForResult(intent_yidong, 0);
                break;
            case R.id.liantong_form_ll:
                PubUtill.isDianxin = false;
                Intent intent_liantong = new Intent(PhoneBillImportActivity.this, FileManagerActivity.class);
                startActivityForResult(intent_liantong, 0);
                break;
            case R.id.dianxin_form_ll:
                PubUtill.isDianxin = true;
                Intent intent_dianxin = new Intent(PhoneBillImportActivity.this, FileManagerActivity.class);
                startActivityForResult(intent_dianxin, 0);
                break;
            /**
             * 导出移动模板
             */
            case R.id.yidong_model_ll:
                Intent intent_yidong_m = new Intent(PhoneBillImportActivity.this, FileManagerExportActivity.class);
                startActivityForResult(intent_yidong_m, 81);
                break;
            case R.id.liantong_model_ll:
                Intent intent_liantong_m = new Intent(PhoneBillImportActivity.this, FileManagerExportActivity.class);
                startActivityForResult(intent_liantong_m, 82);
                break;
            case R.id.dianxin_model_ll:
                Intent intent_dianxin_m = new Intent(PhoneBillImportActivity.this, FileManagerExportActivity.class);
                startActivityForResult(intent_dianxin_m, 83);
                break;
            default:
                break;
        }
    }
}
