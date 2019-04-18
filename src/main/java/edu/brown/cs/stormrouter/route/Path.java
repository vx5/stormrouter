package edu.brown.cs.stormrouter.route;

import java.util.ArrayList;
import java.util.List;

public class Path implements Comparable<Path> {
  // Stores list of all waypoints in this path
  private List<Waypoint> pathPoints = new ArrayList<Waypoint>();
  // Stores current score of this path
  private int score = 0;
  // Stores offset, in UNIX time, of this path from the original time
  private long offset;

  // Constructor only considers offset
  public Path(long pathOffset) {
    offset = pathOffset;
  }

  public long getOffset() {
    return new Long(offset);
  }

  public void addWayPoint() {
    // TODO:
    // 1. Add parameter including way information
    // 2. Processes information into WayPoint
    // 3. Add Waypoint to pathPoints List
  }

  public void fillDurations() {
    // TODO:
    // 1. Iterate through all pathPoints
    // 2. For each pathPoint, set its distance according to the last Waypoint's
    // distance, and add the distance to the edge
  }

  public void setScore(int newScore) {
    score = newScore;
  }

  public int getScore() {
    return new Integer(score);
  }

  public List<Waypoint> getWaypoints() {
    return pathPoints;
  }

  @Override
  public int compareTo(Path o) {
    if (score < o.getScore()) {
      return -1;
    } else if (score == o.getScore()) {
      return 0;
    } else {
      return 1;
    }
  }

}
