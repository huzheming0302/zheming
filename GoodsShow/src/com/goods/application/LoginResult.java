package com.goods.application;

public class LoginResult {
	
	private String result = "";
	public LoginResult()
	{
		result = "不存在该用户";
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}

}
