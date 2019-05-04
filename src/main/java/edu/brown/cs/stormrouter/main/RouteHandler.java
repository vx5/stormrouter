package edu.brown.cs.stormrouter.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import edu.brown.cs.stormrouter.directions.DirectionsAPIHandler;
import edu.brown.cs.stormrouter.directions.DirectionsException;
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
      // Generates alternate paths
      Map<String, Object> pathWeathers = PathRanker.bestPath(startPath);
      // Stores all relevant variables in map to be parsed through JSON
      Map<String, Object> variables = ImmutableMap.of("message", "", "path",
          directionsData.getPoints(), "segments", directions, "weather",
          pathWeathers);
      return GSON.toJson(variables);
    } catch (DirectionsException de) {
      // These custom exception objects already have well-defined messages
      Map<String, Object> variables = ImmutableMap.of("message",
          de.getMessage(), "routes", new String[0]);
      return GSON.toJson(variables);
    } catch (Exception e) {
      // Generates default message to be displayed
      String toDisplay = "We're sorry! There was an error processing your request";
      // Gets message from exception
      String msg = e.getMessage();
      // Checks and handles of two designated, expected errors:
      // a) The exhaustion of API calls
      // b) Inability to judge the given path using weather data
      if (msg.equals("Out of API calls")) {
        toDisplay = "We're unfortunately unable to provide services until tomorrow EST";
      } else if (msg.equals("Path too long")) {
        toDisplay = "Part of the journey you've entered lies outside weather coverage — "
            + "this may have happened because the journey was too long, or the departure "
            + "time was too far from right now. Please try again with a different journey";
      } else if (msg.equals("Invalid departure time")) {
        toDisplay = "Please select a departure time later than the current time "
            + "(at the departure time zone)";
      } else if (msg.equals("No path")) {
        toDisplay = "We unfortunately couldn't connect those points. Please try again "
            + "with a different journey!";
      } else {
        // Prints stacktrace in case of unexpected error (TODO: remove)
        e.printStackTrace();
      }
      // Sends Map associated with Exceptions
      Map<String, Object> variables = ImmutableMap.of("message", toDisplay,
          "routes", new String[0]);
      return GSON.toJson(variables);
    }
  }
}
