let numWays = 0;
let geocoderId = 0;
const LIMIT = 4;

function addWaypoint() {
  if (numWays >= LIMIT) return;
  numWays++;
  const geocoderName = 'waypoint' + geocoderId;
  $(".waypoints").append(
      '<li>' +
      '  <div class="form-row">' +
      '    <div class="location-field">' +
      '      <label for="way">Waypoint </label>' +
      '      <i class="fas fa-minus" id="delete-waypoint" onclick="deleteWaypoint(this)"></i>' +
      `      <div id="${geocoderName}" class="geocoder"></div>` +
      '    </div>' +
      '    <div class="duration-field">' +
      '      <label for="duration">Duration</label>' +
      '      <input id="duration" name="duration" type="number" min="0" required> Minutes' +
      '    </div>' +
      '  </div>' +
      '</li>');
  addGeocoder(geocoderName);
  geocoderId++;
}


function deleteWaypoint(elem) {
  // Delete geocode.
  $(elem).next().find('button').trigger('click');
  // Delete the form row.
  $(elem).closest('.form-row').remove();
  numWays--;
}

$(document).ready(() => {
  $("#add-waypoint").click(addWaypoint);
});