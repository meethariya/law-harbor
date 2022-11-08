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
		<div class="alert alert-warning alert-dismissible show w-50 mx-auto" role="alert">
			${ removeBookingMessage }
			<button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
		</div>
		<div class="container">
			<div class="row">
				<div class="col">
					<h3>User Bookings</h3>
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
										<a href="removeBooking/${booking.getBookingId()}">
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