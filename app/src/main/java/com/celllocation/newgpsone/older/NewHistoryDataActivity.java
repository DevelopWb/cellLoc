package com.celllocation.newgpsone.older;

import android.app.Activity;
import android.app.Dialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;


import com.celllocation.R;
import com.celllocation.newgpsone.database.DataHelper;
import com.celllocation.newgpsone.Utils.PubUtill;
import com.celllocation.newgpsone.bean.CellHisData;

import java.util.List;

public class NewHistoryDataActivity extends Activity {
    Button mbtnconfirm;
    MyHistoryDataListAdapter m_ListAdapter;
    private ListView m_listHistoryData;
    private SQLiteDatabase db;
    private Dialog dialog;
    private DataHelper helper;
    private String phone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cell_historydata);
        helper = new DataHelper(this);
        dialog = new Dialog(this, R.style.DialogStyle);
        dialog.setContentView(R.layout.dialog);
        dialog.show();
        phone = PubUtill.getIMEIDeviceId(this);
        m_listHistoryData = (ListView)findViewById(R.id.listHistoryData);



    }

    @Override
    protected void onResume() {
        super.onResume();
        List<CellHisData> arrays = helper.GetCellHisDatas(phone);
        m_ListAdapter = new MyHistoryDataListAdapter(this,arrays);
        m_listHistoryData.setAdapter(m_ListAdapter);
        dialog.dismiss();
    }

    @Override
    protected void onDestroy() {
        if (dialog!=null) {
            dialog.dismiss();
            dialog=null;
        }
        super.onDestroy();
    }
}

