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
										<label> <form:checkbox id="#field-extendedAlert"
												path="extendedAlert" value="1" /> Yes
										</label>
									</div>
								</div>
							</div>


						</div>
					</div>

					<!-- this should only be displayed, if alert criteria is true  -->
					<div class="panel panel-default" id=extended-alert>
						<div class="row">
							<!-- emtpy col -->
							<div class="col-sm-1"></div>
							<spring:bind path="infrastructureType">
								<div
									class="form-group ${status.error ? 'has-error' : '' } col-sm-4">
									<label class="control-label" for="infrastructureType-room">InfrastructureType</label>
									<form:select id="infrastructureType" path="infrastructureType"
										cssClass="form-control">
										<option value=""></option>
										<form:options items="${infrastructureTypes}" itemLabel="name" />
									</form:select>
								</div>
							</spring:bind>
							<div class="col-sm-1"></div>
						</div>
						<div class="row">
							<!-- emtpy col -->
							<div class="col-sm-1"></div>

							<div class="form-group col-sm-4">
								<label for="earliestMoveInDate">Earliest move-in date</label>
								<form:input type="text" id="field-earliestMoveInDate"
									path="earliestMoveInDate" cssClass="form-control" />
							</div>

							<div class="col-sm-2"></div>
							<div class="form-group col-sm-4">
								<label for="latestMoveInDate">Latest move-in date</label>
								<form:input type="text" id="field-latestMoveInDate"
									path="latestMoveInDate" cssClass="form-control" />
							</div>

							<div class="col-sm-1"></div>
						</div>

						<div class="row">
							<!-- emtpy col -->
							<div class="col-sm-1"></div>

							<div class="col-sm-10">
								<div class="checkbox">
									<label><form:checkbox id="field-balcony" path="balcony"
											value="1" />Balcony or Patio</label>
								</div>
								<div class="checkbox">
									<label><form:checkbox id="field-garage" path="garage"
											value="1" />Garage</label>
								</div>
								<div class="checkbox">
									<label><form:checkbox id="field-parking" path="parking"
											value="1" />Parking</label>
								</div>

								<div class="checkbox">
									<label><form:checkbox id="field-elevator"
											path="elevator" value="1" />Elevator</label>
								</div>

								<div class="checkbox">
									<label><form:checkbox id="field-dishwasher"
											path="dishwasher" value="1" />Dishwasher</label>
								</div>
							</div>

							<div class="col-sm-1"></div>
						</div>

						<div class="row">
							<!-- emtpy col -->
							<div class="col-sm-1"></div>

							<div class="col-sm-4">
								<div class="form-group row">
									<label for="field-floorLevelMin">Floor level between</label>
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
							<div class="col-sm-2"></div>
							<div class="col-sm-4">
								<div class="form-group row ">
									<label for="field-NumberOfBathMin">Nr. of Bath between</label>
									<div class="form-inline">
										<form:input type="number" step="1" id="field-NumberOfBathMin"
											path="numberOfBathMin" cssClass="form-control input60 " />
										<label for="field-NumberOfBathMax" class="betweenLabel">
											- </label>
										<form:input type="number" cssClass="form-control input60"
											path="numberOfBathMax" id="field-NumberOfBathMax" />
									</div>

								</div>
							</div>

							<div class="col-sm-1"></div>
						</div>

						<div class="row">
							<!-- emtpy col -->
							<div class="col-sm-1"></div>

							<div class="col-sm-4">
								<div class="form-group row">
									<label for="field-BuildYearMin">Build year between</label>
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
							<div class="col-sm-2"></div>

							<div class="col-sm-4">
								<div class="form-group row">
									<label for="field-RenovationYearMin">Renovation year
										between</label>
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
							<div class="col-sm-1"></div>
						</div>

						<div class="row">
							<!-- emtpy col -->
							<div class="col-sm-1"></div>

							<div class="col-sm-4">
								<div class="form-group row">
									<label for="field-NumberOfRoomsMin">Nr. of Rooms
										between</label>
									<div class="form-inline">
										<form:input type="number" cssClass="form-control input60"
											path="numberOfRoomsMin" id="field-NumberOfRoomsMin" />
										<label for="field-NumberOfRoomsMax" class="betweenLabel">
											- </label>
										<form:input type="number" cssClass="form-control input60"
											path="numberOfRoomsMax" id="field-NumberOfRoomsMax" />
										<%-- muss man <form_error/> auch noch hinzufügen? --%>
									</div>
								</div>
							</div>

							<div class="col-sm-2"></div>

							<div class="col-sm-4">
								<div class="form-group row">
									<label for="field-DistanceSchoolMin">Distance to school
										from</label>
									<div class="form-inline">
										<form:input id="field-DistanceSchoolMin" type="number" min="0"
											path="distanceSchoolMin" placeholder="0" step="100"
											cssClass="form-control input60" />
										<label for="field-DistanceSchoolMax" class="betweenLabel">
											- </label>

										<form:input id="field-DistanceSchoolMax" type="number" min="0"
											path="distanceSchoolMax" placeholder="0" step="100"
											cssClass="form-control input60" />
									</div>
								</div>
							</div>
							<div class="col-sm-1"></div>
						</div>

						<div class="row">
							<!-- emtpy col -->
							<div class="col-sm-1"></div>

							<div class="col-sm-4">
								<div class="form-group row">

									<label for="field-DistanceShoppingMin">Distance to
										shopping from</label>
									<div class="form-inline">
										<form:input id="field-DistanceShoppingMin" type="number"
											min="0" path="distanceShoppingMin" placeholder="0" step="100"
											cssClass="form-control input60" />

										<label for="field-DistanceShoppingMax" class="betweenLabel">
											- </label>
										<form:input id="field-DistanceShoppingMax" type="number"
											min="0" path="distanceShoppingMax" placeholder="0" step="100"
											cssClass="form-control input60" />
									</div>
								</div>
							</div>

							<div class="col-sm-2"></div>

							<div class="col-sm-4">
								<div class="form-group row">
									<label for="field-DistancePublicTransportMin">Distance
										to public transport from</label>
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
							<div class="col-sm-1"></div>
						</div>
					</div>
					<!-- empty column -->
					<div class="col-md-1"></div>


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
															data-target="#myModal">Details</button>
													</c:if></td>
											</tr>

											<!-- Modal: appears only if button "Details" is clicked.  -->
											<div id="myModal" class="modal fade" role="dialog">
												<div class="modal-dialog">

													<!-- Modal content-->
													<div class="modal-content">
														<div class="modal-header">
															<button type="button" class="close" data-dismiss="modal">&times;</button>
															<h4 class="modal-title">Extended alert criteria</h4>
														</div>
														<div class="modal-body">
															<div class="row">
																<!-- add infrastructureType -->
																<div class="col-md-4">
																	<p>
																		<b>elevator</b>
																	</p>
																	<p>
																		<b>parking</b>
																	</p>
																	<p>
																		<b>balcony</b>
																	</p>
																	<p>
																		<b>garage</b>
																	</p>
																	<p>
																		<b>dishwasher</b>
																	</p>

																	<p>
																		<b>squareFootageMin</b>
																	</p>
																	<p>
																		<b>squareFootageMax</b>
																	</p>
																	<p>
																		<b>buildYearMin</b>
																	</p>
																	<p>
																		<b>buildYearMax</b>
																	</p>
																	<p>
																		<b>renovationYearMin</b>
																	</p>
																	<p>
																		<b>renovationYearMax</b>
																	</p>
																	<p>
																		<b>numberOfRoomsMin</b>
																	</p>
																	<p>
																		<b>numberOfRoomsMax</b>
																	</p>
																	<p>
																		<b>numberOfBathMin</b>
																	</p>
																	<p>
																		<b>numberOfBathMax</b>
																	</p>

																	<p>
																		<b>distanceSchoolMin</b>
																	</p>
																	<p>
																		<b>distanceSchoolMax</b>
																	</p>
																	<p>
																		<b>distanceShoppingMin</b>
																	</p>
																	<p>
																		<b>distanceShoppingMax</b>
																	</p>
																	<p>
																		<b>distancePublicTransportMin</b>
																	</p>
																	<p>
																		<b>distancePublicTransportMax</b>
																	</p>
																	<p>
																		<b>floorLevelMin</b>
																	</p>
																	<p>
																		<b>floorLevelMax</b>
																	</p>

																	<p>
																		<b>earliest move-in date</b>
																	</p>
																	<p>
																		<b>latest move-in date</b>
																	</p>

																</div>
																<div class="col-md-4 col-md-offset-2">
																	<p>${alert.elevator}</p>
																	<p>${alert.parking}</p>
																	<p>${alert.balcony}</p>
																	<p>${alert.garage}</p>
																	<p>${alert.dishwasher}</p>

																	<p>${alert.squareFootageMin}</p>
																	<p>${alert.squareFootageMax}</p>
																	<p>${alert.buildYearMin}</p>
																	<p>${alert.buildYearMax}</p>
																	<p>${alert.renovationYearMin}</p>
																	<p>${alert.renovationYearMax}</p>
																	<p>${alert.numberOfRoomsMin}</p>
																	<p>${alert.numberOfRoomsMax}</p>
																	<p>${alert.numberOfBathMin}</p>
																	<p>${alert.numberOfBathMax}</p>
																	<p>${alert.distanceSchoolMin}</p>
																	<p>${alert.distanceSchoolMax}</p>
																	<p>${alert.distanceShoppingMin}</p>
																	<p>${alert.distanceShoppingMax}</p>
																	<p>${alert.distancePublicTransportMin}</p>
																	<p>${alert.distancePublicTransportMax}</p>
																	<p>${alert.floorLevelMin}</p>
																	<p>${alert.floorLevelMax}</p>

																	<p>${alert.earliestMoveInDate}</p>
																	<p>${alert.latestMoveInDate}</p>



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