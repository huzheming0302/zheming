package com.goods.application;

public class LoginResult {
	
	private String password = "";
	
	public LoginResult()
	{
		password = "不存在该用户";
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

}
