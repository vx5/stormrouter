package edu.brown.cs.stormrouter.route;

import java.util.ArrayList;
import java.util.List;

public class Path implements Comparable<Path> {
  // Stores list of all waypoints in this path
  private List<Waypoint> pathPoints = new ArrayList<Waypoint>();
  // Stores current score of this path
  private int score = 0;
  // Stores offset, in UNIX time, of this path from the original time
  private long startTime;

  // Constructor only considers offset
  public Path(long startTime) {
    this.startTime = startTime;
  }

  public long getStartTime() {
    return new Long(startTime);
  }

  public void addWaypoint(Waypoint x) {
    pathPoints.add(x);
  }

  // MIGHT be unnecessary
  public void fillDurations() {
    // TODO:
    // 1. Iterate through all pathPoints
    for (int i = 0; i < pathPoints.size(); i++) {

    }
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
