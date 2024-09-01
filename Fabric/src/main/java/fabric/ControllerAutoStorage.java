package fabric;

import fabric.storage.AccessoryStorage;
import fabric.storage.AutoStorage;
import fabric.storage.BodyStorage;
import fabric.storage.EngineStorage;
import fabric.task.Task;
import fabric.task.WorkerTask;

import java.util.Arrays;
import java.util.logging.Logger;

public class ControllerAutoStorage extends Thread{

    private AccessoryStorage accessoryStorage;
    private BodyStorage bodyStorage;
    private EngineStorage engineStorage;
    private AutoStorage autoStorage;
    private ThreadPool threadPool;
    public int waitedCar = 0;
    public static final Logger logger = Logger.getLogger(Main.class.getName());
    public ControllerAutoStorage(AccessoryStorage accessoryStorage, EngineStorage engineStorage, BodyStorage bodyStorage, AutoStorage autoStorage, ThreadPool threadPool){

        this.autoStorage = autoStorage;
        this.threadPool = threadPool;
        this.accessoryStorage = accessoryStorage;
        this.autoStorage = autoStorage;
        this.bodyStorage = bodyStorage;
        this.engineStorage = engineStorage;
    }



    @Override
    public synchronized void run(){

        try {


            while (!Thread.currentThread().isInterrupted()) {


                //sleep(100);
                while (autoStorage.isFull()){

                    wait(10);
                }
                threadPool.tasks.offer(new WorkerTask(bodyStorage,engineStorage,accessoryStorage,autoStorage));


            }
        }catch (InterruptedException e) {
            logger.warning(Arrays.toString(e.getStackTrace()));
        }
    }
}
