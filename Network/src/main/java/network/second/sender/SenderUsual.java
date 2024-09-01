package network.second.sender;

import network.second.Server;
import network.second.message.*;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class SenderUsual implements Sender{

    Logger logger = Logger.getLogger(Server.class.getName());

    public SenderUsual(){}

    public String tryToConnect(ObjectOutputStream outputStream, ObjectInputStream inputStream, String username) throws IOException, ClassNotFoundException {

        LoginMessage loginMessage = new LoginMessage("login", null, username);
        outputStream.writeObject(loginMessage);
        outputStream.flush();

        Message sessionMessage = (Message) inputStream.readObject();
        return sessionMessage.session;
    }

    public String getServerMessage(ObjectInputStream in) throws IOException, ClassNotFoundException {

        ServerToClientMessage message = (ServerToClientMessage) in.readObject();
        if(message.commandName.equals("message")){
            return (message.message+"\n");
        }else if(message.commandName.equals("userlogin")){
            return message.name + "connected\n";
        }else if(message.commandName.equals("userlogout")){
            return message.name+"disconnected\n";
        }else if(message.commandName.equals("history")){
            return message.message.replaceAll(", ", "\n").replaceAll("[\\[\\]]", "") + "\n";
        }else if(message.commandName.equals("list")){
            return message.message;
        }
        return null;
    }

    public void sendLogoutCommand(ObjectOutputStream out, String session) throws IOException, ParserConfigurationException {

        LogoutMessage logoutMessage = new LogoutMessage("logout", session);
        out.writeObject(logoutMessage);
        out.flush();
    }

    public void requestUserList(ObjectOutputStream out, String session) throws IOException, ParserConfigurationException {

        ClientToServerMessage userListMessage = new ClientToServerMessage("list", session, "list");
        out.writeObject(userListMessage);
        out.flush();
    }

    public void sendMessageToServer(ObjectOutputStream out, String messageContent, String session) throws IOException, ParserConfigurationException {

        ClientToServerMessage message = new ClientToServerMessage("message", session, messageContent);
        out.writeObject(message);
        out.flush();
    }

    public Message getClientMessage(ObjectInputStream in, ObjectOutputStream out) throws IOException, ParserConfigurationException, SAXException, ClassNotFoundException {

        return (Message) in.readObject();
    }

    public void sendMessage(ObjectOutputStream out, ServerToClientMessage message) throws IOException, ParserConfigurationException {

        out.writeObject(message);
        out.flush();
    }

    public void sendChatHistory(ObjectOutputStream out, String sessionID, LinkedList<String> chatHistory) throws IOException, ParserConfigurationException {
        ServerToClientMessage chatHistoryResponse = new ServerToClientMessage("history", sessionID, "history", chatHistory.toString(), "Server");
        sendMessage(out, chatHistoryResponse);
    }

    public void sendUserListResponse(ObjectOutputStream out, Map<String, String> sessionMap, String sessionID)throws IOException, ParserConfigurationException, ClassNotFoundException{


        List<String> users = new ArrayList<>();
        for (Map.Entry<String, String> entry : sessionMap.entrySet()) {

            users.add(entry.getValue());

        }

        ServerToClientMessage listUsersMessage = new ServerToClientMessage("list", sessionID, "list", "users\n" + users.toString().replaceAll(", ", "\n").replaceAll("[\\[\\]]", "") + "\n", "Server");
        out.writeObject(listUsersMessage);
        out.flush();

    }

}