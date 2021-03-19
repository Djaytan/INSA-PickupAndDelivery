package fr.insa.lyon.ifa1;

import fr.insa.lyon.ifa1.cache.GeoMapRegistry;
import fr.insa.lyon.ifa1.xml.XMLDeserialization;
import fr.insa.lyon.ifa1.xml.XMLMapHandler;
import fr.insa.lyon.ifa1.xml.XMLRequestsHandler;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/** Hello world! */
public class App {

  private static final Logger LOGGER = Logger.getLogger(App.class.getName());
  private static final String TEST_MAP_FILE = "smallMap.xml";
  private static final String TEST_REQUESTS_FILE = "requestsSmall1.xml";

  public static void main(String[] args) {
    System.out.println("Hello World!");
    try {
      InputStream is = XMLDeserialization.class.getClassLoader().getResourceAsStream(TEST_MAP_FILE);
      XMLMapHandler xmlMapHandler = new XMLMapHandler();
      XMLDeserialization.deserialize(is, xmlMapHandler);
      LOGGER.info(GeoMapRegistry.getGeoMap().toString());
    } catch (SAXException e) {
      LOGGER.log(Level.SEVERE, "Error during XML map file content reading", e);
    } catch (ParserConfigurationException e) {
      LOGGER.log(Level.SEVERE, "Something went wrong in map XML parser configuration", e);
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, "Error during XML map file manipulation", e);
    }

    try {
      InputStream is =
          XMLDeserialization.class.getClassLoader().getResourceAsStream(TEST_REQUESTS_FILE);
      XMLRequestsHandler xmlRequestsHandler = new XMLRequestsHandler();
      XMLDeserialization.deserialize(is, xmlRequestsHandler);
    } catch (SAXException e) {
      LOGGER.log(Level.SEVERE, "Error during XML requests file content reading", e);
    } catch (ParserConfigurationException e) {
      LOGGER.log(Level.SEVERE, "Something went wrong in requests XML parser configuration", e);
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, "Error during XML requests file manipulation", e);
    }
  }
}
