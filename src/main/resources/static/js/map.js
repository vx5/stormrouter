// Mapbox GL JS
mapboxgl.accessToken =
    "pk.eyJ1IjoiamNyb3dsZTUiLCJhIjoiY2p0dmg5YzJoMXg1YTRlbXVsMWo2YWxtaSJ9.5-JA68Dz-1Vdhpt3cF2hJg";
L.mapbox.accessToken = mapboxgl.accessToken;


let map = new mapboxgl.Map({
  container: "map",
  style: "mapbox://styles/mapbox/streets-v11",
  center: [-71.4045438, 41.8258524], // center on Brown
  zoom: 16,
});

// add zoom buttons
map.addControl(new mapboxgl.NavigationControl({
  showCompass: false
}));

let results = {};

function addGeocoder(id, name) {
  const $target = $('#' + id);

  let geocoder = new MapboxGeocoder({
    accessToken: mapboxgl.accessToken,
    mapboxgl: mapboxgl
  });
  $target.append(geocoder.onAdd(map));
  geocoder.on('result', result => {
  	console.log(result);
  	results[id] = result.geometry.coordinates;
  });
  
  const $input = $target.find('input');

  $input.attr('name', name);
  $input.prop('required', true);
}

addGeocoder('geocoderStart', 'start');
addGeocoder('geocoderEnd', 'end');

const WEATHER_TYPE = [
    'PLAIN', 'RAIN', 'SNOW', 'HEAT', 'FOG', 'WIND'
];
