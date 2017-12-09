package com.bedrin.utils.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.bedrin.models.pojo.Bug;
import com.bedrin.utils.DBConnectionManager;

public enum BugDAO {
	
	INSTANCE;
	
	private static final Logger l = Logger.getLogger(BugDAO.class);
	
	public void register(Bug bug) throws SQLException {
		l.info("Registering bug " + bug.toString());
		Connection c = DBConnectionManager.INSTANCE.getConnection();
		try {
			c.setAutoCommit(false);
			PreparedStatement stmt = c.prepareStatement("INSERT INTO bugs (bug_description, bug_log_file, bug_user_email) VALUES "
					+ "(?, ?, ?);");;
			try {
				stmt.setString(1, bug.getDescription());
				stmt.setString(2, bug.getLogFile());
				stmt.setString(3, bug.getEmail());
				stmt.execute();
			} finally {
				stmt.close();
			}
			l.info(String.format("Bug %s is registered.", bug.toString()));
			
			l.info(String.format("Extracting current bug_id of %s.", bug.toString()));
			ResultSet result = null;
			long bugID = 0;
			try {
				stmt = c.prepareStatement("SELECT bug_id FROM bugs WHERE (bug_description=? AND bug_log_file=? AND bug_user_email=?);");
				stmt.setString(1, bug.getDescription());
				stmt.setString(2, bug.getLogFile());
				stmt.setString(3, bug.getEmail());
				result = stmt.executeQuery();
				while (result.next()) {
					bugID = result.getLong(1);
				}
				l.info(String.format("The bug_id of %s has been extracted: %s", bug.toString(), String.valueOf(bugID)));
			}
			finally {
				stmt.close();
				result.close();
			}
			l.info(String.format("Creating a link between user %s and bug %s.", bug.getEmail(), bug.toString()));
			try {
				stmt = c.prepareStatement("INSERT INTO users_to_bugs(user_email, bug_id) VALUES (?, ?);");
				stmt.setString(1, bug.getEmail());
				if (bugID == 0) {
					throw new SQLException("BUG ID IS 0");
				}
				stmt.setLong(2, bugID);
				stmt.execute();
			} finally {
				stmt.close();
			}
			l.info("The link is created.");
			c.commit();
		} finally {
			c.close();
		}
		l.info(String.format("The bug %s is proceeded.", bug.toString()));
	}
	
}
