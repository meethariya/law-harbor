<%@page import="java.time.LocalDate"%>
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
<title>Case Records</title>
</head>
<body>
	<jsp:include page="Navbar.jsp"/>
	<div class="fluid-container mx-2 my-2">
		<c:choose>	
			<c:when test="${errMessage!=null && errMessage.length()!=0}">
				<div class="alert alert-warning alert-dismissible show w-50 mx-auto" role="alert">
					${errMessage}
					<button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
				</div>
			</c:when>
		</c:choose>
		<div class="container">
		
			<div class="row text-center">
				<div class="col">
					<h3>Case Records</h3>
				</div>
			</div>
			<div class="row border border-primary rounded-3 my-2">
				<div class="col col-sm-1 my-2">
				</div>
				<div class="col col-sm-3 my-2">
					<!-- Example single danger button -->
					<div class="btn-group">
					  <button type="button" class="btn btn-success dropdown-toggle" data-bs-toggle="dropdown" aria-expanded="false">
					    Get by year
					  </button>
					  <ul class="dropdown-menu">
					  	<%for (int i = LocalDate.now().getYear(); i > LocalDate.now().getYear()-3; i--) {%>
						    <li><a class="dropdown-item" href="/project/lawyer/caseRecordByYear/<%=i%>"><%=i%></a></li>
						<%}%>
					    <li><hr class="dropdown-divider"></li>
					    <li><a class="dropdown-item" href="/project/lawyer/caseRecordByYear/earlier">Earlier</a></li>
					  </ul>
					</div>
				</div>
				<div class="col col-sm-3 my-2">
					<form action="/project/lawyer/caseRecordByUsername" method="POST">
						<div class="input-group">
						  <input type="text" class="form-control" placeholder="User name" required
						  	aria-label="username" aria-describedby="search" max="30" name="username">
						  <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
						  <button class="btn btn-outline-success" type="submit" id="search" value="Submit">Search</button>
						</div>
					</form>
				</div>
			</div>
			<div class="row">
				<div class="col">
					<button class="btn btn-primary" onclick="addFormToggler()">Create Case Record</button>
					<div id="addDiv" style="display: none">
						<div class="card text-bg-light w-75 mx-auto my-2">
							<div class="card-body">
								<form:form class="form-control" action="/project/lawyer/caseRecord" method="POST" modelAttribute="case">
									<label class="form-label" for="userEmail"><strong>User Email: </strong></label>
									<form:input type="email" name="userEmail" id="userEmail" placeholder="client@mail.com"
										path="userEmail" class="form-control" required="true" maxlength="30"/>
									<form:errors class="text-danger" path="userEmail" />
									<br>
				
									<label class="form-label" for="eventDetail"><strong>Event Detail: </strong></label>
									<form:textarea name="eventDetail" id="eventDetail" placeholder="Brief about the details of event happened"
										path="eventDetail" class="form-control" required="true" maxlength="255"/>
									<form:errors class="text-danger" path="eventDetail" />
									<br>
				
									<label class="form-label" for="actionTaken"><strong>Action Taken: </strong></label>
									<form:textarea name="actionTaken" id="actionTaken" placeholder="Brief about the details of action being taken"
										class="form-control" path="actionTaken" required="true" maxlength="255"/>
									<form:errors class="text-danger" path="actionTaken" />
									<br>
									<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
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
						<div class="card text-bg-light w-75 mx-auto my-2">
							<div class="card-body">
								<form:form class="form-control" method="POST" modelAttribute="case" id="editForm">
									<label class="form-label" for="userEmail"><strong>User Email: </strong></label>
									<input class="form-control" type="email" name="userEmail" id="editUserEmail" disabled />
									<form:input type="hidden" id="hiddenEditEmail" path="userEmail" />
									<form:errors class="text-danger" path="userEmail" />
									<br>
				
									<label class="form-label" for="eventDetail"><strong>Event Detail: </strong></label>
									<form:textarea class="form-control" name="eventDetail" id="editEventDetail"
										path="eventDetail" required="true" maxlength="255"/>
									<form:errors class="text-danger" path="eventDetail" />
									<br>
				
									<label class="form-label" for="actionTaken"><strong>Action Taken: </strong></label>
									<form:textarea class="form-control" name="actionTaken" id="editActionTaken"
										path="actionTaken" required="true" maxlength="255"/>
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
		
									<a href="/project/lawyer/caseRecord/${caseRecord.getCaseRecordId()}" class="btn btn-danger">
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
	<script src="<c:url value="/resources/js/lawyercase.js"/>"></script>
	<script src="<c:url value="/resources/js/lawyerscript.js"/>"></script>
	<jsp:include page="BootstrapJs.jsp"/>
</body>
</html>