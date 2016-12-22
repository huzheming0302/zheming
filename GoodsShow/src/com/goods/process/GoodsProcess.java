package com.goods.process;

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
import exhi.net.utils.TransferException;
import exhi.net.utils.TransferUtils;


public class GoodsProcess extends NetProcess {
	
	@Override
	protected void onProcess(String address, String path, Map<String, String> request) {

		NetLog.debug(address, "=============================");
		NetLog.debug(address, "Enter onProcess - GoodsProcess()");
		NetLog.debug(address, "Full Path:" + path);
		NetLog.debug(address, "Work Path:" + this.getWorkPath());
		NetLog.debug(address, "Uri = " + this.getUri());
		
		Map<String, String> map =request;
		String data=map.get("data");
		
		if (path.equals("\\v1\\checktoken")){
			DatabaseParam param = GoodsConfig.instance().getDatabaseParam();
			LoginTable table = new LoginTable(param);
			LoginItem item = new LoginItem();
			Gson gson = new Gson();
			item = gson.fromJson(data, LoginItem.class);
			Map<String, Object> mapper = null;
			String newtoken = "";
			try {
				mapper = TransferUtils.transferBean2Map(item);
			} catch (TransferException e) {
				e.printStackTrace();
			}
			if (mapper != null && mapper.containsKey("newtoken")) {
				newtoken = mapper.get("newtoken").toString();
			}
			List<LoginItem> loginList = null;
			loginList=table.queryListbytoken(0,-1,newtoken);
			LoginResult loginresult = new LoginResult();
			String json = "";
			String result = "";
			if (!loginList.isEmpty() && loginList.size() == 1){
				result = "ALREADY EXISTS";
				loginresult.setPassword(result);
				loginresult.setToken(newtoken);
			}else {
				result = "NOT EXISTS";
				loginresult.setPassword(result);
				/*item = table.insertNewPhonenumber(phonenumber, password);
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
				loginresult.setPassword(succeed);*/
			}
			Map<String, Object> data1 = new HashMap<String, Object>();
			((Map) data1).put("data1",loginresult);
			ServerFlag flag = new ServerFlag();
			json = new MakeJsonReturn().MakeJsonReturn(flag,data1);
			this.print(json);
			
		}
		
		if (path.equals("\\v1\\getInformation")){
			
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
			/*List<NotificationItem> notifList = null;
			notifList = table2.queryList(0,-1,newtoken);
			NotificationItem lastcost = new NotificationItem();
			lastcost = notifList.get(0);
			if (notifList.size() == 0){
				lastcost.setId(-1);
			}
			Map<String, Object> data1 = new HashMap<String, Object>();
			String json = "";
			((Map) data1).put("data1",lastcost);
			ServerFlag flag = new ServerFlag();
			json = new MakeJsonReturn().MakeJsonReturn(flag,data1);
			this.print(json);*/
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
		if (path.equals("\\v1\\updateToken")){
			
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
			List<NotificationItem> notifList = null;
			notifList = table2.queryList(0,-1,newtoken);
			NotificationItem lastcost = new NotificationItem();
			if (notifList.size() == 0){
				lastcost.setId(-1);
			}else {
				lastcost = notifList.get(0);
			}
			Map<String, Object> data1 = new HashMap<String, Object>();
			String json = "";
			((Map) data1).put("data1",lastcost);
			ServerFlag flag = new ServerFlag();
			json = new MakeJsonReturn().MakeJsonReturn(flag,data1);
			this.print(json);
			/*LoginResult loginresult = new LoginResult();
			String succeed = "succeed";
			loginresult.setPassword(succeed);
			loginresult.setToken(newtoken);
			Map<String, Object> data1 = new HashMap<String, Object>();
			String json = "";
			((Map) data1).put("data1",loginresult);
			ServerFlag flag = new ServerFlag();
			json = new MakeJsonReturn().MakeJsonReturn(flag,data1);
			this.print(json);*/
		}
		
		if (path.equals("\\v1\\getLastCost")){
			
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
			String token = "";
			token = mapper.get("token").toString();
			List<NotificationItem> notifList = null;
			notifList = table.queryList(0,-1,token);
			NotificationItem lastcost = new NotificationItem();
			lastcost = notifList.get(0);
			if (notifList.size() == 0){
				lastcost.setId(-1);
			}
			Map<String, Object> data1 = new HashMap<String, Object>();
			String json = "";
			((Map) data1).put("data1",lastcost);
			ServerFlag flag = new ServerFlag();
			json = new MakeJsonReturn().MakeJsonReturn(flag,data1);
			this.print(json);
			/*LoginResult loginresult = new LoginResult();
			String succeed = "succeed";
			loginresult.setPassword(succeed);
			loginresult.setToken(newtoken);
			Map<String, Object> data1 = new HashMap<String, Object>();
			String json = "";
			((Map) data1).put("data1",loginresult);
			ServerFlag flag = new ServerFlag();
			json = new MakeJsonReturn().MakeJsonReturn(flag,data1);
			this.print(json);*/
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
			NotificationItem lastcost = new NotificationItem();
			String succeed = "succeed";
			lastcost.setDate(succeed);
			Map<String, Object> data1 = new HashMap<String, Object>();
			String json = "";
			((Map) data1).put("data1",lastcost);
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
			
			LoginResult result = new LoginResult();
			String password = "update_success";
			result.setPassword(password);
			List<Object> list = new ArrayList<Object>();
			list.add(result);
			NetLog.debug(address, "4");
			Map<String, Object> data1 = new HashMap<String, Object>();
			((Map) data1).put("data1",list);
			ServerFlag flag = new ServerFlag();
			String json = "";
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
			String token = "";
			if (mapper != null && mapper.containsKey("id") && mapper.containsKey("token")){
				id = (int) mapper.get("id");
				token = mapper.get("token").toString();
				NetLog.debug(address, "2");
			}
			String sql = String.format("DELETE FROM %s where id=%d AND token=%s", table.getTableName(), id,token);	
			table.update(sql);
			NetLog.debug(address, "3");
			
			LoginResult result = new LoginResult();
			String password = "delete_success";
			result.setPassword(password);
			List<Object> list = new ArrayList<Object>();
			list.add(result);
			NetLog.debug(address, "4");
			Map<String, Object> data1 = new HashMap<String, Object>();
			((Map) data1).put("data1",list);
			ServerFlag flag = new ServerFlag();
			String json = "";
			json = new MakeJsonReturn().MakeJsonReturn(flag,data1);
			this.print(json);
		}
		
			
		NetLog.debug(address, "Leave onProcess - GoodsProcess()");
		NetLog.debug(address, "=============================");
	}


}
