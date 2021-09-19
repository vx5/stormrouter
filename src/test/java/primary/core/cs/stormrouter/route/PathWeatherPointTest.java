package primary.core.cs.stormrouter.route;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

/**
 * @author vx5
 *
 *         Please note that this class has private instance variables without
 *         accessor methods, because those variables are parsed through JSON.
 *         They are thus implicitly tested through the by-hand tests discussed
 *         in the README, but cannot be tested through JUnit tests.
 */
public class PathWeatherPointTest {

  @Test
  public void testConstruction() {
    assertNotNull(new PathWeatherPoint(0, 0, "", "", 0));
  }

}
