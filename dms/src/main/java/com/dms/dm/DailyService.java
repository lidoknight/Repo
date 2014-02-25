package com.dms.dm;

import java.util.List;

import com.dms.bean.Dormitory;
import com.dms.bean.Student;

public interface DailyService {
	
	boolean takeRecord();
	
	//查询服务------------------
	
	List<Student> queryStuInfos();
	
	List<Dormitory> queryDormInfos();
	
	boolean newDorReport();
	
	boolean newBorRecord();
	
	boolean uptBorRecord();
}
