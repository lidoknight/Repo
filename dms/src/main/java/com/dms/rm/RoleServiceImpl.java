package com.dms.rm;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.dms.bean.IPassword;
import com.dms.comm.CollegeDao;
import com.dms.comm.DBComms;
import com.dms.comm.DormDao;
import com.dms.comm.PasswordDao;
import com.dms.utils.DBUtils;
import com.dms.utils.PageParam;

public class RoleServiceImpl {

	@Autowired
	private PasswordDao stuDao;

	@Autowired
	private DormDao dormDao;

	@Autowired
	private CollegeDao collDao;

	public boolean isExist(String sId) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("sId", sId);
		IPassword exist = stuDao.queryForObject(paramMap);
		return null != exist;
	}

	public boolean register(IPassword stu) {
		Map<String, Object> paramMap = DBUtils.convertToMap(stu, true);
		int count = stuDao.insert(paramMap);
		return count > 0;
	}

	public boolean login(IPassword stu) {
		Map<String, Object> paramMap = DBUtils.convertToMap(stu, true);
		IPassword login = stuDao.queryForObject(paramMap);
		return login != null;
	}

	public boolean updateRole(IPassword role) {
		Map<String, Object> paramMap = DBUtils.convertToMap(role, true);
		int count = stuDao.update(paramMap);
		return count > 0;
	}

	public boolean addRole(IPassword role) {
		Map<String, Object> paramMap = DBUtils.convertToMap(role, false);
		int count = stuDao.insert(paramMap);
		return count > 0;
	}

	public boolean removeRole(IPassword role) {
		Map<String, Object> paramMap = DBUtils.convertToMap(role, true);
		int count = stuDao.delete(paramMap);
		return count > 0;
	}

	public int[] batchAddRoles(List<IPassword> roles) {
		@SuppressWarnings("unchecked")
		Map<String, Object>[] batchValues = (Map<String, Object>[]) Array
				.newInstance(HashMap.class, roles.size());
		int i = 0;
		for (IPassword role : roles) {
			batchValues[i++] = DBUtils.convertToMap(role, false);
		}
		return stuDao.batchInsert(batchValues);
	}

	public int[] batchUpdateRoles(List<IPassword> roles) {
		@SuppressWarnings("unchecked")
		Map<String, Object>[] batchValues = (Map<String, Object>[]) Array
				.newInstance(HashMap.class, roles.size());
		int i = 0;
		for (IPassword role : roles) {
			batchValues[i++] = DBUtils.convertToMap(role, false);
		}
		return stuDao.batchUpdate(batchValues);
	}

	public int[] batchRemoveRoles(List<IPassword> roles) {
		@SuppressWarnings("unchecked")
		Map<String, Object>[] batchValues = (Map<String, Object>[]) Array
				.newInstance(HashMap.class, roles.size());
		int i = 0;
		for (IPassword role : roles) {
			batchValues[i++] = DBUtils.convertToMap(role, false);
		}
		return stuDao.batchRemove(batchValues);
	}

	public <T> List<T> rolePages(PageParam<T> page) {
		Map<String,Object> paramMap=DBUtils.convertToMap(page.getData(), true);
		paramMap.put(DBComms.page, page.getPageNum());
		paramMap.put(DBComms.size, page.getPageSize());
		return null;
	}

}
