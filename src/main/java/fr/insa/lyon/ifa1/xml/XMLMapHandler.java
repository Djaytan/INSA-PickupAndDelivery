package fr.insa.lyon.ifa1.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import fr.insa.lyon.ifa1.cache.GeoMapRegistry;
import fr.insa.lyon.ifa1.models.map.Intersection;

/**
 * This class represents the XML handler for map description file of the city.
 *
 * @author IFA1
 */
public class XMLMapHandler extends DefaultHandler {

    @Override
    public void startElement (String uri, String localName, String qName, Attributes attributes) throws SAXException {
        switch (qName) {
            case "map": {
                /* Do nothing */
                break;
            }
            case "intersection": {
                String id = attributes.getValue("id");
                double latitude = Double.parseDouble(attributes.getValue("latitude"));
                double longitude = Double.parseDouble(attributes.getValue("longitude"));
                Intersection intersection = new Intersection(id, latitude, longitude);
                GeoMapRegistry.getGeoMap().addIntersection(intersection);
                break;
            }
            case "segment": {
                break;
            }
            default: {

                break;
            }
        }
    }
}
