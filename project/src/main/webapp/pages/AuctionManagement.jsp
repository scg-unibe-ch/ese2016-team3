<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<c:import url="template/header.jsp" />

<ol class="breadcrumb">
	<li><a href="/${pagemode}/">Homepage</a></li>
	<li class="active">Manage auctions</li>
</ol>

<div class="row">
	<div class="col-md-12 col-xs-12">
		<h3>Manage auctions</h3>
	</div>
</div>

<div class="panel-group" id="accordion">
	<div class="panel panel-default">
		<div class="panel-heading">
			<h4 class="panel-title">
				<a role="button" data-toggle="collapse" 
					href="#collapseRunning" aria-expanded="true"
					aria-controls="collapseRunning"> Running auctions</a>
			</h4>
		</div>
		<div class="panel-collapse collapse in" id="collapseRunning">
			<div class="panel-body table-responsive">
				<c:choose>
					<c:when test="${empty runningAuctions}">
						<p>No running auctions</p>
					</c:when>
					<c:otherwise>
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
								<c:forEach var="ad" items="${runningAuctions}">
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

	<div class="panel panel-default">
		<div class="panel-heading">
			<h4 class="panel-title">
				<a role="button" data-toggle="collapse" 
					href="#collapseExpired" aria-expanded="true"
					aria-controls="collapseExpired">Expired auctions</a>
			</h4>
		</div>
		<div class="panel-collapse collapse" id="collapseExpired">
			<div class="panel-body table-responsive">
				<c:choose>
					<c:when test="${empty expiredAuctions}">
						<p>No expired auctions</p>
					</c:when>
					<c:otherwise>
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
								<c:forEach var="ad" items="${expiredAuctions}">
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

	<div class="panel panel-default">
		<div class="panel-heading">
			<h4 class="panel-title">
				<a role="button" data-toggle="collapse"
					href="#collapsePaused" aria-expanded="true"
					aria-controls="collapsePaused">Paused auctions</a>
			</h4>
		</div>
		<div class="panel-collapse collapse" id="collapsePaused">
			<div class="panel-body table-responsive">
				<c:choose>
					<c:when test="${empty stoppedAuctions}">
						<p>No paused auctions</p>
					</c:when>
					<c:otherwise>
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

	<div class="panel panel-default">
		<div class="panel-heading">
			<h4 class="panel-title">
				<a role="button" data-toggle="collapse" 
					href="#collapseCompleted" aria-expanded="true"
					aria-controls="collapseCompleted">Completed auctions</a>
			</h4>
		</div>
		<div class="panel-collapse collapse" id="collapseCompleted">
			<div class="panel-body table-responsive">
				<c:choose>
					<c:when test="${empty completedAuctions}">
						<p>No completed auctions</p>
					</c:when>
					<c:otherwise>
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