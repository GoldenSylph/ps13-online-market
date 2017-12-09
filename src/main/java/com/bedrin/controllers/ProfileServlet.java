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
import com.bedrin.models.dto.ProfileEmailChangeDTO;
import com.bedrin.models.pojo.User;
import com.bedrin.utils.Cookies;
import com.bedrin.utils.Utils;
import com.bedrin.utils.dao.AddonDAO;
import com.bedrin.utils.dao.UserDAO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {

	private static final long serialVersionUID = 3351206450167103083L;
	private static final Logger l = Logger.getLogger(ProfileServlet.class);
	private final Gson GSON = new GsonBuilder().create();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setAttribute("page", "profile");
		User u = Utils.getCurrentUserFromCookies(req, resp, this, l);
		if (u.getEmail() == null) {
			try {
				String email = req.getSession(false).getAttribute("user_email").toString();
				if (email.isEmpty() || email == null) {
					resp.sendError(500, "Can not find your email data. Please try again.");
				}
				u = UserDAO.INSTANCE.findUserByEmail(email);
			} catch (SQLException e) {
				Utils.proceedSQLError(l, e);
				resp.sendError(500, "Can not find your profile. Please try again.");
				return;
			}
		}
		req.setAttribute("user_email", u.getEmail());
		req.setAttribute("user_bought", u.isBought());
		AddonsSetDTO attachedToUserAddons = null;
		try {
			attachedToUserAddons = AddonDAO.INSTANCE.getAllAddonsOfUser(u, false);
			l.info(attachedToUserAddons);
		} catch (SQLException e) {
			Utils.proceedSQLError(l, e);
			req.setAttribute("error", "There is some trouble acquiring your addons. Please try again.");
		}
		req.setAttribute("addons", attachedToUserAddons);
		getServletContext().getRequestDispatcher("/profile.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		boolean isFindOldEmailError = false;
		boolean isSetNewEmailError = false;
		
		String oldEmail = null;
		try {
			oldEmail = UserDAO.INSTANCE.findEmailByUUID(Cookies.getCookieValue(req, Cookies.COOKIE_NAME, l));
		} catch (SQLException e) {
			Utils.proceedSQLError(l, e);
			isFindOldEmailError = true;
		}
		
		String newEmail = req.getParameter("email");
		
		try {
			UserDAO.INSTANCE.setUserEmail(newEmail, oldEmail);
		} catch (SQLException e) {
			Utils.proceedSQLError(l, e);
			isSetNewEmailError = true;
		}
		ProfileEmailChangeDTO dto = new ProfileEmailChangeDTO();
		dto.setEmail(newEmail);
		dto.setFindOldEmailError(isFindOldEmailError);
		dto.setSetNewEmailError(isSetNewEmailError);
		String responseJSON = GSON.toJson(dto);
		resp.getWriter().print(responseJSON);
	}

}
