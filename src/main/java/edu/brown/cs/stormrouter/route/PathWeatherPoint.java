package edu.brown.cs.stormrouter.route;

public class PathWeatherPoint {
  // Fields!
  private float lat;
  private float lon;
  private String icon;
  private String weatherSum;

  public PathWeatherPoint(float newLat, float newLong, String iconType,
      String newSum) {
    lat = newLat;
    lon = newLong;
    icon = iconType;
    weatherSum = newSum;
  }
}
