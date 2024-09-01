package network.second;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.function.Consumer;

public class ClientGUI extends JFrame {

    private JTextArea chatArea;
    private JTextField messageField;
    private String username;
    private Consumer<String> messageConsumer;

    public ClientGUI(Consumer<String> messageConsumer) {
        this.messageConsumer = messageConsumer;

        this.username = JOptionPane.showInputDialog("Введите ваше имя:");

        setTitle("Chat Client");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);
        add(scrollPane, BorderLayout.CENTER);

        messageField = new JTextField();
        messageField.addActionListener(e -> {
            String message = messageField.getText().trim();
            if (!message.isEmpty()) {
                messageConsumer.accept(message);
                if (message.equals("/quit")){
                    dispose();
                }
                messageField.setText("");
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                messageConsumer.accept("/quit");
                dispose();
                super.windowClosing(e);
            }
        });

        add(messageField, BorderLayout.SOUTH);
        setVisible(true);
    }

    public void addMessage(String message) {
        chatArea.append(message);
    }

    public String getUsername() {
        return username;
    }
}