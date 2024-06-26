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
let locationMarkers = {};

function clearGeocoderMarker(name) {
  coordinates[name] = null;
  const marker = locationMarkers[name];
  if (marker) {
    marker.remove();
    locationMarkers[name] = null;
  }
}

/* add a geocoder (search bar tied to Mapbox map) to the div with id 'name'.
 Give the <input> element of the geocoder name 'name'*/
function addGeocoder(name) {
  const $target = $('#' + name);

  let geocoder = new MapboxGeocoder({
    accessToken: mapboxgl.accessToken,
    mapboxgl: mapboxgl,
    marker: false
  });
  $target.append(geocoder.onAdd(map));

  // when user selects a location from geocoder dropdown
  geocoder.on('result', result => {
    clearGeocoderMarker(name);
    // flip lnglat to latlng and store in coordinates
    const lnglat = result.result.geometry.coordinates;
    coordinates[name] = [lnglat[1], lnglat[0]];

    // add Mapbox Marker to map and store it by name in locationMarkers
    locationMarkers[name] = new mapboxgl.Marker()
        .setLngLat(lnglat)
        .addTo(map);

    // calculate bounding box of all location markers
    const bounds = new mapboxgl.LngLatBounds();
    for (const marker of Object.values(locationMarkers)) {
      if (!marker) continue;
      bounds.extend(marker.getLngLat());
    }

    // add padding to bounding box based on current panel widths and heights
    map.fitBounds(bounds, {
      padding: {
        top: 60,
        bottom: $('#bottom-bar').height() + 30,
        left: $('#side-bar').width() + 30,
        right: 30
      },
      maxZoom: 16
    });
  });

  //Clear after the input field is cleared.
  geocoder.on('clear', () => {
    clearGeocoderMarker(name);
  });

  // set geocoder <input> name to 'name' and make it required for form data
  const $input = $target.find('input');
  $input.attr('name', name);
  $input.prop('required', true);
}

addGeocoder('start');
addGeocoder('end');
