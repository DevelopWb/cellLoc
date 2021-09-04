package com.celllocation.newgpsone;

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
     * �ļ����У�����
     */
    private List<String> items = null;
    /**
     * �ļ����У�·��
     */
    private List<String> paths = null;
    /**
     * ��Ŀ¼
     **/
    private String rootPath = "/";
    /**
     * ��ʾ��ǰĿ¼
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
        progress.setTitle("�ļ��������");
        progress.setCancelable(false);
        progress.setMessage("���ڵ��룬���Ժ�...");
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
                Toast.makeText(this, "�뽫excel��ʽת��Ϊ.xls������", Toast.LENGTH_SHORT).show();
            }

        }
    }

    public void finish_MyFile(View v) {
        finish();
    }

    /**
     * ��ȡָ��Ŀ¼�µ������ļ�(��)
     *
     * @param filePath
     */
    private void getFileDir(String filePath) {
        mPath.setText(filePath);
        items = new ArrayList<String>();
        paths = new ArrayList<String>();
        File f = new File(filePath);
        File[] files = f.listFiles();

        // ������ʾ �����ظ�Ŀ¼��+"�ϼ�Ŀ¼"
        if (!filePath.equals(rootPath)) {
            items.add("rootPath");
            paths.add(rootPath);

            items.add("parentPath");
            paths.add(f.getParent());
        }

        // ������
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
            Log.i("hnyer", filePath + "�����ļ�");
        }

        setListAdapter(new MyAdapter(this, items, paths));
    }

    class MyAsyncTask extends AsyncTask<String, Integer, String> {

        private Context context;

        MyAsyncTask(Context context) {
            this.context = context;
        }

        //ִ���첽����doInBackground��֮ǰִ�У�������ui�߳���ִ��
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //��ʼ���� �Ի����������ʾ
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
                if (PubUtill.isDianxin) {
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
                        // getCell(Col,Row)��õ�Ԫ���ֵ
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
                            if (PubUtill.isDianxin) {

                                if (data_.length > 0) {
                                    if (!TextUtils.isEmpty(data_[0])) {
                                        if ( !isMobileNO(data_[0])) {
                                            return "�ֻ���ʽ����";
                                        }
                                    }
                                    if (!TextUtils.isEmpty(data_[4])) {
                                        if ( !isDateStringValid(data_[4])) {
                                            return "���ڸ�ʽ����";
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
                                            return "�ֻ���ʽ����";
                                        }
                                    }
                                    if (!TextUtils.isEmpty(data_[3])) {
                                        if ( !isDateStringValid(data_[3])) {
                                            return "���ڸ�ʽ����";
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
                                if (!titlle.contains("PHONE") && titlle.contains("LAC") && titlle.contains("CID") && titlle.contains("����")) {
                                    return "";
                                }
                                String[] titles = titlle.split(",");
                                if (PubUtill.isDianxin) {
                                    if (!titles[0].equals("PHONE")||!titles[1].equals("SID")||!titles[2].equals("BID")||!titles[3].equals("NID")||!titles[4].equals("����")) {
                                        return "";
                                    }
                                }else{
                                    if (!titles[0].equals("PHONE")||!titles[1].equals("LAC")||!titles[2].equals("CID")||!titles[3].equals("����")) {
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

        //��ui�߳���ִ�� ���Բ���ui
        @Override
        protected void onPostExecute(String string) {
            // TODO Auto-generated method stub
            super.onPostExecute(string);
            //������� �Ի������������
            progress.cancel();
            Uri uri = Uri.parse(string);
            if (uri.toString().equals("")) {
                Toast.makeText(context, "����ʧ��,�밴ģ��ĸ�ʽ��������", Toast.LENGTH_SHORT).show();
            } else if (uri.toString().equals("���ڸ�ʽ����")) {
                Toast.makeText(context, "����ʧ��,���ڸ�ʽ����!", Toast.LENGTH_SHORT).show();
            }else if (uri.toString().equals("�ֻ���ʽ����")) {
                Toast.makeText(context, "����ʧ��,�ֻ���ʽ����!", Toast.LENGTH_SHORT).show();
            }else {
                Intent intent = new Intent();
                intent.setData(uri);
                setResult(100, intent);
                finish();
            }

        }

        /*
         * ��doInBackground�������Ѿ�����publishProgress���� ���������ִ�н��Ⱥ�
         * ����������� ʵ�ֽ������ĸ���
         * */
        @Override
        protected void onProgressUpdate(Integer... values) {
            // TODO Auto-generated method stub
            super.onProgressUpdate(values);
            progress.setProgress(values[0]);
        }
    }
    /**
     * �ж��ַ����Ƿ�Ϊ���ڸ�ʽ
     * @param date
     * @return
     */
    public boolean isDateStringValid(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-DD");
        // �������Ϊ��
        try {
            sdf.parse(date);
            return true;
        } catch (java.text.ParseException e) {
            return false;
        }
    }

    /**
     * ��֤�ֻ���ʽ
     */
    public boolean isMobileNO(String mobiles) {
        /*
         * �ƶ���134��135��136��137��138��139��150��151��157(TD)��158��159��187��188
		 * ��ͨ��130��131��132��152��155��156��185��186 ���ţ�133��153��180��189����1349��ͨ��
		 * �ܽ��������ǵ�һλ�ض�Ϊ1���ڶ�λ�ض�Ϊ3��5��8������λ�õĿ���Ϊ0-9
		 */
        String telRegex = "[1][3758]\\d{9}";// "[1]"�����1λΪ����1��"[358]"����ڶ�λ����Ϊ3��5��8�е�һ����"\\d{9}"��������ǿ�����0��9�����֣���9λ��
        if (TextUtils.isEmpty(mobiles))
            return false;
        else
            return mobiles.matches(telRegex);
    }
    @Override
    protected void onDestroy() {
        PubUtill.isDianxin = false;
        super.onDestroy();
    }
}
