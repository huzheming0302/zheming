package com.goods.application;



public class LoginItem {
	
	 
	private int id;
	private String phonenumber = "";
	private String password = "";
	private String newtoken = "";
	private String oldtoken = "";
	//private String device = "";
	
	public LoginItem()
	{
		id = 10;
		phonenumber = "13700000000";
		password = "000000";
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPhonenumber() {
		return phonenumber;
	}
	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getNewtoken() {
		//判断是否为第一次执行getToken
		
		return newtoken;
	}

	public void setNewtoken(String newtoken) {
		this.newtoken = newtoken;
	}
	public String getOldtoken() {
		//判断是否为第一次执行getToken
		
		return oldtoken;
	}

	public void setOldtoken(String oldtoken) {
		this.oldtoken = oldtoken;
	}
	/*public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}*/
	
}
