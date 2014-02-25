package com.dms.bean;

import java.util.Date;

public class TakeRecord {
	/** 学生编号 */
	private String sId;
	/** 外带物品 */
	private String thing;
	/** 外带日期 */
	private Date takeDate;

	public String getsId() {
		return sId;
	}

	public void setsId(String sId) {
		this.sId = sId;
	}

	public String getThing() {
		return thing;
	}

	public void setThing(String thing) {
		this.thing = thing;
	}

	public Date getTakeDate() {
		return takeDate;
	}

	public void setTakeDate(Date takeDate) {
		this.takeDate = takeDate;
	}

}
