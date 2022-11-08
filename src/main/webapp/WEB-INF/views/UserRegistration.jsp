<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="tags"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<jsp:include page="BootstrapCss.jsp"/>
<title>User Registration</title>
</head>
<body>
	<div class = "fluid-container mx-2 my-2">
		<div class="alert alert-danger alert-dismissible show w-50 mx-auto" role="alert">
			${errMessage}
			<button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
		</div>
		
		<div class="card text-bg-light w-50 mx-auto">
		  <div class="card-header text-center">
		    <h1>Registration Form</h1>
		  </div>
		  <div class="card-body">
			<form:form class="form-control" action="registerForm" method="POST" modelAttribute="user">
				<label class="form-label" for="email">Email: </label>
				<form:input type="email" class="form-control" 	name="email"		id="email" 		path="email" required="true" maxlength="30"/>
				<form:errors class="text-danger" path="email"/><br>
				
				<label class="form-label" for="password">Password: </label>
				<form:password class="form-control"				name="password"		id="password" 	path="password" required="true" maxlength="30"/>
				<form:errors class="text-danger" path="password"/><br>
				
				<label class="form-label" for="username">User Name: </label>
				<form:input class="form-control"				name="username"		id="username" 	path="username" required="true" maxlength="30"/>
				<form:errors class="text-danger" path="username"/><br>
				
				<label class="form-label" for="mobile">Mobile Number: </label>
				<form:input type="number" class="form-control"	name="mobileNumber"	id="mobile" 	path="mobileNumber" min="0" required="true" />
				<form:errors class="text-danger" path="mobileNumber"/><br>
				
				<label class="form-label" for="role">Role: </label>
				<form:select class="form-select"				name="role"			id="role" 		path="role" required="true" >  
					<c:forEach var="role" items="${roles }">			
						<option value = "${role }" > ${role } </option>  
					</c:forEach>
				</form:select>  
				<form:errors class="text-danger" path="role"/><br>
				
				<!-- Additional lawyer information -->
				<div id="myDiv" style="display: none">
					<label class="form-label" for="experience">Experience(years): </label>
					<form:input class="form-control" type="number" name="experience"	id="experience" path="experience" min="0"/>
					<label class="form-label" for="expertise">Expertise: </label>
					<form:input class="form-control"			   name="expertise"	id="expertise" path="expertise" maxlength="30"/>
					<label class="form-label" for="lawFirmName">Law Firm Name: </label>
					<form:input class="form-control"			   name="lawFirmName"	id="lawFirmName" path="lawFirmName" maxlength="30"/>
				</div>
				<br>
				<form:button class="btn btn-success" value="Submit">Submit</form:button>
			</form:form>
		  </div>
		</div>
		
	</div>
	<script src="<c:url value="/resources/js/registration.js"/>"></script>
	<jsp:include page="BootstrapJs.jsp"/>
</body>
</html>