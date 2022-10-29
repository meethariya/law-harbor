<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="tags"%>
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
		<form:input type="email" 	name="email"		id="email" 		path="email"/>
		<form:errors path="email"/><br>
		
		<label for="password">Password: </label>
		<form:password 				name="password"		id="password" 	path="password"/>
		<form:errors path="password"/><br>
		
		<label for="username">User Name: </label>
		<form:input 				name="username"		id="username" 	path="username"/>
		<form:errors path="username"/><br>
		
		<label for="mobile">Mobile Number: </label>
		<form:input type="number" 	name="mobileNumber"	id="mobile" 	path="mobileNumber"/>
		<form:errors path="mobileNumber"/><br>
		
		<label for="role">Role: </label>
		<form:select 				name="role"			id="role" 		path="role">  
			<option value = "user" selected> User </option>  
			<option value = "lawyer"> Lawyer </option>  
			<option value = "admin"> Admin </option>   
		</form:select>  
		<form:errors path="role"/><br>
		
		<form:button value="Submit">Submit</form:button>
	</form:form>
</body>
</html>