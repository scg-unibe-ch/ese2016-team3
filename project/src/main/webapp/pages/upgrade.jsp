<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<c:import url="template/header.jsp" />

<ol class="breadcrumb">
	<li><a href="./">Homepage</a></li>
	<li><a href="/user?id=${currentUser.id}">Profile</a></li>
	<li class="active">Upgrade to Premium</li>
</ol>

<!-- check if user is logged in -->
<security:authorize var="loggedIn" url="/profile" />

<div class="row" >
<div class="col-md-12 col-xs-12">
		<h3>Upgrade to premium user</h3>
	<form:form id="upgradeForm" class="form-horizontal" method="post" 
	modelAttribute="upgradeForm" action="./upgrade">
		<div class="panel panel-default">
		<div class="panel-body">
		
					<spring:bind path="creditcardType">
						<div id="form-creditcardType"
							class="form-group ${status.error ? 'has-error' : '' }">
							<label class="col-sm-2 control-label"
								for="field-creditcardType">Type of card</label>
							<div class="col-sm-6">
								<form:select path="creditcardType" id="field-creditcardType"
									class="form-control"> 
								<form:options items="${creditcardTypes}" itemLabel="creditcardTypeName"/>
								</form:select>
								<form:errors path="creditcardType" cssClass="text-danger" />
							</div>
						</div>
					</spring:bind>
					
					<spring:bind path="creditcardName">
						<div id="form-creditcardName"
							class="form-group ${status.error ? 'has-error' : '' }">
							<label class="col-sm-2 control-label"
								for="field-creditcardName">Name on card</label>
							<div class="col-sm-6">
								<form:input path="creditcardName" id="field-creditcardName"
									class="form-control" />
								<form:errors path="creditcardName" cssClass="text-danger" />
							</div>
						</div>
					</spring:bind>
					
					<spring:bind path="creditCard">
						<div id="form-creditcard"
							class="form-group ${status.error ? 'has-error' : '' }">
							<label class="col-sm-2 control-label"
								for="field-creditcardNumber">Card number</label>
							<div class="col-sm-6">
								<form:input path="creditCard" id="field-creditcardNumber"
									class="form-control" />
								<form:errors path="creditCard" cssClass="text-danger" />
							</div>
						</div>
					</spring:bind>
					
					<spring:bind path="securityNumber">
						<div id="form-securityNumber"
							class="form-group ${status.error ? 'has-error' : '' }">
							<label class="col-sm-2 control-label"
								for="field-securityNumber">Security Number</label>
							<div class="col-sm-6">
								<form:input path="securityNumber" id="field-securityNumber"
									class="form-control" placeholder="CVV" />
								<form:errors path="securityNumber" cssClass="text-danger" />
							</div>
						</div>
					</spring:bind>
					
					<spring:bind path="expirationMonth">
						<div id="form-expirationMonth"
							class="form-group ${status.error ? 'has-error' : '' }">
							<label class="col-sm-2 control-label"
								for="field-expirationMonth">Date of Expiration</label>
							<div class="col-sm-3">
								<form:input path="expirationMonth" id="field-expirationMonth"
									class="form-control" placeholder="MM" />
								<form:errors path="expirationMonth" cssClass="text-danger" />
							</div>
						</div>
					</spring:bind>	
				
					<spring:bind path="expirationYear">
						<div id="form-expirationYear"
							class="form-group ${status.error ? 'has-error' : '' }">
							<label class="col-sm-2 control-label"
								for="field-expirationYear"></label>
							<div class="col-sm-3">
								<form:input path="expirationYear" id="field-expirationYear"
									class="form-control" placeholder="YY" />
								<form:errors path="expirationYear" cssClass="text-danger" />
							</div>
						</div>
					</spring:bind>
	
			</div>
			</div>	
			
			<div class="form-group pull-right">
			<div class="col-sm-12">	
				<form:button href="/user?id=${currentUser.id}" class="btn btn-default">Cancel</form:button>
				<form:button type="submit" class="btn btn-primary" >Upgrade Now</form:button>
		</div>
		</div>
	</form:form>
</div>
</div>

<c:import url="template/footer.jsp" />
