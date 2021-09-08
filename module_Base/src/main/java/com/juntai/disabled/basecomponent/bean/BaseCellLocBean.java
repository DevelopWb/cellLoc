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

    private String resultcode;
    private String reason;
    private int error_code;

    public String getResultcode() {
        return resultcode == null ? "" : resultcode;
    }

    public void setResultcode(String resultcode) {
        this.resultcode = resultcode == null ? "" : resultcode;
    }

    public String getReason() {
        return reason == null ? "" : reason;
    }

    public void setReason(String reason) {
        this.reason = reason == null ? "" : reason;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }
}
