package com.bedrin.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.log4j.Logger;

public class HomePageFilter implements Filter {

	private static final Logger l = Logger.getLogger(HomePageFilter.class);
	
	public void init(FilterConfig filterConfig) throws ServletException {}
	public void destroy() {}
	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		request.setAttribute("page", "home");
		l.info("Put attribute page - home");
		chain.doFilter(request, response);
	}

	
	
}
