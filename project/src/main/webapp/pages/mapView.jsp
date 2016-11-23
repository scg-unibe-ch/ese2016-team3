<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:import url="template/header.jsp" />


<!--   TestCode Google   -->

<!DOCTYPE html>
<html>
  <head>
    <style>
       #map {
        height: 400px;
        width: 100%;
       }
    </style>
  </head>
  <body>
    <h3>My Google Maps Demo</h3>
    <div id="map"></div>
    <script>
      function initMap() {
        var uluru = {lat: -25.363, lng: 131.044};
        var map = new google.maps.Map(document.getElementById('map'), {
          zoom: 4,
          center: uluru
        });
        var marker = new google.maps.Marker({
          position: uluru,
          map: map
        });
      }
    </script>
    <script async defer
    src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDUWLIH0Ch16wQFDrRoRQ6XyKjWl2TQF8w&callback=initMap">
    </script>
  </body>
</html>

<div class="row">
								<div class="col-sm-6">
									<div class="form-group ${status.error ? 'has-error' : '' }">
										<label for="field-floorLevelMin"
											class="control-label col-sm-4">Floor level between</label>
										<div class="col-sm-8">
											<div class="input-group">
												<div class="form-inline">
													<form:input type="number" step="1" id="field-floorLevelMin"
														path="floorLevelMin" cssClass="form-control input60" />
													<label for="field-floorLevelMax" class="betweenLabel">
														- </label>
													<form:input type="number" step="1" id="field-floorLevelMax"
														path="floorLevelMax" cssClass="form-control input60" />
												</div>
											</div>
										</div>
									</div>
								</div>
								<div class="col-sm-6">
									<div class="form-group ${status.error ? 'has-error' : '' }">
										<label for="field-NumberOfBathMin"
											class="control-label col-sm-4">Nr. of Baths between</label>
										<div class="col-sm-8">
											<div class="input-group">
												<div class="form-inline">
													<form:input type="number" step="1"
														id="field-NumberOfBathMin" path="numberOfBathMin"
														cssClass="form-control input60 " />
													<label for="field-NumberOfBathMax" class="betweenLabel">
														- </label>
													<form:input type="number" cssClass="form-control input60"
														path="numberOfBathMax" id="field-NumberOfBathMax" />
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-sm-6">
									<div class="form-group ${status.error ? 'has-error' : '' }">
										<label for="field-BuildYearMin" class="control-label col-sm-4">Build
											year between</label>
										<div class="col-sm-8">
											<div class="input-group">
												<div class="form-inline">
													<form:input type="number" cssClass="form-control input60"
														path="buildYearMin" id="field-BuildYearMin" />
													<label for="field-BuildYearMax" class="betweenLabel">
														- </label>
													<form:input type="number" cssClass="form-control input60"
														path="buildYearMax" id="field-BuildYearMax" />
												</div>
											</div>
										</div>
									</div>
								</div>
								<div class="col-sm-6">
									<div class="form-group ${status.error ? 'has-error' : '' }">
										<label for="field-RenovationYearMin"
											class="control-label col-sm-4">Renovation year
											between</label>
										<div class="col-sm-8">
											<div class="input-group">
												<div class="form-inline">
													<form:input type="number" cssClass="form-control input60"
														path="renovationYearMin" id="field-RenovationYearMin" />
													<label for="field-RenovationYearMax" class="betweenLabel">
														- </label>
													<form:input type="number" cssClass="form-control input60"
														path="renovationYearMax" id="field-RenovationYearMax" />
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>



