package fr.insa.lyon.ifa1.cache;

import fr.insa.lyon.ifa1.models.map.GeoMap;

/**
 * Static class that corresponds to the {@link GeoMap} application instance registry.
 *
 * @author IFA1
 */
public final class GeoMapRegistry {

  private static final GeoMap geoMap = new GeoMap();

  /**
   * Returns the {@link GeoMap} instance of the application.
   *
   * @return The {@link GeoMap} instance of the application.
   */
  public static GeoMap getGeoMap() {
    return geoMap;
  }
}
