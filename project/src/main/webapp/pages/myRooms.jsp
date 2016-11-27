<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:import url="template/header.jsp" />

<script>
	$(document).ready(function() {
	});

</script>

<ol class="breadcrumb">
	<li><a href="/${pagemode}/">Homepage</a></li>
	<li class="active">My ads</li>
</ol>
<div class="row">
	<c:choose>
		<c:when test="${empty ownAdvertisements}">
			<div class="col-md-6 col-xs-12">
				<h3>My Advertisements</h3>
				<p>You have not advertised anything yet.</p>
			</div>
		</c:when>
		<c:otherwise>
			<div class="col-md-6 col-xs-12">
				<h3>My Advertisements</h3>
				<p>
					See your placed ads below. If you want to have an overview of your
					auctions, go to <a href="/buy/profile/auctions">auction
						management</a> instead.
				</p>
				<c:forEach var="ad" items="${ownAdvertisements}">
					<div class="row">
						<div class="col-md-12 ad-wide-preview-outer">
							<div class="ad-wide-preview-inner">
								<div class="row">
									<div class="col-sm-4 col-md-4">
										<a href="<c:url value='/${ad.buyMode.name}/ad?id=${ad.id}' />"><img
											class="img-responsive" src="${ad.pictures[0].filePath}" /></a>
									</div>
									<div class="col-sm-8 col-md-8">
										<h4>
											<a
												href="<c:url value='/${ad.buyMode.name}/ad?id=${ad.id}' />">${ad.title }</a>
										</h4>
										<p>${ad.street},&nbsp;${ad.zipcode}&nbsp;${ad.city}</p>
										<br />
										<p>
											<i>${ad.type.name}</i>
										</p>
										<fmt:formatNumber value="${ad.price}" var="formattedPrice"
											pattern="###,###,### CHF" />
										<strong>${formattedPrice}</strong>

										<p>Move-in date: ${ad.moveInDate }</p>
										<c:if test="${ad.auction}">
											<p>
												This is ad is under <strong>auction</strong>. <a
													href="/${ad.buyMode.name}/profile/auction?id=${ad.id}">View
													auction details</a>
											</p>
										</c:if>
									</div>
								</div>
							</div>
						</div>
					</div>
				</c:forEach>
			</div>
		</c:otherwise>
	</c:choose>


	<c:choose>
		<c:when test="${empty bookmarkedAdvertisements}">
			<div class="col-md-6 col-xs-12">
				<h3>My Bookmarks</h3>
				<p>You have not bookmarked anything yet.</p>
			</div>
		</c:when>
		<c:otherwise>
			<div class="col-md-6 col-xs-12">
				<h3>My Bookmarks</h3>
				<p>
					See your bookmarked ads below.
				</p>
				<c:forEach var="ad" items="${bookmarkedAdvertisements}">
					<div class="row">
						<div class="col-md-12 ad-wide-preview-outer">
							<div class="ad-wide-preview-inner">
								<div class="row">
									<div class="col-sm-4 col-md-4">
										<a href="<c:url value='/${ad.buyMode.name}/ad?id=${ad.id}' />"><img
											class="img-responsive" src="${ad.pictures[0].filePath}" /></a>
									</div>
									<div class="col-sm-8 col-md-8">
										<h4>
											<a
												href="<c:url value='/${ad.buyMode.name}/ad?id=${ad.id}' />">${ad.title }</a>
										</h4>
										<p>${ad.street},&nbsp;${ad.zipcode}&nbsp;${ad.city}</p>
										<br />
										<p>
											<i>${ad.type.name}</i>
										</p>
<<<<<<< HEAD
										<fmt:formatNumber value="${ad.price}" var="formattedPrice"
											pattern="###,###,### CHF" />
										<strong>${formattedPrice}</strong>
=======
										<strong><fmt:formatNumber value="${ad.price}"
													var="formattedNumber" pattern="###,### CHF" />${formattedNumber}</strong>
>>>>>>> guiImprovements
										<p>Move-in date: ${ad.moveInDate }</p>
									</div>
								</div>
							</div>
						</div>
					</div>
				</c:forEach>
			</div>
		</c:otherwise>
	</c:choose>
</div>

<c:import url="template/footer.jsp" />