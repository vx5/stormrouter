package edu.brown.cs.stormrouter.weather;

public class HourlyWeather {
  private String summary;
  private String icon;
  private TimePoint[] data;

  public String getSummary() {
    return summary;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }

  public String getIcon() {
    return icon;
  }

  public void setIcon(String icon) {
    this.icon = icon;
  }

  public TimePoint[] getData() {
    return data;
  }

  public void setData(TimePoint[] data) {
    this.data = data;
  }
}
