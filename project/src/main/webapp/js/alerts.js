// function is called when page is loaded
$(document).ready(function() {
	$("#field-earliestMoveInDate").datepicker({
		dateFormat : 'dd-mm-yy'
	});
	$("#field-latestMoveInDate").datepicker({
		dateFormat : 'dd-mm-yy'
	});
	
	
	var setPanelVisibility = function() {
		var me = $("#field-extendedAlert");

		var panelExtendedAlert = $("#extended-alert");

		if (me.is(":checked")) { // if field with id "field-auction" is
									// checked
			panelExtendedAlert.show(); // values, which are entered in
										// panelExtendedAlert are passed to the
										// model, even if the checkbox is
										// unchecked after entering the values
		} else {
			panelExtendedAlert.hide();
		}
	}
	
	var showMessageWhenBelowZero = function() {
		var message = $("#negativeAlertWarning");
		var floorLevelMin = $("#field-floorLevelMin");
		
		if (floorLevelMin > 0) {
			message.show();
		}
		else {
			message.hide();
		}
	}
	
	// when field with Id field-auction is checked, call function
	// setPanelVisibility
	$("#field-extendedAlert").change(function() {
		setPanelVisibility();
		//showMessageWhenBelowZero();
	});

	setPanelVisibility();
	//showMessageWhenBelowZero();
});