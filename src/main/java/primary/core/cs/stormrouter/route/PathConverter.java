package primary.core.cs.stormrouter.route;

import java.util.List;

import primary.core.cs.stormrouter.conversions.TimeZoneOps;
import primary.core.cs.stormrouter.conversions.Units;
import primary.core.cs.stormrouter.directions.LatLon;
import primary.core.cs.stormrouter.directions.Segment;
import primary.core.cs.stormrouter.main.RouteHandler.RouteWaypoint;

/**
 * @author vx5
 * <p>
 * Class that holds static convertPath() method, which takes list of Segments
 * from directions package, and a time, and returns a Path that represents the
 * sum of the given Segments that starts at the given time.
 */
public final class PathConverter {

  private PathConverter() {
  }

  /**
   * Returns Path object representing path formed by input List of Segments and
   * input start time.
   * @param inputPath     List of Segments representing directions
   * @param waypoints     Array of RouteWaypoints, representing stopovers in
   *                      route
   * @param unixStartTime Long unix form of start time (in local time)
   * @return Path object given input Segment list and start time
   * @throws Exception if generated path is out of range
   */
  public static Path convertPath(List<Segment> inputPath,
      RouteWaypoint[] waypoints, long unixStartTime) throws Exception {
    // Checks for path existing
    if (inputPath.size() == 0) {
      throw new Exception("No path");
    }
    LatLon startCoord = inputPath.get(0).getStart();
    // Calculates millisecond difference in start time
    long msAhead = TimeZoneOps.getCurrentMsAhead(unixStartTime,
        startCoord.getLatitude(), startCoord.getLongitude());
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
    // Stores counter to iterate through waypoints
    int waypointId = 0;
    // Stores amount by which next point must be delayed, 0 if no delay
    int minDelay = 0;
    // Iterates through all segments left in path
    for (Segment seg : inputPath) {
      // Selects end point
      LatLon endCoord = seg.getEnd();
      // Checks for intermediary waypoint (will check in order)
      if (seg.isTerminal() && waypointId < waypoints.length) {
        // Obtains first intermediary waypoint
        RouteWaypoint inter = waypoints[waypointId];
        // Updates minute delay
        minDelay = inter.getDuration();
        // Removes first element from array
        waypointId++;
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
      if (Units.unixToHrsFromNow(timeIndex) > 48) {
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
