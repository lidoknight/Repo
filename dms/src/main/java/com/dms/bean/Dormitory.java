package com.dms.bean;

import java.math.BigDecimal;

public class Dormitory {
	/** 寝室编号 */
	private String dId;
	/** 寝室名称 */
	private String dName;
	/** 负责人 */
	private String leader;
	/** 房间数 */
	private int roomNum = -1;
	/** 学生数 */
	private int sNum = -1;
	/** 管理人员数 */
	private int roleNum = -1;
	/** 宿舍费用 */
	private BigDecimal fee;

	public String getdId() {
		return dId;
	}

	public void setdId(String dId) {
		this.dId = dId;
	}

	public String getdName() {
		return dName;
	}

	public void setdName(String dName) {
		this.dName = dName;
	}

	public String getLeader() {
		return leader;
	}

	public void setLeader(String leader) {
		this.leader = leader;
	}

	public int getRoomNum() {
		return roomNum;
	}

	public void setRoomNum(int roomNum) {
		this.roomNum = roomNum;
	}

	public int getsNum() {
		return sNum;
	}

	public void setsNum(int sNum) {
		this.sNum = sNum;
	}

	public int getRoleNum() {
		return roleNum;
	}

	public void setRoleNum(int roleNum) {
		this.roleNum = roleNum;
	}

	public BigDecimal getFee() {
		return fee;
	}

	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}
}
