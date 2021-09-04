package com.celllocation.newgpsone;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.Button;
import android.widget.ListView;

import com.celllocation.R;
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
        TelephonyManager tm = (TelephonyManager) this
                .getSystemService(Context.TELEPHONY_SERVICE);
        phone = tm.getDeviceId();
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

