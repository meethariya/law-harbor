<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="tags"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<jsp:include page="BootstrapCss.jsp"/>
<title>Create Report</title>
</head>
<body>
	<jsp:include page="Navbar.jsp"/>
	<div class="fluid-container mx-2 my-2">
		<div class="alert alert-danger alert-dismissible show w-50 mx-auto" role="alert">
			${errMessage}
			<button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
		</div>
		
		<div class="card text-bg-light w-50 mx-auto">
		  <div class="card-header text-center">
		    <h1 class="cardd-title">Create Report</h1>
		  </div>
		  <div class="card-body">
		  	<h5 class="card-title">${userCaseRecord[0].getUser().getUsername()}</h5>
		  	<h6 class="class-subtitle" id="currentTime"></h6>
			<form:form class="form-control" action="/project/lawyer/report" method="POST" modelAttribute="report">
				<label class="form-label" for="reportDetail">*Report Detail: </label>
				<form:textarea class="form-control" name="reportDetail" id="reportDetail"
					path="reportDetail" required="true" maxlength="255"/>
				<form:errors class="text-danger" path="reportDetail" />
				<br>
			
				<form:input type="hidden" name = "bookingId" path = "bookingId" value="${bookingId }"/>
				
				<label class="form-label">*Case Record: </label>
				<c:forEach var="myCaseRecord" items="${userCaseRecord}">
					<div class="form-check">
						<form:checkbox class="form-check-input" value = "${myCaseRecord.getCaseRecordId()}" path="tempCaseRecord"/>
							<p class="form-check-label">
								Event Detail: ${myCaseRecord.getEventDetail()}
								<br>
								Action Taken: ${myCaseRecord.getActionTaken()}
							</p>
					</div>
				</c:forEach>			
				<form:errors path="tempCaseRecord" />
				<br>
				
				<form:button class="btn btn-success" value="Submit">Submit</form:button>
			</form:form>
		  </div>
		</div>
		
	</div>
	<script src="<c:url value="/resources/js/lawyerreport.js"/>"></script>
	<script src="<c:url value="/resources/js/lawyerscript.js"/>"></script>
	<jsp:include page="BootstrapJs.jsp"/>
</body>
</html>