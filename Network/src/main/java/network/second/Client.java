package network.second;

import network.second.sender.Sender;
import network.second.sender.SenderUsual;
import network.second.sender.SenderXML;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.Socket;
import java.util.logging.Logger;

public class Client {

    private Sender sender;
    private Socket socketToServer;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private String username;
    private String sessionID;
    private static final Logger logger = Logger.getLogger(Server.class.getName());
    private ClientGUI clientGUI;

    public Client(byte senderType) {
        this.clientGUI = new ClientGUI(message -> {
            if (message.equals("/quit")) {
                try {
                    sender.sendLogoutCommand(out, sessionID);
                } catch (ParserConfigurationException | IOException e) {
                    logger.warning(e.getMessage());
                } finally {
                    closeConnection();
                }
            } else if (message.equals("/list")) {
                try {
                    sender.requestUserList(out, sessionID);
                } catch (ParserConfigurationException | IOException e) {
                    logger.warning(e.getMessage());
                }
            } else {
                try {
                    sender.sendMessageToServer(out, message, sessionID);
                } catch (ParserConfigurationException | IOException e) {
                    logger.warning(e.getMessage());
                }
            }
        });

        this.username = clientGUI.getUsername();

        try {
            tryToConnect("localhost", 123, senderType);
        } catch (IOException | ParserConfigurationException | ClassNotFoundException e) {
            logger.warning(e.getMessage());
        }

        new Thread(() -> {
            while (!socketToServer.isClosed()) {
                try {
                    String serverMessage = sender.getServerMessage(in);
                    if (serverMessage != null) {
                        clientGUI.addMessage(serverMessage);
                    }
                } catch (ClassNotFoundException | IOException e) {
                    logger.warning(e.getMessage());
                    closeConnection();
                }
            }
        }).start();
    }

    private void tryToConnect(String host, int port, byte senderType) throws IOException, ParserConfigurationException, ClassNotFoundException {
        socketToServer = new Socket(host, port);

        DataOutputStream dataOut = new DataOutputStream(socketToServer.getOutputStream());
        dataOut.writeByte(senderType);
        dataOut.flush();

        this.out = new ObjectOutputStream(socketToServer.getOutputStream());
        this.out.flush();

        this.in = new ObjectInputStream(socketToServer.getInputStream());

        if (senderType == 1) {
            this.sender = new SenderXML();
        } else {
            this.sender = new SenderUsual();
        }

        sessionID = sender.tryToConnect(out, in, username);
    }

    private void closeConnection() {
        try {
            if (socketToServer != null && !socketToServer.isClosed()) {
                socketToServer.close();
            }
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
            Thread.currentThread().interrupt();
        } catch (IOException e) {
            logger.warning(e.getMessage());
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new Client((byte) 1);
        Thread.sleep(2000);
        new Client((byte) 0);
    }
}

