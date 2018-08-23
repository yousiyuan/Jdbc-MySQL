package com.zxb.controller;

import java.io.IOException;
import java.sql.JDBCType;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.lnson.util.JdbcSqlParameter;
import org.lnson.util.JdbcUtils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class MySqlPagerServlet extends HttpServlet {

	private static final long serialVersionUID = 774885085010648200L;

	public MySqlPagerServlet() {
	}

	@Override
	public void init() throws ServletException {
	}

	/**
	 * MySQL分页
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 定义默认显示第1页数据，设置每页显示20条数据
		int pageIndex = 1, pageSize = 20;

		String i = req.getParameter("i");
		String s = req.getParameter("s");
		if (i != null && !"".equalsIgnoreCase(i.trim())) {
			pageIndex = Integer.valueOf(i);
		}
		if (s != null && !"".equalsIgnoreCase(s.trim())) {
			pageSize = Integer.valueOf(s);
		}
		int startRowNo = (pageIndex - 1) * pageSize;

		// 定义查询语句和参数列表
		String sql = "SELECT ID,OUTSYS_ORDER_NO,OUTSYS_BILL_CODE,ORDER_SOURCE,MSG_CONTENT,MSG_TYPE,REMARK,IS_SYNC_SUCCESS,CREATE_BY,CREATE_TIME,C1,C2,C3 FROM T_EBILL WHERE ORDER_SOURCE = ? ORDER BY CREATE_TIME DESC LIMIT ?, ?";
		List<JdbcSqlParameter> parameters = new ArrayList<JdbcSqlParameter>();
		parameters.add(new JdbcSqlParameter("TAOBAO_ONLINE", JDBCType.VARCHAR));
		parameters.add(new JdbcSqlParameter(startRowNo, JDBCType.INTEGER));
		parameters.add(new JdbcSqlParameter(pageSize, JDBCType.INTEGER));

		// 查询数据
		JdbcUtils jdbc = JdbcUtils.getJdbc();
		List<LinkedHashMap<String, Object>> ebillList = jdbc.queryForList(sql, parameters);
		jdbc.dispose();

		// 处理结果集
		JSONObject.DEFFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";//序列化
		String result = JSONObject.toJSONString(ebillList,
				SerializerFeature.WRITE_MAP_NULL_FEATURES,
				SerializerFeature.DisableCircularReferenceDetect,
				SerializerFeature.WriteDateUseDateFormat,//设置日期格式
				SerializerFeature.QuoteFieldNames);//键名使用双引号
		// 显示结果集
		System.out.println(result);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	}

	@Override
	public void destroy() {
	}

}
