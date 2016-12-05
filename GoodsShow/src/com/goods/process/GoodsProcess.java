package com.goods.process;

import java.io.File;
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
		//String str=new Map2Json().simpleMapToJsonStr(map);
		//System.out.println("str="+str);
		
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
			LoginResult loginresult = new LoginResult();
			String noresult = "noresult";
			String json = "";
			
			if (!loginList.isEmpty() && loginList.size() == 1){
				LoginItem item2 = new LoginItem();
				item2 = loginList.get(0);
				String result = item2.getPassword();
				Map<String, Object> data1 = new HashMap<String, Object>();
				((Map) data1).put("data1",item2);
				ServerFlag flag = new ServerFlag();
				json = new MakeJsonReturn().MakeJsonReturn(flag,data1);
				this.print(json);
			}
			
			if (loginList.isEmpty()) {
				Map<String, Object> data1 = new HashMap<String, Object>();
				loginresult.setResult(noresult);
				((Map) data1).put("data1",loginresult);
				ServerFlag flag = new ServerFlag();
				json = new MakeJsonReturn().MakeJsonReturn(flag,data1);
				this.print(json);
				
			}
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
			
			try {
				mapper = TransferUtils.transferBean2Map(item);
			} catch (TransferException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (mapper != null && mapper.containsKey("phonenumber")){
				phonenumber = mapper.get("phonenumber").toString();
			}
			
			List<LoginItem> loginList = null;
			loginList=table.queryList(0,-1,phonenumber);
			LoginResult loginresult = new LoginResult();
			String json = "";
			
			if (!loginList.isEmpty() && loginList.size() == 1){
				String result = "ALREADY EXISTS";
				loginresult.setResult(result);
			}else {
				int ret = table.insert(mapper);		// 插入
				NetLog.debug("123", ret+"");
				String succeed = "succeed";
				loginresult.setResult(succeed);
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
				
			int ret = table.insert(mapper);		
			NetLog.debug("123", ret+"");
			LoginResult loginresult = new LoginResult();
			String succeed = "succeed";
			loginresult.setResult(succeed);
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
			JSONObject obj = new JSONObject().fromObject(json);
			JSONObject jsonData = null;
			jsonData = obj.getJSONObject("data");
			NetLog.debug("123",jsonData.toString());
			NetLog.debug("12345",json);
			this.print(json);
		}
		
			
		NetLog.debug(address, "Leave onProcess - GoodsProcess()");
		NetLog.debug(address, "=============================");
	}


}
