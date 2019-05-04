package edu.brown.cs.stormrouter.conversions;

import java.util.Date;
import java.util.TimeZone;

import edu.brown.cs.stormrouter.weather.WeatherAPIHandler;

public final class TimeZoneOps {

  private TimeZoneOps() {
  }

  public static long getCurrentMsAhead(long unixTimeThere, double ptLat,
      double ptLong) throws Exception {
    // META: Checks that entered start time is valid in local time at start
    // location
    // Obtains time zone using API
    String timeZone = WeatherAPIHandler.getWeather(ptLat, ptLong).getTimezone();
    // Sets up time zones for start location and current time zone
    TimeZone localZone = TimeZone.getTimeZone(timeZone);
    TimeZone thisZone = TimeZone.getDefault();
    // Stores raw offsets to standard time
    long localZoneOffset = localZone.getRawOffset();
    long thisZoneOffset = thisZone.getRawOffset();
    // Checks for daylight savings, makes appropriate adjustments if necessary
    if (localZone.inDaylightTime(new Date(unixTimeThere * 1000L))) {
      localZoneOffset += localZone.getDSTSavings();
    }
    if (thisZone.inDaylightTime(new Date(System.currentTimeMillis()))) {
      thisZoneOffset += thisZone.getDSTSavings();
    }
    // Return millisecond difference in start time
    return localZoneOffset - thisZoneOffset;
  }

}
