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
		<c:choose>
			<c:when test="${ !booking.isBookingStatus() }">
				<a href="approveBooking/${booking.getBookingId()}"><button>Approve Appointment</button></a>
			</c:when>
		</c:choose>
		<a href="cancelBooking/${booking.getBookingId()}"><button>Cancel Appointment</button></a>	
	</c:forEach>
	<br>

	<a href="caseRecord">Case Record</a>
	<a href="logout">Logout</a>
</body>
</html>