package com.juntai.disabled.basecomponent.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.juntai.disabled.basecomponent.BuildConfig;
import com.juntai.disabled.basecomponent.R;
import com.juntai.disabled.basecomponent.utils.LogUtil;
import com.juntai.disabled.basecomponent.utils.NavigationBarInfo;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import java.lang.ref.SoftReference;
import java.util.ArrayList;


public abstract class BaseApplication extends Application {
    public static int HEIGHT, width, statusBarH;
    public static int navigationBarH;
    public static BaseApplication app;
    private RefWatcher mRefWatcher;
    ArrayList<Activity> activities = new ArrayList<>();
    public static boolean isReLoadWarn = true;//登录被顶，是否提示
    /*app处于后台true,前台false*/


    //活动的activity数量
    int mActivityCount;
    //应用处于前台运行中
    public boolean isRun = true;
    //
    SoftReference<Activity> softReference;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;

        getScreen(this);
        if (BuildConfig.DEBUG) {
            //
            Logger.addLogAdapter(new AndroidLogAdapter(PrettyFormatStrategy.newBuilder().
                    tag(getString(R.string.app_name)).build()));
            LogUtil.logInit(true);
        }
        initLeakCanary();
//        registerActivityLifecycleCallbacks(mCallbacks);
//        hotFix();
        // 调试时，将第三个参数改为true


    }





    /**
     * 记录当前正在活动的activity
     *
     * @return
     */
    public Activity getNowActivity() {
        if (softReference != null) {
            return softReference.get();
        }
       return null;
    }


    public void getScreen(Context aty) {
        //依赖于手机系统，获取到的是系统的屏幕信息；
        DisplayMetrics dm = aty.getResources().getDisplayMetrics();
        HEIGHT = dm.heightPixels;
        width = dm.widthPixels;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarH = getResources().getDimensionPixelSize(resourceId);
        }

        if (NavigationBarInfo.hasNavBar(aty)) {
            int navigationId = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
            if (navigationId > 0) {
                navigationBarH = getResources().getDimensionPixelSize(navigationId);
            }
        }
    }

    private void initLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        mRefWatcher = LeakCanary.install(this);
    }

    public static RefWatcher getRefWatcher(Context context) {
        return app.mRefWatcher;
    }

    /**
     * 下拉刷新
     * static 代码段可以防止内存泄露
     */
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(android.R.color.white, R.color.black);//全局设置主题颜色
                return new ClassicsHeader(context).setTextSizeTitle(13).setTextSizeTime(10).setDrawableArrowSize(15);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(15).setTextSizeTitle(13);
            }
        });
    }

//    private ActivityLifecycleCallbacks mCallbacks = new ActivityLifecycleCallbacks() {
//
//        @Override
//        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
//            //LogUtil.d("Activity-onActivityCreated = " + activity.getClass().getName());
//            activities.add(activity);
//        }
//
//        @Override
//        public void onActivityStarted(Activity activity) {
//            mActivityCount++;
//            if (mActivityCount == 1) {
//                appBackground(false, activity);
//                isRun = true;
//            }
//            softReference = null;
//            softReference = new SoftReference<>(activity);
//        }
//
//        @Override
//        public void onActivityResumed(Activity activity) {
//        }
//
//        @Override
//        public void onActivityPaused(Activity activity) {
//        }
//
//        @Override
//        public void onActivityStopped(Activity activity) {
//            mActivityCount--;
//            if (mActivityCount == 0) {
//                appBackground(true, activity);
//                isRun = false;
//            }
//        }
//
//        @Override
//        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
//        }
//
//        @Override
//        public void onActivityDestroyed(Activity activity) {
//            //LogUtil.d("Activity-onActivityDestroyed = " + activity.getClass().getName());
//            activities.remove(activity);
//        }
//    };


}
