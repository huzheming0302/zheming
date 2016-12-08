package com.goods.application;

import java.sql.Date;

public class NotificationItem {

	private int id;
	private String date;
	private String event = "111";
	private String money = "1";
	private String remark = "нч";
	private String token = "";
	//private String phonenumber = "";
	public NotificationItem()
	{
		id = 10;
		date = "2016-11-21 12:00:00";
		money = "-1";
		event = "111";
		remark = "нч";
		token = "нч";
		//phonenumber = "12345678901";
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	/*public String getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}*/
}
