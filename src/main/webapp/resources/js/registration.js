// Opens additional lawyer information form on role option "lawyer"
var myDiv = document.getElementById("myDiv");
document.getElementById("role").onchange = function() {
	myDiv.style.display = (this.selectedIndex == 1) ? "block" : "none";
}