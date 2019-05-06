$("#clear").click(() => {
	//Clear Route and Weather
	clearRoute();
	//Clear Form Inputs
	$("#itinerary-form")[0].reset();
	//Clear Markers
	for(let key of Object.keys(locationMarkers)){
		clearGeocoderMarker(key);
	}
	//Clear Directions
	$("ol.directions-list").empty();
	//Clear Weather
	curWeather = null;
	//Reset departure time
	departTime = null;
	timeStamps = DEFAULT;
	$("#depart").text("Best Departure Time:");
	$("#slider").prop({
	    min: 0,
	    max: timeStamps.length - 1,
	    value: 2
	});
	$("#slider").next().text("12:00PM");
});