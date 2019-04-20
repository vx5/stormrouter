package edu.brown.cs.stormrouter.directions;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import edu.brown.cs.stormrouter.route.LatLon;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A helper class for handling requests to the directions API.
 */
public final class DirectionsAPIHandler {
  private static final String BASE_URL
      = "https://api.openrouteservice.org/v2/directions/driving-car";
  private static final String API_KEY
      = "5b3ce3597851110001cf6248354b40dadcf845dbbc95c4797348398b";
  private static final Gson GSON = new Gson();

  /**
   * Attempts to retrieve directions between the two specified points.
   * @param start - The latitude and longitude of the starting point
   * @param end - The latitude and longitude of the ending point
   * @return - Returns a Route object containing all of the data
   * for each segment and the processed polyline.
   * @throws Exception - Throws an exception if there is an error retrieving
   * or parsing the data.
   */
  public static List<Segment> getDirections(LatLon start, LatLon end)
      throws Exception{
    String urlString = String.format("%s?api_key=%s&start=%.4f,%.4f"
            + "&end=%.4f,%.4f",
        BASE_URL, API_KEY, start.getLongitude(), start.getLatitude(),
        end.getLongitude(), end.getLatitude());

    URL url = new URL(urlString);
    HttpURLConnection request = (HttpURLConnection) url.openConnection();
    request.connect();

    JsonParser parser = new JsonParser();
    JsonElement root = parser.parse(
        new InputStreamReader(request.getInputStream()));

    return new ArrayList<Segment>();
  }
}
