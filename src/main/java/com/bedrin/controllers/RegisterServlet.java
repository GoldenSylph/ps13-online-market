package com.bedrin.controllers;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.bedrin.models.pojo.User;
import com.bedrin.utils.Cookies;
import com.bedrin.utils.Utils;
import com.bedrin.utils.dao.AddonDAO;
import com.bedrin.utils.dao.UserDAO;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

	private static final long serialVersionUID = -869407754962525595L;
	private static final Logger l = Logger.getLogger(RegisterServlet.class);
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setAttribute("provided_email", req.getParameter("provided_email"));
		req.setAttribute("cert", req.getParameter("cert"));
		req.setAttribute("kind", req.getParameter("kind"));
		req.setAttribute("ad_id", req.getParameter("ad_id"));
		getServletContext().getRequestDispatcher("/registration.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost("https://www.google.com/recaptcha/api/siteverify");
		List<NameValuePair> arguments = new ArrayList<>();
		arguments.add(new BasicNameValuePair("secret", "6LedYjkUAAAAAAqUESIc9qslL2gVT0vltEUeztlA"));
		arguments.add(new BasicNameValuePair("response", req.getParameter("g-recaptcha-response")));
		
		try {
            post.setEntity(new UrlEncodedFormEntity(arguments));
            HttpResponse response = client.execute(post);
            JsonElement je = new JsonParser().parse(EntityUtils.toString(response.getEntity()));
            JsonObject jo = je.getAsJsonObject();
            boolean success = jo.get("success").getAsBoolean();
            if (!success) {
            	req.setAttribute("error", "Robots are not welcome.");
    			getServletContext().getRequestDispatcher("/registration.jsp").forward(req, resp);
    			return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
		
		String email = req.getParameter("email");
		String password = req.getParameter("password");
		String repassword = req.getParameter("repassword");
		boolean remember = "on".equals(req.getParameter("remember_me"));
		
		if (!email.matches("^[A-Za-z0-9_]+@[a-z0-9]+(?:.[a-z]+[a-z]*)+$")) {
			req.setAttribute("error", "Email must be valid.");
			getServletContext().getRequestDispatcher("/registration.jsp").forward(req, resp);
			return;
		}
		
		if (!password.equals(repassword)) {
			req.setAttribute("error", "Typed passwords does not match.");
			getServletContext().getRequestDispatcher("/registration.jsp").forward(req, resp);
			return;
		}
		
		User user = new User();
		user.setEmail(email);
		user.setBought(false);
		try {
			user.setPassword(password);
		} catch (NoSuchAlgorithmException e) {
			l.error(e.getLocalizedMessage());
			l.error(e.getMessage());
			req.setAttribute("error", "Some troubles with encrypting your password. Please try again.");
			getServletContext().getRequestDispatcher("/registration.jsp").forward(req, resp);
			return;
		}
		
		String certificate = req.getParameter("cert");
		if ("game".equals(req.getParameter("kind"))) {
			if (BuyingServlet.BOUGHT_GAME_CERTIFICATES.containsKey(email) && BuyingServlet.BOUGHT_GAME_CERTIFICATES.get(email).equals(certificate)) {
				user.setBought(true);
				BuyingServlet.BOUGHT_GAME_CERTIFICATES.remove(email);
			}
		} else {
			l.info("email has no certificate");
		}
		
		try {
			UserDAO.INSTANCE.register(user);
		} catch (SQLException e) {
			String sqlErrorState = e.getSQLState();
			Utils.proceedSQLError(l, e);
			String errorMessage = "Some troubles occured in database. Please try again.";
			if (sqlErrorState.equals("23505")) {
				errorMessage = "This email is already registered, please use another.";
			}
			req.setAttribute("error", errorMessage);
			getServletContext().getRequestDispatcher("/registration.jsp").forward(req, resp);
			return;
		}
		
		if ("addon".equals(req.getParameter("kind"))) {
			l.info("binding addon");
			if (BuyingServlet.BOUGHT_ADDON_CERTIFICATES.containsKey(email) && BuyingServlet.BOUGHT_ADDON_CERTIFICATES.get(email).equals(certificate)) {
				try {
					l.info(user.getEmail() + " : "  + Long.valueOf(req.getParameter("ad_id")));
					AddonDAO.INSTANCE.bindAddonToUserWithEmail(user.getEmail(), Long.valueOf(req.getParameter("ad_id")));
					l.info("success");
				} catch (SQLException e) {
					Utils.proceedSQLError(l, e);
					resp.sendError(500, "An error acquired during binding the addon to your game. \n"
							+ "Please contact to administration to submit the error.");
				}
				BuyingServlet.BOUGHT_ADDON_CERTIFICATES.remove(email);
			} else {
				l.info("email has no certificate");
			}
		}
		
		try {
			if (remember) {
				String randomUUID = UUID.randomUUID().toString();
				UserDAO.INSTANCE.setUUID(user.getEmail(), randomUUID);
				l.info("For user  " + email + " uuid is stored to db.");
				Cookies.addCookie(req, resp, Cookies.COOKIE_NAME, randomUUID, Cookies.COOKIE_AGE);
				l.info("For user " + email + " uuid cookie is added to the forwarded response.");
			} else {
				UserDAO.INSTANCE.deleteUUID(user.getEmail());
				l.info("For user " + email + " uuid is cleared from the db");
				Cookies.removeCookie(req, resp, Cookies.COOKIE_NAME);
				l.info("For user " + email + " uuid cookie is removed i.e. set to 0 age in forwarded response");
			}
		} catch (SQLException e) {
			Utils.proceedSQLError(l, e);
			req.setAttribute("error", "Remember me function is failed. Please try again.");
			getServletContext().getRequestDispatcher("/registration.jsp").forward(req, resp);
			return;
		}
		//topologiguides.com
		resp.sendRedirect(req.getContextPath() + "/profile");
	}

}
