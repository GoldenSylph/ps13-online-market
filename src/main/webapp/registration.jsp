<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<!DOCTYPE html>
<html>
  <head>
    <title>PS13 - Registration</title>
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
		<div class="jumbotron jumbotron-fluid ps13-jumbotron-1">
			<div class="container">
				<div class="jumbotron ps13-jumbotron-0">
					<div class="container">
				      <form id="register-form" class="form-signin" action="<c:url value="/register"/>" method="POST">
				        <h2 class="form-signin-heading">Please sign up</h2>
				        <c:if test="${ not empty requestScope.error }">
							<p class="lead text-danger">
								<c:out value="${requestScope.error}"/>
							</p>
						</c:if>
				        <label for="inputEmail">Email address</label>
				        <input name="email" type="email" id="inputEmail" class="form-control" 
				        	placeholder="example@domen.com" required autofocus value="
				        		<c:if test="${not empty requestScope.cert and not empty requestScope.provided_email}">
				        			<c:out value="${requestScope.provided_email}"/>
				        		</c:if>
				        	">
				        <label for="inputPassword">Password</label>
				        <input name="password" type="password" id="inputPassword" class="form-control" placeholder="Some hard to guess password" required>
				        <label for="repeatPassword">Repeat password</label>
				        <input name="repassword" type="password" id="repeatPassword" class="form-control" placeholder="Repeat your hard to guess password" required>
				        <div class="checkbox">
				          <label>
				            <input type="checkbox" name="remember_me" value="on"> Remember me
				          </label>
				        </div>
				        <div style="margin-bottom: 5px;" class="g-recaptcha" data-sitekey="6LedYjkUAAAAAFgwbgGopPte9iuYBN54pqm0EAJt"></div>
				        <button class="btn btn-lg btn-primary btn-block ps13-shadow" type="submit">Sign up</button>
				        <c:if test="${not empty requestScope.cert and not empty requestScope.provided_email}">
							<input type="hidden" name="cert" value="${requestScope.cert}">
							<input type="hidden" name="kind" value="${requestScope.kind}">
							<input type="hidden" name="ad_id" value="${requestScope.ad_id}">
		        		</c:if>
				      </form>
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
  	<script type="text/javascript" src="<c:out value="${pageContext.request.contextPath}"/>/assets/js/register-validation.js"></script>
  	
  </body>
</html>