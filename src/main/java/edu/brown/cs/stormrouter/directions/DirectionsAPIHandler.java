package edu.brown.cs.stormrouter.directions;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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
   * @param start - The latitude and longitude of the starting point
   * @param waypoints - An array of any waypoints that will be traversed on the
   *                  route
   * @param end   - The latitude and longitude of the ending point
   * @return - Returns a Route object containing all of the data for each
   *         segment and the processed polyline.
   * @throws Exception - Throws an exception if there is an error retrieving or
   *                   parsing the data.
   */
  public static List<Segment> getDirections(LatLon start, List<LatLon> waypoints,
                                            LatLon end) {
    String urlString = String.format(
        "%s?api_key=%s&start=%.4f,%.4f" + "&end=%.4f,%.4f", BASE_URL, API_KEY,
        start.getLongitude(), start.getLatitude(), end.getLongitude(),
        end.getLatitude());

    List<Segment> segments = new ArrayList<>();

    try {
      URL url = new URL(urlString);
      HttpURLConnection request = (HttpURLConnection) url.openConnection();
      request.connect();

      JsonParser parser = new JsonParser();
      JsonElement root = parser
          .parse(new InputStreamReader(request.getInputStream()));

      if (root != null && root.getAsJsonObject().get("features") != null) {
        JsonArray features = root.getAsJsonObject().getAsJsonArray("features");
        JsonObject properties = features.get(0).getAsJsonObject()
            .getAsJsonObject("properties");
        JsonObject segmentsObject = properties.getAsJsonArray("segments").get(0)
            .getAsJsonObject();
        JsonArray steps = segmentsObject.getAsJsonArray("steps");

        JsonArray coordinates = features.get(0).getAsJsonObject()
            .getAsJsonObject("geometry").getAsJsonArray("coordinates");

        for (int i = 0; i < steps.size(); i++) {
          JsonObject step = steps.get(i).getAsJsonObject();

          double distance = step.get("distance").getAsDouble();
          double duration = step.get("duration").getAsDouble();
          String name = step.get("name").getAsString();
          String instructions = step.get("instruction").getAsString();
          JsonArray routeWaypoints = step.getAsJsonArray("way_points");
          JsonArray coord1 = coordinates.get(routeWaypoints.get(0).getAsInt())
              .getAsJsonArray();
          JsonArray coord2 = coordinates.get(routeWaypoints.get(1).getAsInt())
              .getAsJsonArray();
          LatLon startLatLon = new LatLon(coord1.get(1).getAsDouble(),
              coord1.get(0).getAsDouble());
          LatLon endLatLon = new LatLon(coord2.get(1).getAsDouble(),
              coord2.get(0).getAsDouble());

          Segment segment = new Segment(startLatLon, endLatLon, distance,
              duration, name, instructions);
          segments.add(segment);
        }
      }
    } catch (IOException ioe) {
      // TODO: Add custom exception handling
    }

    return segments;
  }
}
