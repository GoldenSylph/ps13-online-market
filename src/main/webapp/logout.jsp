<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<div class="jumbotron ps13-jumbotron-0">
  <h1 class="display-3"><c:out value="${requestScope.logout_state}"/></h1> 
  <hr class="my-4"> 
  <p class="lead">
    <a class="btn btn-success ps13-shadow-green btn-lg" href="<c:url value="/"/>" role="button">Oki-doki</a>
  </p> 
</div>