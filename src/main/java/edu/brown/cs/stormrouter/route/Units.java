package edu.brown.cs.stormrouter.route;

/**
 * @author vx5
 *
 *         Class which stores both constants and methods relevant to interunit
 *         conversion.
 */
final class Units {
  // Stores all key unit conversion amounts
  static final int MS_PER_S = 1000;
  static final float S_PER_MS = 1 / (float) MS_PER_S;
  static final int S_PER_MIN = 60;
  static final int MIN_PER_HR = 60;
  static final float MILES_PER_DEGREE = 69;
  static final float DEGREES_PER_MILE = 1 / MILES_PER_DEGREE;

  /**
   * Convert hours to seconds.
   * 
   * @param hr input number of hours
   * @return number of seconds
   */
  static long hrToS(double hr) {
    // Converts hours to milliseconds, using 60
    // for minutes, 60 for seconds, 1000 for milliseconds
    return (long) hr * MIN_PER_HR * S_PER_MIN;
  }

  /**
   * Converts unix time to number of hours, in whole (truncated to integers),
   * from time method is called.
   * 
   * @param unix Long unix time
   * @return number of hours from time method is called
   */
  static int UnixToHrsFromNow(long unix) {
    // Calculates number of milliseconds from now
    long sFromNow = (long) (unix - (System.currentTimeMillis() * S_PER_MS));
    // Performs dimensional analysis conversions, truncation
    return (int) sFromNow / (S_PER_MIN * MIN_PER_HR);
  }

}
