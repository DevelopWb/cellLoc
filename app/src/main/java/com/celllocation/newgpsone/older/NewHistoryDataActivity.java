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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cell_historydata);



    }


}

