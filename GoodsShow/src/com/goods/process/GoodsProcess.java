package com.goods.process;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.poi.ss.formula.functions.T;
















import com.goods.application.GoodsConfig;
import com.goods.application.NotificationItem;
import com.goods.application.NotificationTable;
import com.goods.application.Pages;
import com.google.gson.Gson;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;





import com.mysql.jdbc.log.Log;

import exhi.net.database.DatabaseParam;
import exhi.net.database.NetTable;
import exhi.net.log.NetLog;
import exhi.net.netty.NetProcess;
import exhi.net.netty.WebUtil;
import exhi.net.utils.TransferException;
import exhi.net.utils.TransferUtils;
import freemarker.log.Logger;

public class GoodsProcess extends NetProcess {
	private static final String TAG="GoodsProcess";

	//private static final String "date" = null;

	private WebUtil mWebUtil = null;
	
	public GoodsProcess()
	{
		mWebUtil = new WebUtil(this);
	}
	
	
	@Override
	protected void onProcess(String address, String path, Map<String, String> request) {

		NetLog.debug(address, "=============================");
		NetLog.debug(address, "Enter onProcess - GoodsProcess()");
		NetLog.debug(address, "Full Path:" + path);
		NetLog.debug(address, "Work Path:" + this.getWorkPath());
		NetLog.debug(address, "Uri = " + this.getUri());
		
		
		File tempFile = new File(path);

		NetLog.debug(address, "Parent path = " + tempFile.getParent());
		mWebUtil.setTemplatePath(tempFile.getParent());
		Map<String, String> map =request;
		String data=map.get("data");
		
		
		//String str=new Map2Json().simpleMapToJsonStr(map);
		//System.out.println("str="+str);
		if (path.equals("\\v1\\add"))
		{	
			DatabaseParam param = GoodsConfig.instance().getDatabaseParam();
			NotificationTable table = new NotificationTable(param);
			NotificationItem item = new NotificationItem();
			Gson gson = new Gson();
			item = gson.fromJson(data, NotificationItem.class);
			Map<String, Object> mapper = null;
			try {
				mapper = TransferUtils.transferBean2Map(item);
			} catch (TransferException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//String sql = "insert into mySql values("+str+"DESC LIMIT 1)";
			
			if (mapper != null && mapper.containsKey("id")){
				mapper.remove("id");
			}
				
			int ret = table.insert(mapper);		// 插入
			NetLog.debug("123", ret+"");
			//String _data=map.get("data");
			
			//String str1 = String.format("{\"flag\":{\"errorType\":\"ok\"},\"data\":\"%s\"}", _data);
			this.print(data);
		}
		
		
		if (path.equals("\\v1\\query"))
		{	
			DatabaseParam param = GoodsConfig.instance().getDatabaseParam();
			NotificationTable table = new NotificationTable(param);
			JSONObject  jasonObject = JSONObject.fromObject(data);
			Map map2 = (Map<String, String>)jasonObject;
			//NotificationItem item = new NotificationItem();
			//Gson gson = new Gson();
			//item = gson.fromJson(data, NotificationItem.class);
			//String date = item.getDate();
			
			//JSONObject jasonObject = JSONObject.fromObject(date);
			//Map map0 = (Map)jasonObject;
			//Map("date",String) map0 
			
			//Map<String, String> map2;
			//map2.put("date", date);
			List<NotificationItem> notifList = new ArrayList<NotificationItem>();
			notifList=table.queryList(0,-1,map2);
			JSONArray jsonArray = JSONArray.fromObject(notifList); 
			//String json = jsonArray.toString();
			ServerFlag flag = new ServerFlag();
			String json = new MakeJsonReturn().MakeJsonReturn(flag,jsonArray);
			/*if(json.indexOf("[") != -1){
				json = json.replace("[", "");  
			}
			if(json.indexOf("]") != -1){ 
				json = json.replace("]", ""); 
			}*/
			
			//String str = String.format("{\"flag\":{\"errorType\":\"ok\"},\"data\":\"%s\"}", _data1);
			//String str = String.format("{\"flag\":{\"errorType\":\"ok\"},\"data\":\"%s\"}",json);
			this.print(json);
		}

		NetLog.debug(address, "Leave onProcess - GoodsProcess()");
		NetLog.debug(address, "=============================");
	}


}
