<%@page import="ch.unibe.ese.team3.model.Ad"%>
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
					modelAttribute="EditPremiumChoiceForm" action="./adminManagement">
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
					
					<spring:bind path="password">
						<div class="form-group ${status.error ? 'has-error' : '' }">
							<label class="col-sm-2 control-label" for="field-password">Password</label>
							<div class="col-sm-6">
								<form:input path="password" id="field-password" type="password"
									cssClass="form-control" />
								<form:errors path="password" cssClass="text-danger" />
							</div>


						</div>
					</spring:bind>
					
					<spring:bind path="duration">
						<div id="form-duration"
							class="form-group ${status.error ? 'has-error' : '' }">
							<label class="col-sm-2 control-label" for="field-duration">Duration
								in days</label>
							<div class="col-sm-3">
								<form:select path="duration" id="field-duration"
									class="form-control">
									<form:options items="${durations}" />
								</form:select>
								<form:errors path="duration" cssClass="text-danger" />
							</div>
						</div>
					</spring:bind>

					<hr>

					<div class="form-group pull-right">
						<div class="col-sm-12">
							<form:button href="/user?id=${currentUser.id}"
								class="btn btn-default">Cancel</form:button>
							<form:button type="submit" class="btn btn-primary" value="update">Upgrade Now</form:button>
						</div>
					</div>
				</form:form>
			</div>
		</div>
	</div>
</div>


<c:import url="template/footer.jsp" />