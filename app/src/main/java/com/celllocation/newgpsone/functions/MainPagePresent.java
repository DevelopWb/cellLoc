package com.celllocation.newgpsone.functions;


import com.celllocation.newgpsone.AppNetModule;
import com.celllocation.newgpsone.Utils.PublicUtill;
import com.celllocation.newgpsone.base.BaseAppPresent;
import com.celllocation.newgpsone.bean.CellLocResultBean;
import com.juntai.disabled.basecomponent.base.BaseObserver;
import com.juntai.disabled.basecomponent.mvp.IModel;
import com.juntai.disabled.basecomponent.utils.RxScheduler;

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
                .cellLocateCDMA(lac,cid,nid, PublicUtill.LOC_TYPE,key)
                .compose(RxScheduler.ObsIoMain(getView()))
                .subscribe(new BaseObserver<CellLocResultBean>(null) {
                    @Override
                    public void onSuccess(CellLocResultBean o) {
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

    /**
     *
     * @param lac
     * @param cid
     * @param mnc  mnc网络类型0移动1联通(电信对应sid)
     * @param key
     * @param tag
     */
    public void cellLocateOther(String lac,String cid,String mnc,String key, String tag) {
        AppNetModule.createrRetrofit()
                .cellLocateOther(PublicUtill.MCC,lac,cid,mnc,PublicUtill.LOC_TYPE,key)
                .compose(RxScheduler.ObsIoMain(getView()))
                .subscribe(new BaseObserver<CellLocResultBean>(null) {
                    @Override
                    public void onSuccess(CellLocResultBean o) {
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
