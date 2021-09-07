package com.celllocation.newgpsone;

import android.app.Application;

import com.juntai.disabled.basecomponent.app.BaseApplication;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by Administrator on 2016/12/12.
 */

public class MyApplication extends BaseApplication {

    @Override
    public void onCreate() {


        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .build();

        OkHttpUtils.initClient(okHttpClient);
        super.onCreate();
    }
}
