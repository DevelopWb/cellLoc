package com.celllocation.newgpsone.cellInfos;

import com.celllocation.newgpsone.bean.CellPosition;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Author:wang_sir
 * Time:2018/3/21 11:24
 * Description:This is CellSearchInterface
 */
public interface CellSearchInterface {

    @FormUrlEncoded
    @POST("/Luce/RNMStatePosition.asmx/GetInfByCLM2")
    Observable<CellPosition> getCellPosition(@FieldMap Map<String, String> maps);

}
