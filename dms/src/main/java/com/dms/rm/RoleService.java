package com.dms.rm;

/**
 * 
 * 人员管理模块提供功能
 * 
 * @author Danny
 *
 */
public interface RoleService {

	
	boolean register();
	
	boolean login();
	
	boolean updateRole();
	
	boolean addRole();
	
	boolean removeRole();
	
	boolean[] batchAddRoles();
	
	boolean[] batchRevmRoles();
	
	//学生特有管理内容
	boolean withdraw();
	
	boolean offCollege();
}
