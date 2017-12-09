<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<!DOCTYPE html>
<html>
  <head>
    <title>PS13 - Login</title>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="author" content="Oleg Bedrin">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link rel="stylesheet" href="<c:out value="${pageContext.request.contextPath}"/>/assets/css/style.css">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/css/bootstrap.min.css" integrity="sha384-PsH8R72JQ3SOdhVi3uxftmaW6Vc51MKb0q5P2rRUpPvrszuE4W1povHYgTpBfshb" crossorigin="anonymous">
  	<script src='https://www.google.com/recaptcha/api.js'></script>
  </head>
  <body style="background-image: url('<c:out value="${pageContext.request.contextPath}"/>/assets/img/body.png');">
	
	<div class="wrapper">
		<div class="jumbotron ps13-jumbotron-1 jumbotron-fluid">
		  <div class="container">
		      <div class="jumbotron ps13-jumbotron-0">
		      	<div class="container">
		      		<form id="login-form" class="form-signin" method="post" action="<c:url value="/login"/>">
				        <h2 class="form-signin-heading">Please sign in</h2>
				        <c:if test="${ not empty requestScope.error }">
							<p class="lead text-danger">
								<c:out value="${requestScope.error}"/>
							</p>
						</c:if>
				        <label for="inputEmail">Email address</label>
				        <input id="email-input" name="j_username" type="email" id="inputEmail" class="form-control" placeholder="Your email" required autofocus>
				        <label for="inputPassword">Password</label>
				        <input name="j_password" type="password" id="inputPassword" class="form-control" placeholder="Your hard to guess password" required>
				        <hr class="my-4"/>
				        <div class="checkbox">
				          <label>
				            <input name="remember_me" type="checkbox" value="on"> Remember me
				          </label>
				        </div>
				        <input type="hidden" name="url" value="
				        <c:choose>
				        	<c:when test="${ not empty param['url'] }">
				        		<c:out value="${param['url']}"/>
				        	</c:when>
				        	<c:when test="${ empty param['url'] }">
				        		<c:out value="${requestScope['javax.servlet.forward.request_uri']}"/>
				        	</c:when>
				        </c:choose>
				        ">
				        <div style="margin-bottom: 15px;" class="g-recaptcha" data-sitekey="6LedYjkUAAAAAFgwbgGopPte9iuYBN54pqm0EAJt"></div>
				        <button class="btn btn-lg btn-primary btn-block ps13-shadow" type="submit">Sign in</button>
				    </form>
				    <hr class="my-4">
				    <p class="lead">
					  <a href="<c:url value="/register"/>" class="btn btn-success ps13-shadow-green btn-lg btn-block">Register</a>
					</p>
		      	</div>
		      </div>
		  </div> 	
		</div>
	</div>

	<footer class="footer">
	  <div class="container">
	    <span class="text-muted">&copy; 2017 Melon Games</span>
	  </div>
	</footer>		
    <!-- Optional JavaScript -->
    <!-- jQuery first, then Popper.js, then Bootstrap JS -->
    <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.3/umd/popper.min.js" integrity="sha384-vFJXuSJphROIrBnz7yo7oB41mKfc8JzQZiCq4NCceLEaO4IHwicKwpJf9c9IpFgh" crossorigin="anonymous"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/js/bootstrap.min.js" integrity="sha384-alpBpkh1PFOepccYVYDB4do5UnbKysX5WZXm3XxPqe5iKTfUKjNkCk9SaVuEZflJ" crossorigin="anonymous"></script>
  	<script type="text/javascript" src="https://cdn.jsdelivr.net/npm/jquery-validation@1.17.0/dist/jquery.validate.min.js"></script>
  	<script type="text/javascript" src="<c:out value="${pageContext.request.contextPath}"/>/assets/js/login-validation.js"></script>
  </body>
</html>