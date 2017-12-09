package com.bedrin.controllers;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.bedrin.models.dto.AddonsSetDTO;
import com.bedrin.models.pojo.User;
import com.bedrin.utils.Utils;
import com.bedrin.utils.dao.AddonDAO;
import com.bedrin.utils.dao.UserDAO;

@WebServlet("/adswitch")
public class SwitchAddonActivityServlet extends HttpServlet {
	
	private static final long serialVersionUID = 8184254869786009521L;
	private static final Logger l = Logger.getLogger(SwitchAddonActivityServlet.class);

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		AddonsSetDTO attachedToUserAddons = null;
		User currentUser = Utils.getCurrentUserFromCookies(req, resp, this, l);
		if (currentUser.getEmail() == null) {
			try {
				currentUser = UserDAO.INSTANCE.findUserByEmail(req.getParameter("email"));
			} catch (SQLException e) {
				Utils.proceedSQLError(l, e);
				resp.sendError(500, "Can not find your profile. Please try again.");
				return;
			}
		}
		
		String command = req.getParameter("addon_switch_command");
		long addonID = Long.valueOf(req.getParameter("addon_switch_id"));
		if ("enable".equals(command)) {
			try {
				AddonDAO.INSTANCE.enableAddon(addonID, currentUser.getEmail());
			} catch (SQLException e) {
				Utils.proceedSQLError(l, e);
				req.setAttribute("error", "Error during enabling addon. Please try again.");
				getServletContext().getRequestDispatcher("/profile.jsp").forward(req, resp);
				return;
			}
		} else if("disable".equals(command)) {
			try {
				AddonDAO.INSTANCE.disableAddon(addonID, currentUser.getEmail());
			} catch (SQLException e) {
				Utils.proceedSQLError(l, e);
				Utils.processTheRequest(req, currentUser, attachedToUserAddons, "Error during enabling addon. Please try again.");
				getServletContext().getRequestDispatcher("/profile.jsp").forward(req, resp);
				return;
			}
		}
		
		try {
			attachedToUserAddons = AddonDAO.INSTANCE.getAllAddonsOfUser(currentUser, false);
		} catch (SQLException e) {
			Utils.proceedSQLError(l, e);
			Utils.processTheRequest(req, currentUser, 
					attachedToUserAddons, "Error acquiring your attached addons from database. Please try again.");
			getServletContext().getRequestDispatcher("/profile.jsp").forward(req, resp);
			return;
		}
		
		Utils.processTheRequest(req, currentUser, attachedToUserAddons, "");
		getServletContext().getRequestDispatcher("/profile.jsp").forward(req, resp);
	}
	
}
