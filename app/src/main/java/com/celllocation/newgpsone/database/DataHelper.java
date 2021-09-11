package com.celllocation.newgpsone.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.celllocation.newgpsone.bean.CellHisData;
import com.celllocation.newgpsone.bean.FormInfo;
import com.celllocation.newgpsone.bean.PeopleLocation;
import com.celllocation.newgpsone.bean.PhoneNO;

import java.util.ArrayList;
import java.util.List;

import static com.celllocation.newgpsone.database.SqliteHelper.CELL_HISDATA;
import static com.celllocation.newgpsone.database.SqliteHelper.FORM_INFO;

public class DataHelper {

    private static String DB_NAME = "mems.db";

    private static int DB_VERSION = 7;
    private SQLiteDatabase db;
    private SqliteHelper dbHelper;

    public DataHelper(Context context) {
        dbHelper = new SqliteHelper(context, DB_NAME, null, DB_VERSION);
        db = dbHelper.getWritableDatabase();
        dbHelper.onCreate(db);
    }

    public void Close() {
        db.close();
        dbHelper.close();
    }



    //基站定位中 移动联通话单数据存储

    public Long SavePhoneNo(PhoneNO user) {
        ContentValues values = new ContentValues();
        values.put(PhoneNO.PHONE, user.getPhoneNum());
        values.put(PhoneNO.LAC, user.getLac());
        values.put(PhoneNO.CID, user.getCid());
        values.put(PhoneNO.NID, user.getNid());
        values.put(PhoneNO.TIME, user.getTime());
        values.put(PhoneNO.IMPORT_TIME, user.getImportTime());
        Long uid = db.insert(SqliteHelper.PHONE_NO, PhoneNO.ID, values);
        return uid;
    }


    public List<PhoneNO> GetCellInfos_Form(String importTime_) {
        String importTime = importTime_.trim();
        List<PhoneNO> userList = new ArrayList<PhoneNO>();
        Cursor cursor = db
                .query(SqliteHelper.PHONE_NO, null, PhoneNO.IMPORT_TIME + "=?",
                        new String[]{importTime}, null, null, PhoneNO.ID + " ASC"); // DESC
        cursor.moveToFirst();
        while (!cursor.isAfterLast() && (cursor.getString(1) != null)) {
            PhoneNO user = new PhoneNO();
            user.setPhoneNum(cursor.getString(1));
            user.setLac(cursor.getString(2));
            user.setCid(cursor.getString(3));
            user.setNid(cursor.getString(4));
            user.setTime(cursor.getString(5));
            user.setImportTime(cursor.getString(6));
            userList.add(user);
            cursor.moveToNext();

        }
        cursor.close();
        return userList;

    }


    //基站定位中 保存话单人信息
    public Long SaveFormInfo(FormInfo user) {
        ContentValues values = new ContentValues();
        values.put(FormInfo.NAME, user.getName());
        values.put(FormInfo.PHONE, user.getPhone());
        values.put(FormInfo.STARTTIME, user.getStartTime());
        values.put(FormInfo.ENDTIME, user.getEndTime());
        values.put(FormInfo.DETAIL, user.getDetail());
        values.put(FormInfo.IMPORT_TIME, user.getImportTime());
        Long uid = db.insert(FORM_INFO, FormInfo.ID, values);
        return uid;
    }

    public List<FormInfo> GetFormInfos() {
        List<FormInfo> userList = new ArrayList<FormInfo>();
        Cursor cursor = db.query(FORM_INFO, null, null, null, null, null, FormInfo.IMPORT_TIME + " DESC"); // DESC由大到小/ASC是由小到大
        cursor.moveToFirst();
        while (!cursor.isAfterLast() && (cursor.getString(1) != null)) {

            FormInfo user = new FormInfo();
            user.setName(cursor.getString(1));
            user.setPhone(cursor.getString(2));
            user.setStartTime(cursor.getString(3));
            user.setEndTime(cursor.getString(4));
            user.setDetail(cursor.getString(5));
            user.setImportTime(cursor.getString(6));
            userList.add(user);
            cursor.moveToNext();
        }
        cursor.close();
        return userList;
    }

    public FormInfo GetFormInfo(String num1) {
        String num = num1.trim();
        Cursor cursor = db
                .query(FORM_INFO, null, FormInfo.PHONE + "=?",
                        new String[]{num}, null, null, FormInfo.ID + " ASC"); // DESC
        cursor.moveToFirst();
        if (!cursor.isAfterLast() && (cursor.getString(1) != null)) {
            FormInfo user = new FormInfo();
            user.setName(cursor.getString(1));
            user.setPhone(cursor.getString(2));
            user.setStartTime(cursor.getString(3));
            user.setEndTime(cursor.getString(4));
            user.setDetail(cursor.getString(5));
            user.setImportTime(cursor.getString(6));
            cursor.close();
            return user;
        } else {
            return null;
        }

    }


    //基站定位中，话单人位置信息
    public Long SavePeopleLocation(PeopleLocation user) {
        ContentValues values = new ContentValues();
        values.put(PeopleLocation.LAT, user.getLat());
        values.put(PeopleLocation.LNG, user.getLng());
        values.put(PeopleLocation.ADDRESS, user.getAddress());
        values.put(PeopleLocation.LAC, user.getLac());
        values.put(PeopleLocation.CID, user.getCid());
        values.put(PeopleLocation.NID, user.getNid());
        values.put(PeopleLocation.TIME, user.getTime());
        values.put(PeopleLocation.IMPORT_TIME, user.getImportTime());
        Long uid = db.insert(SqliteHelper.PEOPLE_LOCATION, PeopleLocation.ID, values);
        return uid;
    }


    public List<PeopleLocation> GetPeopleLocation(String importTime_) {
        String importTime = importTime_.trim();
        List<PeopleLocation> userList = new ArrayList<PeopleLocation>();
        Cursor cursor = db
                .query(SqliteHelper.PEOPLE_LOCATION, null, PeopleLocation.IMPORT_TIME + "=?",
                        new String[]{importTime}, null, null, PeopleLocation.ID + " ASC");
        cursor.moveToFirst();
        while (!cursor.isAfterLast() && (cursor.getString(1) != null)) {

            PeopleLocation user = new PeopleLocation();
            user.setLat(cursor.getString(1));
            user.setLng(cursor.getString(2));
            user.setAddress(cursor.getString(3));
            user.setLac(cursor.getString(4));
            user.setCid(cursor.getString(5));
            user.setNid(cursor.getString(6));
            user.setTime(cursor.getString(7));
            user.setImportTime(cursor.getString(8));
            userList.add(user);
            cursor.moveToNext();
        }
        cursor.close();
        return userList;
    }

}