<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="tags" %>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="ISO-8859-1">
<title>User Login</title>
</head>
<body>
	<h1>Login</h1>
	<h2>${credentialErr}</h2>
	<form action="loginForm" method="POST">
		<label for="email">Email:</label>
		<br>
		<input type="email" id="email" name="email"/>
		<label for="password">Password:</label>
		<br>
		<input type="password" id="password" name="password"/>		
		<br>
		<input type="submit" value="Submit" />
	</form>
</body>
</html>