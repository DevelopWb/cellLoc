package com.celllocation.newgpsone.Utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

public class PublicUtill {
	
	public static int dianxin_mar = 0;
	public static int MCC = 460;//国家标识
	public static boolean isDianxin = false;
	public static String CDMA_CELL_LOC_KEY ="68e19fece33d40808befd223985b2f03";//cdma  key
	public static String OTHER_CELL_LOC_KEY ="a3c2561c497146fda61ac311be810826";//  key
	public static int LOC_TYPE =0;//   0(google坐标),1( 百度坐标),2(gps坐标)
	public static boolean isDrawable = true;
	public static String URL_Reg_Center = "http://218.246.35.198:8082";//注册码中心系统
	public static String getIMEIDeviceId(Context context) {

		String deviceId;

		if (Build.VERSION.SDK_INT >= 28)
		{
			deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
		} else {
			final TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				if (context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
					return "";
				}
			}
			assert mTelephony != null;
			if (mTelephony.getDeviceId() != null)
			{
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
				{
					deviceId = mTelephony.getImei();
				}else {
					deviceId = mTelephony.getDeviceId();
				}
			} else {
				deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
			}
		}
		Log.d("deviceId", deviceId);
		return deviceId;
	}
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
