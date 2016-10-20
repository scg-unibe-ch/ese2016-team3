<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>

<!DOCTYPE html>
<head>
<link rel="stylesheet" type="text/css" media="screen"
	href="/css/main.css">

<Title>Compass</Title>
<script src="//ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
<script
	src="//ajax.googleapis.com/ajax/libs/jqueryui/1.11.2/jquery-ui.min.js"></script>
<link rel="stylesheet"
	href="//ajax.googleapis.com/ajax/libs/jqueryui/1.11.2/themes/smoothness/jquery-ui.css" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<link rel="stylesheet" href="/css/style.css">
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script src="/js/unreadMessages.js"></script>


<style>
/* ensure that autocomplete lists are not too long and have a scrollbar */
.ui-autocomplete {
	max-height: 200px;
	overflow-y: auto;
	overflow-x: hidden;
}
</style>

</head>

<!-- check if user is logged in -->
<security:authorize var="loggedIn" url="/profile" />



<body>
	<div class="container">
		<h1>
			<img id="logo" src="/img/logoNew.png"> Compass
		</h1>
		<ul class="nav nav-tabs" role="tablist">
			<li class="active"><a href="/">Buy</a></li>
			<li><a href="/">Rent</a></li>
		</ul>
		<nav class="navbar navbar-default">
			<ul class="nav navbar-nav">
				<li><a href="/searchAd">Find</a></li>
				<li><a href="/profile/placeAd">Place ad</a></li>
				<li><a href="#">My Auctions</a></li>
				<li class="navbar-right dropdown"><c:choose>
						<c:when test="${loggedIn}">
							<%@include file='/pages/getUserPicture.jsp'%>
							<a class="dropdown-toggle" data-toggle="dropdown" href="#"> <span
								class="glyphicon glyphicon-user"></span>
								<% out.print(String.format("%s %s", realUser.getFirstName(), realUser.getLastName())); %>
								<span class="caret"></span>
							</a>
							<ul class="dropdown-menu">
								<li><a href="/user?id=<%out.print(realUser.getId());%>">
										<span class="glyphicon glyphicon-cog"></span> Show profile
								</a></li>
								<li><a href="/profile/myRooms"> <span
										class="glyphicon glyphicon-home"></span> My Ads
								</a></li>
								<li><a href="/profile/messages"> <span
										class="glyphicon glyphicon glyphicon-envelope"></span>
										Messages <span class="badge"> <%
 	
 %>
									</span>
								</a>
								<li><a href="/profile/alerts"> <span
										class="glyphicon glyphicon-alert"></span> Alerts
								</a></li>
								<li><a href="/profile/enquiries"> <span
										class="glyphicon glyphicon-log-out"></span> Enquiries
								</a></li>
								<li><a href="/profile/schedule"> <span
										class="glyphicon glyphicon glyphicon-calendar"></span>
										Schedule
								</a></li>
								<li><a href="/logout"> <span
										class="glyphicon glyphicon-log-out"></span> Logout
								</a></li>
							</ul>
						</c:when>
						<c:otherwise>
							<li class="navbar-right"><a href="/login">Login</a></li>
						</c:otherwise>
					</c:choose></li>
			</ul>
		</nav>


		<c:if test="${not empty confirmationMessage }">
			<div class="confirmation-message">
				<img src="/img/check-mark.png" />
				<p>${confirmationMessage }</p>
			</div>
		</c:if>