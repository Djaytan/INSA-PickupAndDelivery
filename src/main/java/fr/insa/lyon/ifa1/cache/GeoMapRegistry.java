package fr.insa.lyon.ifa1.cache;

import fr.insa.lyon.ifa1.models.map.GeoMap;
import fr.insa.lyon.ifa1.models.map.Intersection;
import fr.insa.lyon.ifa1.models.map.Segment;

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

    /**
     * Adds an {@link Intersection} to the {@link GeoMap}.
     * 
     * @param intersection The {@link Intersection} to add.
     */
    public static void addIntersection(Intersection intersection) {
        geoMap.addIntersection(intersection);
    }

    /**
     * Adds a {@link Segment} to the {@link GeoMap}.
     * 
     * @param segment The {@link Segment} to add.
     */
    public static void addSegment(Segment segment) {
        geoMap.addSegment(segment);
    }
}
