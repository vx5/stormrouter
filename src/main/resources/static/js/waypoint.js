let numWays = 0;
let geocoderId = 0;
const LIMIT = 4;

function addWaypoint() {
  if (numWays >= LIMIT) {
    console.log("waypoint limit: " + LIMIT);
    return;
  }
  numWays++;
  $(".waypoints").append(
      '<li>\n' +
      '  <div class="form-row">\n' +
      '    <div class="location-field">\n' +
      '      <label for="way">Waypoint </label>\n' +
      '        <i class="fas fa-minus" id="delete-waypoint" onclick="deleteWaypoint(this)"></i>' +
      `      <div id="geocoder${geocoderId}" class="geocoder"></div>` +
      '    </div>\n' +
      '    <div class="duration-field">\n' +
      '      <label for="duration">Duration</label>\n' +
      //'      <input class="without" id="duration" name="duration" type="time" pattern="([1]?[0-9]|2[0-3]):[0-5][0-9]" required>\n' +
      '      <input id="duration" name="duration" type="number" required> Minutes\n' +
      '    </div>\n' +
      '  </div>\n' +
      '</li>');

  addGeocoder('geocoder' + geocoderId, 'waypoint' + geocoderId);
  geocoderId++;
}


function deleteWaypoint(elem) {
  $($(elem)[0].parentNode)[0].parentNode.remove();
  numWays--;
}

$(document).ready(() => {
  $("#add-waypoint").click(() => {
    addWaypoint();
  });
});