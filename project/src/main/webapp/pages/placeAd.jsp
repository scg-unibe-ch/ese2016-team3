<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<c:import url="template/header.jsp" />

<script src="/js/jquery.ui.widget.js"></script>
<script src="/js/jquery.iframe-transport.js"></script>
<script src="/js/jquery.fileupload.js"></script>

<script src="/js/pictureUpload.js"></script>

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

<pre>
	<a href="/">Home</a>   &gt;   Place ad</pre>

<h1>Place an ad</h1>
<hr />

<form:form method="post" modelAttribute="placeAdForm"
	action="/profile/placeAd" id="placeAdForm" autocomplete="off"
	enctype="multipart/form-data">

	<fieldset>
		<legend>General info</legend>
		<table class="placeAdTable">
			<tr>
				<td><label for="field-title">Ad Title</label></td>
				<td><label for="type-room">Type:</label></td>
			</tr>

			<tr>
				<td><form:input id="field-title" path="title"
						placeholder="Ad Title" /></td>
				<td><form:select id="type" path="type">
						<form:options items="${types}" itemLabel="name" />
					</form:select></td>
			</tr>

			<tr>
				<td><label for="field-street">Street</label></td>
				<td><label for="field-city">City / Zip code</label></td>
			</tr>

			<tr>
				<td><form:input id="field-street" path="street"
						placeholder="Street" /></td>
				<td><form:input id="field-city" path="city" placeholder="City" />
					<form:errors path="city" cssClass="validationErrorText" /></td>
			</tr>

			<tr>
				<td><label for="moveInDate">Move-in date</label></td>
				<td><label for="moveOutDate">Move-out date (optional)</label></td>
			</tr>
			<tr>
				<td><form:input type="text" id="field-moveInDate"
						path="moveInDate" /></td>
				<td><form:input type="text" id="field-moveOutDate"
						path="moveOutDate" /></td>
			</tr>

			<tr>
				<td><label for="field-Prize">Prize per month</label></td>
				<td><label for="field-SquareFootage">Square Meters</label></td>
				<td><label for="field-NumberOfRooms">Number of rooms</label></td>
			</tr>

			<tr>
				<td><form:input id="field-Prize" type="number" min="0" path="prize"
						placeholder="Prize per month" step="50" /> <form:errors
						path="prize" cssClass="validationErrorText" /></td>
				<td><form:input id="field-SquareFootage" type="number" min="0"
						path="squareFootage" placeholder="Square footage" step="5" /> <form:errors
						path="squareFootage" cssClass="validationErrorText" /></td>
				<td><form:input id="field-NumberOfRooms" type="number" min="0"
						path="numberOfRooms" placeholder="numberOfRooms" step="1" /> <form:errors
						path="numberOfRooms" cssClass="validationErrorText" /></td>
			</tr>
			<%-- new --%>
			<tr>
				<td><label for="field-DistanceSchool">Distance to
						school</label></td>
				<td><label for="field-DistanceShopping">Distance to
						shopping center</label></td>
				<td><label for="field-DistancePublicTransport">Distance
						to public transport</label></td>
			</tr>

			<tr>
				<td><form:input id="field-DistanceSchool" type="number" min="0"
						path="distanceSchool" placeholder="" step="100" /> <form:errors
						path="distanceSchool" cssClass="validationErrorText" /></td>

				<td><form:input id="field-DistanceShopping" type="number" min="0"
						path="DistanceShopping" placeholder="Distance shopping" step="100" />
					<form:errors path="DistanceShopping" cssClass="validationErrorText" /></td>

				<td><form:input id="field-DistancePublicTransport"
						type="number" min="0" path="DistancePublicTransport" 
						placeholder="Distance transport" step="100" /> <form:errors
						path="DistancePublicTransport" cssClass="validationErrorText" /></td>
			</tr>
			<tr>
				<td><label for="field-BuildYear">Year of construction</label></td>

				<td><label for="field-RenovationYear">Year of
						renovation </label></td>
				<td><label for="field-FloorLevel">Floor level </label></td>
			</tr>
			<tr>
				<td><form:input id="field-BuildYear" path="buildYear" min="0" /></td>
				<td><form:input id="field-RenovationYear" path="renovationYear" min="0" /></td>


				<td><form:input id="field-Floor" type="number" min="0"
						path="floorLevel" placeholder="0" step="1" /> <form:errors
						path="floorLevel" cssClass="validationErrorText" /></td>
			</tr>
		</table>
	</fieldset>


	<br />
	<fieldset>
		<legend>Room Description</legend>

		<table class="placeAdTable">
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
				<%-- new  --%>
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
	</fieldset>

	<br />

	<br />
	<fieldset>
		<legend>Preferences (optional)</legend>
		<form:textarea path="preferences" rows="5" cols="100"
			placeholder="Preferences"></form:textarea>
	</fieldset>

	<fieldset>
		<legend>Pictures (optional)</legend>
		<br /> <label for="field-pictures">Pictures</label> <input
			type="file" id="field-pictures" accept="image/*" multiple="multiple" />
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
		<button type="submit">Submit</button>
		<a href="/"><button type="button">Cancel</button></a>
	</div>

</form:form>

<c:import url="template/footer.jsp" />
