package edu.brown.cs.stormrouter.route;

import java.util.ArrayList;
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
  private void genAlternatePaths() {
    // Use offsets
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
