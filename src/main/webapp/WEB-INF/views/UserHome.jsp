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
<title>Home</title>
</head>
<body>
	<jsp:include page="Navbar.jsp"/>
	
	<div class = "fluid-container mx-2 my-2">		
		<div class="alert alert-warning alert-dismissible show w-50 mx-auto" role="alert">
			${bookingStatus}
			<button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
		</div>
		<h1>Welcome ${userName}</h1>
		<div class="container">
			<div class="row">
				<div class="col col-sm-8">
					<div class="container">
						<div class="row text-center">
							<div class="col">
								<div class="container">
									<form class="row g-3" action="searchByExpertise" method="POST">
										<div class="col-auto">
											<h3 class="form-label">Search By Expertise</h3>
										</div>
										<div class="col-auto">
											<label for="searchField" class="visually-hidden">Expertise</label>
											<input class="form-control" name="searchField" id="searchField" placeholder="Expertise" required>
										</div>
										<div class="col-auto">
											<button type="submit" class="btn btn-success mb-3">Search</button>
										</div>
									</form>
								</div>
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
											<h2>${lawyer.getUsername()}</h2>
										</div>
										
										<div class="card-body">
											<h5>Experience: ${lawyer.getExperience()}</h5>
											<h5>Expertise: ${lawyer.getExpertise()}</h5>
											<h5>Law Firm: ${lawyer.getLawFirmName()}</h5>
											<div class="text-center">
												<button onclick="toggleForm('${lawyer.getEmail()}')"
													class="btn btn-primary">Book Appointment</button>
											</div>
										</div>
										
										<div class="card-footer text-center text-muted">
											<c:choose>
												<c:when test="${lawyer.isActive() }">
													<h4 class="text-success">Online</h4>
												</c:when>
												<c:otherwise>
													<h4 class="text-danger">Offline</h4>
												</c:otherwise>
											</c:choose>
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
							<form:form action="bookingForm" method='POST' modelAttribute="booking">
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

								<input type="hidden" id="setLawyer" name="lawyerEmail">
								<input type="hidden" id="setClient" name="userEmail"
									value="${ sessionScope.userEmail }">
								<button type="submit" class="btn btn-success">Book</button>
							</form:form>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<script type="text/javascript">
	<!-- Opens booking form on lawyer click and sets lawyer email-->
	function toggleForm(email){
	    var x = document.getElementById("appointmentForm");
	    document.getElementById("setLawyer").value = email;
	    if (x.style.display === "none") {
	      x.style.display = "block";
	    } else {
	      x.style.display = "none";
	    }
	    <!-- Set Min date as today -->
	    var d = new Date();
	    var s = d.getFullYear() + '-' + 
        ('0' + (d.getMonth()+1)).slice(-2) + '-' +
        ('0' + d.getDate()).slice(-2);
	    
	    document.getElementById('appointmentDate').setAttribute('min', s);
	  }
	</script>
	<jsp:include page="UserScript.jsp"/>
	<jsp:include page="BootstrapJs.jsp"/>
</body>
</html>