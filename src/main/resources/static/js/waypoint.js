let numWays = 0;
const LIMIT = 4;
let oldDel = null;
let delway = $("[id='delete-waypoint']");

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
"                  <input id=\"way\" name=\"way\" type=\"text\">\n" + 
"					<i class=\"fas fa-minus\" id=\"delete-waypoint\" onclick='deleteWaypoint(this)'></i>"+
"                </div>\n" + 
"                <div class=\"duration-field\">\n" + 
"                  <label for=\"duration\">Duration</label>\n" + 
"                  <input id=\"duration\" name=\"duration\" type=\"time\">\n" + 
"                </div>\n" + 
"              </div>\n" + 
"            </li>");
}


function deleteWaypoint(elem){
	console.log($(elem));
	// console.log($($(elem)[0].parentNode));
	$($(elem)[0].parentNode)[0].parentNode.remove();
	numWays--;
}

delway.click(() => {
	console.log("delete");
});


$(document).ready(() => {
	$("#add-waypoint").click(() => {
		addWaypoint();
	});
});