package com.goods.process;

import java.io.IOException;
import java.io.InputStream;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

public class JDBCTools {
	public static void update(String sql, Object ... args) {
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
	public static void release(ResultSet rs, Statement statement,Connection conn) {
		if (rs != null) {
			try {
				rs.close();
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (statement != null) {
			try {
				statement.close();
			}
			catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		if (conn != null) {
			try {
				conn.close();
				
			}
			catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	public void uodate(String sql) {
		Connection connection = null;
		Statement statement = null;
		try {
			connection = JDBCTools.getConnection();
			statement = (Statement) connection.createStatement();
			statement.executeUpdate(sql);
		}
		catch (Exception e) { 
			e.printStackTrace();  
		}
		finally {
			JDBCTools.release(statement, connection);  
		}
	}
	public static void release(Statement statement, Connection conn) { 
		if (statement != null) {
			try {
				statement.close();
				
			}
			catch (Exception e2) { 
				e2.printStackTrace(); 
			}
		}
		if (conn != null) {
			try {
				conn.close();
			}
			catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	//编写通用方法获取任意数据库链接，不用修改源程序
	public static Connection getConnection() throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException,IOException {
		String driverClass = null;
		String jdbcUrl = null;
		String user = null;
		String password = null;
		// 读取properties文件
		InputStream in = JDBCTools.class.getClassLoader().getResourceAsStream("jdbc.properties");
		Properties properties = new Properties();
		properties.load(in);
		driverClass = properties.getProperty("driver");
		jdbcUrl = properties.getProperty("url");
		user = properties.getProperty("user");
		password = properties.getProperty("password");
		Class.forName(driverClass);  
		Connection connection = (Connection) DriverManager.getConnection(jdbcUrl, user, password);
		return connection;
	}
}
