package com.celllocation.newgpsone;

/**
 * Created by Administrator on 2016/11/17.
 */

public class PhoneNO {


    public static final String ID = "_id";
    public static final String PHONE = "phone";
    public static final String LAC = "lac";
    public static final String CID = "cid";
    public static final String NID = "nid";
    public static final String TIME = "time";
    public static final String IMPORT_TIME = "importTime";



    private String phoneNum;
    private String lac;
    private String cid;
    private String nid;
    private String time;
    private String importTime;

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getImportTime() {
        return importTime;
    }

    public void setImportTime(String importTime) {
        this.importTime = importTime;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getLac() {
        return lac;
    }

    public void setLac(String lac) {
        this.lac = lac;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
