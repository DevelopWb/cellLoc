package com.celllocation.newgpsone.bean;

import com.juntai.disabled.basecomponent.base.BaseResult;
import com.juntai.disabled.basecomponent.bean.BaseCellLocBean;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2021-09-08 16:36
 * @UpdateUser: 更新者
 * @UpdateDate: 2021-09-08 16:36
 */
public class CellLocCDMAResultBean extends BaseCellLocBean {


    /**
     * resultcode : 200
     * reason : Successed!
     * result : {"sid":"13824","nid":"2","bid":"48563","lat":"39.926544","lon":"116.324097","o_lat":"39.927855088976","o_lon":"116.33022976345","address":"北京市海淀区增光路22号","raggio":"932"}
     * error_code : 0
     */


    private ResultBean result;



    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }


    public static class ResultBean {
        /**
         * sid : 13824
         * nid : 2
         * bid : 48563
         * lat : 39.926544
         * lon : 116.324097
         * o_lat : 39.927855088976
         * o_lon : 116.33022976345
         * address : 北京市海淀区增光路22号
         * raggio : 932
         */

        private String sid;
        private String nid;
        private String bid;
        private String lat;
        private String lon;
        private String o_lat;
        private String o_lon;
        private String address;
        private String raggio;

        public String getSid() {
            return sid;
        }

        public void setSid(String sid) {
            this.sid = sid;
        }

        public String getNid() {
            return nid;
        }

        public void setNid(String nid) {
            this.nid = nid;
        }

        public String getBid() {
            return bid;
        }

        public void setBid(String bid) {
            this.bid = bid;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLon() {
            return lon;
        }

        public void setLon(String lon) {
            this.lon = lon;
        }

        public String getO_lat() {
            return o_lat;
        }

        public void setO_lat(String o_lat) {
            this.o_lat = o_lat;
        }

        public String getO_lon() {
            return o_lon;
        }

        public void setO_lon(String o_lon) {
            this.o_lon = o_lon;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getRaggio() {
            return raggio;
        }

        public void setRaggio(String raggio) {
            this.raggio = raggio;
        }
    }
}
