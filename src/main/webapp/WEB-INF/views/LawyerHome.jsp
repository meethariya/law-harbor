<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="ISO-8859-1">
<title>Home</title>
</head>
<body>
	<h1>Welcome ${username}</h1>
	<h2>All Appointments</h2>
	<h3>${ err }</h3>
	
	<c:forEach var="booking" items="${allBooking}">
		<p>${booking}</p>
		<!-- If Booking not Confirmed -->
		<c:choose>
			<c:when test="${ !booking.isBookingStatus() }">
				<a href="approveBooking/${booking.getBookingId()}"><button>Approve Appointment</button></a>
				<a href="cancelBooking/${booking.getBookingId()}"><button>Cancel Appointment</button></a>	
			</c:when>
			
			<c:otherwise>
				<!-- If Report not generated -->
				<c:choose>
					<c:when test="${booking.getReport() == null }">				
						<a href="report/${booking.getBookingId()}"><button>Generate Report</button></a>
					</c:when>
					
					<c:otherwise>
						<button onclick="divToggler('${booking.getReport().getReportId() }')">View Report</button>	
						<div id="viewReportDiv${booking.getReport().getReportId() }" style="display: none">
							<p>${booking.getReport()}</p>
						</div>			
					</c:otherwise>
				</c:choose>
			</c:otherwise>
		
		</c:choose>
	</c:forEach>
	<br>

	<a href="caseRecord">Case Record</a>
	<a href="logout">Logout</a>
	
	<script type="text/javascript">
		function divToggler(reportId){
			<!-- Toggles display property of view report div -->
			var x = document.getElementById("viewReportDiv"+reportId);
			if (x.style.display === "none") {
				x.style.display = "block";
			} else {
				x.style.display = "none";
			}
		}
	</script>
</body>
</html>