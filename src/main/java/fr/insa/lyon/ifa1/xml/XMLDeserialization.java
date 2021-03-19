package fr.insa.lyon.ifa1.xml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * This static class manages the deserialization of XML files.
 *
 * @author IFA1
 */
public final class XMLDeserialization {

  /**
   * Deserialization method for XML files.
   *
   * @param file The targeted XML file.
   * @param handler The XML handler to use for deserialization.
   * @throws SAXException If general SAX errors or warnings are raised.
   * @throws ParserConfigurationException If a serious configuration error is detected.
   * @throws IOException If a problem occur during the reading process of the XML file.
   */
  public static void deserialize(File file, DefaultHandler handler)
      throws SAXException, ParserConfigurationException, IOException {
    SAXParserFactory spf = SAXParserFactory.newInstance();
    SAXParser saxParser = spf.newSAXParser();
    saxParser.parse(file, handler);
  }

  /**
   * Deserialization method for XML files.
   *
   * @param is The {@link InputStream} of the targeted XML content.
   * @param handler The XML handler to use for deserialization.
   * @throws SAXException If general SAX errors or warnings are raised.
   * @throws ParserConfigurationException If a serious configuration error is detected.
   * @throws IOException If a problem occur during the reading process of the XML file.
   */
  public static void deserialize(InputStream is, DefaultHandler handler)
      throws SAXException, ParserConfigurationException, IOException {
    SAXParserFactory spf = SAXParserFactory.newInstance();
    SAXParser saxParser = spf.newSAXParser();
    saxParser.parse(is, handler);
  }

  /**
   * Deserialization method for XML files of a map description.
   *
   * @param file The targeted XML file.
   * @throws SAXException If general SAX errors or warnings are raised.
   * @throws ParserConfigurationException If a serious configuration error is detected.
   * @throws IOException If a problem occur during the reading process of the XML file.
   */
  public static void deserializeMap(File file)
      throws SAXException, ParserConfigurationException, IOException {
    XMLMapHandler xmlMapHandler = new XMLMapHandler();
    deserialize(file, xmlMapHandler);
  }

  /**
   * Deserialization method for XML files of a requests description.
   *
   * @param file The targeted XML file.
   * @throws SAXException If general SAX errors or warnings are raised.
   * @throws ParserConfigurationException If a serious configuration error is detected.
   * @throws IOException If a problem occur during the reading process of the XML file.
   */
  public static void deserializeRequests(File file)
      throws SAXException, ParserConfigurationException, IOException {
    XMLRequestsHandler xmlRequestsHandler = new XMLRequestsHandler();
    deserialize(file, xmlRequestsHandler);
  }
}
