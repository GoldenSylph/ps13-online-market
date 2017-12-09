package com.bedrin.utils.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.bedrin.models.dto.AddonsSetDTO;
import com.bedrin.models.pojo.Addon;
import com.bedrin.models.pojo.User;
import com.bedrin.utils.DBConnectionManager;

public enum AddonDAO {
	
	INSTANCE;
	
	private static final Logger l = Logger.getLogger(AddonDAO.class);
	
	public void enableAddon(long id, String email) throws SQLException {
		switchStatusOfAddon(id, true, email);
	}
	
	public void disableAddon(long id, String email) throws SQLException{
		switchStatusOfAddon(id, false, email);
	}
	
	public AddonsSetDTO getAllAddonsOfUserWithEmail(String email) throws SQLException {
		User u = new User();
		u.setEmail(email);
		return getAllAddonsOfUser(u, false);
	}

	public AddonsSetDTO getAllAddons() throws SQLException {
		AddonsSetDTO r = new AddonsSetDTO();
		r.setSet(new ArrayList<Addon>());
		try (Connection c = DBConnectionManager.INSTANCE.getConnection();
				PreparedStatement stmt = c.prepareStatement("SELECT * FROM addons;");
				ResultSet res = stmt.executeQuery()) {
			while (res.next()) {
				Addon ad = new Addon();
				ad.setId(res.getLong(1));
				ad.setName(res.getString(2));
				ad.setDescription(res.getString(3));
				ad.setCost(res.getInt(4));
				r.getSet().add(ad);
			}
		}
		return r;
	}
	
	public AddonsSetDTO getAllAddonsWithActivitiesOfUser(User u) throws SQLException {
		AddonsSetDTO r = getAllAddonsOfUser(u, false);
		l.info("Acquiring the relative addons: " + r);
		AddonsSetDTO t = getAllAddons();
		l.info("Acquiring all addons: " + t);
		for (int i = 0; i < t.getSet().size(); i++) {
			Addon a = t.getSet().get(i);
			int indexInAddonsOfUser = r.getSet().indexOf(a);
			if (indexInAddonsOfUser != -1) {
				a.setEnabled(r.getSet().get(indexInAddonsOfUser).isEnabled());
			}
		}
		l.info("Returning: " + t);
		return t;
	}
	
	public AddonsSetDTO getAllAddonsOfUser(User u, boolean excludesEmail) throws SQLException {
		AddonsSetDTO r = new AddonsSetDTO();
		r.setSet(new ArrayList<Addon>());
		Connection c = DBConnectionManager.INSTANCE.getConnection();
		//TODO написать триггеры и вменяемый запрос
		try (PreparedStatement stmt = c.prepareStatement("SELECT * FROM "
				+ "addons AS ad LEFT OUTER JOIN users_to_addons AS u_to_a USING (addon_id) "
				+ "WHERE u_to_a.user_email" + (excludesEmail ? "!" : "") + String.format("='%s';", u.getEmail()));
				ResultSet results = stmt.executeQuery()) {
			c.setAutoCommit(false);
			while (results.next()) {
				Addon ad = new Addon();
				ad.setId(results.getLong(1));
				ad.setName(results.getString(2));
				ad.setDescription(results.getString(3));
				ad.setCost(results.getInt(4));
				ad.setEnabled(results.getBoolean(6));
				r.getSet().add(ad);
			}
		}
		c.close();
		return r;
	}
	
	private void switchStatusOfAddon(long id, boolean status, String email) throws SQLException {
		try (Connection c = DBConnectionManager.INSTANCE.getConnection(); 
				PreparedStatement stmt = c.prepareStatement("UPDATE users_to_addons SET addon_enabled=? WHERE addon_id=? AND user_email=?;")) {
			stmt.setBoolean(1, status);
			stmt.setLong(2, id);
			stmt.setString(3, email);
			stmt.execute();
		}
	}
	
	public Addon getAddonWithIDWithoutEnabledOption(long id) throws SQLException {
		Addon a = new Addon();
		try (Connection c = DBConnectionManager.INSTANCE.getConnection();
		PreparedStatement stmt = c.prepareStatement(String.format("SELECT DISTINCT * FROM"
				+ " addons WHERE addon_id='%s';", Long.toString(id)));
		ResultSet res = stmt.executeQuery()) {
			while (res.next()) {
				a.setId(res.getLong(1));
				a.setName(res.getString(2));
				a.setDescription(res.getString(3));
				a.setCost(res.getInt(4));
			}
		}
		return a;
	}

	public void bindAddonToUserWithEmail(String email, long addonID) throws SQLException {
		try (Connection c = DBConnectionManager.INSTANCE.getConnection();
		PreparedStatement stmt = c.prepareStatement("INSERT INTO users_to_addons (user_email, addon_id) VALUES (?, ?);")) {
			stmt.setString(1, email);
			stmt.setLong(2, addonID);
			stmt.execute();
		}
	}
	
}
