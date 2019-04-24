package edu.brown.cs.stormrouter.route;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vx5
 * 
 *         Class that stores all GUI-relevant information about a time index for
 *         the relevant path, including weather score, and where weather data
 *         must be used.
 */
public class PathWeatherInfo {
  // Stores the offset
  private long pathStartTimeUnix;
  // Stores all weather information associated with this path
  private List<String[]> weatherData = new ArrayList<String[]>();
  // Stores score associated with this path, initialized at 0
  private int pathScore = 0;

  /**
   * Constructor that takes in unix start time.
   * 
   * @param startTimeUnix Long unix start time of corresponding path
   */
  public PathWeatherInfo(long startTimeUnix) {
    pathStartTimeUnix = startTimeUnix;
  }

  /**
   * Adds a point with weather data to the weather information set.
   * 
   * @param pointLat    latitude of given point
   * @param pointLong   longitude of given point
   * @param weatherType integer representing type of weather to be displayed
   * @param scoreIncr   integer representing score of the given weather point
   */
  public void addWeatherData(float pointLat, float pointLong, int weatherType,
      int scoreIncr) {
    // Constructs array with details
    String[] newDataPoint = new String[] {
        Float.toString(pointLat), Float.toString(pointLong),
        Integer.toString(weatherType)
    };
    // Adds details to set
    weatherData.add(newDataPoint);
    // Increments score
    pathScore += scoreIncr;
  }

  /**
   * Return relevant path's score.
   * 
   * @return integer form of relevant path's score
   */
  public int getScore() {
    return new Integer(pathScore);
  }

  /**
   * Returns unix time at which this path starts.
   * 
   * @return Long form of unix time at which this path starts
   */
  public long getStartTime() {
    return new Long(pathStartTimeUnix);
  }

}
