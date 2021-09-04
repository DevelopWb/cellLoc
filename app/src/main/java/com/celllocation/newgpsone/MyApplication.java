package com.celllocation.newgpsone;

import android.app.Application;

import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2016/12/12.
 */

public class MyApplication extends Application {

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
