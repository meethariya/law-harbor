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
	<h1>Customer Registration Form</h1><br>
	<form action="registerForm" method="POST">
		<label for="email">Email: </label>
		<input type="email" 	name="email"		id="email"><br>
		<label for="password">Password: </label>
		<input type="password" 	name="password"		id="password"><br>
		<label for="username">User Name: </label>
		<input type="text" 		name="username"		id="username"><br>
		<label for="mobile">Mobile Number: </label>
		<input type="number" 	name="mobileNumber"	id="mobile"><br>
		<label for="role">Role: </label>
		<input type="text" 		name="role"			id="role"><br>
		<input type="submit" 	value="Submit" />
	</form>
</body>
</html>