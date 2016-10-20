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
				<c:forEach var="ad" items="${newest}">
					<div class="row">
						<div class="col-xs-12 col-md-3">
							<a href="<c:url value='/ad?id=${ad.id}' />">
								<img class="img-responsive" src="${ad.pictures[0].filePath}" />
							</a>
						</div>
						<div class="col-xs-12 col-md-6">
							<h2>
								<a class="link" href="<c:url value='/ad?id=${ad.id}' />">${ad.title}</a>
							</h2>
							<p>${ad.street},${ad.zipcode}${ad.city}</p>
							<br />
							<p>
								<i><c:choose>
										<c:when test="${ad.studio}">Studio</c:when>
										<c:otherwise>Room</c:otherwise>
									</c:choose></i>
							</p>
						</div>
						<div class="col-xs-12 col-md-3">
							<h2>CHF ${ad.prizePerMonth }</h2>
							<br /> <br />

							<fmt:formatDate value="${ad.moveInDate}"
								var="formattedMoveInDate" type="date" pattern="dd.MM.yyyy" />

							<p>Move-in date: ${formattedMoveInDate }</p>
						</div>
					</div>
				</c:forEach>
			</c:otherwise>
		</c:choose>
	</div>
</div>



<c:import url="template/footer.jsp" />