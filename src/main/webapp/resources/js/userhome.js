function toggleForm(email) {
	var x = document.getElementById("appointmentForm");
	document.getElementById("setLawyer").value = email;
	if (x.style.display === "none") {
		x.style.display = "block";
	} else {
		x.style.display = "none";
	}
	var d = new Date();
	var s = d.getFullYear() + '-' +
		('0' + (d.getMonth() + 1)).slice(-2) + '-' +
		('0' + d.getDate()).slice(-2);

	document.getElementById('appointmentDate').setAttribute('min', s);
}