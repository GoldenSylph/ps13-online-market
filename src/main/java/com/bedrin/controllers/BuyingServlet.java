package com.bedrin.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.bedrin.models.dto.AddonsSetDTO;
import com.bedrin.models.pojo.Addon;
import com.bedrin.models.pojo.User;
import com.bedrin.utils.Utils;
import com.bedrin.utils.dao.AddonDAO;
import com.bedrin.utils.dao.UserDAO;
import com.stripe.Stripe;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Charge;

@WebServlet("/buy")
public class BuyingServlet extends HttpServlet {

	public static volatile TreeMap<String, String> BOUGHT_GAME_CERTIFICATES = new TreeMap<>();
	public static volatile TreeMap<String, String> BOUGHT_ADDON_CERTIFICATES = new TreeMap<>();
	public static final int COST_OF_THE_GAME_IN_CENTS_USD = 342;
	
	private static final long serialVersionUID = 109551561753520730L;
	private static final Logger l = Logger.getLogger(BuyingServlet.class);
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setAttribute("page", "buy");
		User u = Utils.getCurrentUserFromCookies(req, resp, this, l);
		HttpSession ses = req.getSession(false);
		if (ses != null) {
			Object sesEmail = ses.getAttribute("user_email");
			if (sesEmail != null) {
				if (u.getEmail() == null) {
					try {
						String email = sesEmail.toString();
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
			}
		}
		try {
			if (u.getEmail() != null) {
				l.info("user is logged in, showing context relative addons");
				AddonsSetDTO asd = AddonDAO.INSTANCE.getAllAddonsWithActivitiesOfUser(u);
				req.setAttribute("addons", asd);
				l.info(asd);
			} else {
				l.info("user is not logged in, showing all addons");
				AddonsSetDTO asd = AddonDAO.INSTANCE.getAllAddons();
				req.setAttribute("addons", asd);
				l.info(asd);
			}
		} catch (SQLException e) {
			Utils.proceedSQLError(l, e);
			e.printStackTrace();
			req.setAttribute("error", "Can not access to addons database.");
		}
		req.setAttribute("user", u);
		getServletContext().getRequestDispatcher("/buy.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		Stripe.apiKey = "sk_test_g3vBCnCNexvc6VBqUpd9CZsV";
		
		l.info("Buying the game.");
		
		// Token is created using Checkout or Elements!
		// Get the payment token ID submitted by the form:
		String token = req.getParameter("stripeToken");

		// Charge the user's card:
		Map<String, Object> params = new HashMap<String, Object>();
		int cost = COST_OF_THE_GAME_IN_CENTS_USD;
		String addonID = req.getParameter("addon_id_to_buy");
		boolean trueAddonFalseGame = !(addonID == null || addonID.isEmpty() );
		if (trueAddonFalseGame) {
			Addon t = null;
			try {
				t = AddonDAO.INSTANCE.getAddonWithIDWithoutEnabledOption(Long.valueOf(addonID));
				cost = t.getCost();
			} catch (NumberFormatException e) {
				Utils.proceedError(l, e);
				resp.sendError(500, "Invalid form of addon ID. "
						+ "Please try again.");
				return;
			} catch (SQLException e) {
				Utils.proceedSQLError(l, e);
				resp.sendError(500, "An error acquired in database connection retrieving the cost of addon. "
						+ "Please try again.");
				return;
			}
		}
		params.put("amount", cost);
		params.put("currency", "usd");
		params.put("description", "Purchasing game Polar Station 13");
		params.put("source", token);

		try {
			Charge.create(params);
		} catch (AuthenticationException e) {
			Utils.proceedError(l, e);
			setErrorOfBuying(req);
			resp.sendError(500, "An error acquired during authorization of your personal data. "
					+ "Please try a bit later.");
		} catch (InvalidRequestException e) {
			Utils.proceedError(l, e);
			setErrorOfBuying(req);
			resp.sendError(500, "An error acquired during validation of the purchase request. "
					+ "Please try a bit later.");
			return;
		} catch (APIConnectionException e) {
			Utils.proceedError(l, e);
			setErrorOfBuying(req);
			resp.sendError(500, "An error acquired during connection to the Stripe API. "
					+ "Please try a bit later.");
			return;
		} catch (CardException e) {
			Utils.proceedError(l, e);
			resp.sendError(500, "An error acquired during card processing. "
					+ "Please try a bit later.");
			return;
		} catch (APIException e) {
			Utils.proceedError(l, e);
			setErrorOfBuying(req);
			resp.sendError(500, "An error acquired in Stripe API. "
					+ "Please try a bit later.");
			return;
		}
		
		l.info("purchased updating the database");
		
		String email = req.getParameter("stripeEmail");
		try {
			if (UserDAO.INSTANCE.ifEmailRegistered(email)) {
				l.info("email is registered");
				if (!trueAddonFalseGame) {
					UserDAO.INSTANCE.setUserBoughtOptionViaEmail(email, true);
				} else {
					AddonDAO.INSTANCE.bindAddonToUserWithEmail(email, Long.valueOf(addonID));
				}
			} else {
				l.info("email is not registered");
				String certificate = UUID.randomUUID().toString();
				l.info("created certificate");
				if (!trueAddonFalseGame) {
					BOUGHT_GAME_CERTIFICATES.put(email, certificate);
				} else {
					BOUGHT_ADDON_CERTIFICATES.put(email, certificate);
				}
				l.info("certificate is logged");
				HttpSession ses = req.getSession();
				ses.setAttribute("provided_email", email);
				ses.setAttribute("cert", certificate);
				l.info("email and cert are saved into the session");
				l.info("moving to register servlet");
				resp.sendRedirect(req.getContextPath() + "/register?provided_email=" 
						+ email + "&cert=" + certificate + "&kind=" + (!trueAddonFalseGame ? "game" : "addon&ad_id=" + addonID));
				return;
			}
		} catch (SQLException e) {
			Utils.proceedSQLError(l, e);
			resp.sendError(500, "Error during checking the email in database. \n"
					+ "Please contact to administration to submit the error.");
			return;
		}
		l.info("all is alright moving to profile");
		resp.sendRedirect(req.getContextPath() + "/profile");
	}
	
	private void setErrorOfBuying(HttpServletRequest req) {
		req.setAttribute("user_bought", false);
	}
	
}
