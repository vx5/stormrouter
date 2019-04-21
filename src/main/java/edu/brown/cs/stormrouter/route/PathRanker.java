package edu.brown.cs.stormrouter.route;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import edu.brown.cs.stormrouter.weather.TimePoint;
import edu.brown.cs.stormrouter.weather.WeatherAPIHandler;

public class PathRanker {
  // Stores default path
  private Path defaultPath;
  // Stores all paths to be ranked
  private Queue<Path> paths = new PriorityQueue<Path>();
  // Stores information about what place-time events have had weather pulled
  private Map<String, Long> checkedWeather = new HashMap<String, Long>();
  private final float TILE_SIZE_MILES = 20;
  private final float MILES_PER_DEGREE = 69;
  private final float TILE_SIZE_DEGREES = TILE_SIZE_MILES / MILES_PER_DEGREE;
  // Stores list of indices in paths to be used for waypoints
  private List<Integer> weatherIds = new ArrayList<Integer>();
  // Stores number of points that should be checked
  private final int NUM_POINTS = 20;
  // Stores desired hour offsets to be checked, if possible
  private final int[] HR_OFFSETS = new int[] {
      -2, -1, 1, 2, 5
  };

  public PathRanker() {
  }

  public PriorityQueue<Path> bestPath(Path centerPath) throws Exception {
    // Assign the given path as default
    defaultPath = centerPath;
    paths.add(defaultPath);
    // Generates alternate paths at desired time offsets, if they are valid
    genNewPaths();
    // Determine which points whether should be pulled for
    fillIds();
    // Uses list of points generated above to score paths
    scorePaths();
    // Returns queue of Paths, ordered by start time
    return new PriorityQueue<Path>(paths);
  }

  // 1. Helper method
  private void genNewPaths() {
    // Sets the start, end time of the path that is desired
    long unixStart = defaultPath.getStartTime();
    List<Waypoint> pathPoints = defaultPath.getWaypoints();
    // Here, the time in seconds is obtained, then transformed to hours
    long unixEnd = unixStart + hrToMs(
        pathPoints.get(pathPoints.size() - 1).getTime() / (float) (60 * 60));
    // Iterates through all desired hour offsets to be checked
    for (int i = 0; i < HR_OFFSETS.length; i++) {
      // Obtains hour offset for path generation, converts to unix offset
      int hrOffset = HR_OFFSETS[i];
      long unixOffset = hrToMs(hrOffset);
      // Checks for valid time requirements, which requires that:
      // a) the would-be start time is not before now
      // b) the would-be end time is not past the 48-hour window DarkSky offers
      // weather data for
      if (System.currentTimeMillis() <= unixStart - unixOffset
          && System.currentTimeMillis() + hrToMs((float) 48.05) >= unixEnd
              + unixOffset) {
        // Instantiates new path to be generated, using new start time
        Path newPath = new Path(hrToMs(unixStart + unixOffset));
        // Iterates through all Waypoints in given path, to be replicated
        Iterator<Waypoint> oldPathPoints = defaultPath.getWaypoints()
            .iterator();
        while (oldPathPoints.hasNext()) {
          // Generate relevant copy of corresponding path point
          Waypoint newWaypoint = oldPathPoints.next().copy();
          // Transfers relevant information to newWaypoint
          newPath.addWaypoint(newWaypoint);
        }
        // Adds new path to the Queue
        paths.add(newPath);
      }
    }
  }

  // Side helper for 1
  private long hrToMs(float hr) {
    // Converts hours to milliseconds, using 60
    // for minutes, 60 for seconds, 1000 for milliseconds
    return (long) hr * 60 * 60 * 1000;
  }

  private int UnixToHrsFromNow(long unix) {
    long msFromNow = unix - System.currentTimeMillis();
    return (int) (msFromNow / 1000) / (60 * 60);
  }

  // 2. Helper method
  private void fillIds() {
    // META: Calculate standard breakdown of paths
    // Clears existing Ids, sets up with start
    ArrayList<Integer> allWeatherIds = new ArrayList<Integer>();
    allWeatherIds.add(0);
    // Check for time since last point, haversine
    List<Waypoint> pathPoints = defaultPath.getWaypoints();
    // Stores information associate with first initial chosen id
    Waypoint startPoint = pathPoints.get(0);
    float[] startCoords = startPoint.getCoords();
    pathPoints.add(startPoint);
    String storeString = "0,0";
    checkedWeather.put(storeString, startPoint.getTime());
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
      } else if (iterUnixTime - checkedWeather.get(storeString) > 30 * 60
          * 1000) {
        newLoad = true;
      }
      if (newLoad) {
        allWeatherIds.add(i);
      }
    }
    // META: Use NUM_Points to reduce number of points
    int skipNum = (int) Math.ceil(allWeatherIds.size() / (double) NUM_POINTS);
    // Iterates through all points to add to the weatherIds
    // Iterates using skip number
    for (int i = 0; i < allWeatherIds.size(); i += skipNum) {
      // Adds requisite number to weatherIds
      weatherIds.add(i);
    }
  }

  private int score(Waypoint consider, TimePoint weather) {
    // Initializes variable to count score
    int pointScore = 0;
    // Initializes variable to keep track of most significant type of weather
    // 0 represents no significant weather
    int weatherType = 0;
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
          weatherType = 1;
        } else {
          pointScore += 40;
          weatherType = 1;
        }
        // Checks for case of sleet or snow
      } else {
        // Checks for different amounts of precipitation, awards points
        // accordingly
        if (precipIntensity < 0.05) {
          pointScore += 5;
        } else if (precipIntensity < 0.15) {
          pointScore += 25;
          weatherType = 2;
        } else {
          pointScore += 60;
          weatherType = 2;
        }
      }
    }
    // Checks for case of very high temperature
    if (weather.getTemperature() > 105) {
      pointScore += 30;
      weatherType = 3;
    }
    // Checks for case of low visibility
    double visibility = weather.getVisibility();
    if (visibility < 0.25) {
      weatherType = 4;
      pointScore += 30;
    } else if (visibility < 0.62) {
      pointScore += 15;
    }
    // Checks for case of high wind speed / wind gust
    double windSpeed = weather.getWindSpeed();
    double windGust = weather.getWindGust();
    if (windGust > 65) {
      pointScore += 100;
      weatherType = 5;
    } else if (windGust > 58 || windSpeed > 40) {
      pointScore += 40;
    } else if (windGust > 45 || windSpeed > 30) {
      pointScore += 15;
    }
    // Now, performs end tasks to:
    // a) Give appropriate weather type
    // b) Increment path score appropriately
    consider.giveWeather(0);
    return pointScore;
  }

  // 3. Helper method
  private void scorePaths() throws Exception {
    // Stores array of all paths
    Path[] localPaths = (Path[]) paths.toArray();
    // Iterates through all weather indices
    for (int i = 0; i < weatherIds.size(); i++) {
      // Gets index, relevant coordinates, and relevant hourly weather
      int currId = weatherIds.get(i);
      float[] currCoords = defaultPath.getWaypoints().get(i).getCoords();
      TimePoint[] hrWeathers = WeatherAPIHandler
          .getWeather(currCoords[0], currCoords[1]).getHourly().getData();
      // Iterates through all paths, and scores the appropriate points
      for (int j = 0; j < localPaths.length; j++) {
        // Obtains specific point to be scored
        Path toEdit = localPaths[i];
        Waypoint consider = toEdit.getWaypoints().get(j);
        // Scores point given conditions at time it will be reached
        int hrsFromNow = UnixToHrsFromNow(consider.getTime());
        toEdit.incrScore(score(consider, hrWeathers[hrsFromNow]));
      }
    }
  }
}
