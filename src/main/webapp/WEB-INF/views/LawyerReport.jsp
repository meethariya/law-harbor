<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="tags"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="ISO-8859-1">
<title>Generate Report</title>
</head>
<body>
	<h3>${errMessage }</h3>
	<form:form action="/project/lawyer/report" method="POST" modelAttribute="report">
		<label for="reportDetail">*Report Detail: </label>
		<form:textarea name="reportDetail" id="reportDetail"
			path="reportDetail" required="true" />
		<form:errors path="reportDetail" />
		<br>
	
		<form:input type="hidden" name = "bookingId" path = "bookingId" value="${bookingId }"/>
		
		<label>*Case Record: </label>
		<c:forEach var="myCaseRecord" items="${userCaseRecord}">
			<form:checkbox value = "${myCaseRecord.getCaseRecordId()}" path="tempCaseRecord" 
				label = "${myCaseRecord.getCaseRecordId()}"/>
		</c:forEach>			
		<form:errors path="tempCaseRecord" />
		<br>
		
		<form:button value="Submit">Submit</form:button>
	</form:form>
</body>
</html>