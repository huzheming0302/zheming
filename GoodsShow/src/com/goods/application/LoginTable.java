package com.goods.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import exhi.net.database.DatabaseParam;
import exhi.net.database.NetDatabase;
import exhi.net.database.NetTable;
import exhi.net.log.NetLog;
import exhi.net.utils.DbUtils;
import exhi.net.utils.TransferException;
import exhi.net.utils.TransferUtils;

public class LoginTable extends NetTable {
	
	private static final int mVersion = 1;
	private static final String mTableName = "information";
	
	public LoginTable(DatabaseParam param) {
		super(param, mTableName, 1, mVersion);
	}
	
	
	@Override
	public void onCreateTable(NetDatabase db) {
		
		NetLog.debug("Expert Table Name: ", this.getTableName());
		
		String mySql = "CREATE TABLE IF NOT EXISTS " + this.getTableName() +" (";
		
		//mySql += "id int not null primary key auto_increment,";
		mySql += "phonenumber VARCHAR(11) not null primary key,";
		mySql += "password VARCHAR(100) default '') ENGINE=InnoDB DEFAULT CHARSET=utf8";
		
		db.createTable(mySql);
	}
	
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
