<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:import url="template/header.jsp" />

<div class="row">
	<div class="col-md-12 col-xs-12">
		<c:choose>
			<c:when test="${empty newest}">
				<h3>No ads placed yet</h3>
			</c:when>
			<c:otherwise>
				<h3>Highlights</h3>
			
				<div class="row">
					<c:forEach var="ad" items="${newest}">
						<div class="col-xs-12 col-sm-6 col-md-4 col-lg-3 ad-small-preview-outer">
							<div class="col-md-12 ad-small-preview-inner">
								<a href="<c:url value='/ad?id=${ad.id}' />">
									<img class="img-responsive" src="${ad.pictures[0].filePath}" />
								</a>
								<h4>
									<a class="link" href="<c:url value='/ad?id=${ad.id}' />">${ad.title}</a>
								</h4>
								<p>${ad.street}, ${ad.zipcode} ${ad.city}</p>
								<p>
									<i>${ad.type.name}</i>
								</p>
								<strong>CHF ${ad.prizePerMonth }</strong>	
								<fmt:formatDate value="${ad.moveInDate}" var="formattedMoveInDate" type="date" pattern="dd.MM.yyyy" />
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