package com.bedrin.utils.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import org.apache.log4j.Logger;

import com.bedrin.models.pojo.User;
import com.bedrin.utils.DBConnectionManager;

public enum UserDAO {
	INSTANCE;
	
	private static final Logger l = Logger.getLogger(UserDAO.class);
	
	public void register(User user) throws SQLException {
		Connection c = DBConnectionManager.INSTANCE.getConnection();;
		try {
			l.trace("Connection established");
			c.setAutoCommit(false);
			PreparedStatement stmt = c.prepareStatement("INSERT INTO users(user_email, user_password, "
					+ "user_bought, user_uuid, user_uuid_expire_date) VALUES (?, ?, "
					+ "?, '0', ?);");
			try {
				stmt.setString(1, user.getEmail());
				stmt.setString(2, user.getPassword());
				stmt.setBoolean(3, user.isBought());
				Calendar calen = Calendar.getInstance();
				calen.setTime(new java.util.Date());
				calen.set(Calendar.DAY_OF_MONTH, calen.get(Calendar.DAY_OF_MONTH) - 1);
				java.util.Date expireDate = calen.getTime();
				stmt.setDate(4, new Date(expireDate.getTime()));
				stmt.execute();
			} finally {
				stmt.close();
			}
			try {
				stmt = c.prepareStatement("INSERT INTO users_to_roles(user_email, role_name) VALUES (?, 'user');");
				stmt.setString(1, user.getEmail());
				stmt.execute();
			} finally {
				stmt.close();
			}
			c.commit();
		} finally {
			c.close();
		}
	}
	
	public void setNewPasswordToUserWithEmail(String email, String newPassword) throws SQLException {
		Connection c = DBConnectionManager.INSTANCE.getConnection();
		l.trace("Connection established");
		try {
			PreparedStatement stmt = c.prepareStatement("UPDATE users SET user_password=? WHERE user_email=?;");
			try {
				stmt.setString(1, newPassword);
				stmt.setString(2, email);
				stmt.execute();
			} finally {
				stmt.close();
			}
		} finally {
			c.close();
		}
	}
	
	public boolean ifEmailRegistered(String email) throws SQLException {
		Connection c = DBConnectionManager.INSTANCE.getConnection();
		try {
			l.trace("Connection established");
			PreparedStatement stmt = c.prepareStatement("SELECT * FROM users WHERE user_email=?;");
			try {
				stmt.setString(1, email);
				ResultSet data = stmt.executeQuery();
				try {
					while (data.next()) {
						return true;
					}

				} finally {
					data.close();
				}
			} finally {
				stmt.close();
			}
		} finally {
			c.close();
		}
		return false;
	}
	
	public String findEmailByUUID(String uuid) throws SQLException {
		Connection c = DBConnectionManager.INSTANCE.getConnection();
		String username = null;
		try {
			l.trace("Connection established");
			PreparedStatement stmt = c.prepareStatement("SELECT user_email FROM users WHERE user_uuid=?;");
			try {
				stmt.setString(1, uuid);
				ResultSet rs = stmt.executeQuery();
				try {
					while (rs.next()) {
						username = rs.getString("user_email");
					}

				} finally {
					rs.close();
				}
			} finally {
				stmt.close();
			}
		} finally {
			c.close();
		}
		return username;
	}
	
	public User findUserByEmail(String email) throws SQLException {
		User r = new User();
		Connection c = DBConnectionManager.INSTANCE.getConnection();
		try {
			l.trace("Connection established");
			PreparedStatement stmt = c.prepareStatement("SELECT user_password, user_bought FROM users WHERE user_email=?;");
			try {
				stmt.setString(1, email);
				ResultSet rs = stmt.executeQuery();
				try {
					while (rs.next()) {
						r.setBought(rs.getBoolean(2));
						r.setPasswordRaw(rs.getString(1));
					}
					r.setEmail(email);
				} finally {
					rs.close();
				}
			} finally {
				stmt.close();
			}
		} finally {
			c.close();
		}
		return r;
	}
	
	public void setUserEmail(String newEmail, String oldEmail) throws SQLException {
		Connection connection = DBConnectionManager.INSTANCE.getConnection();
		l.trace("Connection established");
		try {
			PreparedStatement stmt = connection.prepareStatement("UPDATE users SET user_email=? WHERE user_email=?;");
			try {
				stmt.setString(1, newEmail);
				stmt.setString(2, oldEmail);
				stmt.execute();
			} finally {
				stmt.close();
			}
		} finally {
			connection.close();
		}
	}
	
	public void setUUID(String email, String uuid) throws SQLException {
		Connection connection = DBConnectionManager.INSTANCE.getConnection();
		try {
			l.trace("Connection established");
			PreparedStatement stmt = connection.prepareStatement("UPDATE users SET user_uuid=?, user_uuid_expire_date=? " + "WHERE user_email = ?;");;
			try {
				Calendar c = Calendar.getInstance();
				c.setTime(new java.util.Date());
				if (uuid.equals(User.EMPTY_UUID)) {
					c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) - 1);
				} else {
					c.add(Calendar.DATE, 30);
				}
				java.util.Date expireDate = c.getTime();
				stmt.setString(1, uuid);
				stmt.setDate(2, new Date(expireDate.getTime()));
				stmt.setString(3, email);
				stmt.execute();
				l.trace("Added UUID to user " + email);
			} finally {
				stmt.close();
			}
		} finally {
			connection.close();
		}
	}
	
	public void setUserBoughtOptionViaEmail(String email, boolean status) throws SQLException {
		Connection c = DBConnectionManager.INSTANCE.getConnection();
		l.trace("Connection established");
		try {
			PreparedStatement stmt = c.prepareStatement("UPDATE users SET user_bought=? WHERE user_email=?;");
			try {
				stmt.setBoolean(1, status);
				stmt.setString(2, email);
				stmt.execute();
			} finally {
				stmt.close();
			}
		} finally {
			c.close();
		}
	}
	
	public void deleteUUID(String email) throws SQLException {
		setUUID(email, User.EMPTY_UUID);
	}
}
