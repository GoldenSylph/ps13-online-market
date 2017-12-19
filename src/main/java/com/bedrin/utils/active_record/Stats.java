package com.bedrin.utils.active_record;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.bedrin.utils.DBConnectionManager;
import com.bedrin.utils.Utils;

public enum Stats {

	INSTANCE;
	
	public class DTO {
		
		private long resourcesFound;
		private long resourcesSold;
		private long numberOfKryoStationsStands;
		private long numberOfHydrobotSold;
		private long numberOfKryobotSold;
		private long numberOfPlayersPlaying;
		private long numberOfPlayers;
		private long numberOfPlayersBought;
		
		public long getResourcesFound() {
			return resourcesFound;
		}

		public void setResourcesFound(long resourcesFound) {
			this.resourcesFound = resourcesFound;
		}

		public long getResourcesSold() {
			return resourcesSold;
		}

		public void setResourcesSold(long resourcesSold) {
			this.resourcesSold = resourcesSold;
		}

		public long getNumberOfKryoStationsStands() {
			return numberOfKryoStationsStands;
		}

		public void setNumberOfKryoStationsStands(long numberOfKryoStationsStands) {
			this.numberOfKryoStationsStands = numberOfKryoStationsStands;
		}

		public long getNumberOfHydrobotSold() {
			return numberOfHydrobotSold;
		}

		public void setNumberOfHydrobotSold(long numberOfHydrobotSold) {
			this.numberOfHydrobotSold = numberOfHydrobotSold;
		}

		public long getNumberOfKryobotSold() {
			return numberOfKryobotSold;
		}

		public void setNumberOfKryobotSold(long numberOfKryobotSold) {
			this.numberOfKryobotSold = numberOfKryobotSold;
		}

		public long getNumberOfPlayersPlaying() {
			return numberOfPlayersPlaying;
		}

		public void setNumberOfPlayersPlaying(long numberOfPlayersPlaying) {
			this.numberOfPlayersPlaying = numberOfPlayersPlaying;
		}

		public long getNumberOfPlayers() {
			return numberOfPlayers;
		}

		public void setNumberOfPlayers(long numberOfPlayers) {
			this.numberOfPlayers = numberOfPlayers;
		}

		public long getNumberOfPlayersBought() {
			return numberOfPlayersBought;
		}

		public void setNumberOfPlayersBought(long numberOfPlayersBought) {
			this.numberOfPlayersBought = numberOfPlayersBought;
		}
	}
	
	public final int ID = 1;
	
	private final Logger l = Logger.getLogger(Stats.class);
	private DTO dto;
	
	Stats() {
		try {
			reload();
		} catch (SQLException e) {
			Utils.proceedSQLError(l, e);
			l.error("STATS HAS NOT BEEN LOADED. Please recall: Stats.INSTANCE.reload() or Stats.INSTANCE.getStats()!");
		}
	}
	
	public DTO getStats() throws SQLException {
		reload();
		return dto;
	}
	
	public void reload() throws SQLException {
		dto = new DTO();
		try (Connection c = DBConnectionManager.INSTANCE.getConnection();
				PreparedStatement stmt = c.prepareStatement("SELECT * FROM get_stats;");
				ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				dto.setResourcesFound(rs.getLong(1));
				dto.setResourcesSold(rs.getLong(2));
				dto.setNumberOfKryoStationsStands(rs.getLong(3));
				dto.setNumberOfHydrobotSold(rs.getLong(4));
				dto.setNumberOfPlayersPlaying(rs.getLong(5));
				dto.setNumberOfPlayers(rs.getLong(6));
				dto.setNumberOfPlayersBought(rs.getLong(7));
				dto.setNumberOfKryobotSold(rs.getLong(8));
			}
		}
	}
	
	public byte getUsersCountRegisteredForLast24Hours() throws SQLException {
		return getResultOfCountProcedure("SELECT * FROM get_count_registered_users_for_last_day();");
	}
	
	public byte getProcentOfUsersToUsersBought() throws SQLException {
		return getResultOfCountProcedure("SELECT * FROM get_procent_of_users_bought();");
	}
	
	public byte getProcentOfUsersToUsersPlaying() throws SQLException {
		return getResultOfCountProcedure("SELECT * FROM get_procent_of_users_playing_to_all();");
	}
	
	public byte getProcentOfUsersPlayingToBought() throws SQLException {
		return getResultOfCountProcedure("SELECT * FROM get_procent_of_users_playing_to_bought();");
	}
	
	private byte getResultOfCountProcedure(String query) throws SQLException {
		try (Connection c = DBConnectionManager.INSTANCE.getConnection()) {
			try (PreparedStatement stmt = c.prepareStatement(query)) {
				try (ResultSet rs = stmt.executeQuery()) {
					rs.next();
					return (byte) rs.getLong(1);
				}
			}
		}
	}
	
	
}
