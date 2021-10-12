package com.celllocation.newgpsone.Utils;

import com.celllocation.newgpsone.bean.LatLngBean;

/**
 * @Author: tobato
 * @Description: 作用描述   经纬度转换
 * @CreateDate: 2021/10/12 20:53
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/10/12 20:53
 */
public class LatLngTransFormUtil {


        public static final String BAIDU_LBS_TYPE = "bd09ll";

        public static double pi = 3.1415926535897932384626;
        public static double a = 6378245.0;
        public static double ee = 0.00669342162296594323;

        /**
         * 84 to 火星坐标系 (GCJ-02) World Geodetic System ==> Mars Geodetic System
         *
         * @param lat
         * @param lon
         * @return
         */
        public static LatLngBean gps84_To_Gcj02(double lat, double lon) {
            if (outOfChina(lat, lon)) {
                return null;
            }
            double dLat = transformLat(lon - 105.0, lat - 35.0);
            double dLon = transformLon(lon - 105.0, lat - 35.0);
            double radLat = lat / 180.0 * pi;
            double magic = Math.sin(radLat);
            magic = 1 - ee * magic * magic;
            double sqrtMagic = Math.sqrt(magic);
            dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
            dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
            double mgLat = lat + dLat;
            double mgLon = lon + dLon;
            return new LatLngBean(mgLat, mgLon);
        }

        /**
         * * 火星坐标系 (GCJ-02) to 84 * * @param lon * @param lat * @return
         * */
        public static LatLngBean gcj_To_Gps84(double lat, double lon) {
            LatLngBean gps = transform(lat, lon);
            double lontitude = lon * 2 - gps.getWgLon();
            double latitude = lat * 2 - gps.getWgLat();
            return new LatLngBean(latitude, lontitude);
        }

        /**
         * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换算法 将 GCJ-02 坐标转换成 BD-09 坐标
         *
         * @param gg_lat
         * @param gg_lon
         */
        public static LatLngBean gcj02_To_Bd09(double gg_lat, double gg_lon) {
            double x = gg_lon, y = gg_lat;
            double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * pi);
            double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * pi);
            double bd_lon = z * Math.cos(theta) + 0.0065;
            double bd_lat = z * Math.sin(theta) + 0.006;
            return new LatLngBean(bd_lat, bd_lon);
        }

        /**
         * * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换算法 * * 将 BD-09 坐标转换成GCJ-02 坐标 * * @param
         * bd_lat * @param bd_lon * @return
         */
        public static LatLngBean bd09_To_Gcj02(double bd_lat, double bd_lon) {
            double x = bd_lon - 0.0065, y = bd_lat - 0.006;
            double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * pi);
            double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * pi);
            double gg_lon = z * Math.cos(theta);
            double gg_lat = z * Math.sin(theta);
            return new LatLngBean(gg_lat, gg_lon);
        }


        public static boolean outOfChina(double lat, double lon) {
            if (lon < 72.004 || lon > 137.8347) {
                return true;
            }
            return lat < 0.8293 || lat > 55.8271;
        }

        public static LatLngBean transform(double lat, double lon) {
            if (outOfChina(lat, lon)) {
                return new LatLngBean(lat, lon);
            }
            double dLat = transformLat(lon - 105.0, lat - 35.0);
            double dLon = transformLon(lon - 105.0, lat - 35.0);
            double radLat = lat / 180.0 * pi;
            double magic = Math.sin(radLat);
            magic = 1 - ee * magic * magic;
            double sqrtMagic = Math.sqrt(magic);
            dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
            dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
            double mgLat = lat + dLat;
            double mgLon = lon + dLon;
            return new LatLngBean(mgLat, mgLon);
        }

        public static double transformLat(double x, double y) {
            double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y
                    + 0.2 * Math.sqrt(Math.abs(x));
            ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
            ret += (20.0 * Math.sin(y * pi) + 40.0 * Math.sin(y / 3.0 * pi)) * 2.0 / 3.0;
            ret += (160.0 * Math.sin(y / 12.0 * pi) + 320 * Math.sin(y * pi / 30.0)) * 2.0 / 3.0;
            return ret;
        }

        public static double transformLon(double x, double y) {
            double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1
                    * Math.sqrt(Math.abs(x));
            ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
            ret += (20.0 * Math.sin(x * pi) + 40.0 * Math.sin(x / 3.0 * pi)) * 2.0 / 3.0;
            ret += (150.0 * Math.sin(x / 12.0 * pi) + 300.0 * Math.sin(x / 30.0
                    * pi)) * 2.0 / 3.0;
            return ret;
        }

        public static void main(String[] args) {

            // 北斗芯片获取的经纬度为WGS84地理坐标 31.426896,119.496145
            LatLngBean gps = new LatLngBean(31.426896, 119.496145);
            System.out.println("gps :" + gps);
            LatLngBean gcj = gps84_To_Gcj02(gps.getWgLat(), gps.getWgLon());
            System.out.println("gcj :" + gcj);
            LatLngBean star = gcj_To_Gps84(gcj.getWgLat(), gcj.getWgLon());
            System.out.println("star:" + star);
            LatLngBean bd = gcj02_To_Bd09(31.239666,121.499809);
            System.out.println("bd  :" + bd);
            LatLngBean gcj2 = bd09_To_Gcj02(bd.getWgLat(), bd.getWgLon());
            System.out.println("gcj :" + gcj2);
        }
    }
