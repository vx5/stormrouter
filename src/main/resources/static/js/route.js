/*
Parse the input to dict: 
	start: starting point
	date: date of starting point
	end: end point
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

/*function validateForm() {
  const form = document.forms['itinerary-form'];
  const inputs = $(form).serializeArray();

  for (let i = 0; i < inputs.length; i++) {
    const input = inputs[i];
    if (!input.value) {
      console.log(input);
      alert(`Input '${input.name}' was empty.`);
      return false;
    }
  }
  return true;
}*/

async function getFormInputs() {
  const form = document.forms['itinerary-form'];
  const start = form['start'].value;
  const end = form['end'].value;
  const date = form['date'].value;

  const waypointPromises = [];
  const durations = [];

  const inputs = $(form).serializeArray();
  for (let i = 2; i < inputs.length - 1;) { // begin at first waypoint input and end before destination input
    const waypointName = inputs[i++].value;
    const duration = inputs[i++].value;
    waypointPromises.push(forwardGeocode(waypointName));
    durations.push(duration);
    //points.push({point: $geocoders[i].value, duration: inputs[i].value});
  }

  const promises = [];
  promises.push(forwardGeocode(start));
  promises.push(forwardGeocode(end));
  promises.push(Promise.all(waypointPromises));

  return Promise.all(promises).then(values => {
    [startPoint, endPoint, waypoints] = values;
    const points = waypoints.map((point, i) => ({lat: point[0], lon: point[1], duration: durations[i]}));

    console.log(values);
    return {start: startPoint, date: date, destination: endPoint, points: points};
  });
}

$(document).ready(() => {
  $('#itinerary-form').submit(event => {
    event.preventDefault();

    getFormInputs().then(postParameters => {
      console.log(postParameters);
    }).catch(reason => console.log(reason));
  });
});