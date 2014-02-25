package com.dms.bean;

public class Room {
	/** 房间编号 */
	private String rId;
	/** 寝室编号 */
	private String dId;
	/** 电话 */
	private String phone;
	/** 舍长 */
	private String leader;
	/** 学生数 */
	private int sNum =-1;

	public String getrId() {
		return rId;
	}

	public void setrId(String rId) {
		this.rId = rId;
	}

	public String getdId() {
		return dId;
	}

	public void setdId(String dId) {
		this.dId = dId;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getLeader() {
		return leader;
	}

	public void setLeader(String leader) {
		this.leader = leader;
	}

	public int getsNum() {
		return sNum;
	}

	public void setsNum(int sNum) {
		this.sNum = sNum;
	}
}
