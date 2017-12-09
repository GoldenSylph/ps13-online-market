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
import com.bedrin.utils.dao.UserDAO;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = -8889013342089998973L;
	private static final Logger l = Logger.getLogger(LoginServlet.class);

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
    			getServletContext().getRequestDispatcher("/logon.jsp").forward(req, resp);
    			return;
            }
        } catch (IOException e) {
            e.printStackTrace();
            req.setAttribute("error", "Robot checking is failed. Please try again.");
            getServletContext().getRequestDispatcher("/logon.jsp").forward(req, resp);
            return;
        }
		
		String email = req.getParameter("j_username");
		String password = req.getParameter("j_password");

		if (!email.matches("^[A-Za-z0-9_]+@[a-z0-9]+(?:.[a-z]+[a-z]*)+$")) {
			req.setAttribute("error", "Email must be valid.");
			getServletContext().getRequestDispatcher("/logon.jsp").forward(req, resp);
			return;
		}
		
		l.info("User " + email + " sent the password.");
		boolean remember = "on".equals(req.getParameter("remember_me"));
		l.info("Remember me mode is " + remember + ".");
		try {
			
			User user = new User();
			user.setBought(false);
			try {
				user.setPassword(password);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			user.setEmail(email);
			l.info("Signing in " + user.getEmail());
			req.login(user.getEmail(), user.getPassword());
			l.info("If user succeed with login for email " + email + " the random UUID is generated.");
			String randomUUID = UUID.randomUUID().toString();
			try {
				if (remember) {
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
			}
		} catch (ServletException e) {
			String errorMessage = "Email or password is incorrect, please try again.";
			l.info("Some error occurs for " + email + "; the error message is: " + e.getLocalizedMessage());
			if (!e.getLocalizedMessage().equals("Login failed")) {
				errorMessage = e.getLocalizedMessage();
			}
			req.setAttribute("error", errorMessage);
			getServletContext().getRequestDispatcher("/logon.jsp").forward(req, resp);
			return;
		}
		String redirectTo = req.getParameter("url");
		l.info("For user " + email + " the page is redirected to " + "the initial url user tried to access.");
		redirectTo = redirectTo.trim();
		req.getSession().setAttribute("user_email", email);
		resp.sendRedirect(redirectTo);
	}

}
