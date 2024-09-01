package network.second.sender;

import network.second.message.Message;
import network.second.message.ServerToClientMessage;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.Map;

public interface Sender {
    void sendLogoutCommand(ObjectOutputStream out, String sessionID) throws ParserConfigurationException, IOException;

    void requestUserList(ObjectOutputStream out, String sessionID) throws ParserConfigurationException, IOException;

    void sendMessageToServer(ObjectOutputStream out, String message, String sessionID) throws ParserConfigurationException, IOException;

    String getServerMessage(ObjectInputStream in) throws ClassNotFoundException, IOException;

    String tryToConnect(ObjectOutputStream out, ObjectInputStream in, String username) throws IOException, ClassNotFoundException;

    Message getClientMessage(ObjectInputStream in, ObjectOutputStream out) throws IOException, ParserConfigurationException, SAXException, ClassNotFoundException ;

    void sendMessage(ObjectOutputStream out, ServerToClientMessage registrationResponse)throws IOException, ParserConfigurationException;

    void sendChatHistory(ObjectOutputStream out, String sessionID, LinkedList<String> chatHistory)throws IOException, ParserConfigurationException;

    void sendUserListResponse(ObjectOutputStream objectOutputStream, Map<String, String> sessionMap, String session)throws IOException, ParserConfigurationException, ClassNotFoundException;
}
