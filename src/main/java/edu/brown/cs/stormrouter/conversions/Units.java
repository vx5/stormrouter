package edu.brown.cs.stormrouter.conversions;

/**
 * @author vx5
 * <p>
 * Class which stores both constants and methods relevant to inter-unit
 * conversion. Emphasis on time and distance conversion constants, but includes
 * a few methods for common, more complex conversions.
 */
public final class Units {
  // Stores all key unit conversion amounts
  public static final int MS_PER_S = 1000;
  public static final float S_PER_MS = 1 / (float) MS_PER_S;
  public static final int S_PER_MIN = 60;
  public static final float MIN_PER_S = 1 / (float) S_PER_MIN;
  public static final int MIN_PER_HR = 60;
  public static final float HR_PER_MIN = 1 / (float) MIN_PER_HR;
  public static final float MILES_PER_DEGREE = 69;
  public static final float DEGREES_PER_MILE = 1 / MILES_PER_DEGREE;

  private Units() {
  }

  /**
   * Convert hours to seconds.
   * @param hr input number of hours
   * @return number of seconds
   */
  public static long hrToS(double hr) {
    // Converts hours to milliseconds, using 60
    // for minutes, 60 for seconds, 1000 for milliseconds
    return (long) hr * MIN_PER_HR * S_PER_MIN;
  }

  /**
   * Converts unix time to number of hours, in whole (truncated to integers),
   * from time method is called.
   * @param unix Long unix time
   * @return number of hours from time method is called
   */
  public static int unixToHrsFromNow(long unix) {
    // Calculates number of milliseconds from now
    long sFromNow = (long) (unix - (System.currentTimeMillis() * S_PER_MS));
    // Performs dimensional analysis conversions, truncation
    return (int) sFromNow / (S_PER_MIN * MIN_PER_HR);
  }

}
