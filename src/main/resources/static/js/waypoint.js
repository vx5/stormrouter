let numWays = 0;
let geocoderId = 0;
const LIMIT = 4;

function addWaypoint(){
	if(numWays >= LIMIT) {
		console.log("waypoint limit: " + LIMIT);
		return ;
	}
	numWays++;
// 	$(".waypoints").append("<li>\n" + 
// "              <div class=\"form-row\">\n" + 
// "                <div class=\"location-field\">\n" + 
// "                  <label for=\"way-"+ numWays + "\">Waypoint "+numWays+"</label>\n" + 
// "                  <input id=\"way-"+numWays+"\" name=\"way-"+numWays+"\" type=\"text\">\n" + 
// "					<i class=\"fas fa-minus\" id=\"delete-waypoint\"></i>"+
// "                </div>\n" + 
// "                <div class=\"duration-field\">\n" + 
// "                  <label for=\"duration-"+numWays+"\">Duration</label>\n" + 
// "                  <input id=\"duration-"+numWays+"\" name=\"duration-"+numWays+"\" type=\"time\">\n" + 
// "                </div>\n" + 
// "              </div>\n" + 
// "            </li>");
	$(".waypoints").append("<li>\n" + 
"              <div class=\"form-row\">\n" + 
"                <div class=\"location-field\">\n" + 
"                  <label for=\"way\">Waypoint </label>\n" +
"					<i class=\"fas fa-minus\" id=\"delete-waypoint\" onclick='deleteWaypoint(this)'></i>"+ 
// "                  <input id=\"way\" name=\"way\" type=\"text\" onkeyup='autoCompleteLocation(this, event)' onfocusout='clearSuggests()'>\n" + 
"					<div id='geocoder" + geocoderId + "' class='geocoder'></div>"+
"                </div>\n" + 
"                <div class=\"duration-field\">\n" + 
"                  <label for=\"duration\">Duration</label>\n" + 
"                  <input id=\"duration\" name=\"duration\" type=\"time\">\n" + 
"                </div>\n" + 
"              </div>\n" + 
"            </li>");
	
	document.getElementById('geocoder' + geocoderId).appendChild(new MapboxGeocoder({
		accessToken: mapboxgl.accessToken,
		mapboxgl: mapboxgl
	}).onAdd(map));
	geocoderId++;
}


function deleteWaypoint(elem){
	console.log($(elem));
	$($(elem)[0].parentNode)[0].parentNode.remove();
	numWays--;
}

$(document).ready(() => {
	$("#add-waypoint").click(() => {
		addWaypoint();
	});
});