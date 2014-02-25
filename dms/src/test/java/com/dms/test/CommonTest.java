package com.dms.test;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.dms.bean.Room;
import com.dms.comm.RoomDao;
import com.dms.utils.DBUtils;

/**
 * 通用功能测试
 * 
 * @author Danny
 * 
 */
public class CommonTest {

	private NamedParameterJdbcTemplate jdbcTemplate;

	private final String url = "jdbc:mysql://localhost:3306/TEST?useUnicode=true&characterEncoding=utf8";

	private final String userName = "root";

	private final String password = "root";

	private final String driverClassName = "com.mysql.jdbc.Driver";

	@BeforeClass
	public void autoWiredJdbc() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource(url,
				userName, password);
		dataSource.setDriverClassName(driverClassName);
		jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	// 后期转换为@autowired注解
	RoomDao rDao = new RoomDao();

	/**
	 * 测试获取泛型类实际参数
	 */
	@Test
	public void entityClassTest() {
		Assert.assertEquals(rDao.getActualClass(), Room.class);
	}

	@Test
	public void basicQuery() {
		rDao.setJdbcTemplate(jdbcTemplate);
		Room room = new Room();
		room.setrId("15/15-3206");
		room.setdId("15幢");
		room.setLeader("2084908125");
		room.setPhone("15107987564");
		room.setsNum(6);
		rDao.execute(convertToMap(room, false));
	}

	@Test
	public void sqlValidate() {
		Map<String, Object> paramMap = new HashMap<String, Object>() {
			private static final long serialVersionUID = 1L;
			{
				put("rId", "15/15-503");
				put("xx", "fdsfsd");
			}
		};
		paramMap.clear();
		Room room = new Room();
		room.setdId("15-503");
		room.setsNum(5);
		paramMap = convertToMap(room, true);
		Class<?> entityClass = Room.class;
		System.out.println(DBUtils.getQuerySQL(paramMap, entityClass));
		System.out.println(DBUtils.getUpdateSQL(paramMap, entityClass));
		System.out.println(DBUtils.getRemoveSQL(paramMap, entityClass));
		System.out.println(DBUtils.getInsertSQL(convertToMap(room, false),
				entityClass));
	}

	@Test
	public void utilsTest() throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		Room room = new Room();
		room.setdId("15D");
		Map<String, Object> map = BeanUtils.describe(room);
		map.remove("class");
		System.out.println(map.size());
		System.out.println(Map.class.isAssignableFrom(HashMap.class));//true
	}

	// --------------------------------------
	@SuppressWarnings("unchecked")
	private <T> Map<String, Object> convertToMap(T entity, boolean compress) {
		if (null != entity) {
			try {
				Map<String, Object> paramMap = BeanUtils.describe(entity);
				paramMap.remove("class");
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
									|| field.getType().equals(Integer.class)) {
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
		return new HashMap<String, Object>();
	}

	// @Test
	public void createTable() throws ClassNotFoundException {
		final String path = "src/main/java/com/dms/bean";
		File f = new File(path);
		File[] fs = f.listFiles(new FileFilter() {

			public boolean accept(File pathname) {
				return !pathname.getName().contains("Room");
			}
		});
		for (File s : fs) {
			String name = s.getName()
					.substring(0, s.getName().lastIndexOf('.'));
			Class<?> clazz = getClass().getClassLoader().loadClass(
					"com.dms.bean." + name);
			String sql = parseSQL(clazz);
			jdbcTemplate.execute(sql, Collections.<String, Object> emptyMap(),
					new PreparedStatementCallback<Void>() {

						public Void doInPreparedStatement(PreparedStatement ps)
								throws SQLException, DataAccessException {
							ps.execute();
							return null;
						}
					});
		}
	}

	// @Test
	void testPaserSQL() {
		System.out.println(parseSQL(Room.class));
	}

	private String parseSQL(Class<?> clazz) {
		final String prefix = "create table "
				+ DBUtils.mapField(clazz.getSimpleName(), true) + "(";
		Field[] fields = clazz.getDeclaredFields();
		StringBuilder sb = new StringBuilder(prefix);
		for (Field field : fields) {
			String fieldName = field.getName();
			Class<?> type = field.getType();
			if (type.isPrimitive()) {
				type = Integer.class;
			}
			sb.append(DBUtils.mapField(fieldName, false));
			sb.append(" ");
			sb.append(reflectMap.get(type));
			sb.append(getLen(fieldName));
			sb.append(" ");
			sb.append(",");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append(")DEFAULT CHARSET=utf8;");
		return sb.toString();
	}

	private String getLen(String name) {
		Set<String> keySet = lenMap.keySet();
		for (String key : keySet) {
			if (name.toLowerCase().contains(key)) {
				return lenMap.get(key);
			}
		}
		return "";
	}

	private Map<Type, String> reflectMap = new HashMap<Type, String>() {
		private static final long serialVersionUID = 1L;

		{
			put(String.class, "varchar");
			put(Date.class, "datetime");
			put(BigDecimal.class, "decimal");
			put(Integer.class, "int");
		}
	};

	private Map<String, String> lenMap = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;

		{
			put("id", "(10)");
			put("leader", "(10)");
			put("phone", "(15)");
			put("name", "(100)");
			put("fee", "(5,2)");
			put("num", "(5)");
			put("thing", "(100)");
			put("status", "(2)");
			put("type", "(2)");
			put("is", "(2)");
			put("msg", "(250)");
			put("pswd", "(15)");
			put("belong", "(10)");
		}
	};

}
