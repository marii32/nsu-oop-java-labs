package network.second.message;

import network.second.message.Message;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
public class SuccessMessage extends Message {
    public SuccessMessage(String commandName, String session) {
        super(commandName, session);
    }

    public Document toXMLDocument() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();

        Element successElement = doc.createElement("success");
        Element sessionElement = doc.createElement("session");
        sessionElement.setTextContent(session);
        successElement.appendChild(sessionElement);
        doc.appendChild(successElement);

        return doc;
    }
}
