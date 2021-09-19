package primary.core.cs.stormrouter.directions;

import java.util.Objects;

/**
 * Simple wrapper class for latitude/longitude pair storage.
 */
public class LatLon {
  private double latitude;
  private double longitude;

  /**
   * Constructs a new LatLon object with the specified latitude and longitude.
   * @param latitude  - The latitude as a double
   * @param longitude - The longitude as a double
   */
  public LatLon(double latitude, double longitude) {
    this.latitude = latitude;
    this.longitude = longitude;
  }

  /**
   * @return - The latitude of the point
   */
  public double getLatitude() {
    return latitude;
  }

  /**
   * @return - The longitude of the point
   */
  public double getLongitude() {
    return longitude;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LatLon latLon = (LatLon) o;
    return Double.compare(latLon.latitude, latitude) == 0
        && Double.compare(latLon.longitude, longitude) == 0;
  }

  @Override
  public int hashCode() {
    return Objects.hash(latitude, longitude);
  }
}
