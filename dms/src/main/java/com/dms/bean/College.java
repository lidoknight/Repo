package com.dms.bean;

import java.util.Date;

public class College {
	/** 学院编号 */
	private String collegeId;
	/** 学院名称 */
	private String collegeName;
	/** 创立日期 */
	private Date setDate;
	/** 学院负责人 */
	private String leader;
	/** 学院学生总数 */
	private int sNum = -1;
	/** 学院教师总数 */
	private int tNum = -1;

	public String getCollegeId() {
		return collegeId;
	}

	public void setCollegeId(String collegeId) {
		this.collegeId = collegeId;
	}

	public String getCollegeName() {
		return collegeName;
	}

	public void setCollegeName(String collegeName) {
		this.collegeName = collegeName;
	}

	public Date getSetDate() {
		return setDate;
	}

	public void setSetDate(Date setDate) {
		this.setDate = setDate;
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

	public int gettNum() {
		return tNum;
	}

	public void settNum(int tNum) {
		this.tNum = tNum;
	}

}
