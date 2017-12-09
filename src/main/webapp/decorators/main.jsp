<!DOCTYPE html>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator"
           prefix="decorator"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/logout.tld" prefix="logout"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>


<html>
  <head>
    <title><decorator:title/></title>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="author" content="Oleg Bedrin">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link rel="stylesheet" type="text/css" href="<c:out value="${pageContext.request.contextPath}"/>/assets/css/style.css">
    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/css/bootstrap.min.css" integrity="sha384-PsH8R72JQ3SOdhVi3uxftmaW6Vc51MKb0q5P2rRUpPvrszuE4W1povHYgTpBfshb" crossorigin="anonymous">
  </head>
  <body style="background-image: url('<c:out value="${pageContext.request.contextPath}"/>/assets/img/body.png');">
    <script type="text/javascript" src="<c:out value="${pageContext.request.contextPath}"/>/assets/js/jquery.min.js"></script>
    <script type="text/javascript" src="https://cdn.jsdelivr.net/npm/jquery-validation@1.17.0/dist/jquery.validate.min.js"></script>
  	<c:if test="${requestScope.page == 'profile'}">
	  <script src="http://malsup.github.com/jquery.form.js"></script>
  	</c:if>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.3/umd/popper.min.js" integrity="sha384-vFJXuSJphROIrBnz7yo7oB41mKfc8JzQZiCq4NCceLEaO4IHwicKwpJf9c9IpFgh" crossorigin="anonymous"></script>
  	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/js/bootstrap.min.js" integrity="sha384-alpBpkh1PFOepccYVYDB4do5UnbKysX5WZXm3XxPqe5iKTfUKjNkCk9SaVuEZflJ" crossorigin="anonymous"></script>
	<c:if test="${requestScope.page == 'profile'}">
	  <script type="text/javascript" src="<c:out value="${pageContext.request.contextPath}"/>/assets/js/profile-validation.js"></script>
	</c:if>
  	
  	<nav class="navbar navbar-expand-lg navbar-dark ps13-text-shadow ps13-shadow" style="background-image: url('<c:out value="${pageContext.request.contextPath}"/>/assets/img/head.png');">
	  <a class="navbar-brand" href="<c:url value="/"/>">
	    <img src="<c:out value="${pageContext.request.contextPath}"/>/assets/img/ps13_logo.png" width="30" height="30" class="d-inline-block align-top" alt="">
	    Polar Station 13
	  </a>
	  <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
	    <span class="navbar-toggler-icon"></span>
	  </button>
	
	  <div class="collapse navbar-collapse" id="navbarSupportedContent">
	    <ul class="navbar-nav mr-auto">
	      <li class="nav-item active">
	        <a class="nav-link
	        <c:if test="${requestScope.page == 'home'}">
	        	text-uppercase
	        </c:if>" href="<c:url value="/"/>">Home <span class="sr-only">(current)</span></a>
	      </li>
	      <li class="nav-item active">
	        <a class="nav-link
	        <c:if test="${requestScope.page == 'about'}">
	        	text-uppercase
	        </c:if>" href="<c:url value="/about"/>">About</a>
	      </li>
	      <li class="nav-item active">
	        <a class="nav-link
	        <c:if test="${requestScope.page == 'bug'}">
	        	text-uppercase
	        </c:if>" href="<c:url value="/bugs"/>">Bug Tracker</a>
	      </li>
	      <li class="nav-item active">
	        <a class="nav-link
	        <c:if test="${requestScope.page == 'buy'}">
	        	text-uppercase
	        </c:if>" href="<c:url value="/buy"/>">Purchase</a>
	      </li>
	      <li class="nav-item active">
	        <a class="nav-link" href="#">Community</a>
	      </li>
	    </ul>
	    <form action="<c:url value="/profile"/>" class="form-inline my-2 my-lg-0" method="GET">
	      <button class="btn btn-primary navbar-btn my-2 my-sm-0 ps13-box-shadow 
	      <c:if test="${requestScope.page == 'profile'}">
	      	active
	      </c:if>" type="submit">Profile</button>
	    </form>
	  </div>
	</nav>
	<br>
	<div class="container">
	  <div class="wrapper">
	    <!-- Content goes here -->
	    <decorator:body/>
	  </div>
	</div>
	<footer class="footer">
	  <div class="container">
	    <span class="text-muted">&copy; 2017 Melon Games</span>
	  </div>
	</footer>
  </body>
</html>