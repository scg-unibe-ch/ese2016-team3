//we pass a place: If it is "messages", the function returns the string
//for the inbox; if it is anything else, it returns the string for the header.

function unreadMessages(success) {
	$.get("/profile/unread", function(data){ 
		if (success != undefined){
			success(data);
		}
	});
}