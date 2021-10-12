package com.celllocation.newgpsone.bean;

import android.os.Parcel;
import android.os.Parcelable;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2021/10/12 20:54
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/10/12 20:54
 */

@Entity
public class LatLngBean implements Parcelable {
    @Id
    public long id;
    private double wgLat;
    private double wgLon;
    private  int  locType;
    private String time;

    public LatLngBean(double wgLat, double wgLon) {
        setWgLat(wgLat);
        setWgLon(wgLon);
    }

    public LatLngBean(double wgLat, double wgLon, int locType, String time) {
        this.wgLat = wgLat;
        this.wgLon = wgLon;
        this.locType = locType;
        this.time = time;
    }

    public int getLocType() {
        return locType;
    }

    public void setLocType(int locType) {
        this.locType = locType;
    }

    public String getTime() {
        return time == null ? "" : time;
    }

    public void setTime(String time) {
        this.time = time == null ? "" : time;
    }

    public double getWgLat() {
        return wgLat;
    }

    public void setWgLat(double wgLat) {
        this.wgLat = wgLat;
    }

    public double getWgLon() {
        return wgLon;
    }

    public void setWgLon(double wgLon) {
        this.wgLon = wgLon;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeDouble(this.wgLat);
        dest.writeDouble(this.wgLon);
        dest.writeInt(this.locType);
        dest.writeString(this.time);
    }

    protected LatLngBean(Parcel in) {
        this.id = in.readLong();
        this.wgLat = in.readDouble();
        this.wgLon = in.readDouble();
        this.locType = in.readInt();
        this.time = in.readString();
    }

    public static final Parcelable.Creator<LatLngBean> CREATOR = new Parcelable.Creator<LatLngBean>() {
        @Override
        public LatLngBean createFromParcel(Parcel source) {
            return new LatLngBean(source);
        }

        @Override
        public LatLngBean[] newArray(int size) {
            return new LatLngBean[size];
        }
    };
}

