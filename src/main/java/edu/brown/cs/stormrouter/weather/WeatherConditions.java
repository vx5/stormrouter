package edu.brown.cs.stormrouter.weather;

public class WeatherConditions {
  private double latitude;
  private double longitude;
  private String timezone;
  private TimePoint currently;
  private HourlyWeather hourly;

  public double getLatitude() {
    return latitude;
  }

  public void setLatitude(double latitude) {
    this.latitude = latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }

  public String getTimezone() {
    return timezone;
  }

  public void setTimezone(String timezone) {
    this.timezone = timezone;
  }

  public TimePoint getCurrently() {
    return currently;
  }

  public void setCurrently(TimePoint currently) {
    this.currently = currently;
  }

  public HourlyWeather getHourly() {
    return hourly;
  }

  public void setHourly(HourlyWeather hourly) {
    this.hourly = hourly;
  }
}
