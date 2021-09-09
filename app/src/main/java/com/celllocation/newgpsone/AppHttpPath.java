package com.celllocation.newgpsone;

public class AppHttpPath {
    /**
     * base
     */
    public static final String BASE = "http://api.haoservice.com/api/";
//    public static final String BASE = "http://192.168.124.118:8082/lookWorld/";
    /**
     * 注册
     */
    public static final String REGIST = BASE + "u/appConnector/insertUserRegister.shtml";

    public static final String CELL_LOCATE_CDMA = BASE + "getcdmalbs?";
    public static final String CELL_LOCATE_OTHER = BASE + "getlbs?";


    /**
     * 修改密码
     *
     * @return
     */
    public static final String MODIFY_PWD = BASE + "u/appConnector/updatePaWd.shtml";


}