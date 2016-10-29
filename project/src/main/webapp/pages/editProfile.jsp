<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<c:import url="template/header.jsp" />

<script>
	$(document).ready(function() {
		$("#about-me").val("${currentUser.aboutMe}");
		$("#field-creditcardNumber").val("0000000000000000");
		var premiumCheck = ${currentUser.isPremium()};
		document.getElementById("premiumUser").disabled=premiumCheck == 1 ? true : false;
		});
	
		
</script>

<pre><a href="/">Homepage</a>   &gt;   <a href="/user?id=${currentUser.id}">Public Profile</a>   &gt;   Edit profile</pre>

<h1>Edit your Profile</h1>
<hr />

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

<form:form method="post" modelAttribute="editProfileForm"
	action="/profile/editProfile" id="editProfileForm" autocomplete="off"
	enctype="multipart/form-data">

<table class="editProfileTable">
	<tr>
		<td class="spacingTable"><label for="user-name">Username:</label><a>&emsp;</a>
		<form:input id="user-name" path="username" value="${currentUser.username}" /></td>
		
	</tr>
	<tr>
		<td class="spacingTable"><label for="first-name">First name:</label><a>&emsp;</a>
		<form:input id="first-name" path="firstName" value="${currentUser.firstName}" /></td>
	</tr>
	<tr>	
		<td class="spacingTable"><label for="last-name">Last name:</label><a>&emsp;</a>
		<form:input id="last-name" path="lastName" value="${currentUser.lastName}" /></td>
	</tr>
	<tr>	
		<td class="spacingTable"><label for="password">Password:</label><a>&emsp;&thinsp;</a>
		<form:input type="password" id="password" path="password" value="${currentUser.password}" /></td>
	</tr>

	<tr>
		<td class="spacingTable"><label for="about-me">About me:</label><a>&emsp;&thinsp;</a><br>
		<form:textarea id="about-me" path="aboutMe" rows="10" cols="100" /></td>
	</tr>
</table>

<form:checkbox path="isPremium" value="on" id="premiumUser"/>Do you want to upgrade to Premium User for only 5$ per month?
		<br>
		<table id=creditcardForm>
			<tr class="creditcardInfo">
				<td style="display:none;">
				<td class="signupDescription"><label for="field-creditcardNumber">Credit card number:</label></td>
				<td><form:input path="creditCard" id="field-creditcardNumber" /> <form:errors
						path="creditCard" cssClass="validationErrorText" /></td>
			</tr>
		</table>
		<br />

<div>
	<button type="submit">Update</button>
</div>
</form:form>

<script>
$("#premiumUser").change(function(){
	var self = this;
	
	if ( self.checked){
		$("#field-creditcardNumber").val("");
	}
	else if ( !(self.checked) ){
		$("#field-creditcardNumber").val("0000000000000000");
	}
	
	$("#creditcardForm tr.creditcardInfo").toggle(self.checked);
	
	
}).change();
</script>


<c:import url="template/footer.jsp" />

