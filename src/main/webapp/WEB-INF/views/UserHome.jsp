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
	<h3>List of Lawyers</h3>
	<c:forEach var="lawyer" items="${allLawyer}">
		<button onclick="toggleForm(${lawyer.getId()})">${lawyer}</button>
	</c:forEach>
	<br>
	
	<!-- Booking form -->
	<div class="toggler" id="myForm" style="display: none">
		<form action="bookingForm" method='POST'>
			<label for="appointmentDate">Date</label> 
			<input type="date" id="appointmentDate" name="appointmentDate">
			<br>
			<label for="appointmentTime">Slot Time</label> 
			<select id = "appointmentTime" name= "appointmentTime">
				<%for (int i = 9; i <= 19; i++) {%>
				<option value="<%=i%>"> <%=i %> </option>
				<%}%>
			</select>
			<br/>
			<input type="hidden" id = "setLawyerId" name="lawyerId">
			<button type="submit">Book</button>
		</form>
	</div>
	
	<a href="caseRecord"> Case Record</a>
	<a href="logout">Logout</a>

	<script type="text/javascript">
	<!-- Opens booking form on lawyer click -->
	function toggleForm(cardid){
	    var x = document.getElementById("myForm");
	    document.getElementById("setLawyerId").value = cardid;
	    if (x.style.display === "none") {
	      x.style.display = "block";
	    } else {
	      x.style.display = "none";
	    }
	  }
	</script>
</body>
</html>