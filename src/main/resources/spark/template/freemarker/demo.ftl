<#assign content>
  <div id="map"></div>
  <div class="full-height flex-container">
    <div id="side-bar" class="column panel">
      <div class="full-height flex-container">
        <div id="side-bar-content" class="left column collapsable">
          <div class="directions">
            <h4 class="section-header">Directions</h4>
            <ol class="directions-list">
              <li>
                <div class="img"></div>
                <div class="instructions">Left</div>
              </li>
              <li>
                <div class="img"></div>
                <div class="instructions">Right</div>
              </li>
            </ol>
            <button>Get Suggested Times</button>
          </div>
        </div>
        <div class="column collapse" id="side">
          <i class="fas fa-angle-left"></i>
        </div>
      </div>
    </div>
    <div id="bottom-bar" class="column panel">
      <div id="bottom-header" class="flex-container collapse">
        <h4 class="section-header">Itinerary</h4>
        <i class="fas fa-angle-down"></i>
      </div>
      <div class="itinerary collapsable">
        <form action="" method="get" class="itinerary-form">
          <!-- row with start location and date -->
          <div class="form-row">
            <div class="location-field">
              <label for="start">Start Location</label>
              <input id="start" name="start" type="text">
            </div>
            <div class="date-field">
              <label for="datepicker">Date &amp; Time</label>
              <input id="datepicker" name="date" type="text">
            </div>
          </div>

          <ul class="waypoints">
            <!-- test rows -->
            <li>
              <div class="form-row">
                <div class="location-field">
                  <label for="way-1">Waypoint 1</label>
                  <input id="way-1" name="way-1" type="text">
                </div>
                <div class="duration-field">
                  <label for="duration-1">Duration</label>
                  <input id="duration-1" name="duration-1" type="text">
                </div>
              </div>
            </li>
            <li>
              <div class="form-row">
                <div class="location-field">
                  <label for="way-2">Waypoint 2</label>
                  <input id="way-2" name="way-2" type="text">
                </div>
                <div class="duration-field">
                  <label for="duration-2">Duration</label>
                  <input id="duration-2" name="duration-2" type="text">
                </div>
              </div>
            </li>
            <li>
              <div class="form-row">
                <div class="location-field">
                  <label for="way-3">Waypoint 3</label>
                  <input id="way-3" name="way-3" type="text">
                </div>
                <div class="duration-field">
                  <label for="duration-3">Duration</label>
                  <input id="duration-3" name="duration-3" type="text">
                </div>
              </div>
            </li>
            <li>
              <div class="form-row">
                <div class="location-field">
                  <label for="way-4">Waypoint 4</label>
                  <input id="way-4" name="way-4" type="text">
                </div>
                <div class="duration-field">
                  <label for="duration-4">Duration</label>
                  <input id="duration-4" name="duration-4" type="text">
                </div>
              </div>
            </li>
          </ul>

          <div class="form-row">
            <div class="location-field">
              <label for="destination">Destination</label>
              <input id="destination" name="destination" type="text">
            </div>
            <button type="submit">Submit</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</#assign>
<#include "main.ftl">
