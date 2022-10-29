<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="ISO-8859-1">
<title>Case Records</title>
</head>
<body>
<h3>Case Records</h3>
	<ul>
		<c:forEach var="caseRecord" items="${allCase}">
			<li>${caseRecord}</li>
		</c:forEach>
	</ul>
</body>
</html>