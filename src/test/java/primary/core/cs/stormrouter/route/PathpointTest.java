package primary.core.cs.stormrouter.route;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PathpointTest {
  // Stores Pathpoint instance for testing
  private Pathpoint p;

  @Test
  public void testConstruction() {
    assertNotNull(new Pathpoint(0, 0));
  }

  @Test
  public void testGetCoords() {
    double[] testCoords = p.getCoords();
    // Test for matching coordinates
    assertEquals(testCoords.length, 2);
    assertEquals(testCoords[0], 1, 0);
    assertEquals(testCoords[1], 1, 0);
  }

  /**
   * Tests setTime() and getTime() methods together due to interreliance.
   */
  @Test
  public void testSetGetTime() {
    p.setTime(10);
    assertEquals(p.getTime(), 10);
  }

  /**
   * Tests setDistToReach(), getDistToReach() methods together due to
   * interreliance.
   */
  @Test
  public void testSetGetDistToReach() {
    p.setDistToReach(10);
    assertEquals(p.getDistToReach(), 10, 0);
  }

  @Before
  public void setUp() {
    // Creates new instance to fill instance field
    p = new Pathpoint(1, 1);
  }

  @After
  public void tearDown() {
    // Clears instance
    p = null;
  }

}
