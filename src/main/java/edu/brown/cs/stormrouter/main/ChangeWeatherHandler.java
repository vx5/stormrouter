package edu.brown.cs.stormrouter.main;

import edu.brown.cs.stormrouter.weather.WSet;
import spark.QueryParamsMap;
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
    QueryParamsMap qm = request.queryMap();
    // Stores raw values, to percent form, from form
    double rawRainVal = Double.parseDouble(qm.value("rain")) / 100.0;
    double rawSnowVal = Double.parseDouble(qm.value("snow")) / 100.0;
    double rawWindVal = Double.parseDouble(qm.value("wind")) / 100.0;
    // META: Interprets new scores
    double newRainFactor = 1 + rawRainVal;
    double newSnowFactor = 1 + rawSnowVal;
    double newWindFactor = 1 + rawWindVal;
    // Updates rubric in WSet class
    WSet.setRainFactor(newRainFactor);
    WSet.setSnowFactor(newSnowFactor);
    WSet.setWindFactor(newWindFactor);
    // Returns 0-integer to prevent any errors, though the method is otherwise
    // void
    return new Integer(0);
  }

}
