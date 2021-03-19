package fr.insa.lyon.ifa1.xml;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * This class represents the XML handler for requests description file.
 *
 * @author IFA1
 */
public class XMLRequestsHandler extends DefaultHandler {

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes)
      throws SAXException {
    switch (qName) {
      case "":
        {
          break;
        }
      default:
        {
          throw new InvalidXMLTagException("Invalid XML tag");
        }
    }
  }
}
