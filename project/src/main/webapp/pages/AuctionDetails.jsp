<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:import url="template/header.jsp" />

<ol class="breadcrumb">
	<li><a href="/${pagemode}/">Homepage</a></li>
	<li><a href="/${pagemode}/profile/auctions">Manage auctions</a></li>
	<li class="active">Auction</li>
</ol>

<div class="row">
	<div class="col-md-12 col-sm-12">
		<h3>Auction details</h3>
	</div>
</div>
<div class="row">
	<div class="col-md-12 col-sm-12">
		<div class="panel panel-default">
			<div class="panel-body">
				<div class="row bottom15">
					<div class="col-xs-3">
						<strong>Ad</strong>
					</div>
					<div class="col-xs-9">
						<a href="/${ad.buyMode.name}/ad?id=${ad.id}">${ad.title},
							${ad.street},&nbsp;${ad.zipcode}&nbsp;${ad.city}</a>
					</div>
				</div>
				<div class="row bottom15">
					<div class="col-sm-3 col-xs-3">
						<strong>Auction start</strong>
					</div>
					<div class="col-sm-3 col-xs-9">${ad.startDate}</div>
					<div class="col-sm-3 col-xs-3">
						<strong>Auction end</strong>
					</div>
					<div class="col-sm-3 col-xs-9">${ad.endDate}</div>
				</div>
				<div class="row">
					<div class="col-sm-3 col-xs-3">
						<strong>Start price</strong>
					</div>
					<div class="col-sm-3 col-xs-9">CHF ${ad.startPrice}.-</div>
					<div class="col-sm-3 col-xs-3">
						<strong>Price increment</strong>
					</div>
					<div class="col-sm-3 col-xs-9">CHF ${ad.increaseBidPrice}.-</div>
				</div>
			</div>
		</div>
		<c:if test="${ad.isAuctionRunning() || ad.hasAuctionExpired() || ad.isAuctionStopped() }">
			<form:form method="Post" cssClass="pull-right"
				action="/${pagemode}/profile/auction/complete">
				<input type="hidden" id="adIdComplete" name="adIdComplete"
					value="${ad.id}" />
				<button class="btn btn-success" type="submit">
					<span class="glyphicon glyphicon-check"></span> Complete auction
				</button>
			</form:form>
		</c:if>
		<c:if test="${ad.isAuctionStopped()}">
			<form:form method="Post" cssClass="pull-right"
				action="/${pagemode}/profile/auction/resume">
				<input type="hidden" id="adIdResume" name="adIdResume"
					value="${ad.id}" />
				<button class="btn btn-default" type="submit">
					<span class="glyphicon glyphicon-repeat"></span> Resume auction
				</button>
			</form:form>
		</c:if>
		<c:if test="${ad.isAuctionRunning() }">
			<form:form method="Post" cssClass="pull-right"
				action="/${pagemode}/profile/auction/pause">
				<input type="hidden" id="adIdPause" name="adIdPause"
					value="${ad.id}" />
				<button class="btn btn-default" type="submit">
					<span class="glyphicon glyphicon-pause"></span> Pause auction
				</button>
			</form:form>
		</c:if>
	</div>
</div>
<div class="row">
	<div class="col-sm-12 col-md-6">
		<h4>Bids</h4>
		<c:choose>
			<c:when test="${empty bids}">
				<p>No bids</p>
			</c:when>
			<c:otherwise>
				<table class="table table-striped">
					<thead>
						<tr>
							<th>Bidder</th>
							<th>Amount</th>
							<th>Date</th>
						<tr>
					</thead>
					<tbody>
						<c:forEach var="bid" items="${bids}" varStatus="loop">
							<tr class="${loop.index == 0 ? 'success text-success' : '' }">
								<td>${bid.bidder.firstName}&nbsp;${bid.bidder.lastName}</td>
								<td>${bid.amount}</td>
								<td><fmt:formatDate value="${bid.timeStamp}"
										var="formattedDate" type="date" pattern="yyyy-MM-dd HH:mm:ss" />
									${formattedDate}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</c:otherwise>
		</c:choose>
	</div>
	<div class="col-sm-12 col-md-6">
		<h4>Purchase requests</h4>
		<c:choose>
			<c:when test="${empty purchaseRequests}">
				<p>No purchase Requests</p>
			</c:when>
			<c:otherwise>
				<table class="table table-striped">
					<thead>
						<tr>
							<th>Purchaser</th>
							<th>Date</th>
						<tr>
					</thead>
					<tbody>
						<c:forEach var="request" items="${purchaseRequests}"
							varStatus="loop">
							<tr class="${loop.index == 0 ? 'success text-success' : '' }">
								<td>${request.purchaser.firstName}&nbsp;${request.purchaser.lastName}</td>
								<td><fmt:formatDate value="${request.created}"
										var="formattedDate" type="date" pattern="yyyy-MM-dd HH:mm:ss" />
									${formattedDate}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</c:otherwise>
		</c:choose>
	</div>
</div>