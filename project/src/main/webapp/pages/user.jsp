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

<script>
	$(document).ready(function() {
		$("#newMsg").click(function() {
			$("#content").children().animate({
				opacity : 0.4
			}, 300, function() {
				$("#msgDiv").css("display", "block");
				$("#msgDiv").css("opacity", "1");
			});
		});

		$("#messageCancel").click(function() {
			$("#msgDiv").css("display", "none");
			$("#msgDiv").css("opacity", "0");
			$("#content").children().animate({
				opacity : 1
			}, 300);
		});

		$("#messageSend").click(function() {
			var valid = true;
			
			var subjectControl = $("#msgSubject");
			var messageControl = $("#msgTextarea");
			
			if(subjectControl.val() == ""){
				valid = false;
				subjectControl.parent().addClass('has-error');
			}
			else {
				subjectControl.parent().removeClass('has-error');
			}
				
			
			if(messageControl.val() == ""){
				valid = false;
				messageControl.parent().addClass('has-error');
			}
			else {
				messageControl.parent().removeClass('has-error');
			}
			
			if (valid == true) {
				var subject = $("#msgSubject").val();
				var text = $("#msgTextarea").val();
				var recipientEmail = "${user.username}";
				$.post("profile/messages/sendMessage", {
					subject : subject,
					text : text,
					recipientEmail : recipientEmail
				}, function() {
					$("#msgSubject").val("");
					$("#msgTextarea").val("");
					$('#msgDiv').modal('hide')
				})
			}
		});
	});
</script>

<ol class="breadcrumb">
	<li><a href="/${pagemode}/">Homepage</a></li>
	<li class="active">Profile</li>
</ol>


<div class="col-md-12 col-xs-12">
	<form:form id="profile" class="form-horizontal">
	<div class="panel panel-default">
		<div class="panel-body">
			<div class="container">
				<div class="row">
					<div class="col-md-6">
						<c:choose>
							<c:when test="${user.picture.filePath != null}">
								<img src="${user.picture.filePath}">
							</c:when>
							<c:otherwise>
								<img src="/img/avatar.png">
							</c:otherwise>
						</c:choose>	
					</div>
					<div class="col-md-6">
						<br />
						<h3>Username</h3>${user.email}<p>
						<h3>Name</h3>${user.firstName}
						${user.lastName}
						
						<c:choose>
							<c:when test="${user.accountType == 'PREMIUM' && principalID eq user.id}">
								<br /><br />
								<h4><font color="gold">&#9733</font> This is a premium account <font color="gold">&#9733</font></h4>
								Option valid for ${user.premiumChoice.duration} days
							</c:when>
							<c:otherwise></c:otherwise>
						</c:choose>
					</div>
				</div>
			</div>
			<p>
			<h3>About me</h3>${user.aboutMe}
			<p>
				<c:choose>
					<c:when test="${principalID != null}" >
						<div class="form-group pull-right">
						<div class="col-sm-12">
						<c:if test="${principalID != user.id}">
							<button type="button" data-toggle="modal" data-target="#msgDiv" id="newMsg" type="button" class="btn btn-primary">Message</button>
						</c:if>
						<c:choose>
							<c:when test="${principalID eq user.id}">
								<a id="edit" class="btn btn-primary" href="./profile/editProfile">Edit Profile</a>
							</c:when>
							<c:otherwise></c:otherwise>
						</c:choose>
						<c:choose>
							<c:when test="${'BASIC' eq user.accountType && principalID eq user.id}">
								<a type="button" href="./profile/upgrade" class="btn btn-primary">Get Premium</a>
							</c:when>
							<c:otherwise></c:otherwise>
						</c:choose>
						</div>
						</div>
					</c:when>
					<c:otherwise>
						<p><strong>Please log in to contact this person.</strong></p>
					</c:otherwise>
				</c:choose>
			</div>
			</div>
			</form:form>
		</div>
	
<div class="modal fade" id="msgDiv" tabindex="-1">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="myModalLabel">Send Message</h4>
			</div>
			<div class="modal-body">
				<form class="form">
					<div class="form-group">
						<label for="msgSubject">Subject</label> <input
							class="form-control" type="text" id="msgSubject"
							placeholder="Subject">
					</div>
					<div class="form-group">
						<label for="msgTextarea">Message</label>
						<textarea id="msgTextarea" class="form-control"
							placeholder="Message"></textarea>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
				<button type="button" class="btn btn-primary" id="messageSend">Send</button>
			</div>
		</div>
	</div>
</div>

<% /** <div id="msgDiv" style="display:none;">
	<form class="msgForm">
		<h2>Message this user</h2>
		<br> <br> <label>Subject: <span>*</span></label> <input
			class="msgInput" type="text" id="msgSubject" placeholder="Subject" />
		<br> <br> <label>Message: </label>
		<textarea id="msgTextarea" placeholder="Message"></textarea>
		<br />
		<button type="button" id="messageSend">Send</button>
		<button type="button" id="messageCancel">Cancel</button>
	</form>
</div> **/ %>
<c:import url="template/footer.jsp" />