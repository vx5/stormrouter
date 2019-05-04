package edu.brown.cs.stormrouter.weather;

/**
 * @author vx5
 *
 *         Class which houses scoring rubric for TimePoints, static methods to
 *         manipulate that metric, and the static scoreWeather() method, which
 *         scores TimePoints.
 */
public final class WSet {
  // Stores fixed weather amount cutoffs
  // Please see the README for information on what sources cutoffs
  // were derived and pulled from
  private static final double PRECIP_INTENSITY_CUTOFF = 0.01;
  private static final double RAIN_INTEN_LOW_CUTOFF = 0.1;
  private static final double RAIN_INTEN_MID_CUTOFF = 0.3;
  private static final double SNOW_INTEN_LOW_CUTOFF = 0.05;
  private static final double SNOW_INTEN_MID_CUTOFF = 0.15;
  private static final double TEMP_CUTOFF_FAH = 105;
  private static final double VISIBILITY_HARD_CUTOFF = 0.25;
  private static final double VISIBILITY_SOFT_CUTOFF = 0.62;
  // Note, for the wind cutoffs, that conditions with the high gust cutoff
  // receive the high wind score. For mid and low, conditions that satisfy
  // either the speed or gust cutoff receive the corresponding score
  private static final double WIND_GUST_HIGH_CUTOFF = 65;
  private static final double WIND_GUST_MID_CUTOFF = 58;
  private static final double WIND_GUST_LOW_CUTOFF = 45;
  private static final double WIND_SPEED_MID_CUTOFF = 40;
  private static final double WIND_SPEED_LOW_CUTOFF = 30;
  // Stores fixed weather scores
  // For anyone editing, please note that lower scores represent better
  // conditions
  private static final int RAIN_LOW_SCORE = 5;
  private static final int RAIN_MID_SCORE = 15;
  private static final int RAIN_HIGH_SCORE = 40;
  private static final int SNOW_LOW_SCORE = 5;
  private static final int SNOW_MID_SCORE = 25;
  private static final int SNOW_HIGH_SCORE = 65;
  private static final int TEMP_HIGH_SCORE = 30;
  private static final int VISIBILITY_HARD_SCORE = 30;
  private static final int VISIBILITY_SOFT_SCORE = 15;
  private static final int WIND_HIGH_SCORE = 100;
  private static final int WIND_MID_SCORE = 40;
  private static final int WIND_LOW_SCORE = 15;
  // Stores values related to manipulation of scores
  private static double rainFactor = 1;
  private static double snowFactor = 1;
  private static double windFactor = 1;

  /**
   * Uses the current scoring rubric to assign a weather score to the given
   * TimePoint. Lower scores represent better conditions.
   * 
   * @param weather TimePoint to be scored
   * @return integer score representing optimality of weather conditions
   */
  public static int scoreWeather(TimePoint weather) {
    // Initializes variable to count score
    int pointScore = 0;
    // Checks for case of precipitation
    double precipIntensity = weather.getPrecipIntensity();
    if (precipIntensity > PRECIP_INTENSITY_CUTOFF) {
      // Checks for case of rain
      if (weather.getPrecipType().equals("rain")) {
        // Checks for different amounts of precipitation, awards points
        // accordingly
        if (precipIntensity < RAIN_INTEN_LOW_CUTOFF) {
          pointScore += RAIN_LOW_SCORE;
        } else if (precipIntensity < RAIN_INTEN_MID_CUTOFF) {
          pointScore += RAIN_MID_SCORE * rainFactor;
        } else {
          pointScore += RAIN_HIGH_SCORE * rainFactor;
        }
        // Checks for case of sleet or snow
      } else {
        // Checks for different amounts of precipitation, awards points
        // accordingly
        if (precipIntensity < SNOW_INTEN_LOW_CUTOFF) {
          pointScore += SNOW_LOW_SCORE;
        } else if (precipIntensity < SNOW_INTEN_MID_CUTOFF) {
          pointScore += SNOW_MID_SCORE * snowFactor;
        } else {
          pointScore += SNOW_HIGH_SCORE * snowFactor;
        }
      }
    }
    // Checks for case of very high temperature
    if (weather.getTemperature() > TEMP_CUTOFF_FAH) {
      pointScore += TEMP_HIGH_SCORE;
    }
    // Checks for case of low visibility
    double visibility = weather.getVisibility();
    if (visibility < VISIBILITY_HARD_CUTOFF) {
      pointScore += VISIBILITY_HARD_SCORE;
    } else if (visibility < VISIBILITY_SOFT_CUTOFF) {
      pointScore += VISIBILITY_SOFT_SCORE;
    }
    // Checks for case of high wind speed / wind gust
    double windSpeed = weather.getWindSpeed();
    double windGust = weather.getWindGust();
    if (windGust > WIND_GUST_HIGH_CUTOFF) {
      pointScore += WIND_HIGH_SCORE * windFactor;
    } else if (windGust > WIND_GUST_MID_CUTOFF
        || windSpeed > WIND_SPEED_MID_CUTOFF) {
      pointScore += WIND_MID_SCORE * windFactor;
    } else if (windGust > WIND_GUST_LOW_CUTOFF
        || windSpeed > WIND_SPEED_LOW_CUTOFF) {
      pointScore += WIND_LOW_SCORE * windFactor;
    }
    // Returns totaled score
    return pointScore;
  }

  /**
   * Mutator that allows for altering of how rain is scored. Factor is set to 1
   * by default, and higher factors will increase the weight given to
   * sub-optimal rain conditions relative to other challenges, while lower
   * factors will decrease the weight.
   * 
   * @param newRainFactor new rain factor to be used in rubric
   */
  public static void setRainFactor(double newRainFactor) {
    rainFactor = newRainFactor;
  }

  /**
   * Mutator that allows for altering of how sleet and snow are scored. Factor
   * is set to 1 by default, and higher factors will increase the weight given
   * to sub-optimal sleet/snow conditions relative to other challenges, while
   * lower factors will decrease the weight.
   * 
   * @param newSnowFactor new sleet / snow factor to be used in rubric
   */
  public static void setSnowFactor(double newSnowFactor) {
    snowFactor = newSnowFactor;
  }

  /**
   * Mutator that allows for altering of how wind is scored. Factor is set to 1
   * by default, and higher factors will increase the weight given to
   * sub-optimal wind conditions relative to other challenges, while lower
   * factors will decrease the weight.
   * 
   * @param newWindFactor new wind factor to be used in rubric
   */
  public static void setWindFactor(double newWindFactor) {
    windFactor = newWindFactor;
  }

}
