package com.juntai.disabled.basecomponent.bean;


import com.juntai.disabled.basecomponent.base.BaseResult;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2020/8/23 16:52
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/8/23 16:52
 */
public class BaseCellLocBean extends BaseResult {

    private String access_token;
    private int ErrCode;

    public String getAccess_token() {
        return access_token == null ? "" : access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token == null ? "" : access_token;
    }

    public int getErrCode() {
        return ErrCode;
    }

    public void setErrCode(int errCode) {
        ErrCode = errCode;
    }
}
