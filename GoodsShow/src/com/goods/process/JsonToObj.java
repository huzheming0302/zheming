package com.goods.process;

import net.sf.json.JSONObject;


import com.goods.application.NotificationItem;



public class JsonToObj {
	private String jsonStr;
	public JsonToObj(){}
	public JsonToObj(String str){
		this.jsonStr = str;
	}
	
	public NotificationItem JSON2Object(){ 
		if(jsonStr.indexOf("[") != -1){
			jsonStr = jsonStr.replace("[", "");  
		}
		if(jsonStr.indexOf("]") != -1){ 
			jsonStr = jsonStr.replace("]", ""); 
		}
	
		JSONObject obj = new JSONObject().fromObject(jsonStr);
		NotificationItem data = (NotificationItem)JSONObject.toBean(obj,NotificationItem.class);
		return data;
		 
		
	}
}
