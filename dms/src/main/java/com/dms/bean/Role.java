package com.dms.bean;

public class Role {
	/** 角色编号 */
	private String rId;
	/** 登录密码 */
	private String pswd;
	/** 角色名称 */
	private String rName;
	/** 电话 */
	private String phone;
	/** 类型 */
	private String type;
	/** 所属组织(College;Dormitory) */
	private String belong;

	public String getrId() {
		return rId;
	}

	public void setrId(String rId) {
		this.rId = rId;
	}
	
	public String getPswd() {
		return pswd;
	}

	public void setPswd(String pswd) {
		this.pswd = pswd;
	}

	public String getrName() {
		return rName;
	}

	public void setrName(String rName) {
		this.rName = rName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getBelong() {
		return belong;
	}

	public void setBelong(String belong) {
		this.belong = belong;
	}
}
