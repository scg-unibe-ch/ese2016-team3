<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<c:import url="template/header.jsp" />

<ol class="breadcrumb">
	<li><a href="/${pagemode}/">Homepage</a></li>
	<li class="active">EditAd</li>
</ol>

<script src="/js/jquery.ui.widget.js"></script>
<script src="/js/jquery.iframe-transport.js"></script>
<script src="/js/jquery.fileupload.js"></script>

<script src="/js/pictureUploadEditAd.js"></script>

<script src="/js/editAd.js"></script>


<script>
	$(document)
			.ready(
					function() {
						$("#field-city").autocomplete({
							minLength : 2
						});
						$("#field-city").autocomplete({
							source : <c:import url="getzipcodes.jsp" />
						});
						$("#field-city").autocomplete("option", {
							enabled : true,
							autoFocus : true
						});
						$("#field-moveInDate").datepicker({
							dateFormat : 'dd-mm-yy'
						});

						$("#field-visitDay").datepicker({
							dateFormat : 'dd-mm-yy'
						});

						$("#addVisitButton")
								.click(
										function() {
											var date = $("#field-visitDay")
													.val();
											if (date == "") {
												return;
											}

											var startHour = $("#startHour")
													.val();
											var startMinutes = $(
													"#startMinutes").val();
											var endHour = $("#endHour").val();
											var endMinutes = $("#endMinutes")
													.val();

											if (startHour > endHour) {
												alert("Invalid times. The visit can't end before being started.");
												return;
											} else if (startHour == endHour
													&& startMinutes >= endMinutes) {
												alert("Invalid times. The visit can't end before being started.");
												return;
											}

											var newVisit = date + ";"
													+ startHour + ":"
													+ startMinutes + ";"
													+ endHour + ":"
													+ endMinutes;
											var newVisitLabel = date + " "
													+ startHour + ":"
													+ startMinutes + " to "
													+ endHour + ":"
													+ endMinutes;

											var index = $("#addedVisits input").length;

											var label = "<p>" + newVisitLabel
													+ "</p>";
											var input = "<input type='hidden' value='" + newVisit + "' name='visits[" + index + "]' />";

											$("#addedVisits").append(
													label + input);
										});
					});
</script>

<div class="row">
	<div class="col-md-12 col-xs-12">
		<h3>Edit Ad</h3>
		Fields marked with * are required. 
		<form:form method="post" modelAttribute="placeAdForm"
			action="./editAd" id="placeAdForm" autocomplete="off"
			cssClass="form form-horizontal">

			<input type="hidden" name="adId" value="${adId}" />

			<div class="row">
				<div class="col-md-12">

					<h4>Edit General Information</h4>

					<div class="panel panel-default">
						<div class="panel-body">

							<spring:bind path="title">
								<div class="form-group ${status.error ? 'has-error' : '' }">
									<label class="col-sm-3 control-label" for="field-title">Ad
										Title*</label>
									<div class="col-sm-5">
										<form:input type="text" id="field-title" path="title"
											cssClass="form-control" placeholder="Title" />
									</div>
								</div>
							</spring:bind>

							<spring:bind path="type">
								<div class="form-group ${status.error ? 'has-error' : '' }">
									<label class="col-sm-3 control-label" for="type-room">Type*</label>
									<div class="col-sm-5">
										<form:select path="type" id="type" cssClass="form-control">
											<form:options items="${types}" itemLabel="name" />
										</form:select>
									</div>
								</div>
							</spring:bind>

							<spring:bind path="street">
								<div class="form-group ${status.error ? 'has-error' : '' }">
									<label class="col-sm-3 control-label" for="field-street">Street*</label>
									<div class="col-sm-5">
										<form:input type="text" id="field-street" path="street"
											cssClass="form-control" placeholder="Street" />
									</div>
								</div>
							</spring:bind>

							<spring:bind path="city">
								<div class="form-group ${status.error ? 'has-error' : '' }">

									<label class="col-sm-3 control-label" for="field-city">City
										/ Zip Code*</label>
									<div class="col-sm-5">
										<form:input type="text" name="city" id="field-city"
											path="city" placeholder="City" cssClass="form-control" />
										<form:errors path="city" cssClass="text-danger" />
									</div>
								</div>
							</spring:bind>

							<spring:bind path="moveInDate">
								<div class="form-group ${status.error ? 'has-error' : '' }">
									<label class="col-sm-3 control-label" for="moveInDate">Move-in
										Date*</label>
									<div class="col-sm-5">
										<form:input type="text" id="field-moveInDate"
											path="moveInDate" cssClass="form-control" />
											<form:errors path="moveInDate" cssClass="text-danger" />
									</div>
								</div>
							</spring:bind>
						</div>
					</div>

					<div class="panel panel-default">
						<div class="panel-body">
							<spring:bind path="squareFootage">
								<div class="form-group ${status.error ? 'has-error' : '' }">
									<label class="col-sm-3 control-label" for="field-SquareFootage">Square
										Meters*</label>
									<div class="col-sm-5">
										<form:input id="field-SquareFootage" type="number" min="0"
											path="squareFootage" placeholder="Square footage" step="1"
											cssClass="form-control" />
										<form:errors path="squareFootage" cssClass="text-danger" />
									</div>
								</div>
							</spring:bind>

							<spring:bind path="numberOfRooms">
								<div class="form-group ${status.error ? 'has-error' : '' }">
									<label class="col-sm-3 control-label" for="field-NumberOfRooms">Number
										of Rooms*</label>
									<div class="col-sm-5">
										<form:input id="field-NumberOfRooms" type="number" min="0"
											path="numberOfRooms" placeholder="numberOfRooms" step="1"
											cssClass="form-control" />
										<form:errors path="numberOfRooms" cssClass="text-danger" />
									</div>
								</div>
							</spring:bind>
							
							<spring:bind path="numberOfBath">
								<div class="form-group ${status.error ? 'has-error' : '' }">
									<label class="col-sm-3 control-label" for="field-NumberOfBath">Number
										of Bathrooms</label>
									<div class="col-sm-5">
										<form:input id="field-NumberOfBath" type="number" min="0"
											path="numberOfBath" placeholder="Number of baths" step="1"
											cssClass="form-control" />
										<form:errors path="numberOfBath" cssClass="text-danger" />
									</div>
								</div>
							</spring:bind>

							<spring:bind path="floorLevel">
								<div class="form-group ${status.error ? 'has-error' : '' }">
									<label class="col-sm-3 control-label" for="field-FloorLevel">Floor
										Level </label>
									<div class="col-sm-5">
										<form:input id="field-Floor" type="number" min="0"
											path="floorLevel" placeholder="0" step="1"
											cssClass="form-control" />
										<form:errors path="floorLevel" cssClass="text-danger" />
									</div>
								</div>
							</spring:bind>

							<spring:bind path="distanceSchool">
								<div class="form-group ${status.error ? 'has-error' : '' }">
									<label class="col-sm-3 control-label"
										for="field-DistanceSchool">Distance to School</label>
									<div class="col-sm-5">
										<form:select id="field-DistanceSchool" path="distanceSchool"
											cssClass="form-control">
											<option value="0"></option>
											<form:options items="${distances}" itemLabel="name"
												itemValue="distance" />
										</form:select>
										<form:errors path="distanceSchool" cssClass="text-danger" />

									</div>
								</div>
							</spring:bind>

							<spring:bind path="distanceShopping">
								<div class="form-group ${status.error ? 'has-error' : '' }">
									<label class="col-sm-3 control-label"
										for="field-DistanceShopping">Distance to Shopping
										Centers</label>
									<div class="col-sm-5">
										<form:select id="field-DistanceShopping"
											path="distanceShopping" cssClass="form-control">
											<option value="0"></option>
											<form:options items="${distances}" itemLabel="name"
												itemValue="distance" />
										</form:select>
										<form:errors path="distanceShopping" cssClass="text-danger" />
									</div>
								</div>
							</spring:bind>

							<spring:bind path="distancePublicTransport">
								<div class="form-group ${status.error ? 'has-error' : '' }">
									<label class="col-sm-3 control-label"
										for="field-DistancePublicTransport">Distance to Public
										Transport</label>
									<div class="col-sm-5">
										<form:select id="field-DistancePublicTransport"
											path="distancePublicTransport" cssClass="form-control">
											<option value="0"></option>
											<form:options items="${distances}" itemLabel="name"
												itemValue="distance" />
										</form:select>
										<form:errors path="distancePublicTransport"
											cssClass="text-danger" />
									</div>
								</div>
							</spring:bind>

							<spring:bind path="buildYear">
								<div class="form-group ${status.error ? 'has-error' : '' }">
									<label class="col-sm-3 control-label" for="field-BuildYear">Year
										of Construction</label>
									<div class="col-sm-5">
										<form:input id="field-BuildYear" path="buildYear" min="0"
											cssClass="form-control" />
									</div>
								</div>
							</spring:bind>
							<spring:bind path="renovationYear">
								<div class="form-group ${status.error ? 'has-error' : '' }">
									<label class="col-sm-3 control-label"
										for="field-RenovationYear">Year of Renovation </label>
									<div class="col-sm-5">
										<form:input id="field-RenovationYear" path="renovationYear"
											min="0" cssClass="form-control" />
									</div>
								</div>
							</spring:bind>


							<spring:bind path="validPrice">
								<div class="form-group ${status.error ? 'has-error' : '' }">
									<label class="col-sm-3 control-label" for="field-Price">Price</label>
									<div class="col-sm-5">
										<form:input id="field-Price" type="number" min="0"
											path="price" placeholder="Price" step="1"
											cssClass="form-control" />
										<form:errors path="validPrice" cssClass="text-danger" />
									</div>
								</div>
							</spring:bind>

						</div>
					</div>

					<h4>Edit description</h4>


					<div class="panel panel-default">
						<div class="panel-body">

							<spring:bind path="infrastructureType">
								<div class="form-group ${status.error ? 'has-error' : '' }">
									<label class="col-sm-3 control-label"
										for="infrastructureType-room">Type of Infrastructure*</label>
									<div class="col-sm-5">
										<form:select id="infrastructureType" path="infrastructureType"
											cssClass="form-control">
											<form:options items="${infrastructureTypes}" itemLabel="name" />
										</form:select>
									</div>
								</div>
							</spring:bind>

							<div class="form-group">
								<div class="col-sm-offset-3 col-sm-5">
									<div class="checkbox">
										<label><form:checkbox id="field-garage" path="garage" />
											Garage</label>
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
						<div class="form-group">
							<label class="col-sm-3 control-label" for="roomDescription">Description*</label>
							<div class="col-sm-5">
								<form:textarea path="roomDescription" rows="5" cols="50"
									placeholder="Room Description" class="form-control" />
								<form:errors path="roomDescription" cssClass="text-danger" />
							</div>
						</div>

					</div>


					<h4>Edit Visiting Times (optional)</h4>
					<div class="panel panel-default">
						<div class="panel-body">
							<div class="row form-inline bottom15">
								<div class="col-sm-3">
									<label>Day</label> <input type="text" id="field-visitDay"
										class="form-control">
								</div>
								<div class="col-sm-9">
									<label>Time</label> <select id="startHour" class="form-control">

										<%
											for (int i = 0; i < 24; i++) {
													String hour = String.format("%02d", i);
													out.print("<option value=\"" + hour + "\">" + hour + "</option>");
												}
										%>
									</select> <select id="startMinutes" class="form-control">
										<%
											for (int i = 0; i < 60; i++) {
													String minute = String.format("%02d", i);
													out.print("<option value=\"" + minute + "\">" + minute + "</option>");
												}
										%>
									</select> <span>to&thinsp; </span> <select id="endHour"
										class="form-control">
										<%
											for (int i = 0; i < 24; i++) {
													String hour = String.format("%02d", i);
													out.print("<option value=\"" + hour + "\">" + hour + "</option>");
												}
										%>
									</select> <select id="endMinutes" class="form-control">
										<%
											for (int i = 0; i < 60; i++) {
													String minute = String.format("%02d", i);
													out.print("<option value=\"" + minute + "\">" + minute + "</option>");
												}
										%>
									</select>

									<button type="button" class="btn btn-primary"
										id="addVisitButton">
										<span class="glyphicon glyphicon-plus"></span>Add
									</button>
								</div>
							</div>
							<div class="row">
								<div class="col-sm-12" id="addedVisits">
									<%-- ?? --%>
								</div>
							</div>

						</div>
					</div>



					<h4>Change Pictures</h4>
					<div class="panel panel-default">
						<div class="panel-body">
							<div class="row bottom15">
								<div class="col-sm-12">
									<label class="control-label">Delete Existing Pictures</label>
								</div>
							</div>
							<div class="row">
								<c:forEach items="${existingPictures }" var="picture">
									<div class="col-xs-12 col-sm-4 col-md-3 pictureThumbnail">
										<div class="thumbnail">
											<img src="${picture.filePath}" style="height: 200px;" />
											<div class="caption">
												<button type="button" class="btn btn-danger"
													data-ad-id="${adId }" data-picture-id="${picture.id }">
													<span class="glyphicon glyphicon-trash"></span> Delete
												</button>
											</div>
										</div>
									</div>
								</c:forEach>
							</div>
							<div class="row form-horizontal bottom15">
								<div class="col-sm-3">
									<label class="control-label" for="field-pictures">Add
										Pictures</label>
								</div>
								<div class="col-sm-3">
									<input type="file" id="field-pictures" accept="image/*"
										multiple="multiple" />
								</div>
							</div>
							<div class="row">
								<div class="col-sm-6">
									<table id="uploaded-pictures" class="table">
										<thead>
											<tr>
												<th id="name-column">Uploaded Picture</th>
												<th>Size</th>
												<th>Delete</th>
											</tr>
										</thead>
									</table>
								</div>
							</div>
						</div>
					</div>
					<div class="form-group pull-right">
						<div class="col-sm-12">
							<a href="/">
								<button type="reset" class="btn btn-default">Cancel</button>
							</a>
							<button type="submit" class="btn btn-primary">Submit</button>
						</div>
					</div>
				</div>
			</div>
		</form:form>
	</div>
</div>

<c:import url="template/footer.jsp" />

