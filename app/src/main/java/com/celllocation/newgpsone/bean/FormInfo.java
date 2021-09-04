package com.celllocation.newgpsone.bean;

/**
 * Created by Administrator on 2016/11/21.
 */

public class FormInfo {


    public static final String ID = "_id";
    public static final String NAME = "name";
    public static final String PHONE = "phone";
    public static final String STARTTIME = "startTime";
    public static final String ENDTIME = "endTime";
    public static final String DETAIL = "detail";
    public static final String IMPORT_TIME = "importTime";




    private String name;
    private String phone;
    private String startTime;
    private String endTime;
    private String detail;
    private String importTime;

    public String getImportTime() {
        return importTime;
    }

    public void setImportTime(String importTime) {
        this.importTime = importTime;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }





}
