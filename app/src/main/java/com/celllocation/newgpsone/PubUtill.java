package com.celllocation.newgpsone;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class PubUtill {
	
	public static int dianxin_mar = 0;
	public static boolean isDianxin = false;

	public static boolean isDrawable = true;
	public static boolean DianxinClicked = false;
	public static String URL_Reg_Center = "http://218.246.35.198:8082";//注册码中心系统

// 判断网络是否正常

	public static boolean isConnected(Context context) {
		boolean isOk = true;
		try {
			ConnectivityManager connectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mobNetInfo = connectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			NetworkInfo wifiNetInfo = connectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (wifiNetInfo != null && !wifiNetInfo.isConnectedOrConnecting()) {
				if (mobNetInfo != null && !mobNetInfo.isConnectedOrConnecting()) {
					NetworkInfo info = connectivityManager
							.getActiveNetworkInfo();
					if (info == null) {
						isOk = false;
					}
				}
			}
			mobNetInfo = null;
			wifiNetInfo = null;
			connectivityManager = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isOk;
	}
}
