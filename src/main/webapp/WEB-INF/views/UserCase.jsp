<%@page import="java.time.LocalDate"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

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
	<div class = "fluid-container mx-2 my-2">
		<c:choose>	
			<c:when test="${errMessage!=null && errMessage.length()!=0}">
				<div class="alert alert-warning alert-dismissible show w-50 mx-auto" role="alert">
					${errMessage}
					<button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
				</div>
			</c:when>
		</c:choose>
		<div class = "container">
			<div class="row text-center">
				<div class = "col">	
					<h3>Case Records</h3>
				</div>
			</div>
			<div class="row border border-primary rounded-3 my-2">
				<div class="col col-sm-3">
					<!-- Example single danger button -->
					<div class="btn-group my-2">
					  <button type="button" class="btn btn-info dropdown-toggle" data-bs-toggle="dropdown" aria-expanded="false">
					    Get by year
					  </button>
					  <ul class="dropdown-menu">
					  	<%for (int i = LocalDate.now().getYear(); i > LocalDate.now().getYear()-3; i--) {%>
						    <li><a class="dropdown-item" href="/project/user/caseRecord/<%=i%>"><%=i%></a></li>
						<%}%>
					    <li><hr class="dropdown-divider"></li>
					    <li><a class="dropdown-item" href="/project/user/caseRecord/earlier">Earlier</a></li>
					  </ul>
					</div>
				</div>
				<div class="col col-sm-4">
					<form action="/project/user/caseRecordByLawyername" method="POST">
						<div class="input-group my-2">
						  <input class="form-control" placeholder="Lawyer name" aria-label="Lawyer name" aria-describedby="search"
						  	name="searchField" id="searchField" required max="30">
						  <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
						  <button class="btn btn-outline-success" type="submit" id="search">Search</button>
						</div>
					</form>	
				</div>
			</div>
			<div class="row">
				<c:forEach var="caseRecord" items="${allCase}">
					<div class="col col-sm-4">
						<div class="card text-bg-light mx-auto my-2">

							<div class="card-header text-center">
								<h2 class="card-title">IssuedBy: ${caseRecord.getIssuedBy().getUsername()}</h2>
							</div>

							<div class="card-body">
								<p class="card-text">Event Detail: ${caseRecord.getEventDetail()}</p>
								<p class="card-text">Action Taken: ${caseRecord.getActionTaken()}</p>
							</div>

							<div class="card-footer text-center text-muted">
								<p class="card-text">Date: ${caseRecord.getDate().toString().split(" ")[0]}</p>
							</div>

						</div>
					</div>
				</c:forEach>
			</div>
		</div>
		<br>
	</div>
	<script src="<c:url value="/resources/js/userscript.js"/>"></script>
	<jsp:include page="BootstrapJs.jsp"/>
</body>
</html>