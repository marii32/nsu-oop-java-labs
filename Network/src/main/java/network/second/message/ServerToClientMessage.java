package network.second.message;

import network.second.message.Message;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class ServerToClientMessage extends Message {
    public String eventName;
    public String message;
    public String name;

    public ServerToClientMessage(String commandName, String session, String eventName, String message, String name) {
        super(commandName, session);
        this.eventName = eventName;
        this.message = message;
        this.name = name;
    }

    public Document toXMLDocument() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();

        Element eventElement = doc.createElement("event");
        eventElement.setAttribute("name", eventName);

        Element messageElement = doc.createElement("message");
        messageElement.setTextContent(message);
        eventElement.appendChild(messageElement);

        Element nameElement = doc.createElement("name");
        nameElement.setTextContent(name);
        eventElement.appendChild(nameElement);

        doc.appendChild(eventElement);

        return doc;
    }
}
