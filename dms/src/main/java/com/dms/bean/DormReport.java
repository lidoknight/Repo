package com.dms.bean;

import java.util.Date;

public class DormReport {

	/** 宿舍编号 */
	private String dId;
	/** 宿舍通告 */
	private String msg;
	/** 通告日期 */
	private Date repDate;

	public String getdId() {
		return dId;
	}

	public void setdId(String dId) {
		this.dId = dId;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Date getRepDate() {
		return repDate;
	}

	public void setRepDate(Date repDate) {
		this.repDate = repDate;
	}

}
