<#assign content>
  <div id="map"></div>

  <h2 class="title">Storm Router</h2>
  <div class="full-height flex-container">

    <!-- Side Bar -->
    <div id="side-bar" class="column panel">
      <div class="full-height flex-container">
        <div id="side-bar-content" class="left column collapsable" style="display: none;">

          <!-- Directions list -->
          <div class="directions">
            <h4 class="section-header">Directions</h4>
            <div class="slider">
              <div id="depart">Best Departure Time: 0 hr</div>
              <input type="range" id="slider" min="0" max="5" value="2"/>
              <span style='font-size: 14px;'>0 hr</span>
            </div>
            <ol class="directions-list">
              <!-- <li>
                <div class="img"></div>
                <div class="instructions">Left</div>
              </li>-->
            </ol> 
            <!-- <button>Get Suggested Times</button> -->
          </div>

        </div>

        <!-- Collapse tab -->
        <div class="column collapse" id="side">
          <i class="fas fa-angle-right"></i>
        </div>
      </div>
    </div>

    <!-- Bottom Bar -->
    <div id="bottom-bar" class="column panel">

      <!-- Collapse tab -->
      <div id="bottom-header" class="flex-container collapse">
        <h4 class="section-header">Itinerary</h4>
        <i class="fas fa-angle-down"></i>
      </div>

      <div class="itinerary collapsable">
        <form action="" method="post" id="itinerary-form" name="itinerary-form" class="itinerary-form" autocomplete="off">

          <!-- Row with start location and date -->
          <div class="form-row">
            <div class="location-field">
              <label for="start">Start Location</label>
              <div id="start" class="geocoder"></div>
            </div>
            <div class="date-field">
              <label for="datepicker">Date &amp; Time</label>
              <input id="datepicker" name="date" type="datetime-local" required>
            </div>
          </div>

          <!-- Waypoints panel -->
          <ul class="waypoints">
            <li id="waypoint-header">Waypoints <i class="fas fa-plus" id="add-waypoint"></i></li>
          </ul>

          <!-- Row with destination and submit button -->
          <div class="form-row">
            <div class="location-field">
              <label for="destination">Destination</label>
              <div id="end" class="geocoder"></div>
            </div>
            <button type="submit" id="route">Submit</button>
          </div>

        </form>
      </div>
    </div>
  </div>
</#assign>
<#include "main.ftl">
