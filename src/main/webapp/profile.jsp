<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<%@ taglib uri="/WEB-INF/logout.tld" prefix="logout"%>
<div class="jumbotron ps13-jumbotron-0">
  <h1 class="display-3">Welcome to your profile!</h1> 
  <p class="lead">
 	Here you can change your email and check your addons and license.
  </p> 
  <p class="lead 
  	<c:choose>
  		<c:when test="${requestScope.user_bought}"> text-success </c:when>
  		<c:otherwise> text-danger </c:otherwise>
  	</c:choose>
  ">
  	<c:choose>
  		<c:when test="${requestScope.user_bought}"> You are bought the game! </c:when>
  		<c:otherwise> You aren't bought the game. </c:otherwise>
  	</c:choose>
  </p>
  <hr class="my-4"> 
  <c:if test="${ not empty requestScope.error }">
	<p class="lead text-danger">
		<c:out value="${requestScope.error}"/>
	</p>
  </c:if>
  <div class="container">
  	 <div class="row">
	  	 <div class="col-sm">
		  	 <form id="change-email-form" action="<c:url value="/profile"/>" method="POST">
			  	<div class="form-group">
			  		<label id="emailLabel" for="emailInput">Your email: <c:out value="${requestScope.user_email}"/></label>
			  		<input name="email" type="email" class="form-control" id="emailInput" aria-describedby="emailHelp" placeholder="example@ex.com" required autofocus>
			  		<small id="emailHelp" class="form-text text-muted">If you don't want to change your can just don't click on the button below.</small>
			  	</div>
				<button class="btn btn-primary btn-lg ps13-shadow" type="submit" role="button">Change the email</button>
			  </form>
	  	 </div>
		 <div class="col-sm">
		 	<form id="change-password-form" action="<c:url value="/swpass"/>" method="POST">
			  	<div class="form-group">
			  		<label id="oldPasswordLabel" for="oldPasswordInput">Your old password:</label>
			  		<input name="oldPassword" type="password" class="form-control" id="oldPasswordInput" placeholder="Old password, man, respect it." required autofocus>
			  		<label id="newPasswordLabel" for="newPasswordInput">Your new password:</label>
			  		<input name="newPassword" type="password" class="form-control" id="newPasswordInput" placeholder="Some new and hard to guess password" required autofocus>
			  		<input name="email" type="hidden" value="<c:out value="${requestScope.user_email}"/>">
			  	</div>
				<button class="btn btn-success btn-lg ps13-shadow-green" type="submit" role="button">Change the password</button>
			</form>
		 </div>
	  </div>
  </div>
  <hr class="my-4">
  <p class="lead">
  	<c:choose>
  		<c:when test="${not empty addons.set}">
  			Your purchased addons:
  		</c:when>
  		<c:otherwise>
  			You don't have any purchased addons.
  		</c:otherwise>
  	</c:choose>
  </p>
  <c:if test="${not empty addons.set}">
  	<table class="table table-hover">
	  <thead>
	    <tr>
	      <th scope="col">№</th>
	      <th scope="col">Addon Name</th>
	      <th scope="col">Addon Description</th>
	      <th scope="col">Activity</th>
	    </tr>
	  </thead>
	  <tbody>
	    <c:forEach var="addon" items="${addons.set}" varStatus="loop">
		    <tr>
		      <th scope="row"><c:out value="${loop.index}"/></th>
		      <td><c:out value="${addon.name}"/></td>
		      <td><c:out value="${addon.description}"/></td>
		      <td>
				<form method="POST" action="<c:url value="/adswitch"/>">
					<c:choose>
						<c:when test="${addon.enabled}">
							<button type="submit" name="addon_switch_command" value="disable" class="btn btn-warning ps13-shadow-warning">Disable</button>
			      		</c:when>
			      		<c:otherwise>
			      			<button type="submit" name="addon_switch_command" value="enable" class="btn btn-success ps13-shadow-green">Enable</button>
			      		</c:otherwise>
		      		</c:choose>
		      		<input type="hidden" name="email" value="<c:out value="${requestScope.user_email}"/>">
		      		<input type="hidden" name="addon_switch_id" value="<c:out value="${addon.id}"/>">  		
	      		</form>
		      </td>
		    </tr>
		</c:forEach>
	  </tbody>
    </table>
  </c:if>
  <hr class="my-4">
  <form action="<c:url value="/logout"/>">
  	<logout:logout />
  </form>
</div>