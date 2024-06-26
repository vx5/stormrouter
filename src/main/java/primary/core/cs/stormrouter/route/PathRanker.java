package primary.core.cs.stormrouter.route;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import primary.core.cs.stormrouter.conversions.Units;
import primary.core.cs.stormrouter.weather.TimePoint;
import primary.core.cs.stormrouter.weather.WSet;
import primary.core.cs.stormrouter.weather.WeatherAPIHandler;

/**
 * @author vx5
 *         <p>
 *         Class which handles core functionality of deciding what time offset
 *         yields the most weather-sound path.
 */
public final class PathRanker {
  // Stores default path
  private static Path defaultPath;
  // Stores path long time zone offset, in seconds
  private static long tzOffset;
  // Stores information of all valid start time indices
  private static Map<String, PathWeatherInfo> diffTimesWeather = new HashMap<>();
  // Stores information about what place-time events have had weather pulled
  private static Map<String, Long> checkedWeather;
  private static final double TILE_SIZE_MILES = 20;
  private static final double TILE_SIZE_DEGREES = TILE_SIZE_MILES
      * Units.DEGREES_PER_MILE;
  // Stores list of indices in paths to be used for waypoints
  private static List<Integer> weatherIds;
  // Stores number of points that should be checked
  private static final int NUM_POINTS = 4;
  // Stores desired hour offsets to be checked, if possible
  private static final int[] HR_OFFSETS = new int[] {
      -12, -8, -5, -2, -1, 1, 2, 5, 8, 12
  };

  private PathRanker() {
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
  public static Map<String, Object> bestPath(Path centerPath) throws Exception {
    // Resets storage variables
    diffTimesWeather = new HashMap<>();
    checkedWeather = new HashMap<>();
    weatherIds = new ArrayList<>();
    // Assign the given path as default
    defaultPath = centerPath;
    tzOffset = centerPath.getOffset();
    // Generates alternate paths at desired time offsets, if they are valid
    genNewPaths();
    // Determine which points whether should be pulled for
    fillIds();
    // Uses list of points generated above to score paths
    scorePaths();
    // Iterates through paths, checks for which offset has the best score
    Set<String> chosenHrOffsets = diffTimesWeather.keySet();
    String bestPathKey = "";
    int bestPathScore = Integer.MAX_VALUE;
    for (String offset : chosenHrOffsets) {
      int offsetScore = diffTimesWeather.get(offset).getScore();
      if (offsetScore < bestPathScore) {
        bestPathScore = offsetScore;
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

  private static void genNewPaths() {
    // Sets the start, end time of the path that is desired
    long unixStart = defaultPath.getStartTime();
    List<Pathpoint> pathPoints = defaultPath.getPathpoints();
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
      long pathStartTime = unixStart + unixOffset;
      long pathEndTime = unixEnd + unixOffset;
      if (System.currentTimeMillis() * Units.S_PER_MS <= pathStartTime
          - tzOffset
          && System.currentTimeMillis() * Units.S_PER_MS
              + Units.hrToS(48.05) >= pathEndTime - tzOffset) {
        diffTimesWeather.put(Integer.toString(hrOffset),
            new PathWeatherInfo(unixStart + unixOffset));
      }
    }
  }

  private static void fillIds() {
    // Calculate standard breakdown of paths
    // Clears existing Ids, sets up with start
    ArrayList<Integer> allWeatherIds = new ArrayList<Integer>();
    allWeatherIds.add(0);
    // Acquires list of Waypoints of the default path
    List<Pathpoint> pathPoints = defaultPath.getPathpoints();
    // Stores information associate with first initial chosen id
    Pathpoint startPoint = pathPoints.get(0);
    String storeString = "0,0";
    checkedWeather.put(storeString, startPoint.getTime());
    // Obtains coordinates for use in tile calculation in loop
    double[] startCoords = startPoint.getCoords();
    // Iterates through pathPoints to check for default paths
    for (int i = 1; i < pathPoints.size(); i++) {
      // Obtain the relevant pathPoint
      Pathpoint iterPoint = pathPoints.get(i);
      // Calculates all storage information for this event
      // Gets the time block
      long iterUnixTime = iterPoint.getTime();
      // Gets the tile coordinates
      double[] ptCoords = iterPoint.getCoords();
      int tileX = (int) Math
          .round((ptCoords[1] - startCoords[1]) / TILE_SIZE_DEGREES);
      int tileY = (int) Math
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
          .getDistToReach();
      double sectionDistance = Math
          .ceil(totalPathDistance / (double) NUM_POINTS);
      // Stores the distance required to reach the next point
      double minDistToNext = 0;
      // Iterates through allWeatherIds based on certain journey durations
      double lastDistChosen = 0;
      for (int coord : allWeatherIds) {
        // Calculates distance from start for current point
        double distFromStart = pathPoints.get(coord).getDistToReach();
        // Calculates distance moved from last chosen point
        double distMoved = distFromStart - lastDistChosen;
        // Checks for sufficient distance for using a new path
        if (distMoved > minDistToNext) {
          // Adds index
          weatherIds.add(coord);
          // Moves marker for last point
          lastDistChosen = distFromStart;
          // Calculates distance to the next point
          minDistToNext = Math.max(
              sectionDistance - (distMoved - minDistToNext),
              TILE_SIZE_MILES * Units.METERS_PER_MILE);
        }
      }
    } else {
      weatherIds = new ArrayList<Integer>(allWeatherIds);
    }
  }

  private static void scorePaths() throws Exception {
    // Stores array over all PathWeatherInfos
    Set<String> chosenHrOffsets = diffTimesWeather.keySet();
    // Iterates through all weather indices
    for (int currId : weatherIds) {
      // Gets relevant point in default path
      Pathpoint currPoint = defaultPath.getPathpoints().get(currId);
      long timeReached = currPoint.getTime();
      double[] currCoords = currPoint.getCoords();
      TimePoint[] hrWeathers = WeatherAPIHandler
          .getWeather(currCoords[0], currCoords[1]).getHourly().getData();
      // Iterates through all paths, and scores the appropriate points
      for (String chosenHrOffset : chosenHrOffsets) {
        // Stores time from now for iteration
        int hrOffset = Integer.parseInt(chosenHrOffset);
        int hrsFromNowSys = Units.unixToHrsFromNow(timeReached - tzOffset)
            + hrOffset;
        long trueTimeReached = timeReached + Units.hrToS(hrOffset);
        // Stores relevant TimePoint
        TimePoint hrWeather = hrWeathers[hrsFromNowSys];
        // Scores TimePoint
        int ptScore = WSet.scoreWeather(hrWeather);
        // Obtains specific time to be scored
        PathWeatherInfo toModify = diffTimesWeather.get(chosenHrOffset);
        // Adds weather information to relevant PathWeatherInfo
        toModify.addWeatherData(currCoords[0], currCoords[1],
            hrWeather.getIcon(), hrWeather.getSummary(), ptScore,
            trueTimeReached);
      }
    }
  }
}
