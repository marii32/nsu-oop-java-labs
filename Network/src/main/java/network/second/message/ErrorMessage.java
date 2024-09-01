package network.second.message;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class ErrorMessage extends Message {
    private String reason;

    public ErrorMessage(String commandName, String session, String reason) {
        super(commandName, session);
        this.reason = reason;
    }

    public Document toXMLDocument() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();

        Element rootElement = doc.createElement("message");


        if (session != null) {
            Element sessionElement = doc.createElement("session");
            sessionElement.setTextContent(session);
            rootElement.appendChild(sessionElement);
        }


        if (reason != null) {
            Element messageElement = doc.createElement("messageContent");
            messageElement.setTextContent(reason);
            rootElement.appendChild(messageElement);
        }

        doc.appendChild(rootElement);

        return doc;
    }

}
