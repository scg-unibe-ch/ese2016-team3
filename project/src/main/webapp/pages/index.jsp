<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:import url="template/header.jsp" />

<script type="text/javascript">
	$(document).ready(function() {
		$("#cityInput").autocomplete({
			minLength : 2,
			source : <c:import url="getzipcodes.jsp" />,
			enabled : true,
			autoFocus : true
		});
	});
</script>

<div class="row">
	<div class="col-md-12 col-xs-12">
		<div class="row">
			<div class="col-md-12">
				<div class="panel panel-default">
					<div class="panel-body">
						<form:form method="post" modelAttribute="searchForm"
							action="/${pagemode}/results" id="searchForm"
							cssClass="form-horizontal">
							<div class="col-md-3">
								<form:input type="text" name="city" id="cityInput" path="city"
									placeholder="City (e.g. Bern)" cssClass="form-control" />
							</div>
							<div class="col-md-3">
								<div class="input-group">
									<form:input type="number" name="radius" id="radiusInput"
										path="radius" placeholder="Radius (max)"
										cssClass="form-control" />
									<span class="input-group-addon">km</span>
								</div>
							</div>
							<div class="col-md-3">
								<div class="input-group">
									<span class="input-group-addon">Fr.</span>
									<form:input type="number" name="price" id="priceInput"
										path="price" placeholder="Price (max)" cssClass="form-control" />
								</div>
							</div>
							<div class="col-md-3">
								<button type="submit" class="btn btn-primary pull-right">Search</button>
							</div>
						</form:form>
					</div>
				</div>
			</div>
		</div>
		<c:choose>
			<c:when test="${empty newest}">
				<h3>No ads placed yet</h3>
			</c:when>
			<c:otherwise>
				<h3>Highlights</h3>

				<div class="row">
					<c:forEach var="ad" items="${newest}">
						<div
							class="col-xs-12 col-sm-6 col-md-4 col-lg-3 ad-small-preview-outer">
							<div class="col-md-12 ad-small-preview-inner">
								<a href="<c:url value='./ad?id=${ad.id}' />"> <img
									class="img-responsive" src="${ad.pictures[0].filePath}" />
								</a>
								<h4>
									<a class="link" href="<c:url value='./ad?id=${ad.id}' />">${ad.title}</a>
								</h4>
								<p>${ad.street},&nbsp;${ad.zipcode}&nbsp;${ad.city}</p>
								<p>
									<i>${ad.type.name}</i>
								</p>
								<strong><fmt:formatNumber value="${ad.price}"
										var="formattedNumber" pattern="###,### CHF" />${formattedNumber}</strong>
								<fmt:formatDate value="${ad.moveInDate}"
									var="formattedMoveInDate" type="date" pattern="dd.MM.yyyy" />
								<p>Move-in date: ${formattedMoveInDate }</p>
							</div>
						</div>
					</c:forEach>
				</div>
			</c:otherwise>
		</c:choose>
	</div>
</div>

<c:import url="template/footer.jsp" />



<c:import url="template/footer.jsp" />