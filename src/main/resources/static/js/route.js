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

function clearPath() {
  map.removeLayer('route');
  map.removeSource('route');
}

//Clear a layer by ID.
function clearLayer(id) {
  map.removeLayer(id);
  map.removeSource(id);
}

//Clear weather icons
/*function removeWeather() {
  for (let i = 0; i < weatherOnMap.length; i++) {
    clearLayer(weatherOnMap[i]);
  }
  weatherOnMap = [];
}*/

/*
weatherInfo = {
	lat:
	lon:
	enum weather:
}
*/
/*function addWeather(weatherInfo) {
  map.addLayer({
    "id": "weather" + weatherId,
    "type": "symbol",
    "source": {
      "type": "geojson",
      "data": {
        "type": "FeatureCollection",
        "features": [{
          "type": "Feature",
          "geometry": {
            "type": "Point",
            "coordinates": [weatherInfo.lon, weatherInfo.lat]
          }
        }]
      }
    },
    "layout": {
      "icon-image": WEATHER_TYPE[weatherInfo.weather],
      "icon-size": 0.1
    }
  });
  weatherOnMap.push("weather" + weatherId);
  weatherId++;
}*/

function addWeatherMarker(type, lat, lon) {
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
  }).setHTML('<h1>Popup!</h1>');

  marker.setPopup(popup);

  const $img = $('#' + id);

  $img.mouseover(() => {
    marker.togglePopup();
  });

  $img.mouseleave(() => {
    marker.togglePopup();
  });
}

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
