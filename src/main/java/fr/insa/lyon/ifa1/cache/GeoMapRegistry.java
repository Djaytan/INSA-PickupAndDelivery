package fr.insa.lyon.ifa1.cache;

import fr.insa.lyon.ifa1.models.map.GeoMap;

/**
 * The static class in charges of the in memory storage of the {@link GeoMap} instance.
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
