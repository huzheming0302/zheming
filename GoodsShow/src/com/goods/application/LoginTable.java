package com.goods.application;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import exhi.net.database.DatabaseParam;
import exhi.net.database.NetDatabase;
import exhi.net.database.NetTable;
import exhi.net.log.NetLog;
import exhi.net.utils.DbUtils;
import exhi.net.utils.TransferException;
import exhi.net.utils.TransferUtils;

public class LoginTable extends NetTable {
	
	static public final String FLAG = "goods";
	static public final String FIELDS_ACCOUNT	= "phonenumber";
	private static final int mVersion = 1;
	private static final String mTableName = "information";
	
	public LoginTable(DatabaseParam param) {
		super(param, mTableName, 1, mVersion);
	}
	
	
	@Override
	public void onCreateTable(NetDatabase db) {
		
		NetLog.debug("Expert Table Name: ", this.getTableName());
		
		String mySql = "CREATE TABLE IF NOT EXISTS " + this.getTableName() +" (";
		
		mySql += "id INT NOT NULL AUTO_INCREMENT,";
		mySql += "PRIMARY KEY (id),";
		mySql += "device VARCHAR(64),";
		//mySql += "id int not null primary key auto_increment,";
		mySql += "phonenumber VARCHAR(11) not null,";
		mySql += "token VARCHAR(64) NOT NULL DEFAULT '',";
		mySql += "password VARCHAR(100) default '') ENGINE=InnoDB DEFAULT CHARSET=utf8";
		
		db.createTable(mySql);
	}
	/**
	 * 
	 * @param account
	 * @param password
	 * @param type
	 * @return
	 */
	/*public AccountData insertNewAccount(String phonenumber, String password)
	{
		return this.insertNewAccount(phonenumber, password, null);
	}*/
	
	public LoginItem insertNewPhonenumber(String phonenumber, String password)
	{
		if (phonenumber.isEmpty() || password.isEmpty())
		{
			return null;
		}else {
			LoginItem loginitem = new LoginItem();
			String token = makeRandCode();
			loginitem.setPassword(makeMd5(password+token));
			loginitem.setPhonenumber(phonenumber);
			loginitem.setToken(token);
			
			
			return loginitem;
		}
		
		/*if (lang != null)
		{
			loginitem.setLanguage(lang);
		}

		Map<String, Object> map = MyUtils.transferBean2Map(data);
		map.remove("id");
		map.remove("manager");
		map.remove("signdate");

		if (this.insert(map, "id") == -1)
		{
			data = null;
		}
		
		return data;
		return null;*/
	}
	
	private String makeRandCode()
	{
		return String.format("%d", (new Random()).nextInt(1000000));
	}
	
	private String makeMd5(String string) {
		byte[] data = (FLAG+string).getBytes();
		MessageDigest md = null;
		boolean result = false;
		try {
			md = MessageDigest.getInstance("MD5");
			md.update(data, 0, data.length);
			data = md.digest();
			result = true;
		} catch (NoSuchAlgorithmException e) {
			
		}

		StringBuilder sb = new StringBuilder();
		if (result == true)
		{
			for (byte b:data)
			{
				sb.append(String.format("%02X", b));
			}
		}

		return sb.toString();
	}
	
	public boolean checkPhonenumber(String phonenumber, String password)
	{
		LoginItem loginitem = queryPhonenumber(phonenumber);
		
		if (loginitem != null)
		{
			password = makeMd5(password + loginitem.getToken());
			return (phonenumber.equals(loginitem.getPhonenumber()) && password.equals(loginitem.getPassword()));
		}
		else
		{
			return false;
		}
	}
	
	public boolean checkToken(String phonenumber, String token)
	{
		if (this.getCount() == 0)
		{
			return false;
		}
		
		LoginItem loginitem = queryPhonenumber(phonenumber);
		
		if (loginitem == null)
		{
			return false;
		}
		
		return token.equals(loginitem.getToken());
	}
	
	public boolean updateRandCode(String phonenumber, String password)
	{	
		return this.updateRandCode(phonenumber, password, 0);
	}
	
	public boolean updateRandCode(String phonenumber, String password, int device)
	{
		if (this.getCount() == 0)
		{
			return false;
		}
		
		boolean retval = false;
		
		LoginItem loginitem = queryPhonenumber(phonenumber);
		if (loginitem != null)
		{
			String newPassword = makeMd5(password + loginitem.getToken());
			if (newPassword.equals(loginitem.getPassword()))
			{
				String token = loginitem.getToken();
				
				if (device == 0)
				
				{
					token = makeRandCode();
				}
				
				newPassword = makeMd5(password + token);
				String sql = String.format("UPDATE %s SET password='%s', token='%s', device=%d WHERE %s LIKE '%s'",
						this.getTableName(), newPassword, token, device, FIELDS_ACCOUNT, phonenumber);
				retval = this.update(sql);
			}
		}
		
		return retval;
	}
	
	public LoginItem queryPhonenumber(String phonenumber)
	{
		LoginItem loginitem = null;
		
		String sql = String.format("SELECT * FROM %s WHERE %s LIKE '%s' LIMIT 1", this.getTableName(), FIELDS_ACCOUNT, phonenumber);
		List<Map<String, Object>> listMap = this.query(sql);
		
		if (listMap != null && listMap.size() == 1)
		{
			Map<String, Object> map = listMap.get(0);
			loginitem = MyUtils.transferMap2Bean(map, LoginItem.class);
		}

		return loginitem;
	}
	
	public LoginItem updatePassword(String phonenumber, String password,
			String newPassword) {
		
		LoginItem retval = null;
		
		if (!this.checkPhonenumber(phonenumber, password))
		{
			return null;
		}
		
		LoginItem loginitem = queryPhonenumber(phonenumber);
		if (loginitem != null)
		{
			newPassword = makeMd5(newPassword + loginitem.getToken());
			String sql = String.format("UPDATE %s SET password='%s', token='%s' WHERE %s LIKE '%s'",
					this.getTableName(), newPassword, loginitem.getToken(), FIELDS_ACCOUNT, phonenumber);
			if (this.update(sql))
			{
				retval = queryPhonenumber(phonenumber);
			}
		}
		
		return retval;
	}
	/**
	 * 
	 * @param map
	 * @return
	 */
	public int insert(Map<String, Object> map)
	{
		String sql = DbUtils.getInsertSql(this.getTableName(), map);
		return this.insertEx(sql);
	}
	
	public int insertEx(String sql)
	{
		int result = -1;
		
		if (super.insert(sql))
		{
			String query = String.format("SELECT * FROM %s ORDER BY id DESC LIMIT 1", this.getTableName());
			List<Map<String, Object>> list = this.query(query);
			
			if (list != null && list.size() == 1)
			{
				result = (int)list.get(0).get("id");
			}
		}
		else
		{
			
		}
		
		return result;
	}


	@Override
	public void onInitTable() {
		// TODO Auto-generated method stub
		
	}
	public List<LoginItem> queryList(long limit, long offset,String number )
	{
		List<LoginItem> loginList = new ArrayList<LoginItem>();

		String sql = String.format("SELECT * FROM %s WHERE phonenumber = %s ",this.getTableName(),number);
		List<Map<String, Object>> list = this.query(sql);
		if (list != null)
		{
			for(Map<String, Object> map : list)
			{
				LoginItem item = new LoginItem();
				try {
					item = TransferUtils.transferMap2Bean(map, LoginItem.class);
					loginList.add(item);
				} catch (TransferException e) {
					NetLog.error("Transfer", e.getMessage());
				}
			}
		}
		
		return loginList;
	}
	
	
	

}
