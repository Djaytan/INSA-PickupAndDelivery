package fr.insa.lyon.ifa1.xml;

import fr.insa.lyon.ifa1.cache.GeoMapRegistry;
import fr.insa.lyon.ifa1.cache.PlanningRequestRegistry;
import fr.insa.lyon.ifa1.models.map.Intersection;
import fr.insa.lyon.ifa1.models.request.Depot;
import fr.insa.lyon.ifa1.models.request.Depot.Time;
import fr.insa.lyon.ifa1.models.request.DurationPassagePoint;
import fr.insa.lyon.ifa1.models.request.Request;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * This class represents the XML handler for requests description file.
 *
 * @author IFA1
 */
public class XMLRequestsHandler extends DefaultHandler {

  public static final String PICKUP_TYPE = "pickup";
  public static final String DELIVERY_TYPE = "delivery";

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes)
      throws SAXException {
    switch (qName) {
      case "planningRequest":
        {
          /* Do nothing */
          break;
        }
      case "depot":
        {
          String intersectionId = attributes.getValue("address");
          String departureTimeStr = attributes.getValue("departureTime");
          String[] departureTimeSplited = departureTimeStr.split(":");
          int hours = Integer.parseInt(departureTimeSplited[0]);
          int minutes = Integer.parseInt(departureTimeSplited[1]);
          int seconds = Integer.parseInt(departureTimeSplited[2]);
          Time departureTime = new Time(hours, minutes, seconds);
          Intersection intersection = GeoMapRegistry.getGeoMap().getIntersection(intersectionId);
          Depot depot = new Depot(intersection, departureTime);
          PlanningRequestRegistry.createPlanningRequest(depot);
          break;
        }
      case "request":
        {
          String pickupIntersectionId = attributes.getValue("pickupAddress");
          String deliveryIntersectionId = attributes.getValue("deliveryAddress");
          int pickupDuration = Integer.parseInt(attributes.getValue("pickupDuration"));
          int deliveryDuration = Integer.parseInt(attributes.getValue("deliveryDuration"));
          Intersection pickupIntersection =
              GeoMapRegistry.getGeoMap().getIntersection(pickupIntersectionId);
          Intersection deliveryIntersection =
              GeoMapRegistry.getGeoMap().getIntersection(deliveryIntersectionId);
          DurationPassagePoint pickupPassagePoint =
              new DurationPassagePoint(pickupIntersection, pickupDuration, PICKUP_TYPE);
          DurationPassagePoint delivertPassagePoint =
              new DurationPassagePoint(deliveryIntersection, deliveryDuration, DELIVERY_TYPE);
          Request request = new Request(pickupPassagePoint, delivertPassagePoint);
          PlanningRequestRegistry.getPlanningRequest().addRequest(request);
          break;
        }
      default:
        {
          throw new InvalidXMLTagException("Invalid XML tag");
        }
    }
  }
}
