<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>


<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<jsp:include page="BootstrapCss.jsp"/>
<title>Home</title>

<style type="text/css">
	.status-circle {
	  width: 15px;
	  height: 15px;
	  border-radius: 50%;
	  background-color: grey;
	  border: 2px solid white;
	  bottom: 0;
	  right: 0;
	  position: absolute;
	}
	.icon-container {
	  position: relative;
	}
</style>

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
		<h1>Welcome ${userName}</h1><br>
		<div class="container">
			<div class="row">
				<div class="col col-sm-8">
					<div class="container">
						<div class="row text-center border border-primary rounded-3 my-2">
							<div class="col col-sm-4">
								<form action="/project/user/searchByExpertise" method="POST">
									<div class="input-group my-2">
									  <input class="form-control" placeholder="Expertise" aria-label="Expertise" aria-describedby="search"
									  	name="searchField" id="searchField" required max="30">
									  <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
									  <button class="btn btn-outline-success" type="submit" id="search">Search</button>
									</div>
								</form>								
							</div>
							<div class="col col-sm-4">
								<form action="/project/user/searchByLawyername" method="POST">
									<div class="input-group my-2">
									  <input class="form-control" placeholder="Lawyer name" aria-label="Lawyer name" aria-describedby="search"
									  	name="searchField" id="searchField" required max="30">
									  <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
									  <button class="btn btn-outline-success" type="submit" id="search">Search</button>
									</div>
								</form>	
							</div>
						</div>
						<div class="row text-center">
							<div class="col">
								<h3>List of Lawyers</h3>
							</div>
						</div>
						<div class="row">
						
							<c:forEach var="lawyer" items="${allLawyer}">
								<div class="col col-sm-4">
									<div class="card text-bg-light mx-auto my-2">
									
										<div class="card-header text-center">
											<h2 class="icon-container">
												${lawyer.getUsername()}
												<c:choose>
													<c:when test="${lawyer.isActive()}">
														<div class='status-circle' style="background-color: green"></div>
													</c:when>
													<c:otherwise>
														<div class='status-circle' style="background-color: red"></div>
													</c:otherwise>
												</c:choose>
											</h2>
										</div>
										
										<div class="card-body">
											<p>Experience: ${lawyer.getExperience()}</p>
											<p>Expertise: ${lawyer.getExpertise()}</p>
											<p>Law Firm: ${lawyer.getLawFirmName()}</p>
											<p>Charge(Rs): ${lawyer.getCharge()}</p>
											<div class="text-center">
												<button onclick="toggleForm('${lawyer.getEmail()}', '${lawyer.getCharge()}')"
													class="btn btn-primary">Book Appointment</button>
											</div>
										</div>
									</div>
								</div>
							</c:forEach>
							
						</div>
					</div>
				</div>
				
				<div class = "col col-sm-4" id = "appointmentForm" style="display: none">					
					<!-- Booking form -->

					<div class="card text-bg-light mx-2 my-2">
						<div class="card-header text-center">
							<h3>Book Appointment</h3>
						</div>
						<div class="card-body">
							<form:form action="/project/user/bookingForm" method='POST' modelAttribute="booking">
								<div class="mb-3">
									<label for="appointmentDate" class="form-label">Date</label> 
									<form:input type="date" id="appointmentDate" name="appointmentDate" 
												class="form-control" path="appointmentDate" required="true" />
									<form:errors path="appointmentDate" />
								</div>
								<div class="mb-3">
									<label for="appointmentTime" class="form-label">Slot Time</label> 
									<form:select class="form-select" id="appointmentTime" name="appointmentTime"
												path="appointmentTime" required="true">
										<%for (int i = 9; i <= 19; i++) {%>
											<form:option value="<%=i%>"><%=i%>:00</form:option>
										<%}%>
									</form:select>
									<form:errors path="appointmentTime" />
								</div>
								
								<div class="mb-3">								
									<label for="subject" class="form-label">Subject</label>
									<form:input type="text" class="form-control" id="subject" name="subject"
										path="subject" required="true" maxlength="30"/>
									<form:errors path="subject" />
								</div>

								<div class="mb-3">								
									<label for="setLawyerCharge" class="form-label">Charge(Rs): </label>
									<input type="number" class="form-control-plaintext" id="setLawyerCharge" readonly>
								</div>

								<input type="hidden" id="setLawyer" name="lawyerEmail">
								<input type="hidden" id="setClient" name="userEmail"
									value="<sec:authentication property='principal.username' />">
								<button type="submit" class="btn btn-success">Book</button>
							</form:form>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

	
	<script src="<c:url value="/resources/js/userhome.js"/>"></script>
	<script src="<c:url value="/resources/js/userscript.js"/>"></script>
	<jsp:include page="BootstrapJs.jsp"/>
</body>
</html>