<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="tags"%>
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
		<form:form action="bookingForm" method='POST' modelAttribute="booking">
			<label for="appointmentDate">Date</label> 
			<form:input type="date" id="appointmentDate" name="appointmentDate" path="appointmentDate" required="true" />
			<form:errors path="appointmentDate"/>
			<br>
			<label for="appointmentTime">Slot Time</label> 
			<form:select id = "appointmentTime" name= "appointmentTime" path="appointmentTime" required="true" >
				<%for (int i = 9; i <= 19; i++) {%>
				<form:option value="<%=i%>"> <%=i %>:00 </form:option>
				<%}%>
			</form:select>
			<form:errors path="appointmentTime"/>
			<br>
			<label for="subject">Subject</label> 
			<form:input type="text" id="subject" name="subject" path="subject" required="true" />
			<form:errors path="subject"/>
			<br>
			<input type="hidden" id = "setLawyer" name="lawyerEmail">
			<input type="hidden" id = "setClient" name="userEmail" value="${ sessionScope.userEmail }">			
			<button type="submit">Book</button>
		</form:form>
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