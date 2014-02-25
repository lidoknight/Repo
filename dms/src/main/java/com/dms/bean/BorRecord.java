package com.dms.bean;

import java.util.Date;

public class BorRecord {

	/** 学生编号 */
	private String sId;
	/** 外借物品 */
	private String thing;
	/** 状态：借出/归还/遗失/补偿 */
	private String status;
	/** 外借日期 */
	private Date borDate;
	/** 归还日期 */
	private Date returnDate;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getBorDate() {
		return borDate;
	}
	public void setBorDate(Date borDate) {
		this.borDate = borDate;
	}
	public Date getReturnDate() {
		return returnDate;
	}
	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}
}
