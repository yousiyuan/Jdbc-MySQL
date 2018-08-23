package com.zxb.entity;

import java.util.Date;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

public class T_USER {
	/*
	 * 用户ID
	 */
	@JSONField(name = "UserId")
	private Integer USER_ID;
	/*
	 * 用户编号
	 */
	@JSONField(name = "UserCode")
	private String USER_CODE;
	/*
	 * 用户名
	 */
	@JSONField(name = "UserName")
	private String USER_NAME;
	/*
	 * 密码
	 */
	@JSONField(name = "Password")
	private String PASSWORD;
	/*
	 * 性别
	 */
	@JSONField(name = "Gender")
	private Integer GENDER;
	/*
	 * 地址
	 */
	@JSONField(name = "Address")
	private String ADDRESS;
	/*
	 * 年龄
	 */
	@JSONField(name = "Age")
	private Integer AGE;
	/*
	 * 生日
	 */
	@JSONField(name = "Birthday", format = "yyyy-MM-dd")
	private Date BIRTHDAY;
	/*
	 * 状态
	 */
	@JSONField(name = "Status")
	private Integer STATUS;
	/*
	 * 创建日期
	 */
	@JSONField(name = "CreateDate", format = "yyyy-MM-dd HH:mm:ss", serialize = true, deserialize = false)
	private Date CREATE_DATE;
	/*
	 * 更新日期
	 */
	@JSONField(name = "UpdateDate", format = "yyyy-MM-dd HH:mm:ss", serialize = true, deserialize = false)
	private Date UPDATE_DATE;

	public Integer getUSER_ID() {
		return USER_ID;
	}

	public void setUSER_ID(Integer uSER_ID) {
		USER_ID = uSER_ID;
	}

	public String getUSER_CODE() {
		return USER_CODE;
	}

	public void setUSER_CODE(String uSER_CODE) {
		USER_CODE = uSER_CODE;
	}

	public String getUSER_NAME() {
		return USER_NAME;
	}

	public void setUSER_NAME(String uSER_NAME) {
		USER_NAME = uSER_NAME;
	}

	public String getPASSWORD() {
		return PASSWORD;
	}

	public void setPASSWORD(String pASSWORD) {
		PASSWORD = pASSWORD;
	}

	public Integer getGENDER() {
		return GENDER;
	}

	public void setGENDER(Integer gENDER) {
		GENDER = gENDER;
	}

	public String getADDRESS() {
		return ADDRESS;
	}

	public void setADDRESS(String aDDRESS) {
		ADDRESS = aDDRESS;
	}

	public Integer getAGE() {
		return AGE;
	}

	public void setAGE(Integer aGE) {
		AGE = aGE;
	}

	public Date getBIRTHDAY() {
		return BIRTHDAY;
	}

	public void setBIRTHDAY(Date bIRTHDAY) {
		BIRTHDAY = bIRTHDAY;
	}

	public Integer getSTATUS() {
		return STATUS;
	}

	public void setSTATUS(Integer sTATUS) {
		STATUS = sTATUS;
	}

	public Date getCREATE_DATE() {
		return CREATE_DATE;
	}

	public void setCREATE_DATE(Date cREATE_DATE) {
		CREATE_DATE = cREATE_DATE;
	}

	public Date getUPDATE_DATE() {
		return UPDATE_DATE;
	}

	public void setUPDATE_DATE(Date uPDATE_DATE) {
		UPDATE_DATE = uPDATE_DATE;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
}
