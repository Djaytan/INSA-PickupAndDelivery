package fr.insa.lyon.ifa1.controller;

import fr.insa.lyon.ifa1.models.map.GeoMap;
import fr.insa.lyon.ifa1.models.map.Intersection;
import fr.insa.lyon.ifa1.xml.XMLDeserialization;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class GeoMapController {

    private static final Logger LOGGER = Logger.getLogger(GeoMapController.class.getName());

    private static final GeoMap GEO_MAP = new GeoMap();

    public static GeoMap getModel() { return GEO_MAP; }

    public static void importGeoMap(File file) {

        try { XMLDeserialization.deserializeMap(file); }
        catch (SAXException e)
        { LOGGER.log(Level.SEVERE, "Error during XML map file content reading", e); }
        catch (ParserConfigurationException e)
        { LOGGER.log(Level.SEVERE, "Something went wrong in map XML parser configuration", e); }
        catch (IOException e)
        { LOGGER.log(Level.SEVERE, "Error during XML map file manipulation", e); }

    }

    public static List<Map<String, Map<String, Double>>> getSegments() {

        return GEO_MAP.getSegments().stream().map(segment -> Map.ofEntries(
                Map.entry("origin", Map.ofEntries(
                        Map.entry("x", segment.getOrigin().getLongitude()),
                        Map.entry("y", segment.getOrigin().getLatitude())
                )),
                Map.entry("destination", Map.ofEntries(
                        Map.entry("x", segment.getDest().getLongitude()),
                        Map.entry("y", segment.getDest().getLatitude())
                ))
        )).collect(Collectors.toList());

    }

    public static Collection<Intersection> getIntersections() {
        return GEO_MAP.getIntersections();
    }

    public static Map<String, Map<String, Double>> getRange() {

        double minLatitude = 90., maxLatitude = -90., minLongitude = 180., maxLongitude = -180.;

        for (Intersection intersection : GEO_MAP.getIntersections()) {

            double latitude = intersection.getLatitude();
            double longitude = intersection.getLongitude();

            if (latitude < minLatitude) { minLatitude = latitude; }
            else if (latitude > maxLatitude) { maxLatitude = latitude; }

            if (longitude < minLongitude) { minLongitude = longitude; }
            else if (longitude > maxLongitude) { maxLongitude = longitude; }

        }

        return Map.ofEntries(
                Map.entry("x", Map.ofEntries(
                        Map.entry("min", minLongitude),
                        Map.entry("max", maxLongitude)
                )),
                Map.entry("y", Map.ofEntries(
                        Map.entry("min", minLatitude),
                        Map.entry("max", maxLatitude)
                ))
        );

    }

    public static Intersection getClosestIntersection(double x, double y) {
        Intersection closestIntersection = null;
        double distance = Double.MAX_VALUE;
        double tmpDistance;

        for (Intersection intersection : GeoMapController.getIntersections()) {

            tmpDistance = Math.sqrt((y - intersection.getLatitude()) * (y - intersection.getLatitude()) + (x - intersection.getLongitude()) * (x - intersection.getLongitude()));
            if(tmpDistance < distance) {
                closestIntersection = intersection;
                distance = tmpDistance;
            }
        }

        return closestIntersection;
    }

}
