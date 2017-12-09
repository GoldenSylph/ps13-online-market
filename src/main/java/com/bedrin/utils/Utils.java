package com.bedrin.utils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.bedrin.models.dto.AddonsSetDTO;
import com.bedrin.models.pojo.User;
import com.bedrin.utils.dao.UserDAO;

public class Utils {
	
	public static User getCurrentUserFromCookies(HttpServletRequest req, HttpServletResponse resp, HttpServlet servlet, Logger l) throws IOException, ServletException {
		User currentUser = null;
		String emailOfCurrentUser = null;
		try {
			emailOfCurrentUser = UserDAO.INSTANCE.findEmailByUUID(Cookies.getCookieValue(req, Cookies.COOKIE_NAME, l));
		} catch (SQLException e) {
			Utils.proceedSQLError(l, e);
			req.setAttribute("error", "Error acquiring your email from database. Please try again.");
		}
		try {
			currentUser = UserDAO.INSTANCE.findUserByEmail(emailOfCurrentUser);
		} catch (SQLException e) {
			Utils.proceedSQLError(l, e);
			req.setAttribute("error", "Error acquiring your profile from database. Please try again.");
		}
		return currentUser;
	}
	
	public static void proceedError(Logger l, Exception e) {
		l.error(e.getLocalizedMessage());
		l.error(e.getMessage());
		l.error(Arrays.toString(e.getStackTrace()));
	}
	
	public static void proceedSQLError(Logger l, SQLException e) {
		l.error(e.getErrorCode());
		l.error(e.getSQLState());
		l.error(e.getMessage());
		l.error(e.getLocalizedMessage());
	}
	
	public static void processTheRequest(HttpServletRequest req, User currentUser, AddonsSetDTO attachedToUserAddons, String errorMessage) {
		req.setAttribute("error", errorMessage);
		req.setAttribute("user_email", currentUser.getEmail());
		req.setAttribute("user_bought", currentUser.isBought());
		req.setAttribute("addons", attachedToUserAddons);
	}
}
