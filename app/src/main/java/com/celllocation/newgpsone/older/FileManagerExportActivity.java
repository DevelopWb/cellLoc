package com.celllocation.newgpsone.older;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import com.celllocation.R;
import com.celllocation.newgpsone.bean.MyAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Administrator on 2016/11/17.
 */

public class FileManagerExportActivity extends ListActivity {

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
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.model_export);
        mPath = (TextView) findViewById(R.id.mPath);
        getFileDir("/mnt/sdcard");
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        file = new File(paths.get(position));
        if (file.isDirectory()) {
            getFileDir(paths.get(position));
        }
    }

    public void MakeSureThePath(View v) {
        Uri uri = null;
        if (file==null) {
            uri = Uri.parse("/mnt/sdcard");
        }else{
            uri = Uri.parse(file.getPath());
        }

        Intent intent = new Intent();
        intent.setData(uri);
        setResult(99,intent);
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

}
