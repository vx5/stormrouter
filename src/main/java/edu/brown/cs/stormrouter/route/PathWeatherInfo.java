package edu.brown.cs.stormrouter.route;

import java.util.ArrayList;
import java.util.Iterator;
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
   * Converts all information stored in this PathWeatherInfo instance into a
   * String array.
   * 
   * @return a String[][] type. Given that the underlying object has weather
   *         data for n points, String[0] will contain PathWeatherInfo details,
   *         with the Unix start time at [0][0] and the score at [0][1].
   *         String[1] through String[n] will contain information about the
   *         weather points, such that, for every i between 1 and n, inclusive,
   *         [i][0] represents the latitude, [i][1] represents the longitude,
   *         and [i][2] represents the type of weather, mapped from integers
   *         such that 0 through 5 represent plain weather, rain, snow, heat,
   *         fog, and wind, respectively
   */
  public String[][] toStringArray() {
    // Creates String array to be returned
    String[][] strArray = new String[weatherData.size() + 1][3];
    // Fills the first element with the start time and score
    strArray[0][0] = Long.toString(pathStartTimeUnix);
    strArray[0][1] = Integer.toString(pathScore);
    // Iterates through and populates strArray
    int strId = 1;
    Iterator<String[]> weatherIt = weatherData.iterator();
    while (weatherIt.hasNext()) {
      // Assigns array to proper place
      strArray[strId] = weatherIt.next();
      // Increments assignment index
      strId++;
    }
    // Returns String array
    return strArray;
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
