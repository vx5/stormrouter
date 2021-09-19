package primary.core.cs.stormrouter.route;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import primary.core.cs.stormrouter.conversions.TimeZoneOps;
import primary.core.cs.stormrouter.conversions.Units;
import primary.core.cs.stormrouter.main.RouteHandler;
import primary.core.cs.stormrouter.main.RouteHandler.RouteWaypoint;
import primary.core.cs.stormrouter.directions.LatLon;
import primary.core.cs.stormrouter.directions.Segment;

public class PathConverterTest {

  @Test
  public void testConvertPathSuccess() {
    try {
      // Constructs dummy input path
      // Coordinates are all on east coast
      List<Segment> inputPath = new ArrayList<Segment>();
      inputPath.add(new Segment(new LatLon(41.835265, -71.389404),
          new LatLon(41.837006, -71.389919), 1, 1, "1", "1", 1, false));
      inputPath.add(new Segment(new LatLon(41.837006, -71.389919),
          new LatLon(41.839068, -71.390458), 1, 1, "1", "1", 1, true));
      // TODO: Remove 'new RouteHandler()' when PathConverter changed
      // Constructs dummy stop waypoint
      RouteWaypoint wp = new RouteHandler().new RouteWaypoint();
      wp.setWaypoint(new double[] {
          41.837006, -71.389919
      });
      wp.setDuration(10);
      RouteWaypoint[] w = new RouteWaypoint[] {
          wp
      };
      // Calculate valid departure time (hour from now)
      long eastCoastOffset = TimeZoneOps.getCurrentMsAhead(
          System.currentTimeMillis() / 1000L, 41.835265, -71.389404);
      long departureTime = (long) ((System.currentTimeMillis()
          + eastCoastOffset) / (double) 1000) + Units.hrToS(1);
      // Generate path
      Path p = PathConverter.convertPath(inputPath, w, departureTime);
      // Check general path characteristics
      // Check for hard-set departure time
      assertEquals(p.getStartTime(), departureTime);
      // Check for path characteristics
      List<Pathpoint> pathPts = p.getPathpoints();
      assertEquals(pathPts.size(), 3);
      assertEquals(pathPts.get(0).getTime(), departureTime);
      // Checks for properly integrated waypoint
      assertEquals(pathPts.get(2).getTime(),
          departureTime + 2 + (10 * Units.S_PER_MIN));
      // Checks for properly calculated distance
      assertEquals(pathPts.get(2).getDistToReach(), 2, 0);
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void testConvertPathErrorNoPath() {
    try {
      // Construct empty input path
      List<Segment> inputPath = new ArrayList<Segment>();
      // Construct empty RouteWaypoint array
      RouteWaypoint[] r = new RouteWaypoint[0];
      // Attempt to get path
      Path p = PathConverter.convertPath(inputPath, r, 0);
      // Fail if no exception thrown
      fail();
    } catch (Exception e) {
      // Check for correct message
      String msg = e.getMessage();
      assertEquals(msg, "No path");
    }
  }

  @Test
  public void testConvertPathErrorInvalidDepartureTime() {
    try {
      // Constructs dummy input path
      List<Segment> inputPath = new ArrayList<Segment>();
      inputPath.add(new Segment(new LatLon(41.835265, -71.389404),
          new LatLon(41.837006, -71.389919), 1, 1, "1", "1", 1, false));
      // Calculate valid departure time (hour from now)
      long eastCoastOffset = TimeZoneOps.getCurrentMsAhead(
          System.currentTimeMillis() / 1000L, 41.835265, -71.389404);
      long departureTime = (long) ((System.currentTimeMillis()
          + eastCoastOffset) / (double) 1000) + Units.hrToS(-3);
      // Generate path
      PathConverter.convertPath(inputPath, new RouteWaypoint[0], departureTime);
      // If no exception thrown, fail test
      fail();
    } catch (Exception e) {
      // Check for correct message
      String msg = e.getMessage();
      assertEquals(msg, "Invalid departure time");
    }
  }

  @Test
  public void testConvertPathErrorPathTooLong() {
    try {
      // Constructs dummy input path
      // Segment has high duration to prevent successful path calculation
      List<Segment> inputPath = new ArrayList<Segment>();
      inputPath.add(new Segment(new LatLon(41.835265, -71.389404),
          new LatLon(41.837006, -71.389919), 1, 999999999, "1", "1", 1, false));
      // Calculate valid departure time (hour from now)
      long eastCoastOffset = TimeZoneOps.getCurrentMsAhead(
          System.currentTimeMillis() / 1000L, 41.835265, -71.389404);
      long departureTime = (long) ((System.currentTimeMillis()
          + eastCoastOffset) / (double) 1000) + Units.hrToS(1);
      // Generate path
      PathConverter.convertPath(inputPath, new RouteWaypoint[0], departureTime);
      // Fail if error is not thrown
      fail();
    } catch (Exception e) {
      // Check for correct message
      String msg = e.getMessage();
      assertEquals(msg, "Path too long");
    }
  }

}
