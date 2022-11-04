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
	<h2>${errMessage}</h2>
	
	<form:form action="loginForm" method="POST" modelAttribute="user">
		<label for="email">Email: </label>
		<form:input type="email" 	name="email"		id="email" 		path="email" required="true" />
		<form:errors path="email"/><br>
		
		<label for="password">Password: </label>
		<form:password 				name="password"		id="password" 	path="password" required="true" />
		<form:errors path="password"/><br>
		
		<form:button value="Submit">Submit</form:button>
	</form:form>
	
	<a href="register">Not yet registered? SignUp</a><br>
</body>
</html>