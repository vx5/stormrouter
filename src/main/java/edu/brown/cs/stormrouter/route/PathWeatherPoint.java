package edu.brown.cs.stormrouter.route;

public class PathWeatherPoint {
  // Fields!
  private float lat;
  private float lon;
  private int weatherType;
  private String weatherSum;

  public PathWeatherPoint(float newLat, float newLong, int newType,
      String newSum) {
    lat = newLat;
    lon = newLong;
    weatherType = newType;
    weatherSum = newSum;
  }
}
