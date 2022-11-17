var d = new Date();
const weekday = ["Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"];
var s = weekday[d.getDay()] +' '+('0' + d.getDate()).slice(-2)+ '-' +
		('0' + (d.getMonth() + 1)).slice(-2) + '-' +
		d.getFullYear() + " " +
		d.getHours()+ ":" +d.getMinutes();
document.getElementById("currentTime").innerHTML = s;