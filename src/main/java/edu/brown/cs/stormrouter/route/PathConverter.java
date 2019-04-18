package edu.brown.cs.stormrouter.route;

public final class PathConverter {
  public static final PathConverter inst = new PathConverter();

  private PathConverter() {
  }

  public PathConverter getInst() {
    return inst;
  }

  public Path convertPath() {
    // TODO:
    // 1. Take in input path (change parameters)
    // 1a. Make path with offset of 0
    Path centerPath = new Path(0);
    // 2. Iterate through path, getting information on each point, converting to
    // waypoint, adding to path
    // 3. Return path
    return centerPath;
  }

}
