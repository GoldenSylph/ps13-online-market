package com.bedrin.controllers;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.bedrin.utils.Utils;
import com.bedrin.utils.active_record.Stats;

@WebServlet("/about")
public class AboutServlet extends HttpServlet {

	private static final long serialVersionUID = -1759292122152315852L;
	private static final Logger l = Logger.getLogger(AboutServlet.class);
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setAttribute("page", "about");
		Stats.DTO stat_dto = null;
		try {
			stat_dto = Stats.INSTANCE.getStats();
		} catch (SQLException e) {
			Utils.proceedSQLError(l, e);
			req.setAttribute("error", "There is some problems acquiring statistic data. You can try reload page or "
					+ "if it will not help, just wait. We're about to fix it.");
		}
		req.setAttribute("stat_dto", stat_dto);
		getServletContext().getRequestDispatcher("/about.jsp").forward(req, resp);
	}
	
}