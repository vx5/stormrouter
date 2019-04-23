package edu.brown.cs.stormrouter.route;

/**
 * @author vx5
 *
 *         Represention of a single waypoint in a path set of directions. This
 *         is used in the Path class, as paths are represented through lists of
 *         Waypoints.
 */
class Waypoint {
  // Instance variable to store key attributes of each Waypoint
  private float wayLat;
  private float wayLong;
  private long timeToReach;

  /**
   * Basic constructor that only requires, stores Waypoint's location.
   * 
   * @param newLat  latitude of corresponding waypoint
   * @param newLong longitude of corresponding waypoint
   */
  Waypoint(float newLat, float newLong) {
    // Stores latitude and longitude
    wayLat = newLat;
    wayLong = newLong;
  }

  /**
   * Returns latitude and longitude coordinates of this Waypoint.
   * 
   * @return float array containing latitude and longitude (in order)
   *         coordinates of this Waypoint
   */
  float[] getCoords() {
    // Returns float array containing coordinates
    return new float[] {
        wayLat, wayLong
    };
  }

  /**
   * Sets the milliseconds' time it takes to reach this waypoint from the
   * relevant path's start.
   * 
   * @param newTime Long time in milliseconds it takes to reach this waypoint
   *                from the relevant path's start
   */
  void setTime(long newTime) {
    timeToReach = newTime;
  }

  /**
   * Returns the time it takes to reach this waypoint from the relevant path's
   * start.
   * 
   * @return Long form of the number of milliseconds it takes to reach this
   *         waypoint from the relevant path's start
   */
  long getTime() {
    return new Long(timeToReach);
  }
}
