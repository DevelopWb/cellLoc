package com.celllocation.newgpsone;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.celllocation.newgpsone.bean.CellHisData;
import com.celllocation.newgpsone.bean.FormInfo;
import com.celllocation.newgpsone.bean.PeopleLocation;

public class SqliteHelper extends SQLiteOpenHelper {

    //��վ��λ�еĻ���
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


        //��վ��λ�� �ƶ���ͨ�Ļ���
        db.execSQL("CREATE TABLE IF NOT EXISTS " + PHONE_NO + "(" + PhoneNO.ID
                + " integer primary key," + PhoneNO.PHONE + " varchar,"
                + PhoneNO.LAC + " varchar," + PhoneNO.CID + " varchar," + PhoneNO.NID + " varchar,"
                + PhoneNO.TIME + " varchar," + PhoneNO.IMPORT_TIME + " varchar" + ")");

//��վ��λ�У���������Ϣ
        db.execSQL("CREATE TABLE IF NOT EXISTS " + FORM_INFO + "(" + FormInfo.ID
                + " integer primary key," + FormInfo.NAME + " varchar," + FormInfo.PHONE + " varchar,"
                + FormInfo.STARTTIME + " varchar," + FormInfo.ENDTIME + " varchar,"
                + FormInfo.DETAIL + " varchar," + FormInfo.IMPORT_TIME + " varchar" + ")");

        //��վ��λ�У�������λ����Ϣ
        db.execSQL("CREATE TABLE IF NOT EXISTS " + PEOPLE_LOCATION + "(" + PeopleLocation.ID
                + " integer primary key," + PeopleLocation.LAT + " varchar," + PeopleLocation.LNG + " varchar," + PeopleLocation.ADDRESS + " varchar,"
                + PeopleLocation.LAC + " varchar," + PeopleLocation.CID + " varchar," + PeopleLocation.NID + " varchar,"
                + PeopleLocation.TIME + " varchar," + PeopleLocation.IMPORT_TIME + " varchar" + ")");
//��վ��λ�У���վ��¼����
        db.execSQL("CREATE TABLE IF NOT EXISTS " + CELL_HISDATA + "(" + CellHisData.ID
                + " integer primary key," + CellHisData.PHONE + " varchar," + CellHisData.LNG + " varchar," + CellHisData.LAT + " varchar,"
                + CellHisData.LAC + " varchar," + CellHisData.CID + " varchar," + CellHisData.NID + " varchar,"
                + CellHisData.ADDRESS + " varchar," + CellHisData.ACCURACY + " varchar," + CellHisData.TIME + " varchar" + ")");


    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

//		db.execSQL("DROP TABLE IF EXISTS " + TB_NAME);
//
//		onCreate(db);

    }
}
