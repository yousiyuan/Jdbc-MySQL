package org.lnson.util;

import java.io.*;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;
import java.util.Map.Entry;

/**
 * JDBC工具类
 * <p>1、执行CURD语句</p>
 * <p>2、批量执行删除、更新、插入操作</p>
 * <p>3、执行CURD任意组合的字符串命令</p>
 * <p>4、执行存储过程</p>
 * <p>5、支持数据库连接池</p>
 * <p>6、支持数据库事务</p>
 */
public final class JdbcUtils {

	private Connection connection = null;

	/**
	 * 从连接池中获取一个连接数据库的对象
	 */
	public static JdbcUtils getJdbc() {
		try {
			return new JdbcUtils();
		} catch (SQLException | ClassNotFoundException | IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 释放连接数据库的对象回连接池
	 */
	public void dispose() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将Object对象按照给定类型拆箱
	 * 
	 * @param type
	 *            要转换的类型
	 * @param value
	 *            Object对象
	 * @return 类型转换后的对象
	 */
	@SuppressWarnings("unchecked")
	public <T extends Object> T cast(Class<T> type, Object value) {
		// 把value拆箱为T类型
		if (type.isInstance(value))
			return type.cast(value);

		// 允许BigDecimal拆箱为Integer
		if (type.equals(Integer.class) && BigDecimal.class.isInstance(value))
			return (T) Integer.valueOf(value.toString());

		// 允许BigDecimal拆箱为Short
		if (type.equals(Short.class) && BigDecimal.class.isInstance(value))
			return (T) Short.valueOf(value.toString());

		// 允许BigDecimal拆箱为Long
		if (type.equals(Long.class) && BigDecimal.class.isInstance(value))
			return (T) Long.valueOf(value.toString());

		return null;
	}

	/**
	 * 将集合切割为若干个等长度的子集合的集合
	 */
	public static <U> List<List<U>> SliceList(List<U> list, int size) {
		List<List<U>> coll = new ArrayList<List<U>>();
		List<U> divList = list.subList(0, Math.min(size, list.size()));
		coll.add(new ArrayList<U>(divList));
		divList.clear();
		if (list.size() > 0)
			coll.addAll(SliceList(list, size));
		return coll;
	}

	/**
	 * 合并数组
	 */
	public static int[] mergeArray(int[] array1, int[] array2) {
		int[] array = new int[array1.length + array2.length];
		System.arraycopy(array1, 0, array, 0, array1.length);
		System.arraycopy(array2, 0, array, array1.length, array2.length);
		return array;
	}

	/**
	 * Integer数组转int数组
	 */
	public static int[] arrayConverter(Integer[] array) {
		return Arrays.stream(array).mapToInt(Integer::valueOf).toArray();
	}

	/**
	 * 开启事务
	 */
	public boolean beginTransaction() {
		try {
			connection.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 提交事务
	 */
	public boolean commit() {
		try {
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 回滚事务
	 */
	public boolean rollback() {
		try {
			connection.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 执行T-SQL插入语句
	 * 
	 * @param sql
	 *            T-SQL语句
	 * @param parameters
	 *            T-SQL参数列表
	 * @return 执行T-SQL删除语句受影响的记录数
	 */
	public int insert(String sql, List<JdbcSqlParameter> parameters) {
		try {
			return executeUpdate(sql, parameters);
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 执行T-SQL删除语句
	 * 
	 * @param sql
	 *            T-SQL语句
	 * @param parameters
	 *            T-SQL参数列表
	 * @return 执行T-SQL删除语句受影响的记录数
	 */
	public int delete(String sql, List<JdbcSqlParameter> parameters) {
		try {
			return executeUpdate(sql, parameters);
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 执行T-SQL更新语句
	 * 
	 * @param sql
	 *            T-SQL语句
	 * @param parameters
	 *            T-SQL参数列表
	 * @return 执行T-SQL更新语句受影响的记录数
	 */
	public int update(String sql, List<JdbcSqlParameter> parameters) {
		try {
			return executeUpdate(sql, parameters);
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 执行查询SQL语句返回一个结果集
	 * 
	 * @param sql
	 *            T-SQL语句
	 * @param parameters
	 *            SQL语句的参数列表
	 * @return 执行T-SQL返回的结果集
	 */
	public LinkedHashMap<String, Object> queryForObject(String sql, List<JdbcSqlParameter> parameters) {
		List<LinkedHashMap<String, Object>> entityList = queryForList(sql, parameters);
		if (entityList != null && entityList.size() > 0)
			return entityList.get(0);
		else
			return null;
	}

	/**
	 * 执行查询SQL语句返回所有结果集的列表
	 * 
	 * @param sql
	 *            T-SQL语句
	 * @param parameters
	 *            SQL语句的参数列表
	 * @return 结果集
	 */
	public List<LinkedHashMap<String, Object>> queryForList(String sql, List<JdbcSqlParameter> parameters) {
		try {
			return executeQuery(sql, parameters);
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 用来执行返回多个结果集的sql语句，即当执行某个存储过程或动态执行未知 SQL
	 * 字符串（即应用程序程序员在编译时未知）时，有可能出现多个结果的情况。
	 * 
	 * @param sql
	 *            存储过程或者未知的SQL字符串
	 * @param parameters
	 *            参数列表
	 * @return Object类型表示的集合
	 */
	public List<Object> executeCommand(String sql, List<JdbcSqlParameter> parameters) {
		try {
			List<Object> result = execute(sql, parameters);
			return result;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 向数据库提交要运行的一批命令。
	 * 
	 * @param sqlCommands
	 *            要执行的SQL命令
	 * @param parametersList
	 *            参数列表的集合
	 * @param batchNumber
	 *            每次执行SQL命令的数量
	 * @return 返回更新记录集的条数的集合
	 */
	public int[] executeBatchCommand(String sqlCommands, List<List<JdbcSqlParameter>> parametersList,
			Integer batchNumber) {
		try {
			int[] updateCountArray = executeBatch(sqlCommands, parametersList, batchNumber);
			return updateCountArray;
		} catch (SQLException e) {
			e.printStackTrace();
			return new int[0];
		}
	}

	/**
	 * 执行无返回值的数据库存储过程
	 * 
	 * @param procStatement
	 *            存储过程执行语句
	 * @param parameters
	 *            参数列表
	 */
	public void executeDatabaseProc(String procStatement, List<JdbcSqlParameter> parameters) {
		try {
			executeProcedure(procStatement, parameters);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 执行带返回值的数据库存储过程
	 * 
	 * @param procStatement
	 *            存储过程执行语句
	 * @param parameters
	 *            参数列表
	 * @param outParameter
	 *            输出参数
	 * @return 存储过程执行完毕的返回值
	 */
	public Object executeDatabaseProc(String procStatement, List<JdbcSqlParameter> parameters,
			JdbcSqlParameter outParameter) {
		try {
			Object value = executeProcedure(procStatement, parameters, outParameter);
			return value;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 用于执行单个 INSERT、UPDATE 、 DELETE 语句或不返回任何内容的 SQL 语句
	 */
	private int executeUpdate(String sql, List<JdbcSqlParameter> parameters) throws SQLException {
		int effectRows = 0;

		PreparedStatement stat = connection.prepareStatement(sql);
		if (null != parameters && parameters.size() > 0)
			for (int i = 0; i < parameters.size(); i++)
				setParameter(stat, i + 1, parameters.get(i));

		effectRows = stat.executeUpdate();
		stat.close();

		return effectRows;
	}

	/**
	 * 用于产生单个结果集的语句，执行单个查询语句
	 */
	private List<LinkedHashMap<String, Object>> executeQuery(String sql, List<JdbcSqlParameter> parameters)
			throws SQLException, ClassNotFoundException {
		List<LinkedHashMap<String, Object>> entityList = null;

		PreparedStatement stat = connection.prepareStatement(sql);
		if (null != parameters && parameters.size() > 0)
			for (int i = 0; i < parameters.size(); i++)
				setParameter(stat, i + 1, parameters.get(i));

		ResultSet rs = stat.executeQuery();
		entityList = formatResultSet(rs);

		rs.close();
		stat.close();

		return entityList;
	}

	/**
	 * 用来执行返回多个结果集的sql语句，即当执行某个存储过程或动态执行未知 SQL
	 * 字符串（即应用程序程序员在编译时未知）时，有可能出现多个结果的情况。
	 */
	private List<Object> execute(String sql, List<JdbcSqlParameter> parameters)
			throws SQLException, ClassNotFoundException {
		List<Object> resultSetList = new ArrayList<Object>();

		PreparedStatement stat = connection.prepareStatement(sql);
		if (null != parameters && parameters.size() > 0)
			for (int i = 0; i < parameters.size(); i++)
				setParameter(stat, i + 1, parameters.get(i));
		boolean hasResultSet = stat.execute();
		getResultSetAndUpdateCount(stat, hasResultSet, resultSetList);
		stat.close();

		return resultSetList;
	}

	/**
	 * 向数据库提交要运行的一批命令。如果所有命令都成功运行，则返回一个更新计数数组。
	 */
	private int[] executeBatch(String sql, List<List<JdbcSqlParameter>> parametersList, Integer batchNumber)
			throws SQLException {
		PreparedStatement stat = connection.prepareStatement(sql);

		int[] result = new int[0];
		List<List<List<JdbcSqlParameter>>> divParametersColl = SliceList(parametersList,
				(batchNumber == null || batchNumber == 0) ? 50 : batchNumber);
		for (List<List<JdbcSqlParameter>> divParameters : divParametersColl) {
			int[] updateCountList = executeBatch(stat, divParameters);
			result = mergeArray(result, updateCountList);
		}
		return result;
	}

	/**
	 * 将传递过来的一批SQL或DDL指令全部执行完毕
	 */
	private int[] executeBatch(PreparedStatement stat, List<List<JdbcSqlParameter>> parametersList)
			throws SQLException {
		for (List<JdbcSqlParameter> parameters : parametersList) {
			for (int i = 0; i < parameters.size(); i++) {
				setParameter(stat, i + 1, parameters.get(i));
			}
			stat.addBatch();
		}
		int[] updateCountArray = stat.executeBatch();
		stat.clearBatch();

		return updateCountArray;
	}

	/**
	 * 执行无返回值的存储过程
	 */
	private void executeProcedure(String procedure, List<JdbcSqlParameter> parameters) throws SQLException {
		CallableStatement stat = connection.prepareCall(procedure);
		if (null != parameters && parameters.size() > 0)
			for (int i = 0; i < parameters.size(); i++)
				setParameter(stat, i + 1, parameters.get(i));
		stat.execute();
		stat.close();
	}

	/**
	 * 执行有返回值的存储过程
	 */
	private Object executeProcedure(String procedure, List<JdbcSqlParameter> parameters, JdbcSqlParameter outParameter)
			throws SQLException {
		int size = parameters.size();
		CallableStatement stat = connection.prepareCall(procedure);
		if (null != parameters && size > 0)
			for (int i = 0; i < size; i++)
				setParameter(stat, i + 1, parameters.get(i));

		stat.registerOutParameter(size + 1, outParameter.getTargetSqlType().getVendorTypeNumber(),
				outParameter.getScaleOrLength());

		Object result = stat.getObject(size + 1);
		stat.close();
		return result;
	}

	/**
	 * 通过递归按照执行SQL或DDL命令的顺序获取所有ResultSet格式化后的结果集以及更新的记录数
	 */
	private void getResultSetAndUpdateCount(PreparedStatement statement, Boolean hasResultSet,
			List<Object> resultSetList) throws SQLException, ClassNotFoundException {
		// 如果为true则没有更多的结果集
		if ((hasResultSet == false) && (statement.getUpdateCount() == -1)) {
			return;
		}
		if (hasResultSet) {
			ResultSet rs = statement.getResultSet();
			resultSetList.add(formatResultSet(rs));
			rs.close();

			hasResultSet = statement.getMoreResults();
			getResultSetAndUpdateCount(statement, hasResultSet, resultSetList);
		} else {
			int effectRows = statement.getUpdateCount();
			resultSetList.add(effectRows);

			hasResultSet = statement.getMoreResults();
			getResultSetAndUpdateCount(statement, hasResultSet, resultSetList);
		}
	}

	/**
	 * 为Statement的SQL语句设置参数
	 */
	private void setParameter(PreparedStatement stat, int parameterIndex, JdbcSqlParameter parameter)
			throws SQLException {
		Object value = parameter.getValue();
		SQLType targetSqlType = parameter.getTargetSqlType();
		int scaleOrLength = parameter.getScaleOrLength();

		stat.setObject(parameterIndex, value, targetSqlType.getVendorTypeNumber(), scaleOrLength);
	}

	/**
	 * 格式化ResultSet返回的结果集
	 */
	private List<LinkedHashMap<String, Object>> formatResultSet(ResultSet resultSet)
			throws SQLException, ClassNotFoundException {
		LinkedHashMap<String, String> columns = getColumns(resultSet);

		List<LinkedHashMap<String, Object>> entityList = new ArrayList<LinkedHashMap<String, Object>>();
		LinkedHashMap<String, Object> entity = null;
		while (resultSet.next()) {
			entity = new LinkedHashMap<String, Object>();

			Iterator<Entry<String, String>> iter = columns.entrySet().iterator();
			while (iter.hasNext()) {
				Entry<String, String> field = iter.next();
				String columnLabel = field.getKey();
				String className = field.getValue();

				entity.put(columnLabel, this.cast(Class.forName(className), resultSet.getObject(columnLabel)));
			}

			entityList.add(entity);
		}

		return entityList;
	}

	/**
	 * 返回Statement语句中ColumnLabel的集合
	 */
	private LinkedHashMap<String, String> getColumns(ResultSet resultSet) throws SQLException {
		LinkedHashMap<String, String> columns = new LinkedHashMap<String, String>();
		ResultSetMetaData rsmd = resultSet.getMetaData();
		int columnCount = rsmd.getColumnCount();
		for (int i = 1; i <= columnCount; i++) {
			columns.put(rsmd.getColumnLabel(i), rsmd.getColumnClassName(i));
		}
		return columns;
	}

	/**
	 * 从连接池中取用一个连接
	 */
	private JdbcUtils() throws SQLException, ClassNotFoundException, IOException {
		this(JdbcUtils.class.getClassLoader().getResourceAsStream("db.properties"));
	}

	/**
	 * 不使用连接池，使用db.properties配置文件字节输入流创建一个连接数据库的对象
	 */
	private JdbcUtils(InputStream inStream) throws IOException, SQLException, ClassNotFoundException {
		// 加载资源文件
		Properties dbConfig = new Properties();
		dbConfig.load(inStream);
		inStream.close();

		// 获得数据库注册信息
		String driverClass = dbConfig.getProperty("driverClass");
		String url = dbConfig.getProperty("url");
		String username = dbConfig.getProperty("username");
		String password = dbConfig.getProperty("password");

		// 注册数据库驱动
		Class.forName(driverClass);
		// 创建连接数据库的对象
		connection = DriverManager.getConnection(url, username, password);
	}
}
