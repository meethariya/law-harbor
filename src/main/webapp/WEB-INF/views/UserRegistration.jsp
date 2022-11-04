<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="tags"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="ISO-8859-1">
<title>User Registration</title>
</head>
<body>
	<h1>Customer Registration Form</h1>
	<h2>${errMessage}</h2><br>
	
	<form:form action="registerForm" method="POST" modelAttribute="user">
		<label for="email">Email: </label>
		<form:input type="email" 	name="email"		id="email" 		path="email" required="true" />
		<form:errors path="email"/><br>
		
		<label for="password">Password: </label>
		<form:password 				name="password"		id="password" 	path="password" required="true" />
		<form:errors path="password"/><br>
		
		<label for="username">User Name: </label>
		<form:input 				name="username"		id="username" 	path="username" required="true" />
		<form:errors path="username"/><br>
		
		<label for="mobile">Mobile Number: </label>
		<form:input type="number" 	name="mobileNumber"	id="mobile" 	path="mobileNumber" min="0" required="true" />
		<form:errors path="mobileNumber"/><br>
		
		<label for="role">Role: </label>
		<form:select 				name="role"			id="role" 		path="role" required="true" >  
			<c:forEach var="role" items="${roles }">			
				<option value = "${role }" > ${role } </option>  
			</c:forEach>
		</form:select>  
		<form:errors path="role"/><br>
		
		<!-- Additional lawyer information -->
		<div id="myDiv" style="display: none">
			<label for="experience">Experience(years): </label>
			<form:input type="number" name="experience"	id="experience" path="experience" min="0"/><br>
			<label for="expertise">Expertise: </label>
			<form:input  			name="expertise"	id="expertise" path="expertise"/><br>
			<label for="lawFirmName">Law Firm Name: </label>
			<form:input 			name="lawFirmName"	id="lawFirmName" path="lawFirmName"/><br>
		</div>
		
		<form:button value="Submit">Submit</form:button>
	</form:form>
	
	<script type="text/javascript">
		<!-- Opens additional lawyer information form on role option "lawyer" -->
		var myDiv = document.getElementById("myDiv");
		document.getElementById("role").onchange = function(){
		  myDiv.style.display = (this.selectedIndex == 1) ? "block" : "none";
		}
	</script>
</body>
</html>