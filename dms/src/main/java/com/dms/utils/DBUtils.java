package com.dms.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.util.CollectionUtils;

import com.dms.comm.DBComms;
import com.dms.comm.PostCallback;
import com.mysql.jdbc.StringUtils;

public class DBUtils extends DBComms {

	private static ConcurrentHashMap<String, String> fieldMap = new ConcurrentHashMap<String, String>();

	public static String getQuerySQL(Map<String, Object> paramMap,
			Class<?> entityClass) {
		List<String> retCols = new ArrayList<String>();
		retCols.add("*");
		return getQuerySQL(paramMap, entityClass, retCols);
	}

	public static String getQuerySQL(Map<String, Object> paramMap,
			Class<?> entityClass, List<String> retCols) {
		return getQuerySQL(paramMap, entityClass, retCols, null);
	}

	public static String getQuerySQL(Map<String, Object> paramMap,
			Class<?> entityClass, PostCallback callback) {
		List<String> retCols = new ArrayList<String>();
		retCols.add("*");
		return getQuerySQL(paramMap, entityClass, retCols, callback);
	}

	public static String getQuerySQL(Map<String, Object> paramMap,
			Class<?> entityClass, List<String> retCols, PostCallback callback) {
		String tableName = toTableField(entityClass.getSimpleName(), true);
		StringBuilder temp = new StringBuilder(retCols.get(0));
		for (int i = 1; i < retCols.size(); i++) {
			temp.append(comma + toTableField(retCols.get(i), false));
		}
		StringBuilder sb = new StringBuilder(select).append(temp).append(
				from + tableName);
		String clause = "";
		if (!CollectionUtils.isEmpty(paramMap)
				&& !(clause = extractField(paramMap, entityClass, true))
						.equals("")) {
			sb.append(where);
			return sb.append(clause)
					.append(callback != null ? callback.append() : "")
					.toString();
		}
		return sb.append(callback != null ? callback.append() : "").toString();
	}

	public static String getUpdateSQL(Map<String, Object> paramMap,
			Class<?> entityClass) {
		assert !CollectionUtils.isEmpty(paramMap) : "Collection should not be null";
		String tableName = toTableField(entityClass.getSimpleName(), true);
		String clause = "";
		StringBuilder sb = new StringBuilder(update + tableName + set);
		// collections not null && clause not null
		clause = extractField(paramMap, entityClass, true);
		assert !StringUtils.isNullOrEmpty(clause) : "Ensure field match";
		return sb.append(clause).toString();
	}

	public static String getRemoveSQL(Map<String, Object> paramMap,
			Class<?> entityClass) {
		// 不支持批量删除
		assert !CollectionUtils.isEmpty(paramMap) : "Collection should not be null";
		String tableName = toTableField(entityClass.getSimpleName(), true);
		String clause = "";
		StringBuilder sb = new StringBuilder(delete + from + tableName);
		// collections not null && clause not null
		clause = extractField(paramMap, entityClass, true);
		assert !StringUtils.isNullOrEmpty(clause) : "Ensure field match";
		return sb.append(where + clause).toString();
	}

	public static String getInsertSQL(Map<String, Object> paramMap,
			Class<?> entityClass) {
		assert !CollectionUtils.isEmpty(paramMap) : "Collection should not be null";
		String tableName = toTableField(entityClass.getSimpleName(), true);
		String clause = "";
		StringBuilder sb = new StringBuilder(insert + tableName);
		// collections not null && clause not null
		clause = extractField(paramMap, entityClass, false);
		assert !StringUtils.isNullOrEmpty(clause) : "Ensure field match";
		StringBuilder sb2 = new StringBuilder(bracketL);
		for (String fieldName : clause.split(comma + "|:")) {
			if (!StringUtils.isEmptyOrWhitespaceOnly(fieldName)) {
				sb2.append(toTableField(fieldName, false) + ",");
			}
		}
		sb2.deleteCharAt(sb2.length() - 1);
		sb2.append(bracketR);
		return sb.append(sb2).append(values + bracketL + clause + bracketR)
				.toString();
	}

	// ////////////Customization
	public static String getCountSQL(Map<String, Object> paramMap,
			Class<?> entityClass) {
		List<String> retCols = new ArrayList<String>();
		retCols.add("count(*)");
		return getQuerySQL(paramMap, entityClass, retCols);
	}

	public static String getPageSQL(Map<String, Object> paramMap,
			Class<?> entityClass) {
		// you should have properties:page.startIndex and page.length in Map
		List<String> retCols = new ArrayList<String>();
		retCols.add("*");
		final String start = (String) paramMap.get(startIndex);
		final String len = (String) paramMap.get(length);
		assert !StringUtils.isNullOrEmpty(start)
				|| !StringUtils.isNullOrEmpty(len) : "Page Query Parameters missing";
		return getQuerySQL(paramMap, entityClass, retCols, new PostCallback() {

			public String append() {
				return limit + start + comma + len;
			}
		});
	}

	private static String toTableField(String fieldName, boolean isClass) {
		char[] eles = fieldName.toCharArray();
		StringBuilder sb = new StringBuilder();
		for (char ele : eles) {
			if (Character.isUpperCase(ele)) {
				sb.append("_" + ele);
			} else {
				sb.append(Character.toUpperCase(ele));
			}
		}
		return isClass ? sb.substring(1) : sb.toString();
	}

	private static String extractField(Map<String, Object> paramMap,
			Class<?> entityClass, boolean eqMode) {
		StringBuilder sb = new StringBuilder();
		Set<String> keySet = paramMap.keySet();
		for (String key : keySet) {
			try {
				entityClass.getDeclaredField(key);// check validate
			} catch (SecurityException e) {
				continue;
			} catch (NoSuchFieldException e) {
				continue;
			}
			if (eqMode) {
				sb.append(toTableField(key, false));
				sb.append(eq);
			}
			sb.append(":" + key);
			sb.append(eqMode ? and : comma);
		}
		if (StringUtils.isNullOrEmpty(sb.toString())) {
			return "";
		}
		return sb.substring(0, sb.length() - (eqMode ? and.length() : 1));
	}

	@SuppressWarnings("unchecked")
	public static <T> Map<String, Object> convertToMap(T entity,
			boolean compress) {
		if (null != entity) {
			if (entity instanceof Map) {
				return (Map<String, Object>) entity;
			} else {
				try {
					Map<String, Object> paramMap = BeanUtils.describe(entity);
					if (compress) {
						Field[] fields = entity.getClass().getDeclaredFields();
						Object obj = null;
						for (Field field : fields) {
							obj = paramMap.get(field.getName());
							if (null == obj) {
								paramMap.remove(field.getName());
							} else if (obj.getClass() != field.getType()) {
								// 默认值处理
								if (field.getType().equals(int.class)
										|| field.getType()
												.equals(Integer.class)) {
									field.setAccessible(true);
									Integer value = field.getInt(entity);
									if (value == -1) {
										paramMap.remove(field.getName());
									}
								}
							}
						}
					}
					return paramMap;
				} catch (IllegalAccessException e) {
				} catch (InvocationTargetException e) {
				} catch (NoSuchMethodException e) {
				}
			}
		}
		return Collections.emptyMap();
	}

	public static String mapField(String fieldName, boolean isClass) {
		String field = fieldMap.get(fieldName);
		if (StringUtils.isNullOrEmpty(field)) {
			field = toTableField(fieldName, isClass);
			fieldMap.put(fieldName, field);
		}
		return field;
	}
}
