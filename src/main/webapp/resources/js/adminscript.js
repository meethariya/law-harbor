function toggleForm(email, username, role, mobile, experience, expertise, lawFirmName, id, charge) {
	//Enables the edit form and sets lawyer values and sets form url

	document.getElementById("email").value = email;
	document.getElementById("displayemail").value = email;
	document.getElementById("username").value = username;
	document.getElementById("role").value = role;
	document.getElementById("mobile").value = mobile;
	document.getElementById("experience").value = experience;
	document.getElementById("expertise").value = expertise;
	document.getElementById("lawFirmName").value = lawFirmName;
	document.getElementById("editcharge").value = charge;

	divToggler(document.getElementById("editDiv"));
}

function addForm() {
	// Displays Add Lawyer Form
	divToggler(document.getElementById("addDiv"));
}

function divToggler(x) {
	// Toggles display property of a div
	if (x.style.display === "none") {
		x.style.display = "block";
	} else {
		x.style.display = "none";
	}
}

document.getElementById("bookingTag").style.display = "none";
document.getElementById("homeTag").style.display = "none";
document.getElementById("caseRecordTag").style.display = "none";