<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<c:import url="template/header.jsp" />

<ol class="breadcrumb">
	<li><a href="/${pagemode}/">Homepage</a></li>
	<li class="active">Find ad</li>
</ol>

<script>
	$(document).ready(function() {
		$("#cityInput").autocomplete({
			minLength : 2,
			source : <c:import url="getzipcodes.jsp" />,
			enabled : true,
			autoFocus : true
		});
		
		var price = document.getElementById('prizeInput');
		var radius = document.getElementById('radiusInput');
		
		if(price.value == null || price.value == "" || price.value == "0")
			price.value = "500";
		if(radius.value == null || radius.value == "" || radius.value == "0")
			radius.value = "5";
	});
</script>

<div class="row">
	<div class="col-md-12 col-xs-12">

		<h3>Search for an ad</h3>
		<div class="row">
			<div class="col-md-12">
				<form:form method="post" modelAttribute="searchForm"
					action="/${pagemode}/results" id="searchForm" autocomplete="off"
					class="form-horizontal">
					<div class="panel panel-default">
						<div class="panel-body">
							<div class="form-group">
								<label class="col-sm-2 control-label">Type</label>
								<div class="col-sm-6">
									<c:forEach var="type" items="${types}">
										<label class="checkbox-inline"> <form:checkbox
												path="types" value="${type}" /> ${type.name}
										</label>
									</c:forEach>
								</div>
							</div>
							<spring:bind path="city">
								<div class="form-group ${status.error ? 'has-error' : '' }">
									<label for="cityInput" class="col-sm-2 control-label">City
										/ zip code</label>
									<div class="col-sm-4">
										<form:input type="text" name="city" id="cityInput" path="city"
											placeholder="e.g. Bern" cssClass="form-control" />
										<form:errors path="city"/>
									</div>
								</div>
							</spring:bind>
							<spring:bind path="radius">
								<div class="form-group ${status.error ? 'has-error' : '' }">
									<label for="radiusInput" class="col-sm-2 control-label">Within
										radius of (max.)</label>
									<div class="col-sm-4">
										<div class="input-group">
											<form:input id="radiusInput" type="number" path="radius"
												placeholder="e.g. 5" step="5" cssClass="form-control" />
											<span class="input-group-addon">km</span>
										</div>
										<form:errors path="radius"/>
									</div>
								</div>
							</spring:bind>
							<spring:bind path="prize">
								<div class="form-group ${status.error ? 'has-error' : '' }">
									<label class="col-sm-2 control-label" for="prizeInput">Price
										(max.)</label>
									<div class="col-sm-4">
										<div class="input-group">
											<span class="input-group-addon">Fr.</span>
											<form:input id="prizeInput" type="number" path="prize"
												placeholder="e.g. 5" step="50" cssClass="form-control" />
										</div>
										<form:errors path="prize"/>
									</div>
								</div>
							</spring:bind>
						</div>
					</div>
					<div class="form-group pull-right">
						<div class="col-sm-12">
							<button type="reset" class="btn btn-default">Cancel</button>
							<button type="submit" class="btn btn-primary">Search</button>
						</div>
					</div>
				</form:form>
			</div>
		</div>
	</div>
</div>

<c:import url="template/footer.jsp" />