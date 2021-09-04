package com.celllocation.newgpsone.cellInfos;

/**
 * Author:wang_sir
 * Time:2018/3/21 14:27
 * Description:This is CellPosition
 */
public class CellPosition {


    /**
     * Result : true
     * Desc :
     * Model : {"Longitude":"116.324097","Latitude":"39.926544","o_Longitude":"116.33022976345","o_Latitude":"39.927855088976","Address":"北京市海淀区增光路22号","Precision":"932"}
     */

    private String Result;
    private String Desc;
    private ModelBean Model;

    public String getResult() {
        return Result;
    }

    public void setResult(String Result) {
        this.Result = Result;
    }

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String Desc) {
        this.Desc = Desc;
    }

    public ModelBean getModel() {
        return Model;
    }

    public void setModel(ModelBean Model) {
        this.Model = Model;
    }

    public static class ModelBean {
        /**
         * Longitude : 116.324097
         * Latitude : 39.926544
         * o_Longitude : 116.33022976345
         * o_Latitude : 39.927855088976
         * Address : 北京市海淀区增光路22号
         * Precision : 932
         */

        private String Longitude;
        private String Latitude;
        private String o_Longitude;
        private String o_Latitude;
        private String Address;
        private String Precision;

        public String getLongitude() {
            return Longitude;
        }

        public void setLongitude(String Longitude) {
            this.Longitude = Longitude;
        }

        public String getLatitude() {
            return Latitude;
        }

        public void setLatitude(String Latitude) {
            this.Latitude = Latitude;
        }

        public String getO_Longitude() {
            return o_Longitude;
        }

        public void setO_Longitude(String o_Longitude) {
            this.o_Longitude = o_Longitude;
        }

        public String getO_Latitude() {
            return o_Latitude;
        }

        public void setO_Latitude(String o_Latitude) {
            this.o_Latitude = o_Latitude;
        }

        public String getAddress() {
            return Address;
        }

        public void setAddress(String Address) {
            this.Address = Address;
        }

        public String getPrecision() {
            return Precision;
        }

        public void setPrecision(String Precision) {
            this.Precision = Precision;
        }
    }
}
