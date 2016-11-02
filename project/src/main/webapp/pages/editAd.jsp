<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

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
		<h3>Edit ad</h3>
		<form:form method="post" modelAttribute="placeAdForm" action="./editAd" 
			id="placeAdForm" autocomplete="off" cssClass="form form-horizontal">
			
			<input type="hidden" name="adId" value="${ad.id}" />
			
			<div class="row">
				<div class="col-md-12">

					<h4>Edit general information</h4>

					<div class="panel panel-default">
						<div class="panel-body">

							<spring:bind path="title">
								<div class="form-group ${status.error ? 'has-error' : '' }">
									<label class="col-sm-3 control-label" for="field-title">Ad
										Title</label>
									<div class="col-sm-5">
										<form:input type="text" id="field-title" path="title"
											value="${ad.title}" cssClass="form-control"
											placeholder="Title" />
									</div>
								</div>
							</spring:bind>

							<spring:bind path="type">
								<div class="form-group ${status.error ? 'has-error' : '' }">
									<label class="col-sm-3 control-label" for="type-room">Type</label>
									<div class="col-sm-5">
										<form:select path="type" value="${ad.type}" id="type"
											cssClass="form-control">
											<form:options items="${types}" itemLabel="name" />
										</form:select>
									</div>
								</div>
							</spring:bind>

							<spring:bind path="street">
								<div class="form-group ${status.error ? 'has-error' : '' }">
									<label class="col-sm-3 control-label" for="field-street">Street</label>
									<div class="col-sm-5">
										<form:input type="text" value="${ad.street}" id="field-street"
											path="street" cssClass="form-control" placeholder="Street" />
									</div>
								</div>
							</spring:bind>

							<spring:bind path="city">
								<div class="form-group ${status.error ? 'has-error' : '' }">

									<label class="col-sm-3 control-label" for="field-city">City
										/ zip code</label>
									<div class="col-sm-5">
										<form:input type="text" name="city" id="field-city"
											path="city" value="${ad.zipcode} - ${ad.city}" placeholder="City"
											cssClass="form-control" />
										<form:errors path="city" cssClass="validationErrorText" />
									</div>
								</div>
							</spring:bind>

							<spring:bind path="moveInDate">
								<div class="form-group ${status.error ? 'has-error' : '' }">
									<label class="col-sm-3 control-label" for="moveInDate">Move-in
										date</label>
									<div class="col-sm-5">
										<form:input type="text" id="field-moveInDate"
											path="moveInDate" value="${ad.moveInDate}"
											cssClass="form-control" />
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
										Meters</label>
									<div class="col-sm-5">
										<form:input id="field-SquareFootage" type="number" min="0"
											path="squareFootage" value="${ad.squareFootage}"
											placeholder="Square footage" step="5" cssClass="form-control" />
										<form:errors path="squareFootage"
											cssClass="validationErrorText" />
									</div>
								</div>
							</spring:bind>

							<spring:bind path="numberOfRooms">
								<div class="form-group ${status.error ? 'has-error' : '' }">
									<label class="col-sm-3 control-label" for="field-NumberOfRooms">Number
										of rooms</label>
									<div class="col-sm-5">
										<form:input id="field-NumberOfRooms" type="number" min="0"
											path="numberOfRooms" value="${ad.numberOfRooms}"
											placeholder="numberOfRooms" step="1" cssClass="form-control" />
										<form:errors path="numberOfRooms"
											cssClass="validationErrorText" />
									</div>
								</div>
							</spring:bind>

							<spring:bind path="floorLevel">
								<div class="form-group ${status.error ? 'has-error' : '' }">
									<label class="col-sm-3 control-label" for="field-FloorLevel">Floor
										level </label>
									<div class="col-sm-5">
										<form:input id="field-Floor" type="number" min="0"
											path="floorLevel" value="${ad.floorLevel}" placeholder="0"
											step="1" cssClass="form-control" />
										<form:errors path="floorLevel" cssClass="validationErrorText" />
									</div>
								</div>
							</spring:bind>

							<spring:bind path="distanceSchool">
								<div class="form-group ${status.error ? 'has-error' : '' }">
									<label class="col-sm-3 control-label"
										for="field-DistanceSchool">Distance to school</label>
									<div class="col-sm-5">
										<form:input id="field-DistanceSchool" type="number" min="0"
											path="distanceSchool" value="${ad.distanceSchool}"
											placeholder="0" step="100" cssClass="form-control" />
										<form:errors path="distanceSchool"
											cssClass="validationErrorText" />

									</div>
								</div>
							</spring:bind>

							<spring:bind path="distanceShopping">
								<div class="form-group ${status.error ? 'has-error' : '' }">
									<label class="col-sm-3 control-label"
										for="field-DistanceShopping">Distance to shopping
										center</label>
									<div class="col-sm-5">
										<form:input id="field-DistanceShopping" type="number" min="0"
											path="DistanceShopping" value="${ad.distanceShopping}"
											placeholder="0" step="100" cssClass="form-control" />
										<form:errors path="DistanceShopping"
											cssClass="validationErrorText" />
									</div>
								</div>
							</spring:bind>

							<spring:bind path="distancePublicTransport">
								<div class="form-group ${status.error ? 'has-error' : '' }">
									<label class="col-sm-3 control-label"
										for="field-DistancePublicTransport">Distance to public
										transport</label>
									<div class="col-sm-5">
										<form:input id="field-DistancePublicTransport" type="number"
											min="0" path="DistancePublicTransport"
											value="${ad.distancePublicTransport}" placeholder="0"
											step="100" cssClass="form-control" />
										<form:errors path="DistancePublicTransport"
											cssClass="validationErrorText" />
									</div>
								</div>
							</spring:bind>

							<spring:bind path="buildYear">
								<div class="form-group ${status.error ? 'has-error' : '' }">
									<label class="col-sm-3 control-label" for="field-BuildYear">Year
										of construction</label>
									<div class="col-sm-5">
										<form:input id="field-BuildYear" path="buildYear"
											value="${ad.buildYear}" min="0" cssClass="form-control" />
									</div>
								</div>
							</spring:bind>
							<spring:bind path="renovationYear">
								<div class="form-group ${status.error ? 'has-error' : '' }">
									<label class="col-sm-3 control-label"
										for="field-RenovationYear">Year of renovation </label>
									<div class="col-sm-5">
										<form:input id="field-RenovationYear" path="renovationYear"
											value="${ad.renovationYear}" min="0" cssClass="form-control" />
									</div>
								</div>
							</spring:bind>


							<spring:bind path="prize">
								<div class="form-group ${status.error ? 'has-error' : '' }">
									<label class="col-sm-3 control-label" for="field-Prize">Prize
										per month</label>
									<div class="col-sm-5">
										<form:input id="field-Prize" type="number" min="0"
											path="prize" placeholder="Prize per month" step="50"
											value="${ad.prizePerMonth}" cssClass="form-control" />
										<form:errors path="prize" cssClass="validationErrorText" />
									</div>
								</div>
							</spring:bind>

						</div>
					</div>

					<h4>Edit description</h4>


					<div class="panel panel-default">
						<div class="panel-body">

							<spring:bind path="numberOfBath">
								<div class="form-group ${status.error ? 'has-error' : '' }">
									<label class="col-sm-3 control-label" for="field-NumberOfBath">Number
										of baths</label>
									<div class="col-sm-5">
										<form:input id="field-NumberOfBath" type="number" min="0"
											path="numberOfBath" placeholder="Number of baths" step="1"
											cssClass="form-control" />
										<form:errors path="numberOfBath"
											cssClass="validationErrorText" />
									</div>
								</div>
							</spring:bind>
							<spring:bind path="infrastructureType">
								<div class="form-group ${status.error ? 'has-error' : '' }">
									<label class="col-sm-3 control-label"
										for="infrastructureType-room">InfrastructureType</label>
									<div class="col-sm-5">
										<form:select id="infrastructureType" path="infrastructureType"
											cssClass="form-control">
											<form:options items="${infrastructureTypes}" itemLabel="name" />
										</form:select>
									</div>
								</div>
							</spring:bind>

							<div class="checkbox">

								<div class="col-sm-5">
									<c:choose>
										<c:when test="${ad.garage}">
											<form:checkbox id="field-garage" path="garage"
												checked="checked" />
											<label>Garage</label>
										</c:when>
										<c:otherwise>
											<form:checkbox id="field-garage" path="garage" />
											<label>Garage</label>
										</c:otherwise>
									</c:choose>
								</div>


							</div>
							<div class="checkbox">
								<div class="col-sm-5">
									<c:choose>
										<c:when test="${ad.balcony}">
											<form:checkbox id="field-balcony" path="balcony"
												checked="checked" />
											<label>Balcony or Patio</label>
										</c:when>
										<c:otherwise>
											<form:checkbox id="field-balcony" path="balcony" />
											<label>Balcony or Patio</label>
										</c:otherwise>
									</c:choose>

									<div class="checkbox">
										<c:choose>
											<c:when test="${ad.parking}">
												<form:checkbox id="field-parking" path="parking"
													checked="checked" />
												<label>Parking</label>
											</c:when>
											<c:otherwise>
												<form:checkbox id="field-parking" path="parking" />
												<label>Parking</label>
											</c:otherwise>
										</c:choose>
									</div>

									<div class="checkbox">
										<c:choose>
											<c:when test="${ad.elevator}">
												<form:checkbox id="field-elevator" path="elevator"
													checked="checked" />
												<label>Elevator</label>
											</c:when>
											<c:otherwise>
												<form:checkbox id="field-elevator" path="elevator" />
												<label>Elevator</label>
											</c:otherwise>
										</c:choose>
									</div>

									<div class="checkbox">
										<c:choose>
											<c:when test="${ad.dishwasher}">
												<form:checkbox id="field-dishwasher" path="dishwasher"
													checked="checked" />
												<label>dishwasher</label>
											</c:when>
											<c:otherwise>
												<form:checkbox id="field-dishwasher" path="dishwasher" />
												<label>dishwasher</label>
											</c:otherwise>
										</c:choose>
									</div>

								</div>
							</div>

						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label" for="roomDescription">Room
								Description:</label>
							<div class="col-sm-5">
								<form:textarea path="roomDescription" rows="5" cols="50"
									placeholder="Room Description" class="form-control" />
								<form:errors path="roomDescription"
									cssClass="validationErrorText" />
							</div>
						</div>

					</div>
				</div>

				<h4>Edit visiting times (optional)</h4>
				<div class="panel panel-default">
					<div class="panel-body">
						<div class="form-group">
							<label>Day</label> <input type="text" id="field-visitDay">
							<label>Time</label> <select id="startHour">
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


							<button type="button" class="btn btn-default">+</button>
							<div id="addedVisits"></div>

						</div>

					</div>
				</div>



				<h4>Change Pictures</h4>
				<div class="panel panel-default">
					<div class="panel-body">
						<fieldset>
							<label class="control-label" for="field-pictures">Delete
								existing pictures</label> <br />
							<div>
								<c:forEach items="${ad.pictures }" var="picture">
									<div class="pictureThumbnail">
										<div>
											<img src="${picture.filePath}" />
										</div>
										<button type="button" data-ad-id="${ad.id }"
											data-picture-id="${picture.id }">Delete</button>
									</div>
								</c:forEach>
							</div>
							<p class="clearBoth"></p>
							<br /> <br />
							<hr />

						</fieldset>


						<label class="control-label" for="field-pictures">Add
							pictures</label> <input type="file" id="field-pictures" class="file"
							accept="image/*" multiple data-show-preview="false">



						<div class="col-sm-6">
							<table id="uploaded-pictures" class="table">
								<thead>
									<tr>
										<th id="name-column">Uploaded picture</th>
										<th>Size</th>
										<th>Delete</th>


									</tr>
								</thead>
							</table>
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
		</form:form>
	</div>
</div>








<!-- OLD page -->
<%-- 
<!-- format the dates -->
<fmt:formatDate value="${ad.moveInDate}" var="formattedMoveInDate"
	type="date" pattern="dd-MM-yyyy" />

	
<pre><a href="/">Homepage</a>   &gt;   <a href="/profile/myRooms">My Rooms</a>   &gt;   <a href="/ad?id=${ad.id}">Ad Description</a>   &gt;   Edit Ad</pre>


<h1>Edit Ad</h1>
<hr />

<form:form method="post" modelAttribute="placeAdForm"
	action="/profile/editAd" id="placeAdForm" autocomplete="off"
	enctype="multipart/form-data">

<input type="hidden" name="adId" value="${ad.id }" />

	<fieldset>
		<legend>Change General info</legend>
		<table class="placeAdTable">
			<tr>
				<td><label for="field-title">Ad Title</label></td>
				<td><label for="type-room">Type:</label></td>
			</tr>

			<tr>
				<td><form:input id="field-title" path="title" value="${ad.title}" /></td>
				<td>
					<form:select id="type-room" path="type">
						<form:options items="${types}" itemLabel="name" />
					</form:select>
			</tr>

			<tr>
				<td><label for="field-street">Street</label></td>
				<td><label for="field-city">City / Zip code</label></td>
			</tr>

			<tr>
				<td><form:input id="field-street" path="street"
						value="${ad.street}" /></td>
				<td>
					<form:input id="field-city" path="city" value="${ad.zipcode} - ${ad.city}" />
					<form:errors path="city" cssClass="validationErrorText" />
				</td>
			</tr>

			<tr>
				<td><label for="moveInDate">Move-in date</label></td>
			</tr>
			<tr>
				<td>
					<form:input type="text" id="field-moveInDate"
						path="moveInDate" value="${formattedMoveInDate }"/>
				</td>

			</tr>

			<tr>
				<td><label for="field-Prize">Prize per month</label></td>
				<td><label for="field-SquareFootage">Square Meters</label></td>
			</tr>
			<tr>
				<td>
					<form:input id="field-Prize" type="number" path="prize"
						placeholder="Prize per month" step="50" value="${ad.prizePerMonth }"/> <form:errors
						path="prize" cssClass="validationErrorText" />
				</td>
				<td>
					<form:input id="field-SquareFootage" type="number"
						path="squareFootage" placeholder="Prize per month" step="5" 
						value="${ad.squareFootage }"/> <form:errors
						path="squareFootage" cssClass="validationErrorText" />
				</td>
			</tr>
		</table>
	</fieldset>


	<br />
	<fieldset>
		<legend>Change Room Description</legend>

		<table class="placeAdTable">
			<tr>
				
				<td>
					<c:choose>
						<c:when test="${ad.balcony}">
							<form:checkbox id="field-balcony" path="balcony"  checked="checked" /><label>Balcony
						or Patio</label>
						</c:when>
						<c:otherwise>
							<form:checkbox id="field-balcony" path="balcony" /><label>Balcony
						or Patio</label>
						</c:otherwise>
					</c:choose>
				</td>
			</tr>
			<tr>
				
				<td>
					<c:choose>
						<c:when test="${ad.garage}">
							<form:checkbox id="field-garage" path="garage"  checked="checked" /><label>Garage
							</label>
						</c:when>
						<c:otherwise>
							<form:checkbox id="field-garage" path="garage" /><label>Garage</label>
						</c:otherwise>
					</c:choose>
				</td>
			</tr>
			<tr>
			</tr>

		</table>
		<br />
		<form:textarea path="roomDescription" rows="10" cols="100" value="${ad.roomDescription}" />
		<form:errors path="roomDescription" cssClass="validationErrorText" />
	</fieldset>

	
	<fieldset>
		<legend>Add visiting times</legend>
		
		<table>
			<tr>
				<td>
					<input type="text" id="field-visitDay" />
					
					<select id="startHour">
 					<% 
 						for(int i = 0; i < 24; i++){
 							String hour = String.format("%02d", i);
							out.print("<option value=\"" + hour + "\">" + hour + "</option>");
 						}
 					%>
					</select>
					
					<select id="startMinutes">
 					<% 
 						for(int i = 0; i < 60; i++){
 							String minute = String.format("%02d", i);
							out.print("<option value=\"" + minute + "\">" + minute + "</option>");
 						}
 					%>
					</select>
					
					<span>to&thinsp; </span>
					
					<select id="endHour">
 					<% 
 						for(int i = 0; i < 24; i++){
 							String hour = String.format("%02d", i);
							out.print("<option value=\"" + hour + "\">" + hour + "</option>");
 						}
 					%>
					</select>
					
					<select id="endMinutes">
 					<% 
 						for(int i = 0; i < 60; i++){
 							String minute = String.format("%02d", i);
							out.print("<option value=\"" + minute + "\">" + minute + "</option>");
 						}
 					%>
					</select>
			

					<div id="addVisitButton" class="smallPlusButton">+</div>
					
					<div id="addedVisits"></div>
				</td>
				
			</tr>
			
		</table>
		<br>
	</fieldset>

	<br />

	<fieldset>
		<legend>Change pictures</legend>
		<h3>Delete existing pictures</h3>
		<br />
		<div>
			<c:forEach items="${ad.pictures }" var="picture">
				<div class="pictureThumbnail">
					<div>
					<img src="${picture.filePath}" />
					</div>
					<button type="button" data-ad-id="${ad.id }" data-picture-id="${picture.id }">Delete</button>
				</div>
			</c:forEach>
		</div>
		<p class="clearBoth"></p>
		<br /><br />
		<hr />
		<h3>Add new pictures</h3>
		<br />
		<label for="field-pictures">Pictures</label> <input
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

	<div>
		<button type="submit">Submit</button>
		<a href="<c:url value='/ad?id=${ad.id}' />"> 
			<button type="button">Cancel</button>
		</a>
	</div>

</form:form>
--%>

<c:import url="template/footer.jsp" />

