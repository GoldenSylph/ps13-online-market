package com.bedrin.controllers;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
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

@WebServlet("/swpass")
public class SwitchPasswordServlet extends HttpServlet {

	private static final long serialVersionUID = 2618076004705764550L;
	private static final Logger l = Logger.getLogger(SwitchPasswordServlet.class);
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
		
		AddonsSetDTO attachedToUserAddons = null;
		try {
			attachedToUserAddons = AddonDAO.INSTANCE.getAllAddonsOfUser(currentUser, false);
		} catch (SQLException e) {
			Utils.proceedSQLError(l, e);
			req.setAttribute("error", "Error acquiring your attached addons from database. Please try again.");
		}
		String newPassword = null;
		try {
			newPassword = User.encodePassword(req.getParameter("newPassword"));
		} catch (NoSuchAlgorithmException e) {
			Utils.proceedError(l, e);
			Utils.processTheRequest(req, currentUser, attachedToUserAddons, "Error during encoding your new password. Please try again.");
			getServletContext().getRequestDispatcher("/profile.jsp").forward(req, resp);
			return;
		}
		String oldPassword = null;
		try {
			oldPassword = User.encodePassword(req.getParameter("oldPassword"));
		} catch (NoSuchAlgorithmException e) {
			Utils.proceedError(l, e);
			Utils.processTheRequest(req, currentUser, attachedToUserAddons, "Error during encoding your password. Please try again.");
			getServletContext().getRequestDispatcher("/profile.jsp").forward(req, resp);
			return;
		}
		
		if (!currentUser.getPassword().equals(oldPassword)) {
			Utils.processTheRequest(req, currentUser, attachedToUserAddons, "The old password isn't valid. Please try again.");
			getServletContext().getRequestDispatcher("/profile.jsp").forward(req, resp);
			return;
		}
		
		l.info(newPassword);
		l.info(oldPassword);
		l.info(currentUser.getEmail());
		
		try {
			UserDAO.INSTANCE.setNewPasswordToUserWithEmail(currentUser.getEmail(), newPassword);
		} catch (SQLException e) {
			Utils.processTheRequest(req, currentUser, attachedToUserAddons, "Error during setting your new password. Please try again.");
			getServletContext().getRequestDispatcher("/profile.jsp").forward(req, resp);
			return;
		}
		Utils.processTheRequest(req, currentUser, attachedToUserAddons, "You changed your password successfully!");
		getServletContext().getRequestDispatcher("/profile.jsp").forward(req, resp);
	}
	
	

}
