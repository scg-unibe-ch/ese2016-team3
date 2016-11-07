$(document).ready(function() {

	$("#field-moveInDate").datepicker({
		dateFormat : 'dd-mm-yy'
	});
	$("#field-startDate").datepicker({
		dateFormat : 'dd-mm-yy'
	});
	$("#field-endDate").datepicker({
		dateFormat : 'dd-mm-yy'
	});

	$("#field-visitDay").datepicker({
		dateFormat : 'dd-mm-yy'
	});
	
	var setPanelVisibility = function(){
		var me = $("#field-auction");
		
		var panelAuction = $("#panel-buy-auction");
		var panelBuy = $("#panel-buy-normal");
		
		if (me.is(":checked")){
			panelAuction.show();
			panelBuy.hide();
		}
		else {
			panelAuction.hide();
			panelBuy.show();
		}
	}
	
	$("#field-auction").change(function(){
		setPanelVisibility();
	});

	$("#addVisitButton").click(function() {
		var date = $("#field-visitDay").val();
		if (date == "") {
			return;
		}

		var startHour = $("#startHour").val();
		var startMinutes = $("#startMinutes")
				.val();
		var endHour = $("#endHour").val();
		var endMinutes = $("#endMinutes").val();

		if (startHour > endHour) {
			alert("Invalid times. The visit can't end before being started.");
			return;
		} else if (startHour == endHour
				&& startMinutes >= endMinutes) {
			alert("Invalid times. The visit can't end before being started.");
			return;
		}

		var newVisit = date + ";" + startHour
				+ ":" + startMinutes + ";"
				+ endHour + ":" + endMinutes;
		var newVisitLabel = date + " "
				+ startHour + ":"
				+ startMinutes + " to "
				+ endHour + ":" + endMinutes;

		var index = $("#addedVisits input").length;

		var label = "<p>" + newVisitLabel
				+ "</p>";
		var input = "<input type='hidden' value='"
				+ newVisit
				+ "' name='visits["
				+ index + "]' />";

		$("#addedVisits").append(label + input);
	});
	
	setPanelVisibility();
});