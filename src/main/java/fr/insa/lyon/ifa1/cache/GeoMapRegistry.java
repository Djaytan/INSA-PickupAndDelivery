package fr.insa.lyon.ifa1.cache;

import fr.insa.lyon.ifa1.models.map.GeoMap;

/**
 * Singleton class that corresponds to the {@link GeoMap} application instance registry.
 *
 * @author IFA1
 */
public final class GeoMapRegistry {

  private static final GeoMap geoMap = new GeoMap();

  /** Private constructor */
  private GeoMapRegistry() {}

  /**
   * Returns the {@link GeoMap} instance of the application.
   *
   * @return The {@link GeoMap} instance of the application.
   */
  public static GeoMap getGeoMap() {
    return geoMap;
  }
}
