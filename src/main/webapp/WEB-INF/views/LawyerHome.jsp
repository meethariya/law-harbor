<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<jsp:include page="BootstrapCss.jsp"/>
<title>Home</title>
</head>
<body>
	<jsp:include page="Navbar.jsp"/>
	<div class = "fluid-container mx-2 my-2">
		<div class="alert alert-warning alert-dismissible show w-50 mx-auto" role="alert">
			${err}
			<button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
		</div>
		<h1>Welcome ${username}</h1>
		<div class="container">
			<div class="row text-center">
				<div class="col">
					<h2>All Appointments</h2>
				</div>
			</div>
			<div class="row">
				<c:forEach var="booking" items="${allBooking}">
					<div class="col col-sm-4">
						<div class="card text-bg-light mx-auto my-2">
							<div class="card-body">
								<h5 class="card-title">Appointment by: ${booking.getClient().getUsername()}</h5>
								<h6 class="card-subtitle">Slot: ${booking.getDate()}</h6>
								<p class="card-text">Subject: ${booking.getSubject()}</p>
							</div>

							<div class="card-footer text-center text-muted">
								<c:choose>
									<c:when test="${ !booking.isBookingStatus() }">
										<div class="btn-group" role="group" aria-label="Basic example">
											<a href="approveBooking/${booking.getBookingId()}" class="btn btn-success">
												Approve
											</a>
											<a href="cancelBooking/${booking.getBookingId()}" class="btn btn-danger">
												  Reject 
											</a>
										</div>
									</c:when>
									<c:otherwise>
										<!-- If Report not generated -->
										<c:choose>
											<c:when test="${booking.getReport() == null }">
												<a href="report/${booking.getBookingId()}">
													<button class="btn btn-warning">Generate Report</button>
												</a>
											</c:when>
											<c:otherwise>
												<button class="btn btn-info" onclick="divToggler('${booking.getReport().getReportId() }')">
													View Report
												</button>
												<div id="viewReportDiv${booking.getReport().getReportId() }" style="display: none">
													<p>Report Date: ${booking.getReport().getDateTime().toString().split(" ")[0]}</p>
													<p>Report Detail: ${booking.getReport().getReportDetail()}</p>
												</div>
											</c:otherwise>
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
	<script src="<c:url value="/resources/js/lawyerhome.js"/>"></script>
	<script src="<c:url value="/resources/js/lawyerscript.js"/>"></script>
	<jsp:include page="BootstrapJs.jsp"/>
</body>
</html>