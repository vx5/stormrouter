package edu.brown.cs.stormrouter.route;

import java.util.List;

import edu.brown.cs.stormrouter.directions.Segment;

public final class PathConverter {
  // Stores only instance of PathConverter for use by singleton pattern
  public static final PathConverter inst = new PathConverter();

  private PathConverter() {
  }

  public PathConverter getInst() {
    return inst;
  }

  public Path convertPath(List<Segment> inputPath, long unixStartTime) {
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
      timeIndex += seg.getDuration() * 1000;
      newPoint.setTime(timeIndex);
      // Adds point
      centerPath.addWaypoint(newPoint);
    }
    // Return filled path
    return centerPath;
  }

}
