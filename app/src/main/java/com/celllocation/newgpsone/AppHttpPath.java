package com.celllocation.newgpsone;

public class AppHttpPath {
    /**
     * base
     */
    public static final String BASE = "http://v.juhe.cn/";
//    public static final String BASE = "http://192.168.124.118:8082/lookWorld/";
    /**
     * 注册
     */
    public static final String REGIST = BASE + "u/appConnector/insertUserRegister.shtml";

    public static final String CELL_LOCATE_CDMA = BASE + "cdma/?";
    public static final String CELL_LOCATE_OTHER = BASE + "cell/get?";


    /**
     * 修改密码
     *
     * @return
     */
    public static final String MODIFY_PWD = BASE + "u/appConnector/updatePaWd.shtml";


}