package fabric;

import fabric.storage.AccessoryStorage;
import fabric.storage.AutoStorage;
import fabric.storage.BodyStorage;
import fabric.storage.EngineStorage;
import fabric.supplier.AccessorySupplier;
import fabric.supplier.BodySupplier;
import fabric.supplier.EngineSupplier;
import fabric.supplier.Supplier;

import java.util.ArrayList;
import java.util.List;

public class Controller {


    private List<Thread> accessorySuppliersList;
    public List<Thread> bodySuppliersList;
    private List<Thread> engineSuppliersList;
    private List<Thread> workerList;
    private List<Thread> dealerList;

    private Context context;
    public BodyStorage bodyStorage;
    public EngineStorage engineStorage;
    public AccessoryStorage accessoryStorage;
    public AutoStorage autoStorage;

    private ThreadPool threadPool;
    private ControllerAutoStorage controllerAutoStorage;

    public Controller(Context context){

        this.context = context;

        this.threadPool = new ThreadPool();

        accessorySuppliersList = new ArrayList<>(context.accessorySuppliers);
        bodySuppliersList = new ArrayList<>(context.bodySuppliers);
        engineSuppliersList = new ArrayList<>(context.engineSuppliers);
        workerList = new ArrayList<>(context.workers);
        dealerList = new ArrayList<>(context.dealers);

        bodyStorage = new BodyStorage(context.storageBodySize);
        engineStorage = new EngineStorage(context.storageEngineSize);
        accessoryStorage = new AccessoryStorage(context.storageAccessorySize);
        autoStorage = new AutoStorage(context.storageAutoSize);

        controllerAutoStorage = new ControllerAutoStorage(accessoryStorage,engineStorage,bodyStorage,autoStorage, threadPool);

        for (int i = 0; i<context.accessorySuppliers;i++){

            accessorySuppliersList.add( new Thread(new AccessorySupplier(accessoryStorage, context)));
        }

        for (int i = 0; i<context.bodySuppliers; i++){

            bodySuppliersList.add(new Thread(new BodySupplier(bodyStorage, context)));
        }

        for (int i = 0; i<context.engineSuppliers;i++){

            engineSuppliersList.add( new Thread(new EngineSupplier(engineStorage, context)));
        }

        for (int i = 0; i<context.dealers; i++){

            dealerList.add(new Thread(new Dealer(autoStorage, context)));
        }

        for (int i=0;i<context.workers;i++){

            threadPool.addWorker(new Thread(new Worker(threadPool,accessoryStorage,bodyStorage,engineStorage,autoStorage)));
        }
    }

    public void run(){

        for (int i = 0; i<context.accessorySuppliers;i++){

            accessorySuppliersList.get(i).start();
        }

        for (int i = 0; i<context.bodySuppliers; i++){

            bodySuppliersList.get(i).start();
        }

        for (int i = 0; i<context.engineSuppliers;i++){

            engineSuppliersList.get(i).start();

        }

        for (int i=0;i<context.dealers;i++){

            dealerList.get(i).start();
        }
        threadPool.start();
        controllerAutoStorage.start();

    }

    public void shutdown() {
        for (Thread accessorySupplier : accessorySuppliersList) {
            accessorySupplier.interrupt();
        }
        for (Thread bodySupplier : bodySuppliersList) {
            bodySupplier.interrupt();
        }
        for (Thread engineSupplier : engineSuppliersList) {
            engineSupplier.interrupt();
        }
        for (Thread dealer : dealerList) {
            dealer.interrupt();
        }
        threadPool.stop();
        controllerAutoStorage.interrupt();
    }
}
