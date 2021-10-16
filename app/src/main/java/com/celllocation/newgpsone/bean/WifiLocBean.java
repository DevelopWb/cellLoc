package com.celllocation.newgpsone.bean;

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

    public static class LocationBean {
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

        public static class AddressBean {
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
        }
    }
}
