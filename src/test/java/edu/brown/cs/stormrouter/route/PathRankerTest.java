package edu.brown.cs.stormrouter.route;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import edu.brown.cs.stormrouter.conversions.TimeZoneOps;
import edu.brown.cs.stormrouter.conversions.Units;
import edu.brown.cs.stormrouter.directions.LatLon;
import edu.brown.cs.stormrouter.directions.Segment;
import edu.brown.cs.stormrouter.main.RouteHandler;

public class PathRankerTest {

  @Test
  public void testBestPath() {
    try {
      // Constructs dummy input path
      // Segment has high duration to prevent successful path calculation
      List<Segment> inputPath = new ArrayList<Segment>();
      inputPath.add(new Segment(new LatLon(41.835265, -71.389404),
          new LatLon(41.837006, -71.389919), 1, 40000, "1", "1", 1, false));
      // Calculate valid departure time (hour from now)
      long eastCoastOffset = TimeZoneOps.getCurrentMsAhead(
          System.currentTimeMillis() / 1000L, 41.835265, -71.389404);
      long departureTime = (long) (((System.currentTimeMillis()
          + eastCoastOffset) + Units.hrToS(0.5)) / 1000);
      // Generate path
      Path p = PathConverter.convertPath(inputPath,
          new RouteHandler.RouteWaypoint[0], departureTime);
      // Generate best alternate paths' map
      Map<String, Object> m = PathRanker.bestPath(p);
      Set<String> keys = m.keySet();
      // Check for correct number of objects
      assertEquals(keys.size(), 5);
      // Check that invalid offsets not contained
      assertFalse(keys.contains("-1"));
      assertFalse(keys.contains("-2"));
      // META: Check that best path was, in fact, returned
      // Generate array of valid offsets
      String[] offsets = new String[] {
          "0", "1", "2", "5"
      };
      // Looks for best path key
      String bestPathKey = "";
      int bestPathScore = Integer.MAX_VALUE;
      for (String offset : offsets) {
        int currScore = ((PathWeatherInfo) m.get(offset)).getScore();
        if (currScore < bestPathScore) {
          bestPathScore = currScore;
          bestPathKey = offset;
        }
      }
      // Compares with given
      assertEquals((String) m.get("best"), bestPathKey);
    } catch (Exception e) {
      // Fail on any error
      fail();
    }

  }

}
