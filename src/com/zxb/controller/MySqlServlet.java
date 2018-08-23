package com.zxb.controller;

import java.io.IOException;
import java.sql.JDBCType;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.lnson.util.JdbcSqlParameter;
import org.lnson.util.JdbcUtils;

import com.alibaba.fastjson.JSON;
import com.zxb.entity.T_USER;

public class MySqlServlet extends HttpServlet {

	private static final long serialVersionUID = 3204789361051965325L;

	public MySqlServlet() {
		super();
	}

	@Override
	public void init() throws ServletException {
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		insertEntity();

		queryEntity();

		queryEntityList();

		updateEntity();

		deleteEntity();
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	@Override
	public void destroy() {
	}

	/**
	 * 插入记录
	 */
	public void insertEntity() {
		// 插入语句
		String sql = "INSERT INTO T_USER(USER_CODE,USER_NAME,`PASSWORD`,GENDER,ADDRESS,AGE,BIRTHDAY,`STATUS`,CREATE_DATE,UPDATE_DATE)VALUES(?,?,?,?,?,?,?,?,?,?)";

		// 定义传入参数
		List<JdbcSqlParameter> parameters = new ArrayList<JdbcSqlParameter>();
		parameters.add(new JdbcSqlParameter(UUID.randomUUID().toString(), JDBCType.VARCHAR));// USER_CODE
		parameters.add(new JdbcSqlParameter("诸葛冷冷", JDBCType.VARCHAR));// USER_NAME
		parameters.add(new JdbcSqlParameter("zhugenuannuan", JDBCType.VARCHAR));// PASSWORD
		parameters.add(new JdbcSqlParameter(1, JDBCType.INTEGER));// GENDER
		parameters.add(new JdbcSqlParameter("上海市宝山区通河新村", JDBCType.VARCHAR));// ADDRESS
		parameters.add(new JdbcSqlParameter(25, JDBCType.INTEGER));// AGE
		parameters.add(new JdbcSqlParameter(new Date(), JDBCType.DATE));// BIRTHDAY
		parameters.add(new JdbcSqlParameter(1, JDBCType.INTEGER));// STATUS
		parameters.add(new JdbcSqlParameter(new Date(), JDBCType.TIMESTAMP));// CREATE_DATE
		parameters.add(new JdbcSqlParameter(new Date(), JDBCType.TIMESTAMP));// UPDATE_DATE

		// 添加数据到数据库
		JdbcUtils jdbc = JdbcUtils.getJdbc();
		jdbc.insert(sql, parameters);
		jdbc.dispose();
	}

	/**
	 * 查询单条记录
	 */
	public void queryEntity() {
		// 定义查询语句和参数列表
		String sql = "SELECT * FROM T_USER WHERE USER_CODE = ?";
		List<JdbcSqlParameter> parameters = new ArrayList<JdbcSqlParameter>();
		parameters.add(new JdbcSqlParameter("3ae728e1-6d02-466c-99bd-ea73829a6bf1", JDBCType.VARCHAR));

		// 执行T-SQL语句
		JdbcUtils jdbc = JdbcUtils.getJdbc();
		LinkedHashMap<String, Object> user = jdbc.queryForObject(sql, parameters);
		jdbc.dispose();

		if(user==null)
			return;
		
		// 处理结果集
		T_USER userItem = new T_USER();
		userItem.setUSER_ID(jdbc.cast(Integer.class, user.get("USER_ID")));
		userItem.setUSER_CODE(jdbc.cast(String.class, user.get("USER_CODE")));
		userItem.setUSER_NAME(jdbc.cast(String.class, user.get("USER_NAME")));
		userItem.setPASSWORD(jdbc.cast(String.class, user.get("PASSWORD")));
		userItem.setGENDER(jdbc.cast(Integer.class, user.get("GENDER")));
		userItem.setADDRESS(jdbc.cast(String.class, user.get("ADDRESS")));
		userItem.setAGE(jdbc.cast(Integer.class, user.get("AGE")));
		userItem.setBIRTHDAY(jdbc.cast(Date.class, user.get("BIRTHDAY")));
		userItem.setSTATUS(jdbc.cast(Integer.class, user.get("STATUS")));
		userItem.setCREATE_DATE(jdbc.cast(Date.class, user.get("CREATE_DATE")));
		userItem.setUPDATE_DATE(jdbc.cast(Date.class, user.get("UPDATE_DATE")));

		// 打印结果集
		System.out.println(JSON.toJSONString(userItem));
	}

	/**
	 * 查询多条记录
	 */
	public void queryEntityList() {
		// 定义查询语句和参数列表
		String sql = "SELECT * FROM T_USER";
		List<JdbcSqlParameter> parameters = new ArrayList<JdbcSqlParameter>();

		// 执行T-SQL语句
		JdbcUtils jdbc = JdbcUtils.getJdbc();
		List<LinkedHashMap<String, Object>> userList = jdbc.queryForList(sql, parameters);
		jdbc.dispose();

		// 处理结果集
		List<T_USER> users = new ArrayList<T_USER>();
		T_USER userItem = null;
		for (LinkedHashMap<String, Object> user : userList) {
			userItem = new T_USER();
			userItem.setUSER_ID(jdbc.cast(Integer.class, user.get("USER_ID")));
			userItem.setUSER_CODE(jdbc.cast(String.class, user.get("USER_CODE")));
			userItem.setUSER_NAME(jdbc.cast(String.class, user.get("USER_NAME")));
			userItem.setPASSWORD(jdbc.cast(String.class, user.get("PASSWORD")));
			userItem.setGENDER(jdbc.cast(Integer.class, user.get("GENDER")));
			userItem.setADDRESS(jdbc.cast(String.class, user.get("ADDRESS")));
			userItem.setAGE(jdbc.cast(Integer.class, user.get("AGE")));
			userItem.setBIRTHDAY(jdbc.cast(Date.class, user.get("BIRTHDAY")));
			userItem.setSTATUS(jdbc.cast(Integer.class, user.get("STATUS")));
			userItem.setCREATE_DATE(jdbc.cast(Date.class, user.get("CREATE_DATE")));
			userItem.setUPDATE_DATE(jdbc.cast(Date.class, user.get("UPDATE_DATE")));
			users.add(userItem);
		}
		// 打印结果集
		System.out.println(JSON.toJSONString(users));
	}

	/**
	 * 更新记录
	 */
	public void updateEntity() {
		String sql = "UPDATE T_USER SET `PASSWORD` = ?, UPDATE_DATE = ? WHERE USER_CODE = ?";
		List<JdbcSqlParameter> parameters = new ArrayList<JdbcSqlParameter>();
		parameters.add(new JdbcSqlParameter("123456", JDBCType.VARCHAR));
		parameters.add(new JdbcSqlParameter(new Date(), JDBCType.TIMESTAMP));
		parameters.add(new JdbcSqlParameter("3ae728e1-6d02-466c-99bd-ea73829a6bf1", JDBCType.VARCHAR));

		JdbcUtils jdbc = JdbcUtils.getJdbc();
		Integer effectRows = jdbc.update(sql, parameters);
		jdbc.dispose();
		if (effectRows > 0) {
			System.out.println("更新成功");
		}
	}

	/**
	 * 删除记录
	 */
	public void deleteEntity() {
		String sql = "DELETE FROM T_USER WHERE USER_CODE = ?";
		List<JdbcSqlParameter> parameters = new ArrayList<JdbcSqlParameter>();
		parameters.add(new JdbcSqlParameter("3ae728e1-6d02-466c-99bd-ea73829a6bf1", JDBCType.VARCHAR));

		JdbcUtils jdbc = JdbcUtils.getJdbc();
		Integer effectRows = jdbc.delete(sql, parameters);
		jdbc.dispose();
		if (effectRows > 0) {
			System.out.println("删除成功");
		}
	}
}
