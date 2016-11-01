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
					<spring:bind path="prize">
						<div class="form-group ${status.error ? 'has-error' : '' }">
							<label for="prizeInput">Price (max.)</label>
							<div class="input-group">
								<span class="input-group-addon">Fr.</span>
								<form:input id="prizeInput" type="number" path="prize"
									placeholder="e.g. 5" step="50" cssClass="form-control" />
								<form:errors path="prize" />
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

					<div class="form-group">
						<label for="field-floorLevelMin">Floor level (min)</label>
						<form:input type="number" step="1" id="field-floorLevelMin"
							path="floorLevelMin" cssClass="form-control" />
					</div>

					<div class="form-group">
						<label for="field-floorLevelMax">Floor level (max)</label>
						<form:input type="number" step="1" id="field-floorLevelMax"
							path="floorLevelMax" cssClass="form-control" />
					</div>
					
					<div class="form-group">
						<label for="field-NumberOfBathMin">Number of Bath (min)</label>
						<input type="number" class="form-control" id="field-NumberOfBathMin">
					</div>
					<div class="form-group">
						<label for="field-NumberOfBathMax">Number of Bath (max)</label>
						<input type="number" class="form-control" id="field-NumberOfBathMax">
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
				<div class="row bottom15">
					<div class="col-xs-12">
						<div class="btn-group">
							<button type="button" class="btn btn-default">All</button>
							<button type="button" class="btn btn-default">Buy
								directly</button>
							<button type="button" class="btn btn-default">Buy by
								auction</button>
						</div>
					</div>
				</div>
				<div class="row" id="resultsDiv">
					<c:forEach var="ad" items="${results}">
						<div data-price="${ad.prizePerMonth}"
							data-moveIn="${ad.moveInDate}" data-age="${ad.moveInDate}"
							class="col-xs-12 col-sm-12 col-md-12 col-lg-12 ad-wide-preview-outer resultAd">
							<div class="col-md-12 ad-wide-preview-inner">
								<div class="row">
									<div class="col-sm-4 col-md-4">
										<a href="<c:url value='./ad?id=${ad.id}' />"> <img
											class="img-responsive" src="${ad.pictures[0].filePath}" />
										</a>
									</div>
									<div class="col-sm-5 col-md-5">
										<p>
											<strong> <a class="link"
												href="<c:url value='./ad?id=${ad.id}' />">${ad.title}</a>
											</strong>
										</p>
										<p>${ad.street},&nbsp;${ad.zipcode}&nbsp;${ad.city}</p>
										<p>
											<i>${ad.type.name}</i>
										</p>
										<strong>CHF ${ad.prizePerMonth }</strong>
										<fmt:formatDate value="${ad.moveInDate}"
											var="formattedMoveInDate" type="date" pattern="dd.MM.yyyy" />
										<p>Move-in date: ${formattedMoveInDate }</p>
									</div>
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