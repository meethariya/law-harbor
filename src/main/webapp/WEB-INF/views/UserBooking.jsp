<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="ISO-8859-1">
<title>User Bookings</title>
</head>
<body>
	<h3>User Bookings</h3>
	<h3>${ removeBookingMessage }</h3>

	<ul>
		<c:forEach var="booking" items="${allBooking}">
			<li>${booking}<!-- If Booking is not confirmed --> <c:choose>
					<c:when test="${!booking.isBookingStatus() }">
						<a href="removeBooking/${booking.getBookingId()}">
							<button>Remove Booking</button>
						</a>
					</c:when>
					<c:otherwise>
						<c:choose>
							<c:when test="${booking.getReport() != null }">
								<button
									onclick="divToggler('${booking.getReport().getReportId() }')">View
									Report</button>
								<div id="viewReportDiv${booking.getReport().getReportId() }"
									style="display: none">
									<p>${booking.getReport()}</p>
								</div>
							</c:when>
						</c:choose>
					</c:otherwise>
				</c:choose>
			</li>
		</c:forEach>
	</ul>
	<br>

	<a href="home">Home</a>

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