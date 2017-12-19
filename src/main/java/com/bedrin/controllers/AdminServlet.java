package com.bedrin.controllers;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.bedrin.models.dto.MarketDataSetDTO;
import com.bedrin.models.dto.MarketDataUnitDTO;
import com.bedrin.utils.Utils;
import com.bedrin.utils.active_record.Stats;

@WebServlet("/admin")
public class AdminServlet extends HttpServlet {

	private static final long serialVersionUID = -5592514612470933995L;
	private static final Logger l = Logger.getLogger(AdminServlet.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		MarketDataUnitDTO d1 = new MarketDataUnitDTO();
		d1.setDescription("The relation between users and users who bought the game.");
		MarketDataUnitDTO d2 = new MarketDataUnitDTO();
		d2.setDescription("The relation between users and users who playing game now.");
		MarketDataUnitDTO d3 = new MarketDataUnitDTO();
		d3.setDescription("The relation between users who bought the game and users play it now.");
		MarketDataUnitDTO d4 = new MarketDataUnitDTO();
		d3.setDescription("Users registered during last 24 hours.");
		
		try {
			d1.setValue(Stats.INSTANCE.getProcentOfUsersToUsersBought());
			d2.setValue(Stats.INSTANCE.getProcentOfUsersToUsersPlaying());
			d3.setValue(Stats.INSTANCE.getProcentOfUsersPlayingToBought());
			d4.setValue(Stats.INSTANCE.getUsersCountRegisteredForLast24Hours());
		} catch (SQLException e) {
			Utils.proceedSQLError(l, e);
			resp.sendError(500, "We can not access to the market statistics please try again");
		}
		
		MarketDataSetDTO marketDataDTO = new MarketDataSetDTO(d1, d2, d3, d4);
		req.setAttribute("market_data_dto", marketDataDTO);
		getServletContext().getRequestDispatcher("/admin.jsp").forward(req, resp);
	}
	
}
