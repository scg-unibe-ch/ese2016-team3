<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<c:import url="template/header.jsp" />

<ol class="breadcrumb">
	<li><a href="/${pagemode}/">Homepage</a></li>
	<li class="active">Alerts</li>
</ol>

<script>
function deleteAlert(button) {
	var id = $(button).attr("data-id");
	$.get("/profile/alerts/deleteAlert?id=" + id, function(){
		$("#alertsDiv").load(document.URL + " #alertsDiv");
	});
}
</script>

<script>
function validateType(form)
{
	var room = document.getElementById('room');
	var studio = document.getElementById('studio');
	var neither = document.getElementById('neither');
	var both = document.getElementById('both');
	
	if(room.checked && studio.checked) {
		both.checked = true;
		neither.checked = false;
	}
	else if(!room.checked && !studio.checked) {
		both.checked = false;
		neither.checked = true;
	}
	else {
		both.checked = false;
		neither.checked = false;
	}
}
</script>

<script>
function typeOfAlert(alert) {
	if(alert.getBothRoomAndStudio())
		return "Both"
	else if(alert.getStudio())
		return "Studio"
	else
		return "Room"
}	
</script>
	
<script>
	$(document).ready(function() {
		$("#city").autocomplete({
			minLength : 2
		});
		$("#city").autocomplete({
			source : <c:import url="getzipcodes.jsp" />
		});
		$("#city").autocomplete("option", {
			enabled : true,
			autoFocus : true
		});
		
		var price = document.getElementById('priceInput');
		var radius = document.getElementById('radiusInput');
		
		if(price.value == null || price.value == "" || price.value == "0")
			price.value = "500";
		if(radius.value == null || radius.value == "" || radius.value == "0")
			radius.value = "5";
	});
</script>

<div class="row">
	<div class="col-md-12 col-xs-12">
		<h3>Create and manage alerts</h3>

		<div class="row">
			<div class="col-md-12">

				<h4>Create new alert</h4>
				<form:form method="post" modelAttribute="alertForm"
					action="/profile/alerts" id="alertForm" autocomplete="off"
					class="form-horizontal">
					<div class="panel panel-default">
						<div class="panel-body">
						
							<div class="form-group">
								<label class="col-sm-2 control-label">Type</label>
								<div class="col-sm-6">
									<c:forEach var="type" items="${types}">
										<label class="checkbox-inline"> <form:checkbox
												path="types" value="${type}" /> ${type.name}
										</label>
									</c:forEach>
								</div>
							</div>

							<spring:bind path="city">
								<div class="form-group">
									<label class="col-sm-2 control-label" for="city">City /
										zip code:</label>
									<div class="col-sm-4">
										<form:input type="text" name="city" id="city" path="city"
											placeholder="e.g. Bern" tabindex="3" cssClass="form-control" />
										<form:errors path="city" cssClass="validationErrorText" />
									</div>
								</div>
							</spring:bind>
							
							<spring:bind path="radius">
								<div class="form-group ${status.error ? 'has-error' : '' }">
									<label for="radiusInput" class="col-sm-2 control-label">Within
										radius of (max.)</label>
									<div class="col-sm-4">
										<div class="input-group">
											<form:input id="radiusInput" type="number" path="radius"
												placeholder="e.g. 5" step="5" cssClass="form-control" />
											<span class="input-group-addon">km</span>
										</div>
										<form:errors path="radius" />
									</div>
								</div>
							</spring:bind>
							
							<spring:bind path="price">
								<div class="form-group ${status.error ? 'has-error' : '' }">
									<label class="col-sm-2 control-label" for="price">Price
										(max.)</label>
									<div class="col-sm-4">
										<div class="input-group">
											<span class="input-group-addon">Fr.</span>
											<form:input id="priceInput" type="number" path="price"
												placeholdr="e.g. 5" step="50" cssClass="form-control" />
										</div>
										<form:errors path="price" cssClass="validationErrorText" />
									</div>
								</div>
							</spring:bind>
						</div>
					</div>

					<div class="form-group pull-right">
						<div class="col-sm-12">
							<button type="reset" class="btn btn-default">Cancel</button>
							<button type="submit" class="btn btn-primary"
								onClick="validateType(this.form)">Subscribe</button>

						</div>
					</div>

				</form:form>
				<br />


				<h4>Your active alerts</h4>
				<form:form method="post" modelAttribute="alertForm"
					action="/profile/alerts" id="alertForm" autocomplete="off"
					class="form-horizontal">
					<div class="panel panel-default">
						<div class="panel-body">

							<div id="alertsDiv" class="alertsDiv">
								<c:choose>
									<c:when test="${empty alerts}">
										<p>You currently aren't subscribed to any alerts.
									</c:when>
									<c:otherwise>
										<table class="table" id="alerts">
											<thead>
												<tr>
													<th>Type</th>
													<th>City</th>
													<th>Radius</th>
													<th>max. Price</th>
													<th>Action</th>
												</tr>
											</thead>
											<c:forEach var="alert" items="${alerts}">
												<tr>
													<td>${alert.type}</td>
													<td>${alert.city}</td>
													<td>${alert.radius}km</td>
													<td>${alert.price}Chf</td>
													<td><button type="button" class="deleteButton"
															data-id="${alert.id}" onClick="deleteAlert(this)">Delete</button></td>
												</tr>
											</c:forEach>
										</table>
									</c:otherwise>
								</c:choose>
							</div>
						</div>
					</div>
				</form:form>
			</div>
		</div>
	</div>
</div>

<c:import url="template/footer.jsp" />