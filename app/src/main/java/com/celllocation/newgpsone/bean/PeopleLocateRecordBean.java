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

    @Transient
    private PeopleLocateUserBean peopleLocateUserBean;

    private String lat;
    private String lng;
    private String locTime;

    public PeopleLocateUserBean getPeopleLocateUserBean() {
        return peopleLocateUserBean;
    }

    public void setPeopleLocateUserBean(PeopleLocateUserBean peopleLocateUserBean) {
        this.peopleLocateUserBean = peopleLocateUserBean;
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
