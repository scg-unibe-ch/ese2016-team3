<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<c:import url="template/header.jsp" />

<script>
	$(document).ready(function() {
		$("field-creditcardType").val('null');
		$("field-creditcardName").val('null');
		$("field-creditcardNumber").val('null');
		$("field-securityNumber").val('null');
		$("field-expirationMonth").val('null');
		$("field-expirationYear").val('null');
		$(currentUser.isPremium()).val('true');
	})
</script>
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
		<br />
		<div class="panel panel-default">
			<div class="panel-body">
				<p><strong>The following premium packages are available:</strong></p>
				<br />
				<c:forEach var="choice" items="${premiumChoices}">
					<div class="row bottom15">
						<div class="col-md-2">
							<strong>Duration:</strong> ${choice.duration} days
						</div>
						<div class="col-md-6">
							<strong>Price:</strong> ${choice.price} CHF
						</div>
					</div>
				</c:forEach>
				<br />
				<form:form id="upgradeForm" class="form-horizontal" method="post" 
				modelAttribute="upgradeForm" action="./upgrade">
		
				<spring:bind path="duration">
					<div id="form-duration"
						class="form-group ${status.error ? 'has-error' : '' }">
						<label class="col-sm-2 control-label"
							for="field-duration">Duration in days</label>
						<div class="col-sm-3">
							<form:select path="duration" id="field-duration"
								class="form-control"> 
							<form:options items="${durations}"/>
							</form:select>
							<form:errors path="duration" cssClass="text-danger" />
						</div>
					</div>
				</spring:bind>
				
				<hr>
		
				<spring:bind path="creditcardType">
					<div id="form-creditcardType"
						class="form-group ${status.error ? 'has-error' : '' }">
						<label class="col-sm-2 control-label"
							for="field-creditcardType">Type of card</label>
						<div class="col-sm-3">
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
							<form:select path="expirationMonth" id="field-expirationMonth"
								cssClass="form-control">
								<option>Month</option>
								<form:options items="${months}" />
							</form:select>
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
							<form:select path="expirationYear" cssClass="form-control"
								id="field-expirationYear">
								<option>Year</option>
								<form:options items="${years}" />
							</form:select>
							<form:errors path="expirationYear" cssClass="text-danger" />
						</div>
					</div>
				</spring:bind>
				
			</div>
		</div>	
			
		<div class="form-group pull-right">
			<div class="col-sm-12">	
				<form:button href="/user?id=${currentUser.id}" class="btn btn-default">Cancel</form:button>
				<form:button type="submit" class="btn btn-primary" value="update" >Upgrade Now</form:button>
			</div>
		</div>
		</form:form>
	</div>
</div>


<c:import url="template/footer.jsp" />
