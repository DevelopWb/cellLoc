package com.celllocation.newgpsone.bean;

import android.text.TextUtils;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2021/4/18 15:33
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/4/18 15:33
 */
public class HomePageMenuBean {


    private  String menuName;
    private int menuPicId;

    public HomePageMenuBean(String menuName, int menuPicId) {
        this.menuName = menuName;
        this.menuPicId = menuPicId;
    }

    public String getMenuName() {
        return TextUtils.isEmpty(menuName) ? "暂无" : menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName == null ? "" : menuName;
    }

    public int getMenuPicId() {
        return menuPicId;
    }

    public void setMenuPicId(int menuPicId) {
        this.menuPicId = menuPicId;
    }
}
