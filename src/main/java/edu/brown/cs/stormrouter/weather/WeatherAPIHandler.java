package edu.brown.cs.stormrouter.weather;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/**
 * Handles retrieving weather from the DarkSky API.
 */
public final class WeatherAPIHandler {
  // private static final String API_KEY = "b95d19569678f2f58131eb90be980761";
  private static final String API_KEY = "eb377b405380eb96daf3d4e994ff582a";
  private static final Gson GSON = new Gson();

  private WeatherAPIHandler() {
  }

  /**
   * Gets weather at a certain latitude for the next 48 hours (at most).
   * 
   * @param lat latitude
   * @param lon longitude
   * @return the WeatherConditions with HourlyWeather data parsed from DarkSky
   * @throws Exception If there was an error making or parsing the API request
   */
  public static WeatherConditions getWeather(double lat, double lon)
      throws Exception {
    String apiString = String
        .format("https://api.darksky.net/forecast/%s/%.4f,%.4f?"
            + "exclude=minutely,daily,flags,alerts", API_KEY, lat, lon);

    URL url = new URL(apiString);
    HttpURLConnection request = (HttpURLConnection) url.openConnection();
    request.connect();

    JsonParser parser = new JsonParser();
    try {
      JsonElement root = parser
          .parse(new InputStreamReader(request.getInputStream()));

      return GSON.fromJson(root, WeatherConditions.class);
    } catch (Exception e) {
      throw new Exception("Out of API calls");
    }
  }
}
