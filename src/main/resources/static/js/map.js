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

let coordinates = {};

function addGeocoder(name) {
  const $target = $('#' + name);

  let geocoder = new MapboxGeocoder({
    accessToken: mapboxgl.accessToken,
    mapboxgl: mapboxgl
  });
  $target.append(geocoder.onAdd(map));

  geocoder.on('result', result => {
  	console.log(result);
  	// TODO: is this lon lat?
    const lnglat = result.result.geometry.coordinates;
  	coordinates[name] = [lnglat[1], lnglat[0]];
  });
  
  const $input = $target.find('input');

  $input.attr('name', name);
  $input.focusout(() => {
  	console.log($input);
  	console.log($input[0]);
  	console.log($input[0].nextElementSibling);
  	console.log($($input[0].nextElementSibling).find('a'));
  	console.log($($input[0].nextElementSibling).find('a')[0]);
  	
  	$($($input[0].nextElementSibling).find('.active')[0]).trigger('click');
  });
  $input.prop('required', true);
}

addGeocoder('start');
addGeocoder('end');

const WEATHER_TYPE = [
    'PLAIN', 'RAIN', 'SNOW', 'HEAT', 'FOG', 'WIND'
];
