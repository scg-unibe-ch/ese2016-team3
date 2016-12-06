<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>

<!DOCTYPE html>
<html>
<head>
<Title>Ithaca</Title>
<script src="/js/jquery.min.js"></script>
<script src="/js/jquery-ui.min.js"></script>
<link rel="stylesheet" href="/css/jquery-ui.css" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="/css/bootstrap.min.css">
<link rel="stylesheet" href="/css/style.css">
<script src="/js/bootstrap.min.js"></script>
<script src="/js/unreadMessages.js"></script>
<script src="/js/jquery.fileupload.js"></script>

<style>
/* ensure that autocomplete lists are not too long and have a scrollbar */
.ui-autocomplete {
	max-height: 200px;
	overflow-y: auto;
	overflow-x: hidden;
}
</style>

<style>
/* TABS: define css class "customTab" in orded to display the tabs of the header green (and define hover effects)*/
.customTab>li.active>a, .customTab>li.active>a:focus, .customTab>li.active>a:hover {
	background-color: #65bf05 !important;
	color: #FFFFFF;
}

.customTab>li>a:hover {
	background-color: #5b8a28 !important;
	color: #FFFFFF;
	/* fill	color #d9edf7 default f5f5f5 #337ab7 fff*/
}

/* CONTAINER: define new class to handle the background color of the container */
.custom-Container {
	background-color: #65bf05;
}

/* NAVBAR set color of writing and define hover effect.*/
.navbar-default .navbar-nav>li>a:hover, .navbar-default .navbar-nav>li>a:focus
	{
	color: #FFFFFF; /*Sets the text hover color on navbar*/
	background-color: #5b8a28;
}
/* set writing color */
.navbar-default .navbar-nav>li>a {
	color: #FFFFFF;
}

/*sets color of dropdown (user)*/
.dropdown-menu>li>a:hover, .dropdown-menu>li>a:focus {
	color: #FFFFFF;
	text-decoration: none;
	background-color: #65bf05; /*change color of links in drop down here*/
}

.dropdown-toggle:active, .open .dropdown-toggle {
	background: #65bf05 !important;
	color: #FFFFFF !important;
}
</style>

<script type="text/javascript">
	$(document).ready(function() {
		unreadMessages(function(unread) {
			$('#navUnread').html(unread);
		});
	});
</script>
</head>

<!-- check if user is logged in -->
<security:authorize var="loggedIn" url="/profile" />

<body>
	<div class="container" id="main-container">
		<ul class="nav nav-tabs header-tabs customTab" role="tablist">
			<li id="ithaca-brand"><img id="logo" src="/img/logoNew.png">Ithaca</li>
			<c:choose>
				<c:when test="${pagemode == 'buy'}">
					<li class="active"><a href="/buy/"><b>Buy</b></a></li>
					<li><a href="/rent/">Rent</a></li>
				</c:when>
				<c:when test="${pagemode == 'rent'}">
					<li><a href="/buy/">Buy</a></li>
					<li class="active"><a href="/rent/"><b>Rent</b></a></li>
				</c:when>
				<c:otherwise>
					<li><a href="/buy/">Buy</a></li>
					<li><a href="/rent/">Rent</a></li>
				</c:otherwise>
			</c:choose>
		</ul>
		<nav class="navbar navbar-default" id="mainNav">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle" data-toggle="collapse"
					data-target=".navbar-collapse">
					<span class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
			</div>
			<div class="navbar-inner">
				<div
					class="container-fluid collapse navbar-collapse custom-Container">
					<ul class="nav navbar-nav">
						<li><a href="/${pagemode}/">Homepage</a></li>
						<li><a href="/${pagemode}/searchAd">Find ad</a></li>
						<c:if test="${loggedIn}">
							<li><a href="/buy/profile/mybids">My bids</a></li>
						</c:if>
					</ul>
					<ul class="nav navbar-nav navbar-right">
						<c:choose>
							<c:when test="${loggedIn}">
								<li><a href="/${pagemode}/profile/placeAd"><span
										class="glyphicon glyphicon-pencil"></span> Place ad</a></li>
								<li class="navbar-right dropdown"><%@include
										file='/pages/getUserPicture.jsp'%> <a
									class="dropdown-toggle" data-toggle="dropdown" href="#"> <span
										class="glyphicon glyphicon-user"></span> <%
 	out.print(String.format("%s %s", realUser.getFirstName(), realUser.getLastName()));
 %> <span class="caret"></span>
								</a>
									<ul class="dropdown-menu">
										<li><a
											href="/${pagemode}/user?id=<%out.print(realUser.getId());%>">
												<span class="glyphicon glyphicon-cog"></span> Show profile
										</a></li>
										<li><a href="/${pagemode}/profile/myRooms"> <span      
												class="glyphicon glyphicon-home"></span> My Ads
										</a></li>
										<li><a href="/buy/profile/auctions"> <span
												class="glyphicon glyphicon-th-list"></span> Manage auctions
										</a></li>
										<li><a href="/${pagemode}/profile/messages"> <span
												class="glyphicon glyphicon glyphicon-envelope"></span>
												Messages <span class="badge" id="navUnread"></span>
										</a>
										<li><a href="/${pagemode}/profile/alerts"> <span
												class="glyphicon glyphicon-bell"></span> Alerts
										</a></li>
										<li><a href="/${pagemode}/profile/enquiries"> <span
												class="glyphicon glyphicon-eye-open"></span> Enquiries
										</a></li>
										<li><a href="/${pagemode}/profile/schedule"> <span
												class="glyphicon glyphicon glyphicon-calendar"></span>
												Schedule
										</a></li>
										<li><a href="/${pagemode}/logout"> <span
												class="glyphicon glyphicon-log-out"></span> Logout
										</a></li>
									</ul>
							</c:when>
							<c:otherwise>
								<li class="navbar-right"><a href="/${pagemode}/login">Login</a></li>
							</c:otherwise>
						</c:choose>
						</li>
					</ul>
				</div>
			</div>
		</nav>

		<c:if test="${not empty confirmationMessage }">
			<div class="alert alert-success">
				<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
				<p>${confirmationMessage }</p>
			</div>
		</c:if>
		<c:if test="${not empty errorMessage }">
			<div class="alert alert-danger">
				<p>${errorMessage }</p>
			</div>
		</c:if>
		<c:if test="${not empty warningMessage }">
			<div class="alert alert-warning">
				<p>${warningMessage }</p>
			</div>
		</c:if>
		
		
		