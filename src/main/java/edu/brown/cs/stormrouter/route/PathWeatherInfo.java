package edu.brown.cs.stormrouter.route;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vx5
 * <p>
 * Class that stores all GUI-relevant information about a time index for the
 * relevant path, including weather score, and where weather data must be used.
 */
public class PathWeatherInfo {
  // Stores the offset
  private long pathStartTimeUnix;
  // Stores all weather information associated with this path
  // TODO: shouldn't weatherData be used?
  private List<PathWeatherPoint> weatherData = new ArrayList<>();
  // Stores score associated with this path, initialized at 0
  private int pathScore = 0;

  /**
   * Constructor that takes in unix start time.
   * @param startTimeUnix Long unix start time of corresponding path
   */
  public PathWeatherInfo(long startTimeUnix) {
    pathStartTimeUnix = startTimeUnix;
  }

  /**
   * Adds a point with weather data to the weather information set.
   * @param pointLat    latitude of given point
   * @param pointLong   longitude of given point
   * @param weatherType Stringr representing type of weather to be displayed
   * @param weatherSum  String summary of weather occurring at this point
   * @param scoreIncr   integer representing score of the given weather point
   * @param pointTime   Unix time at which point is reached
   */
  public void addWeatherData(float pointLat, float pointLong,
      String weatherType, String weatherSum, int scoreIncr, long pointTime) {
    // Adds details to set
    weatherData.add(new PathWeatherPoint(pointLat, pointLong, weatherType,
        weatherSum, pointTime));
    // Increments score
    pathScore += scoreIncr;
  }

  /**
   * Return relevant path's score.
   * @return integer form of relevant path's score
   */
  public int getScore() {
    return pathScore;
  }

  /**
   * Returns unix time at which this path starts.
   * @return Long form of unix time at which this path starts
   */
  public long getStartTime() {
    return pathStartTimeUnix;
  }

}
