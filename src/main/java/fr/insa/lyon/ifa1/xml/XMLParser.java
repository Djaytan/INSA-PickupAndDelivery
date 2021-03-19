package fr.insa.lyon.ifa1.xml;

import fr.insa.lyon.ifa1.models.map.GeoMap;
import fr.insa.lyon.ifa1.models.map.Intersection;
import fr.insa.lyon.ifa1.models.map.Segment;
import fr.insa.lyon.ifa1.models.request.Depot;
import fr.insa.lyon.ifa1.models.request.DurationPassagePoint;
import fr.insa.lyon.ifa1.models.request.PlanningRequest;
import fr.insa.lyon.ifa1.models.request.Request;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class XMLParser {

    public XMLParser() { }

    public Document xmlFileToString(String filename)
            throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();
        df.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, ""); // Compliant
        df.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, ""); // compliant
        DocumentBuilder builder = df.newDocumentBuilder();

        try (FileInputStream fis = new FileInputStream(filename)) {
            return builder.parse(new ByteArrayInputStream(fis.readAllBytes()));
        }

    }

    public GeoMap parseGeoMap(Document xml) {
        Element root = xml.getDocumentElement();

        NodeList intersectionsList = root.getElementsByTagName("intersection");
        NodeList segmentsList = root.getElementsByTagName("segment");
        Map<String, Intersection> intersections = new HashMap<>();
        Map<String, Map<String, Segment>> segments = new HashMap<>();

        for (int i = 0; i < intersectionsList.getLength(); i++) {
            Node intersectionItem = intersectionsList.item(i);
            String id = intersectionItem.getAttributes().getNamedItem("id").getNodeValue();
            intersections.put(id, new Intersection(id,
                    Float.parseFloat(intersectionItem.getAttributes().getNamedItem("latitude").getNodeValue()),
                    Float.parseFloat(intersectionItem.getAttributes().getNamedItem("longitude").getNodeValue())));
            segments.put(id, new HashMap<>());
        }

        for (int i = 0; i < segmentsList.getLength(); i++) {
            Node segmentItem = segmentsList.item(i);
            String origin = segmentItem.getAttributes().getNamedItem("origin").getNodeValue();
            String dest = segmentItem.getAttributes().getNamedItem("destination").getNodeValue();

            segments.get(origin).put(
                    dest,
                    new Segment(
                            Double.parseDouble(segmentItem.getAttributes().getNamedItem("length").getNodeValue()),
                            segmentItem.getAttributes().getNamedItem("name").getNodeValue(),
                            intersections.get(origin),
                            intersections.get(dest)
                    )
            );
        }

        return new GeoMap(intersections, segments);
    }

    public PlanningRequest parsePlanning(Document xml, GeoMap gm) {
        Element root = xml.getDocumentElement();

        NodeList requestsList = root.getElementsByTagName("request");
        Request[] requests = new Request[requestsList.getLength()];
        for (int i = 0; i < requestsList.getLength(); i++) {
            Node requestItem = requestsList.item(i);

            requests[i] = new Request(
                    new DurationPassagePoint(
                            gm.getIntersection(requestItem.getAttributes().getNamedItem("pickupAddress").getNodeValue()),
                            Integer.parseInt(requestItem.getAttributes().getNamedItem("pickupDuration").getNodeValue()),
                            "pickup"),
                    new DurationPassagePoint(
                            gm.getIntersection(requestItem.getAttributes().getNamedItem("deliveryAddress").getNodeValue()),
                            Integer.parseInt(requestItem.getAttributes().getNamedItem("deliveryDuration").getNodeValue()),
                            "delivery")
            );
        }

        Node depotItem = root.getElementsByTagName("depot").item(0);
        int[] time = Arrays.stream(depotItem.getAttributes().getNamedItem("departureTime").getNodeValue().split(":"))
                .mapToInt(Integer::parseInt).toArray();
        Depot depot = new Depot(
                gm.getIntersection(depotItem.getAttributes().getNamedItem("address").getNodeValue()),
                new Depot.Time(time[0], time[1], time[2]));

        return new PlanningRequest(Arrays.asList(requests), depot);
    }
}
