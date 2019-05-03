package edu.brown.cs.stormrouter.route;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import edu.brown.cs.stormrouter.directions.LatLon;
import edu.brown.cs.stormrouter.directions.Segment;
import edu.brown.cs.stormrouter.main.RouteHandler.RouteWaypoint;
import edu.brown.cs.stormrouter.weather.WeatherAPIHandler;

/**
 * @author vx5
 *
 *         Class that holds static convertPath() method, which takes list of
 *         Segments from directions package, and a time, and returns a Path that
 *         represents the sum of the given Segments that starts at the given
 *         time.
 */
public final class PathConverter {

  private PathConverter() {
  }

  /**
   * Returns Path object representing path formed by input List of Segments and
   * input start time.
   * 
   * @param inputPath     List of Segments representing directions
   * @param waypoints     Array of RouteWaypoints, representing stopovers in
   *                      route
   * @param unixStartTime Long unix form of start time (in local time)
   * @return Path object given input Segment list and start time
   * @throws Exception if generated path is out of range
   */
  public static Path convertPath(List<Segment> inputPath,
      RouteWaypoint[] waypoints, long unixStartTime) throws Exception {
    LatLon startCoord = inputPath.get(0).getStart();
    float startLat = (float) startCoord.getLatitude();
    float startLong = (float) startCoord.getLongitude();
    // META: Checks that entered start time is valid in local time at start
    // location
    // Obtains time zone using API
    String timeZone = WeatherAPIHandler.getWeather(startLat, startLong)
        .getTimezone();
    // Sets up time zones for start location and current time zone
    TimeZone localZone = TimeZone.getTimeZone(timeZone);
    TimeZone thisZone = TimeZone.getDefault();
    // Stores raw offsets to standard time
    long localZoneOffset = localZone.getRawOffset();
    long thisZoneOffset = thisZone.getRawOffset();
    // Checks for daylight savings, makes appropriate adjustments if necessary
    if (localZone.inDaylightTime(new Date(unixStartTime))) {
      localZoneOffset += localZone.getDSTSavings();
    }
    if (thisZone.inDaylightTime(new Date(System.currentTimeMillis()))) {
      thisZoneOffset += thisZone.getDSTSavings();
    }
    // Calculates millisecond difference in start time
    long msAhead = localZoneOffset - thisZoneOffset;
    long unixStartTimeSystemZone = (unixStartTime * 1000L) - msAhead;
    // Checks for past time
    if (unixStartTimeSystemZone <= System.currentTimeMillis()) {
      throw new Exception("Invalid departure time");
    }
    // If no error, move on with Path generation
    // Generates proper seconds offset
    long unixOffset = msAhead / 1000L;
    // Makes new Path object using the given start time
    Path centerPath = new Path(unixStartTime, unixOffset);
    // Stores information associated with very first point in the weather-loaded
    // tracker
    long timeIndex = unixStartTime;
    double distSoFar = 0;
    Pathpoint newPoint = new Pathpoint((float) startCoord.getLatitude(),
        (float) startCoord.getLongitude());
    newPoint.setTime(timeIndex);
    newPoint.setDistToReach(distSoFar);
    // Adds that new point to the tracker
    centerPath.addPathpoint(newPoint);
    // Stores amount by which next point must be delayed, 0 if no delay
    int minDelay = 0;
    // Iterates through all segments left in path
    for (Segment seg : inputPath) {
      // Selects end point
      LatLon endCoord = seg.getEnd();
      // Checks for intermediary waypoint (will check in order)
      if (seg.isTerminal() && waypoints.length > 0) {
        // Obtains first intermediary waypoint
        RouteWaypoint inter = waypoints[0];
        // Updates minute delay
        minDelay = inter.duration;
        // Removes first element from array
        if (waypoints.length > 1) {
          waypoints = Arrays.copyOfRange(waypoints, 1, waypoints.length);
        } else {
          // In case of only element in array, set to empty array
          waypoints = new RouteWaypoint[0];
        }
      }
      // Constructs Waypoint
      newPoint = new Pathpoint((float) endCoord.getLatitude(),
          (float) endCoord.getLongitude());
      // Iterates time index based on duration
      timeIndex += seg.getDuration();
      // If applicable, iterates time index based on delay
      if (minDelay != 0) {
        timeIndex += minDelay * Units.S_PER_MIN;
        // Resets delay variable
        minDelay = 0;
      }
      // Checks time index for validity
      if (Units.UnixToHrsFromNow(timeIndex) > 48) {
        throw new Exception("Path too long");
      }
      // Sets time index
      newPoint.setTime(timeIndex);
      // Iterates and sets distance
      distSoFar += seg.getLength();
      newPoint.setDistToReach(distSoFar);
      // Adds point
      centerPath.addPathpoint(newPoint);
    }
    // Return filled path
    return centerPath;
  }

}
