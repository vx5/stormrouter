package edu.brown.cs.stormrouter.directions;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import edu.brown.cs.stormrouter.route.DirectionsWrapper;

/**
 * A helper class for handling requests to the directions API.
 */
public final class DirectionsAPIHandler {
  private static final String BASE_URL = "https://api.openrouteservice.org/v2/directions/driving-car";
  private static final String API_KEY = "5b3ce3597851110001cf6248354b40dadcf845dbbc95c4797348398b";
  private static final Gson GSON = new Gson();

  /**
   * Attempts to retrieve directions between the two specified points.
   *
   * @param start     - The latitude and longitude of the starting point
   * @param waypoints - An array of any waypoints that will be traversed on the
   *                  route
   * @param end       - The latitude and longitude of the ending point
   * @return - Returns a DirectionsWrapper object containing all of the data for
   *         each segment, and the full GeoJSON reference for rendering in the
   *         GUI
   * @throws Exception - Throws an exception if there is an error retrieving or
   *                   parsing the data.
   */
  public static DirectionsWrapper getDirections(LatLon start,
      List<LatLon> waypoints, LatLon end) {
    List<Segment> segments = new ArrayList<>();
    JsonElement geoJSON = null;

    // Build out the main JSON request body
    JsonObject body = new JsonObject();
    JsonArray coordinatesArray = new JsonArray();

    JsonArray startArray = new JsonArray();
    startArray.add(start.getLongitude());
    startArray.add(start.getLatitude());
    coordinatesArray.add(startArray);
    for (LatLon waypoint : waypoints) {
      JsonArray waypointArray = new JsonArray();
      waypointArray.add(waypoint.getLongitude());
      waypointArray.add(waypoint.getLatitude());
      coordinatesArray.add(waypointArray);
    }
    JsonArray endArray = new JsonArray();
    endArray.add(end.getLongitude());
    endArray.add(end.getLatitude());
    coordinatesArray.add(endArray);

    body.add("coordinates", coordinatesArray);
    String bodyString = GSON.toJson(body);

    try {
      // Build the POST request
      URL url = new URL(BASE_URL);
      HttpURLConnection request = (HttpURLConnection) url.openConnection();
      request.setRequestMethod("POST");
      request.setRequestProperty("Authorization", API_KEY);
      request.setRequestProperty("Content-Type", "application/json");
      request.setRequestProperty("Content-Length",
          "" + bodyString.getBytes().length);
      request.setRequestProperty("Content-Language", "en-US");
      request.setDoOutput(true);
      request.setDoInput(true);
      request.connect();

      OutputStreamWriter out = new OutputStreamWriter(
          request.getOutputStream());
      out.write(bodyString);
      out.close();

      int responseCode = request.getResponseCode();
      if (responseCode == HttpURLConnection.HTTP_OK) {
        JsonParser parser = new JsonParser();
        JsonObject root = parser
            .parse(new InputStreamReader(request.getInputStream()))
            .getAsJsonObject();

        if (root != null && root.getAsJsonObject().get("routes") != null) {
          JsonObject routes = root.getAsJsonArray("routes").get(0)
              .getAsJsonObject();
          JsonObject segmentsObject = routes.getAsJsonArray("segments").get(0)
              .getAsJsonObject();
          JsonArray steps = segmentsObject.getAsJsonArray("steps");

          // Retrieve and decode the polyline string
          String polyline = routes.get("geometry").getAsString();
          List<LatLon> polylinePts = PolylineDecoder.decodePolyline(polyline);

          for (int i = 0; i < steps.size(); i++) {
            JsonObject step = steps.get(i).getAsJsonObject();

            double distance = step.get("distance").getAsDouble();
            double duration = step.get("duration").getAsDouble();
            String name = step.get("name").getAsString();
            String instructions = step.get("instruction").getAsString();
            JsonArray routeWaypoints = step.getAsJsonArray("way_points");
            LatLon startLatLon = polylinePts
                .get(routeWaypoints.get(0).getAsInt());
            LatLon endLatLon = polylinePts
                .get(routeWaypoints.get(1).getAsInt());

            Segment segment = new Segment(startLatLon, endLatLon, distance,
                duration, name, instructions, false);
            segments.add(segment);
            geoJSON = root;
          }
        } else {
          System.out.println("Error building response object");
        }
      } else {
        // TODO: More custom exception handling
        System.out.println("Error making request");
      }
    } catch (IOException ioe) {
      // TODO: Add custom exception handling
      System.out.println("Error reading response");
    }

    return new DirectionsWrapper(segments, geoJSON);
  }
}
