package com.bedrin.controllers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/about")
public class AboutServlet extends HttpServlet {

	private static final long serialVersionUID = -1759292122152315852L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setAttribute("page", "about");
		getServletContext().getRequestDispatcher("/about.jsp").forward(req, resp);
	}
	
}
