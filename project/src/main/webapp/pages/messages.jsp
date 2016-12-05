<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:import url="template/header.jsp" />

<ol class="breadcrumb">
	<li><a href="/${pagemode}/">Homepage</a></li>
	<li>Messages</li>
</ol>

<!-- format the dates -->
<fmt:formatDate value="${messages[0].dateSent}" var="formattedDateSent"
	type="date" pattern="HH:mm, dd.MM.yyyy" />

<script src="/js/unreadMessages.js"></script>
<script src="/js/messages.js"></script>

<script>
	$(document).ready(function(){
		unreadMessages(function(unread){
			$('#unreadMessages').html(unread);
		});
		
	});
</script>

<div class="row">
	<div class="col-md-12 col-xs-12">

		<h3>Messages</h3>
	</div>
	<div class="col-md-12 bottom15">
		<div class="btn-group">
			<button class="btn btn-default active" id="inbox">
				Inbox <span class="badge" id="unreadMessages"></span>
			</button>
			<button class="btn btn-default" id="sent">Sent</button>
		</div>
		<div class="pull-right">
			<button class="btn btn-primary" data-toggle="modal"
				data-target="#messageModal">
				<span class="glyphicon glyphicon-envelope"></span> New message
			</button>
		</div>
	</div>
	<div class="col-md-12" id="messageList">
		<div class="table-responsive">
			<table class="table table-striped table-hover">
				<thead>
					<tr>
						<th id="subjectColumn">Subject</th>
						<th>Sender</th>
						<th>Recipient</th>
						<th>Date sent</th>
					</tr>
				</thead>
				<tbody>
					<c:choose>
						<c:when test="${not empty messages }">
							<c:forEach items="${messages}" var="message" varStatus="loop">
								<fmt:formatDate value="${message.dateSent}"
									var="singleFormattedDateSent" type="date"
									pattern="HH:mm, dd.MM.yyyy" />

								<tr data-id="${message.id}"
									class="message-row ${loop.index == 0 ? 'info' : ''} ${message.state == 'UNREAD' ? 'message-unread' : ''}">
									<td><a>${message.subject}</a></td>
									<td>${message.sender}</td>
									<td>${message.recipient}</td>
									<td>${singleFormattedDateSent}</td>
								</tr>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<tr>
								<td colspan="4">You have no messages</td>
							</tr>
						</c:otherwise>
					</c:choose>
				</tbody>
			</table>
		</div>
		<div class="panel panel-default" style="${empty messages ? 'display: none;' : '' }" id="messageDetail">
			<div class="panel-heading">
				<h4 id="message-preview-subject">${messages[0].subject }</h4>
			</div>
			<div class="panel-body">
				<p>
					<strong>To: </strong><span id="message-preview-receiver">${messages[0].recipient }</span>
				</p>
				<p>
					<strong>From: </strong><span id="message-preview-sender">${messages[0].sender }</span>
				</p>
				<p>
					<strong>Date sent: </strong><span id="message-preview-date">${formattedDateSent}</span>
				</p>
				<br />
				<p id="message-preview-content">${messages[0].getTextWithLineBreaks() }</p>
			</div>
		</div>
	</div>
</div>
<c:import url="getMessageForm.jsp" />


<c:import url="template/footer.jsp" />