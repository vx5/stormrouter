package edu.brown.cs.stormrouter.route;

import java.util.List;

import edu.brown.cs.stormrouter.directions.LatLon;
import edu.brown.cs.stormrouter.directions.Segment;

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
  public static Path convertPath(List<Segment> inputPath, long unixStartTime) {
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
    // Iterates through all segments left in path
    for (Segment seg : inputPath) {
      // Selects end point
      LatLon endCoord = seg.getEnd();
      // Constructs Waypoint
      newPoint = new Waypoint((float) endCoord.getLatitude(),
          (float) endCoord.getLongitude());
      // Iterates, sets time index
      timeIndex += seg.getDuration() * Units.MS_PER_S;
      newPoint.setTime(timeIndex);
      // Adds point
      centerPath.addWaypoint(newPoint);
    }
    // Return filled path
    return centerPath;
  }

}
