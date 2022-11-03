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
			<li>
				${booking}
				<c:choose>
					<c:when test="${!booking.isBookingStatus() }">
						<a href="removeBooking/${booking.getBookingId()}"><button>Remove Booking</button></a>
					</c:when>
				</c:choose>
			</li>
		</c:forEach>
	</ul>
	<br><a href="home">Home</a>
</body>
</html>