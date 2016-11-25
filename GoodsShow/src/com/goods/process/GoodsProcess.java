package com.goods.process;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
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
			
			String str1 = String.format("{\"flag\":{\"errorType\":\"ok\"},\"data\":null}");
										
			this.print(str1);
		}
		
		
		if (path.equals("\\v1\\query"))
		{	
			DatabaseParam param = GoodsConfig.instance().getDatabaseParam();
			NotificationTable table = new NotificationTable(param);
			JSONObject  jasonObject = JSONObject.fromObject(data);
			Map map2 = (Map<String, String>)jasonObject;
			List<NotificationItem> notifList = null;
			notifList=table.queryList(0,-1,map2);
			String json = "";
			if (notifList != null && notifList.size()>0)
			{
				Map<String, Object> data1 = new HashMap<String, Object>();
				((Map) data1).put("data1",notifList);
				ServerFlag flag = new ServerFlag();
				json = new MakeJsonReturn().MakeJsonReturn(flag,data1);
				
			}
			else
			{
			 // log error
			}
			//JSONArray jsonArray = JSONArray.fromObject(notifList); 
			//String json = jsonArray.toString();
			//ServerFlag flag = new ServerFlag();
			//String json = new MakeJsonReturn().MakeJsonReturn(flag,jsonArray);
			JSONObject obj = new JSONObject().fromObject(json);
			JSONObject jsonData = null;
			//JSONObject jsonResponse = null;
			//jsonResponse = new JSONObject(json);
			jsonData = obj.getJSONObject("data");
			NetLog.debug("123",jsonData.toString());
			NetLog.debug("12345",json);
			//String str = String.format("{\"flag\":{\"errorType\":\"ok\"},\"data\":\"%s\"}",json);
			this.print(json);
		}

		NetLog.debug(address, "Leave onProcess - GoodsProcess()");
		NetLog.debug(address, "=============================");
	}


}
