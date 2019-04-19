package edu.brown.cs.stormrouter.route;

import java.util.List;

public final class PathConverter {
  public static final PathConverter inst = new PathConverter();

  private PathConverter() {
  }

  public PathConverter getInst() {
    return inst;
  }

  public Path convertPath(List<LatLon> inputPath, long unixStartTime) {
    // 1. Take in input path (change parameters)
    // 1a. Make path with offset of 0
    Path centerPath = new Path(unixStartTime);
    // 2. Iterate through path, getting information on each point, converting to
    // waypoint, adding to path
    for (LatLon point : inputPath) {
      centerPath.addWaypoint(new Waypoint((float) point.getLatitude(),
          (float) point.getLongitude()));
    }
    // 3. Return path
    return centerPath;
  }

}
