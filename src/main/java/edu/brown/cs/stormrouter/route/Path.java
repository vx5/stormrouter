package edu.brown.cs.stormrouter.route;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vx5
 * <p>
 * Class which represents the path between the desired points using a series of
 * point locations.
 */
public class Path {
  // Stores list of all points in this path
  private List<Pathpoint> pathPoints = new ArrayList<Pathpoint>();
  // Stores start time of path in local time, in Unix
  private long startTime;
  // Stores Unix offset from system time
  private long offset;

  /**
   * Constructor which only processes the start time in unix terms.
   * @param startTime Long representation of Unix time at which given path
   *                  starts, in seconds
   * @param offset    Long representation of Unix time offset between path start
   *                  time zone and system time zone, in seconds
   */
  Path(long startTime, long offset) {
    this.startTime = startTime;
    this.offset = offset;
  }

  /**
   * Returns the Unix time at which the path is meant to start.
   * @return Long representation of time at which path starts, in unix terms
   */
  long getStartTime() {
    return new Long(startTime);
  }

  /**
   * Returns Unix offset of path from system time.
   * @return Unix offset of path from system time in seconds
   */
  long getOffset() {
    return new Long(offset);
  }

  /**
   * Adds a given Pathpoint to the Path, at its current end.
   * @param endPoint Pathpoint to be added to end of path
   */
  void addPathpoint(Pathpoint endPoint) {
    pathPoints.add(endPoint);
  }

  /**
   * Returns all Pathpoints stored in this path.
   * @return List of all Pathpoints stored in the path
   */
  List<Pathpoint> getPathpoints() {
    return new ArrayList<Pathpoint>(pathPoints);
  }

}
