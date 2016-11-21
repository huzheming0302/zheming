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

public class NotificationTable extends NetTable {

	private static final int mVersion = 1;
	private static final String mTableName = "Acount";
	
	public NotificationTable(DatabaseParam param) {
		super(param, mTableName, 1, mVersion);
	}
	
	@Override
	public void onCreateTable(NetDatabase db) {
		
		NetLog.debug("Expert Table Name: ", this.getTableName());
		
		String mySql = "CREATE TABLE IF NOT EXISTS " + this.getTableName() +" (";
		
		mySql += "id int not null primary key auto_increment,";
		mySql += "date VARCHAR(100) not null,";
		mySql += "event VARCHAR(100) not null DEFAULT '',";
		mySql += "money VARCHAR(100) not null,";
		
		mySql += "remark VARCHAR(100) default '') ENGINE=InnoDB DEFAULT CHARSET=utf8";
		
		db.createTable(mySql);
	}

	public long getCount()
	{
		long result = 0;
		
		String query = String.format("SELECT count(*) FROM %s", this.getTableName());
		List<Map<String, Object>> list = this.query(query);
		
		if (list != null && list.size() == 1)
		{
			String val = String.valueOf(list.get(0).get("count(*)"));
			result = Long.valueOf(val).longValue();
		}
		
		return result;
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

	public List<NotificationItem> queryList(long limit, long offset)
	{
		List<NotificationItem> notifList = new ArrayList<NotificationItem>();

		String sql = String.format("SELECT * FROM %s limit %d,%d", this.getTableName(), limit, offset);
		List<Map<String, Object>> list = this.query(sql);
		if (list != null)
		{
			for(Map<String, Object> map : list)
			{
				NotificationItem item = new NotificationItem();
				try {
					item = TransferUtils.transferMap2Bean(map, NotificationItem.class);
					notifList.add(item);
				} catch (TransferException e) {
					NetLog.error("Transfer", e.getMessage());
				}
			}
		}
		
		return notifList;
	}
	
	// return id
	public int insert(NotificationItem item)
	{
		// insert
		String sql = String.format("INSERT INTO %s (date,event,money,remark)"
				+ " VALUES('%s','%s','%s','%s')", this.getTableName(), item.getDate(),item.getEvent(), item.getMoney(),item.getRemark());
		return this.insertEx(sql);
	}
	
	public int insert(Map<String, Object> map)
	{
		String sql = DbUtils.getInsertSql(this.getTableName(), map);
		return this.insertEx(sql);
	}
	
	public boolean deleteById(int id)
	{
		String sql = String.format("DELETE FROM %s where id=%d", this.getTableName(), id);
		return this.update(sql);
	}

	@Override
	public void onInitTable() {

	}

}
