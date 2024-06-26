package primary.core.cs.stormrouter.weather;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import primary.core.cs.stormrouter.conversions.Units;

public class WSetTest {
  // Stores basis TimePoint to be adapted
  private TimePoint weather;

  @Before
  public void setUp() {
    // Instantiates new object
    weather = new TimePoint();
    // Initializes fields necessary for scoring test
    weather.setPrecipIntensity(0);
    weather.setPrecipType("");
    weather.setSummary("summary");
    weather.setTemperature(0);
    weather.setVisibility(10);
    weather.setWindGust(0);
    weather.setWindSpeed(0);
    // Sets all factors to standard
    WSet.setRainFactor(1);
    WSet.setSnowFactor(1);
    WSet.setWindFactor(1);
  }

  @After
  public void tearDown() {
    // Clears instance field
    weather = null;
  }

  @Test
  public void testScoreWeather() {
    assertEquals(WSet.scoreWeather(weather), 0);
  }

  @Test
  public void testSetRainFactor() {
    // Adjust weather to include rain
    weather.setPrecipIntensity(1 * Units.IN_TO_MM);
    weather.setPrecipType("rain");
    // Check for initial score
    assertEquals(WSet.scoreWeather(weather), 40);
    // Adjust rain factor
    WSet.setRainFactor(2);
    // Check for new score
    assertEquals(WSet.scoreWeather(weather), 80);
  }

  @Test
  public void testSetSnowFactor() {
    // Adjust weather to include snow
    weather.setPrecipIntensity(1 * Units.IN_TO_MM);
    weather.setPrecipType("snow");
    // Check for initial score
    assertEquals(WSet.scoreWeather(weather), 75);
    // Adjust rain factor
    WSet.setSnowFactor(2);
    // Check for new score
    assertEquals(WSet.scoreWeather(weather), 150);
  }

  @Test
  public void testSetWindFactor() {
    // Adjust weather to include wind
    weather.setWindGust(100 * Units.MPH_TO_METERS_PER_S);
    // Check for initial score
    assertEquals(WSet.scoreWeather(weather), 100);
    // Adjust rain factor
    WSet.setWindFactor(2);
    // Check for new score
    assertEquals(WSet.scoreWeather(weather), 200);
  }

}
