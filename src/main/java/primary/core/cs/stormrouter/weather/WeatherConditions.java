package primary.core.cs.stormrouter.weather;

/**
 * All descriptions come from DarkSky's documentation.
 * https://darksky.net/dev/docs#response-format
 * <p>
 * Represents an API response from the DarkSky API.
 */
public class WeatherConditions {
  private double latitude;
  private double longitude;
  private String timezone;
  private TimePoint currently;
  private HourlyWeather hourly;

  /**
   * @return "The requested latitude."
   */
  public double getLatitude() {
    return latitude;
  }

  /**
   * @param latitude to set to
   */
  public void setLatitude(double latitude) {
    this.latitude = latitude;
  }

  /**
   * @return "The requested longitude."
   */
  public double getLongitude() {
    return longitude;
  }

  /**
   * @param longitude to set to
   */
  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }

  /**
   * @return "(e.g. 'America/New_York') The IANA timezone name for the requested
   * location."
   */
  public String getTimezone() {
    return timezone;
  }

  /**
   * @param timezone to set to
   */
  public void setTimezone(String timezone) {
    this.timezone = timezone;
  }

  /**
   * @return "A [TimePoint] containing the current weather conditions at the
   * requested location."
   */
  public TimePoint getCurrently() {
    return currently;
  }

  /**
   * @param currently to set to
   */
  public void setCurrently(TimePoint currently) {
    this.currently = currently;
  }

  /**
   * @return [An HourlyWeather] containing the weather conditions hour-by-hour
   * for the next two days."
   */
  public HourlyWeather getHourly() {
    return hourly;
  }

  /**
   * @param hourly to set to
   */
  public void setHourly(HourlyWeather hourly) {
    this.hourly = hourly;
  }
}
