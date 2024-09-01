package fabric.detail;

import java.util.UUID;

public abstract class Detail {

    public UUID id;
    public Detail(){

        this.id = UUID.randomUUID();
    }
}
