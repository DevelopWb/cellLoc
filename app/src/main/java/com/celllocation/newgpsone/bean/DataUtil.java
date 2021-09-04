package com.celllocation.newgpsone.bean;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class DataUtil {

	public static String select_status = "1";
	public static int num = 0;
	public static Bitmap bm1 =null;
	public static boolean ChangeBG = false;
	public static boolean checkBt = false;//实时跟踪按钮点击或关闭
	public static String getDataFromSp(Context context,String filename,String key,String defaut){
		SharedPreferences reg = context.getSharedPreferences(filename, 0);
		String regCode = reg.getString(key, defaut);
		return regCode;
	}
	public static String getStr(String str) {
		int ii = 0;
		int j = 0;
		ii = str.indexOf("{");
		j = str.lastIndexOf("}");
		return str.substring(ii, j + 1);
	}

	public static Bitmap toRoundBitmap(Bitmap bitmap, String string, Context context) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float roundPx;
		float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
		if (width <= height) {
			roundPx = width / 2;
			top = 0;
			bottom = width;
			left = 0;
			right = width;
			height = width;
			dst_left = 0;
			dst_top = 0;
			dst_right = width;
			dst_bottom = width;
		} else {
			roundPx = height / 2;
			float clip = (width - height) / 2;
			left = clip;
			right = width - clip;
			top = 0;
			bottom = height;
			width = height;
			dst_left = 0;
			dst_top = 0;
			dst_right = height;
			dst_bottom = height;
		}
		Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect src = new Rect((int) left, (int) top, (int) right,
				(int) bottom);
		final Rect dst = new Rect((int) dst_left, (int) dst_top,
				(int) dst_right, (int) dst_bottom);
		final RectF rectF = new RectF(dst);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, src, dst, paint);

		// 获取这个图片的宽和高
		int width_ = output.getWidth();
		int height_ = output.getHeight();

		// 定义预转换成的图片的宽度和高度
		int newWidth = 30;
		int newHeight =30;
	   int newWidth_ = dip2px(context, newWidth);
	   int newHeight_ = dip2px(context, newWidth);
		// 计算缩放率，新尺寸除原始尺寸
		float scaleWidth = ((float) newWidth_) / width;
		float scaleHeight = ((float) newHeight_) / height;

		// 创建操作图片用的matrix对象
		Matrix matrix = new Matrix();

		// 缩放图片动作
		matrix.postScale(scaleWidth, scaleHeight);

		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(output, 0, 0, width, height,
				matrix, true);

		Bitmap result_bitmap = Bitmap.createBitmap(resizedBitmap.getWidth()*2+60,
				resizedBitmap.getHeight() + 40, Config.ARGB_8888);
		Canvas result_canvas = new Canvas(result_bitmap);

		result_canvas.drawBitmap(resizedBitmap, resizedBitmap.getWidth()/2+30, 0, null);

		Paint fontpaint = new Paint();
		fontpaint.setColor(Color.parseColor("#FF020C"));
		fontpaint.setTextSize(scalaFonts(dip2px(context, 10)));
		fontpaint.setTypeface(Typeface.create("宋体", Typeface.BOLD));
		fontpaint.setAntiAlias(true);
		
		fontpaint.setFilterBitmap(true);

		result_canvas.drawText(string, resizedBitmap.getWidth()-50, resizedBitmap.getHeight() + 10, fontpaint);
		//10表示距离bitmap的距离

		// 将上面创建的Bitmap转换成Drawable对象，使得其可以使用在ImageView, ImageButton中
		BitmapDrawable bmd = new BitmapDrawable(result_bitmap);

		return bmd.getBitmap();
	}
	private static float scalaFonts(int size) {
		// 暂未实现
		return size;
	}


	public static ArrayList<HashMap<String, String>> GetAllStatusFromService(
			String str) {

		return null;

	}

	// 将时间戳转成字符串
	public static String getDateToString(long time) {
		Date d = new Date(time);
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		return sf.format(d);
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

	public static void stopProgressDialog(Dialog dialog) {
		if (dialog != null) {
			dialog.dismiss();
			dialog = null;
		}
	}
	public static Bitmap toRoundBitmap(Bitmap bitmap, Context context) {
		int width = bitmap.getWidth();
		
		int height = bitmap.getHeight();
		float roundPx;
		float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
		if (width <= height) {
			roundPx = width / 2;
			top = 0;
			bottom = width;
			left = 0;
			right = width;
			height = width;
			dst_left = 0;
			dst_top = 0;
			dst_right = width;
			dst_bottom = width;
		} else {
			roundPx = height / 2;
			float clip = (width - height) / 2;
			left = clip;
			right = width - clip;
			top = 0;
			bottom = height;
			width = height;
			dst_left = 0;
			dst_top = 0;
			dst_right = height;
			dst_bottom = height;
		}
		Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect src = new Rect((int) left, (int) top, (int) right,
				(int) bottom);
		final Rect dst = new Rect((int) dst_left, (int) dst_top,
				(int) dst_right, (int) dst_bottom);
		final RectF rectF = new RectF(dst);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, src, dst, paint);

		// 获取这个图片的宽和高
		int width_ = output.getWidth();
		int height_ = output.getHeight();

		// 定义预转换成的图片的宽度和高度
				int newWidth = 30;
				int newHeight =30;
			   int newWidth_ = dip2px(context, newWidth);
			   int newHeight_ = dip2px(context, newWidth);
				// 计算缩放率，新尺寸除原始尺寸
				float scaleWidth = ((float) newWidth_) / width;
				float scaleHeight = ((float) newHeight_) / height;


		// 创建操作图片用的matrix对象
		Matrix matrix = new Matrix();

		// 缩放图片动作
		matrix.postScale(scaleWidth, scaleHeight);

		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(output, 0, 0, width, height,
				matrix, true);

		// 将上面创建的Bitmap转换成Drawable对象，使得其可以使用在ImageView, ImageButton中
		BitmapDrawable bmd = new BitmapDrawable(resizedBitmap);

		return bmd.getBitmap();
	}
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}
}
