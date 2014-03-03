package com.dms.comm;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.dms.utils.DBUtils;
import com.mysql.jdbc.StringUtils;

public class BaseDao<T> {

	protected Class<?> entityClass;

	private boolean isInterface = false;

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	protected BaseDao() {
		Type clazz = getClass().getGenericSuperclass();
		Type params[] = ((ParameterizedType) clazz).getActualTypeArguments();
		entityClass = (Class<?>) params[0];
		isInterface = entityClass.isInterface();
	}

	// Query-------------------------------
	@SuppressWarnings("unchecked")
	public T queryForObject(Map<String, Object> paramMap) {
		setEntityClass(paramMap);
		return (T) jdbcTemplate.queryForObject(
				DBUtils.getQuerySQL(paramMap, entityClass), paramMap,
				entityClass);
	}

	@SuppressWarnings("unchecked")
	public List<T> queryForList(Map<String, Object> paramMap) {
		setEntityClass(paramMap);
		return (List<T>) jdbcTemplate.queryForList(
				DBUtils.getQuerySQL(paramMap, entityClass), paramMap,
				entityClass);
	}

	public List<Map<String, Object>> queryForListMap(
			Map<String, Object> paramMap) {
		setEntityClass(paramMap);
		return jdbcTemplate.queryForList(
				DBUtils.getQuerySQL(paramMap, entityClass), paramMap);
	}

	// DML
	public int insert(Map<String, Object> paramMap) {
		setEntityClass(paramMap);
		return jdbcTemplate.update(DBUtils.getInsertSQL(paramMap, entityClass),
				paramMap);
	}

	public int update(Map<String, Object> paramMap) {
		setEntityClass(paramMap);
		return jdbcTemplate.update(DBUtils.getUpdateSQL(paramMap, entityClass),
				paramMap);
	}

	public int delete(Map<String, Object> paramMap) {
		setEntityClass(paramMap);
		return jdbcTemplate.update(DBUtils.getRemoveSQL(paramMap, entityClass),
				paramMap);
	}

	// customization
	public T execute(Map<String, Object> paramMap) {
		setEntityClass(paramMap);
		return jdbcTemplate.execute(
				"insert into room values(:rId,:dId,:leader,:phone,:sNum)",
				paramMap, new PreparedStatementCallback<T>() {

					public T doInPreparedStatement(PreparedStatement ps)
							throws SQLException, DataAccessException {
						ps.execute();
						return null;
					}
				});
	}

	public int count(Map<String, Object> paramMap) {
		setEntityClass(paramMap);
		return jdbcTemplate.queryForInt(
				DBUtils.getCountSQL(paramMap, entityClass), paramMap);
	}

	@SuppressWarnings("unchecked")
	public List<T> pageQueryWithStartAndLenth(Map<String, Object> paramMap) {
		setEntityClass(paramMap);
		return (List<T>) jdbcTemplate.queryForList(
				DBUtils.getPageSQL(paramMap, entityClass), paramMap,
				entityClass);
	}

	public List<T> pageQueryWithSizeAndPage(Map<String, Object> paramMap) {
		setEntityClass(paramMap);
		String sizeValid = (String) paramMap.get(DBComms.size);
		String pageValid = (String) paramMap.get(DBComms.page);
		assert !StringUtils.isNullOrEmpty(sizeValid) : "Size missing";
		// make sure to cast successfully
		int size = Integer.valueOf(sizeValid);
		int pageNum = Integer.valueOf(pageValid);
		paramMap.put(DBComms.startIndex, pageNum * size);
		paramMap.put(DBComms.length, size);
		return pageQueryWithStartAndLenth(paramMap);
	}

	public int[] batchUpdate(Map<String, Object>[] batchValues) {
		return jdbcTemplate.batchUpdate(
				DBUtils.getUpdateSQL(batchValues[0], entityClass), batchValues);
	}

	public int[] batchInsert(Map<String, Object>[] batchValues) {
		return jdbcTemplate.batchUpdate(
				DBUtils.getInsertSQL(batchValues[0], entityClass), batchValues);
	}

	public int[] batchRemove(Map<String, Object>[] batchValues) {
		return jdbcTemplate.batchUpdate(
				DBUtils.getRemoveSQL(batchValues[0], entityClass), batchValues);
	}

	// Utility-----------------------------
	public Class<?> getActualClass() {
		return entityClass;
	}

	public void setEntityClass(Map<String, Object> paramMap) {
		if (!isInterface) {
			return;
		}
		String clazz = (String) paramMap.get("class");
		if (StringUtils.isNullOrEmpty(clazz)) {
			return;
		} else {
			try {
				Class<?> c = Class.forName(clazz.substring("class ".length()));
				if (entityClass.isAssignableFrom(c) && !c.isInterface()) {
					entityClass = c;
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	public NamedParameterJdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
}
