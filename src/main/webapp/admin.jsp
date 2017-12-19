<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<div class="jumbotron ps13-jumbotron-0">
  	<h1>Dashboard</h1>
    <h2>Market statistics</h2>
    <div class="table-responsive">
      <table class="table table-striped">
        <thead>
          <tr>
            <th>#</th>
            <th>Description</th>
            <th>Value</th>
          </tr>
        </thead>
        <tbody>
          <c:choose>
          	<c:when test="${ not empty market_data_dto }">
          		<c:forEach var="data_unit" items="${market_data_dto.set}" varStatus="loop">
					<tr>
					  <td><c:out value="${loop.index}"/></td>
					  <td><c:out value="${data_unit.description}"/></td>
					  <td><c:out value="${data_unit.value}"/>%</td>
					</tr>
          		</c:forEach>
          	</c:when>
          	<c:otherwise>
          		There is no market data available now. Please reload the page.
          	</c:otherwise>
          </c:choose>
        </tbody>
      </table>
    </div>
</div>