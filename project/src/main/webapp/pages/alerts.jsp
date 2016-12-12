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

<!-- loads functions from placeAd.js -->
<script src="/js/alerts.js"></script>

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
										<form:errors path="city" cssClass="text-danger" />
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
										<form:errors path="radius" cssClass="text-danger" />
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
										<form:errors path="price" cssClass="text-danger" />
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
							<div class="row panel">
								<div class="col-sm-6">
									<spring:bind path="infrastructureType">
										<div class="form-group ${status.error ? 'has-error' : '' }">
											<label class="control-label col-sm-4"
												for="infrastructureType-room">Infrastructure type</label>
											<div class="col-sm-8">
												<div class="input-group">
													<span class="input-group-addon">Type</span>
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

									<div class="form-group ${status.error ? 'has-error' : '' }">
										<label for="earliestMoveInDate" class="control-label col-sm-4">Earliest
											move-in date</label>
										<div class="col-sm-8">
											<div class="input-group">
												<span class="input-group-addon">Date</span>
												<form:input type="text" id="field-earliestMoveInDate"
													path="earliestMoveInDate" cssClass="form-control" />
											</div>
										</div>
									</div>
									<div class="form-group ${status.error ? 'has-error' : '' }">
										<label for="latestMoveInDate" class="control-label col-sm-4">Latest
											move-in date</label>
										<div class="col-sm-8">
											<div class="input-group">
												<span class="input-group-addon">Date</span>
												<form:input type="text" id="field-latestMoveInDate"
													path="latestMoveInDate" cssClass="form-control" />
											</div>
										</div>
									</div>
								</div>
								<div class="col-sm-6">
									<div class="form-group">
										<label class="control-label col-sm-4" for="form-group">Details</label>
										<div class="form-group">
											<div class="col-sm-5">
												<div class="checkbox">
													<label><form:checkbox id="field-garage"
															path="garage" /> Garage</label>
												</div>
												<div class="checkbox">
													<label><form:checkbox id="field-balcony"
															path="balcony" /> Balcony or Patio</label>
												</div>

												<div class="checkbox">
													<label><form:checkbox id="field-parking"
															path="parking" /> Parking</label>
												</div>

												<div class="checkbox">
													<label><form:checkbox id="field-elevator"
															path="elevator" /> Elevator</label>
												</div>

												<div class="checkbox">
													<label><form:checkbox id="field-dishwasher"
															path="dishwasher" /> Dishwasher</label>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-sm-6">
									<div class="form-group ${status.error ? 'has-error' : '' }">
										<label for="field-squareFootageMin"
											class="control-label col-sm-4">Square Footage</label>
										<div class="col-sm-8">
											<div class="col-sm-12 row form-inline">
												<div class="col-sm-5 input-group">
													<span class="input-group-addon">min</span>
													<form:input type="number" min="0" step="1"
														id="field-squareFootageMin" path="squareFootageMin"
														cssClass="form-control" />
												</div>
												<div class="col-sm-5 input-group">
													<span class="input-group-addon">max</span>
													<form:input type="number" min="0" step="1"
														id="field-squareFootageMax" path="squareFootageMax"
														cssClass="form-control" />
												</div>
											</div>
										</div>
									</div>
								</div>
								<div class="col-sm-6">
									<div class="form-group ${status.error ? 'has-error' : '' }">
										<label for="field-numberOfBathMin"
											class="control-label col-sm-4">Nr. of Baths</label>
										<div class="col-sm-8">
											<div class="col-sm-12 row form-inline">
												<div class="col-sm-5 input-group">
													<span class="input-group-addon">min</span>
													<form:input type="number" min="0" step="1"
														id="field-numberOfBathMin" path="numberOfBathMin"
														cssClass="form-control" />
												</div>
												<div class="col-sm-5 input-group">
													<span class="input-group-addon">max</span>
													<form:input type="number" min="0" step="1"
														id="field-numberOfBathMax" path="numberOfBathMax"
														cssClass="form-control" />
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-sm-6">
									<div class="form-group ${status.error ? 'has-error' : '' }">
										<label for="field-buildYearMin" class="control-label col-sm-4">Build
											year</label>
										<div class="col-sm-8">
											<div class="col-sm-12 row form-inline">
												<div class="col-sm-5 input-group">
													<span class="input-group-addon">min</span>
													<form:input type="number" min="0" step="1"
														id="field-buildYearMin" path="buildYearMin"
														cssClass="form-control" />
												</div>
												<div class="col-sm-5 input-group">
													<span class="input-group-addon">max</span>
													<form:input type="number" min="0" step="1"
														id="field-buildYearMax" path="buildYearMax"
														cssClass="form-control" />
												</div>
											</div>
										</div>
									</div>
								</div>

								<div class="col-sm-6">
									<div class="form-group ${status.error ? 'has-error' : '' }">
										<label for="field-renovationYearMin"
											class="control-label col-sm-4">Renovation year</label>
										<div class="col-sm-8">
											<div class="col-sm-12 row form-inline">
												<div class="col-sm-5 input-group">
													<span class="input-group-addon">min</span>
													<form:input type="number" min="0" step="1"
														id="field-renovationYearMin" path="renovationYearMin"
														cssClass="form-control" />
												</div>
												<div class="col-sm-5 input-group">
													<span class="input-group-addon">max</span>
													<form:input type="number" min="0" step="1"
														id="field-renovationYearMax" path="renovationYearMax"
														cssClass="form-control" />
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>


							<!--  -->
							<div class="row">

								<div class="col-sm-6">
									<div class="form-group ${status.error ? 'has-error' : '' }">
										<label for="field-numberOfRoomsMin"
											class="control-label col-sm-4">Nr. of Rooms</label>
										<div class="col-sm-8">
											<div class="col-sm-12 row form-inline">
												<div class="col-sm-5 input-group">
													<span class="input-group-addon">min</span>
													<form:input type="number" min="0" step="1"
														id="field-numberOfRoomsMin" path="numberOfRoomsMin"
														cssClass="form-control" />
												</div>
												<div class="col-sm-5 input-group">
													<span class="input-group-addon">max</span>
													<form:input type="number" min="0" step="1"
														id="field-numberOfRoomsMax" path="numberOfRoomsMax"
														cssClass="form-control" />
												</div>
											</div>
										</div>
									</div>
								</div>
								<div class="col-sm-6">
									<div class="form-group ${status.error ? 'has-error' : '' }">
										<label for="field-distanceSchoolMin"
											class="control-label col-sm-4">Distance to school (m)</label>
										<div class="col-sm-8">
											<div class="col-sm-12 row form-inline">
												<div class="col-sm-5 input-group">
													<span class="input-group-addon">min</span>
													<form:input type="number" min="0" placeholder="0"
														step="100" id="field-distanceSchoolMin"
														path="distanceSchoolMin" cssClass="form-control" />
												</div>
												<div class="col-sm-5 input-group">
													<span class="input-group-addon">max</span>
													<form:input type="number" min="0" placeholder="0"
														step="100" id="field-distanceSchoolMax"
														path="distanceSchoolMax" cssClass="form-control" />
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-sm-6">
									<div class="form-group ${status.error ? 'has-error' : '' }">
										<label for="field-distanceShoppingMin"
											class="control-label col-sm-4">Distance to shopping
											(m)</label>
										<div class="col-sm-8">
											<div class="col-sm-12 row form-inline">
												<div class="col-sm-5 input-group">
													<span class="input-group-addon">min</span>
													<form:input type="number" min="0" placeholder="0"
														step="100" id="field-distanceShoppingMin"
														path="distanceShoppingMin" cssClass="form-control" />
												</div>
												<div class="col-sm-5 input-group">
													<span class="input-group-addon">max</span>
													<form:input type="number" min="0" placeholder="0"
														step="100" id="field-distanceShoppingMax"
														path="distanceShoppingMax" cssClass="form-control" />
												</div>
											</div>
										</div>
									</div>
								</div>
								<div class="col-sm-6">
									<div class="form-group ${status.error ? 'has-error' : '' }">
										<label for="field-distancePublicTransportMin"
											class="control-label col-sm-4">Distance to public
											transport (m)</label>
										<div class="col-sm-8">
											<div class="col-sm-12 row form-inline">
												<div class="col-sm-5 input-group">
													<span class="input-group-addon">min</span>
													<form:input type="number" min="0" placeholder="0"
														step="100" id="field-distancePublicTransportMin"
														path="distancePublicTransportMin" cssClass="form-control" />
												</div>
												<div class="col-sm-5 input-group">
													<span class="input-group-addon">max</span>
													<form:input type="number" min="0" placeholder="0"
														step="100" id="field-distancePublicTransportMax"
														path="distancePublicTransportMax" cssClass="form-control" />
												</div>
											</div>
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
				<br></br>
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
																								<c:choose>
																									<c:when
																										test="${alert.earliestMoveInDate == null}">
																										<td>no date</td>
																									</c:when>
																									<c:otherwise>
																										<td>${alert.earliestMoveInDate}</td>
																									</c:otherwise>
																								</c:choose>
																							</tr>
																							<tr>
																								<td><b>Latest move-in date</b></td>
																								<c:choose>
																									<c:when
																										test="${alert.latestMoveInDate == null}">
																										<td>no date</td>
																									</c:when>
																									<c:otherwise>
																										<td>${alert.latestMoveInDate}</td>
																									</c:otherwise>
																								</c:choose>

																							</tr>
																							<tr>
																								<td><b>Infrastructure Type</b></td>
																								<c:choose>
																									<c:when
																										test="${alert.infrastructureType.name == null}">
																										<td>no type</td>
																									</c:when>
																									<c:otherwise>
																										<td>${alert.infrastructureType.name}</td>
																									</c:otherwise>
																								</c:choose>

																							</tr>
																							<tr>
																								<td><b>Elevator</b></td>
																								<c:choose>
																									<c:when test="${alert.elevator}">
																										<td>available</td>
																									</c:when>
																									<c:otherwise>
																										<td>not available</td>
																									</c:otherwise>
																								</c:choose>
																							</tr>
																							<tr>
																								<td><b>Parking</b></td>
																								<c:choose>
																									<c:when test="${alert.parking}">
																										<td>available</td>
																									</c:when>
																									<c:otherwise>
																										<td>not available</td>
																									</c:otherwise>
																								</c:choose>
																							</tr>
																							<tr>
																								<td><b>Balcony</b></td>
																								<c:choose>
																									<c:when test="${alert.balcony}">
																										<td>available</td>
																									</c:when>
																									<c:otherwise>
																										<td>not available</td>
																									</c:otherwise>
																								</c:choose>
																							</tr>
																							<tr>
																								<td><b>Garage</b></td>
																								<c:choose>
																									<c:when test="${alert.garage}">
																										<td>available</td>
																									</c:when>
																									<c:otherwise>
																										<td>not available</td>
																									</c:otherwise>
																								</c:choose>
																							</tr>
																							<tr>
																								<td><b>Dishwasher</b></td>
																								<c:choose>
																									<c:when test="${alert.dishwasher}">
																										<td>available</td>
																									</c:when>
																									<c:otherwise>
																										<td>not available</td>
																									</c:otherwise>
																								</c:choose>
																							</tr>


																						</table>

																					</div>
																					<div class="col-sm-6">
																						<table class="table">
																							<tr>
																								<td><b>Square footage between</b></td>
																								<td>${alert.squareFootageMin}-
																									${alert.squareFootageMax}</td>
																							</tr>
																							<tr>
																								<td><b>Build year between</b></td>
																								<td>${alert.buildYearMin}-
																									${alert.buildYearMax}</td>
																							</tr>
																							<tr>
																								<td><b>Renovation Year between</b></td>
																								<td>${alert.renovationYearMin}-
																									${alert.renovationYearMax}</td>
																							</tr>
																							<tr>
																								<td><b>Number of Rooms between</b></td>
																								<td>${alert.numberOfRoomsMin}-
																									${alert.numberOfRoomsMax}</td>
																							</tr>
																							<tr>
																								<td><b>Number of Baths between</b></td>
																								<td>${alert.numberOfBathMin}-
																									${alert.numberOfBathMax}</td>
																							</tr>
																							<tr>
																								<td><b>Distance School from</b></td>
																								<td>${alert.distanceSchoolMin}-
																									${alert.distanceSchoolMax}</td>
																							</tr>
																							<tr>
																								<td><b>Distance Shopping from</b></td>
																								<td>${alert.distanceShoppingMin}-
																									${alert.distanceShoppingMax}</td>
																							</tr>
																							<tr>
																								<td><b>Distance public transport from</b></td>
																								<td>${alert.distancePublicTransportMin}-
																									${alert.distancePublicTransportMax}</td>
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