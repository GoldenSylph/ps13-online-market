<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<div class="jumbotron ps13-jumbotron-0">
  <h1 class="display-3">Found a bug?</h1> 
  <p class="lead">
  	So your game is crashed, or you found an error and everything is glitching?
  </p> 
  <hr class="my-4"> 
  <p>You can help us to develop the game! We would be very grateful if you 
  		contact us through this form about all the bugs in the game.
  </p>
  <form enctype="multipart/form-data" action="<c:url value="/bugs"/>" method="POST">
  	<c:if test="${ not empty requestScope.error }">
		<p class="lead text-danger">
			<c:out value="${requestScope.error}"/>
		</p>
	</c:if>
  	<div class="form-group">
  		<label for="emailInput">Please enter your contact email</label>
  		<input name="email" type="email" class="form-control" id="emailInput" aria-describedby="emailHelp" placeholder="example@ex.com" required autofocus>
  		<small id="emailHelp" class="form-text text-muted">We'll never share your email with anyone else.</small>
  	</div>
  	<div class="form-group">
	    <label for="unity-log-file">Include the log file of game</label>
	    <input name="unity_log_file" type="file" class="form-control-file" id="unity-log-file" required>
	    <small id="emailHelp" class="form-text text-muted">Windows: C:\Users\username\AppData\LocalLow\CompanyName\ProductName\output_log.txt <br>
	    Linux: ~/.config/unity3d/CompanyName/ProductName/Player.log <br>
	    Mac OS: ~/Library/Logs/Unity/Player.log
	    </small>
	</div>
  	<div class="form-group">
	    <label for="bug-description">Then, describe the bug</label>
	    <textarea name="bug_description" class="form-control" id="bug-description" rows="3" required></textarea>
	</div>
	<script src='https://www.google.com/recaptcha/api.js'></script>
	<div class="g-recaptcha form-group" data-sitekey="6LedYjkUAAAAAFgwbgGopPte9iuYBN54pqm0EAJt"></div>
	<button class="btn btn-success btn-lg" type="submit" role="button">Submit a bug</button>
  </form>
  
</div>