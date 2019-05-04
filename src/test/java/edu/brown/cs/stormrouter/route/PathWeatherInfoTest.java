package edu.brown.cs.stormrouter.route;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class PathWeatherInfoTest {

  @Test
  public void testConstruction() {
    assertNotNull(new PathWeatherInfo(0));
  }

}
