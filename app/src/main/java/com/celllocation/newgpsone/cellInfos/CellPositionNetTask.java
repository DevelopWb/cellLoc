package com.celllocation.newgpsone.cellInfos;

import android.util.ArrayMap;

import com.celllocation.newgpsone.Utils.CellInfoUtil;
import com.celllocation.newgpsone.Utils.RegOperateTool;
import com.celllocation.newgpsone.bean.CellPosition;

import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Author:wang_sir
 * Time:2018/3/21 11:39
 * Description:This is CellPositionNetTask
 */
public class CellPositionNetTask {

    private PositionCallBack positionCallBack;

    public CellPositionNetTask(PositionCallBack positionCallBack) {
        this.positionCallBack = positionCallBack;
    }

    public void getCellPosition(final String lac, final String cid, final String nid, final String mnc) {
        Map<String, String> map = getFileMap(lac, cid, nid, mnc);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(CellInfoUtil.URL_CELL_LOCATION)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        CellSearchInterface cellSearchInterface = retrofit.create(CellSearchInterface.class);
        cellSearchInterface.getCellPosition(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CellPosition>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(CellPosition cellPosition) {
                        if (cellPosition == null) {
                            if (positionCallBack != null) {
                                positionCallBack.onErro();
                            }
                            return;
                        }

                        if (positionCallBack != null) {
                            positionCallBack.onSuccessed(cellPosition);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (positionCallBack != null) {
                        positionCallBack.onErro();
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }


    private static Map<String, String> getFileMap(String lac, String cid, String nid, String mnc) {
        Map<String, String> map = new ArrayMap<>();
        map.put("regCode", RegOperateTool.strreg);
        map.put("bid", cid);
        map.put("sid", lac);
        map.put("nid", nid);
        map.put("strmnc", mnc);
        return map;
    }

}
