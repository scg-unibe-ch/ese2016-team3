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
<script src="/js/placeAd.js"></script>

<script>
	$(document).ready(function(){
		$("#field-city").autocomplete({
								minLength : 2,
								source : <c:import url="getzipcodes.jsp" />,
								enabled : true,
								autoFocus : true
							});
	});
</script>


<ol class="breadcrumb">
	<li><a href="/${pagemode}/">Homepage</a></li>
	<li class="active">Place ad</li>
</ol>


<div class="row">
	<div class="col-md-12 col-xs-12">

		<h3>Place an ad for ${ pagemode eq 'buy' ? 'buying' : 'renting' }</h3>
		<div class="row">
			<div class="col-md-12">
				<form:form method="post" modelAttribute="placeAdForm"
					action="./placeAd" id="placeAdForm" autocomplete="off"
					cssClass="form form-horizontal">
					<h4>General information</h4>

					<div class="panel panel-default">
						<div class="panel-body">

							<spring:bind path="title">
								<div class="form-group ${status.error ? 'has-error' : '' }">
									<label class="col-sm-3 control-label" for="field-title">Ad
										Title</label>
									<div class="col-sm-5">
										<form:input type="text" id="field-title" path="title"
											cssClass="form-control" placeholder="Title" />
										<form:errors path="title" cssClass="text-danger" />
									</div>
								</div>
							</spring:bind>

							<spring:bind path="type">
								<div class="form-group ${status.error ? 'has-error' : '' }">
									<label class="col-sm-3 control-label" for="type-room">Type</label>
									<div class="col-sm-5">
										<form:select path="type" id="type" cssClass="form-control">
											<form:options items="${types}" itemLabel="name" />
										</form:select>
									</div>
								</div>
							</spring:bind>

							<spring:bind path="street">
								<div class="form-group ${status.error ? 'has-error' : '' }">
									<label class="col-sm-3 control-label" for="field-street">Street</label>
									<div class="col-sm-5">
										<form:input type="text" id="field-street" path="street"
											cssClass="form-control" placeholder="Street" />
									</div>
								</div>
							</spring:bind>

							<spring:bind path="city">
								<div class="form-group ${status.error ? 'has-error' : '' }">

									<label class="col-sm-3 control-label" for="field-city">City
										/ zip code</label>
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
										date</label>
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
										Meters</label>
									<div class="col-sm-5">
										<form:input id="field-SquareFootage" type="number" min="0"
											path="squareFootage" placeholder="Square footage" step="5"
											cssClass="form-control" />
										<form:errors path="squareFootage" cssClass="text-danger" />
									</div>
								</div>
							</spring:bind>

							<spring:bind path="numberOfRooms">
								<div class="form-group ${status.error ? 'has-error' : '' }">
									<label class="col-sm-3 control-label" for="field-NumberOfRooms">Number
										of rooms</label>
									<div class="col-sm-5">
										<form:input id="field-NumberOfRooms" type="number" min="0"
											path="numberOfRooms" placeholder="numberOfRooms" step="1"
											cssClass="form-control" />
										<form:errors path="numberOfRooms" cssClass="text-danger" />
									</div>
								</div>
							</spring:bind>

							<spring:bind path="floorLevel">
								<div class="form-group ${status.error ? 'has-error' : '' }">
									<label class="col-sm-3 control-label" for="field-FloorLevel">Floor
										level </label>
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
										for="field-DistanceSchool">Distance to school</label>
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
										for="field-DistanceShopping">Distance to shopping
										center</label>
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
										for="field-DistancePublicTransport">Distance to public
										transport</label>
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
										of construction</label>
									<div class="col-sm-5">
										<form:input id="field-BuildYear" path="buildYear" min="0"
											cssClass="form-control" />
										<form:errors path="buildYear" cssClass="text-danger" />
									</div>
								</div>
							</spring:bind>
							<spring:bind path="renovationYear">
								<div class="form-group ${status.error ? 'has-error' : '' }">
									<label class="col-sm-3 control-label"
										for="field-RenovationYear">Year of renovation </label>
									<div class="col-sm-5">
										<form:input id="field-RenovationYear" path="renovationYear"
											min="0" cssClass="form-control" />
										<form:errors path="renovationYear" cssClass="text-danger" />
									</div>
								</div>
							</spring:bind>
						</div>
					</div>
					<h4>Prizing</h4>
					<div class="panel panel-default">
						<div class="panel-body">
							<c:if test="${pagemode == 'buy' }">
								<div class="form-group">
									<label class="col-sm-3 control-label">Auction</label>
									<div class="col-sm-9">
										<div class="checkbox">
											<label> <form:checkbox id="field-auction"
													path="auction" value="1" /> Yes
											</label>
										</div>
									</div>
								</div>
							</c:if>
							<div id="panel-buy-normal">
								<spring:bind path="validPrice">
									<div class="form-group ${status.error ? 'has-error' : '' }">
										<label class="col-sm-3 control-label" for="field-Price">Normal
											Price (without Auction) </label>
										<div class="col-sm-5">
											<form:input id="field-Price" type="number" min="0"
												path="price" placeholder="Price" step="1"
												cssClass="form-control" />
											<form:errors path="validPrice" cssClass="text-danger" />
										</div>
									</div>
								</spring:bind>
							</div>
							<c:if test="${pagemode == 'buy' }">
								<div id="panel-buy-auction">
									<spring:bind path="validPrice">
										<div class="form-group ${status.error ? 'has-error' : '' }">
											<label class="col-sm-3 control-label" for="field-BuyNowPrice">Immediate
												Buy Price for Auction </label>
											<div class="col-sm-5">
												<form:input id="field-BuyNowPrice" type="number" min="0"
													path="auctionPrice" placeholder="Price "
													cssClass="form-control" />
												<form:errors path="validPrice" cssClass="text-danger" />
											</div>
										</div>
									</spring:bind>

									<spring:bind path="validStartDate">
										<div class="form-group ${status.error ? 'has-error' : '' }">
											<label class="col-sm-3 control-label" for="field-startDate">Startdate
												for Auction </label>
											<div class="col-sm-5">
												<form:input type="text" id="field-startDate"
													path="startDate" cssClass="form-control" />
												<form:errors path="validStartDate" cssClass="text-danger" />
											</div>
										</div>
									</spring:bind>

									<spring:bind path="validEndDate">
										<div class="form-group ${status.error ? 'has-error' : '' }">
											<label class="col-sm-3 control-label" for="field-endDate">Enddate
												for Auction </label>
											<div class="col-sm-5">
												<form:input type="text" id="field-endDate" path="endDate"
													cssClass="form-control" />
													<form:errors path="validEndDate" cssClass="text-danger" />
											</div>
										</div>
									</spring:bind>

									<spring:bind path="validStartPrice">
										<div class="form-group ${status.error ? 'has-error' : '' }">
											<label class="col-sm-3 control-label" for="field-startPrice">Startprice
												for Auction </label>
											<div class="col-sm-5">
												<form:input id="field-startPrice" path="startPrice"
													type="number" min="0" placeholder="Startprice " step="1"
													cssClass="form-control" />
												<form:errors path="validStartPrice" cssClass="text-danger" />
											</div>
										</div>
									</spring:bind>

									<spring:bind path="validIncreaseBidPrice">
										<div class="form-group ${status.error ? 'has-error' : '' }">
											<label class="col-sm-3 control-label"
												for="field-increasePrice">Amount of increase of bid
												price </label>
											<div class="col-sm-5">
												<form:input id="field-increasePrice" path="increaseBidPrice"
													type="number" min="0" placeholder="Startprice " step="1"
													cssClass="form-control" />
												<form:errors path="validIncreaseBidPrice" cssClass="text-danger" />
											</div>
										</div>
									</spring:bind>
								</div>
							</c:if>
						</div>
					</div>
					<h4>Description</h4>
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
										<form:errors path="numberOfBath" cssClass="text-danger" />
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

							<spring:bind path="roomDescription">
								<div class="form-group ${status.error ? 'has-error' : '' }">
									<label class="col-sm-3 control-label" for="roomDescription">Description:</label>
									<div class="col-sm-5">
										<form:textarea path="roomDescription" rows="10" cols="70"
											placeholder="Description" class="form-control" />
										<form:errors path="roomDescription" cssClass="text-danger" />
									</div>
								</div>
							</spring:bind>
						</div>
					</div>
					<h4>Visiting times (optional)</h4>
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
										<span class="glyphicon glyphicon-plus"></span>
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
					<h4>Pictures (optional)</h4>
					<div class="panel panel-default">
						<div class="panel-body">
							<div class="row form-horizontal bottom15">
								<div class="col-sm-3">
									<label class="control-label" for="field-pictures">Add
										pictures</label>
								</div>
								<div class="col-sm-3">
									<input type="file" id="field-pictures" accept="image/*"
										multiple="multiple">
								</div>
							</div>
							<div class="row">
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
					</div>
					<div class="form-group pull-right">
						<div class="col-sm-12">
							<a href="/">
								<button type="reset" class="btn btn-default">Cancel</button>
							</a>
							<button type="submit" class="btn btn-primary">Submit</button>
						</div>
					</div>
				</form:form>
			</div>
		</div>
	</div>
</div>


<c:import url="template/footer.jsp" />