package edu.brown.cs.stormrouter.route;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vx5
 *
 *         Class which represents the path between the desired points using a
 *         series of point locations.
 */
class Path {
  // Stores list of all waypoints in this path
  private List<Waypoint> pathPoints = new ArrayList<Waypoint>();
  // Stores offset, in UNIX time, of this path from the original time
  private long startTime;

  /**
   * Constructor which only processes the start time in unix terms.
   * 
   * @param startTime Long representation of time at which given path starts, in
   *                  unix terms
   */
  Path(long startTime) {
    this.startTime = startTime;
  }

  /**
   * Returns the unix time at which the path is meant to start.
   * 
   * @return Long representation of time at which path starts, in unix terms
   */
  long getStartTime() {
    return new Long(startTime);
  }

  /**
   * Adds a given Waypoint to the Path, at its current end.
   * 
   * @param endPoint Waypoint to be added to end of path
   */
  void addWaypoint(Waypoint endPoint) {
    pathPoints.add(endPoint);
  }

  /**
   * Returns all Waypoints stored in this path.
   * 
   * @return List of all Waypoints stored in the path
   */
  List<Waypoint> getWaypoints() {
    return new ArrayList<Waypoint>(pathPoints);
  }

}
