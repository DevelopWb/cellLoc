package com.celllocation.newgpsone.base;

import com.juntai.disabled.basecomponent.mvp.BasePresenter;
import com.juntai.disabled.basecomponent.mvp.IModel;
import com.juntai.disabled.basecomponent.mvp.IView;

import okhttp3.FormBody;
import okhttp3.MultipartBody;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2020/6/3 8:38
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/6/3 8:38
 */
public abstract class BaseAppPresent<M extends IModel, V extends IView> extends BasePresenter<M,V> {

    /**
     * 获取builder
     * @return
     */
    public MultipartBody.Builder getPublishMultipartBody() {
        return new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
    }

    /**
     * 获取builder
     *
     * @return
     */
    public FormBody.Builder getBaseBuilder() {
        FormBody.Builder builder = new FormBody.Builder();
        return builder;
    }
}
