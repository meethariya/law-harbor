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
<title>User Login</title>
<jsp:include page="BootstrapCss.jsp" />
</head>
<body>
	<div class="fluid-container mx-2 my-2">
		<c:choose>	
			<c:when test="${param.error != null}">
				<div class="alert alert-danger alert-dismissible show w-50 mx-auto" role="alert">
					Invalid Credentials
					<button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
				</div>
			</c:when>
		</c:choose>
		<c:choose>	
			<c:when test="${param.logout != null}">
				<div class="alert alert-danger alert-dismissible show w-50 mx-auto" role="alert">
					Logged Out
					<button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
				</div>
			</c:when>
		</c:choose>
		<c:choose>	
			<c:when test="${errMessage!=null && errMessage.length()!=0}">
				<div class="alert alert-warning alert-dismissible show w-50 mx-auto" role="alert">
					${errMessage}
					<button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
				</div>
			</c:when>
		</c:choose>
		<div class="card text-bg-light w-50 mx-auto">
		  <div class="card-header text-center">
		    <h1>Login</h1>
		  </div>
		  <div class="card-body">
			<form:form action="loginForm" method="POST" modelAttribute="user">
				<div class="form-floating mb-3">
					<form:input type="email" class="form-control" name="email"
						id="email" placeholder="name@example.com" path="email"
						required="true" maxlength="30"/>
					<label for="email">Email </label>
					<form:errors class="text-danger" path="email" />
				</div>
				
				<div class="form-floating mb-3">
					<form:password class="form-control" name="password" id="password"
						placeholder="password" path="password" required="true" maxlength="30"/>
					<label for="password">Password </label>
					<form:errors class="text-danger" path="password" />
				</div>
				<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
				<div class="mb-3">
					<a href="register" class="link-info">Not yet registered? SignUp</a><br>
				</div>
	
				<form:button value="Submit" class="btn btn-success">Submit</form:button>
			</form:form>
		  </div>
		</div>

		<jsp:include page="BootstrapJs.jsp" />
	</div>
</body>
</html>