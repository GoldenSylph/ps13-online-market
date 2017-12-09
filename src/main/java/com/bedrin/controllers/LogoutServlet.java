package com.bedrin.controllers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.bedrin.utils.Cookies;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

	private static final long serialVersionUID = 7672936783333020562L;
	private static final Logger l = Logger.getLogger(LogoutServlet.class);
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession ses = req.getSession(false);
		ses.invalidate();
		Cookies.removeCookie(req, resp, "ps13UUID");
		l.info("Cookie ps13UUID removed");
		req.setAttribute("logout_state", "You are logged out successfully.");
		getServletContext().getRequestDispatcher("/logout.jsp").forward(req, resp);
	}
	
}
