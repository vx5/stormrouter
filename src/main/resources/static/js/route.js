/*
Parse the input to dict: 
	start: starting point
	date: date of starting point
	destination: destination point
	points: array of waypoints
*/
function getFormInputs(){

	const form = $(".itinerary-form")[0];
	const inputs = $(form).serializeArray();
	// console.assert(inputs.length >= 1, {inputs: inputs});
	// console.log(inputs);
	const allGeocoderRes = $(".geocoder").find("input");
	const start = allGeocoderRes[0].value;
	const date = inputs[0].value;
	const destination = allGeocoderRes[allGeocoderRes.length - 1].value;
	let points = [];
	for(let i = 1; i < inputs.length; i++){
		points.push({point: allGeocoderRes[i].value, duration: inputs[i].value});
	}
	return {start: start, date: date, destination: destination, points: points};
}

$(document).ready(() => {
	$("#route").click(() => {
		const inputs = getFormInputs();

	});
});