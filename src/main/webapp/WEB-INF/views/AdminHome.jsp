<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="tags"%>
<%@ page import = "java.util.ResourceBundle" %>
<% ResourceBundle resource = ResourceBundle.getBundle("staticmessages");
   String charge=resource.getString("defaultCharge");
   String roleLawyer=resource.getString("role.lawyer"); %>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">

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

<jsp:include page="BootstrapCss.jsp"/>
<title>Home</title>
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
		<h1>Welcome ${username}</h1>
		<div class="container">
			<div class="row">
				<div class="col">
					<button class="btn btn-primary" onclick = "addForm()">Add Lawyer</button>
					
					<div id="addDiv" style="display: none">
						<div class="card text-bg-light mx-auto my-2">
							<div class="card-header text-center">
								<h3 class="card-title">Add Lawyer</h3>
							</div>
							<div class="card-body">
							
								<form:form class="form-control" action="addLawyer" method="POST" modelAttribute="user">
									<div class="row">
										<div class="col">
											<label class="form-label" for="email"><strong>Email: </strong></label>
											<form:input class="form-control" type="email" 	name="email"		id="addemail" 		path="email" required="true" maxlength="30"/>
											<form:errors class="text-danger" path="email"/><br>
										</div>
										<div class="col">
											<label class="form-label" for="password"><strong>Password: </strong></label>
											<form:password class="form-control"				name="password"		id="addpassword" 	path="password" required="true" maxlength="30"/>
											<form:errors class="text-danger" path="password"/><br>
										</div>
									</div>
									<div class="row">
										<div class="col">
											<label class="form-label" for="username"><strong>User Name: </strong></label>
											<form:input class="form-control"				name="username"		id="addusername" 	path="username" required="true" maxlength="30"/>
											<form:errors class="text-danger" path="username"/><br>
										</div>
										<div class="col">
											<label class="form-label" for="mobile"><strong>Mobile Number: </strong></label>
											<form:input type="number" class="form-control" 	name="mobileNumber"	id="addmobile" 	path="mobileNumber" min="0" required="true"/>
											<form:errors class="text-danger" path="mobileNumber"/><br>
										</div>
									</div>
									<div class="row">
										<div class="col col-sm-4">
											<label class="form-label" for="lawFirmName"><strong>Law Firm Name: </strong></label>
											<form:input class="form-control"				name="lawFirmName"	id="addlawFirmName" path="lawFirmName" maxlength="30"/><br>
										</div>
										<div class="col col-sm-4">
											<label class="form-label" for="expertise"><strong>Expertise: </strong></label>
											<form:input class="form-control" 				name="expertise"	id="addexpertise" path="expertise" maxlength="30"/><br>
										</div>
										<div class="col col-sm-2">
											<label class="form-label" for="experience"><strong>Experience(years): </strong></label>
											<form:input type="number" class="form-control"  name="experience"	id="addexperience" path="experience" min="0"/><br>
										</div>
										<div class="col col-sm-2">
											<label class="form-label" for="charge"><strong>Appointment Cost:</strong></label>
											<form:input class="form-control" type="number" name="charge"		id="addcharge" 	path="charge" step=".01" value="<%= charge %>" min="0" /><br>
										</div>
									</div>
									<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
									<form:input type="hidden" name="role" id="addrole" path="role" value="<%= roleLawyer %>"/>
									<form:errors class="text-danger" path="role"/>
									<form:button class="btn btn-success" value="Submit">Submit</form:button>
								</form:form>
							</div>
						</div>
					</div>

					<div id="editDiv" style="display: none">
						<div class="card text-bg-light mx-auto my-2">
							<div class="card-header text-center">
								<h3 class="card-title">Edit Lawyer</h3>
							</div>
						
							<div class="card-body">
								<form:form class="form-control" action="lawyer" method="POST" modelAttribute="editUser" id="myForm">
									<div class="row">
										<div class="col">
											<label class="form-label" for="email"><strong>Email: </strong></label>
											<input class="form-control-plaintext" type="email" name="displayemail" id="displayemail" readonly />
											<form:errors class="text-danger" path="email" />
										</div>
										<div class="col">
											<label class="form-label" for="username"><strong>User Name: </strong></label>
											<form:input class="form-control" name="username" id="username" path="username" required="true" maxlength="30"/>
											<form:errors class="text-danger" path="username" />
										</div>
									</div>
									<div class="row">
										<div class="col">
											<label class="form-label" for="mobile"><strong>Mobile Number: </strong></label>
											<form:input class="form-control" type="number" name="mobileNumber" id="mobile" path="mobileNumber" required="true" min="0"/>
											<form:errors class="text-danger" path="mobileNumber" />
										</div>
										<div class="col">
											<label class="form-label" for="expertise"><strong>Expertise: </strong></label>
											<form:input class="form-control" name="expertise" id="expertise" path="expertise" maxlength="30"/>
										</div>
									</div>
									<div class="row">
										<div class="col col-sm-4">
											<label class="form-label" for="experience"><strong>Experience (years): </strong></label>
											<form:input class="form-control" type="number" name="experience" id="experience" path="experience" min="0"/>
										</div>
										<div class="col col-sm-4">
											<label class="form-label" for="lawFirmName"><strong>Law Firm Name: </strong></label>
											<form:input class="form-control" name="lawFirmName" id="lawFirmName" path="lawFirmName" maxlength="30"/>
										</div>
										<div class="col col-sm-4">
											<label class="form-label" for="charge"><strong>Charge per Appointment: </strong></label>
											<form:input class="form-control" type="number" name="charge"		id="editcharge" 	path="charge" step=".01" value="<%= charge %>" min="0" /><br>
										</div>
									</div>
									<form:input type="hidden" name="email" id="email" path="email" />
									<form:input type="hidden" name="role" id="role" path="role" />
									<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
									<form:button class="btn btn-success" value="Submit" >Submit</form:button>
								</form:form>
							</div>
						</div>
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
								<p>Email: ${lawyer.getEmail()}</p>
								<p>Mobile Number: ${lawyer.getMobileNumber()}</p>
								<p>Experience: ${lawyer.getExperience()}</p>
								<p>Expertise: ${lawyer.getExpertise()}</p>
								<p>Law Firm: ${lawyer.getLawFirmName()}</p>
								<button onclick="toggleForm('${lawyer.getEmail()}', '${lawyer.getUsername()}',
								 '${lawyer.getRole()}', '${lawyer.getMobileNumber()}', '${lawyer.getExperience()}', 
								 '${lawyer.getExpertise()}', '${lawyer.getLawFirmName()}', '${lawyer.getId()}', '${lawyer.getCharge()}')" class="btn btn-warning">
								 	Edit lawyer
								 </button>
								<a href="lawyer/${lawyer.getId()}" class="btn btn-danger">Delete Lawyer</a>
							</div>

						</div>
				</div>
			</c:forEach>
		</div>
		</div>

	</div>
	<script src="<c:url value = "/resources/js/adminscript.js"/>"></script>
	<jsp:include page="BootstrapJs.jsp"/>
</body>
</html>