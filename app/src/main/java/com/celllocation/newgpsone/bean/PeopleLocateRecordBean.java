package com.celllocation.newgpsone.bean;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Transient;

/**
 * @Author: tobato
 * @Description: 作用描述  人员定位详情
 * @CreateDate: 2021/10/28 21:34
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/10/28 21:34
 */

@Entity
public class PeopleLocateRecordBean {
    @Id
    public long id;

    private String peopleName;
    private String peopleMobile;
    /**
     * 0代表手机定位 1代表gps设备
     */
    private int locType = 0;
    private String headPicPath;
    private String lat;
    private String lng;
    private String locTime;

    public String getPeopleName() {
        return peopleName == null ? "" : peopleName;
    }

    public void setPeopleName(String peopleName) {
        this.peopleName = peopleName == null ? "" : peopleName;
    }

    public String getPeopleMobile() {
        return peopleMobile == null ? "" : peopleMobile;
    }

    public void setPeopleMobile(String peopleMobile) {
        this.peopleMobile = peopleMobile == null ? "" : peopleMobile;
    }

    public int getLocType() {
        return locType;
    }

    public void setLocType(int locType) {
        this.locType = locType;
    }

    public String getHeadPicPath() {
        return headPicPath == null ? "" : headPicPath;
    }

    public void setHeadPicPath(String headPicPath) {
        this.headPicPath = headPicPath == null ? "" : headPicPath;
    }

    public String getLat() {
        return lat == null ? "" : lat;
    }

    public void setLat(String lat) {
        this.lat = lat == null ? "" : lat;
    }

    public String getLng() {
        return lng == null ? "" : lng;
    }

    public void setLng(String lng) {
        this.lng = lng == null ? "" : lng;
    }

    public String getLocTime() {
        return locTime == null ? "" : locTime;
    }

    public void setLocTime(String locTime) {
        this.locTime = locTime == null ? "" : locTime;
    }
}
