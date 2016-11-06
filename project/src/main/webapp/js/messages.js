function loadMessages(data) {
	$("#messageList table tr:gt(0)").remove();
	if (data.length > 0) {
		$.each(data, function(index, message) {
			var result = '<tr data-id="' + message.id + '" class="message-row ' + (message.state == 'UNREAD' ? 'message-unread' : '') + '" >';
			result += '<td>' + message.subject + '</td>';
			result += '<td>' + message.sender.email + '</td>';
			result += '<td>' + message.recipient.email + '</td>';
			result += '<td>' + message.dateSent + '</td></tr>';

			$("#messageList table").append(result);
		});
	}
	else {
		$("#messageList table").append('<tr><td colspan="4">No messages</td></tr>');
	}
}

function prepareRows() {
	var rows = $("#messageList table tr:gt(0)");
	$(rows).click(function() {
		rows.each(function(index, row){
			$(row).removeClass('info');
		});
		var me = $(this);
		me.addClass('info');
		var id = $(this).attr("data-id");
		if (id == undefined){ return; }
		$.get("/profile/readMessage?id=" + id, function(data) {
			$.get("/profile/messages/getMessage?id=" + id, function(data) {
				$('#messageDetail').show();
				$('#message-preview-subject').html(data.subject);
				$('#message-preview-sender').html(data.sender.email);
				$('#message-preview-receiver').html(data.recipient.email);
				$('#message-preview-date').html(data.dateSent);
				$('#message-preview-content').html(data.text.replace(/(\r\n?|\n)/g, '<br/>'));
			}, 'json');
			unreadMessages(function(unread) {
				$('#navUnread').html(unread);
				$('#unreadMessages').html(unread);
				me.removeClass('message-unread')
			});
		});
	});
}

$(document).ready(function() {
	prepareRows();

	$("#inbox").click(function() {
		$('#messageDetail').hide();
		$.post("/profile/message/inbox", function(data) {
			$("#inbox").addClass('active');
			$("#sent").removeClass('active');
			loadMessages(data);
			prepareRows();
		}, 'json');
	});

	$("#sent").click(function() {
		$('#messageDetail').hide();
		$.post("/profile/message/sent", function(data) {
			$("#sent").addClass('active');
			$("#inbox").removeClass('active');
			loadMessages(data);
			prepareRows();
		}, 'json');
	});

});