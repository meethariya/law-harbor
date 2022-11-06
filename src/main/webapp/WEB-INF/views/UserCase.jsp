<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="ISO-8859-1">
<jsp:include page="BootstrapCss.jsp"/>
<title>Case Records</title>
</head>
<body>
	<jsp:include page="Navbar.jsp"/>
	<div class = "fluid-container mx-2 my-2">
		<div class = "container">
			<div class="row text-center">
				<div class = "col">	
					<h3>Case Records</h3>
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
	<jsp:include page="UserScript.jsp"/>
	<jsp:include page="BootstrapJs.jsp"/>
</body>
</html>