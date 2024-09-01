package network.second.message;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
public class ClientToServerMessage extends Message {
    public String message;

    public ClientToServerMessage(String commandName, String session, String message) {
        super(commandName, session);
        this.message = message;
    }

    public Document toXMLDocument() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();

        Element rootElement = document.createElement("command");
        rootElement.setAttribute("name", commandName);

        if (session != null) {
            Element sessionElement = document.createElement("session");
            sessionElement.setTextContent(session);
            rootElement.appendChild(sessionElement);
        }

        if (message != null) {
            Element messageElement = document.createElement("message");
            messageElement.setTextContent(message);
            rootElement.appendChild(messageElement);
        }

        document.appendChild(rootElement);

        return document;
    }

}
