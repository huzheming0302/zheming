package com.goods.application;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import exhi.net.log.NetLog;
import exhi.net.utils.TransferException;
import exhi.net.utils.TransferUtils;

public class MyUtils {

	static final String TAG = MyUtils.class.getName();
	
	public static Map<String, Object> transferBean2Map(Object object)
	{
		Map<String, Object> map = null;
		
		try {
			map = TransferUtils.transferBean2Map(object);
		} catch (TransferException e) {
			NetLog.error(TAG, e.getMessage());
		}
		
		return map;
	}

	public static <T> T transferMap2Bean(Map<String, Object> map, Class<T> type)
	{
		T object = null;
		
		try {
			object = TransferUtils.transferMap2Bean(map, type);
		} catch (TransferException e) {
			NetLog.error(TAG, e.getMessage());
		}
		
		return object;
	}
	

	public static Map<String, Object> mysql_escape_string(Map<String, Object> request) {
		for (Entry<String, Object> entry : request.entrySet()) {
			if (entry.getValue() instanceof String)
			{
				String val = entry.getValue().toString();
				val = val.replace("\\", "\\\\").replace("\"", "\\\"").replace("'", "\\'");
				entry.setValue(val);
			}
		}

		return request;
	}
	
	public static Object html_escape_string(Object object)
	{
		Object value = object;
		if (object instanceof String)
		{
			String val = object.toString();
			val = val.replace("&", "&amp;").replace("\"", "&quot;");
			val = val.replace("<", "&lt;").replace(">", "&gt;");
			value = val;
		}
		
		return value;
	}
	
	public static Map<String, Object> html_escape_string(Map<String, Object> map)
	{
		for (Entry<String, Object> entry : map.entrySet()) {
			
			if (entry.getValue() instanceof String)
			{
				String val = entry.getValue().toString();
				val = val.replace("&", "&amp;").replace("\"", "&quot;");
				val = val.replace("<", "&lt;").replace(">", "&gt;");
				entry.setValue(val);
			}
		}

		return map;
	}
	
	public static List<Map<String, Object>> html_escape_string(List<Map<String, Object>> list)
	{
		for (Map<String, Object> map : list)
		{
			html_escape_string(map);
		}
		
		return list;
	}
	
	public static <T> Map<String, Object> filterMapData(Class<T> type, Map<String, String> map)
	{
		Map<String, Object> value = new HashMap<String, Object>();
		
		T object = null;
		try {
			object = type.newInstance();
		} catch (InstantiationException e) {
			object = null;
		} catch (IllegalAccessException e) {
			object = null;
		}
		
		if (object == null)
		{
			return value;
		}
		
		Map<String, Object> data = MyUtils.transferBean2Map(object);

		for (String key : data.keySet()) {
			if (map.containsKey(key) && map.get(key) != null)
			{
				value.put(key, map.get(key));
			}
		}
		
		return value;
	}
}
