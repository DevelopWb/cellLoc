package com.celllocation.newgpsone;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.celllocation.R;
import com.celllocation.newgpsone.bean.FormInfo;
import com.celllocation.newgpsone.bean.FormInfoAdapter;

import java.util.List;


/**
 * Created by Administrator on 2016/11/21.
 */

public class PhoneDataHistoryActivity extends Activity {

    private ListView phoneHistoryData_lv;
    private DataHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_history);
        phoneHistoryData_lv = (ListView) findViewById(R.id.phoneHistoryData);
        helper = new DataHelper(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        GetDatas();
    }

    private void GetDatas() {

        List<FormInfo> arrays = helper.GetFormInfos();
       FormInfoAdapter adapter = new FormInfoAdapter(this, arrays);
        phoneHistoryData_lv.setAdapter(adapter);

    }

}
