<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>


<c:import url="template/header.jsp" />
<ol class="breadcrumb">
	<li><a href="./">Homepage</a></li>
	<li><a href="./searchAd/">Find ad</a></li>
	<li class="active">Results</li>
</ol>

<script>
	/*
	 * This script takes all the resultAd divs and sorts them by a parameter specified by the user.
	 * No arguments need to be passed, since the function simply looks up the dropdown selection.
	 */
	function sort_div_attribute() {
		//determine sort modus (by which attribute, asc/desc)
		var sortmode = $('#modus').find(":selected").val();

		//only start the process if a modus has been selected
		if (sortmode.length > 0) {
			var attname;

			//determine which variable we pass to the sort function
			if (sortmode == "price_asc" || sortmode == "price_desc")
				attname = 'data-price';
			else if (sortmode == "moveIn_asc" || sortmode == "moveIn_desc")
				attname = 'data-moveIn';
			else
				attname = 'data-age';

			//copying divs into an array which we're going to sort
			var divsbucket = new Array();
			var divslist = $('div.resultAd');
			var divlength = divslist.length;
			for (a = 0; a < divlength; a++) {
				divsbucket[a] = new Array();
				divsbucket[a][0] = divslist[a].getAttribute(attname);
				divsbucket[a][1] = divslist[a];
				divslist[a].remove();
			}

			//sort the array
			divsbucket.sort(function(a, b) {
				if (a[0] == b[0])
					return 0;
				else if (a[0] > b[0])
					return 1;
				else
					return -1;
			});

			//invert sorted array for certain sort options
			if (sortmode == "price_desc" || sortmode == "moveIn_asc"
					|| sortmode == "dateAge_asc")
				divsbucket.reverse();

			//insert sorted divs into document again
			for (a = 0; a < divlength; a++)
				$("#resultsDiv").append($(divsbucket[a][1]));
		}
	}
</script>

<script>
	$(document).ready(function() {
		$("#cityInput").autocomplete({
			minLength : 2,
			source : <c:import url="getzipcodes.jsp" />,
			enabled : true,
			autoFocus : true
		});

		$("#field-earliestMoveInDate").datepicker({
			dateFormat : 'dd-mm-yy'
		});
		$("#field-latestMoveInDate").datepicker({
			dateFormat : 'dd-mm-yy'
		});
	});
</script>

<div class="row">

	<div class="col-xs-12 col-sm-12 col-md-4 col-ls-4">
		<h4>Filter results</h4>
		<div class="panel panel-default form-inline">
			<div class="panel-body">
				<div class="form-group">
					<select id="modus" class="form-control">
						<option value="">Sort by:</option>
						<option value="price_asc">Price (ascending)</option>
						<option value="price_desc">Price (descending)</option>
						<option value="moveIn_desc">Move-in date (earliest to
							latest)</option>
						<option value="moveIn_asc">Move-in date (latest to
							earliest)</option>
						<option value="dateAge_asc">Date created (youngest to
							oldest)</option>
						<option value="dateAge_desc">Date created (oldest to
							youngest)</option>
					</select>
					<button class="btn btn-default" onClick="sort_div_attribute()">Sort</button>
				</div>
			</div>
		</div>
		<form:form method="post" modelAttribute="searchForm"
			action="./results" id="filterForm" autocomplete="off">
			<div class="panel panel-default">
				<div class="panel-body">
					<div class="form-group">
						<label>Type</label>
						<c:forEach var="type" items="${types}">
							<div class="checkbox">
								<label> <form:checkbox path="types" value="${type}" />
									${type.name}
								</label>
							</div>
						</c:forEach>
					</div>
					<spring:bind path="city">
						<div class="form-group ${status.error ? 'has-error' : '' }">
							<label for="cityInput">City / zip code</label>
							<form:input type="text" name="cityInput" id="cityInput"
								path="city" placeholder="e.g. Bern" cssClass="form-control" />
							<form:errors path="city" />
						</div>
					</spring:bind>
					<spring:bind path="radius">
						<div class="form-group ${status.error ? 'has-error' : '' }">
							<label for="radiusInput">Within radius of (max.)</label>
							<div class="input-group">
								<form:input id="radiusInput" type="number" path="radius"
									placeholder="e.g. 5" step="5" cssClass="form-control" />
								<span class="input-group-addon">km</span>
								<form:errors path="radius" />
							</div>
						</div>
					</spring:bind>
					<spring:bind path="price">
						<div class="form-group ${status.error ? 'has-error' : '' }">
							<label for="priceInput">Price (max.)</label>
							<div class="input-group">
								<span class="input-group-addon">Fr.</span>
								<form:input id="priceInput" type="number" path="price"
									placeholder="e.g. 5" step="50" cssClass="form-control" />
								<form:errors path="price" />
							</div>
						</div>
					</spring:bind>
					<div class="form-group">

						<label for="earliestMoveInDate">Earliest move-in date</label>
						<form:input type="text" id="field-earliestMoveInDate"
							path="earliestMoveInDate" cssClass="form-control" />
					</div>
					<div class="form-group">
						<label for="latestMoveInDate">Latest move-in date</label>
						<form:input type="text" id="field-latestMoveInDate"
							path="latestMoveInDate" cssClass="form-control" />
					</div>
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
						<label><form:checkbox id="field-elevator" path="elevator"
								value="1" />Elevator</label>
					</div>

					<div class="checkbox">
						<label><form:checkbox id="field-dishwasher"
								path="dishwasher" value="1" />Dishwasher</label>
					</div>

					<spring:bind path="infrastructureType">
						<div class="form-group ${status.error ? 'has-error' : '' }">
							<label class="control-label" for="infrastructureType-room">InfrastructureType</label>
							<form:select id="infrastructureType" path="infrastructureType"
								cssClass="form-control">
								<option value=""></option>
								<form:options items="${infrastructureTypes}" itemLabel="name" />
							</form:select>
						</div>
					</spring:bind>

					<div class="form-group row">
						<label for="field-floorLevelMin" class="col-md-6">Floor
							level between</label>
						<div class="col-md-6 form-inline">
							<form:input type="number" step="1" id="field-floorLevelMin"
								path="floorLevelMin" cssClass="form-control input60" />
							<label for="field-floorLevelMax" class="betweenLabel"> -
							</label>
							<form:input type="number" step="1" id="field-floorLevelMax"
								path="floorLevelMax" cssClass="form-control input60" />
						</div>
					</div>


					<div class="form-group row ">
						<label for="field-NumberOfBathMin" class="col-md-6">Nr. of
							Bath between</label>

						<div class="col-md-6 form-inline">
							<form:input type="number" step="1" id="field-NumberOfBathMin"
								path="numberOfBathMin" cssClass="form-control input60 " />
							<label for="field-NumberOfBathMax" class="betweenLabel">
								- </label>
							<form:input type="number" cssClass="form-control input60"
								path="numberOfBathMax" id="field-NumberOfBathMax" />
						</div>

					</div>



					<div class="form-group row">

						<label class="col-md-6" for="field-NumberOfRoomsMin">Nr.
							of Rooms between</label>
						<div class="col-md-6 form-inline">
							<form:input type="number" cssClass="form-control input60"
								path="numberOfRoomsMin" id="field-NumberOfRoomsMin" />
							<label for="field-NumberOfRoomsMax" class="betweenLabel">
								- </label>
							<form:input type="number" cssClass="form-control input60"
								path="numberOfRoomsMax" id="field-NumberOfRoomsMax" />
							<%-- muss man <form_error/> auch noch hinzufügen? --%>
						</div>
					</div>



					<div class="form-group row">
						<label class="col-md-6" for="field-BuildYearMin">Build
							year between</label>
						<div class="col-md-6 form-inline">
							<form:input type="number" cssClass="form-control input60"
								path="buildYearMin" id="field-BuildYearMin" />
							<label for="field-BuildYearMax" class="betweenLabel"> - </label>
							<form:input type="number" cssClass="form-control input60"
								path="buildYearMax" id="field-BuildYearMax" />
						</div>

					</div>


					<div class="form-group row">
						<label class="col-md-6" for="field-RenovationYearMin">Renovation
							year between</label>
						<div class="col-md-6 form-inline">
							<form:input type="number" cssClass="form-control input60"
								path="renovationYearMin" id="field-RenovationYearMin" />
							<label for="field-RenovationYearMax" class="betweenLabel">
								- </label>
							<form:input type="number" cssClass="form-control input60"
								path="renovationYearMax" id="field-RenovationYearMax" />
						</div>
					</div>


					<div class="form-group row">
						<label class="col-md-6" for="field-DistanceSchoolMin">Distance
							to school from</label>
						<div class="col-md-6 form-inline">
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




					<div class="form-group row">

						<label class="col-md-6" for="field-DistanceShoppingMin">Distance
							to shopping from</label>
						<div class="col-md-6 form-inline">
							<form:input id="field-DistanceShoppingMin" type="number" min="0"
								path="distanceShoppingMin" placeholder="0" step="100"
								cssClass="form-control input60" />

							<label for="field-DistanceShoppingMax" class="betweenLabel">
								- </label>
							<form:input id="field-DistanceShoppingMax" type="number" min="0"
								path="distanceShoppingMax" placeholder="0" step="100"
								cssClass="form-control input60" />
						</div>
					</div>

					<div class="form-group row">
						<label class="col-md-6" for="field-DistancePublicTransportMin">Distance
							to public transport from</label>
						<div class="col-md-6 form-inline">
							<form:input id="field-DistancePublicTransportMin" type="number"
								min="0" path="distancePublicTransportMin" placeholder="0"
								step="100" cssClass="form-control input60" />

							<label for="field-DistancePublicTransportMax"
								class="betweenLabel"> - </label>

							<form:input id="field-DistancePublicTransportMax" type="number"
								min="0" path="distancePublicTransportMax" placeholder="0"
								step="100" cssClass="form-control input60" />

						</div>
					</div>




				</div>
			</div>
			<div class="form-group pull-right">
				<button type="reset" class="btn btn-default">Cancel</button>
				<button type="submit" class="btn btn-primary">Filter</button>
			</div>
		</form:form>
	</div>

	<div class="col-xs-12 col-sm-12 col-md-8 col-ls-8">
		<h4>Results</h4>
		<c:choose>
			<c:when test="${empty results}">
				<p>No results found!
			</c:when>
			<c:otherwise>
				<!-- 				<div class="row bottom15"> -->
				<!-- 					<div class="col-xs-12"> -->
				<!-- 						<div class="btn-group"> -->
				<!-- 							<button type="button" class="btn btn-default">All</button> -->
				<!-- 							<button type="button" class="btn btn-default">Buy -->
				<!-- 								directly</button> -->
				<!-- 							<button type="button" class="btn btn-default">Buy by -->
				<!-- 								auction</button> -->
				<!-- 						</div> -->
				<!-- 					</div> -->
				<!-- 				</div> -->
				<div class="row" id="resultsDiv">
					<c:forEach var="ad" items="${results}">
						<div data-price="${ad.price}"
							data-moveIn="${ad.moveInDate}" data-age="${ad.moveInDate}"
							class="col-xs-12 col-sm-12 col-md-12 col-lg-12 ad-wide-preview-outer resultAd ${ad.isPremiumAd() ? 'premiumAd' : '' }">
							<div class="col-md-12 ad-wide-preview-inner">
								<div class="row">
									<div class="col-sm-4 col-md-4">
										<a href="<c:url value='./ad?id=${ad.id}' />"> <img
											class="img-responsive" src="${ad.pictures[0].filePath}" />
										</a>
									</div>
									<div class="col-sm-4 col-md-4">
										<p>
											<strong> <a class="link"
												href="<c:url value='./ad?id=${ad.id}' />">${ad.title}</a>
											</strong>
										</p>
										<p>${ad.street},&nbsp;${ad.zipcode}&nbsp;${ad.city}</p>
										<p>
											<i>${ad.type.name}</i>
										</p>
										<strong>CHF ${ad.price }</strong>
										<fmt:formatDate value="${ad.moveInDate}"
											var="formattedMoveInDate" type="date" pattern="dd.MM.yyyy" />
										<p>Move-in date: ${formattedMoveInDate }</p>
									</div>

									<c:if test="${ad.auction  && ad.isAuctionRunning() && loggedInUserEmail != ad.user.username }">

										<div class="col-sm-4 col-md-4 auction-Column">
											<p>
												<strong>Auction</strong>
											</p>
											<fmt:formatDate value="${ad.startDate}"
												var="formattedStartDate" type="date" pattern="dd.MM.yyyy" />
											<p>Running until: ${formattedStartDate}</p>

											<p>
												Current price: <strong>${ad.currentAuctionPrice - ad.increaseBidPrice}
													CHF</strong>
											</p>
											<p>
												<a href="./ad?id=${ad.id}">Bid</a>
											</p>
											<%-- 	
											<form:form method="post" modelAttribute="placeAdForm"
												action="./results" id="bidForm" autocomplete="off">
												<div class="form-group">
													<label class="sr-only" for="bid">Amount</label>
													<!-- for="bid" stimmt wahrscheinlich nicht -->
													<div class="input-group">
														<div class="input-group-addon">CHF</div>
														<!--<input type="number" class="form-controll" placeholder="Amount" name="bid"> es fehlt: id = und value= -->
														<!-- fehlt noch: if not logged in-> you can not bid, und wirst zur Login Seite umgeleitet beim Klicken auf bid -->
														<input class="form-control" id="disabledInput" type="text"
															placeholder=${ad.bidPriceForUser } disabled> <span
															class="input-group-btn">
															<button type="button" class="btn btn-success">Bid</button>
														</span>

													</div>
												</div>
											</form:form>

											<form:form method="post" modelAttribute="placeAdForm"
												action="./results" id="buyForm" autocomplete="off">
												<div class="form-group">
													<label class="sr-only" for="exampleInputAmount">Buy
														now Price in CHF</label>
													<!-- for stimmt wahrscheinlich nicht -->
													<!-- fehlt noch: if not logged in-> you can not bid, und wirst zur Login Seite umgeleitet beim Klicken auf bid -->
													<div class="input-group">
														<div class="input-group-addon">CHF</div>
														<input class="form-control" id="disabledInput" type="text"
															placeholder=${ad.buyItNowPrice } disabled> <span
															class="input-group-btn">
															<button type="button" class="btn btn-success">Buy
															</button>
														</span>
													</div>
												</div>
											</form:form>
											--%>
										</div>
									</c:if>
								</div>
							</div>
							<div class="clearfix"></div>
						</div>
					</c:forEach>
				</div>
			</c:otherwise>
		</c:choose>
	</div>
</div>
<c:import url="template/footer.jsp" />