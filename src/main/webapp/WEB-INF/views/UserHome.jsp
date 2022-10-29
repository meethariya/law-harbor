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
	<ul>
		<c:forEach var="lawyer" items="${allLawyer}">
			<li>${lawyer}</li>
		</c:forEach>
	</ul>
	<a href="caseRecord"> Case Record</a>
	<a href="logout">Logout</a>
</body>
</html>