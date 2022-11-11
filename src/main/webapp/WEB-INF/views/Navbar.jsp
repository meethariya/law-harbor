<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<nav class="navbar navbar-expand-lg" style="background-color: #e3f2fd;">
	<div class="container-fluid">
		<a class="navbar-brand">Law Harbor</a>
		<button class="navbar-toggler" type="button" data-bs-toggle="collapse"
			data-bs-target="#navbarSupportedContent"
			aria-controls="navbarSupportedContent" aria-expanded="false"
			aria-label="Toggle navigation">
			<span class="navbar-toggler-icon"></span>
		</button>
		<div class="collapse navbar-collapse" id="navbarSupportedContent">
			<ul class="navbar-nav me-auto mb-2 mb-lg-0">
				<li class="nav-item"><a class="nav-link" id="homeTag" href="#">Home</a></li>
				<li class="nav-item"><a class="nav-link" id="bookingTag">Bookings</a></li>
				<li class="nav-item"><a class="nav-link" id="caseRecordTag">CaseRecord</a></li>
			</ul>
			<span class="navbar-text"> 
				<button class="btn btn-danger text-light" onClick="logoutFunction()">Logout</button>
			</span>
			<form method="POST" action="/project/logout" id="logoutForm" style="display: none">
				<input type="hidden" name="${_csrf.parameterName}"
					value="${_csrf.token}" /> 
				<span class="navbar-text">
					<button class="btn btn-danger text-light" id="logoutTag"
						type="submit" value="Sign Out">Logout</button>
				</span>
			</form>
		</div>
	</div>
</nav>

<script>
function logoutFunction(){	
	fetch("http://localhost:8081/project/logoutUser").then(function(response) {
		  return response.status;
		}).then(function(data) {
		  if(data==200){
			  document.getElementById("logoutForm").submit()
		  }
		});
}
</script>