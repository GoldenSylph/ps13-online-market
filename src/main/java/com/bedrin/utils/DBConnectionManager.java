package com.bedrin.utils;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;


public enum DBConnectionManager {
	
	INSTANCE;
	
	public Connection getConnection() {
		
		InitialContext initContext = null;
		try {
			initContext = new InitialContext();
		} catch (NamingException e) {
			e.printStackTrace();
		}
		DataSource ds = null;
		try {
			ds = (DataSource) initContext.lookup("java:comp/env/jdbc/ps13_db");
		} catch (NamingException e1) {
			e1.printStackTrace();
		}
		Connection con = null;
		try {
			con = ds.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return con;
	}

}
