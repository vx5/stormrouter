package edu.brown.cs.stormrouter.route;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author vx5
 *
 *         Class that stores all information about a point for which weather was
 *         requested, for a given time at which the point is reached. Its
 *         private instance fields, "lat", "lon", "icon", and "weatherSum" offer
 *         information that can be accessed when the object is passed through
 *         JSON.
 */
public class PathWeatherPoint {
  // Private instance fields to be accessed from inside JSON
  private float lat;
  private float lon;
  private String icon;
  private String weatherSum;

  /**
   * Constructor that takes in position, information about icon and String to be
   * displayed, and time reached.
   * 
   * @param newLat    point latitude
   * @param newLong   point longitude
   * @param iconType  String representing type of icon that should be used to
   *                  represent this point
   * @param newSum    Summary of weather information and time at this point
   * @param pointTime long Unix time at which point is reached
   */
  public PathWeatherPoint(float newLat, float newLong, String iconType,
      String newSum, long pointTime) {
    // Sets requisite instance fields
    lat = newLat;
    lon = newLong;
    icon = iconType;
    // Determines String representing time window to be displayed
    weatherSum = "Expected weather from ";
    // Calculates unix time hour from start time
    long plusHrTime = pointTime + Units.hrToS(1);
    // Creates Date objects for start, end times
    Date start = new Date(pointTime * 1000L);
    Date end = new Date(plusHrTime * 1000L);
    // Generates formatter to produce appropriate date
    SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMM d 'at' h a");
    // Adds requisite date times
    weatherSum += formatter.format(start) + " to " + formatter.format(end);
    // Adds final weather description to String
    weatherSum += " at this point's time zone:\n" + newSum;
  }
}
