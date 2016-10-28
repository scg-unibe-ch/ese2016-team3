<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<c:import url="template/header.jsp" />

<%-- <script src="/js/jquery.ui.widget.js"></script>
<script src="/js/jquery.iframe-transport.js"></script>
<script src="/js/jquery.fileupload.js"></script>--%>

<script src="/js/placeAd.js"></script>

<script>
	$(document)
			.ready(
					function() {

						// Go to controller take what you need from user
						// save it to a hidden field
						// iterate through it
						// if there is id == x then make "Bookmark Me" to "bookmarked"

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
						$("#field-moveOutDate").datepicker({
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

<%-- <pre>
	<a href="/">Home</a>   &gt;   Place ad</pre>  new --%>

<ol class="breadcrumb">
	<li><a href="/${pagemode}/">Home</a></li>
	<li class="active">Place ad</li>
</ol>


<div class="row">
	<div class="col-md-12 col-xs-12">
	
		<h3>Place an ad</h3>
		<div class="row">
			<div class="col-md-12">
				<%--<hr />--%>

				<%--<form:form method="post" modelAttribute="placeAdForm"
	action="./profile/placeAd" id="placeAdForm" autocomplete="off"
	enctype="multipart/form-data">--%>

				<form:form method="post" modelAttribute="placeAdForm"
					action="./profile/placeAd" id="placeAdForm" autocomplete="off">
					<div class="panel panel-default">
						<div class="panel-body">

						 	<fieldset>
								<legend>General info</legend>

								<%-- <form class="form-inline">--%>
									<spring:bind path="title">
										<div class="form-group ${status.error ? 'has-error' : '' }">
											<label class="col-sm-2 control-label" for="field-title">Ad Title</label>
											<div class="col-sm-3">
											<form:input type="text" id="field-title" path="title"
												cssClass="form-control" placeholder="Title" />
												</div>
										</div>
									</spring:bind>
									<spring:bind path="type">
										<div class="form-group ${status.error ? 'has-error' : '' }">
											<label for="type-room">Type</label>
											<form:select path="type" id="type">
												<form:options items="${types}" itemLabel="name" />
											</form:select>
										</div>
									</spring:bind>
								<%--</form>--%>


							<%--	<form class="form-inline">  --%>
									<spring:bind path="street">
										<div class="form-group ${status.error ? 'has-error' : '' }">
											<label class="col-sm-2 control-label" for="field-street">Street</label>
											<div class="col-sm-4">
											<form:input type="text" id="field-street" path="street"
												cssClass="form-control" placeholder="Street" />
												</div>
										</div>
									</spring:bind>
									<spring:bind path="city">
										<div class="form-group ${status.error ? 'has-error' : '' }">
										
											<label class="col-sm-2 control-label" for="field-city">City / zip code</label>
											<div class="col-sm-4">
											<form:input type="text" name="city" id="field-city"
												path="city" placeholder="City" cssClass="form-control" />
											<form:errors path="city" cssClass="validationErrorText" />
											</div>
										</div>
									</spring:bind>
							<%--	</form> --%>


								<%--  <form class="form-inline">--%>

										<spring:bind path="moveInDate">
											<div class="form-group ${status.error ? 'has-error' : '' }">
												<label class="col-sm-2 control-label" for="moveInDate">Move-in date</label>
												<div class="col-sm-4">
												<form:input type="text" id="field-moveInDate"
													path="moveInDate" cssClass="form-control" />
											</div>
											</div>
										</spring:bind>

										<spring:bind path="moveOutDate">
											<div class="form-group ${status.error ? 'has-error' : '' }">
												<label class="col-sm-2 control-label"for="moveOutDate">Move-out date (optional)</label>
												<div class="col-sm-4">
												<form:input type="text" id="field-moveOutDate"
													path="moveOutDate" cssClass="form-control" />
											</div>
											</div>
										</spring:bind>
									
								<%--	</form>--%>


							<form class="form-inline">
									<spring:bind path="squareFootage">
										<div class="form-group ${status.error ? 'has-error' : '' }">
											<label  for="field-SquareFootage">Square Meters</label>
											
											<form:input id="field-SquareFootage" type="number" min="0"
												path="squareFootage" placeholder="Square footage" step="5"
												cssClass="form-control" />
											<form:errors path="squareFootage"
												cssClass="validationErrorText" />
										
										
										</div>
									</spring:bind>

									<spring:bind path="numberOfRooms">
										<div class="form-group ${status.error ? 'has-error' : '' }">
											<label  for="field-NumberOfRooms">Number of rooms</label>
										
											<form:input id="field-NumberOfRooms" type="number" min="0"
												path="numberOfRooms" placeholder="numberOfRooms" step="1"
												cssClass="form-control" />
											<form:errors path="numberOfRooms"
												cssClass="validationErrorText" />
										
										</div>
									</spring:bind>
									<spring:bind path="floorLevel">
										<div class="form-group ${status.error ? 'has-error' : '' }">
											<label  for="field-FloorLevel">Floor level </label>
											
											<form:input id="field-Floor" type="number" min="0"
												path="floorLevel" placeholder="0" step="1"
												cssClass="form-control" />
											<form:errors path="floorLevel" cssClass="validationErrorText" />
										
										</div>
									</spring:bind>
							</form>


								<form class="form-inline">
									<spring:bind path="distanceSchool">
										<div class="form-group ${status.error ? 'has-error' : '' }">
											<label for="field-DistanceSchool">Distance to school</label>
											<form:input id="field-DistanceSchool" type="number" min="0"
												path="distanceSchool" placeholder="0" step="100"
												cssClass="form-control" />
											<form:errors path="distanceSchool"
												cssClass="validationErrorText" />

										</div>
									</spring:bind>
									<spring:bind path="distanceShopping">
										<div class="form-group ${status.error ? 'has-error' : '' }">
											<label for="field-DistanceShopping">Distance to
												shopping center</label>
											<form:input id="field-DistanceShopping" type="number" min="0"
												path="DistanceShopping" placeholder="0" step="100"
												cssClass="form-control" />
											<form:errors path="DistanceShopping"
												cssClass="validationErrorText" />


										</div>
									</spring:bind>
									<spring:bind path="distancePublicTransport">
										<div class="form-group ${status.error ? 'has-error' : '' }">
											<label for="field-DistancePublicTransport">Distance
												to public transport</label>
											<form:input id="field-DistancePublicTransport" type="number"
												min="0" path="DistancePublicTransport" placeholder="0"
												step="100" cssClass="form-control" />
											<form:errors path="DistancePublicTransport"
												cssClass="validationErrorText" />
										</div>
									</spring:bind>
								</form>


								<form class="form-inline">
									<spring:bind path="buildYear">
										<div class="form-group ${status.error ? 'has-error' : '' }">
											<label for="field-BuildYear">Year of construction</label>
											<form:input id="field-BuildYear" path="buildYear" min="0"
												cssClass="form-control" />
										</div>
									</spring:bind>
									<spring:bind path="renovationYear">
										<div class="form-group ${status.error ? 'has-error' : '' }">
											<label for="field-RenovationYear">Year of renovation
											</label>
											<form:input id="field-RenovationYear" path="renovationYear"
												min="0" cssClass="form-control" />
										</div>
									</spring:bind>
								</form>


								<spring:bind path="prize">
									<div class="form-group ${status.error ? 'has-error' : '' }">
										<label for="field-Prize">Prize per month</label>
										<form:input id="field-Prize" type="number" min="0"
											path="prize" placeholder="Prize per month" step="50"
											cssClass="form-control" />
										<form:errors path="prize" cssClass="validationErrorText" />
									</div>
								</spring:bind>


								<table class="placeAdTable">
									<tr>
										<%--	<td><label for="field-title">Ad Title</label></td> 
				<td><label for="type-room">Type:</label></td>--%>
									</tr>

									<tr>
										<%--<td><form:input id="field-title" path="title"
						placeholder="Ad Title" /></td> 
				<td><form:select id="type" path="type">
						<form:options items="${types}" itemLabel="name" />
					</form:select></td>   --%>
									</tr>

									<tr>
										<%--	<td><label for="field-street">Street</label></td>
				<td><label for="field-city">City / Zip code</label></td>  --%>
									</tr>

									<tr>
										<%--	<td><form:input id="field-street" path="street"
						placeholder="Street" /></td>
				<td><form:input id="field-city" path="city" placeholder="City" />
					<form:errors path="city" cssClass="validationErrorText" /></td>--%>
									</tr>

									<tr>
										<%--	<td><label for="moveInDate">Move-in date</label></td>
				<td><label for="moveOutDate">Move-out date (optional)</label></td>--%>
									</tr>
									<tr>
										<%--	<td><form:input type="text" id="field-moveInDate"
						path="moveInDate" /></td>
				<td><form:input type="text" id="field-moveOutDate"
						path="moveOutDate" /></td>  --%>
									</tr>

									<tr>
										<%--	<td><label for="field-Prize">Prize per month</label></td>
				<td><label for="field-SquareFootage">Square Meters</label></td>
				<td><label for="field-NumberOfRooms">Number of rooms</label></td>  --%>
									</tr>

									<tr>
										<%--	<td><form:input id="field-Prize" type="number" min="0" path="prize"
						placeholder="Prize per month" step="50" /> <form:errors
						path="prize" cssClass="validationErrorText" /></td>
				<td><form:input id="field-SquareFootage" type="number" min="0"
						path="squareFootage" placeholder="Square footage" step="5" /> <form:errors
						path="squareFootage" cssClass="validationErrorText" /></td>
				<td><form:input id="field-NumberOfRooms" type="number" min="0"
						path="numberOfRooms" placeholder="numberOfRooms" step="1" /> <form:errors
						path="numberOfRooms" cssClass="validationErrorText" /></td>  --%>
									</tr>
									<%-- new --%>
									<tr>
										<%--	<td><label for="field-DistanceSchool">Distance to
						school</label></td>
				<td><label for="field-DistanceShopping">Distance to
						shopping center</label></td>
				<td><label for="field-DistancePublicTransport">Distance
						to public transport</label></td>  --%>
									</tr>
									<%-- it is possible to enter numbers below zero, even though min="0" is specified --%>
									<tr>
										<%-- 		<td><form:input id="field-DistanceSchool" type="number" min="0"
						path="distanceSchool" placeholder="" step="100" /> <form:errors
						path="distanceSchool" cssClass="validationErrorText" /></td>

				<td><form:input id="field-DistanceShopping" type="number" min="0"
						path="DistanceShopping" placeholder="Distance shopping" step="100" />
					<form:errors path="DistanceShopping" cssClass="validationErrorText" /></td>

				<td><form:input id="field-DistancePublicTransport"
						type="number" min="0" path="DistancePublicTransport" 
						placeholder="Distance transport" step="100" /> <form:errors
						path="DistancePublicTransport" cssClass="validationErrorText" /></td> --%>
									</tr>
									<tr>
										<%-- 		<td><label for="field-BuildYear">Year of construction</label></td>

				<td><label for="field-RenovationYear">Year of
						renovation </label></td>
				<td><label for="field-FloorLevel">Floor level </label></td>   --%>
									</tr>
									<tr>
										<%-- 	<td><form:input id="field-BuildYear" path="buildYear" min="0" /></td>
				<td><form:input id="field-RenovationYear" path="renovationYear" min="0" /></td>


				<td><form:input id="field-Floor" type="number" min="0"
						path="floorLevel" placeholder="0" step="1" /> <form:errors
						path="floorLevel" cssClass="validationErrorText" /></td>  --%>
									</tr>
								</table>
						 	</fieldset>


							<br />
							<fieldset>
								<legend>Room Description</legend>

								<%-- 	<div class="form-group ${status.error ? 'has-error' : '' }"></div>  --%>

								<form class="form-inline">
									<spring:bind path="numberOfBath">
										<div class="form-group ${status.error ? 'has-error' : '' }">
											<label for="field-NumberOfBath">Number of baths</label>
											<form:input id="field-NumberOfBath" type="number" min="0"
												path="numberOfBath" placeholder="Number of baths" step="1"
												cssClass="form-control" />
											<form:errors path="numberOfBath"
												cssClass="validationErrorText" />
										</div>
									</spring:bind>
									<spring:bind path="infrastructureType">
										<div class="form-group ${status.error ? 'has-error' : '' }">
											<label for="infrastructureType-room">InfrastructureType</label>
											<form:select id="infrastructureType"
												path="infrastructureType">
												<form:options items="${infrastructureTypes}"
													itemLabel="name" />
											</form:select>
										</div>
									</spring:bind>
								</form>

								<form class="form-inline">
									<label class="checkbox-inline">Garage</label>
									<form:checkbox id="field-garage" path="garage" value="1" />
									<label class="checkbox-inline">Parking</label>
									<form:checkbox id="field-parking" path="parking" value="1" />
								</form>

								<form class="form-inline">
									<label class="checkbox-inline">Balcony or Patio</label>
									<form:checkbox id="field-balcony" path="balcony" value="1" />
									<label class="checkbox-inline">Elevator </label>
									<form:checkbox id="field-elevator" path="elevator" value="1" />
								</form>
								<label class="checkbox-inline">Dishwasher</label>
								<form:checkbox id="field-dishwasher" path="dishwasher" value="1" />

								<div class="form-group">
									<label for="roomDescription">Room Description:</label>
									<form:textarea path="roomDescription" rows="10" cols="100"
										placeholder="Room Description" class="form-control" />
									<form:errors path="roomDescription"
										cssClass="validationErrorText" />
								</div>

								<%-- 	<table class="placeAdTable">
			<tr>
				<td><label for="field-NumberOfBath">Number of baths</label></td>
				<td><label for="infrastructureType-room">InfrastructureType</label></td>
			</tr>
			<tr>
				<td><form:input id="field-NumberOfBath" type="number" min="0"
						path="numberOfBath" placeholder="Number of baths" step="1" /> <form:errors
						path="numberOfBath" cssClass="validationErrorText" /></td>
				<td><form:select id="infrastructureType"
						path="infrastructureType">
						<form:options items="${infrastructureTypes}" itemLabel="name" />
					</form:select></td>
			</tr>

			<tr>
				<td><form:checkbox id="field-balcony" path="balcony" value="1" /><label>Balcony
						or Patio</label></td>
				<td><form:checkbox id="field-parking" path="parking" value="1" /><label>Parking</label></td>
			</tr>
			<tr>

			</tr>
			<tr>
				<td><form:checkbox id="field-garage" path="garage" value="1" /><label>Garage</label>
				</td>
				
				<td><form:checkbox id="field-dishwasher" path="dishwasher"
						value="1" /><label>Dishwasher</label></td>
			</tr>
			<tr>
				<td><form:checkbox id="field-elevator" path="elevator"
						value="1" /><label>Elevator </label></td>
			</tr>

		</table>
		<br />
		<form:textarea path="roomDescription" rows="10" cols="100"
			placeholder="Room Description" />
		<form:errors path="roomDescription" cssClass="validationErrorText" />
	--%>
							</fieldset>

							<%--	<br />

	<br />
	<fieldset>
		<legend>Preferences (optional)</legend>
		<form:textarea path="preferences" rows="5" cols="100"
			placeholder="Preferences"></form:textarea>
	</fieldset>
--%>
							<fieldset>
								<legend>Pictures (optional)</legend>
								<label for="field-pictures">Pictures</label> <input type="file"
									id="field-pictures" accept="image/*" multiple="multiple" />
								<table id="uploaded-pictures" class="styledTable">
									<tr>
										<th id="name-column">Uploaded picture</th>
										<th>Size</th>
										<th>Delete</th>
									</tr>
								</table>
								<br>
							</fieldset>

							<fieldset>
								<legend>Visiting times (optional)</legend>

								<table>
									<tr>
										<td><input type="text" id="field-visitDay" /> <select
											id="startHour">
												<%
							for (int i = 0; i < 24; i++) {
									String hour = String.format("%02d", i);
									out.print("<option value=\"" + hour + "\">" + hour + "</option>");
								}
						%>
										</select> <select id="startMinutes">
												<%
							for (int i = 0; i < 60; i++) {
									String minute = String.format("%02d", i);
									out.print("<option value=\"" + minute + "\">" + minute + "</option>");
								}
						%>
										</select> <span>to&thinsp; </span> <select id="endHour">
												<%
							for (int i = 0; i < 24; i++) {
									String hour = String.format("%02d", i);
									out.print("<option value=\"" + hour + "\">" + hour + "</option>");
								}
						%>
										</select> <select id="endMinutes">
												<%
							for (int i = 0; i < 60; i++) {
									String minute = String.format("%02d", i);
									out.print("<option value=\"" + minute + "\">" + minute + "</option>");
								}
						%>
										</select>



											<div id="addVisitButton" class="smallPlusButton">+</div>

											<div id="addedVisits"></div></td>

									</tr>

								</table>
								<br>
							</fieldset>



							<br />
							<div>
								<div class="form-group pull-left">
									<div class="col-sm-12">
										<button type="submit" class="btn btn-primary">Submit</button>
										<a href="/">
											<button type="reset" class="btn btn-default">Cancel</button>
										</a>
									</div>
								</div>
							</div>
						</div>
					</div>
				</form:form>
			</div>
		</div>
	</div>
</div>

<c:import url="template/footer.jsp" />
