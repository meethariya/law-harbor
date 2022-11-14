<%@page import="java.time.LocalDate"%>
<%@page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<jsp:include page="BootstrapCss.jsp"/>
<title>User Bookings</title>
</head>
<body>
	<jsp:include page="Navbar.jsp"/>
	<div class = "fluid-container mx-2 my-2">		
		<c:choose>	
			<c:when test="${errMessage!=null && errMessage.length()!=0}">
				<div class="alert alert-warning alert-dismissible show w-50 mx-auto" role="alert">
					${errMessage}
					<button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
				</div>
			</c:when>
		</c:choose>
		
		<div class="container">
			<div class="row text-center">
				<div class="col">
					<h3>User Bookings</h3>
				</div>
			</div>
			<div class="row border border-primary rounded-3 my-2">
				<div class="col col-sm-3">
					<!-- Example single danger button -->
					<div class="btn-group my-2">
					  <button type="button" class="btn btn-info dropdown-toggle" data-bs-toggle="dropdown" aria-expanded="false">
					    Get by year
					  </button>
					  <ul class="dropdown-menu">
					  	<%for (int i = LocalDate.now().getYear(); i > LocalDate.now().getYear()-3; i--) {%>
						    <li><a class="dropdown-item" href="/project/user/allbooking/<%=i%>"><%=i%></a></li>
						<%}%>
					    <li><hr class="dropdown-divider"></li>
					    <li><a class="dropdown-item" href="/project/user/allbooking/earlier">Earlier</a></li>
					  </ul>
					</div>
				</div>
				<div class="col col-sm-4">
					<form action="/project/user/bookingByLawyername" method="POST">
						<div class="input-group my-2">
						  <input class="form-control" placeholder="Lawyer name" aria-label="Lawyer name" aria-describedby="search"
						  	name="searchField" id="searchField" required max="30">
						  <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
						  <button class="btn btn-outline-success" type="submit" id="search">Search</button>
						</div>
					</form>	
				</div>
			</div>
			<div class="row">
				<c:forEach var="booking" items="${allBooking}">
					<div class="col col-sm-4">

						<div class="card text-bg-light mx-auto my-2">

							<div class="card-body">
								<h5 class="card-title">Appointment for: ${ booking.getLawyer().getUsername()}</h5>
								<br>
								<h6 class="card-subtitle">Slot: ${booking.getDate()}</h6>
								<p class="card-text">Topic: ${booking.getTopic()}</p>
								<p class="card-text">Subject: ${booking.getSubject()}</p>
								<c:choose>
									<c:when test="${booking.isBookingStatus() }">							
										<h5 class="text-success">Booking Status: Confirmed</h5>
									</c:when>
									<c:otherwise>
										<h5 class="text-warning">Booking Status: Not Confirmed</h5>									
									</c:otherwise>
								</c:choose>
							</div>

							<div class="card-footer text-muted">
								<!-- If Booking is not confirmed -->
								<c:choose>
									<c:when test="${!booking.isBookingStatus() }">
										<a href="/project/user/removeBooking/${booking.getBookingId()}">
											<button class="btn btn-danger">Remove Booking</button>
										</a>
									</c:when>
									<c:otherwise>
										<c:choose>
											<c:when test="${booking.getReport() != null }">
												<button class="btn btn-primary" onclick="myDivToggler('${booking.getReport().getReportId() }')">
													View Report</button>
												<div id="viewReportDiv${booking.getReport().getReportId() }"
													style="display: none">
													<p>Report Date: ${booking.getReport().getDateTime().toString().split(" ")[0]}</p>
													<p>Report Detail: ${booking.getReport().getReportDetail()}</p>
												</div>
											</c:when>
										</c:choose>
									</c:otherwise>
								</c:choose>
							</div>

						</div>

					</div>
				</c:forEach>
			</div>
		</div>
	</div>
	<script src="<c:url value="/resources/js/userbooking.js"/>"></script>
	<script src="<c:url value="/resources/js/userscript.js"/>"></script>
	<jsp:include page="BootstrapJs.jsp"/>
</body>
</html>