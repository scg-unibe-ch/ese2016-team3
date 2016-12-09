<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<c:import url="template/header.jsp" />

<ol class="breadcrumb">
	<li><a href="/${pagemode}/">Homepage</a></li>
	<li><a href="/user?id=${currentUser.id}">Profile</a></li>
	<li class="active">Edit profile</li>
</ol>

<!-- check if user is logged in -->
<security:authorize var="loggedIn" url="/profile" />


<c:choose>
	<c:when test="${loggedIn}">
		<a id="profile_picture_editPage"> <c:import
					url="/pages/getUserPicture.jsp" />
		</a>
	</c:when>
	<c:otherwise>
		<a href="/login">Login</a>
	</c:otherwise>
</c:choose>



<div class="row">
	<div class="col-md-12 col-xs-12">
		<h3>Edit profile</h3>
		Fields marked with * are required. 
		<form:form id="editProfileForm" class="form-horizontal" method="post"
			modelAttribute="editProfileForm" action="./editProfile">
			<div class="panel panel-default">
				<div class="panel-body">

					<spring:bind path="username">
						<div class="form-group ${status.error ? 'has-error' : '' }">
							<label class="col-sm-2 control-label" for="field-username">E-mail*
							</label>
							<div class="col-sm-6">
								<form:input path="username" cssClass="form-control"
									id="field-username" />
								<form:errors path="username" cssClass="text-danger" />
							</div>


						</div>
					</spring:bind>
					
					<spring:bind path="password">
						<div class="form-group ${status.error ? 'has-error' : '' }">
							<label class="col-sm-2 control-label" for="field-password">Password*</label>
							<div class="col-sm-6">
								<form:input path="password" id="field-password" type="password"
									cssClass="form-control" />
								<form:errors path="password" cssClass="text-danger" />
							</div>


						</div>
					</spring:bind>
					
					<spring:bind path="firstName">
						<div class="form-group ${status.error ? 'has-error' : '' }">
							<label class="col-sm-2 control-label" for="field-firstName">First
								Name*</label>
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
								Name*</label>
							<div class="col-sm-6">
								<form:input path="lastName" id="field-lastName"
									cssClass="form-control" />
								<form:errors path="lastName" cssClass="text-danger" />
							</div>


						</div>
					</spring:bind>
					
					<spring:bind path="aboutMe">
						<div class="form-group ${status.error ? 'has-error' : '' }">
							<label class="col-sm-2 control-label" for="field-aboutMe">About Me
							</label>
							<div class="col-sm-6">
								<form:textarea class="form-control" rows="5" path="aboutMe"
								id="field-aboutMe" cssClass="form-control" />
								<form:errors path="aboutMe" cssClass="text-danger" />
							</div>
							
						</div>
					</spring:bind>

				</div>
			</div>
			<div class="form-group pull-right">
				<div class="col-sm-12">
					<a href="/${pagemode}/" class="btn btn-default">Cancel</a>
					<button type="submit" class="btn btn-primary" value="update">Update</button>
				</div>
			</div>
		</form:form>
	</div>
</div>


<c:import url="template/footer.jsp" />

