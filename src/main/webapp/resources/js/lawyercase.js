function addFormToggler() {
	//Displays Add Case Form 
	divToggler(document.getElementById("addDiv"));
}
function editFormToggler(email, eventDetail, actionTaken, id) {
	//Sets values of edit form and Displays it 
	var x = divToggler(document.getElementById("editDiv"));

	document.getElementById("editUserEmail").value = email;
	document.getElementById("hiddenEditEmail").value = email;
	document.getElementById("editEventDetail").value = eventDetail;
	document.getElementById("editActionTaken").value = actionTaken;
	document.getElementById("editForm").action = "caseRecord/" + id;

}
function divToggler(x) {
	//Toggles display property of a div 
	if (x.style.display === "none") {
		x.style.display = "block";
	} else {
		x.style.display = "none";
	}
}