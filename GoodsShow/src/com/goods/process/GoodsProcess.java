package com.goods.process;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.goods.application.GoodsConfig;
import com.goods.application.LoginItem;
import com.goods.application.LoginResult;
import com.goods.application.LoginTable;
import com.goods.application.NotificationItem;
import com.goods.application.NotificationTable;
import com.google.gson.Gson;

import exhi.net.database.DatabaseParam;
import exhi.net.log.NetLog;
import exhi.net.netty.NetProcess;
import exhi.net.netty.WebUtil;
import exhi.net.utils.TransferException;
import exhi.net.utils.TransferUtils;


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
		
		if (path.equals("\\v1\\newtoken")){
			
			DatabaseParam param = GoodsConfig.instance().getDatabaseParam();
			LoginTable table = new LoginTable(param);
			LoginItem item = new LoginItem();
			NotificationTable table2 = new NotificationTable(param);
			Gson gson = new Gson();
			item = gson.fromJson(data, LoginItem.class);
			Map<String, Object> mapper = null;
			String oldtoken = "";
			String newtoken = "";
			String password = "";
			try {
				mapper = TransferUtils.transferBean2Map(item);
			} catch (TransferException e) {
				e.printStackTrace();
			}
			if (mapper != null && mapper.containsKey("oldtoken") && mapper.containsKey("newtoken") && mapper.containsKey("password")){
				
				oldtoken = mapper.get("oldtoken").toString();
				newtoken = mapper.get("newtoken").toString();
				password = mapper.get("password").toString();
			}
			
			String sql = String.format("UPDATE %s SET password='%s', oldtoken='%s' ,newtoken='%s'WHERE newtoken='%s'",
					table.getTableName(), password, oldtoken,newtoken, oldtoken);
			String sql2 = String.format("UPDATE %s SET token='%s' WHERE token='%s'",
					table2.getTableName(), newtoken, oldtoken);
			table.update(sql);
			table2.update(sql2);
			
			LoginResult loginresult = new LoginResult();
			String succeed = "succeed";
			loginresult.setPassword(succeed);
			loginresult.setToken(newtoken);
			Map<String, Object> data1 = new HashMap<String, Object>();
			String json = "";
			((Map) data1).put("data1",loginresult);
			ServerFlag flag = new ServerFlag();
			json = new MakeJsonReturn().MakeJsonReturn(flag,data1);
			this.print(json);
		}
		
		if (path.equals("\\v1\\verify"))
		{
			DatabaseParam param = GoodsConfig.instance().getDatabaseParam();
			LoginTable table = new LoginTable(param);
			LoginItem item = new LoginItem();
			Gson gson = new Gson();
			item = gson.fromJson(data, LoginItem.class);
			Map<String, Object> mapper = null;
			String phonenumber = "";
			try {
				mapper = TransferUtils.transferBean2Map(item);
			} catch (TransferException e) {
				e.printStackTrace();
			}
			
			if (mapper != null && mapper.containsKey("phonenumber")){
				phonenumber = mapper.get("phonenumber").toString();
			}
			List<LoginItem> loginList = null;
			loginList=table.queryList(0,-1,phonenumber);
			String json = "";
			Map<String, Object> data1 = new HashMap<String, Object>();
			if (!loginList.isEmpty() && loginList.size() == 1){
				LoginItem loginitem = loginList.get(0);
				String oldpassword = "";
				String oldtoken = "";
				String newtoken = "";
				newtoken = table.makeRandCode();
				oldpassword = loginitem.getPassword();
				oldtoken = loginitem.getNewtoken();
				LoginResult loginresult = new LoginResult();
				loginresult.setPassword(oldpassword);
				loginresult.setToken(oldtoken);
				loginresult.setNewtoken(newtoken);
				((Map) data1).put("data1",loginresult);
			}else {
				String noresult = "noresult";
				LoginItem loginitem = new LoginItem();
				loginitem.setPassword(noresult);
				loginitem.setNewtoken(noresult);
				((Map) data1).put("data1",loginitem);
			}
			ServerFlag flag = new ServerFlag();
			json = new MakeJsonReturn().MakeJsonReturn(flag,data1);
			this.print(json);
		}
		
		if (path.equals("\\v1\\register"))
		{
			DatabaseParam param = GoodsConfig.instance().getDatabaseParam();
			LoginTable table = new LoginTable(param);
			LoginItem item = new LoginItem();
			Gson gson = new Gson();
			item = gson.fromJson(data, LoginItem.class);
			Map<String, Object> mapper = null;
			String phonenumber = "";
			String password = "";
			try {
				mapper = TransferUtils.transferBean2Map(item);
			} catch (TransferException e) {
				e.printStackTrace();
			}
			
			if (mapper != null && mapper.containsKey("phonenumber")&& mapper.containsKey("password")){
				phonenumber = mapper.get("phonenumber").toString();
				password = mapper.get("password").toString();
			}
			List<LoginItem> loginList = null;
			loginList=table.queryList(0,-1,phonenumber);
			LoginResult loginresult = new LoginResult();
			String json = "";
			if (!loginList.isEmpty() && loginList.size() == 1){
				String result = "ALREADY EXISTS";
				loginresult.setPassword(result);
			}else {
				item = table.insertNewPhonenumber(phonenumber, password);
				try {
					mapper = TransferUtils.transferBean2Map(item);
				} catch (TransferException e) {
					e.printStackTrace();
				}
				if (mapper != null && mapper.containsKey("id")){
					mapper.remove("id");
				}
				int ret = table.insert(mapper);		// 插入
				NetLog.debug("123", ret+"");
				String succeed = "succeed";
				loginresult.setPassword(succeed);
			}
			Map<String, Object> data1 = new HashMap<String, Object>();
			((Map) data1).put("data1",loginresult);
			ServerFlag flag = new ServerFlag();
			json = new MakeJsonReturn().MakeJsonReturn(flag,data1);
			this.print(json);
				
		}
			
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
			
			if (mapper != null && mapper.containsKey("id")){
				mapper.remove("id");
			}
			table.insert(mapper);		
			LoginResult loginresult = new LoginResult();
			String succeed = "succeed";
			loginresult.setPassword(succeed);
			Map<String, Object> data1 = new HashMap<String, Object>();
			String json = "";
			((Map) data1).put("data1",loginresult);
			ServerFlag flag = new ServerFlag();
			json = new MakeJsonReturn().MakeJsonReturn(flag,data1);
			this.print(json);
			
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
			Map<String, Object> data1 = new HashMap<String, Object>();
			((Map) data1).put("data1",notifList);
			ServerFlag flag = new ServerFlag();
			json = new MakeJsonReturn().MakeJsonReturn(flag,data1);
			this.print(json);
		}
		
		if (path.equals("\\v1\\update"))
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
			
			int id = -1;
			String date = "";
			String money = "";
			String event = "";
			String remark = "";
			String token = "";
			String olddate = "";
			
			if (mapper != null && mapper.containsKey("id")){
				id = (int) mapper.get("id");
				date = mapper.get("date").toString();
				money = mapper.get("money").toString();
				event = mapper.get("event").toString();
				remark = mapper.get("remark").toString();
				token = mapper.get("token").toString();
				olddate = mapper.get("olddate").toString();
			}
			String sql = String.format("UPDATE %s SET date='%s',olddate='%s', money='%s' ,event='%s', remark='%s'WHERE token='%s' AND id='%s'",
					table.getTableName(), date, olddate,money,event, remark,token,id);	
			table.update(sql);
			
			Map<String ,String> mapper2 = new HashMap<String ,String>();
			mapper2.put("date", olddate);
			mapper2.put("token", token);
			List<NotificationItem> notifList = null;
			notifList=table.queryList(0,-1,mapper2);
			String json = "";
			
			Map<String, Object> data1 = new HashMap<String, Object>();
			((Map) data1).put("data1",notifList);
			ServerFlag flag = new ServerFlag();
			json = new MakeJsonReturn().MakeJsonReturn(flag,data1);
			this.print(json);
		}
		
		if (path.equals("\\v1\\delete"))
		{
			DatabaseParam param = GoodsConfig.instance().getDatabaseParam();
			NotificationTable table = new NotificationTable(param);
			NotificationItem item = new NotificationItem();
			Gson gson = new Gson();
			item = gson.fromJson(data, NotificationItem.class);
			Map<String, Object> mapper = null;
			NetLog.debug(address, "1");
			
			try {
				mapper = TransferUtils.transferBean2Map(item);
			} catch (TransferException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			int id = -1;
			String date = "";
			String token = "";
			if (mapper != null && mapper.containsKey("id")){
				id = (int) mapper.get("id");
				date = mapper.get("date").toString();
				token = mapper.get("token").toString();
				NetLog.debug(address, "2");
			}
			String sql = String.format("DELETE FROM %s where id=%d AND token=%s", table.getTableName(), id,token);	
			table.update(sql);
			NetLog.debug(address, "3");
			
			Map<String ,String> mapper2 = new HashMap<String ,String>();
			mapper2.put("date", date);
			mapper2.put("token", token);
			List<NotificationItem> notifList = null;
			notifList = table.queryList(0,-1,mapper2);
			String json = "";
			
			Map<String, Object> data1 = new HashMap<String, Object>();
			((Map) data1).put("data1",notifList);
			ServerFlag flag = new ServerFlag();
			json = new MakeJsonReturn().MakeJsonReturn(flag,data1);
			this.print(json);
			
			/*Map<String ,Object> mapper2 = new HashMap<String ,Object>();
			LoginResult result = new LoginResult();
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			mapper2.put("delete_success", result);
			list.add(mapper2);
			NetLog.debug(address, "4");
			Map<String, Object> data1 = new HashMap<String, Object>();
			((Map) data1).put("data1",list);
			ServerFlag flag = new ServerFlag();
			String json = "";
			json = new MakeJsonReturn().MakeJsonReturn(flag,data1);
			this.print(json);*/
			
		}
		
			
		NetLog.debug(address, "Leave onProcess - GoodsProcess()");
		NetLog.debug(address, "=============================");
	}


}
