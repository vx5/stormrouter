package primary.core.cs.stormrouter.directions;

/**
 * Custom exception class for errors in processing directions.
 */
public class DirectionsException extends Exception {
  /**
   *
   * @param message - The error message that will be displayed
   */
  public DirectionsException(String message) {
    super(message);
  }

  /**
   *
   * @param message - The error message that will be displayed
   * @param cause - Thowable instance for error propagation
   */
  public DirectionsException(String message, Throwable cause) {
    super(message, cause);
  }
}
