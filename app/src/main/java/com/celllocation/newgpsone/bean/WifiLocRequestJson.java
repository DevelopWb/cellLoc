package com.celllocation.newgpsone.bean;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2021/10/15 22:35
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/10/15 22:35
 */
public class WifiLocRequestJson {

    private String mac_address;
    private int age;
    private int singal_strength;

    public WifiLocRequestJson(String mac_address, int age, int singal_strength) {
        this.mac_address = mac_address;
        this.age = age;
        this.singal_strength = singal_strength;
    }

    public String getMac_address() {
        return mac_address == null ? "" : mac_address;
    }

    public void setMac_address(String mac_address) {
        this.mac_address = mac_address == null ? "" : mac_address;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getSingal_strength() {
        return singal_strength;
    }

    public void setSingal_strength(int singal_strength) {
        this.singal_strength = singal_strength;
    }
}
