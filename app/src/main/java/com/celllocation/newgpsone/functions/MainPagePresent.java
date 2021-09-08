package com.celllocation.newgpsone.functions;


import com.celllocation.newgpsone.AppNetModule;
import com.celllocation.newgpsone.base.BaseAppPresent;
import com.celllocation.newgpsone.bean.CellLocCDMAResultBean;
import com.juntai.disabled.basecomponent.base.BaseObserver;
import com.juntai.disabled.basecomponent.base.BaseResult;
import com.juntai.disabled.basecomponent.mvp.BasePresenter;
import com.juntai.disabled.basecomponent.mvp.IModel;
import com.juntai.disabled.basecomponent.utils.RxScheduler;

import okhttp3.RequestBody;

/**
 * Describe:首页present
 * Create by zhangzhenlong
 * 2020-8-8
 * email:954101549@qq.com
 */
public class MainPagePresent extends BaseAppPresent<IModel, MainPageContract.IMainPageView> implements MainPageContract.IMainPagePresent {

    @Override
    protected IModel createModel() {
        return null;
    }




    public void cellLocateCDMA(String lac,String cid,String nid,String key, String tag) {
        AppNetModule.createrRetrofit()
                .cellLocateCDMA(lac,cid,nid,key)
                .compose(RxScheduler.ObsIoMain(getView()))
                .subscribe(new BaseObserver<CellLocCDMAResultBean>(null) {
                    @Override
                    public void onSuccess(CellLocCDMAResultBean o) {
                        if (getView() != null) {
                            getView().onSuccess(tag, o);
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        if (getView() != null) {
                            getView().onError(tag, msg);
                        }
                    }
                });
    }

    public void cellLocateOther(RequestBody requestBody, String tag) {
        AppNetModule.createrRetrofit()
                .cellLocateOther(requestBody)
                .compose(RxScheduler.ObsIoMain(getView()))
                .subscribe(new BaseObserver<BaseResult>(null) {
                    @Override
                    public void onSuccess(BaseResult o) {
                        if (getView() != null) {
                            getView().onSuccess(tag, o);
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        if (getView() != null) {
                            getView().onError(tag, msg);
                        }
                    }
                });
    }
}
