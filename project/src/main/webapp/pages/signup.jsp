<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<c:import url="template/header.jsp" />

<script>
	// Validate the email field
	$(document).ready(function() {
		$("#field-email").focusout(function() {
			var text = $(this).val();
			$.post("/signup/doesEmailExist", {email: text}, function(data){
				if(data){
					alert("This username is taken. Please choose another one!");
					$("#field-email").val("");
				}
			});
		});
	});
</script>

<ol class="breadcrumb">
	<li><a href="./">Home</a></li>
	<li class="active">Sign up</li>
</ol>

<div class="row">
	<div class="col-md-12 col-xs-12">
		<h3>Sign up</h3>
		<form:form id="signupForm" class="form-horizontal" method="post"
			modelAttribute="signupForm" action="./signup">
			<div class="panel panel-default">
				<div class="panel-body">

					<spring:bind path="firstName">
						<div class="form-group ${status.error ? 'has-error' : '' }">
							<label class="col-sm-2 control-label" for="field-firstName">First
								Name</label>
							<div class="col-sm-6">
								<form:input path="firstName" cssClass="form-control"
									id="field-firstName" />
								<form:errors path="firstName" cssClass="text-danger" />
							</div>


						</div>
					</spring:bind>

					<spring:bind path="lastName">
						<div class="form-group ${status.error ? 'has-error' : '' }">
							<label class="col-sm-2 control-label" for="field-lastName">Last
								Name</label>
							<div class="col-sm-6">
								<form:input path="lastName" id="field-lastName"
									cssClass="form-control" />
								<form:errors path="lastName" cssClass="text-danger" />
							</div>


						</div>
					</spring:bind>

					<spring:bind path="email">
						<div class="form-group ${status.error ? 'has-error' : '' }">
							<label class="col-sm-2 control-label" for="field-email">Email</label>
							<div class="col-sm-6">
								<form:input path="email" id="field-email"
									cssClass="form-control" />
								<form:errors path="email" cssClass="text-danger" />
							</div>


						</div>
					</spring:bind>

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

					<spring:bind path="gender">
						<div class="form-group ${status.error ? 'has-error' : '' }">
							<label class="col-sm-2 control-label" for="field-gender">Gender</label>
							<div class="col-sm-6">
								<form:select path="gender" class="form-control">
									<form:options items="${genders}" itemLabel="genderName"/>
								</form:select>
							</div>

						</div>
					</spring:bind>

					<div class="form-group">
						<div class="col-sm-6 col-sm-offset-2">							
							<label class="checkbox-inline">
								<form:checkbox path="isPremium" value="on" id="premiumUser"/>
								 Sign up as a Premium User for only 5$ per month</label>
						</div>
					</div>

					<spring:bind path="validCreditCard">
						<div id="form-creditcard"
							class="form-group ${status.error ? 'has-error' : '' }">
							<label class="col-sm-2 control-label"
								for="field-creditcardNumber">Credit card number</label>
							<div class="col-sm-6">
								<form:input path="creditCard" id="field-creditcardNumber"
									class="form-control" />
								<form:errors path="validCreditCard" cssClass="text-danger" />
							</div>


						</div>
					</spring:bind>
				</div>
			</div>
			<div class="form-group pull-right">
				<div class="col-sm-12">
					<a href="./" class="btn btn-default">Cancel</a>
					<button type="submit" class="btn btn-primary" value="signup">Sign
						up</button>
				</div>
			</div>
		</form:form>
	</div>
</div>



<script>
$("#premiumUser").change(function(){
	var self = this;
	
	if ( self.checked){
		$("#field-creditcardNumber").val("");
	}
	
	$("#form-creditcard").toggle(self.checked);
	
	
}).change();
</script>

<c:import url="template/footer.jsp" />