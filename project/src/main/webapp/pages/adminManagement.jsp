<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>


<c:import url="template/header.jsp" />

<ol class="breadcrumb">
	<li><a href="./">Homepage</a></li>
	<li class="active">Manage Website</li>
</ol>

<!-- check if user is logged in -->
<security:authorize var="loggedIn" url="/profile" />

<div class="row" >
	<div class="col-md-6 col-xs-12">
		<h3>Possible Premium Packages</h3>

		<c:choose>
			<c:when test="${empty premiumChoices}">
				<p>You haven't yet decided on premium packages.
			</c:when>
			<c:otherwise>
				<form:form id="PremiumChoiceManagement" class="form-horizontal" method="post"
					modelAttribute="EditPremiumChoiceForm" action="/profile/adminManagement">					<thead>
						<tr>
							<th>Duration</th>
							<th>Price</th>
						</tr>
					</thead>
					<c:forEach var="choice" items="${premiumChoices}">
						<tr>
							<td><form:input path="${choice.duration}" cssClass="form-control" />days</td>
							<td><form:input path="${choice.price}" cssClass="form-control" /> CHF</td>
						</tr>
					</c:forEach>
				</form:form>
			</c:otherwise>
		</c:choose>
	</div>
	<div class="col-md-12 col-xs-12">
		<h3>Manage Premium Packages</h3>
		<br />
		<div class="panel panel-default">
			<div class="panel-body">
				<p>
					<strong>The following premium packages are available:</strong>
				</p>
				<br />
				<form:form id="PremiumChoiceManagement" class="form-horizontal" method="post"
					modelAttribute="EditPremiumChoiceForm" action="/profile/adminManagement">
				<c:forEach var="choice" items="${premiumChoices}">
					<div class="row bottom15">
						<div class="col-md-2">
							<strong>Duration:</strong> 
							<form:input path="${choice.duration}" cssClass="form-control" />
						</div> days
						<div class="col-md-6">
							<strong>Price:</strong> 
							<form:input path="${choice.price}" cssClass="form-control" /> CHF
						</div>
					</div>
				</c:forEach>
				<br />

					<hr>

					<div class="form-group pull-right">
						<div class="col-sm-12">
							<a href="/user?id=${currentUser.id}"
								class="btn btn-default">Cancel</a>
							<% /**<form:button type="submit" class="btn btn-primary" value="update">Upgrade Now</form:button> **/ %>
						</div>
					</div>
				</form:form>
			</div>
		</div>
	</div>
</div>


<c:import url="template/footer.jsp" />