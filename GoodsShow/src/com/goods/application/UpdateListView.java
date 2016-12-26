package com.goods.application;

public class UpdateListView {
	
	private int id;
	private String date = "";
	private String event = "";
	private String money = "";
	private String remark = "";
	private String password = "";
	private String olddate = "";
	
	
	public UpdateListView()
	{
		id = 10;
		date = "0000-00-00 00:00:00";
		money = "-1";
		event = "0";
		remark = "Œﬁ";
		password = "≥ı º÷µ";
	}
	
	public String getOlddate() {
		return olddate;
	}
	public void setOlddate(String olddate) {
		this.olddate = olddate;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getEvent() {
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
