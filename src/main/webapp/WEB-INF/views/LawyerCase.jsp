<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="tags"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="ISO-8859-1">
<title>Case Records</title>
</head>
<body>
<h3>Case Records</h3>
	<h3>${errMessage}</h3>
	<button onclick="addFormToggler()">Create Case Record</button>
	<div id="addDiv" style="display: none">
	<form:form action="caseRecord" method="POST" modelAttribute="case">
		<label for="userEmail">*User Email: </label>
		<form:input type="email" 	name="userEmail"	id="userEmail" 		path="userEmail" required="true"/>
		<form:errors path="userEmail"/><br>
		
		<label for="eventDetail">*Event Detail: </label>
		<form:textarea 				name="eventDetail"	id="eventDetail" 	path="eventDetail" required="true"/>
		<form:errors path="eventDetail"/><br>
		
		<label for="actionTaken">*Action Taken: </label>
		<form:textarea 				name="actionTaken"	id="actionTaken" 	path="actionTaken" required="true"/>
		<form:errors path="actionTaken"/><br>
		<form:button value="Submit">Submit</form:button>
	</form:form>
	</div>
	<div id="editDiv" style="display: none">
	<form:form method="POST" modelAttribute="case" id = "editForm">
		<label for="userEmail">*User Email: </label>
		<input type="email" 	name="userEmail"	id="editUserEmail" 	disabled/>
		<form:input type="hidden"  	id="hiddenEditEmail" path="userEmail"/>
		<form:errors path="userEmail"/><br>
		
		<label for="eventDetail">*Event Detail: </label>
		<form:textarea 				name="eventDetail"	id="editEventDetail" 	path="eventDetail" required="true"/>
		<form:errors path="eventDetail"/><br>
		
		<label for="actionTaken">*Action Taken: </label>
		<form:textarea 				name="actionTaken"	id="editActionTaken" 	path="actionTaken" required="true"/>
		<form:errors path="actionTaken"/><br>
		<form:button value="Submit">Submit</form:button>
	</form:form>
	</div>
	
	<ul>
		<c:forEach var="caseRecord" items="${allCaseRecord}">
			<li>${caseRecord}</li>
			<button onclick="editFormToggler('${caseRecord.getUser().getEmail()}',
			 '${caseRecord.getEventDetail()}', '${caseRecord.getActionTaken()}', '${ caseRecord.getCaseRecordId() }')"> Edit Case Record </button>
			<a href="caseRecord/${caseRecord.getCaseRecordId()}">
				<button>Delete Lawyer</button>
			</a>
		</c:forEach>
	</ul>
	<br><a href="/project/lawyer/">Home</a>
	
	<script type="text/javascript">
		function addFormToggler(){
			divToggler(document.getElementById("addDiv"));
		}
		function editFormToggler(email, eventDetail, actionTaken,id){			
			divToggler(document.getElementById("editDiv"));
			
			document.getElementById("editUserEmail").value = email;
			document.getElementById("hiddenEditEmail").value = email;
			document.getElementById("editEventDetail").value = eventDetail;
			document.getElementById("editActionTaken").value = actionTaken;
			document.getElementById("editForm").action = "caseRecord/"+id;
			
		}
		function divToggler(x){
			if (x.style.display === "none") {
			      x.style.display = "block";
			    } else {
			      x.style.display = "none";
			    }
		}
	</script>
</body>
</html>