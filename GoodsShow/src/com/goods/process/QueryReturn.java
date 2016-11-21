package com.goods.process;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.ResultSetMetaData;



public class QueryReturn {
	//更新数据库操作
	public void update(String sql, Object... args) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = JDBCTools.getConnection();
			preparedStatement = (PreparedStatement) connection.prepareStatement(sql);
			for (int i = 0; i < args.length; i++) {
				preparedStatement.setObject(i + 1, args[i]); 
			}
			preparedStatement.executeUpdate(); 
		}
		catch (Exception e) {
			e.printStackTrace(); 
		}
		finally { 
			JDBCTools.release(preparedStatement, connection);
		}
	}
	//通用查询方法，返回一条记录
	public <T> T get(Class<T> clazz, String sql, Object... args) {
		T entity = null;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet result = null; 
		try {
			connection = JDBCTools.getConnection();
			preparedStatement = (PreparedStatement) connection.prepareStatement(sql); 
			for (int i = 0; i < args.length; i++) {
				preparedStatement.setObject(i + 1, args[i]);
			}
			result = preparedStatement.executeQuery();
			Map<String, Object> map = new HashMap<String, Object>();
			ResultSetMetaData rsmd = (ResultSetMetaData) result.getMetaData();
			if (result.next()) {
				for (int i = 0; i < rsmd.getColumnCount(); i++) {
					String columnLabel = rsmd.getColumnLabel(i + 1);
					Object value = result.getObject(i + 1);
					map.put(columnLabel, value);
				}
			}
			if (map.size() > 0) {
				entity = clazz.newInstance();
				for (Map.Entry<String, Object> entry : map.entrySet()) {
					String filedName = entry.getKey();
					Object filedObject = entry.getValue();
					BeanUtils.setProperty(entity, filedName, filedObject);
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace(); 
		}
		finally { 
			JDBCTools.release(result, preparedStatement, connection);
		}
		return entity; 
	}
	//通用查询方法，返回一个结果集 
	public <T> List<T> getForList(Class<T> clazz, String sql, Object... args) {
		List<T> list = new ArrayList<T>();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		try {
			connection = JDBCTools.getConnection();
			preparedStatement = (PreparedStatement) connection.prepareStatement(sql);
			for (int i = 0; i < args.length; i++) { 
				preparedStatement.setObject(i + 1, args[i]);
				
			}
			result = preparedStatement.executeQuery(); 
			List<Map<String, Object>> values = handleResultSetToMapList(result);
			list = transfterMapListToBeanList(clazz, values);
		}
		catch (Exception e) {
			e.printStackTrace();
			
		}
		finally {
			JDBCTools.release(result, preparedStatement, connection); 
		}
		return list; 
	}
	public <T> List<T> transfterMapListToBeanList(Class<T> clazz,
			List<Map<String, Object>> values) throws InstantiationException,
			IllegalAccessException, InvocationTargetException { 
		List<T> result = new ArrayList<T>();
		T bean = null;
		if (values.size() > 0) {
			for (Map<String, Object> m : values) {
				bean = clazz.newInstance();
				for (Map.Entry<String, Object> entry : m.entrySet()) { 
					String propertyName = entry.getKey();
					Object value = entry.getValue();
					BeanUtils.setProperty(bean, propertyName, value);
				}
				result.add(bean);
			}
		}
		return result;
	}
	public List<Map<String, Object>> handleResultSetToMapList(
			ResultSet resultSet) throws SQLException {
		List<Map<String, Object>> values = new ArrayList<Map<String, Object>>();
		List<String> columnLabels = getColumnLabels(resultSet);
		Map<String, Object> map = null; 
		while (resultSet.next()) {
			map = new HashMap<String, Object>();
			for (String columnLabel : columnLabels) { 
				Object value = resultSet.getObject(columnLabel);
				map.put(columnLabel, value);
				
			}
			values.add(map);
		}
		return values; 
	}
	private List<String> getColumnLabels(ResultSet resultSet)throws SQLException {
		List<String> labels = new ArrayList<String>();
		ResultSetMetaData rsmd = (ResultSetMetaData) resultSet.getMetaData();
		for (int i = 0; i < rsmd.getColumnCount(); i++) {
			labels.add(rsmd.getColumnLabel(i + 1));
		}
		return labels;
	}
	//通用查询方法，返回一个值（可能是统计值）
	@SuppressWarnings("unchecked")
	public <E> E getForValue(String sql, Object... args) {
		Connection connection = null;
		PreparedStatement preparedStatement = null; 
		ResultSet resultSet = null;
		try {
			connection = JDBCTools.getConnection();
			preparedStatement = (PreparedStatement) connection.prepareStatement(sql);
			for (int i = 0; i < args.length; i++) {
				preparedStatement.setObject(i + 1, args[i]);
				
			}
			resultSet = preparedStatement.executeQuery(); 
			if (resultSet.next()) {  
				return (E) resultSet.getObject(1);
			}
		}
		catch (Exception e) {
			e.printStackTrace(); 
		}
		finally {
			JDBCTools.release(resultSet, preparedStatement, connection);
		}
		return null;
	}
			
}
