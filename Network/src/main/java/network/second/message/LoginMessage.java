package network.second.message;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class LoginMessage extends Message {
    public String userName;

    public LoginMessage(String commandName, String session, String userName) {
        super(commandName, session);
        this.userName = userName;

    }

    public Document toXMLDocument() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();

            Element rootElement = document.createElement("command");
            rootElement.setAttribute("name", commandName);

            Element nameElement = document.createElement("name");
            nameElement.setTextContent(userName);

            rootElement.appendChild(nameElement);
            document.appendChild(rootElement);

            return document;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            return null;
        }
    }
}