package network.second.message;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class LogoutMessage extends Message {


    public LogoutMessage(String commandName, String session) {
        super(commandName, session);

    }

    public Document toXMLDocument() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();

        Element commandElement = document.createElement("command");
        commandElement.setAttribute("name", commandName);

        Element sessionElement = document.createElement("session");
        sessionElement.setTextContent(session);

        commandElement.appendChild(sessionElement);
        document.appendChild(commandElement);

        return document;
    }
}
