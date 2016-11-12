<%@page import="ch.unibe.ese.team3.model.Ad"%>
<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>

<!-- check if user is logged in -->
<security:authorize var="loggedIn" url="/profile" />

<c:import url="template/header.jsp" />

<ol class="breadcrumb">
	<li><a href="./">Homepage</a></li>
	<li class="active">Ad Description</li>
</ol>

<script src="/js/adDescription.js"></script>

<script>
	<%-- defines functionality of bid button--%>
	function bidButton(){
		print("HelloWorld");
	}
	
	var shownAdvertisementID = "${shownAd.id}";
	var shownAdvertisement = "${shownAd}";
	
	function attachBookmarkClickHandler(){
		$("#bookmarkButton").click(function() {
			
			$.post("/bookmark", {id: shownAdvertisementID, screening: false, bookmarked: false}, function(data) {
				$('#bookmarkButton').replaceWith($('<button class="btn btn-default active" id="bookmarkedButton">' + "Bookmarked" + '</button>'));
				switch(data) {
				case 0:
					alert("You must be logged in to bookmark ads.");
					break;
				case 1:
					// Something went wrong with the principal object
					alert("Return value 1. Please contact the WebAdmin.");
					break;
				case 3:
					$('#bookmarkButton').replaceWith($('<button class="btn btn-default active" id="bookmarkedButton">' + "Bookmarked" + '</button>'));
					break;
				default:
					alert("Default error. Please contact the WebAdmin.");	
				}
				
				attachBookmarkedClickHandler();
			});
		});
	}
	
	function attachBookmarkedClickHandler(){
		$("#bookmarkedButton").click(function() {
			$.post("/bookmark", {id: shownAdvertisementID, screening: false, bookmarked: true}, function(data) {
				$('#bookmarkedButton').replaceWith($('<button class="btn btn-default" id="bookmarkButton"><span class="glyphicon glyphicon-star"></span> ' + "Bookmark Ad" + '</button>'));
				switch(data) {
				case 0:
					alert("You must be logged in to bookmark ads.");
					break;
				case 1:
					// Something went wrong with the principal object
					alert("Return value 1. Please contact the WebAdmin.");
					break;
				case 2:
					$('#bookmarkedButton').replaceWith($('<button class="btn btn-default" id="bookmarkButton"><span class="glyphicon glyphicon-star"></span> ' + "Bookmark Ad" + '</button>'));
					break;
				default:
					alert("Default error. Please contact the WebAdmin.");
					
				}			
				attachBookmarkClickHandler();
			});
		});
	}

	$(document).ready(function() {
		attachBookmarkClickHandler();
		attachBookmarkedClickHandler();
		
		$.post("/bookmark", {id: shownAdvertisementID, screening: true, bookmarked: true}, function(data) {
			if(data == 3) {
				$('#bookmarkButton').replaceWith($('<button class="btn btn-default active" id="bookmarkedButton">' + "Bookmarked" + '</button>'));
				attachBookmarkedClickHandler();
			}
		});
		
		$("#messageSend").click(function (){
			var valid = true;
			
			var subjectControl = $("#msgSubject");
			var messageControl = $("#msgTextarea");
			
			if(subjectControl.val() == ""){
				valid = false;
				subjectControl.parent().addClass('has-error');
			}
			else {
				subjectControl.parent().removeClass('has-error');
			}
				
			
			if(messageControl.val() == ""){
				valid = false;
				messageControl.parent().addClass('has-error');
			}
			else {
				messageControl.parent().removeClass('has-error');
			}
		
			if (valid == true){
				var subject = subjectControl.val();
				var text = messageControl.val();
				var recipientEmail = "${shownAd.user.username}";
				$.post("./profile/messages/sendMessage", {subject : subject, text: text, recipientEmail : recipientEmail}, function(){
					subjectControl.val("");
					messageControl.val("");
					$('#messageModal').modal('hide')
				});
			}
		});
	});
		
</script>


<!-- format the dates -->
<fmt:formatDate value="${shownAd.moveInDate}" var="formattedMoveInDate"
	type="date" pattern="dd.MM.yyyy" />
<fmt:formatDate value="${shownAd.creationDate}"
	var="formattedCreationDate" type="date" pattern="dd.MM.yyyy" />
<%--- 
<c:choose>
	<c:when test="${empty shownAd.moveOutDate }">
		<c:set var="formattedMoveOutDate" value="unlimited" />
	</c:when>
	<c:otherwise>
		<fmt:formatDate value="${shownAd.moveOutDate}"
			var="formattedMoveOutDate" type="date" pattern="dd.MM.yyyy" />
	</c:otherwise>
</c:choose>
--%>
<div class="row">
	<div class="col-md-12 col-xs-12">
		<h3 id="shownAdTitle">${shownAd.title}</h3>
		<div class="btn-group bottom15">
			<c:choose>
				<c:when test="${loggedIn}">
					<c:choose>
						<c:when test="${loggedInUserEmail != shownAd.user.username }">
							<button class="btn btn-default" id="bookmarkButton">
								<span class="glyphicon glyphicon-star"></span> Bookmark Ad
							</button>
						</c:when>
						<c:otherwise>
							<a class="btn btn-primary"
								href="./profile/editAd?id=${shownAd.id}">Edit Ad</a>
						</c:otherwise>
					</c:choose>
				</c:when>
			</c:choose>
		</div>
		<div class="row">
			<div class="col-xs-12 col-sm-6 col-md-6 col-lg-6">
				<div class="panel panel-default">
					<div class="panel-body">
						<div class="row bottom15">
							<div class="col-sm-8">
								<div class="row">
									<div class="col-sm-3">
										<strong>Type</strong>
									</div>
									<div class="col-sm-9">${shownAd.type.name}</div>
								</div>
								<div class="row">
									<div class="col-sm-3">
										<strong>Address</strong>
									</div>
									<div class="col-sm-9">
										<a target="_blank"
											href="http://maps.google.com/?q=${shownAd.street},&nbsp;${shownAd.zipcode}, ${shownAd.city}">${shownAd.street},
											${shownAd.zipcode}&nbsp;${shownAd.city}</a>
									</div>
								</div>
								<div class="row">
									<div class="col-sm-3">
										<strong>Rooms</strong>
									</div>
									<div class="col-sm-9">${shownAd.numberOfRooms}</div>
								</div>
							</div>
							<div class="col-sm-4">
								<div class="pull-right">
									<h3>${shownAd.price}&#32;CHF</h3>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-12">
								<h4>Description</h4>
								<p>${shownAd.getRoomDescriptionWithLineBreaks()}</p>
							</div>
						</div>
					</div>
				</div>


				<h4>Details</h4>
				<table id="adDescTable" class="table table-striped">
					<tr>
						<th>Available from</th>
						<td>${formattedMoveInDate}</td>
					</tr>


					<tr>
						<th>Square meters</th>
						<td>${shownAd.squareFootage}&#32;m²</td>
					</tr>

					<tr>
						<th>Number of bath rooms</th>
						<td>${shownAd.numberOfBath}</td>
					</tr>

					<tr>
						<th>Internet/TV infrastructure</th>
						<td>${shownAd.infrastructureType.name}</td>
					</tr>

					<tr>
						<th>Distance to school</th>
						<td>${shownAd.getDistanceSchoolAsEnum().name}</td>
					</tr>
					<tr>
						<th>Distance to shopping center</th>
						<td>${shownAd.getDistanceShoppingAsEnum().name}</td>
					</tr>
					<tr>
						<th>Distance to public transport</th>
						<td>${shownAd.getDistancePublicTransportAsEnum().name}</td>
					</tr>

					<tr>
						<th>Year of construction</th>
						<td>${shownAd.buildYear}</td>
					</tr>
					<tr>
						<th>Year of renovation</th>
						<td>${shownAd.renovationYear}</td>
					</tr>
					<tr>
						<th>Floor level</th>
						<td>${shownAd.floorLevel}</td>
					</tr>
				</table>
				<h4>Additional information</h4>
				<table class="table">
					<tr>
						<td><span
							class="glyphicon glyphicon-${ shownAd.garage ? 'ok': 'remove' }"></span>
							Garage</td>
						<td><span
							class="glyphicon glyphicon-${ shownAd.parking ? 'ok': 'remove' }"></span>
							Parking</td>
					</tr>
					<tr>
						<td><span
							class="glyphicon glyphicon-${ shownAd.balcony ? 'ok': 'remove' }"></span>
							Balcony</td>
						<td><span
							class="glyphicon glyphicon-${ shownAd.elevator ? 'ok': 'remove' }"></span>
							Elevator</td>
					</tr>
					<tr>
						<td><span
							class="glyphicon glyphicon-${ shownAd.dishwasher ? 'ok': 'remove' }"></span>
							Dishwasher</td>
						<td></td>
					</tr>
				</table>
				<p class="bottom15">
					<span class="glyphicon glyphicon-ok"></span> = Available, <span
						class="glyphicon glyphicon-remove"></span> = Not available
				</p>
				<h4>Visiting times</h4>
				<c:choose>
					<c:when test="${empty visits }">
						<p>No visiting times available</p>
					</c:when>
					<c:otherwise>
						<table class="table table-striped" id="visitList">
							<c:forEach items="${visits }" var="visit">
								<tr>
									<td><fmt:formatDate value="${visit.startTimestamp}"
											pattern="dd-MM-yyyy " /> &nbsp; from <fmt:formatDate
											value="${visit.startTimestamp}" pattern=" HH:mm " /> until <fmt:formatDate
											value="${visit.endTimestamp}" pattern=" HH:mm" /></td>
									<td><c:choose>
											<c:when test="${loggedIn}">
												<c:if test="${loggedInUserEmail != shownAd.user.username}">
													<button class="btn btn-primary" type="button"
														data-id="${visit.id}" onclick="sendEnquiry(${visit.id});">Send
														enquiry to advertiser</button>
												</c:if>
											</c:when>
											<c:otherwise>
												<a href="./login"><button class="btn btn-default"
														type="button" data-id="${visit.id}">Login to send
														enquiries</button></a>
											</c:otherwise>
										</c:choose></td>
								</tr>
							</c:forEach>
						</table>
					</c:otherwise>
				</c:choose>
			</div>
			<div class="col-xs-12 col-sm-6 col-md-6 col-lg-6">
				<c:if test="${ not empty shownAd.pictures }">
					<div id="carousel-example-generic" class="carousel slide bottom15"
						data-ride="carousel">
						<!-- Indicators -->
						<ol class="carousel-indicators">
							<c:forEach var="picture" items="${shownAd.pictures}"
								varStatus="loop">
								<li data-target="#carousel-example-generic"
									data-slide-to="${loop.index}"
									class=" ${loop.index == 0 ? 'active' : ''}"></li>
							</c:forEach>
						</ol>

						<!-- Wrapper for slides -->
						<div class="carousel-inner" role="listbox">
							<c:forEach items="${shownAd.pictures}" var="picture"
								varStatus="loop">
								<div class="item ${loop.index == 0 ? 'active' : '' }">
									<img src="${picture.filePath}">
								</div>
							</c:forEach>
						</div>

						<!-- Controls -->
						<a class="left carousel-control" href="#carousel-example-generic"
							role="button" data-slide="prev"> <span
							class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
							<span class="sr-only">Previous</span>
						</a> <a class="right carousel-control"
							href="#carousel-example-generic" role="button" data-slide="next">
							<span class="glyphicon glyphicon-chevron-right"
							aria-hidden="true"></span> <span class="sr-only">Next</span>
						</a>
					</div>
				</c:if>
				<div class="panel panel-default">
					<div class="panel-body">
						<div class="row">
							<div class="col-sm-4">
								<img class="img-responsive img-circle"
									src="${shownAd.user.picture.filePath != null ? shownAd.user.picture.filePath : '/img/avatar.png'}" />
							</div>
							<div class="col-sm-8">
								<h4>Advertiser</h4>
								<h5>${shownAd.user.firstName}${shownAd.user.lastName}</h5>
								<p>${shownAd.user.username}</p>
								<div class="btn-group">
									<c:choose>
										<c:when test="${loggedIn}">
											<a class="btn btn-default"
												href="./user?id=${shownAd.user.id}"> <span
												class="glyphicon glyphicon-user"></span> Visit profile
											</a>
										</c:when>
										<c:otherwise>
											<a class="btn btn-default" href="./login">Login to visit
												profile</a>
										</c:otherwise>
									</c:choose>
									<c:choose>
										<c:when test="${loggedIn}">
											<c:if test="${loggedInUserEmail != shownAd.user.username }">
												<button class="btn btn-default" id="newMsg" type="button"
													data-toggle="modal" data-target="#messageModal">
													<span class="glyphicon glyphicon-envelope"></span> Contact
													Advertiser
												</button>
											</c:if>
										</c:when>
										<c:otherwise>
											<a class="btn btn-default" href="./login">Login to
												contact</a>
										</c:otherwise>
									</c:choose>
								</div>
							</div>
						</div>
					</div>
				</div>


				<c:if
					test="${shownAd.auction  && shownAd.isAuctionRunning() && loggedInUserEmail != shownAd.user.username}">
					<div class="panel panel-default">
						<div class="panel-body">
							<div class="row">
								<div class="col-sm-12">
									<h4>Auction</h4>
								</div>
							</div>
							<div class="row">
								<div class="col-sm-4">
									<fmt:formatDate value="${shownAd.endDate}"
										var="formattedEndDate" type="date" pattern="dd.MM.yyyy" />
									<p>Running until: ${formattedEndDate}</p>

									<p>
										Current price: <strong>${shownAd.currentAuctionPrice  - shownAd.increaseBidPrice}
											CHF</strong>
									</p>
								</div>
								<!-- Exception is thrown if button is pressed without login -->

								<!-- 2 forms are created: one for buy, one for bid. The buy / bid buttons submit the form to the controller 
									creates different forms when user is logged in, and when user is not logged in
									
									Better alternative to avoid duplicated code?-->

								<div class="col-sm-8">

									<form:form name="AuctionBid" action="./profile/bidAuction"
										method="post">
										<input type="hidden" name="id" value="${shownAd.id }" />
										<input type="hidden" name="amount"
											value="${shownAd.currentAuctionPrice}" />
										<%-- fehlt noch: if not logged in-> you can not bid, und wirst zur Login Seite umgeleitet beim Klicken auf bid --%>
										<div class="form-group">
											<label class="sr-only" for="bid">Amount</label>
											<%-- for="bid" stimmt wahrscheinlich nicht --%>
											<div class="input-group">
												<div class="input-group-addon">CHF</div>
												<%--<input type="number" class="form-controll" placeholder="Amount" name="bid"> es fehlt: id = und value= --%>
												<input class="form-control" id="disabledInput" type="text"
													placeholder=${shownAd.currentAuctionPrice } disabled>
												<span class="input-group-btn"> <c:choose>
														<c:when test="${loggedIn }">
															<button type="submit" class="btn btn-success">Bid</button>
														</c:when>
														<c:otherwise>
															<a href="./login" class="btn btn-success">Bid</a>
														</c:otherwise>
													</c:choose>
												</span>
											</div>
										</div>
									</form:form>


									<!-- 2nd form -->
									<form:form name="AuctionBuy" action="./profile/buyAuction"
										method="post">
										<input type="hidden" name="id" value="${shownAd.id }" />
										<input type="hidden" name="amount"
											value="${shownAd.price}" />
										<div class="form-group">
											<label class="sr-only" for="exampleInputAmount">Buy
												now Price in CHF</label>
											<%-- for stimmt wahrscheinlich nicht --%>
											<%-- fehlt noch: if not logged in-> you can not bid, und wirst zur Login Seite umgeleitet beim Klicken auf bid --%>
											<div class="input-group">
												<div class="input-group-addon">CHF</div>
												<input class="form-control" id="disabledInput" type="text"
													placeholder=${shownAd.price } disabled> <span
													class="input-group-btn"> <c:choose>
														<c:when test="${loggedIn }">
															<button type="submit" class="btn btn-success">Buy</button>
														</c:when>
														<c:otherwise>
															<a href="./login" class="btn btn-success">Buy</a>
														</c:otherwise>
													</c:choose>
												</span>
											</div>
										</div>
									</form:form>
								</div>

							</div>
						</div>
					</div>


				</c:if>





			</div>
		</div>




	</div>
</div>

<div class="modal fade" id="messageModal" tabindex="-1">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="myModalLabel">Contact the
					advertiser</h4>
			</div>
			<div class="modal-body">
				<form class="form">
					<div class="form-group">
						<label for="msgSubject">Subject</label> <input
							class="form-control" type="text" id="msgSubject"
							placeholder="Subject">
					</div>
					<div class="form-group">
						<label for="msgTextarea">Message</label>
						<textarea id="msgTextarea" class="form-control"
							placeholder="Message"></textarea>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
				<button type="button" class="btn btn-primary" id="messageSend">Send</button>
			</div>
		</div>
	</div>
</div>

<c:import url="template/footer.jsp" />