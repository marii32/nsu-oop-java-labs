package fabric.task;

import fabric.detail.Accessory;
import fabric.detail.Auto;
import fabric.detail.Body;
import fabric.detail.Engine;
import fabric.storage.AccessoryStorage;
import fabric.storage.AutoStorage;
import fabric.storage.BodyStorage;
import fabric.storage.EngineStorage;

public class WorkerTask implements Task{

    private BodyStorage bodyStorage;
    private EngineStorage engineStorage;
    private AutoStorage autoStorage;
    private AccessoryStorage accessoryStorage;
    private int num;

    public WorkerTask(BodyStorage bodyStorage, EngineStorage engineStorage, AccessoryStorage accessoryStorage, AutoStorage autoStorage){

        this.accessoryStorage = accessoryStorage;
        this.bodyStorage = bodyStorage;
        this.engineStorage = engineStorage;
        this.autoStorage = autoStorage;

    }

    @Override
    public void execute() throws InterruptedException{

        Body body = (Body)bodyStorage.takeDetail();
        System.out.println("b");
        Engine engine = (Engine)engineStorage.takeDetail();
        System.out.println("e");
        Accessory accessory = (Accessory)accessoryStorage.takeDetail();
        System.out.println("a");

        Auto auto = new Auto(body, accessory, engine);
        autoStorage.addDetail(auto);
    }
}
