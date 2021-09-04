package com.celllocation.newgpsone.bean;

/**
 * Created by Administrator on 2016/11/25.
 */

public class PeopleLocation {


    public static final String ID = "_id";
    public static final String LAT = "lat";
    public static final String LNG = "lng";
    public static final String ADDRESS = "address";
    public static final String LAC = "lac";
    public static final String CID = "cid";
    public static final String NID = "nid";
    public static final String TIME = "time";
    public static final String IMPORT_TIME = "importTime";





    private String lat;
    private String lng;
    private String address;
    private String cid;
    private String lac;
    private String nid;
    private String time;
    private String importTime;//Ψһ��ʶ

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

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getLac() {
        return lac;
    }

    public void setLac(String lac) {
        this.lac = lac;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImportTime() {
        return importTime;
    }

    public void setImportTime(String importTime) {
        this.importTime = importTime;
    }
}
