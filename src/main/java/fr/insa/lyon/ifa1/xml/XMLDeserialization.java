package fr.insa.lyon.ifa1.xml;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * This class manage the deserialization of XML files.
 *
 * @author IFA1
 */
public class XMLDeserialization {

  /**
   * Deserialization method for XML files.
   *
   * @param fileName The name of the targeted XML file.
   * @param handler The XML handler to use for deserialization.
   * @throws SAXException If general SAX errors or warnings are raised.
   * @throws ParserConfigurationException If a serious configuration error is detected.
   */
  public void deserialize(String fileName, DefaultHandler handler)
      throws SAXException, ParserConfigurationException {
    SAXParserFactory spf = SAXParserFactory.newInstance();
    SAXParser saxParser = spf.newSAXParser();
    // TODO: complete
  }

  /**
   * Deserialization method for XML files of a map description.
   *
   * @param fileName The name of the targeted XML file of map description.
   * @throws SAXException If general SAX errors or warnings are raised.
   * @throws ParserConfigurationException If a serious configuration error is detected.
   */
  public void deserializeMap(String fileName) throws SAXException, ParserConfigurationException {
    XMLMapHandler xmlMapHandler = new XMLMapHandler();
    deserialize(fileName, xmlMapHandler);
  }

  /**
   * Deserialization method for XML files of a requests description.
   *
   * @param fileName The name of the targeted XML file of requests description.
   * @throws SAXException If general SAX errors or warnings are raised.
   * @throws ParserConfigurationException If a serious configuration error is detected.
   */
  public void deserializeRequests(String fileName)
      throws SAXException, ParserConfigurationException {
    XMLRequestsHandler xmlRequestsHandler = new XMLRequestsHandler();
    deserialize(fileName, xmlRequestsHandler);
  }
}
