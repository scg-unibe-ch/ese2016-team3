<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:import url="template/header.jsp" />

<ol class="breadcrumb">
	<li><a href="/${pagemode}/">Homepage</a></li>
	<li class="active">Enquiries</li>
</ol>

<!-- format the dates -->
<fmt:formatDate value="${enquiries[0].dateSent}" var="formattedDateSent"
	type="date" pattern="HH:mm, dd.MM.yyyy" />

<script>
	$(document)
			.ready(
					function() {
						var rows = $("#enquiryList table tr:gt(0)");
						$(rows).hover(
								function() {
									$(this).children().css("background-color",
											"#ececec");
								},
								function() {
									$(this).children().css("background-color",
											"white");
								});

						function attachHandlers() {
							$(".acceptButton")
									.click(
											function() {
												var cell = $(this).parent();
												var id = $(this)
														.attr("data-id");
												$
														.get("/profile/enquiries/acceptEnquiry?id="
																+ id);
												$(cell)
														.html(
																"Accepted <button class='undoButton btn btn-danger' data-id='"+ id+ "'>Undo</button>");
												attachUndoHandler();
											});
							
							$(".declineButton")
									.click(
											function() {
												var cell = $(this).parent();
												var id = $(this)
														.attr("data-id");
												$
														.get("/profile/enquiries/declineEnquiry?id="
																+ id);
												$(cell)
														.html(
																"Declined <button class='undoButton btn btn-danger' data-id='"+ id+ "'>Undo</button>");
												attachUndoHandler();
											});
						}

						function attachUndoHandler() {
							$(".undoButton")
									.click(
											function() {
												var cell = $(this).parent();
												var id = $(this)
														.attr("data-id");
												$
														.get("/profile/enquiries/reopenEnquiry?id="
																+ id);
												$(cell)
														.html(
																"<button class='acceptButton btn btn-success' data-id='"+ id + "'>Accept</button><button class='declineButton btn btn-danger' data-id='" + id + "'>Decline</button>");
												attachHandlers();
											});
						}

						attachHandlers();

					});
</script>

<h3>Enquiries</h3>
<div class="row">
	<div class="col-sm-12 col-sm-12">

		<table class="table">
			<tr>
				<th>Sender</th>
				<th>Ad</th>
				<th>Date of the visit</th>
				<th>Date sent</th>
				<th>Actions</th>
			</tr>
			<c:forEach items="${enquiries}" var="enquiry">
				<fmt:formatDate value="${enquiry.dateSent}"
					var="singleFormattedDateSent" type="date"
					pattern="HH:mm, dd.MM.yyyy" />
				<fmt:formatDate value="${enquiry.visit.startTimestamp}"
					var="startTime" type="date" pattern="HH:mm" />
				<fmt:formatDate value="${enquiry.visit.endTimestamp}" var="endTime"
					type="date" pattern="HH:mm" />
				<fmt:formatDate value="${enquiry.visit.startTimestamp }" var="date"
					type="date" pattern="dd.MM.yyyy" />

				<tr>
					<td><a href="/user?id=${enquiry.sender.id}">${enquiry.sender.email}</a></td>
					<td><a href="/${enquiry.visit.ad.buyMode.name}/ad?id=${enquiry.visit.ad.id }">${enquiry.visit.ad.street },
							${enquiry.visit.ad.zipcode } ${enquiry.visit.ad.city }</a></td>

					<td>${date},&#32;${startTime}&#32;to&#32;${endTime }</td>
					<td>${singleFormattedDateSent}</td>
					<td><c:choose>
							<c:when test="${enquiry.state == 'ACCEPTED'}">
								<p>Accepted</p>
							</c:when>
							<c:when test="${enquiry.state == 'DECLINED' }">
								<p>Declined</p>
							</c:when>
							<c:otherwise>
								<button type="button" class="acceptButton btn btn-success"
									data-id="${enquiry.id}">Accept</button>
								<button type="button" class="declineButton btn btn-danger"
									data-id="${enquiry.id}">Decline</button>
							</c:otherwise>
						</c:choose></td>
				</tr>
			</c:forEach>
		</table>
	</div>
</div>

<c:import url="template/footer.jsp" />