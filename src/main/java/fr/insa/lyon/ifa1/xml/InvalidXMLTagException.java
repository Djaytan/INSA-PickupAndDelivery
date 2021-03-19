package fr.insa.lyon.ifa1.xml;

import org.xml.sax.SAXException;

public class InvalidXMLTagException extends SAXException {

  private static final long serialVersionUID = 1L;

  /**
   * Constructor of an XML exception.
   *
   * <p>This exception may be raised when an element
   *
   * @param message
   */
  public InvalidXMLTagException(String message) {
    super(message);
  }
}
