package edu.brown.cs.stormrouter.route;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.brown.cs.stormrouter.weather.TimePoint;
import edu.brown.cs.stormrouter.weather.WeatherAPIHandler;

/**
 * @author vx5
 * 
 *         Class which handles core functionality of deciding what time offset
 *         yields the most weather-sound path.
 */
public class PathRanker {
  // Stores default path
  private Path defaultPath;
  // Stores information of all valid start time indices
  private Map<String, PathWeatherInfo> diffTimesWeather = new HashMap<String, PathWeatherInfo>();
  // Stores information about what place-time events have had weather pulled
  private Map<String, Long> checkedWeather = new HashMap<String, Long>();
  private final float TILE_SIZE_MILES = 20;
  private final float TILE_SIZE_DEGREES = TILE_SIZE_MILES
      * Units.DEGREES_PER_MILE;
  // Stores list of indices in paths to be used for waypoints
  private List<Integer> weatherIds = new ArrayList<Integer>();
  // Stores number of points that should be checked
  private final int NUM_POINTS = 4;
  // Stores desired hour offsets to be checked, if possible
  private final int[] HR_OFFSETS = new int[] {
      -2, -1, 1, 2, 5
  };

  // Enum to represent different weather types for assignment in weather data
  private enum WEATHER_TYPE {
    PLAIN, RAIN, SNOW, HEAT, FOG, WIND
  }

  /**
   * Empty constructor.
   */
  public PathRanker() {
  }

  /**
   * Returns map of weather information objects, of type PathWeatherInfo, which
   * include, for all valid paths (including those at alternate times), the
   * weather points, and type of weather they represent.
   * 
   * @param centerPath Path object to be used as default Path
   * @return Set of PathWeatherInfo, one for each valid paths
   * @throws Exception if there is error in API call
   */
  public Map<String, Object> bestPath(Path centerPath) throws Exception {
    // Assign the given path as default
    defaultPath = centerPath;
    // Generates alternate paths at desired time offsets, if they are valid
    genNewPaths();
    // Determine which points whether should be pulled for
    fillIds();
    // Uses list of points generated above to score paths
    scorePaths();
    // Iterates through paths, checks for best score
    Set<String> chosenHrOffsets = diffTimesWeather.keySet();
    String bestPathKey = "";
    int bestPathScore = Integer.MAX_VALUE;
    for (String offset : chosenHrOffsets) {
      PathWeatherInfo timePath = diffTimesWeather.get(offset);
      if (timePath.getScore() < bestPathScore) {
        bestPathKey = offset;
      }
    }
    // Sets best
    Map<String, Object> finalMap = new HashMap<String, Object>(
        diffTimesWeather);
    finalMap.put("best", bestPathKey);
    // Returns queue of Paths, ordered by start time
    return finalMap;
  }

  private void genNewPaths() {
    // Sets the start, end time of the path that is desired
    long unixStart = defaultPath.getStartTime();
    List<Waypoint> pathPoints = defaultPath.getWaypoints();
    // Adds initial path to map
    diffTimesWeather.put("0", new PathWeatherInfo(unixStart));
    // Here, the time in seconds is obtained, then transformed to hours
    // TODO: Make sure pathPoints.size() > 0
    long unixEnd = pathPoints.get(pathPoints.size() - 1).getTime();
    // Iterates through all desired hour offsets to be checked
    for (Integer hrOffset : HR_OFFSETS) {
      long unixOffset = Units.hrToS(hrOffset);
      // Checks for valid time requirements, which requires that:
      // a) the would-be start time is not before now
      // b) the would-be end time is not past the 48-hour window DarkSky offers
      // weather data for
      long pathStartTime = unixStart - unixOffset;
      long pathEndTime = unixEnd + unixOffset;
      if (System.currentTimeMillis() * Units.S_PER_MS <= pathStartTime
          && System.currentTimeMillis() * Units.S_PER_MS
              + Units.hrToS(48.05) >= pathEndTime) {
        diffTimesWeather.put(Integer.toString(hrOffset),
            new PathWeatherInfo(unixStart + unixOffset));
      }
    }
  }

  private void fillIds() {
    // Calculate standard breakdown of paths
    // Clears existing Ids, sets up with start
    ArrayList<Integer> allWeatherIds = new ArrayList<Integer>();
    allWeatherIds.add(0);
    // Acquires list of Waypoints of the default path
    List<Waypoint> pathPoints = defaultPath.getWaypoints();
    // Stores information associate with first initial chosen id
    Waypoint startPoint = pathPoints.get(0);
    String storeString = "0,0";
    checkedWeather.put(storeString, startPoint.getTime());
    // Obtains coordinates for use in tile calculation in loop
    float[] startCoords = startPoint.getCoords();
    // Iterates through pathPoints to check for default paths
    for (int i = 1; i < pathPoints.size(); i++) {
      // Obtain the relevant pathPoint
      Waypoint iterPoint = pathPoints.get(i);
      // Calculates all storage information for this event
      // Gets the time block
      long iterUnixTime = iterPoint.getTime();
      // Gets the tile coordinates
      float[] ptCoords = iterPoint.getCoords();
      int tileX = Math
          .round((ptCoords[1] - startCoords[1]) / TILE_SIZE_DEGREES);
      int tileY = Math
          .round((ptCoords[0] - startCoords[0]) / TILE_SIZE_DEGREES);
      storeString = tileX + "," + tileY;
      // Checks if criteria for new point or met, if so, adds
      boolean newLoad = false;
      if (!checkedWeather.containsKey(storeString)) {
        newLoad = true;
      } else if (iterUnixTime - checkedWeather.get(storeString) > 30 * 60) {
        newLoad = true;
      }
      if (newLoad) {
        // Adds relevant index
        allWeatherIds.add(i);
        // Adds to the set to represent that weather was checked
        checkedWeather.put(storeString, iterUnixTime);
      }
    }
    // Checks for case where reduction is necessary
    if (allWeatherIds.size() > NUM_POINTS) {
      // Uses NUM_POINTS to identify time that should be spent in each segment
      double totalPathDistance = pathPoints.get(pathPoints.size() - 1)
          .getDistToReach() - pathPoints.get(0).getDistToReach();
      double sectionDistance = Math
          .ceil(totalPathDistance / (double) NUM_POINTS);
      // Stores the distance required to reach the next point
      double minDistToNext = new Double(sectionDistance);
      // Iterates through allWeatherIds based on certain journey durations
      double distAtLast = Double.NEGATIVE_INFINITY;
      for (int i = 0; i < allWeatherIds.size(); i++) {
        // Stores actual coordinate
        int coord = allWeatherIds.get(i);
        // Calculates distance from start for current point
        double distFromStart = pathPoints.get(coord).getDistToReach();
        // Calculates distance moved from last chosen point
        double distMoved = distFromStart - distAtLast;
        // Checks for sufficient distance for using a new path
        if (distMoved > minDistToNext) {
          // Adds index
          weatherIds.add(coord);
          // Moves marker for last point
          distAtLast = distFromStart;
          // Calculates distance to the next point
          minDistToNext = sectionDistance - (distMoved - sectionDistance);
        }
      }
    } else {
      weatherIds = new ArrayList<Integer>(allWeatherIds);
    }
  }

  private void score(float pointLat, float pointLong, PathWeatherInfo pathInfo,
      TimePoint weather) {
    // Initializes variable to count score
    int pointScore = 0;
    // Initializes variable to keep track of most significant type of weather
    // 0 represents no significant weather
    int weatherType = WEATHER_TYPE.PLAIN.ordinal();
    // Checks for case of precipitation
    double precipIntensity = weather.getPrecipIntensity();
    if (precipIntensity > 0.01) {
      // Checks for case of rain
      if (weather.getPrecipType().equals("rain")) {
        // Checks for different amounts of precipitation, awards points
        // accordingly
        if (precipIntensity < 0.1) {
          pointScore += 5;
        } else if (precipIntensity < 0.3) {
          pointScore += 15;
          weatherType = WEATHER_TYPE.RAIN.ordinal();
        } else {
          pointScore += 40;
          weatherType = WEATHER_TYPE.RAIN.ordinal();
        }
        // Checks for case of sleet or snow
      } else {
        // Checks for different amounts of precipitation, awards points
        // accordingly
        if (precipIntensity < 0.05) {
          pointScore += 5;
        } else if (precipIntensity < 0.15) {
          pointScore += 25;
          weatherType = WEATHER_TYPE.SNOW.ordinal();
        } else {
          pointScore += 60;
          weatherType = WEATHER_TYPE.SNOW.ordinal();
        }
      }
    }
    // Checks for case of very high temperature
    if (weather.getTemperature() > 105) {
      pointScore += 30;
      weatherType = WEATHER_TYPE.HEAT.ordinal();
    }
    // Checks for case of low visibility
    double visibility = weather.getVisibility();
    if (visibility < 0.25) {
      weatherType = WEATHER_TYPE.FOG.ordinal();
      pointScore += 30;
    } else if (visibility < 0.62) {
      pointScore += 15;
    }
    // Checks for case of high wind speed / wind gust
    double windSpeed = weather.getWindSpeed();
    double windGust = weather.getWindGust();
    if (windGust > 65) {
      pointScore += 100;
      weatherType = WEATHER_TYPE.WIND.ordinal();
    } else if (windGust > 58 || windSpeed > 40) {
      pointScore += 40;
    } else if (windGust > 45 || windSpeed > 30) {
      pointScore += 15;
    }
    // Now, performs change to relevant pathInfo object
    pathInfo.addWeatherData(pointLat, pointLong, weatherType,
        weather.getSummary(), pointScore);
  }

  private void scorePaths() throws Exception {
    // Stores array over all PathWeatherInfos
    Set<String> chosenHrOffsets = diffTimesWeather.keySet();
    // Iterates through all weather indices
    for (int currId : weatherIds) {
      // Gets relevant point in default path
      Waypoint currPoint = defaultPath.getWaypoints().get(currId);
      long timeReached = currPoint.getTime();
      float[] currCoords = currPoint.getCoords();
      TimePoint[] hrWeathers = WeatherAPIHandler
          .getWeather(currCoords[0], currCoords[1]).getHourly().getData();
      // Stores time from now for iteration
      int hrsFromNow = Units.UnixToHrsFromNow(timeReached);
      // Stores relevant TimePoint
      TimePoint hrWeather = hrWeathers[hrsFromNow];
      // Iterates through all paths, and scores the appropriate points
      for (String chosenHrOffset : chosenHrOffsets) {
        // Obtains specific time to be scored
        PathWeatherInfo toModify = diffTimesWeather.get(chosenHrOffset);
        // Scores time index
        score(currCoords[0], currCoords[1], toModify, hrWeather);
      }
    }
  }
}
