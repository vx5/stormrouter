let weatherMarkers = [];
let weatherId = 0;

let skycons = new Skycons({"color": "#00022E"});

// Clear a layer by ID.
function clearLayer(id) {
  if (!map.getLayer(id)) return;
  map.removeLayer(id);
  map.removeSource(id);
}

// Clear the current path.
const clearPath = () => clearLayer('route');

// Add weather weatherMarkers.
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
  weatherMarkers.push(marker);

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

// Remove all weather weatherMarkers.
function clearWeatherMarkers() {
  weatherMarkers.forEach(marker => marker.remove());
  weatherMarkers = [];
  weatherId = 0;
}

function clearRoute() {
  clearPath();
  clearWeatherMarkers();
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

// Parse from [{lat: , lon: },...] to [[lon,lat],...]
function parsePolyline(polyline) {
  return polyline.map(coord => [coord.longitude, coord.latitude]);
}

// Display directions
function displayDirections(directions) {
  const $directs = $('ol.directions-list');
  $directs.empty();
  directions.forEach(direction => {
    const length = direction.length;
    const instruction = direction.instructions;
    const type = direction.type;
    let iconName = iconForDirectionType(type);
    $directs.append(
        '<li>'
        + `<img class="direction-icon" alt="${iconName} icon" src="/img/${iconName}" width="24">`
        + `<div class="instructions">${instruction} for ${formatLength(length)}</div></li>`);
  });
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
    return Math.ceil(feetLength / 10) * 10 + ' feet';
  } else {
    let milesLength = feetLength / 5280;
    return milesLength.toFixed(1) + ' miles';
  }
}

// Displays a GeoJSON path on the map.
function displayPath(path) {
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
}

function displayBestWeather(weather) {
  if (!weather) {
    console.log('no weather found');
    return;
  }
  const bestWeather = weather[weather.best];
  resetWeather(weather);
  clearWeatherMarkers();
  if (!bestWeather) {
    console.log('no best weather found');
    return;
  }
  const weatherInfo = bestWeather.weatherData;
  weatherInfo.forEach(addWeatherMarker);
}

function displayWeather(weather) {
  if (!weather) {
    console.log('no weather found at this time');
    return;
  }
  clearWeatherMarkers();
  const weatherInfo = weather.weatherData;
  weatherInfo.forEach(addWeatherMarker);
}


/*
Parse the form input get request object:
	start: starting point
	date: date of starting point (unix time)
	destination: end point
	waypoints: array of waypoint coordinates and durations
*/
function getFormInputs() {
  const form = document.forms['itinerary-form'];
  const startPoint = coordinates['start'];
  const endPoint = coordinates['end'];
  const date = form['date'].value;
  departTime = new Date(date);
  const unixTime = +new Date(date) / 1000;
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

$(document).ready(() => {
  $('#itinerary-form').submit(event => {
    event.preventDefault();

    try {
      const postParameters = getFormInputs();

      $.post('/stormrouter/route', {params: JSON.stringify(postParameters)}, responseJSON => {
        const response = JSON.parse(responseJSON);
        const message = response.message;

        if (message) {
          clearRoute();
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
      clearRoute();
      console.log(err);
      alert('There was an error processing your request.');
    }
  });
});


