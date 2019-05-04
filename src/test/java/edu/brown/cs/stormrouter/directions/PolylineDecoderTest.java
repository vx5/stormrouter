package edu.brown.cs.stormrouter.directions;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class PolylineDecoderTest {
  @Test
  public void testDecode() {
    String polyline = "_p~iF~ps|U_ulLnnqC_mqNvxq`@";
    List<LatLon> path = PolylineDecoder.decodePolyline(polyline);
    assertEquals(path.size(), 3);
    assertEquals(path.get(0), new LatLon(38.5, -120.2));
    assertEquals(path.get(1), new LatLon(40.7, -120.95));
    assertEquals(path.get(2), new LatLon(43.252, -126.453));
  }
}
