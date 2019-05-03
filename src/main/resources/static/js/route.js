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

let skycons = new Skycons({"color": "#00022E"});

/*function forwardGeocode(input) {
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
}*/


function getFormInputs() {
  const form = document.forms['itinerary-form'];
  /*const start = form['start'].value;
  const end = form['end'].value;*/
  const startPoint = coordinates['start'];
  const endPoint = coordinates['end'];
  const date = form['date'].value;
  const unixTime = +new Date(date) / 1000;

  //const waypointPromises = [];
  const waypointStops = [];

  const inputs = $(form).serializeArray();
  for (let i = 2; i < inputs.length - 1;) { // begin at first waypoint input and end before destination input
    const waypointName = inputs[i++].name;
    const waypoint = coordinates[waypointName];
    const duration = inputs[i++].value;

    if (!waypoint) {
      throw 'ERROR: no coordinate for ' + waypointName;
    }
    waypointStops.push({waypoint, duration});
  }

  return {start: startPoint, date: unixTime, destination: endPoint, waypoints: waypointStops};
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
  const iconString = weatherData.icon;
  const lat = weatherData.lat;
  const lon = weatherData.lon;
  const summary = weatherData.weatherSum;
  const id = 'weather' + weatherId++;
  let markerCanvas = document.createElement("canvas");
  markerCanvas.width = 48;
  markerCanvas.height = 48;
  markerCanvas.id = id;
  document.body.appendChild(markerCanvas);
  skycons.add(id, getSkyconForString(iconString));
  skycons.play();

  const marker = new mapboxgl.Marker(markerCanvas)
      .setLngLat([lon, lat])
      .addTo(map);
  markers.push(marker);

  const popup = new mapboxgl.Popup({
    closeButton: false,
    closeOnClick: false
  }).setHTML('<div id="popup">' + summary + '</div>');

  marker.setPopup(popup);

  marker.getElement().addEventListener('mouseover', () => {
    marker.togglePopup();
  });

  marker.getElement().addEventListener('mouseout', () => {
    marker.togglePopup();
  });
}

function getSkyconForString(iconString) {
  switch (iconString) {
    case "clear-day":
      return Skycons.CLEAR_DAY;
    case "clear-night":
      return Skycons.CLEAR_NIGHT;
    case "partly-cloudy-day":
      return Skycons.PARTLY_CLOUDY_DAY;
    case "partly-cloudy-night":
      return Skycons.PARTLY_CLOUDY_NIGHT;
    case "cloudy":
      return Skycons.CLOUDY;
    case "rain":
      return Skycons.RAIN;
    case "sleet":
      return Skycons.SLEET;
    case "snow":
      return Skycons.SNOW;
    case "wind":
      return Skycons.WIND;
    case "fog":
      return Skycons.FOG;
    default:
      return Skycons.CLEAR_DAY;
  }
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
		const type = directions[i].type;
		let iconName = iconForDirectionType(type);
		$directs.append(
			'<li>'
        + '<img src="/img/' + iconName + '" width=24 height=24">'
        + '<div class="instructions">' + instruction + ' for ' + formatLength(length) + '</div></li>');
	}
}

function iconForDirectionType(type) {
  switch (type) {
    case 0:
      return "turn_left.svg";
    case 1:
      return "turn_right.svg";
    case 2:
      return "turn_sharp_left.svg";
    case 3:
      return "turn_sharp_right.svg";
    case 4:
      return "turn_slight_left.svg";
    case 5:
      return "turn_slight_right.svg";
    case 6:
      return "turn_straight.svg";
    case 7:
      return "roundabout.svg";
    case 8:
      return "roundabout.svg";
    case 9:
      return "uturn.svg";
    case 10:
      return "arrive.svg";
    case 11:
      return "depart.svg";
    case 12:
      return "fork_slight_left.svg";
    case 13:
      return "fork_slight_right.svg";
    default:
      return "turn_straight.svg";
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
      "line-color": "#5995DA",
      "line-width": 8
    }
  });
  
  pathExists = true;
}

function displayBestWeather(weather) {
  if (!weather) {
    console.log('no weather found');
    return;
  }
  console.log(weather);
  const bestWeather = weather[weather.best];
  resetWeather(weather);
  removeWeatherMarkers();
  if(weather == null){
  	console.log("No best weather. NULL");
  	return; 
  }
  const weatherInfo = bestWeather.weatherData;
  for(let i = 0; i < weatherInfo.length; i++){
  	addWeatherMarker(weatherInfo[i]);
  }
}

function displayWeather(weather){
  if(weather == null){
  	console.log("No weather at this time.");
  	return; 
  }
  removeWeatherMarkers();
  const weatherInfo = weather.weatherData;
  for(let i = 0; i < weatherInfo.length; i++){
  	addWeatherMarker(weatherInfo[i]);
  }
}

$(document).ready(() => {
  $('#itinerary-form').submit(event => {
    event.preventDefault();

    try {
      const postParameters = getFormInputs();

      console.log(postParameters);
      $.post('/stormrouter/route', {params: JSON.stringify(postParameters)}, responseJSON => {
        const response = JSON.parse(responseJSON);
        console.log(response);

        const message = response.message;

        if (message) {
          // console.log(message);
          alert(message);
          return;
        }
        curPath = response.path;
        curSegments = response.segments;
        curWeather = response.weather;
        displayPath(parsePolyline(curPath));
        displayDirections(curSegments);
        displayBestWeather(curWeather);
        showContent($collapsable[0], $($arrow[0])[0]);
        collapse($collapsable[1], $($arrow[1])[0]);
      });
    } catch (err) {
      console.log(err);
      alert('There was an error processing your request.');
    }

    /*getFormInputs().then(postParameters => {
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
        displayPath(parsePolyline(path));
        displayDirections(segments);
        displayWeather(weather);
      });
    }).catch(reason => {
      console.log(reason);
      alert('There was an error processing your request.');
    });*/
  });
});
