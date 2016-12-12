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
	
	// resets the values of the extended alert fields
	var deleteExtendedAlertValues = function() {
		$("#field-squareFootageMin").val(0)
		$("#field-squareFootageMax").val(0)
		$("#field-renovationYearMin").val(0)
		$("#field-renovationYearMax").val(0)
		$("#field-buildYearMin").val(0)
		$("#field-buildYearMax").val(0)
		$("#field-numberOfRoomsMin").val(0)
		$("#field-numberOfRoomsMax").val(0)
		$("#field-numberOfBathMin").val(0)
		$("#field-numberOfBathMax").val(0)
		$("#field-distanceSchoolMin").val(0)
		$("#field-distanceSchoolMax").val(0)
		$("#field-distanceShoppingMin").val(0)
		$("#field-distanceShoppingMax").val(0)
		$("#field-distancePublicTransportMin").val(0)
		$("#field-distancePublicTransportMax").val(0)
		
		// other criteria do not have to be reseted, as they are dicarded anyways when extendedAlert is not true
	}
	
	// when field with Id field-auction is checked, call function
	// setPanelVisibility
	$("#field-extendedAlert").change(function() {
		setPanelVisibility();
		deleteExtendedAlertValues();
	});

	setPanelVisibility();
});