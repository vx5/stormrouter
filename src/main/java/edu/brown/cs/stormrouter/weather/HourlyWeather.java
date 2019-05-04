package edu.brown.cs.stormrouter.weather;

/**
 * All descriptions come from DarkSky's documentation.
 * https://darksky.net/dev/docs#response-format
 * <p>
 * "[An HourlyWeather] object represents the various weather phenomena occurring
 * over a period of time. Such objects contain the following properties:"
 */
public class HourlyWeather {
  private String summary;
  private String icon;
  private TimePoint[] data;

  /**
   * @return "A human-readable summary of this [HourlyWeather]."
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
   * @return "A machine-readable text summary of this [HourlyWeather]. (May take
   * on the same values as the icon property of [TimePoints].)"
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
   * @return "An array of [TimePoints], ordered by time, which together describe
   * the weather conditions at the requested location over time."
   */
  public TimePoint[] getData() {
    return data;
  }

  /**
   * @param data An array of TimePoints to set the hourly data to
   */
  public void setData(TimePoint[] data) {
    this.data = data;
  }
}
