<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<c:import url="template/header.jsp" />

<ol class="breadcrumb">
	<li><a href="/${pagemode}/">Homepage</a></li>
	<li><a href="./schedule">Schedule</a></li>
	<li class="active">Visitors</li>
</ol>

<h2>Visitors for your property</h2>

<p>Information about the property: <a href="/${ad.buyMode.name}/ad?id=${ad.id }">${ad.street }, ${ad.zipcode } ${ad.city }</a></p>

<div id="visitorsDiv">			
<c:choose>
	<c:when test="${empty visitors}">
		<p>This property doesn't have any scheduled visitors at the moment.
	</c:when>
	<c:otherwise>
				<table id="visitors" class="table table-striped">
			<thead>
			<tr>
				<th>Name</th>
				<th>Username</th>
				<th>Profile</th>
			</tr>
			</thead>
		<c:forEach var="visitor" items="${visitors}">
			<tr>
				<td>${visitor.firstName} ${visitor.lastName }</td>
				<td>${visitor.username}</td>
				<td><a class="btn btn-default" href="../user?id=${visitor.id}">Visit Profile</a></td>
			</tr>
		</c:forEach>
		</table>
	</c:otherwise>
</c:choose>
</div>

<c:import url="template/footer.jsp" />