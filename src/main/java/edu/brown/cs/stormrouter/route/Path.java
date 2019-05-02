package edu.brown.cs.stormrouter.route;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vx5
 *
 *         Class which represents the path between the desired points using a
 *         series of point locations.
 */
public class Path {
  // Stores list of all points in this path
  private List<Pathpoint> pathPoints = new ArrayList<Pathpoint>();
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
   * Adds a given Pathpoint to the Path, at its current end.
   * 
   * @param endPoint Pathpoint to be added to end of path
   */
  void addPathpoint(Pathpoint endPoint) {
    pathPoints.add(endPoint);
  }

  /**
   * Returns all Pathpoints stored in this path.
   * 
   * @return List of all Pathpoints stored in the path
   */
  List<Pathpoint> getPathpoints() {
    return new ArrayList<Pathpoint>(pathPoints);
  }

}
