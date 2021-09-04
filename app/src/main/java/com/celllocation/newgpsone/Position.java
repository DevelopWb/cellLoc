package com.celllocation.newgpsone;


import java.io.Serializable;

public class Position implements Serializable {

	public String posStr = "";
	public String httpsurl = "";
	public String name = "请给我起个名吧...";
	public String address = "未知地名";
	public String number = "";

	public double x = 0.0;
	public double y = 0.0;

	public int wucha = 0;
	public long postime;
	public int lac;
	public int cid;
	public int nid = -1;
	public int numType;
	public String accuracy;
	public Position() {

	}

	public Position(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public boolean IsValid() {
		if ((this.x > 0.09 || this.x < -0.09)
				&& (this.y > 0.09 || this.y < -0.09)) {
			if (x > 18.2 && x < 53.6 && y > 73.5 && y < 135) {
				if (x > 29.0 && x < 31.0 && y > 69.0 && y < 71.0) {
					return false;
				}

				if (x > 29.0 && x < 31.0 && y > 78.0 && y < 80.0) {
					return false;
				}
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

}