<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<c:import url="template/header.jsp" />

<script type="text/javascript">
	$(document).ready(function(){
		$('#accordion').on('hidden.bs.collapse', toggleChevron);
		$('#accordion').on('shown.bs.collapse', toggleChevron);
	});
	
	toggleChevron = function(e){
		$(e.target)
        .prev('.panel-heading')
        .find("span.indicator")
        .toggleClass('glyphicon-chevron-down glyphicon-chevron-right');
    }
</script>

<ol class="breadcrumb">
	<li><a href="/${pagemode}/">Homepage</a></li>
	<li class="active">Manage Auctions</li>
</ol>

<div class="row">
	<div class="col-md-12 col-xs-12">
		<h3>Manage Auctions</h3>
	</div>
</div>

<div class="panel-group" id="accordion">

	<!-- Running auctions -->
	<div class="panel panel-default">
		<div class="panel-heading">
			<h4 class="panel-title">
				<a role="button" data-toggle="collapse" href="#collapseRunning"
					aria-expanded="true" aria-controls="collapseRunning">Running
					auctions</a>
				<span class="pull-right indicator glyphicon glyphicon-chevron-down"></span>
			</h4>
		</div>
		<div class="panel-collapse collapse in" id="collapseRunning">
			<div class="panel-body table-responsive">
				<c:choose>
					<c:when test="${empty runningAuctions}">
						<p>No Running Auctions</p>
					</c:when>
					<c:otherwise>
						<table class="table table-striped">
							<thead>
								<tr>
									<th>Ad</th>
									<th>Address</th>
									<th>Start Date</th>
									<th>End Date</th>
									<th>Bids</th>
									<th>Purchase Requests</th>
									<th></th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="ad" items="${runningAuctions}">
									<tr>
										<td><a href="/${pagemode}/ad?id=${ad.id}">${ad.title}</a></td>
										<td>${ad.street},&nbsp;${ad.zipcode}&nbsp;${ad.city}</td>
										<td>${ad.startDate}</td>
										<td>${ad.endDate}</td>
										<td>${ad.bids.size() }</td>
										<td>${ad.purchaseRequests.size() }</td>
										<td><a href="/${pagemode}/profile/auction?id=${ad.id}">Auction
												Details</a></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
	</div>

	<!-- Not yet started auctions -->
	<div class="panel panel-default">
		<div class="panel-heading">
			<h4 class="panel-title">
				<a role="button" data-toggle="collapse"
					href="#collapseNotYetRunning" aria-expanded="true"
					aria-controls="collapseNotYetRunning">Not yet started auctions</a>
				<span class="pull-right indicator glyphicon glyphicon-chevron-right"></span>
			</h4>
		</div>
		<div class="panel-collapse collapse" id="collapseNotYetRunning">
			<div class="panel-body table-responsive">
				<c:choose>
					<c:when test="${empty notStartedAuctions}">
						<p>No not yet started auctions</p>
					</c:when>
					<c:otherwise>
						<p>The following auctions will start running soon.</p>
						<table class="table table-striped">
							<thead>
								<tr>
									<th>Ad</th>
									<th>Address</th>
									<th>Start Date</th>
									<th>End Date</th>
									<th>Bids</th>
									<th>Purchase Requests</th>
									<th></th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="ad" items="${notStartedAuctions}">
									<tr>
										<td><a href="/${pagemode}/ad?id=${ad.id}">${ad.title}</a></td>
										<td>${ad.street},&nbsp;${ad.zipcode}&nbsp;${ad.city}</td>
										<td>${ad.startDate}</td>
										<td>${ad.endDate}</td>
										<td>${ad.bids.size() }</td>
										<td>${ad.purchaseRequests.size() }</td>
										<td><a href="/${pagemode}/profile/auction?id=${ad.id}">Auction
												Details</a></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
	</div>

	<!-- Expired auctions -->
	<div class="panel panel-default">
		<div class="panel-heading">
			<h4 class="panel-title">
				<a role="button" data-toggle="collapse" href="#collapseExpired"
					aria-expanded="true" aria-controls="collapseExpired">Expired
					Auctions</a>
				<span class="pull-right indicator glyphicon glyphicon-chevron-right"></span>
			</h4>
		</div>
		<div class="panel-collapse collapse" id="collapseExpired">
			<div class="panel-body table-responsive">
				<c:choose>
					<c:when test="${empty expiredAuctions}">
						<p>No expired auctions</p>
					</c:when>
					<c:otherwise>
						<p>The following auctions have expired. Please review them and
							contact the auction winners. Please complete the auction afterwards.</p>
						<table class="table table-striped">
							<thead>
								<tr>
									<th>Ad</th>
									<th>Address</th>
									<th>Start Date</th>
									<th>End Date</th>
									<th>Bids</th>
									<th>Purchase Requests</th>
									<th></th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="ad" items="${expiredAuctions}">
									<tr>
										<td><a href="/${pagemode}/ad?id=${ad.id}">${ad.title}</a></td>
										<td>${ad.street},&nbsp;${ad.zipcode}&nbsp;${ad.city}</td>
										<td>${ad.startDate}</td>
										<td>${ad.endDate}</td>
										<td>${ad.bids.size() }</td>
										<td>${ad.purchaseRequests.size() }</td>
										<td><a href="/${pagemode}/profile/auction?id=${ad.id}">Auction
												Details</a></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
	</div>

	<!-- Paused auctions -->
	<div class="panel panel-default">
		<div class="panel-heading">
			<h4 class="panel-title">
				<a role="button" data-toggle="collapse" href="#collapsePaused"
					aria-expanded="true" aria-controls="collapsePaused">Paused
					auctions</a>
				<span class="pull-right indicator glyphicon glyphicon-chevron-right"></span>
			</h4>
		</div>
		<div class="panel-collapse collapse" id="collapsePaused">
			<div class="panel-body table-responsive">
				<c:choose>
					<c:when test="${empty stoppedAuctions}">
						<p>No paused auctions</p>
					</c:when>
					<c:otherwise>
						<p>The following auctions have been stopped. Now you have two options:</p>
						<ol>
							<li>Contact the winner or purchaser and complete the auction afterwards</li>
							<li>Resume the auction</li>
						</ol>
						<table class="table table-striped">
							<thead>
								<tr>
									<th>Ad</th>
									<th>Address</th>
									<th>Start date</th>
									<th>End date</th>
									<th>Bids</th>
									<th>Purchase requests</th>
									<th></th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="ad" items="${stoppedAuctions}">
									<tr>
										<td><a href="/${pagemode}/ad?id=${ad.id}">${ad.title}</a></td>
										<td>${ad.street},&nbsp;${ad.zipcode}&nbsp;${ad.city}</td>
										<td>${ad.startDate}</td>
										<td>${ad.endDate}</td>
										<td>${ad.bids.size() }</td>
										<td>${ad.purchaseRequests.size() }</td>
										<td><a href="/${pagemode}/profile/auction?id=${ad.id}">Auction
												details</a></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
	</div>

	<!-- Completed auctions -->
	<div class="panel panel-default">
		<div class="panel-heading">
			<h4 class="panel-title">
				<a role="button" data-toggle="collapse" href="#collapseCompleted"
					aria-expanded="true" aria-controls="collapseCompleted">Completed
					auctions</a>
				<span class="pull-right indicator glyphicon glyphicon-chevron-right"></span>
			</h4>
		</div>
		<div class="panel-collapse collapse" id="collapseCompleted">
			<div class="panel-body table-responsive">
				<c:choose>
					<c:when test="${empty completedAuctions}">
						<p>No completed auctions</p>
					</c:when>
					<c:otherwise>
						<p>The following auctions have been completed</p>
						<table class="table table-striped">
							<thead>
								<tr>
									<th>Ad</th>
									<th>Address</th>
									<th>Start date</th>
									<th>End date</th>
									<th>Bids</th>
									<th>Purchase requests</th>
									<th></th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="ad" items="${completedAuctions}">
									<tr>
										<td><a href="/${pagemode}/ad?id=${ad.id}">${ad.title}</a></td>
										<td>${ad.street},&nbsp;${ad.zipcode}&nbsp;${ad.city}</td>
										<td>${ad.startDate}</td>
										<td>${ad.endDate}</td>
										<td>${ad.bids.size() }</td>
										<td>${ad.purchaseRequests.size() }</td>
										<td><a href="/${pagemode}/profile/auction?id=${ad.id}">Auction
												details</a></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
	</div>
</div>

<c:import url="template/footer.jsp" />