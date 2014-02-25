package com.dms.bean;

import java.util.Date;

public class Student {

	/** 学号 */
	private String sId;
	/** 登录密码 */
	private String pswd;
	/** 姓名 */
	private String sName;
	/** 性别 */
	private String isBoy;
	/** 学院 */
	private String collegeId;
	/** 班级 */
	private String classId;
	/**寝室编号*/
	private String dormitoryId;
	/**房间编号*/
	private String roomId;
	/** 辅导员 */
	private String instructorId;
	/** 电话 */
	private String phone;
	/** 出生日期 */
	private Date birDate;
	/** 入校日期 */
	private Date inDate;
	/** 离校日期 */
	private Date outDate;

	public String getsId() {
		return sId;
	}

	public void setsId(String sId) {
		this.sId = sId;
	}

	public String getPswd() {
		return pswd;
	}

	public void setPswd(String pswd) {
		this.pswd = pswd;
	}

	public String getsName() {
		return sName;
	}

	public void setsName(String sName) {
		this.sName = sName;
	}

	public String getIsBoy() {
		return isBoy;
	}

	public void setIsBoy(String isBoy) {
		this.isBoy = isBoy;
	}

	public String getCollegeId() {
		return collegeId;
	}

	public void setCollegeId(String collegeId) {
		this.collegeId = collegeId;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getDormitoryId() {
		return dormitoryId;
	}

	public void setDormitoryId(String dormitoryId) {
		this.dormitoryId = dormitoryId;
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public String getInstructorId() {
		return instructorId;
	}

	public void setInstructorId(String instructorId) {
		this.instructorId = instructorId;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Date getBirDate() {
		return birDate;
	}

	public void setBirDate(Date birDate) {
		this.birDate = birDate;
	}

	public Date getInDate() {
		return inDate;
	}

	public void setInDate(Date inDate) {
		this.inDate = inDate;
	}

	public Date getOutDate() {
		return outDate;
	}

	public void setOutDate(Date outDate) {
		this.outDate = outDate;
	}
}
