/*
Parse the input to dict: 
	start: starting point
	date: date of starting point
	destination: destination point
	points: array of waypoints
*/
let geocoder = new L.mapbox.geocoder('mapbox.places');

function forwardGeocode(input) {
	return new Promise((resolve, reject) => {
		geocoder.query({
			query: input,
			limit: 1,
		}, (error, result) => {
			if (error) {
				reject(error);
			} else {
				resolve(result.latlng);
			}
		});
	});
}

async function getFormInputs(){
	const form = $(".itinerary-form")[0];
	const inputs = $(form).serializeArray();
	// console.assert(inputs.length >= 1, {inputs: inputs});
	// console.log(inputs);
	const $allGeocoderRes = $(".geocoder").find("input");
	const start = $allGeocoderRes[0].value;
	let promises = [];
	promises.push(forwardGeocode(start));
	const date = inputs[0].value;
	const destination = $allGeocoderRes[$allGeocoderRes.length - 1].value;
	promises.push(forwardGeocode(destination));
	let points = [];
	for(let i = 1; i < inputs.length; i++){
		const locationName = $allGeocoderRes[i].value;
		promises.push(forwardGeocode(locationName));
		//points.push({point: $allGeocoderRes[i].value, duration: inputs[i].value});
	}

	const coordinates = await Promise.all(promises).then(values => {
		console.log(values);
		return values;
	});
	return coordinates;
	//return {start: start, date: date, destination: destination, points: points};
}

$(document).ready(() => {
	$("#route").click(() => {
		const inputs = getFormInputs();

	});
});