package edu.brown.cs.stormrouter.route;

import com.google.gson.JsonElement;
import edu.brown.cs.stormrouter.directions.LatLon;
import edu.brown.cs.stormrouter.directions.Segment;

import java.util.List;

/**
 * A simple wrapper class for returning directions data including a list of
 * parsed Segment objects and a reference to the full GeoJSON data for display
 * in the GUI.
 */
public class DirectionsWrapper {
  private List<Segment> segments;
  private JsonElement geoJSON;
  private List<LatLon> points;

  /**
   * Constructs a new DirectionsWrapper from the data.
   * @param segments - A list of Segment fields retrieved from the GeoJSON
   * @param points - The list of all points making up the route
   * @param geoJSON - The full GeoJSON returned from a directions call
   */
  public DirectionsWrapper(List<Segment> segments, List<LatLon> points,
                           JsonElement geoJSON) {
    this.segments = segments;
    this.points = points;
    this.geoJSON = geoJSON;
  }

  /**
   * @return - The list of segments in the path
   */
  public List<Segment> getSegments() {
    return segments;
  }

  /**
   * @return - The full GeoJSON returned from a directions call
   */
  public JsonElement getGeoJSON() {
    return geoJSON;
  }

  /**
   * @return - The full list of points making up the route
   */
  public List<LatLon> getPoints() {
    return points;
  }
}
