package edu.brown.cs.stormrouter.route;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PathWeatherPoint {
  // Fields!
  private float lat;
  private float lon;
  private String icon;
  private String weatherSum;

  public PathWeatherPoint(float newLat, float newLong, String iconType,
      String newSum, long pointTime) {
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
