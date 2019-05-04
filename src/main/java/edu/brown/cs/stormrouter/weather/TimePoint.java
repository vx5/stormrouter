package edu.brown.cs.stormrouter.weather;

/**
 * All descriptions come from DarkSky's documentation.
 * https://darksky.net/dev/docs#response-format
 * <p>
 * "A [TimePoint] object contains various properties, each representing the
 * average (unless otherwise specified) of a particular weather phenomenon
 * occurring during a period of time."
 */
public class TimePoint {
  private long time;
  private String summary;
  private String icon;
  private double precipIntensity;
  //  private double precipProbability;
  private String precipType;
  private double temperature;
  //  private double apparentTemperature;
  //  private double dewPoint;
  //  private double humidity;
  //  private double pressure;
  private double windSpeed;
  private double windGust;
  //  private int windBearing;
  //  private double cloudCover;
  //  private int uvIndex;
  private double visibility;
  //  private double ozone;

  /**
   * @return "The UNIX time at which this [TimePoint] begins." "hourly
   * [TimePoint] objects [are always aligned] to the top of the hour."
   */
  public long getTime() {
    return time;
  }

  /**
   * @param time to set to
   */
  public void setTime(long time) {
    this.time = time;
  }

  /**
   * @return "A human-readable text summary of this [TimePoint]."
   */
  public String getSummary() {
    return summary;
  }

  /**
   * @param summary to set to
   */
  public void setSummary(String summary) {
    this.summary = summary;
  }

  /**
   * @return "A machine-readable text summary of this [TimePoint], suitable for
   * selecting an icon for display. If defined, this property will have one of
   * the following values: clear-day, clear-night, rain, snow, sleet, wind, fog,
   * cloudy, partly-cloudy-day, or partly-cloudy-night."
   */
  public String getIcon() {
    return icon;
  }

  /**
   * @param icon to set to
   */
  public void setIcon(String icon) {
    this.icon = icon;
  }

  /**
   * @return "The intensity (in inches of liquid water per hour) of
   * precipitation occurring at the given time. This value is conditional on
   * probability (that is, assuming any precipitation occurs at all)."
   */
  public double getPrecipIntensity() {
    return precipIntensity;
  }

  /**
   * @param precipIntensity to set to
   */
  public void setPrecipIntensity(double precipIntensity) {
    this.precipIntensity = precipIntensity;
  }

  /*public double getPrecipProbability() {
    return precipProbability;
  }

  public void setPrecipProbability(double precipProbability) {
    this.precipProbability = precipProbability;
  }*/

  /**
   * @return "The type of precipitation occurring at the given time. If defined,
   * this property will have one of the following values: "rain", "snow", or
   * "sleet" (which refers to each of freezing rain, ice pellets, and “wintery
   * mix”). (If precipIntensity is zero, then this property will not be
   * defined."
   */
  public String getPrecipType() {
    return precipType;
  }

  /**
   * @param precipType to set to
   */
  public void setPrecipType(String precipType) {
    this.precipType = precipType;
  }

  /**
   * @return "The air temperature in degrees Fahrenheit."
   */
  public double getTemperature() {
    return temperature;
  }

  /**
   * @param temperature to set to
   */
  public void setTemperature(double temperature) {
    this.temperature = temperature;
  }

  /*public double getApparentTemperature() {
    return apparentTemperature;
  }

  public void setApparentTemperature(double apparentTemperature) {
    this.apparentTemperature = apparentTemperature;
  }

  public double getDewPoint() {
    return dewPoint;
  }

  public void setDewPoint(double dewPoint) {
    this.dewPoint = dewPoint;
  }

  public double getHumidity() {
    return humidity;
  }

  public void setHumidity(double humidity) {
    this.humidity = humidity;
  }

  public double getPressure() {
    return pressure;
  }

  public void setPressure(double pressure) {
    this.pressure = pressure;
  }*/

  /**
   * @return "The wind speed in miles per hour."
   */
  public double getWindSpeed() {
    return windSpeed;
  }

  /**
   * @param windSpeed to set to
   */
  public void setWindSpeed(double windSpeed) {
    this.windSpeed = windSpeed;
  }

  /**
   * @return "The wind gust speed in miles per hour."
   */
  public double getWindGust() {
    return windGust;
  }

  /**
   * @param windGust to set to
   */
  public void setWindGust(double windGust) {
    this.windGust = windGust;
  }

  /*public int getWindBearing() {
    return windBearing;
  }

  public void setWindBearing(int windBearing) {
    this.windBearing = windBearing;
  }

  public double getCloudCover() {
    return cloudCover;
  }

  public void setCloudCover(double cloudCover) {
    this.cloudCover = cloudCover;
  }

  public int getUvIndex() {
    return uvIndex;
  }

  public void setUvIndex(int uvIndex) {
    this.uvIndex = uvIndex;
  }*/

  /**
   * @return "The average visibility in miles, capped at 10 miles."
   */
  public double getVisibility() {
    return visibility;
  }

  /**
   * @param visibility to set to
   */
  public void setVisibility(double visibility) {
    this.visibility = visibility;
  }

  /*public double getOzone() {
    return ozone;
  }

  public void setOzone(double ozone) {
    this.ozone = ozone;
  }*/

  @Override
  public String toString() {
    return "TimePoint{"
        + "time=" + time
        + ", precipIntensity=" + precipIntensity
        + ", precipType='" + precipType + '\''
        + ", temperature=" + temperature
        + ", windSpeed=" + windSpeed
        + ", visibility=" + visibility
        + '}';
  }
}
