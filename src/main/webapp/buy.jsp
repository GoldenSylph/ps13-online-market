<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>
<script src="https://checkout.stripe.com/checkout.js"></script>
<div class="jumbotron ps13-jumbotron-0">
	<h1 class="display-3">Buy the game</h1>
	<c:if test="${ not empty requestScope.error }">
		<p class="lead text-danger">
			<c:out value="${requestScope.error}" />
		</p>
	</c:if>
	<p class="lead">Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua!</p> 
	<form action="<c:url value="/buy"/>" method="POST">
		<script
		  src="https://checkout.stripe.com/checkout.js" class="stripe-button"
		  data-key="pk_test_fkUohm8lxIlqn81IXbMEe8Te"
		  data-amount="342"
		  data-name="PS13 Market"
		  data-label="Buy the Polar Station 13"
		  data-description="Get the Polar Station 13 now!"
		  data-zip-code="true"
		  data-allow-remember-me="true"
		  data-panel-label="Pay {{amount}}"
		  data-image="<c:out value="${pageContext.request.contextPath}"/>/assets/img/ps13_logo.png"
		  data-email="<c:if test="${not empty requestScope.user}"><c:out value="${requestScope.user.email}"/></c:if>"
		  data-locale="auto">
		</script>
	</form>
	<hr class="my-4"> 
	<h1 class="display-3">Or take a look to the addons!</h1>
	<c:choose>
		<c:when test="${empty addons.set}">
			<p class="lead text-success">You have every addon which can exists connected.</p>
		</c:when>
		<c:otherwise>
			<p class="lead">Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua!</p>
		</c:otherwise>
	</c:choose>
	<c:if test="${not empty addons.set}">
		<c:forEach var="addon" items="${addons.set}" varStatus="loop">
			<div class="jumbotron ps13-jumbotron-1">
				<div class="container">
					<h4 class="display-5">№<c:out value="${loop.index}"/> - <c:out value="${addon.name}"/></h4>
					<p class="lead"><c:out value="${addon.description}"/></p>
					<c:choose>
						<c:when test="${addon.enabled == false}">
							<form action="<c:url value="/buy"/>" method="POST">
								<script
								  src="https://checkout.stripe.com/checkout.js" class="stripe-button"
								  data-key="pk_test_fkUohm8lxIlqn81IXbMEe8Te"
								  data-amount="<c:out value="${addon.cost}"/>"
								  data-name="PS13 Market"
								  data-label="Activate"
								  data-description="Activate the <c:out value="${addon.name}"/> addon now!"
								  data-zip-code="true"
								  data-allow-remember-me="true"
								  data-panel-label="Pay {{amount}}"
								  data-image="<c:out value="${pageContext.request.contextPath}"/>/assets/img/ps13_logo.png"
								  data-email="<c:if test="${not empty requestScope.user}"><c:out value="${requestScope.user.email}"/></c:if>"
								  data-locale="auto">
								</script>
								<input name="addon_id_to_buy" type="hidden" value="<c:out value="${addon.id}"/>">
							</form>
						</c:when>
						<c:otherwise>
							<p class="lead">This addon is active in your account!</p>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
		</c:forEach>
	</c:if>
</div>