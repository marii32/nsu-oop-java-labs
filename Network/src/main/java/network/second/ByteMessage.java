package network.second;

import java.io.Serializable;

public class ByteMessage implements Serializable {
    private byte[] data;
    private int length;

    public ByteMessage(byte[] data, int length) {
        this.data = data;
        this.length = length;
    }

    public byte[] getData() {
        return data;
    }

}