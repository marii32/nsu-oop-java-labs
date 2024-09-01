package fabric.detail;

public class Auto extends Detail{

    public Body body;
    public Accessory accessory;
    public Engine engine;
    public Auto(Body body, Accessory accessory, Engine engine){

        super();
        this.accessory = accessory;
        this.body = body;
        this.engine = engine;
    }
}
