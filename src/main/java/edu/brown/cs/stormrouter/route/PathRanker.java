package edu.brown.cs.stormrouter.route;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class PathRanker {
  // Stores default path
  private Path defaultPath;
  // Stores all paths to be ranked
  private Queue<Path> paths = new PriorityQueue<Path>();
  // Stores list of indices in paths to be used for waypoints
  private List<Integer> weatherIds = new ArrayList<Integer>();
  // Stores number of points that should be checked
  private final int NUM_POINTS = 20;
  // Stores desired hour offsets to be checked, if possible
  private final int[] HR_OFFSETS = new int[] {
      -2, -1, 1, 2, 5
  };

  public PathRanker() {
  }

  // Returns best path
  public Path bestPath(Path centerPath) {
    // 0. Assign path as default
    defaultPath = centerPath;
    paths.add(defaultPath);
    // 1. Check for all desired time offsets, adds those copy paths to Queue
    genNewPaths();
    // 2. Check for appropriate IDs of waypoints to check weather for
    fillIds();
    // 3. Use weatherIds list and weather pulls to score all paths
    scorePaths();
    // 4. Return best path
    return paths.poll();
  }

  // 1. Helper method
  private void genNewPaths() {
    // Sets the start, end time of the path that is desired
    long unixStart = defaultPath.getStartTime();
    List<Waypoint> pathPoints = defaultPath.getWaypoints();
    // Here, the time in seconds is obtained, then transformed to hours
    long unixEnd = unixStart + hrToMs(
        pathPoints.get(pathPoints.size() - 1).getTime() / (float) (60 * 60));
    // Iterates through all desired hour offsets to be checked
    for (int i = 0; i < HR_OFFSETS.length; i++) {
      // Obtains hour offset for path generation, converts to unix offset
      int hrOffset = HR_OFFSETS[i];
      long unixOffset = hrToMs(hrOffset);
      // Checks for valid time requirements, which requires that:
      // a) the would-be start time is not before now
      // b) the would-be end time is not past the 48-hour window DarkSky offers
      // weather data for
      if (System.currentTimeMillis() <= unixStart - unixOffset
          && System.currentTimeMillis() + hrToMs((float) 48.05) >= unixEnd
              + unixOffset) {
        // Instantiates new path to be generated, using new start time
        Path newPath = new Path(hrToMs(unixStart + unixOffset));
        // Iterates through all Waypoints in given path, to be replicated
        Iterator<Waypoint> oldPathPoints = defaultPath.getWaypoints()
            .iterator();
        while (oldPathPoints.hasNext()) {
          // Generate relevant copy of corresponding path point
          Waypoint newWaypoint = oldPathPoints.next().copy();
          // Transfers relevant information to newWaypoint
          newPath.addWaypoint(newWaypoint);
        }
        // Adds new path to the Queue
        paths.add(newPath);
      }
    }
  }

  // Side helper for 1
  private long hrToMs(float hr) {
    // Converts hours to milliseconds, using 60
    // for minutes, 60 for seconds, 1000 for milliseconds
    return (long) hr * 60 * 60 * 1000;
  }

  // 2. Helper method
  private void fillIds() {
    // META: Calculate standard breakdown of paths
    // Clears existing Ids, sets up with start
    ArrayList<Integer> allWeatherIds = new ArrayList<Integer>();
    allWeatherIds.add(0);
    // Check for time since last point, haversine
    int numWeatherPoints = 0;
    List<Waypoint> pathPoints = defaultPath.getWaypoints();
    // Stores information associate with last path id chosen
    float[] startCoords = pathPoints.get(0).getCoords();
    float lastLat = startCoords[0];
    float lastLong = startCoords[1];
    int lastUnixTime = pathPoints.get(0).getTime();
    // Iterates through pathPoints to check for default paths
    for (int i = 1; i < pathPoints.size(); i++) {
      // Obtain the relevant pathPoint
      Waypoint iterPoint = pathPoints.get(i);
      // Calculates unix time of this point being reached
      int iterUnixTime = iterPoint.getTime();
      // Checks if criteria for new point or met, if so, adds
      if (iterUnixTime - lastUnixTime > hrToMs((float) 0.5)) {
        // Adds to the index list
        allWeatherIds.add(i);
        // Resets information for this path id
        lastUnixTime = iterUnixTime;
        float[] iterCoords = iterPoint.getCoords();
        lastLat = iterCoords[0];
        lastLong = iterCoords[1];
      }
    }
    // META: Use NUM_Points to reduce number of points
    int skipNum = (int) Math.ceil(weatherIds.size() / (double) NUM_POINTS);
    // Iterates through all points to add to the weatherIds
    // Iterates using skip number
    for (int i = 0; i < pathPoints.size(); i += skipNum) {
      // Adds requisite number to weatherIds
      weatherIds.add(i);
    }
  }

  // 3. Helper method
  private void scorePaths() {
    // Needs to store weather data

    // Needs to score each path according to weather at given points (uses
    // second helper method?)

    // At each waypoint being scored, be sure to modify waypoint if necessary...
    // Also check timing to see if it's even valid for checking scores

    // Be sure to update path score at end
  }
}
