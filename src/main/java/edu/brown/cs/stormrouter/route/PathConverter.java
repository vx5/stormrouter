package edu.brown.cs.stormrouter.route;

import java.util.List;

import edu.brown.cs.stormrouter.directions.Segment;

public final class PathConverter {
  public static final PathConverter inst = new PathConverter();

  private PathConverter() {
  }

  public PathConverter getInst() {
    return inst;
  }

  public Path convertPath(List<Segment> inputPath, long unixStartTime) {
    // 1. Take in input path (change parameters)
    // 1a. Make path with offset of 0
    Path centerPath = new Path(unixStartTime);
    // 2. Iterate through path, getting information on each point, converting to
    // waypoint, adding to path
    long timeIndex = unixStartTime;
    LatLon startCoord = inputPath.get(0).getStart();
    Waypoint newPoint = new Waypoint((float) startCoord.getLatitude(),
        (float) startCoord.getLongitude());
    newPoint.setTime(timeIndex);
    centerPath.addWaypoint(new Waypoint((float) startCoord.getLatitude(),
        (float) startCoord.getLongitude()));
    for (Segment seg : inputPath) {
      LatLon endCoord = seg.getEnd();
      newPoint = new Waypoint((float) endCoord.getLatitude(),
          (float) endCoord.getLongitude());
      timeIndex += seg.getDuration() * 1000;
      newPoint.setTime(timeIndex);
      centerPath.addWaypoint(newPoint);
    }
    // 3. Return path
    return centerPath;
  }

}
