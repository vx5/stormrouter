// Mapbox GL JS
mapboxgl.accessToken =
    "pk.eyJ1IjoiamNyb3dsZTUiLCJhIjoiY2p0dmg5YzJoMXg1YTRlbXVsMWo2YWxtaSJ9.5-JA68Dz-1Vdhpt3cF2hJg";
L.mapbox.accessToken = mapboxgl.accessToken;


let map = new mapboxgl.Map({
  container: "map",
  style: "mapbox://styles/mapbox/streets-v11",
  center: [-71.4045438, 41.8258524],
  zoom: 16,
});

// add zoom buttons
map.addControl(new mapboxgl.NavigationControl({
  showCompass: false
}));

function addGeocoder(id, name) {
  const $target = $('#' + id);

  $target.append(new MapboxGeocoder({
    accessToken: mapboxgl.accessToken,
    mapboxgl: mapboxgl
  }).onAdd(map));

  $target.find('input').attr('name', name);
}

addGeocoder('geocoderStart', 'start');
addGeocoder('geocoderEnd', 'end');

$(".mapboxgl-ctrl-geocoder").css("z-index", null);






// Leaflet + OpenStreetMap
/*let map = L.map('map', {zoomControl: false}).setView([41.8268238, -71.4035084], 16);

L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
  attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
}).addTo(map);

new L.Control.Zoom({position: 'topright'}).addTo(map);

L.marker([41.8268238, -71.4035084]).addTo(map)
    .bindPopup('A pretty CSS3 popup.<br> Easily customizable.')
    .openPopup();*/
