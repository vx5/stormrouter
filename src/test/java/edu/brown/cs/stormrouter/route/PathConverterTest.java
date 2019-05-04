package edu.brown.cs.stormrouter.route;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import edu.brown.cs.stormrouter.conversions.TimeZoneOps;
import edu.brown.cs.stormrouter.conversions.Units;
import edu.brown.cs.stormrouter.directions.LatLon;
import edu.brown.cs.stormrouter.directions.Segment;
import edu.brown.cs.stormrouter.main.RouteWaypoint;

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
      // Constructs dummy stop waypoint
      RouteWaypoint wp = new RouteWaypoint();
      wp.waypoint = new double[] {
          41.837006, -71.389919
      };
      wp.duration = 10;
      RouteWaypoint[] w = new RouteWaypoint[] {
          wp
      };
      // Calculate valid departure time (hour from now)
      long eastCoastOffset = TimeZoneOps
          .getCurrentMsAhead(System.currentTimeMillis(), 41.835265, -71.389404);
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

  }

  @Test
  public void testConvertPathErrorPathTooLong() {

  }

}
