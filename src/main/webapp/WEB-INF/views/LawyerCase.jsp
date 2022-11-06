<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="tags"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="ISO-8859-1">
<jsp:include page="BootstrapCss.jsp"/>
<title>Case Records</title>
</head>
<body>
	<jsp:include page="Navbar.jsp"/>

	<div class="fluid-container mx-2 my-2">
		<div class="alert alert-warning alert-dismissible show w-50 mx-auto" role="alert">
			${errMessage}
			<button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
		</div>
		<div class="container">
		
			<div class="row">
				<div class="col">
					<h3>Case Records</h3>
				</div>
			</div>
			
			<div class="row">
				<div class="col">
					<button class="btn btn-primary" onclick="addFormToggler()">Create Case Record</button>
					<div id="addDiv" style="display: none">
						<div class="card text-bg-light mx-auto my-2">
							<div class="card-body">
								<form:form class="form-control" action="caseRecord" method="POST" modelAttribute="case">
									<label class="form-label" for="userEmail">*User Email: </label>
									<form:input type="email" name="userEmail" id="userEmail"
										path="userEmail" class="form-control" required="true" maxlength="30"/>
									<form:errors class="text-danger" path="userEmail" />
									<br>
				
									<label class="form-label" for="eventDetail">*Event Detail: </label>
									<form:textarea name="eventDetail" id="eventDetail"
										path="eventDetail" class="form-control" required="true" maxlength="255"/>
									<form:errors class="text-danger" path="eventDetail" />
									<br>
				
									<label class="form-label" for="actionTaken">*Action Taken: </label>
									<form:textarea name="actionTaken" id="actionTaken"
										class="form-control" path="actionTaken" required="true" maxlength="255"/>
									<form:errors class="text-danger" path="actionTaken" />
									<br>
									<form:button class="btn btn-success" value="Submit">Submit</form:button>
								</form:form>
							</div>
						</div>
					</div>
				</div>
			</div>
			
			<div class="row">
				<div class="col">
					<div id="editDiv" style="display: none">
						<form:form class="form-control" method="POST" modelAttribute="case" id="editForm">
							<label class="form-label" for="userEmail">User Email: </label>
							<input class="form-control" type="email" name="userEmail" id="editUserEmail" disabled />
							<form:input type="hidden" id="hiddenEditEmail" path="userEmail" />
							<form:errors class="text-danger" path="userEmail" />
							<br>
		
							<label class="form-label" for="eventDetail">Event Detail: </label>
							<form:textarea class="form-control" name="eventDetail" id="editEventDetail"
								path="eventDetail" required="true" maxlength="255"/>
							<form:errors class="text-danger" path="eventDetail" />
							<br>
		
							<label class="form-label" for="actionTaken">Action Taken: </label>
							<form:textarea class="form-control" name="actionTaken" id="editActionTaken"
								path="actionTaken" required="true" maxlength="255"/>
							<form:errors class="text-danger" path="actionTaken" />
							<br>
							<form:button class="btn btn-success" value="Submit">Submit</form:button>
						</form:form>
					</div>
				</div>
			</div>

			<div class="row">
			
				<c:forEach var="caseRecord" items="${allCaseRecord}">
					<div class="col col-sm-4">	
					
					<div class="card text-bg-light mx-auto my-2">
										
						<div class="card-body">
							<h5 class="card-title">UserName: ${caseRecord.getUser().getUsername()}</h5>
							<h6 class="card-subtitle">Date: ${caseRecord.getDate().toString().split(" ")[0]}</h6>
							<p class="card-text">Event Details: ${caseRecord.getEventDetail()}</p>
							<p class="card-text">Action Taken: ${caseRecord.getActionTaken()}</p>
						</div>
						
						<div class="card-footer text-center text-muted">
							<!-- If report is not generated for this case -->
							<c:choose>
								<c:when test="${caseRecord.getReport()==null }">
		
									<button class="btn btn-warning"
										onclick="editFormToggler('${caseRecord.getUser().getEmail()}',
					 '${caseRecord.getEventDetail()}', '${caseRecord.getActionTaken()}', '${ caseRecord.getCaseRecordId() }')">
										Edit Case Record</button>
		
									<a href="caseRecord/${caseRecord.getCaseRecordId()}" class="btn btn-danger">
										Delete Case Record
									</a>
		
								</c:when>
								<c:otherwise>
									<h6 class="card-subtitle">Used in a Report</h6>
								</c:otherwise>
							</c:choose>
						</div>
										
					</div>
					
									
					</div>
				</c:forEach>
			</div>			
		</div>
	</div>
	<script type="text/javascript">
		function addFormToggler() {
			<!-- Displays Add Case Form -->
			divToggler(document.getElementById("addDiv"));
		}
		function editFormToggler(email, eventDetail, actionTaken, id) {
			<!-- Sets values of edit form and Displays it -->
			var x = divToggler(document.getElementById("editDiv"));

			document.getElementById("editUserEmail").value = email;
			document.getElementById("hiddenEditEmail").value = email;
			document.getElementById("editEventDetail").value = eventDetail;
			document.getElementById("editActionTaken").value = actionTaken;
			document.getElementById("editForm").action = "caseRecord/" + id;

		}
		function divToggler(x) {
			<!-- Toggles display property of a div -->
			if (x.style.display === "none") {
				x.style.display = "block";
			} else {
				x.style.display = "none";
			}
		}
	</script>
	<jsp:include page="LawyerScript.jsp"/>
	<jsp:include page="BootstrapJs.jsp"/>
</body>
</html>