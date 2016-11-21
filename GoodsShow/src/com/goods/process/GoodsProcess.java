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
			/*String date=map.get("date");
			String event=map.get("event");
			String money=map.get("money");
			String remark=map.get("remark");
			
			item.setDate(date);
			item.setEvent(event);
			item.setMoney(money);
			item.setRemark(remark);*/
			//Data data=new JsonToObj(str).JSON2Object();
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
				
			int ret = table.insert(mapper);
			NetLog.debug("123", ret+"");
			String _data=map.get("data");
			
			String str1 = String.format("{\"flag\":{\"errorType\":\"ok\"},\"data\":\"%s\"}", _data);
			this.print(str1);
		}
		
		
		if (path.equals("\\v1\\query"))
		{	
			
			
			String _data=map.get("data");
			String sql = "select *from mySql where date = " ;
			
			
			
			//list=new QueryReturn().getForList(clazz, sql, args);
			String _data1=map.get("data");
			String str = String.format("{\"flag\":{\"errorType\":\"ok\"},\"data\":\"%s\"}", _data1);
			this.print(str);
		}

		NetLog.debug(address, "Leave onProcess - GoodsProcess()");
		NetLog.debug(address, "=============================");
	}


}
