<%@page import="java.time.LocalDate"%>
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
		<c:choose>	
			<c:when test="${errMessage!=null && errMessage.length()!=0}">
				<div class="alert alert-warning alert-dismissible show w-50 mx-auto" role="alert">
					${errMessage}
					<button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
				</div>
			</c:when>
		</c:choose>
		<h1>Welcome ${username}</h1>
		<div class="container">
			<div class="row text-center">
				<div class="col">
					<h2>All Appointments</h2>
				</div>
			</div>
			<div class="row border border-primary rounded-3">
				<div class="col col-sm-1 my-2">
				</div>
				<div class="col col-sm-3 my-2">
					<div class="btn-group">
					  <button type="button" class="btn btn-success dropdown-toggle" data-bs-toggle="dropdown" aria-expanded="false">
					    Get by year
					  </button>
					  <ul class="dropdown-menu">
					  	<%for (int i = LocalDate.now().getYear(); i > LocalDate.now().getYear()-3; i--) {%>
						    <li><a class="dropdown-item" href="/project/lawyer/booking/<%=i%>"><%=i%></a></li>
						<%}%>
					    <li><hr class="dropdown-divider"></li>
					    <li><a class="dropdown-item" href="/project/lawyer/booking/earlier">Earlier</a></li>
					  </ul>
					</div>
				</div>
				<div class="col col-sm-3 my-2">
					<form action="/project/lawyer/searchByUsername" method="POST">
						<div class="input-group">
						  <input type="text" class="form-control" placeholder="User name" required
						  	aria-label="username" aria-describedby="search" max="30" name="username">
						  <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
						  <button class="btn btn-outline-success" type="submit" id="search" value="Submit">Search</button>
						</div>
					</form>
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
											<a href="/project/lawyer/approveBooking/${booking.getBookingId()}" class="btn btn-success">
												Approve
											</a>
											<a href="/project/lawyer/cancelBooking/${booking.getBookingId()}" class="btn btn-danger">
												  Reject 
											</a>
										</div>
									</c:when>
									<c:otherwise>
										<!-- If Report not generated -->
										<c:choose>
											<c:when test="${booking.getReport() == null }">
												<a href="/project/lawyer/report/${booking.getBookingId()}">
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