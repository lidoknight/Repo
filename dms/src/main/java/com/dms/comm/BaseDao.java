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

public class BaseDao<T> {

	protected Class<?> entityClass;

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	protected BaseDao() {
		Type clazz = getClass().getGenericSuperclass();
		Type params[] = ((ParameterizedType) clazz).getActualTypeArguments();
		entityClass = (Class<?>) params[0];
	}

	// Query-------------------------------
	@SuppressWarnings("unchecked")
	public T queryForObject(Map<String, ?> paramMap) {
		return (T) jdbcTemplate.queryForObject(
				DBUtils.getQuerySQL(paramMap, entityClass), paramMap,
				entityClass);
	}

	@SuppressWarnings("unchecked")
	public List<T> queryForList(Map<String, ?> paramMap) {
		return (List<T>) jdbcTemplate.queryForList(
				DBUtils.getQuerySQL(paramMap, entityClass), paramMap,
				entityClass);
	}

	public List<Map<String, Object>> queryForListMap(Map<String, ?> paramMap) {
		return jdbcTemplate.queryForList(
				DBUtils.getQuerySQL(paramMap, entityClass), paramMap);
	}

	// DML
	public int insert(Map<String, ?> paramMap) {
		return jdbcTemplate.update(DBUtils.getInsertSQL(paramMap, entityClass),
				paramMap);
	}

	public int update(Map<String, ?> paramMap) {
		return jdbcTemplate.update(DBUtils.getUpdateSQL(paramMap, entityClass),
				paramMap);
	}

	public int delete(Map<String, ?> paramMap) {
		return jdbcTemplate.update(DBUtils.getRemoveSQL(paramMap, entityClass),
				paramMap);
	}

	
	//customization
	public T execute(Map<String, ?> paramMap) {
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

	// Utility-----------------------------
	public Class<?> getActualClass() {
		return entityClass;
	}

	public NamedParameterJdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
}
