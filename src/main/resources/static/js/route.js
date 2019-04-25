function getFormInputs(){
	const form = $(".itinerary-form")[0];
	const inputs = $(form).serializeArray();
	//assert that length >= 3.
	console.assert(inputs.length >= 3, {inputs: inputs});
	const start = inputs[0];
	const date = inputs[1];
	const destination = inputs[inputs.length - 1];
	let points = [];
	for(let i = 2; i < inputs.length - 1; i+=2){
		points.push({point: inputs[i].value, duration: inputs[i+1].value});
	}
	console.log(points);
	return {start: start, date: date, destination: destination, points: points};
}

