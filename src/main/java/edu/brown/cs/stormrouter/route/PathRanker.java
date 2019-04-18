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
    // TODO
    // 0. Assign path as default
    defaultPath = centerPath;
    // 1. Check for all desired time offsets, adds those copy paths to Queue
    // 2. Check for appropriate IDs of waypoints to check weather for
    // 3. Use weatherIds list and weather pulls to score all paths
    // 4. Return best path
    return null;
  }

  // 1. Helper method
  private void genNewPaths(Path given) {
    // Sets the start, end time of the path that is desired
    long unixStart = given.getStartTime();
    List<Waypoint> pathPoints = given.getWaypoints();
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
        Iterator<Waypoint> oldPathPoints = given.getWaypoints().iterator();
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

  // Helper for 1
  private long hrToMs(float hr) {
    // Converts hours to milliseconds, using 60
    // for minutes, 60 for seconds, 1000 for milliseconds
    return (long) hr * 60 * 60 * 1000;
  }

  // 2. Helper method
  private void fillIds(Path basis) {
    // Make use of NUM_POINTS

    // Iterate through center path

    // Use Haversine distance between points

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
