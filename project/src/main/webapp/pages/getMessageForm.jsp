<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<script type="text/javascript">
	validateField = function(valid, control) {
		if(control.val() == ""){
			control.parent().addClass('has-error');
			return false;
		}
		else {
			control.parent().removeClass('has-error');
		}
		return valid;
	}
	
	validateReceiver = function(valid){
		var receiverControl = $("#receiverEmail");
		var text = receiverControl.val();

		$.post("/profile/messages/validateEmail", {
			email : text
		}, function(data) {
			if (data != text) {
				receiverControl.parent().addClass('has-error');
			}
			else {
				receiverControl.parent().removeClass('has-error');
			}
		});
	}

	$(document).ready(function(){
		$("#messageSend").click(function (){
			var valid = true;
			
			var subjectControl = $("#msgSubject");
			var messageControl = $("#msgTextarea");
			var receiverControl = $("#receiverEmail");
			
			valid = validateField(valid, subjectControl);
			valid = validateField(valid, messageControl);
			valid = validateField(valid, receiverControl);
		
			if (valid == true){
				var subject = subjectControl.val();
				var text = messageControl.val();
				var recipientEmail = receiverControl.val();
				$.post("/${pageMode.parameter}/profile/messages/sendMessage", {subject : subject, text: text, recipientEmail : recipientEmail}, function(){
					subjectControl.val("");
					messageControl.val("");
					receiverControl.val("");
					$('#messageModal').modal('hide')
				});
			}
		});
		
		$("#receiverEmail").focusout(function() {
			validateReceiver(true);
		});
	
		$("#messageForm").submit(function(event) {
			if ($("#receiverEmail").val() == "") {
				event.preventDefault();
			}
		});
		
	});

</script>


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
				<button type="submit" class="btn btn-primary" id="messageSend">
					<span class="glyphicon glyphicon-send"></span> Send
				</button>
			</div>
		</div>
	</div>
</div>