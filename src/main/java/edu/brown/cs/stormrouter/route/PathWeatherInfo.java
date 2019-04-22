package edu.brown.cs.stormrouter.route;

import java.util.ArrayList;
import java.util.List;

public class PathWeatherInfo {
  // Stores the offset
  private long pathStartTimeUnix;
  // Stores all weather information associated with this path
  private List<String[]> weatherData = new ArrayList<String[]>();
  // Stores score associated with this path, initialized at 0
  private int pathScore = 0;

  public PathWeatherInfo(long startTimeUnix) {
    pathStartTimeUnix = startTimeUnix;
  }

  public void addWeatherData(float pointLat, float pointLong, int weatherType,
      int scoreIncr) {
    String[] newDataPoint = new String[] {
        Float.toString(pointLat), Float.toString(pointLong),
        Integer.toString(weatherType)
    };
    weatherData.add(newDataPoint);
    pathScore += scoreIncr;
  }

  public int getScore() {
    return new Integer(pathScore);
  }

  public List<String[]> getWeatherData() {
    return new ArrayList<String[]>(weatherData);
  }

  public long getStartTime() {
    return new Long(pathStartTimeUnix);
  }

}
