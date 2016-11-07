<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:import url="template/header.jsp" />

<ol class="breadcrumb">
	<li><a href="/${pagemode}/">Homepage</a></li>
	<li>Schedule</li>
</ol>

<div class="row">
	<div class="col-md-6 col-xs-12">
		<h3>Your presentations</h3>
		<c:choose>
			<c:when test="${empty presentations}">
				<p>You currently haven't scheduled any presentations.
			</c:when>
			<c:otherwise>
				<table class="table table-striped" id="visits">
					<thead>
						<tr>
							<th>Address</th>
							<th>Date</th>
							<th>Time</th>
							<th></th>
						</tr>
					</thead>
					<c:forEach var="presentation" items="${presentations}">
						<tr>
							<td><a href="/${presentation.ad.buyMode.name}/ad?id=${presentation.ad.id}">${presentation.ad.street}, ${presentation.ad.zipcode}
								 ${presentation.ad.city}</a></td>
							<td><fmt:formatDate value="${presentation.startTimestamp}"
									var="formattedVisitDay" type="date" pattern="dd.MM.yyyy" />
								${formattedVisitDay}</td>
							<td><fmt:formatDate value="${presentation.startTimestamp}"
									var="formattedStartTime" type="date" pattern="hh.mm" /> <fmt:formatDate
									value="${presentation.endTimestamp}" var="formattedEndTime"
									type="date" pattern="hh.mm" /> ${formattedStartTime} -
								${formattedEndTime}</td>
							<td><a href="./visitors?visit=${presentation.id}">Visitor list</a></td>
						</tr>
					</c:forEach>
				</table>
			</c:otherwise>
		</c:choose>
	</div>
	<div class="col-md-6 col-xs-12">
		<h3>Your visits</h3>

		<c:choose>
			<c:when test="${empty visits}">
				<p>You currently haven't scheduled any visits.
			</c:when>
			<c:otherwise>
				<table class="table table-striped" id="visits">
					<thead>
						<tr>
							<th>Address</th>
							<th>Date</th>
							<th>Time</th>
						</tr>
					</thead>
					<c:forEach var="visit" items="${visits}">
						<tr>
							<td><a href="/${visit.ad.buyMode.name}/ad?id=${visit.ad.id}">${visit.ad.street}, ${visit.ad.zipcode} ${visit.ad.city}</a></td>
							<td><fmt:formatDate value="${visit.startTimestamp}"
									var="formattedVisitDay" type="date" pattern="dd.MM.yyyy" />
								${formattedVisitDay}</td>
							<td><fmt:formatDate value="${visit.startTimestamp}"
									var="formattedStartTime" type="date" pattern="hh.mm" /> <fmt:formatDate
									value="${visit.endTimestamp}" var="formattedEndTime"
									type="date" pattern="hh.mm" /> ${formattedStartTime} -
								${formattedEndTime}</td>
						</tr>
					</c:forEach>
				</table>
			</c:otherwise>
		</c:choose>
	</div>
</div>

<c:import url="template/footer.jsp" />