package edu.brown.cs.stormrouter.route;

/**
 * @author vx5
 *
 *         Represention of a single waypoint in a path set of directions. This
 *         is used in the Path class, as paths are represented through lists of
 *         Waypoints.
 */
class Waypoint {
  // Instance variable to store key attributes of all waypoints
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

  float[] getCoords() {
    // Returns float array containing coordinates
    return new float[] {
        wayLat, wayLong
    };
  }

  void setTime(long newTime) {
    timeToReach = newTime;
  }

  long getTime() {
    return new Long(timeToReach);
  }

}
