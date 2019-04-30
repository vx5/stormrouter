package edu.brown.cs.stormrouter.route;

public class PathWeatherPoint {
  // Fields!
  private float pointLat;
  private float pointLong;
  private int weatherType;
  private String weatherSum;

  public PathWeatherPoint(float newLat, float newLong, int newType,
      String newSum) {
    pointLat = newLat;
    pointLong = newLong;
    weatherType = newType;
    weatherSum = newSum;
  }
}
