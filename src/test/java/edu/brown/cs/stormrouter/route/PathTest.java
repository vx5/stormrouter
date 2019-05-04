package edu.brown.cs.stormrouter.route;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;

public class PathTest {
  // Stores instance of Path to be tested
  private Path testPath = new Path(1, 1);

  @Test
  public void testConstruction() {
    assertNotNull(new Path(1, 1));
  }

  @Test
  public void testGetStartTime() {
    assertEquals(testPath.getStartTime(), 1);
  }

  @Test
  public void testGetOffset() {
    assertEquals(testPath.getOffset(), 1);
  }

  /**
   * Tests the addPathpoint() and getPathpoints() method together, due to
   * interreliance.
   */
  @Test
  public void testAddGetPathpoints() {
    // Creates Pathpoints for testing
    Pathpoint p1 = new Pathpoint(1, 1);
    Pathpoint p2 = new Pathpoint(2, 2);
    // Adds points
    testPath.addPathpoint(p1);
    testPath.addPathpoint(p2);
    // Tests for proper internal list
    List<Pathpoint> pts = testPath.getPathpoints();
    assertEquals(pts.size(), 2);
    // Check for correct order
    assertEquals(pts.get(0), p1);
    assertEquals(pts.get(1), p2);
  }

}
