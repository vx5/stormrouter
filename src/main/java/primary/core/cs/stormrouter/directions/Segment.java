package primary.core.cs.stormrouter.directions;

/**
 * Models a single segment of a route, including the start and endpoints, length
 * and expected duration, and the name and driving instructions.
 */
public class Segment {
  private LatLon start;
  private LatLon end;
  private double length;
  private double duration;
  private String name;
  private String instructions;
  private boolean terminal;
  private int type;

  /**
   * Constructs a new segment object from all of the data returned by the
   * routing API.
   * @param start        - A LatLon representation of the starting point
   * @param end          - A LatLon representation of the ending point
   * @param length       - The length of the segment in meters
   * @param duration     - The expected duration of travel in seconds
   * @param name         - The name of the segment (such as street name)
   * @param instructions - The driving directions for the segment
   * @param type         - Integer value specifying the type of direction
   * @param terminal     - Whether this is the final segment of a subpath
   */
  public Segment(LatLon start, LatLon end, double length, double duration,
      String name, String instructions, int type, boolean terminal) {
    this.start = start;
    this.end = end;
    this.length = length;
    this.duration = duration;
    this.name = name;
    this.instructions = instructions;
    this.type = type;
    this.terminal = terminal;
  }

  public LatLon getStart() {
    return start;
  }

  public LatLon getEnd() {
    return end;
  }

  public double getLength() {
    return length;
  }

  public double getDuration() {
    return duration;
  }

  public String getName() {
    return name;
  }

  public String getInstructions() {
    return instructions;
  }

  public int getType() {
    return type;
  }

  public boolean isTerminal() {
    return terminal;
  }
}
