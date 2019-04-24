package edu.brown.cs.stormrouter.route;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import edu.brown.cs.stormrouter.directions.DirectionsAPIHandler;
import edu.brown.cs.stormrouter.directions.LatLon;
import edu.brown.cs.stormrouter.directions.PolylineDecoder;
import edu.brown.cs.stormrouter.directions.Segment;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;

public class RouteHandler implements Route {
  private static final Gson GSON = new Gson();

  @Override
  public Object handle(Request request, Response response) {
    QueryParamsMap qm = request.queryMap();

    try {
      // TODO: Handle converting the locations to lat/lon values
      Double lat1 = Double.parseDouble(qm.value("lat1"));
      Double lon1 = Double.parseDouble(qm.value("lon1"));
      Double lat2 = Double.parseDouble(qm.value("lat2"));
      Double lon2 = Double.parseDouble(qm.value("lon2"));

      LatLon start = new LatLon(lat1, lon1);
      LatLon end = new LatLon(lat2, lon2);
      List<Segment> directions = DirectionsAPIHandler.getDirections(start, end);

      // TODO: Get the polyline
      String polylineStr = "";
      // Decodes string
      List<LatLon> polylinePts = PolylineDecoder.decodePolyline(polylineStr);

      // Parse the start time format into UNIX time
      Long startTime = Long.parseLong(qm.value("startTime"));
      Path startPath = PathConverter.convertPath(directions, startTime);
      // Creates ranker, generates alternate paths
      PathRanker ranker = new PathRanker();
      Set<PathWeatherInfo> bestPaths = ranker.bestPath(startPath);
      // Stores paths in the list
      List<PathWeatherInfo> pathWeathers = new ArrayList<PathWeatherInfo>();
      Iterator<PathWeatherInfo> bestPathsIt = bestPaths.iterator();
      while (bestPathsIt.hasNext()) {
        pathWeathers.add(bestPathsIt.next());
      }
      // Stores all relevant variables in map to be parsed through JSON
      Map<String, Object> variables = ImmutableMap.of("message", "", "decoded",
          polylinePts, "segments", directions, "weather", pathWeathers);
      return GSON.toJson(variables);
    } catch (NumberFormatException nfe) {
      Map<String, Object> variables = ImmutableMap.of("message",
          "There was an error processing.", "routes", new String[0]);
      return GSON.toJson(variables);
    } catch (Exception e) {
      Map<String, Object> variables = ImmutableMap.of("message",
          "There was an error processing.", "routes", new String[0]);
      return GSON.toJson(variables);
    }
  }
}
