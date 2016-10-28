<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>


<div class="modal fade" id="messageModal" tabindex="-1">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title">New message</h4>
			</div>
			<div class="modal-body">
				<form:form class="form" id="messageForm" method="post"
					modelAttribute="messageForm" action="#">
					<div class="form-group">
						<label for="receiverEmail">To</label>
						<form:input cssClass="form-control" type="text" id="receiverEmail"
							placeholder="Receiver" path="recipient" />
					</div>
					<div class="form-group">
						<label for="msgSubject">Subject</label>
						<form:input cssClass="form-control" type="text" id="msgSubject"
							placeholder="Subject" path="subject" />
					</div>
					<div class="form-group">
						<label for="msgTextarea">Message</label>
						<form:textarea id="msgTextarea" cssClass="form-control"
							placeholder="Message" path="text" />
					</div>
				</form:form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
				<button type="submit" class="btn btn-primary" id="messageSend"><span class="glyphicon glyphicon-send"></span> Send</button>
			</div>
		</div>
	</div>
</div>