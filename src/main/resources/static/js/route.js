/*
Parse the input to dict: 
	start: starting point
	date: date of starting point
	end: end point
	points: array of waypoints
*/
let geocoder = new L.mapbox.geocoder('mapbox.places');
//let weatherOnMap = [];
let markers = [];
let weatherId = 0;
let pathExists = false;

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

//Clear the current path.
function clearPath() {
  if(!pathExists) return;
  map.removeLayer('route');
  map.removeSource('route');
}

//Clear a layer by ID.
function clearLayer(id) {
  map.removeLayer(id);
  map.removeSource(id);
}

//Add weather markers.
function addWeatherMarker(weatherData) {
  const type = WEATHER_TYPE[weatherData.weatherType];
  const lat = weatherData.lat;
  const lon = weatherData.lon;
  const summary = weatherData.weatherSum;
  const id = 'weather' + weatherId++;
  const img = new Image();
  img.id = id;
  img.src = '/js/images/' + type + '.svg';
  img.width = 30;

  const marker = new mapboxgl.Marker(img)
      .setLngLat([lon, lat])
      .addTo(map);
  markers.push(marker);

  const popup = new mapboxgl.Popup({
    closeButton: false,
    closeOnClick: false
  }).setHTML('<div id="popup">' + summary + '</div>');

  marker.setPopup(popup);

  const $img = $('#' + id);

  $img.mouseover(() => {
    marker.togglePopup();
  });

  $img.mouseleave(() => {
    marker.togglePopup();
  });
}

//Remove all weather markers.
function removeWeatherMarkers() {
  markers.forEach(marker => marker.remove());
  markers = [];
  weatherId = 0;
}

// Parse from [{lat: , lon: },...] to [[lon,lat],...]
function parsePolyline(polyline){
	let res = [];
	for(let i = 0; i < polyline.length; i++){
		res.push([polyline[i].longitude, polyline[i].latitude]);
	}
	return res; 
}

// Display directions
function displayDirections(directions){
	const $directs = $("ol.directions-list");
	$directs.empty();
	for(let i = 0; i < directions.length; i++){
		const length = directions[i].length;
		const instruction = directions[i].instructions;
		$directs.append(
			'<li><div class="instructions">' + instruction + ' for ' + formatLength(length) + '</div></li>');
	}
}

// Converts a length in meters into a displayable format
function formatLength(metersLength) {
  let feetLength = metersLength * 3.28084;
  if (feetLength < 1000) {
    return Math.ceil(feetLength / 10) * 10 + " feet";
  } else {
    let milesLength = feetLength / 5280;
    return milesLength.toFixed(1) + " miles";
  }
}

/*
Path is geoJson.
 */
function displayPath(path) {
  console.log(path);
  // return;

  clearPath();
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
		  "coordinates": path
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
  
  pathExists = true;
}

function displayWeather(weather) {
  if (!weather) {
    console.log('no weather found');
    return;
  }

  const bestWeather = weather[weather.best];
  removeWeatherMarkers();
  if(bestWeather == null){
  	console.log("No best weather. NULL");
  	return; 
  }
  const weatherInfo = bestWeather.weatherData;
  for(let i = 0; i < weatherInfo.length; i++){
  	addWeatherMarker(weatherInfo[i]);
  }
}

$(document).ready(() => {
  $('#itinerary-form').submit(event => {
    event.preventDefault();

    getFormInputs().then(postParameters => {
      console.log(postParameters);
      $.post('/stormrouter/route', {params: JSON.stringify(postParameters)}, responseJSON => {
        const response = JSON.parse(responseJSON);
        console.log(response);

        const message = response.message;

        if (message) {
          console.log(message);
          return;
        }
        const path = response.path;
        const segments = response.segments;
        const weather = response.weather;
        displayPath(path);
        displayDirections(segments);
        displayWeather(weather);
      });
    }).catch(reason => {
      console.log(reason);
      alert('There was an error processing your request.');
    });
  });
});
