package com.goods.application;

public class LoginResult {
	
	private String password = "";
	private String token = "";
	private String newtoken = "";
	
	public LoginResult()
	{
		password = "不存在该用户";
		token = "初始值";
		newtoken = "初始值";
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	public String getNewtoken() {
		return newtoken;
	}

	public void setNewtoken(String newtoken) {
		this.newtoken = newtoken;
	}
}
