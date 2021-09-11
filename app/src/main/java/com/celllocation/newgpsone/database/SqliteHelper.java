package com.celllocation.newgpsone.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.celllocation.newgpsone.bean.CellHisData;
import com.celllocation.newgpsone.bean.FormInfo;
import com.celllocation.newgpsone.bean.PeopleLocation;
import com.celllocation.newgpsone.bean.PhoneNO;

public class SqliteHelper extends SQLiteOpenHelper {

    //基站定位中的话单
    public static final String PHONE_NO = "phone_no";
    public static final String FORM_INFO = "form_info";
    public static final String PEOPLE_LOCATION = "people_location";
    public static final String CELL_HISDATA = "cell_hisdata";


    public SqliteHelper(Context context, String name, CursorFactory factory,
                        int version) {

        super(context, name, factory, version);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        //基站定位中 移动联通的话单
        db.execSQL("CREATE TABLE IF NOT EXISTS " + PHONE_NO + "(" + PhoneNO.ID
                + " integer primary key," + PhoneNO.PHONE + " varchar,"
                + PhoneNO.LAC + " varchar," + PhoneNO.CID + " varchar," + PhoneNO.NID + " varchar,"
                + PhoneNO.TIME + " varchar," + PhoneNO.IMPORT_TIME + " varchar" + ")");

//基站定位中，话单人信息
        db.execSQL("CREATE TABLE IF NOT EXISTS " + FORM_INFO + "(" + FormInfo.ID
                + " integer primary key," + FormInfo.NAME + " varchar," + FormInfo.PHONE + " varchar,"
                + FormInfo.STARTTIME + " varchar," + FormInfo.ENDTIME + " varchar,"
                + FormInfo.DETAIL + " varchar," + FormInfo.IMPORT_TIME + " varchar" + ")");

        //基站定位中，话单人位置信息
        db.execSQL("CREATE TABLE IF NOT EXISTS " + PEOPLE_LOCATION + "(" + PeopleLocation.ID
                + " integer primary key," + PeopleLocation.LAT + " varchar," + PeopleLocation.LNG + " varchar," + PeopleLocation.ADDRESS + " varchar,"
                + PeopleLocation.LAC + " varchar," + PeopleLocation.CID + " varchar," + PeopleLocation.NID + " varchar,"
                + PeopleLocation.TIME + " varchar," + PeopleLocation.IMPORT_TIME + " varchar" + ")");


    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

//		db.execSQL("DROP TABLE IF EXISTS " + TB_NAME);
//
//		onCreate(db);

    }
}
