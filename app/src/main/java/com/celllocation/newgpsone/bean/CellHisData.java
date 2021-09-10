package com.celllocation.newgpsone.bean;

/**
 * Created by Administrator on 2016/12/13.
 */

public class CellHisData {


    public static String  ID = "_id";
    public static String  PHONE = "phone";
    public static String  LNG = "lng";
    public static String  LAT = "lat";
    public static String  LAC = "lac";
    public static String  CID = "cid";
    public static String  NID = "nid";
    public static String  TYPE = "type";//0移动1联通3电信
    public static String  ADDRESS = "address";
    public static String  ACCURACY = "accuracy";
    public static String  TIME = "time";

    private String phone;
    private String lng;
    private String lat;
    private String lac;
    private String cid;
    private String nid;
    private String type;
    private String address;
    private String accuracy;
    private String time;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
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

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }

    public String getTime() {
        return time;
    }

    public String getType() {
        return type == null ? "" : type;
    }

    public void setType(String type) {
        this.type = type == null ? "" : type;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
