sendEnquiry = function(id){
	$.get("/profile/enquiries/sendEnquiryForVisit?id=" + id, function(){
		var enquiryButton = $("#visitList tr button[data-id='" + id + "']");
		$(enquiryButton).prop('disabled', true);
		$(enquiryButton).addClass('btn-default');
		$(enquiryButton).removeClass('btn-primary');
		$(enquiryButton).html('Enquiry sent');
	});		
}
