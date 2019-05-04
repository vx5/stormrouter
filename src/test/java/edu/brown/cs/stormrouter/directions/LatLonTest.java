package edu.brown.cs.stormrouter.directions;

import org.junit.Test;
import static org.junit.Assert.*;

public class LatLonTest {
  @Test
  public void testConstruction() {
    LatLon latLon = new LatLon(-2.0, 183.57);
    assertNotNull(latLon);
    assertEquals(latLon.getLatitude(), -2.0, 0.01);
    assertEquals(latLon.getLongitude(), 183.57, 0.01);
  }

  @Test
  public void testEquality() {
    LatLon latLon1 = new LatLon(43.7, -65.2);
    LatLon latLon2 = new LatLon(43.8, -65.2);
    LatLon latLon3 = new LatLon(-65.2, 43.7);
    LatLon latLon4 = new LatLon(43.7, -65.2);

    assertNotEquals(latLon1, latLon2);
    assertNotEquals(latLon1, latLon3);
    assertEquals(latLon1, latLon4);
  }
}
