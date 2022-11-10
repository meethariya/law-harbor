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
				<div class="col col-sm-6">
					<button class="btn btn-primary" onclick = "addForm()">Add Lawyer</button>
					
					<div id="addDiv" style="display: none">
						<div class="card text-bg-light mx-auto my-2">
							<div class="card-header text-center">
								<h3 class="card-title">Add Lawyer</h3>
							</div>
							<div class="card-body">
							
								<form:form class="form-control" action="addLawyer" method="POST" modelAttribute="user">
									<label class="form-label" for="email">*Email: </label>
									<form:input class="form-control" type="email" 	name="email"		id="addemail" 		path="email" required="true" maxlength="30"/>
									<form:errors class="text-danger" path="email"/><br>
									
									<label class="form-label" for="password">*Password: </label>
									<form:password class="form-control"				name="password"		id="addpassword" 	path="password" required="true" maxlength="30"/>
									<form:errors class="text-danger" path="password"/><br>
									
									<label class="form-label" for="username">*User Name: </label>
									<form:input class="form-control"				name="username"		id="addusername" 	path="username" required="true" maxlength="30"/>
									<form:errors class="text-danger" path="username"/><br>
									
									<label class="form-label" for="mobile">*Mobile Number: </label>
									<form:input type="number" class="form-control" 	name="mobileNumber"	id="addmobile" 	path="mobileNumber" min="0" required="true"/>
									<form:errors class="text-danger" path="mobileNumber"/><br>
									
									<form:input type="hidden" name="role" id="addrole" path="role" value="${lawyerRole }"/>
									<form:errors class="text-danger" path="role"/><br>
									
									<label class="form-label" for="experience">Experience(years): </label>
									<form:input type="number" class="form-control"  name="experience"	id="addexperience" path="experience" min="0"/><br>
									
									<label class="form-label" for="expertise">Expertise: </label>
									<form:input class="form-control" 				name="expertise"	id="addexpertise" path="expertise" maxlength="30"/><br>
									
									<label class="form-label" for="lawFirmName">Law Firm Name: </label>
									<form:input class="form-control"				name="lawFirmName"	id="addlawFirmName" path="lawFirmName" maxlength="30"/><br>
									
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
								<form:form class="form-control" action="lawyer" method="POST" modelAttribute="user"
									id="myForm">
									<label class="form-label" for="email">Email: </label>
									<input class="form-control" type="email" name="displayemail" id="displayemail"
										disabled />
									<form:errors class="text-danger" path="email" />
									<br>
		
									<form:input type="hidden" name="email" id="email" path="email" />
									<form:input type="hidden" name="password" id="password"
										path="password" />
									<form:input type="hidden" name="role" id="role" path="role" />
		
									<label class="form-label" for="username">User Name: </label>
									<form:input class="form-control" name="username" id="username" path="username"
										required="true" maxlength="30"/>
									<form:errors class="text-danger" path="username" />
									<br>
		
									<label class="form-label" for="mobile">Mobile Number: </label>
									<form:input class="form-control" type="number" name="mobileNumber" id="mobile"
										path="mobileNumber" required="true" min="0"/>
									<form:errors class="text-danger" path="mobileNumber" />
									<br>
		
									<label class="form-label" for="experience">Experience (years): </label>
									<form:input class="form-control" type="number" name="experience" id="experience"
										path="experience" min="0"/>
									<br>
		
									<label class="form-label" for="expertise">Expertise: </label>
									<form:input class="form-control" name="expertise" id="expertise" path="expertise" maxlength="30"/>
									<br>
		
									<label class="form-label" for="lawFirmName">Law Firm Name: </label>
									<form:input class="form-control" name="lawFirmName" id="lawFirmName"
										path="lawFirmName" maxlength="30"/>
									<br>
		
									<form:button class="btn btn-success" value="Submit" >Submit</form:button>
								</form:form>
							</div>
						</div>
					</div>
				</div>
			</div>
		<div class="row">			
			<h3>List of Lawyers</h3>
			<c:forEach var="lawyer" items="${allLawyer}">
				<div class="col col-sm-4">

						<div class="card text-bg-light mx-auto my-2">

							<div class="card-header text-center">
								<h2>${lawyer.getUsername()}</h2>
							</div>

							<div class="card-body">
								<h5>Email: ${lawyer.getEmail()}</h5>
								<h5>Mobile Number: ${lawyer.getMobileNumber()}</h5>
								<h5>Experience: ${lawyer.getExperience()}</h5>
								<h5>Expertise: ${lawyer.getExpertise()}</h5>
								<h5>Law Firm: ${lawyer.getLawFirmName()}</h5>
								<button onclick="toggleForm('${lawyer.getEmail()}', '${lawyer.getUsername()}', '${lawyer.getPassword()}',
								 '${lawyer.getRole()}', '${lawyer.getMobileNumber()}', '${lawyer.getExperience()}', 
								 '${lawyer.getExpertise()}', '${lawyer.getLawFirmName()}', '${lawyer.getId()}')" class="btn btn-warning">
								 	Edit lawyer
								 </button>
								<a href="lawyer/${lawyer.getId()}" class="btn btn-danger">Delete Lawyer</a>
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
	<script src="<c:url value = "/resources/js/adminscript.js"/>"></script>
	<jsp:include page="BootstrapJs.jsp"/>
</body>
</html>