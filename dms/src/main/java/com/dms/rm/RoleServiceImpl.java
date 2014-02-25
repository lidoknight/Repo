package com.dms.rm;

import org.springframework.beans.factory.annotation.Autowired;

import com.dms.comm.CollegeDao;
import com.dms.comm.DormDao;
import com.dms.comm.RoleDao;
import com.dms.comm.StudentDao;

public class RoleServiceImpl {
	
	@Autowired
	private StudentDao stuDao;
	
	@Autowired
	private RoleDao roleDao;
	
	@Autowired
	private DormDao dormDao;
	
	@Autowired
	private CollegeDao collDao;
	
}
