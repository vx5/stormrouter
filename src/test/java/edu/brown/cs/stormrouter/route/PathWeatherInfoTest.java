package edu.brown.cs.stormrouter.route;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class PathWeatherInfoTest {
  // Instance fields stores instance of PathWeatherInfo
  private PathWeatherInfo p = new PathWeatherInfo(0);

  @Test
  public void testConstruction() {
    assertNotNull(new PathWeatherInfo(0));
  }

  @Test
  public void testGetStartTime() {
    assertEquals(p.getStartTime(), 0);
  }

  @Test
  public void testGetScore() {
    // Test initial score
    assertEquals(p.getScore(), 0);
    // Adds point to instance
    p.addWeatherData(0, 0, "", "", 5, 1);
    // Checks for increased score
    assertEquals(p.getScore(), 5);
  }

}
