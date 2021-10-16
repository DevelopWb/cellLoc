package com.celllocation.newgpsone.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.juntai.disabled.basecomponent.bean.BaseCellLocBean;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2021/10/15 22:39
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/10/15 22:39
 */
public class WifiLocBean extends BaseCellLocBean {


    /**
     * location : {"address":{"region":"浙江省","county":"西湖区","street":"文新街道","street_number":"沈家门","city":"杭州市",
     * "country":"中国"},"addressDescription":"中国浙江省杭州市西湖区文新街道沈家门","longitude":120.10214,"latitude":30.26541,
     * "accuracy":"150"}
     * ErrCode : 0
     */

    private LocationBean location;

    public LocationBean getLocation() {
        return location;
    }

    public void setLocation(LocationBean location) {
        this.location = location;
    }

    public static class LocationBean implements Parcelable {
        /**
         * address : {"region":"浙江省","county":"西湖区","street":"文新街道","street_number":"沈家门","city":"杭州市","country":"中国"}
         * addressDescription : 中国浙江省杭州市西湖区文新街道沈家门
         * longitude : 120.10214
         * latitude : 30.26541
         * accuracy : 150
         */

        private AddressBean address;
        private String addressDescription;
        private double longitude;
        private double latitude;
        private String accuracy;

        public AddressBean getAddress() {
            return address;
        }

        public void setAddress(AddressBean address) {
            this.address = address;
        }

        public String getAddressDescription() {
            return addressDescription;
        }

        public void setAddressDescription(String addressDescription) {
            this.addressDescription = addressDescription;
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

        public String getAccuracy() {
            return accuracy;
        }

        public void setAccuracy(String accuracy) {
            this.accuracy = accuracy;
        }

        public static class AddressBean implements Parcelable {
            /**
             * region : 浙江省
             * county : 西湖区
             * street : 文新街道
             * street_number : 沈家门
             * city : 杭州市
             * country : 中国
             */

            private String region;
            private String county;
            private String street;
            private String street_number;
            private String city;
            private String country;

            public String getRegion() {
                return region;
            }

            public void setRegion(String region) {
                this.region = region;
            }

            public String getCounty() {
                return county;
            }

            public void setCounty(String county) {
                this.county = county;
            }

            public String getStreet() {
                return street;
            }

            public void setStreet(String street) {
                this.street = street;
            }

            public String getStreet_number() {
                return street_number;
            }

            public void setStreet_number(String street_number) {
                this.street_number = street_number;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getCountry() {
                return country;
            }

            public void setCountry(String country) {
                this.country = country;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.region);
                dest.writeString(this.county);
                dest.writeString(this.street);
                dest.writeString(this.street_number);
                dest.writeString(this.city);
                dest.writeString(this.country);
            }

            public AddressBean() {
            }

            protected AddressBean(Parcel in) {
                this.region = in.readString();
                this.county = in.readString();
                this.street = in.readString();
                this.street_number = in.readString();
                this.city = in.readString();
                this.country = in.readString();
            }

            public static final Creator<AddressBean> CREATOR = new Creator<AddressBean>() {
                @Override
                public AddressBean createFromParcel(Parcel source) {
                    return new AddressBean(source);
                }

                @Override
                public AddressBean[] newArray(int size) {
                    return new AddressBean[size];
                }
            };
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeParcelable(this.address, flags);
            dest.writeString(this.addressDescription);
            dest.writeDouble(this.longitude);
            dest.writeDouble(this.latitude);
            dest.writeString(this.accuracy);
        }

        public LocationBean() {
        }

        protected LocationBean(Parcel in) {
            this.address = in.readParcelable(AddressBean.class.getClassLoader());
            this.addressDescription = in.readString();
            this.longitude = in.readDouble();
            this.latitude = in.readDouble();
            this.accuracy = in.readString();
        }

        public static final Parcelable.Creator<LocationBean> CREATOR = new Parcelable.Creator<LocationBean>() {
            @Override
            public LocationBean createFromParcel(Parcel source) {
                return new LocationBean(source);
            }

            @Override
            public LocationBean[] newArray(int size) {
                return new LocationBean[size];
            }
        };
    }
}
