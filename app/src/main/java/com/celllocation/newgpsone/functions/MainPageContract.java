package com.celllocation.newgpsone.functions;

import com.juntai.disabled.basecomponent.mvp.IPresenter;
import com.juntai.disabled.basecomponent.mvp.IView;

/**
 * Describe: 首页
 * Create by zhangzhenlong
 * 2020-8-8
 * email:954101549@qq.com
 */
public interface MainPageContract {
    String CELL_CDMA = "cellcdma";
    String CELL_OTHER = "cellother";


    interface IMainPageView extends IView {
    }

    interface IMainPagePresent extends IPresenter<IMainPageView> {
    }
}
