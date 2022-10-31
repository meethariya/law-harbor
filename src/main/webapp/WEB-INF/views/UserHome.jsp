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
	<h1>Welcome ${userName}</h1>
	<h3>${ bookingStatus }</h3>
	<h3>List of Lawyers</h3>
	<c:forEach var="lawyer" items="${allLawyer}">
		<button onclick="toggleForm('${lawyer.getEmail()}')">${lawyer}</button>
	</c:forEach>
	<br>
	
	<!-- Booking form -->
	<div id="appointmentForm" style="display: none">
		<form action="bookingForm" method='POST'>
			<label for="appointmentDate">Date</label> 
			<input type="date" id="appointmentDate" name="appointmentDate">
			<br>
			<label for="appointmentTime">Slot Time</label> 
			<select id = "appointmentTime" name= "appointmentTime">
				<%for (int i = 9; i <= 19; i++) {%>
				<option value="<%=i%>"> <%=i %>:00 </option>
				<%}%>
			</select>
			<br/>
			<input type="hidden" id = "setLawyer" name="lawyerEmail">
			<input type="hidden" id = "setClient" name="userEmail" value="${ sessionScope.userEmail }">			
			<button type="submit">Book</button>
		</form>
	</div>
	
	<a href="caseRecord"> Case Record</a>
	<a href="allBooking"> All Bookings</a>
	<a href="logout">Logout</a>

	<script type="text/javascript">
	<!-- Opens booking form on lawyer click and sets lawyer email-->
	function toggleForm(email){
	    var x = document.getElementById("appointmentForm");
	    document.getElementById("setLawyer").value = email;
	    if (x.style.display === "none") {
	      x.style.display = "block";
	    } else {
	      x.style.display = "none";
	    }
	    <!-- Set Min date as today -->
	    var d = new Date();
	    var s = d.getFullYear() + '-' + 
        ('0' + (d.getMonth()+1)).slice(-2) + '-' +
        ('0' + d.getDate()).slice(-2);
	    document.getElementById('appointmentDate').setAttribute('min', s);
	  }
	</script>
</body>
</html>