var d = new Date();
var s = d.getFullYear() + '-' +
	('0' + (d.getMonth() + 1)).slice(-2) + '-' +
	('0' + d.getDate()).slice(-2);
document.getElementById("currentTime").innerHTML = d;