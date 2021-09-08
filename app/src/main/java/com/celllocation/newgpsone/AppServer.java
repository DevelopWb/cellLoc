package com.celllocation.newgpsone;


import com.celllocation.newgpsone.bean.CellLocCDMAResultBean;
import com.juntai.disabled.basecomponent.base.BaseResult;
import com.juntai.disabled.basecomponent.bean.BaseStreamBean;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * responseBody里的数据只能调用(取出)一次，第二次为空。可赋值给新的变量使用
 */
public interface AppServer {
    /**
     * 注册
     *
     * @return
     */
    @POST(AppHttpPath.REGIST)
    Observable<BaseResult> regist(@Body RequestBody requestBody);


    @GET(AppHttpPath.CELL_LOCATE_CDMA)
    Observable<CellLocCDMAResultBean> cellLocateCDMA(@Query("sid") String sid, @Query("cellid") String cellid, @Query("nid") String nid, @Query("key") String key);

    @GET(AppHttpPath.CELL_LOCATE_OTHER)
    Observable<BaseResult> cellLocateOther(@Body RequestBody requestBody);


    /**
     * 找回密码
     *
     * @return
     */
    @POST(AppHttpPath.MODIFY_PWD)
    Observable<BaseResult> setPwd(@Query("password") String password, @Query("phoneNumber") String phoneNumber, @Query("code") String code);

}
