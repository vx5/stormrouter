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
      if (error || !result.latlng) {
        reject(`Could not find location for input '${input}'.`);
      } else {
        resolve(result.latlng);
      }
    });
  });
}

function getFormInputs() {
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
    durations.push(+duration);
  }

  const promises = [];
  promises.push(forwardGeocode(start));
  promises.push(forwardGeocode(end));
  promises.push(Promise.all(waypointPromises));

  return Promise.all(promises).then(values => {
    const [startPoint, endPoint, waypoints] = values;
    const waypointStops = waypoints.map((waypoint, i) => ({waypoint: waypoint, duration: durations[i]}));
    const unixTime = +new Date(date) / 1000;

    return {start: startPoint, date: unixTime, destination: endPoint, waypoints: waypointStops};
  });
}

/*
Polyline is an array of arrays of length 2.
 */
function displayPath(polyline) {
  map.removeSource('route');
  map.addLayer({
    "id": "route",
    "type": "line",
    "source": {
      "type": "geojson",
      "data": {
        "type": "Feature",
        "properties": {},
        "geometry": {
          "type": "LineString",
          "coordinates": polyline
        }
      }
    },
    "layout": {
      "line-join": "round",
      "line-cap": "round"
    },
    "paint": {
      "line-color": "#888",
      "line-width": 8
    }
  });
}

$(document).ready(() => {
  $('#itinerary-form').submit(event => {
    event.preventDefault();

    getFormInputs().then(postParameters => {
      console.log(postParameters);
      $.post('/stormrouter/parse', {params: JSON.stringify(postParameters)}, responseJSON => {
        const response = JSON.parse(responseJSON);
        console.log(response);
      });
    }).catch(reason => {
      console.log(reason);
      alert('There was an error processing your request.');
    });
  });
});