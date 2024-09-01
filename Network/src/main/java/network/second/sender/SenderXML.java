package network.second.sender;

import network.second.ByteMessage;
import network.second.Server;
import network.second.message.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class SenderXML implements Sender{

    Logger logger = Logger.getLogger(SenderXML.class.getName());

    public SenderXML() {
    }

    private String readMessageBytes(InputStream in, int messageLength) throws IOException {
        byte[] messageBytes = new byte[messageLength];
        in.read(messageBytes);
        return (new String(messageBytes, StandardCharsets.UTF_8));
    }

    private Document createDocument(String xmlResponse) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return (builder.parse(new ByteArrayInputStream(xmlResponse.getBytes(StandardCharsets.UTF_8))));
    }

    public String tryToConnect(ObjectOutputStream out, ObjectInputStream in, String username) {
        Document loginDocument = createLoginDocument(username, null);
        sendXMLDocument(out, loginDocument);
        return handleServerResponse(in);
    }

    private Document createLoginDocument(String username, String session) {
        LoginMessage loginMessage = new LoginMessage("login", session, username);
        return loginMessage.toXMLDocument();
    }

    public void sendChatHistory(ObjectOutputStream out, String sessionID, LinkedList<String> chatHistory) {
        try {
            ServerToClientMessage chatHistoryResponse = new ServerToClientMessage("history", sessionID, "history", chatHistory.toString(), "Server");
            sendXMLDocument(out, chatHistoryResponse.toXMLDocument());
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(ObjectOutputStream out, ServerToClientMessage registrationResponse) throws IOException, ParserConfigurationException {
        sendXMLDocument(out, registrationResponse.toXMLDocument());
    }

    private void sendXMLDocument(ObjectOutputStream out, Document doc) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.transform(new DOMSource(doc), new StreamResult(outputStream));

            byte[] messageBytes = outputStream.toByteArray();
            outputStream.close();

            ByteMessage byteMessage = new ByteMessage(messageBytes, messageBytes.length);
            sendByteMessage(out, byteMessage);
        } catch (TransformerException | IOException e) {
            e.printStackTrace();
        }
    }

    private void sendByteMessage(ObjectOutputStream out, ByteMessage byteMessage) throws IOException {

        int length = byteMessage.getData().length;
        out.writeInt(length);
        out.flush();


        out.write(byteMessage.getData());
        out.flush();
    }

    private String handleServerResponse(ObjectInputStream in) {
        String session = null;
        try {
            // Reading the length of the message first
            int length = in.readInt();

            // Reading the actual byte data
            byte[] messageBytes = new byte[length];
            in.readFully(messageBytes);

            String xmlResponse = new String(messageBytes, StandardCharsets.UTF_8);

            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(new ByteArrayInputStream(xmlResponse.getBytes(StandardCharsets.UTF_8)));
                Element root = doc.getDocumentElement();

                String responseType = root.getAttribute("name");
                System.out.println(responseType);

                if (responseType.equals("success")) {
                    NodeList nameNodes = root.getElementsByTagName("name");
                    if (nameNodes.getLength() > 0) {
                        session = nameNodes.item(0).getTextContent().trim();
                        System.out.println("Session ID: " + session);
                    }
                } else if (responseType.equals("error")) {
                    NodeList messageNodes = root.getElementsByTagName("message");
                    if (messageNodes.getLength() > 0) {
                        String errorMessage = messageNodes.item(0).getTextContent().trim();
                        System.err.println("Error: " + errorMessage);
                    }
                }
            } catch (ParserConfigurationException | SAXException | IOException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return session;
    }

    public void sendMessageToServer(ObjectOutputStream out, String messageContent, String session) throws ParserConfigurationException, IOException {
        ClientToServerMessage message = new ClientToServerMessage("message", session, messageContent);
        sendXMLDocument(out, message.toXMLDocument());
    }

    public void sendLogoutCommand(ObjectOutputStream out, String session) throws ParserConfigurationException, IOException {
        LogoutMessage logoutMessage = new LogoutMessage("logout", session);
        sendXMLDocument(out, logoutMessage.toXMLDocument());
    }

    public List<String> handleServerListResponse(ObjectInputStream in) {
        List<String> users = new ArrayList<>();
        try {

            int length = in.readInt();


            byte[] messageBytes = new byte[length];
            in.readFully(messageBytes);

            String xmlResponse = new String(messageBytes, StandardCharsets.UTF_8);

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream(xmlResponse.getBytes(StandardCharsets.UTF_8)));
            Element root = doc.getDocumentElement();

            if (root.getNodeName().equals("success")) {
                NodeList userNodes = root.getElementsByTagName("user");

                for (int i = 0; i < userNodes.getLength(); i++) {
                    Element userElement = (Element) userNodes.item(i);
                    NodeList nameNodes = userElement.getElementsByTagName("name");
                    if (nameNodes.getLength() > 0) {
                        String username = nameNodes.item(0).getTextContent().trim();
                        users.add(username + "\n");
                    }
                }
            } else if (root.getNodeName().equals("error")) {
                String errorMessage = root.getElementsByTagName("message").item(0).getTextContent().trim();
                System.err.println("Error: " + errorMessage);
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return users;
    }

    public void requestUserList(ObjectOutputStream out, String session) throws ParserConfigurationException, IOException {
        ClientToServerMessage userListMessage = new ClientToServerMessage("list", session, "list");
        sendXMLDocument(out, userListMessage.toXMLDocument());
    }

    public String handleServerMessage(String xmlResponse) throws ClassNotFoundException{
        try {
            Document doc = createDocument(xmlResponse);
            Element root = doc.getDocumentElement();
            String eventType = root.getAttribute("name");
            System.out.println("event" + eventType);
            if (eventType.equals("message")) {
                NodeList messageNodes = root.getElementsByTagName("message");
                NodeList nameNodes = root.getElementsByTagName("name");
                if (messageNodes.getLength() > 0 && nameNodes.getLength() > 0) {
                    String messageContent = messageNodes.item(0).getTextContent().trim();
                    String senderName = nameNodes.item(0).getTextContent().trim();
                    return (messageContent + "\n");
                }
            } else if (eventType.equals("userlogin")) {
                NodeList nameNodes = root.getElementsByTagName("name");
                if (nameNodes.getLength() > 0) {
                    String username = nameNodes.item(0).getTextContent().trim();
                    return (username + " connected\n");
                }
            } else if (eventType.equals("userlogout")) {
                NodeList nameNodes = root.getElementsByTagName("name");
                if (nameNodes.getLength() > 0) {
                    String username = nameNodes.item(0).getTextContent().trim();
                    return (username + " disconnected\n");
                }
            } else if (eventType.equals("history")) {
                NodeList messageNodes = root.getElementsByTagName("message");
                if (messageNodes.getLength() > 0) {
                    for (int i = 0; i < messageNodes.getLength(); i++) {
                        String message = messageNodes.item(i).getTextContent().trim();
                        return (message.replaceAll(", ", "\n").replaceAll("[\\[\\]]", "") + "\n");
                    }
                }
            } else {

                if (root.getNodeName().equals("success")) {

                    NodeList userNodes = root.getElementsByTagName("user");
                    StringBuilder users = new StringBuilder();
                    users.append("urers:\n");
                    for (int i = 0; i < userNodes.getLength(); i++) {
                        Element userElement = (Element) userNodes.item(i);
                        String username = userElement.getElementsByTagName("name").item(0).getTextContent().trim();
                        users.append(username).append("\n");
                    }
                    return users.toString();

                } else if (root.getNodeName().equals("error")) {
                    String errorMessage = root.getElementsByTagName("message").item(0).getTextContent().trim();
                    System.err.println("Error: " + errorMessage);
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getServerMessage(ObjectInputStream in) throws IOException, ClassNotFoundException {
        try {

            int length = in.readInt();


            byte[] messageBytes = new byte[length];
            in.readFully(messageBytes);

            String xmlResponse = new String(messageBytes, StandardCharsets.UTF_8);
            return handleServerMessage(xmlResponse);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Message handleClientMessage(ObjectOutputStream out, String xmlMessage) throws IOException, SAXException, ParserConfigurationException, ClassNotFoundException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        ByteArrayInputStream input = new ByteArrayInputStream(xmlMessage.getBytes(StandardCharsets.UTF_8));
        Document doc = builder.parse(input);
        Element root = doc.getDocumentElement();

        logger.info("Received XML message: " + xmlMessage);

        String messageType = root.getAttribute("name");

        System.out.println(messageType);

        if (messageType.equals("login")) {
            return handleLoginMessage(out, doc);
        } else if (messageType.equals("message")) {
            return handleMessageMessage(out, doc);
        } else if (messageType.equals("logout")) {
            return handleLogoutMessage(doc);
        } else if (messageType.equals("list")) {
            return handleListMessage(doc);
        }
        return null;
    }

    private ListUsersMessage handleListMessage(Document doc) {
        NodeList sessionNodes = doc.getElementsByTagName("session");
        if (sessionNodes.getLength() > 0) {
            String sessionID = sessionNodes.item(0).getTextContent().trim();

            ListUsersMessage listUsersMessage = new ListUsersMessage("list", sessionID);
            return listUsersMessage;
        }
        return null;
    }

    private LogoutMessage handleLogoutMessage(Document doc) {
        NodeList sessionNodes = doc.getElementsByTagName("session");
        LogoutMessage logoutMessage = new LogoutMessage("logout", sessionNodes.item(0).getTextContent().trim());
        return logoutMessage;
    }

    private ClientToServerMessage handleMessageMessage(ObjectOutputStream out, Document doc) {
        NodeList messageNodes = doc.getElementsByTagName("message");
        NodeList sessionNodes = doc.getElementsByTagName("session");

        if (messageNodes.getLength() > 0 && sessionNodes.getLength() > 0) {
            String messageContent = messageNodes.item(0).getTextContent().trim();
            String sessionID = sessionNodes.item(0).getTextContent().trim();

            ClientToServerMessage clientToServerMessage = new ClientToServerMessage("message", sessionID, messageContent);
            return clientToServerMessage;
        } else {
            sendServerErrorAnswer(out, "Empty message", null);
        }
        return null;
    }

    private void sendServerErrorAnswer(ObjectOutputStream out, String reason, String session) {
        try {
            ErrorMessage errorResponse = new ErrorMessage("error", session, reason);
            sendXMLDocument(out, errorResponse.toXMLDocument());
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void sendUserListResponse(ObjectOutputStream out, Map<String, String> sessionMap, String sessionID) throws ClassNotFoundException{
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            Element successElement = doc.createElement("success");
            Element listUsersElement = doc.createElement("listusers");

            for (Map.Entry<String, String> entry : sessionMap.entrySet()) {
                String username = entry.getValue();
                Element userElement = doc.createElement("user");
                Element nameElement = doc.createElement("name");
                nameElement.setTextContent(username);
                userElement.appendChild(nameElement);
                listUsersElement.appendChild(userElement);
            }

            successElement.appendChild(listUsersElement);
            doc.appendChild(successElement);

            sendXMLDocument(out, doc);

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    private LoginMessage handleLoginMessage(ObjectOutputStream out, Document doc) throws ParserConfigurationException {
        NodeList nameNodes = doc.getElementsByTagName("name");
        if (nameNodes.getLength() > 0 && nameNodes.item(0) != null) {
            String name = nameNodes.item(0).getTextContent().trim();

            LoginMessage loginMessage = new LoginMessage("login", null, name);

            return loginMessage;
        } else {
            sendServerErrorAnswer(out, "Wrong name", null);
        }
        return null;
    }

    public Message getClientMessage(ObjectInputStream in, ObjectOutputStream out) throws IOException, ParserConfigurationException, SAXException {
        try {

            int length = in.readInt();


            byte[] messageBytes = new byte[length];
            in.readFully(messageBytes);

            String message = new String(messageBytes, StandardCharsets.UTF_8);
            return handleClientMessage(out, message);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}