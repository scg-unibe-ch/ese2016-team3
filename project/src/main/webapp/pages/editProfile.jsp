<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<c:import url="template/header.jsp" />

<script>
	$(document).ready(function() {
		$("#about-me").val("${currentUser.aboutMe}")
		});		
</script>

<pre><a href="/">Home</a>   &gt;   <a href="/user?id=${currentUser.id}">Public Profile</a>   &gt;   Edit profile</pre>

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

<div>
		<button type="button" id="premiumUser">Upgrade to Premium Account for only 5$!</button>
</div>

<script>
$(document).ready(function() {
	$("#premiumUser").click(function() {
		$("#content").children().animate({
			opacity : 0.4
		}, 300, function() {
			$("#upgrade").css("display", "block");
			$("#upgrade").css("opacity", "1");
		});
	});
	
	$("#CancelUpgrade").click(function() {
		$("#upgrade").css("display", "none");
		$("#upgrade").css("opacity", "0");
		$("#content").children().animate({
			opacity : 1
		}, 300);
	});
});
</script>

<div>
	<button type="submit">Update</button>
</div>
</form:form>

<div id="upgrade" style="display:none;" >
	<form class="upgradeForm" >
		<h2>Upgrade to Premium User</h2>
		<br>Please enter your credit card number:<br>
			<form:input path="currentUser.creditCard" id="field-creditcardNumber" /> <form:errors
				path="creditCard" cssClass="validationErrorText" />
		<br>
		<form:checkbox path="currentUser.accountType" value="PREMIUM" id="tac"/>Please accept the terms and conditions
		<br>
		<button type="submit"  id="UpgradeNow" >Upgrade Now</button>
		<button type="button" id="CancelUpgrade">Cancel</button>
	</form>
</div>

<c:import url="template/footer.jsp" />

