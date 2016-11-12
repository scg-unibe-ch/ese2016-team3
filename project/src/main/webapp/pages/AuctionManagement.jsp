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

<div class="row">
	<div class="col-sm-12">
		<h4>Running auctions</h4>
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
<div class="row">
	<div class="col-sm-12">
		<h4>Expired auctions</h4>
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
<div class="row">
	<div class="col-sm-12">
		<h4>Paused auctions</h4>
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
<div class="row">
	<div class="col-sm-12">
		<h4>Completed auctions</h4>
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