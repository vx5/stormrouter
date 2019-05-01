package edu.brown.cs.stormrouter.route;

import java.util.Arrays;
import java.util.List;

import edu.brown.cs.stormrouter.directions.LatLon;
import edu.brown.cs.stormrouter.directions.Segment;
import edu.brown.cs.stormrouter.main.RouteHandler.RouteWaypoint;

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
   * @param unixStartTime Long unix form of start time
   * @return Path object given input Segment list and start time
   */
  public static Path convertPath(List<Segment> inputPath,
      RouteWaypoint[] waypoints, long unixStartTime) {
    // Makes new Path object using the given start time
    Path centerPath = new Path(unixStartTime);
    // Stores information associated with very first point in the weather-loaded
    // tracker
    long timeIndex = unixStartTime;
    LatLon startCoord = inputPath.get(0).getStart();
    Waypoint newPoint = new Waypoint((float) startCoord.getLatitude(),
        (float) startCoord.getLongitude());
    newPoint.setTime(timeIndex);
    // Adds that new point to the tracker
    centerPath.addWaypoint(newPoint);
    // Stores amount by which next point must be delayed, 0 if no delay
    int minDelay = 0;
    // Iterates through all segments left in path
    for (Segment seg : inputPath) {
      // Selects end point
      LatLon endCoord = seg.getEnd();
      // Checks for intermediary waypoint (will check in order)
      if (waypoints.length > 0) {
        RouteWaypoint inter = waypoints[0];
        // Generates LatLon object for comparison testing
        LatLon interLoc = new LatLon(inter.waypoint[0], inter.waypoint[1]);
        // Checks for coordinate match
        if (endCoord.equals(interLoc)) {
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
      }
      // Constructs Waypoint
      newPoint = new Waypoint((float) endCoord.getLatitude(),
          (float) endCoord.getLongitude());
      // Iterates time index based on distance
      timeIndex += seg.getDuration();
      // If applicable, iterates time index based on delay
      if (minDelay != 0) {
        // timeIndex += minDelay * Units.S_PER_MIN;
        minDelay = 0;
      }
      // Sets time index
      newPoint.setTime(timeIndex);
      // Adds point
      centerPath.addWaypoint(newPoint);
    }
    // Return filled path
    return centerPath;
  }

}
