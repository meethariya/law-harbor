<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="tags"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import = "java.util.ResourceBundle" %>
<% ResourceBundle resource = ResourceBundle.getBundle("staticmessages");
   String charge=resource.getString("defaultCharge");
   String lawyerRole = resource.getString("role.lawyer");
   String adminRole = resource.getString("role.admin");
   String userRole = resource.getString("role.user");%>
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
		<c:choose>	
			<c:when test="${errMessage!=null && errMessage.length()!=0}">
				<div class="alert alert-warning alert-dismissible show w-50 mx-auto" role="alert">
					${errMessage}
					<button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
				</div>
			</c:when>
		</c:choose>
		
		<div class="card text-bg-light mx-auto">
		  <div class="card-header text-center">
		    <h1>Registration Form</h1>
		  </div>
		  <div class="card-body">
			<form:form class="form-control" action="register" method="POST" modelAttribute="user">
				<div class="row">
					<div class="col">
						<label class="form-label" for="email"><strong>Email: </strong></label>
						<form:input type="email" class="form-control" 	name="email"		id="email" 		path="email" required="true" maxlength="30" placeholder="john.23@mail.com"/>
						<form:errors class="text-danger" path="email"/>
					</div>
					<div class="col">
						<label class="form-label" for="password"><strong>Password: </strong></label>
						<form:password class="form-control"				name="password"		id="password" 	path="password" required="true" maxlength="30"/>
						<div id="passwordHelpBlock" class="form-text">
						  Your password must be 8-20 characters long, contain upper case and lower case letters, numbers and special character.
						</div>
						<form:errors class="text-danger" path="password"/>
					</div>
				</div>
				<div class="row">
					<div class="col col-sm-5">
						<label class="form-label" for="username"><strong>User Name: </strong></label>
						<form:input class="form-control"				name="username"		id="username" 	path="username" required="true" maxlength="30" placeholder="Jhon Singh"/>
						<form:errors class="text-danger" path="username"/>
					</div>
					<div class="col col-sm-4">
						<label class="form-label" for="mobile"><strong>Mobile Number: </strong></label>
						<form:input type="number" class="form-control"	name="mobileNumber"	id="mobile" 	path="mobileNumber" min="0" required="true" />
						<form:errors class="text-danger" path="mobileNumber"/>
					</div>
					<div class="col col-sm-3">
						<label class="form-label" for="role"><strong>Role: </strong></label>
						<form:select class="form-select"				name="role"			id="role" 		path="role" required="true" >  			
							<option value = "<%= userRole %>" > <%= userRole.split("_")[1] %> </option>  
							<option value = "<%= lawyerRole %>" > <%= lawyerRole.split("_")[1] %> </option>  
							<!-- <option value = "<%= adminRole %>" > <%= adminRole.split("_")[1] %> </option>   -->
						</form:select>  
						<form:errors class="text-danger" path="role"/>
					</div>
				</div>
				<!-- Additional lawyer information -->
				<div id="myDiv" style="display: none">
					<div class="row">
						<div class="col col-sm-4">
							<label class="form-label" for="expertise"><strong>Expertise: </strong></label>
							<form:input class="form-control"			   name="expertise"		id="expertise" 	path="expertise" maxlength="30" placeholder="Crime"/>
						</div>
						<div class="col col-sm-4">
							<label class="form-label" for="lawFirmName"><strong>Law Firm Name: </strong></label>
							<form:input class="form-control"			   name="lawFirmName"	id="lawFirmName" path="lawFirmName" maxlength="30" placeholder="Suits Agency"/>
						</div>
						<div class="col col-sm-2">
							<label class="form-label" for="experience"><strong>Experience(years): </strong></label>
							<form:input class="form-control" type="number" name="experience"	id="experience" path="experience" min="0"/>
						</div>
						<div class="col col-sm-2">
							<label class="form-label" for="charge"><strong>Appointment Cost: </strong></label>
							<form:input class="form-control" type="number" name="charge"		id="charge" 	path="charge" step=".01" value="<%= charge %>"/>
						</div>
					</div>
				</div>
				<br>
				<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
				<form:button class="btn btn-success" value="Submit">Submit</form:button>
			</form:form>
		  </div>
		</div>
		
	</div>
	<script src="<c:url value="/resources/js/registration.js"/>"></script>
	<jsp:include page="BootstrapJs.jsp"/>
</body>
</html>