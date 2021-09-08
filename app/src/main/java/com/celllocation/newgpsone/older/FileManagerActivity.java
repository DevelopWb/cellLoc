package com.celllocation.newgpsone.older;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.celllocation.R;
import com.celllocation.newgpsone.Utils.PublicUtill;
import com.celllocation.newgpsone.database.DataHelper;
import com.celllocation.newgpsone.bean.PhoneNO;
import com.celllocation.newgpsone.bean.DataUtil;
import com.celllocation.newgpsone.bean.MyAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 * Created by Administrator on 2016/11/17.
 */

public class FileManagerActivity extends ListActivity {

    /**
     * 文件（夹）名字
     */
    private List<String> items = null;
    /**
     * 文件（夹）路径
     */
    private List<String> paths = null;
    /**
     * 根目录
     **/
    private String rootPath = "/";
    /**
     * 显示当前目录
     **/
    private TextView mPath;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fileselect);
        mPath = (TextView) findViewById(R.id.mPath);
        getFileDir("/mnt/sdcard");
        progress = new ProgressDialog(this);
        progress.setTitle("文件导入进度");
        progress.setCancelable(false);
        progress.setMessage("正在导入，请稍候...");
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        File file = new File(paths.get(position));
        if (file.isDirectory()) {
            getFileDir(paths.get(position));
        } else {
            String filePath = file.getName();
            String path = filePath.substring(filePath.lastIndexOf(".") + 1);
            if (path.equals("xls")) {
                new MyAsyncTask(this).execute(file.getPath());
            } else {
                Toast.makeText(this, "请将excel格式转换为.xls后重试", Toast.LENGTH_SHORT).show();
            }

        }
    }

    public void finish_MyFile(View v) {
        finish();
    }

    /**
     * 获取指定目录下的所有文件(夹)
     *
     * @param filePath
     */
    private void getFileDir(String filePath) {
        mPath.setText(filePath);
        items = new ArrayList<String>();
        paths = new ArrayList<String>();
        File f = new File(filePath);
        File[] files = f.listFiles();

        // 用来显示 “返回根目录”+"上级目录"
        if (!filePath.equals(rootPath)) {
            items.add("rootPath");
            paths.add(rootPath);

            items.add("parentPath");
            paths.add(f.getParent());
        }

        // 先排序
        List<File> resultList = null;
        if (files != null) {
            resultList = new ArrayList<File>();
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                if (!file.getName().startsWith(".")) {
                    resultList.add(file);
                }
            }

            //
            Collections.sort(resultList, new Comparator<File>() {
                @Override
                public int compare(File bean1, File bean2) {
                    return bean1.getName().toLowerCase()
                            .compareTo(bean2.getName().toLowerCase());

                }
            });

            for (int i = 0; i < resultList.size(); i++) {
                File file = resultList.get(i);
                items.add(file.getName());
                paths.add(file.getPath());
            }
        } else {
            Log.i("hnyer", filePath + "无子文件");
        }

        setListAdapter(new MyAdapter(this, items, paths));
    }

    class MyAsyncTask extends AsyncTask<String, Integer, String> {

        private Context context;

        MyAsyncTask(Context context) {
            this.context = context;
        }

        //执行异步任务（doInBackground）之前执行，并且在ui线程中执行
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //开始下载 对话框进度条显示
            progress.show();
            progress.setProgress(0);
        }

        @Override
        protected String doInBackground(String... params) {

            String importTime = "";
            String filePath = params[0];
            StringBuffer timers = new StringBuffer();
            try {

                DataHelper helper = new DataHelper(context);
                InputStream is = new FileInputStream(filePath);
                Workbook workbook = Workbook.getWorkbook(is);
                Sheet sheet = workbook.getSheet(0);
                int sheetRows = sheet.getRows();
                int sheetNum = workbook.getNumberOfSheets();
                int sheetColumns = sheet.getColumns();
                if (PublicUtill.isDianxin) {
                    if (sheetColumns != 5) {
                        return "";
                    }
                }else{
                    if (sheetColumns != 4) {
                        return "";
                    }
                }
                if (sheetColumns > 5) {
                    return "";
                }
                StringBuffer sb = new StringBuffer();

                for (int i = 0; i < sheetRows; ++i) {
                    for (int j = 0; j < sheetColumns; ++j) {
                        // getCell(Col,Row)获得单元格的值
                        String content = sheet.getCell(j, i).getContents();
                        if (j == sheetColumns - 1) {
                            sb.append(content + ";");
                        } else {
                            sb.append(content + ",");
                        }

                    }
                }
                importTime = DataUtil.getDateToString(System
                        .currentTimeMillis());
                String[] data = sb.toString().split(";");
                if (data.length > 0) {
                    for (int i = 0; i < data.length; i++) {
                        if (i > 0) {
                            String[] data_ = data[i].split(",");
                            if (PublicUtill.isDianxin) {

                                if (data_.length > 0) {
                                    if (!TextUtils.isEmpty(data_[0])) {
                                        if ( !isMobileNO(data_[0])) {
                                            return "手机格式错误";
                                        }
                                    }
                                    if (!TextUtils.isEmpty(data_[4])) {
                                        if ( !isDateStringValid(data_[4])) {
                                            return "日期格式错误";
                                        }
                                    }
                                    if (i == 1) {
                                        timers.append("," + data_[4] + ",");
                                    }
                                    if (i == data.length - 1) {
                                        timers.append(data_[4]);
                                    }
                                    PhoneNO pn = new PhoneNO();
                                    pn.setPhoneNum(data_[0]);
                                    pn.setLac(data_[1]);
                                    pn.setCid(data_[2]);
                                    pn.setNid(data_[3]);
                                    pn.setTime(data_[4]);
                                    pn.setImportTime(importTime);
                                    helper.SavePhoneNo(pn);
                                }

                            } else {
                                if (data_.length > 0) {
                                    if (!TextUtils.isEmpty(data_[0])) {
                                        if ( !isMobileNO(data_[0])) {
                                            return "手机格式错误";
                                        }
                                    }
                                    if (!TextUtils.isEmpty(data_[3])) {
                                        if ( !isDateStringValid(data_[3])) {
                                            return "日期格式错误";
                                        }
                                    }
                                    if (i == 1) {
                                        timers.append("," + data_[3] + ",");
                                    }
                                    if (i == data.length - 1) {
                                        timers.append(data_[3]);
                                    }
                                    PhoneNO pn = new PhoneNO();
                                    pn.setPhoneNum(data_[0]);
                                    pn.setLac(data_[1]);
                                    pn.setCid(data_[2]);
                                    pn.setTime(data_[3]);
                                    pn.setImportTime(importTime);
                                    helper.SavePhoneNo(pn);
                                }

                            }
                            int progress = (int) (i / (float) data.length * 100);
                            publishProgress(progress);
                        } else {
                            String titlle = data[0];
                            if (!TextUtils.isEmpty(titlle)&&titlle!=null) {
                                if (!titlle.contains("PHONE") && titlle.contains("LAC") && titlle.contains("CID") && titlle.contains("日期")) {
                                    return "";
                                }
                                String[] titles = titlle.split(",");
                                if (PublicUtill.isDianxin) {
                                    if (!titles[0].equals("PHONE")||!titles[1].equals("SID")||!titles[2].equals("BID")||!titles[3].equals("NID")||!titles[4].equals("日期")) {
                                        return "";
                                    }
                                }else{
                                    if (!titles[0].equals("PHONE")||!titles[1].equals("LAC")||!titles[2].equals("CID")||!titles[3].equals("日期")) {
                                        return "";
                                    }
                                }

                            }

                        }
                    }

                }

                workbook.close();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (BiffException e) {
                e.printStackTrace();
            }
            return filePath + timers.toString() + "," + importTime;
        }

        //在ui线程中执行 可以操作ui
        @Override
        protected void onPostExecute(String string) {
            // TODO Auto-generated method stub
            super.onPostExecute(string);
            //下载完成 对话框进度条隐藏
            progress.cancel();
            Uri uri = Uri.parse(string);
            if (uri.toString().equals("")) {
                Toast.makeText(context, "导入失败,请按模板的格式导入数据", Toast.LENGTH_SHORT).show();
            } else if (uri.toString().equals("日期格式错误")) {
                Toast.makeText(context, "导入失败,日期格式错误!", Toast.LENGTH_SHORT).show();
            }else if (uri.toString().equals("手机格式错误")) {
                Toast.makeText(context, "导入失败,手机格式错误!", Toast.LENGTH_SHORT).show();
            }else {
                Intent intent = new Intent();
                intent.setData(uri);
                setResult(100, intent);
                finish();
            }

        }

        /*
         * 在doInBackground方法中已经调用publishProgress方法 更新任务的执行进度后
         * 调用这个方法 实现进度条的更新
         * */
        @Override
        protected void onProgressUpdate(Integer... values) {
            // TODO Auto-generated method stub
            super.onProgressUpdate(values);
            progress.setProgress(values[0]);
        }
    }
    /**
     * 判断字符串是否为日期格式
     * @param date
     * @return
     */
    public boolean isDateStringValid(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-DD");
        // 输入对象不为空
        try {
            sdf.parse(date);
            return true;
        } catch (java.text.ParseException e) {
            return false;
        }
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
    @Override
    protected void onDestroy() {
        PublicUtill.isDianxin = false;
        super.onDestroy();
    }
}
