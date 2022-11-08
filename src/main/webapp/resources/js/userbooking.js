function myDivToggler(reportId) {
	//Toggles display property of view report div
	var x = document.getElementById("viewReportDiv" + reportId);
	if (x.style.display === "none") {
		x.style.display = "block";
	} else {
		x.style.display = "none";
	}
}