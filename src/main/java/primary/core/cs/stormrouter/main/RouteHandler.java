package primary.core.cs.stormrouter.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import primary.core.cs.stormrouter.directions.DirectionsAPIHandler;
import primary.core.cs.stormrouter.directions.LatLon;
import primary.core.cs.stormrouter.directions.Segment;
import primary.core.cs.stormrouter.route.DirectionsWrapper;
import primary.core.cs.stormrouter.route.Path;
import primary.core.cs.stormrouter.route.PathConverter;
import primary.core.cs.stormrouter.route.PathRanker;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;

public class RouteHandler implements Route {
  private static final Gson GSON = new Gson();

  // TODO: Change to private if PathConverter is changed.
  public class RouteWaypoint {
    private double[] waypoint;
    private int duration;

    // TODO: Remove if PathConverter is changed.
    public double[] getWaypoint() {
      return waypoint;
    }

    public void setWaypoint(double[] waypoint) {
      this.waypoint = waypoint;
    }

    public int getDuration() {
      return duration;
    }

    public void setDuration(int duration) {
      this.duration = duration;
    }
  }

  private class RouteRequest {
    private double[] start;
    private long date;
    private double[] destination;
    private RouteWaypoint[] waypoints;
  }

  @Override
  public Object handle(Request request, Response response) {
    QueryParamsMap qm = request.queryMap();

    try {
      RouteRequest params = GSON.fromJson(qm.value("params"),
          RouteRequest.class);
      double[] startArray = params.start;
      RouteWaypoint[] waypoints = params.waypoints;
      double[] endArray = params.destination;
      long date = params.date;

      // Convert the data returned from the front end into LatLon data that
      // can be used in the front end
      LatLon start = new LatLon(startArray[0], startArray[1]);
      LatLon end = new LatLon(endArray[0], endArray[1]);
      List<LatLon> waypointsList = new ArrayList<>();
      for (RouteWaypoint routeWaypoint : waypoints) {
        double[] coords = routeWaypoint.waypoint;
        waypointsList.add(new LatLon(coords[0], coords[1]));
      }

      DirectionsWrapper directionsData = DirectionsAPIHandler
          .getDirections(start, waypointsList, end);
      List<Segment> directions = directionsData.getSegments();

      // Parse the start time format into UNIX time
      Path startPath = PathConverter.convertPath(directions, waypoints, date);
      // Generates alternate paths
      Map<String, Object> pathWeathers = PathRanker.bestPath(startPath);
      // Stores all relevant variables in map to be parsed through JSON
      Map<String, Object> variables = ImmutableMap.of("message", "", "path",
          directionsData.getPoints(), "segments", directions, "weather",
          pathWeathers);
      return GSON.toJson(variables);
    } catch (Exception e) {
      // Generates default message to be displayed
      String toDisplay =
          "We're sorry! There was an error processing your request";
      // Gets message from exception
      String msg = e.getMessage();
      // Checks and handles of two designated, expected errors:
      // a) The exhaustion of API calls
      // b) Inability to judge the given path using weather data
      switch (msg) {
        case "Out of API calls":
          toDisplay = "We're unfortunately unable to provide services until "
              + "tomorrow (EST).";
          break;
        case "Path too long":
          toDisplay = "Part of the journey you've entered lies outside weather "
              + "coverage — this may have happened because the journey was "
              + "too long, or the departure time was too far from now. "
              + "Please try again with a different journey.";
          break;
        case "Invalid departure time":
          toDisplay = "Please select a departure time (in the departure time "
              + "zone) later than the current time.";
          break;
        case "No path":
          toDisplay = "We unfortunately couldn't connect those points. "
              + "Please try again with a different journey.";
          break;
        default:
          // Prints stacktrace in case of unexpected error (TODO: remove)
          e.printStackTrace();
          break;
      }

      // Sends Map associated with Exceptions
      // TODO: Remove unnecessary "routes" array?
      Map<String, Object> variables = ImmutableMap.of("message", toDisplay,
          "routes", new String[0]);
      return GSON.toJson(variables);
    }
  }
}
