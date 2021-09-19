package primary.core.cs.stormrouter.directions;

import org.junit.Test;
import static org.junit.Assert.*;

public class SegmentTest {
  @Test
  public void testConstruction() {
    LatLon start = new LatLon(53.7, -160.2);
    LatLon end = new LatLon(57.2, -158.3);
    Segment segment = new Segment(start, end, 3007.2, 553.1,
        "Test Path", "Continue along Test Path", 1, true);

    assertNotNull(segment);
    assertEquals(segment.getStart(), start);
    assertEquals(segment.getEnd(), end);
    assertEquals(segment.getLength(), 3007.2, 0.01);
    assertEquals(segment.getDuration(), 553.1, 0.01);
    assertEquals(segment.getName(), "Test Path");
    assertEquals(segment.getInstructions(), "Continue along Test Path");
    assertEquals(segment.getType(), 1);
    assertEquals(segment.isTerminal(), true);
  }
}
