<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>



<c:import url="template/header.jsp" />

<!-- loads functions from placeAd.js -->
<script src="/js/alerts.js"></script>

<ol class="breadcrumb">
	<li><a href="/${pagemode}/">Homepage</a></li>
	<li class="active">Alerts</li>
</ol>

<script>
	function deleteAlert(button) {
		var id = $(button).attr("data-id");
		$.get("/profile/alerts/deleteAlert?id=" + id, function() {
			$("#alertsDiv").load(document.URL + " #alertsDiv");
		});
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

		if (price.value == null || price.value == "" || price.value == "0")
			price.value = "500";
		if (radius.value == null || radius.value == "" || radius.value == "0")
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

							<spring:bind path="buyMode">
								<div class="form-group">
									<label class="col-sm-2 control-label" for="buyMode">For</label>
									<div class="col-sm-4">
										<form:select path="buyMode" cssClass="form-control">
											<form:options items="${buyModes}" itemLabel="label" />
										</form:select>
									</div>
								</div>
							</spring:bind>

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

							<div class="form-group">
								<label class="col-sm-2 control-label">Extended Alert</label>
								<div class="col-sm-4">
									<div class="checkbox">
										<label> <form:checkbox id="field-extendedAlert"
												path="extendedAlert" value="1" /> Yes
										</label>
									</div>
								</div>
							</div>


						</div>
					</div>



					<div class="panel panel-default" id="extended-alert">
						<div class="panel-body">

							<div class="row">
								<div class="col-sm-6">
									<spring:bind path="infrastructureType">
										<div class="form-group ${status.error ? 'has-error' : '' }">
											<label class="control-label col-sm-4"
												for="infrastructureType-room">Infrastructure type</label>

											<div class="col-sm-8">
												<div class="input-group">
													<form:select id="infrastructureType"
														path="infrastructureType" cssClass="form-control">
														<option value=""></option>
														<form:options items="${infrastructureTypes}"
															itemLabel="name" />
													</form:select>
												</div>
											</div>
										</div>
									</spring:bind>
								</div>
							</div>
							<div class="row">
								<div class="col-sm-6">
									<div class="form-group ${status.error ? 'has-error' : '' }">
										<label for="earliestMoveInDate" class="control-label col-sm-4">Earliest
											move-in date</label>
										<div class="col-sm-8">
											<div class="input-group">

												<form:input type="text" id="field-earliestMoveInDate"
													path="earliestMoveInDate" cssClass="form-control" />
											</div>
										</div>
									</div>
								</div>
								<div class="col-sm-6">
									<div class="form-group ${status.error ? 'has-error' : '' }">
										<label for="latestMoveInDate" class="control-label col-sm-4">Latest
											move-in date</label>
										<div class="col-sm-8">
											<div class="input-group">
												<form:input type="text" id="field-latestMoveInDate"
													path="latestMoveInDate" cssClass="form-control" />
											</div>
										</div>
									</div>
								</div>
							</div>
							
							<div class="row">
								<div class="col-sm-6">
									<div class="form-group ${status.error ? 'has-error' : '' }">
										<label for="field-floorLevelMin"
											class="control-label col-sm-4">Floor level between </label>
										<div class="col-sm-8">
											<div class="form-inline">
													<form:input type="number" step="1" id="field-floorLevelMin"
														path="floorLevelMin" cssClass="form-control input60" />
													<label for="field-floorLevelMax" class="betweenLabel">
														- </label>
													<form:input type="number" step="1" id="field-floorLevelMax"
														path="floorLevelMax" cssClass="form-control input60" />
												</div>
										</div>
									</div>
								</div>
								<div class="col-sm-6">
									<div class="form-group ${status.error ? 'has-error' : '' }">
										<label for="field-NumberOfBathMin"
											class="control-label col-sm-4">Nr. of Baths between</label>
										<div class="col-sm-8">
											<div class="form-inline">
													<form:input type="number" step="1"
														id="field-NumberOfBathMin" path="numberOfBathMin"
														cssClass="form-control input60 " />
													<label for="field-NumberOfBathMax" class="betweenLabel">
														- </label>
													<form:input type="number" cssClass="form-control input60"
														path="numberOfBathMax" id="field-NumberOfBathMax" />
												</div>
										</div>
									</div>
								</div>
							</div><div class="row">
								<div class="col-sm-6">
									<div class="form-group ${status.error ? 'has-error' : '' }">
										<label for="field-BuildYearMin"
											class="control-label col-sm-4">Build year between
										</label>
										<div class="col-sm-8">
											<div class="form-inline">
													<form:input type="number" cssClass="form-control input60"
														path="buildYearMin" id="field-BuildYearMin" />
													<label for="field-BuildYearMax" class="betweenLabel">
														- </label>
													<form:input type="number" cssClass="form-control input60"
														path="buildYearMax" id="field-BuildYearMax" />
												</div>
										</div>
									</div>
								</div>
																
								<div class="col-sm-6">
									<div class="form-group ${status.error ? 'has-error' : '' }">
										<label for="field-RenovationYearMin"
											class="control-label col-sm-4">Renovation year between
										</label>
										<div class="col-sm-8">
											<div class="form-inline">
												<form:input type="number" cssClass="form-control input60"
													path="renovationYearMin" id="field-RenovationYearMin" />
												<label for="field-RenovationYearMax" class="betweenLabel">
													- </label>
												<form:input type="number" cssClass="form-control input60"
													path="renovationYearMax" id="field-RenovationYearMax" />
											</div>
										</div>
									</div>
								</div>
							</div>
							
							
							<!--  -->
							<div class="row">
								<div class="col-sm-6">
									<div class="form-group ${status.error ? 'has-error' : '' }">
										<label for="field-NumberOfRoomsMin"
											class="control-label col-sm-4">Nr. of Rooms between </label>
										<div class="col-sm-8">
											<div class="form-inline">
												<form:input type="number" cssClass="form-control input60"
													path="numberOfRoomsMin" id="field-NumberOfRoomsMin" />
												<label for="field-NumberOfRoomsMax" class="betweenLabel">
													- </label>
												<form:input type="number" cssClass="form-control input60"
													path="numberOfRoomsMax" id="field-NumberOfRoomsMax" />

											</div>
										</div>
									</div>
								</div>
								<div class="col-sm-6">
									<div class="form-group ${status.error ? 'has-error' : '' }">
										<label for="field-DistanceSchoolMin"
											class="control-label col-sm-4">Distance to school
											from</label>
										<div class="col-sm-8">
											<div class="form-inline">
												<form:input id="field-DistanceSchoolMin" type="number"
													min="0" path="distanceSchoolMin" placeholder="0" step="100"
													cssClass="form-control input60" />
												<label for="field-DistanceSchoolMax" class="betweenLabel">
													- </label>

												<form:input id="field-DistanceSchoolMax" type="number"
													min="0" path="distanceSchoolMax" placeholder="0" step="100"
													cssClass="form-control input60" />
											</div>
										</div>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-sm-6">
									<div class="form-group ${status.error ? 'has-error' : '' }">
										<label for="field-DistanceShoppingMin"
											class="control-label col-sm-4">Distance to shopping
											from </label>
										<div class="col-sm-8">
											<div class="form-inline">
												<form:input id="field-DistanceShoppingMin" type="number"
													min="0" path="distanceShoppingMin" placeholder="0"
													step="100" cssClass="form-control input60" />

												<label for="field-DistanceShoppingMax" class="betweenLabel">
													- </label>
												<form:input id="field-DistanceShoppingMax" type="number"
													min="0" path="distanceShoppingMax" placeholder="0"
													step="100" cssClass="form-control input60" />
											</div>
										</div>
									</div>
								</div>
								<div class="col-sm-6">
									<div class="form-group ${status.error ? 'has-error' : '' }">
										<label for="field-DistancePublicTransportMin"
											class="control-label col-sm-4">Distance to public
											transport from</label>
										<div class="col-sm-8">
											<div class="form-inline">
												<form:input id="field-DistancePublicTransportMin"
													type="number" min="0" path="distancePublicTransportMin"
													placeholder="0" step="100" cssClass="form-control input60" />

												<label for="field-DistancePublicTransportMax"
													class="betweenLabel"> - </label>

												<form:input id="field-DistancePublicTransportMax"
													type="number" min="0" path="distancePublicTransportMax"
													placeholder="0" step="100" cssClass="form-control input60" />
											</div>
										</div>
									</div>
								</div>
							</div>

							<div class="row">
								<div class="col-sm-12">
									<div class="form-group ${status.error ? 'has-error' : '' }">
										<label class="control-label col-sm-2">Details</label>
										<div class="col-sm-10">
											<div class="checkbox-inline">
												<label><form:checkbox id="field-balcony"
														path="balcony" value="1" />Balcony or Patio</label>
											</div>
											<div class="checkbox-inline">
												<label><form:checkbox id="field-garage"
														path="garage" value="1" />Garage</label>
											</div>
											<div class="checkbox-inline">
												<label><form:checkbox id="field-parking"
														path="parking" value="1" />Parking</label>
											</div>
											<div class="checkbox-inline">
												<label><form:checkbox id="field-elevator"
														path="elevator" value="1" />Elevator</label>
											</div>
											<div class="checkbox-inline">
												<label><form:checkbox id="field-dishwasher"
														path="dishwasher" value="1" />Dishwasher</label>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>

						<div class="form-group pull-right">
							<div class="col-sm-12">
								<button type="reset" class="btn btn-default">Cancel</button>
								<button type="submit" class="btn btn-primary">Subscribe</button>
							</div>
						</div>
				</form:form>
			</div>
			
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
												<th>For</th>
												<th>City</th>
												<th>Radius</th>
												<th>max. Price</th>
												<th>Actions</th>
											</tr>
										</thead>
										<c:forEach var="alert" items="${alerts}">
											<tr>
												<td><c:forEach var="alertType"
														items="${alert.alertTypes}" varStatus="loop">
														<c:choose>
															<c:when test="${loop.index eq 0 }">${alertType.type.name}</c:when>
															<c:otherwise>, ${alertType.type.name}</c:otherwise>
														</c:choose>
													</c:forEach></td>
												<td>${alert.buyMode.label}</td>
												<td>${alert.city}</td>
												<td>${alert.radius}km</td>
												<td>${alert.price}Chf</td>
												<td><button type="button" class="btn btn-danger"
														data-id="${alert.id}" onClick="deleteAlert(this)">Delete</button>
													<c:if test="${alert.extendedAlert == true}">
														<button type="button" class="btn btn-default"
															data-id="${alert.id}" data-toggle="modal"
															data-target="#alert-details-${alert.id}">Details</button>

														<!-- Modal: appears only if button "Details" is clicked.  -->
														<div id="alert-details-${alert.id}"
															class="modal fade bs-example-modal-lg" role="dialog">
															<div class="modal-dialog modal-lg">

																<!-- Modal content-->
																<div class="modal-content">
																	<div class="modal-header">
																		<button type="button" class="close"
																			data-dismiss="modal">&times;</button>
																		<h4 class="modal-title">Extended alert criteria</h4>
																	</div>
																	<div class="modal-body">
																		<div class="panel panel-default">
																			<div class="panel-body">

																				<div class="row">
																					<div class="col-sm-6">
																						<table class="table">
																							<tr>
																								<td><b>Earliest move-in date</b></td>
																								<td>${alert.earliestMoveInDate}</td>
																							</tr>
																							<tr>
																								<td><b>Latest move-in date</b></td>
																								<td>${alert.latestMoveInDate}</td>
																							</tr>
																							<tr>
																								<td><b>Infrastructure Type</b></td>
																								<td>${alert.infrastructureType.name}</td>
																							</tr>
																							
																							<tr>
																								<td><b>Square footage</b></td>
																								<td>${alert.squareFootageMin} - ${alert.squareFootageMin}</td>
																							</tr>
																							
																							<tr>
																								<td><b>Build year</b></td>
																								<td>${alert.buildYearMin} - ${alert.buildYearMax}</td>
																							</tr>
																							<tr>
																								<td><b>Renovation Year</b></td>
																								<td>${alert.renovationYearMin} - ${alert.renovationYearMax}</td>
																							</tr>
																							<tr>
																								<td><b>Number of Rooms</b></td>
																								<td>${alert.numberOfRoomsMin} - ${alert.numberOfRoomsMax}</td>
																							</tr>
																							<tr>
																								<td><b>Number of Baths</b></td>
																								<td>${alert.numberOfBathMin} - ${alert.numberOfBathMax}</td>
																							</tr>
																						</table>
																		
																					</div>
																					<div class="col-sm-6">
																						<table class="table">
																									<tr>
																								<td><b>Distance School</b></td>
																								<td>${alert.distanceSchoolMin} - ${alert.distanceSchoolMax}</td>
																							</tr>
																							<tr>
																								<td><b>Distance Shopping</b></td>
																								<td>${alert.distanceShoppingMin} - ${alert.distanceShoppingMax}</td>
																							</tr>
																							<tr>
																								<td><b>Distance public transport</b></td>
																								<td>${alert.distancePublicTransportMin} - ${alert.distancePublicTransportMax}</td>
																							</tr>
																							<tr>
																								<td><b>Floor level</b></td>
																								<td>${alert.floorLevelMin} - ${alert.floorLevelMax}</td>
																							</tr>
																							
																							<tr>
																								<td><b>Elevator</b></td>
																								<td>${alert.elevator}</td>
																							</tr>
																							<tr>
																								<td><b>parking</b></td>
																								<td>${alert.parking}</td>
																							</tr>
																							<tr>
																								<td><b>Balcony</b></td>
																								<td>${alert.balcony}</td>
																							</tr>
																							<tr>
																								<td><b>Garage</b></td>
																								<td>${alert.garage}</td>
																							</tr>
																							<tr>
																								<td><b>Dishwasher</b></td>
																								<td>${alert.dishwasher}</td>
																							</tr>
																						</table>
																					</div>
																				</div>
																			</div>
																			<div class="modal-footer">
																				<button type="button" class="btn btn-default"
																					data-dismiss="modal">Close</button>
																			</div>
																		</div>
																	</div>
																</div>
															</div>
														</div>
													</c:if></td>
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


<c:import url="template/footer.jsp" />