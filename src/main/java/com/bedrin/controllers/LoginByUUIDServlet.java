package com.bedrin.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.bedrin.utils.Cookies;
import com.bedrin.utils.Utils;
import com.bedrin.utils.dao.UserDAO;

@WebServlet("/prelogin")

public class LoginByUUIDServlet extends HttpServlet {

	private static final long serialVersionUID = 2068113957432895383L;
	private static final Logger l = Logger.getLogger(LoginByUUIDServlet.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doProcess(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doProcess(req, resp);
	}

	private void doProcess(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String uuid = Cookies.getCookieValue(req, Cookies.COOKIE_NAME, l);
		Object user = req.getSession().getAttribute("user");
		String email = null;

		if (uuid == null) {
			if (user != null) {
				l.info("The user is defined from session as: " + user.toString());
				Cookies.addCookie(req, resp, Cookies.COOKIE_NAME, uuid, Cookies.COOKIE_AGE); // Extends age.
				l.info("Cookie was added and time prolonged ");
			} else {
				l.info("The user is not defined from session as authorized.");
				Cookies.removeCookie(req, resp, Cookies.COOKIE_NAME);
				l.info("Cookie is removed and request is dispatched to the login.jsp");
				getServletContext().getRequestDispatcher("/logon.jsp").forward(req, resp);
			}
		} else {
			try {
				email = UserDAO.INSTANCE.findEmailByUUID(uuid);
				l.info("The cookie for the online market accepted; the corresponding user found: " + email);
				req.login(email, uuid);
				l.info("The user " + email
						+ " was requested to log in using email and uuid received from cookie");
				Cookies.addCookie(req, resp, Cookies.COOKIE_NAME, uuid, Cookies.COOKIE_AGE); // Extends age.
				l.info("Cookie was added and time prolonged ");
				PrintWriter pw = resp.getWriter();
				pw.print("<body onLoad=\"window.location.reload()\"/>");
			} catch (SQLException e) {
				Utils.proceedSQLError(l, e);
				getServletContext().getRequestDispatcher("/logon.jsp").forward(req, resp);
				return;
			} catch (Exception e) {
				l.error(e.getLocalizedMessage());
				l.error(e.getMessage());
				Cookies.removeCookie(req, resp, Cookies.COOKIE_NAME);
				getServletContext().getRequestDispatcher("/logon.jsp").forward(req, resp);
				return;
			}
		}
	}
}
