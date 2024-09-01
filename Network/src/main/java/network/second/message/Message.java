package network.second.message;

import java.io.Serializable;

public class Message implements Serializable {
    public String commandName;
    public String session;

    public Message(String commandName, String session) {
        this.commandName = commandName;
        this.session = session;
    }

}
