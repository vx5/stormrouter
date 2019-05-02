package edu.brown.cs.stormrouter.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import edu.brown.cs.stormrouter.directions.DirectionsAPIHandler;
import edu.brown.cs.stormrouter.directions.LatLon;
import edu.brown.cs.stormrouter.directions.Segment;
import edu.brown.cs.stormrouter.route.DirectionsWrapper;
import edu.brown.cs.stormrouter.route.Path;
import edu.brown.cs.stormrouter.route.PathConverter;
import edu.brown.cs.stormrouter.route.PathRanker;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;

public class RouteHandler implements Route {
  private static final Gson GSON = new Gson();

  public class RouteWaypoint {
    public double[] waypoint;
    public int duration;

    @Override
    public String toString() {
      return "RouteWaypoint{" + "waypoint=" + Arrays.toString(waypoint)
          + ", duration=" + duration + '}';
    }
  }

  private class RouteRequest {
    double[] start;
    long date;
    double[] destination;
    RouteWaypoint[] waypoints;
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
      for (int i = 0; i < waypoints.length; i++) {
        double[] coords = waypoints[i].waypoint;
        waypointsList.add(new LatLon(coords[0], coords[1]));
      }

      DirectionsWrapper directionsData = DirectionsAPIHandler
          .getDirections(start, waypointsList, end);
      List<Segment> directions = directionsData.getSegments();

      // Parse the start time format into UNIX time
      Path startPath = PathConverter.convertPath(directions, waypoints, date);
      // Creates ranker, generates alternate paths
      PathRanker ranker = new PathRanker();
      Map<String, Object> pathWeathers = ranker.bestPath(startPath);
      // Stores all relevant variables in map to be parsed through JSON
      Map<String, Object> variables = ImmutableMap.of("message", "", "path",
          directionsData.getPoints(), "segments", directions, "weather",
          pathWeathers);
      return GSON.toJson(variables);
    } catch (Exception e) {
      String toDisplay = "We're sorry! There was an error processing your request";
      if (e.getMessage().equals("Out of API calls")) {
        toDisplay = "We're unfortunately unable to provide services until tomorrow EST";
      } else {
        e.printStackTrace();
      }
      Map<String, Object> variables = ImmutableMap.of("message", toDisplay,
          "routes", new String[0]);
      return GSON.toJson(variables);
    }
  }
}
