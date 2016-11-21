// function is called when page is loaded
$(document).ready(function() {
	
	var setPanelVisibility = function(){
		var me = $("#field-alert");
		
		var panelExtendedAlert = $("#panel-extended-alert");
		
		if (me.is(":checked")){   // if field with id "field-auction" is checked
			panelExtendedAlert.show();	//values, which are entered in panelExtendedAlert are passed to the model, even if the checkbox is unchecked after entering the values
		}
		else {
			panelExtendedAlert.hide();
		}
	}
	
	// when field with Id field-auction is checked, call function setPanelVisibility
	$("#field-alert").change(function(){
		setPanelVisibility();
	});
	
	setPanelVisibility();
});