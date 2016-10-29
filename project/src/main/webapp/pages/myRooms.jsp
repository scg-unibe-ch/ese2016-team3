<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<c:import url="template/header.jsp" />

<script>
	$(document).ready(function() {
	});

</script>

<ol class="breadcrumb">
	<li><a href="./">Homepage</a></li>
	<li class="active">My ads</li>
</ol>

<c:choose>
	<c:when test="${empty ownAdvertisements}">
		<div class="col-md-12 col-xs-12">
			<h3>My Advertisements</h3>
			<p>You have not advertised anything yet.</p>
		</div>
	</c:when>
	<c:otherwise>
	
	<div class="col-md-12 col-xs-12">
		<div id="resultsDiv" class="resultsDiv">
			<h3>My Advertisements</h3>		
			<c:forEach var="ad" items="${ownAdvertisements}">
			<div class="clearfix">
			<div class="col-md-12 ad-wide-preview-inner">
				<div class="resultAd" data-price="${ad.prizePerMonth}" 
								data-moveIn="${ad.moveInDate}" data-age="${ad.moveInDate}">
					<div class="row">
						<div class="col-sm-4 col-md-4">
							<a href="<c:url value='/ad?id=${ad.id}' />"><img
							class="img-responsive" src="${ad.pictures[0].filePath}" /></a>
						</div>
						<div class="col-sm-5 col-md-5">
							<h4>
								<a href="<c:url value='/ad?id=${ad.id}' />">${ad.title }</a>
							</h4>
							<p>${ad.street}, ${ad.zipcode} ${ad.city}</p>
							<br />
							<p>
								<i>${ad.type.name}</i>
							</p>
							<strong>CHF ${ad.prizePerMonth }</strong>
							<p>Move-in date: ${ad.moveInDate }</p>
						</div>
					</div>
				</div>
			</div>
			<br />
			</div>
			</c:forEach>
		</div>	
		<br /><br />
	</div>	
	</c:otherwise>
</c:choose>


<c:choose>
	<c:when test="${empty bookmarkedAdvertisements}">
		<div class="col-md-12 col-xs-12">
			<h3>My Bookmarks</h3>
			<p>You have not bookmarked anything yet.</p>
		</div>
	</c:when>
	<c:otherwise>
		
	<div class="col-md-12 col-xs-12">
		<div id="resultsDiv" class="resultsDiv">
			<h3>My Bookmarks</h3>		
			<c:forEach var="ad" items="${bookmarkedAdvertisements}">
				<div class="col-md-12 ad-wide-preview-inner">
					<div class="resultAd" data-price="${ad.prizePerMonth}" 
									data-moveIn="${ad.moveInDate}" data-age="${ad.moveInDate}">
							<div class="row">
								<div class="col-sm-4 col-md-4">
								<a href="<c:url value='/ad?id=${ad.id}' />"><img
									class="img-responsive" src="${ad.pictures[0].filePath}" /></a>
								</div>
								<div class="col-sm-5 col-md-5">
								<h4>
									<a href="<c:url value='/ad?id=${ad.id}' />">${ad.title }</a>
								</h4>
								<p>${ad.street}, ${ad.zipcode} ${ad.city}</p>
								<br />
								<p>
									<i>${ad.type.name}</i>
								</p>
								<strong>CHF ${ad.prizePerMonth }</strong>
								<p>Move-in date: ${ad.moveInDate }</p>
							</div>
						</div>
					</div>
				</div>
			</c:forEach>
		</div>	
	</div>	
	</c:otherwise>
</c:choose>


<c:import url="template/footer.jsp" />