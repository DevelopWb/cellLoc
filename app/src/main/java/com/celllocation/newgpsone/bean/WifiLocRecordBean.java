package com.celllocation.newgpsone.bean;

import android.os.Parcel;
import android.os.Parcelable;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2021-10-16 14:33
 * @UpdateUser: 更新者
 * @UpdateDate: 2021-10-16 14:33
 */

@Entity
public class WifiLocRecordBean implements Parcelable {
    @Id
    public long id;
    private String mac;
    private double latitude;
    private double longitude;
    private String locTime;
    private String addr;

    public WifiLocRecordBean(String mac, double latitude, double longitude, String addr) {
        this.mac = mac;
        this.latitude = latitude;
        this.longitude = longitude;
        this.addr = addr;
    }

    public WifiLocRecordBean(String mac, double latitude, double longitude, String locTime, String addr) {
        this.mac = mac;
        this.latitude = latitude;
        this.longitude = longitude;
        this.locTime = locTime;
        this.addr = addr;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAddr() {
        return addr == null ? "" : addr;
    }

    public void setAddr(String addr) {
        this.addr = addr == null ? "" : addr;
    }

    public String getLocTime() {
        return locTime == null ? "" : locTime;
    }

    public void setLocTime(String locTime) {
        this.locTime = locTime == null ? "" : locTime;
    }

    public String getMac() {
        return mac == null ? "" : mac;
    }

    public void setMac(String mac) {
        this.mac = mac == null ? "" : mac;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.mac);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
        dest.writeString(this.locTime);
        dest.writeString(this.addr);
    }

    protected WifiLocRecordBean(Parcel in) {
        this.id = in.readLong();
        this.mac = in.readString();
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
        this.locTime = in.readString();
        this.addr = in.readString();
    }

    public static final Parcelable.Creator<WifiLocRecordBean> CREATOR = new Parcelable.Creator<WifiLocRecordBean>() {
        @Override
        public WifiLocRecordBean createFromParcel(Parcel source) {
            return new WifiLocRecordBean(source);
        }

        @Override
        public WifiLocRecordBean[] newArray(int size) {
            return new WifiLocRecordBean[size];
        }
    };
}
