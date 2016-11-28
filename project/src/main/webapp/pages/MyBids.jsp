<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:import url="template/header.jsp" />

<ol class="breadcrumb">
	<li><a href="/${pagemode}/">Homepage</a></li>
	<li class="active">My Bids</li>
</ol>

<div class="row bottom15">
	<div class="col-md-12 col-xs-12">
		<h3>My Bids</h3>
		<p>
			See the auctions in which you participated and your bids below. <span
				class="text-success">Green rows</span> mean that your bid is the
			highest bid so far.
		</p>
	</div>
</div>

<c:choose>
	<c:when test="${empty myauctions }">
		<p>You haven't participated in any auctions yet.</p>
	</c:when>
	<c:otherwise>
		<c:forEach var="auction" items="${myauctions}">
			<div class="panel panel-default">
				<div class="panel-heading">
					<a href="/${auction.key.buyMode.name}/ad?id=${auction.key.id}">${auction.key.title }</a>
				</div>
				<div class="panel-body">
					<div class="row">
						<div class="col-sm-12">
							<p>
								Auction is running from <strong>${auction.key.startDate}</strong>
								to <strong>${auction.key.endDate}</strong>.
							</p>
							<p>
								Current auction status: <i>${auction.key.getAuctionStatus().name}</i>
							</p>
						</div>
					</div>
				</div>
				<table class="table">
					<thead>
						<tr>
							<th>Amount</th>
							<th>Date</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="bid" items="${auction.value}">
							<tr
								class="${ bid.amount == auction.key.currentAuctionPrice - auction.key.increaseBidPrice ? 'success text-success' : '' }">
								<td><fmt:formatNumber value="${bid.amount}"
										var="formattedNumber" type="currency" pattern="###,### CHF" />${formattedNumber}
								<td><fmt:formatDate value="${bid.timeStamp}"
										var="formattedDate" type="date" pattern="yyyy-MM-dd HH:mm:ss" />${formattedDate}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</c:forEach>
	</c:otherwise>
</c:choose>
