package fr.insa.lyon.ifa1;

import fr.insa.lyon.ifa1.cache.GeoMapRegistry;
import fr.insa.lyon.ifa1.xml.XMLDeserialization;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/** Hello world! */
public class App {

  private static final Logger LOGGER = Logger.getLogger(App.class.getName());
  private static final String TEST_MAP_FILE = "smallMap.xml";

  public static void main(String[] args) {
    System.out.println("Hello World!");
    try {
      XMLDeserialization.deserializeMap(TEST_MAP_FILE);
      LOGGER.info(GeoMapRegistry.getGeoMap().toString());
    } catch (SAXException e) {
      LOGGER.log(Level.SEVERE, "Error during XML file content reading", e);
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, "Error during XML file manipulation", e);
    } catch (ParserConfigurationException e) {
      LOGGER.log(Level.SEVERE, "Something went wrong in XML parser configuration", e);
    }
  }
}
