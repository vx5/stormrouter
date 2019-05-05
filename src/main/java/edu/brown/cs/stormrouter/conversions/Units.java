package edu.brown.cs.stormrouter.conversions;

/**
 * @author vx5
 *         <p>
 *         Class which stores both constants and methods relevant to inter-unit
 *         conversion. Emphasis on time and distance conversion constants, but
 *         includes a few methods for common, more complex conversions.
 */
public final class Units {
  // Stores all key unit conversion amounts
  public static final int MS_PER_S = 1000;
  public static final double S_PER_MS = 1 / (double) MS_PER_S;
  public static final int S_PER_MIN = 60;
  public static final double MIN_PER_S = 1 / (double) S_PER_MIN;
  public static final int MIN_PER_HR = 60;
  public static final double HR_PER_MIN = 1 / (double) MIN_PER_HR;
  public static final double MILES_PER_DEGREE = 69;
  public static final double DEGREES_PER_MILE = 1 / MILES_PER_DEGREE;
  public static final double METERS_PER_MILE = 1609.34;
  public static final double MILES_PER_METER = 1 / METERS_PER_MILE;
  public static final double MPH_TO_METERS_PER_S = 0.44704;
  public static final double METERS_PER_S_TO_MPH = 1 / MPH_TO_METERS_PER_S;
  public static final double IN_TO_MM = 25.4;
  public static final double MM_TO_IN = 1 / IN_TO_MM;

  private Units() {
  }

  /**
   * Convert hours to seconds.
   * 
   * @param hr input number of hours
   * @return number of seconds
   */
  public static long hrToS(double hr) {
    // Converts hours to milliseconds, using 60
    // for minutes, 60 for seconds, 1000 for milliseconds
    return (long) (hr * MIN_PER_HR * S_PER_MIN);
  }

  /**
   * Converts unix time to number of hours, in whole (truncated to integers),
   * from time method is called.
   * 
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
