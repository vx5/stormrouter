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
let locationMarkers = [];

function addGeocoder(name) {
  const $target = $('#' + name);

  let geocoder = new MapboxGeocoder({
    accessToken: mapboxgl.accessToken,
    mapboxgl: mapboxgl,
    marker: false
  });
  $target.append(geocoder.onAdd(map));

  geocoder.on('result', result => {
  	console.log(result);

    const lnglat = result.result.geometry.coordinates;
  	coordinates[name] = [lnglat[1], lnglat[0]];

    const marker = new mapboxgl.Marker()
        .setLngLat(lnglat)
        .addTo(map);
    markers.push(marker);
    locationMarkers.push(marker);

    // zoom to display all markers
    const bounds = new mapboxgl.LngLatBounds();
    locationMarkers.forEach(marker => bounds.extend(marker.getLngLat()));
    map.fitBounds(bounds);
  });
  
  const $input = $target.find('input');

  $target.find('button').click(() => {
    coordinates[name] = null;
  });
  $input.on('keyup', () => {
    coordinates[name] = null;
  });
  $input.attr('name', name);
  $input.prop('required', true);
}

addGeocoder('start');
addGeocoder('end');

const WEATHER_TYPE = [
    'PLAIN', 'RAIN', 'SNOW', 'HEAT', 'FOG', 'WIND'
];
