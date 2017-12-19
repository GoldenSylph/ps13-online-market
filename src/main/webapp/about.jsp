<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>
<div class="jumbotron ps13-jumbotron-0">
	<h1 class="display-3">First information block</h1>
	<p class="lead">Lorem ipsum dolor sit amet, consectetur adipiscing
		elit, sed do eiusmod tempor incididunt ut labore et dolore magna
		aliqua!</p>
	<hr class="my-4">
	<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do
		eiusmod tempor incididunt ut labore et dolore magna aliqua.</p>
	<p class="lead">
		<a class="btn btn-danger btn-lg" href="#" role="button">Purchase!</a>
	</p>
	<hr class="my-4">
	<c:if test="${ not empty requestScope.error }">
		<p class="lead text-danger">
			<c:out value="${requestScope.error}" />
		</p>
	</c:if>
	<c:if test="${ not empty requestScope.stat_dto }">
		<div class="container">
			<div class="row">
				<div class="col-sm">
					<p class="lead">Here is some statistics:</p>
					<p class="lead">
						Resources found:
						<c:out value="${ requestScope.stat_dto.resourcesFound }" />
					</p>
					<p class="lead">
						Resources sold:
						<c:out value="${ requestScope.stat_dto.resourcesSold }" />
					</p>
					<p class="lead">
						Kryobot Stations built:
						<c:out value="${ requestScope.stat_dto.numberOfKryoStationsStands }" />
					</p>
					<p class="lead">
						Hydrobots sold:
						<c:out value="${ requestScope.stat_dto.numberOfHydrobotSold }" />
					</p>
					<p class="lead">
						Kryobots sold:
						<c:out value="${ requestScope.stat_dto.numberOfKryobotSold }" />
					</p>
					<p class="lead">
						Players playing:
						<c:out value="${ requestScope.stat_dto.numberOfPlayersPlaying }" />
					</p>
					<p class="lead">
						Players registered:
						<c:out value="${ requestScope.stat_dto.numberOfPlayers }" />
					</p>
					<p class="lead">
						And
						<c:out value="${ requestScope.stat_dto.numberOfPlayersBought }" />
						players bought the game.
					</p>
				</div>
				<div class="col-sm" align="center">
					<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.7.1/Chart.min.js"></script>
					<canvas id="myChart" width="100%" height="80%"></canvas>
					<script>
					var ctx = document.getElementById("myChart").getContext('2d');
					var myChart = new Chart(ctx, {
					    type: 'polarArea',
					    data: {
					        labels: ["Resources found", "Resources sold", "Kryobot Stations built", 
					        	"Hydrobots sold", "Kryobots sold", "Players playing now", "Players",
					        	"Players who bought the game"],
					        datasets: [{
					            label: 'Game Statistics',
					            data: [
					            	<c:out value="${ requestScope.stat_dto.resourcesFound }" />, 
					            	<c:out value="${ requestScope.stat_dto.resourcesSold }" />, 
					            	<c:out value="${ requestScope.stat_dto.numberOfKryoStationsStands }" />, 
					            	<c:out value="${ requestScope.stat_dto.numberOfHydrobotSold }" />,
					            	<c:out value="${ requestScope.stat_dto.numberOfKryobotSold }" />,
					            	<c:out value="${ requestScope.stat_dto.numberOfPlayersPlaying }" />,
					            	<c:out value="${ requestScope.stat_dto.numberOfPlayers }" />,
					            	<c:out value="${ requestScope.stat_dto.numberOfPlayersBought }" />],
					            backgroundColor: [
					                'rgba(255, 99, 132, 0.2)',
					                'rgba(54, 162, 235, 0.2)',
					                'rgba(255, 206, 86, 0.2)',
					                'rgba(75, 192, 192, 0.2)',
					                'rgba(153, 102, 255, 0.2)',
					                'rgba(255, 159, 64, 0.2)',
					                'rgba(54, 162, 235, 0.2)',
					                'rgba(75, 192, 192, 0.2)'
					            ],
					            borderColor: [
					                'rgba(255,99,132,1)',
					                'rgba(54, 162, 235, 1)',
					                'rgba(255, 206, 86, 1)',
					                'rgba(75, 192, 192, 1)',
					                'rgba(153, 102, 255, 1)',
					                'rgba(255, 159, 64, 1)',
					                'rgba(255,99,132,1)',
					                'rgba(75, 192, 192, 1)'
					            ],
					            borderWidth: 1
					        }]
					    },
					    options: {
					        scales: {
					            yAxes: [{
					                ticks: {
					                    beginAtZero:true
					                }
					            }]
					        }
					    }
					});
					</script>
				</div>
			</div>
		</div>
	</c:if>
</div>