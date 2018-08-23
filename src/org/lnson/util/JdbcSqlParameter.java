package org.lnson.util;

import java.sql.JDBCType;

/**
 * 参数传递类型
 */
public class JdbcSqlParameter {
	
	private final static Integer DEFAULT_SCALEORLENGTH = 0;

	public JdbcSqlParameter(Object data, JDBCType targetSqlType) {
		this(data, targetSqlType, DEFAULT_SCALEORLENGTH);
	}

	public JdbcSqlParameter(Object data, JDBCType targetSqlType, Integer scaleOrLength) {
		this.value = data;
		this.targetSqlType = targetSqlType;
		this.scaleOrLength = scaleOrLength;
	}

	private Object value;

	public Object getValue() {
		return value;
	}

	private JDBCType targetSqlType;

	public JDBCType getTargetSqlType() {
		return targetSqlType;
	}

	private Integer scaleOrLength;

	public Integer getScaleOrLength() {
		return scaleOrLength;
	}
}