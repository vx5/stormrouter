package conversions;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import edu.brown.cs.stormrouter.conversions.Units;

public class UnitsTest {

  @Test
  public void testPublicConstants() {
    // Tests non-inverted forms of constants
    assertEquals(Units.MS_PER_S, 1000);
    assertEquals(Units.S_PER_MIN, 60);
    assertEquals(Units.MIN_PER_HR, 60);
    assertEquals(Units.MILES_PER_DEGREE, 69, 0);
    assertEquals(Units.METERS_PER_MILE, 1609.34, 0);
    assertEquals(Units.MPH_TO_METERS_PER_S, 0.44704, 0);
    assertEquals(Units.IN_TO_MM, 25.4, 0);
    // Tests inverted forms of constants
    assertEquals(Units.S_PER_MS, 1 / Units.MS_PER_S, 0.05);
    assertEquals(Units.MIN_PER_S, 1 / Units.S_PER_MIN, 0.05);
    assertEquals(Units.HR_PER_MIN, 1 / Units.MIN_PER_HR, 0.05);
    assertEquals(Units.DEGREES_PER_MILE, 1 / Units.MILES_PER_DEGREE, 0.05);
    assertEquals(Units.MILES_PER_METER, 1 / Units.METERS_PER_MILE, 0.05);
    assertEquals(Units.METERS_PER_S_TO_MPH, 1 / Units.MPH_TO_METERS_PER_S,
        0.05);
    assertEquals(Units.MM_TO_IN, 1 / Units.IN_TO_MM, 0.05);
  }

  @Test
  public void testHrsToS() {
    // Test for case of 0
    assertEquals(Units.hrToS(0), 0);
    // Test case of whole number
    assertEquals(Units.hrToS(1), 3600);
    // Test case of decimal number
    assertEquals(Units.hrToS(0.5), 1800);
  }

  @Test
  public void testUnixToHrsFromNow() {
    // Test for case of zero hour
    long halfHrFromNowUnix = (System.currentTimeMillis() / 1000)
        + Units.hrToS(0.5);
    assertEquals(Units.unixToHrsFromNow(halfHrFromNowUnix), 0);
    // Test for case of non-zero hour
    long TwoHrFromNowUnix = (System.currentTimeMillis() / 1000)
        + Units.hrToS(2.5);
    assertEquals(Units.unixToHrsFromNow(TwoHrFromNowUnix), 2);
  }
}
