let numWays = 0;
const LIMIT = 4;

function addWaypoint(){
	if(numWays == LIMIT) {
		console.log("waypoint limit: " + LIMIT);
		return ;
	}
	numWays++;
	$(".waypoints").append("<li>\n" + 
"              <div class=\"form-row\">\n" + 
"                <div class=\"location-field\">\n" + 
"                  <label for=\"way-"+ numWays + "\">Waypoint "+numWays+"</label>\n" + 
"                  <input id=\"way-"+numWays+"\" name=\"way-"+numWays+"\" type=\"text\">\n" + 
"					<i class=\"fas fa-minus\" id=\"delete-waypoint\"></i>"+
"                </div>\n" + 
"                <div class=\"duration-field\">\n" + 
"                  <label for=\"duration-"+numWays+"\">Duration</label>\n" + 
"                  <input id=\"duration-"+numWays+"\" name=\"duration-"+numWays+"\" type=\"time\">\n" + 
"                </div>\n" + 
"              </div>\n" + 
"            </li>");
}

function deleteWaypoint(){
	
	numWays--;
}


$(document).ready(() => {
	$("#add-waypoint").click(addWaypoint);
	const delway = $("[id='delete-waypoint']");
	for(let i = 0; i < delway.length; i++){

	}
});