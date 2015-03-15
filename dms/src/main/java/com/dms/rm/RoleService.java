package com.dms.rm;

import java.util.List;

import com.dms.bean.IPassword;

/**
 * 
 * 人员管理模块提供功能
 * 
 * @author Danny
 *
 */
public interface RoleService {

	boolean isExist();
	
	boolean register();
	
	boolean login();
	
	boolean updateRole();
	
	boolean addRole();
	
	boolean removeRole();
	
	boolean[] batchAddRoles();
	
	boolean[] batchRevmRoles();
	
	<T> List<T> queryRoles();
	
	
	
	//学生特有管理内容
	boolean withdraw();
	
	boolean offCollege();
}
