package conversions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Date;
import java.util.TimeZone;

import org.junit.Test;

import primary.core.cs.stormrouter.conversions.TimeZoneOps;

public class TimeZoneOpsTest {

  @Test
  public void testGetCurrentMsAhead() {
    // Test for same time zone
    double eastCoastLat = 42.090754;
    double eastCoastLong = -71.265124;
    try {
      long genOffset = TimeZoneOps.getCurrentMsAhead(
          System.currentTimeMillis() / 1000L, eastCoastLat, eastCoastLong);
      assertEquals(genOffset, getExpectedOffset("America/New_York"));
    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }
    // Test for different time zone
    // Sri Lanka is used because of its offset, different-signed UTC, and not
    // being offset by an hour
    double colomboLat = 6.923536;
    double colomboLong = 79.935510;
    try {
      long genOffset = TimeZoneOps.getCurrentMsAhead(
          System.currentTimeMillis() / 1000L, colomboLat, colomboLong);
      assertEquals(genOffset, getExpectedOffset("Asia/Colombo"));
    } catch (Exception e) {
      fail();
    }
  }

  private long getExpectedOffset(String timeZone) {
    TimeZone localZone = TimeZone.getTimeZone(timeZone);
    TimeZone thisZone = TimeZone.getDefault();
    // Stores raw offsets to standard time
    long localZoneOffset = localZone.getRawOffset();
    long thisZoneOffset = thisZone.getRawOffset();
    // Checks for daylight savings, makes appropriate adjustments if necessary
    if (localZone.inDaylightTime(new Date(System.currentTimeMillis()))) {
      localZoneOffset += localZone.getDSTSavings();
    }
    if (thisZone.inDaylightTime(new Date(System.currentTimeMillis()))) {
      thisZoneOffset += thisZone.getDSTSavings();
    }
    // Return millisecond difference in start time
    return localZoneOffset - thisZoneOffset;
  }

}
