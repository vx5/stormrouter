package edu.brown.cs.stormrouter.directions;

import edu.brown.cs.stormrouter.route.LatLon;

import java.util.ArrayList;
import java.util.List;

/**
 * Final helper class for converting between the compressed polyline data
 * type returned by directions APIs and a list of segments for display to
 * a mapping interface.
 */
public final class PolylineDecoder {
  /**
   *
   * @param encoded - The encoded polyline text to be read from
   * @return - A list of latitude/longitude coordinates detailing the path
   * in a form that cab be rendered to the Leaflet map.
   */
  public static List<LatLon> decodePolyline(String encoded) {
    // Keep track of the current index in the encoded string and the current
    // latitude and longitude
    int currentStringIndex = 0;
    int lat = 0;
    int lon = 0;
    List<LatLon> currentList = new ArrayList<>();

    while (currentStringIndex < encoded.length()) {
      // Keep track of the current byte value, the latest tally of how far the
      // data has been shifted from the original value, and a running total
      // result
      int currentByte = 0;
      int shiftAmount = 0;
      int currentResult = 0;

      do {
        currentByte = encoded.charAt(currentStringIndex++) - 63;
        currentResult |= (currentByte & 0x01f) << shiftAmount;

        int temporaryLat = ((currentResult & 1) != 0 ? ~(currentResult >> 1)
            : (currentResult >> 1));


        do {
          currentByte = encoded.charAt(currentStringIndex++) - 63;
          currentResult |= (currentByte & 0x01f) << shiftAmount;
        } while (currentByte >= 0x20);

        int temporaryLon = ((currentResult & 1) != 0 ? ~(currentResult >> 1)
            : (currentResult >> 1));

        do {
          currentByte = encoded.charAt(currentStringIndex++) - 63;
          currentResult |= (currentByte & 0x01f) << shiftAmount;
        } while (currentByte >= 0x20);

        lat += temporaryLat;
        lon += temporaryLon;

        LatLon coords = new LatLon((lat / 1E5), (lon / 1E5));
        currentList.add(coords);
      } while (currentByte >= 0x20);
    }

    return currentList;
  }
}
