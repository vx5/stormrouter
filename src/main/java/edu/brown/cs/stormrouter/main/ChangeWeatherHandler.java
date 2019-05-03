package edu.brown.cs.stormrouter.main;

import edu.brown.cs.stormrouter.weather.WSet;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * @author vx5
 *
 *         Route that takes in data regarding changes to the weather-scoring
 *         rubric, and makes them to the rubric.
 */
public class ChangeWeatherHandler implements Route {

  @Override
  public Object handle(Request request, Response response) throws Exception {
    // META: Interprets new scores
    double newRainFactor = 1;
    double newSnowFactor = 1;
    double newWindFactor = 1;
    // Updates rubric in WSet class
    WSet.setRainFactor(newRainFactor);
    WSet.setSnowFactor(newSnowFactor);
    WSet.setWindFactor(newWindFactor);
    // Returns 0-integer to prevent any errors, though the method is otherwise
    // void
    return new Integer(0);
  }

}
