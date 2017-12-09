package com.bedrin.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.bedrin.models.pojo.Bug;
import com.bedrin.utils.Utils;
import com.bedrin.utils.dao.BugDAO;
import com.bedrin.utils.dao.UserDAO;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@WebServlet("/bugs")
@MultipartConfig
public class BugTrackerServlet extends HttpServlet {

	private static final long serialVersionUID = -4691296167180039196L;
	private static final Logger l = Logger.getLogger(BugTrackerServlet.class);
	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setAttribute("page", "bug");
		getServletContext().getRequestDispatcher("/bugs.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost("https://www.google.com/recaptcha/api/siteverify");
		List<NameValuePair> arguments = new ArrayList<>();
		arguments.add(new BasicNameValuePair("secret", "6LedYjkUAAAAAAqUESIc9qslL2gVT0vltEUeztlA"));
		arguments.add(new BasicNameValuePair("response", getValue(req.getPart("g-recaptcha-response"))));
		try {
            post.setEntity(new UrlEncodedFormEntity(arguments));
            HttpResponse response = client.execute(post);
            JsonElement je = new JsonParser().parse(EntityUtils.toString(response.getEntity()));
            JsonObject jo = je.getAsJsonObject();
            boolean success = jo.get("success").getAsBoolean();
            if (!success) {
            	req.setAttribute("error", "Robots are not welcome.");
            	req.setAttribute("page", "bug");
    			getServletContext().getRequestDispatcher("/bugs.jsp").forward(req, resp);
    			return;
            }
        } catch (IOException e) {
            e.printStackTrace();
            req.setAttribute("error", "Robot checking is failed. Please try again.");
            req.setAttribute("page", "bug");
            getServletContext().getRequestDispatcher("/bugs.jsp").forward(req, resp);
            return;
        } 
		
		Bug bug = new Bug();
		
		String email = getValue(req.getPart("email"));
		String bugDescription = getValue(req.getPart("bug_description"));
		String unityLogFile = getValue(req.getPart("unity_log_file"));
		try {
			if (!UserDAO.INSTANCE.ifEmailRegistered(email)) {
				req.setAttribute("error", "This email is not registered. Please register your email.");
		        req.setAttribute("page", "bug");
		        getServletContext().getRequestDispatcher("/bugs.jsp").forward(req, resp);
			} else {
				bug.setEmail(email);
			}
		} catch (SQLException e) {
			Utils.proceedSQLError(l, e);
			req.setAttribute("error", "There is some trouble in database with email checking. Please try again.");
	        req.setAttribute("page", "bug");
	        getServletContext().getRequestDispatcher("/bugs.jsp").forward(req, resp);
            return;
		}
		
		bug.setLogFile(unityLogFile);
		
		if (bugDescription.isEmpty()) {
			req.setAttribute("error", "Please type the description of issue.");
	        req.setAttribute("page", "bug");
	        getServletContext().getRequestDispatcher("/bugs.jsp").forward(req, resp);
            return;
		} else {
			bug.setDescription(bugDescription);
		}
		
		try {
			BugDAO.INSTANCE.register(bug);
		} catch (SQLException e) {
			Utils.proceedSQLError(l, e);
			req.setAttribute("error", "There is some trouble in database with bug registering. Please try again.");
	        req.setAttribute("page", "bug");
	        getServletContext().getRequestDispatcher("/bugs.jsp").forward(req, resp);
            return;
		}
		
		req.setAttribute("error", "Thank you for your feedback!");
		req.setAttribute("page", "bug");
		getServletContext().getRequestDispatcher("/bugs.jsp").forward(req, resp);
	}
	
	private static String getValue(Part part) throws IOException {
	    BufferedReader reader = new BufferedReader(new InputStreamReader(part.getInputStream(), "UTF-8"));
	    StringBuilder value = new StringBuilder();
	    char[] buffer = new char[1024];
	    for (int length = 0; (length = reader.read(buffer)) > 0;) {
	        value.append(buffer, 0, length);
	    }
	    return value.toString();
	}
	
}
