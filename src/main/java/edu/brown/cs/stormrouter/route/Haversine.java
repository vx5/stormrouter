package edu.brown.cs.stormrouter.route;

/**
 * @author vx5
 * @author gglass (REMEMBER TO CITE)
 *
 *         Class which stores static methods related to the calculation of
 *         Haversine distance across earth's surface.
 */
public final class Haversine {
  // Stores static variables indicating end coordinates
  private static float endLat = 0;
  private static float endLong = 0;
  // Stores radius of the earth
  private static final float EARTH_RADIUS = 6371;

  private Haversine() {
  }

  /**
   * Specifies the end coordinates to be used for future Haversine distance
   * calculations that only supply start coordinates.
   *
   * @param newLat  end latitude
   * @param newLong end longitude
   */
  public static void setHaverEnd(float newLat, float newLong) {
    endLat = newLat;
    endLong = newLong;
  }

  /**
   * Calculates Haversine distance between coordinate specified by input and
   * stored static coordinates.
   *
   * @param givenLat  latitude for starting coordinate of Haversine calculation
   * @param givenLong longitude for starting coordinate of Haversine calculation
   * @return Haversine distance in float form
   */
  public static float haverDist(float givenLat, float givenLong) {
    // Substitudes instance fields for end coordinates
    return haverDist(givenLat, givenLong, endLat, endLong);
  }

  /**
   * Calculates Haversine distance between coordinates specified by inputs.
   *
   * @param lat1  latitude for starting coordinate of Haversine calculation
   * @param long1 longitude for starting coordinate of Haversine calculation
   * @param lat2  latitude for ending coordinate of Haversine calculation
   * @param long2 longitude for ending coordinate of Haversine calculation
   * @return Haversine distance in float form
   */
  public static float haverDist(float lat1, float long1, float lat2,
      float long2) {
    double rootContents = Math.pow(Math.sin(lat1 - lat2) / 2.0, 2)
        + (Math.cos(lat1) * Math.cos(lat2)
            * Math.pow((long1 - long2) / 2.0, 2));
    return (float) (2 * EARTH_RADIUS * Math.asin(rootContents));
  }
}
