<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="tags"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="ISO-8859-1">
<title>Home</title>
</head>
<body>
	<h1>Welcome ${username}</h1>
	<h2>${errMessage}</h2><br>
	
	<button onclick = "addForm()">Add Lawyer</button>
	
	<div id="addDiv" style="display: none">
	<form:form action="addLawyer" method="POST" modelAttribute="user">
		<label for="email">*Email: </label>
		<form:input type="email" 	name="email"		id="addemail" 		path="email" required="true"/>
		<form:errors path="email"/><br>
		
		<label for="password">*Password: </label>
		<form:password 				name="password"		id="addpassword" 	path="password" required="true"/>
		<form:errors path="password"/><br>
		
		<label for="username">*User Name: </label>
		<form:input 				name="username"		id="addusername" 	path="username" required="true"/>
		<form:errors path="username"/><br>
		
		<label for="mobile">*Mobile Number: </label>
		<form:input type="number" 	name="mobileNumber"	id="addmobile" 	path="mobileNumber" min="0" required="true"/>
		<form:errors path="mobileNumber"/><br>
		
		<form:input type="hidden" name="role" id="addrole" path="role" value="lawyer"/>
		<form:errors path="role"/><br>
		
		<label for="experience">Experience(years): </label>
		<form:input type="number" name="experience"	id="addexperience" path="experience" min="0"/><br>
		
		<label for="expertise">Expertise: </label>
		<form:input  			name="expertise"	id="addexpertise" path="expertise"/><br>
		
		<label for="lawFirmName">Law Firm Name: </label>
		<form:input 			name="lawFirmName"	id="addlawFirmName" path="lawFirmName"/><br>
		
		<form:button value="Submit">Submit</form:button>
	</form:form>
	</div>
	
	<h3>List of Lawyers</h3>
	<c:forEach var="lawyer" items="${allLawyer}">
		<p>${lawyer}</p>
		<button onclick="toggleForm('${lawyer.getEmail()}', '${lawyer.getUsername()}', '${lawyer.getPassword()}',
		 '${lawyer.getRole()}', '${lawyer.getMobileNumber()}', '${lawyer.getExperience()}', 
		 '${lawyer.getExpertise()}', '${lawyer.getLawFirmName()}', '${lawyer.getId()}')">Edit</button>
		<a href="lawyer/${lawyer.getId()}">
			<button>Delete Lawyer</button>
		</a>
	</c:forEach>
	
	<div id="editDiv" style="display: none">
		<form:form action = "lawyer" method="POST" modelAttribute="user" id="myForm">
			<label for="email">Email: </label>
			<input type="email" name="displayemail" id="displayemail" disabled/>
			<form:errors path="email" />
			<br>

			<form:input type="hidden" name="email" id="email" path="email" />
			<form:input type="hidden" name="password" id="password"
				path="password" />
			<form:input type="hidden" name="role" id="role" path="role" />

			<label for="username">User Name: </label>
			<form:input name="username" id="username" path="username" required="true" />
			<form:errors path="username" />
			<br>

			<label for="mobile">Mobile Number: </label>
			<form:input type="number" name="mobileNumber" id="mobile"
				path="mobileNumber" required="true" />
			<form:errors path="mobileNumber" />
			<br>

			<label for="experience">Experience (years): </label>
			<form:input type="number" name="experience" id="experience"
				path="experience" />
			<br>

			<label for="expertise">Expertise: </label>
			<form:input name="expertise" id="expertise" path="expertise" />
			<br>

			<label for="lawFirmName">Law Firm Name: </label>
			<form:input name="lawFirmName" id="lawFirmName" path="lawFirmName" />
			<br>

			<form:button value="Submit">Submit</form:button>
		</form:form>
	</div>

	<a href="logout">Logout</a>

	<script type="text/javascript">
	<!-- Enables the edit form and sets lawyer values and sets form url -->
	function toggleForm(email, username, password, role, mobile, experience, expertise, lawFirmName, id){
		
	    document.getElementById("email").value = email;
	    document.getElementById("displayemail").value = email;
	    document.getElementById("username").value = username;
	    document.getElementById("password").value = password;
	    document.getElementById("role").value = role;
	    document.getElementById("mobile").value = mobile;
	    document.getElementById("experience").value = experience;
	    document.getElementById("expertise").value = expertise;
	    document.getElementById("lawFirmName").value = lawFirmName;

	    divToggler(document.getElementById("editDiv"));
	  }
	
	function addForm(){
	<!-- Displays Add Lawyer Form -->
	    divToggler(document.getElementById("addDiv"));
	}
	
	function divToggler(x){
	<!-- Toggles display property of a div -->
		if (x.style.display === "none") {
		   x.style.display = "block";
		} else {
		   x.style.display = "none";
		}
	}
	</script>
</body>
</html>