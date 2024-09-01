package network.second;

import network.second.message.*;
import network.second.sender.Sender;
import network.second.sender.SenderUsual;
import network.second.sender.SenderXML;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class Server {

    private static final Logger logger = Logger.getLogger(Server.class.getName());
    private static int port = 123;
    private static Map<String, String> sessionMap = new HashMap<>();
    private static Map<String, ObjectOutputStream> outputStreamMap = new HashMap<>();
    private static Map<String, ClientHandler> sessionHandlerMap = new HashMap<>();
    private static LinkedList<String> chatHistory = new LinkedList<>();
    private static int MAX_HISTORY_SIZE = 100;

    public static void main(String[] args) {
        ExecutorService executor = Executors.newCachedThreadPool();

        FileHandler fileHandler = null;
        try {
            fileHandler = new FileHandler("server.log");
        } catch (IOException ex) {
            logger.warning(ex.getMessage());
            throw new RuntimeException(ex);
        }

        logger.addHandler(fileHandler);

        try(ServerSocket serverSocket = new ServerSocket(port)) {

            System.out.println("Server started on port " + port);

            while (!Thread.currentThread().isInterrupted()) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected: " + socket);

                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                byte senderType = dataInputStream.readByte();

                ClientHandler clientHandler = new ClientHandler(socket, senderType);
                clientHandler.run();

            }
        } catch (IOException e) {
            logger.warning(e.getMessage());
        } finally {
            executor.shutdown();
        }
    }

    private static class ClientHandler  {
        private Socket socket;
        private ObjectInputStream in;
        private ObjectOutputStream out;
        private Sender sender;


        public MessageReader messageReader;
        public MessageSender messageSender;

        private List<String> messageQueue = Collections.synchronizedList(new LinkedList<>());


        private void addToHistory(String message) {
            if (chatHistory.size() > MAX_HISTORY_SIZE) {
                chatHistory.removeFirst();
            }
            chatHistory.add(message);
        }

        public ClientHandler(Socket socket, byte senderType) {
            this.socket = socket;

            try {
                this.in = new ObjectInputStream(socket.getInputStream());
                this.out = new ObjectOutputStream(socket.getOutputStream());
                this.out.flush();
                if (senderType == 1) {
                    this.sender = new SenderXML();
                } else {
                    this.sender = new SenderUsual();
                }
            } catch (IOException e) {
                logger.warning(e.getMessage());
            }
        }

        public void run() {

            this.messageReader = new MessageReader();
            this.messageSender = new MessageSender();
            messageReader.start();
            messageSender.start();
        }

        private class MessageReader extends Thread {
            @Override
            public void run() {
                try {
                    while (true) {
                        Message message = sender.getClientMessage(in, out);
                        System.out.println(message.commandName);

                        String senderName = sessionMap.get(message.session);

                        if (message.commandName.equals("login")) {


                            String sessionID = Integer.toString(socket.getPort());
                            sessionMap.put(sessionID, ((LoginMessage) message).userName);
                            outputStreamMap.put(sessionID, out);
                            sessionHandlerMap.put(sessionID, ClientHandler.this);

                            ServerToClientMessage registrationResponse = new ServerToClientMessage("command", sessionID, "success", "session", sessionID);
                            sender.sendMessage(out, registrationResponse);

                            if (!chatHistory.isEmpty()) {
                                sender.sendChatHistory(out, sessionID, chatHistory);
                            }

                            String messageContent = ((LoginMessage) message).userName + " : " +" connected";
                            messageQueue.add(messageContent);

                            addToHistory(((LoginMessage) message).userName + " connected");

                        } else if (message.commandName.equals("message")) {

                            String messageContent = senderName + " : " + ((ClientToServerMessage) message).message;
                            addToHistory(messageContent);
                            messageQueue.add(messageContent);

                        } else if (message.commandName.equals("logout")) {
                            System.out.println("close " + message.session);
                            addToHistory(sessionMap.get(message.session) + " disconnected");
                            closeConnection(message.session);
                        } else {
                            sender.sendUserListResponse(out, sessionMap, message.session);
                        }
                    }
                } catch (IOException | ParserConfigurationException | SAXException | ClassNotFoundException e) {
                    logger.warning(e.getMessage());
                }
            }
        }

        private class MessageSender extends Thread {
            @Override
            public void run() {
                try {
                    while (true) {
                        synchronized (messageQueue) {
                            if (!messageQueue.isEmpty()) {
                                String messageContent = messageQueue.remove(0);
                                for (String sessionID : sessionMap.keySet()) {
                                    sessionHandlerMap.get(sessionID).sender.sendMessage(outputStreamMap.get(sessionID), new ServerToClientMessage("message", sessionID, "message", messageContent, "Server"));
                                }
                            }
                        }
                        Thread.sleep(100);
                    }
                } catch (IOException | ParserConfigurationException | InterruptedException e) {
                    logger.warning(e.getMessage());
                }
            }
        }

        private void closeConnection(String sessionID) {
            try {
                socket.close();
                String username = sessionMap.get(sessionID);
                sessionMap.remove(sessionID);
                sendUserLogoutResponse(username);
                ObjectOutputStream remove = outputStreamMap.remove(sessionID);
                remove.close();
                messageSender.interrupt();
                messageReader.interrupt();
                sessionHandlerMap.remove(sessionID);

            } catch (IOException e) {
                logger.warning(e.getMessage());
            }
        }

        private void sendUserLogoutResponse(String username) {
            try {
                ServerToClientMessage userLogoutResponse = new ServerToClientMessage("message", null, "message", username + " : " + "disconnected", username);
                for (String id : sessionMap.keySet()) {
                    sessionHandlerMap.get(id).sender.sendMessage(outputStreamMap.get(id), userLogoutResponse);
                }
            } catch (IOException | ParserConfigurationException e) {
                logger.warning(e.getMessage());
            }
        }
    }
}