package com.celllocation.newgpsone.base;


import com.juntai.disabled.basecomponent.base.BaseMvpFragment;
import com.juntai.disabled.basecomponent.mvp.IPresenter;

/**
 * @aouther tobato
 * @description 描述  app的fragment的基类
 * @date 2020/7/18 16:43
 */
public abstract class BaseAppFragment<P extends IPresenter> extends BaseMvpFragment<P> {


    /**
     * 获取activity
     *
     * @return
     */
    public BaseAppActivity getBaseAppActivity() {
        return (BaseAppActivity) getActivity();
    }
}
