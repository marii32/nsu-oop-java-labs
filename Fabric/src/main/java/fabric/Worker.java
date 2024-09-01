package fabric;

import fabric.detail.Accessory;
import fabric.detail.Auto;
import fabric.detail.Body;
import fabric.detail.Engine;
import fabric.storage.AccessoryStorage;
import fabric.storage.AutoStorage;
import fabric.storage.BodyStorage;
import fabric.storage.EngineStorage;
import fabric.task.Task;
import fabric.task.WorkerTask;

import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

public class Worker implements Runnable{

    private AccessoryStorage accessoryStorage;
    private BodyStorage bodyStorage;
    private EngineStorage engineStorage;
    private AutoStorage autoStorage;
    public ThreadPool threadPool;
    public static final Logger logger = Logger.getLogger(Main.class.getName());
    public Worker(ThreadPool threadPool, AccessoryStorage accessoryStorage, BodyStorage bodyStorage, EngineStorage engineStorage,AutoStorage autoStorage){

        this.accessoryStorage = accessoryStorage;
        this.autoStorage = autoStorage;
        this.bodyStorage = bodyStorage;
        this.engineStorage = engineStorage;
        this.threadPool = threadPool;

    }

    @Override
    public void run(){

        try {

            while (!Thread.currentThread().isInterrupted()) {

                //threadPool.tasks.offer(new WorkerTask(bodyStorage,engineStorage,accessoryStorage,autoStorage));

                Task task;


                task = threadPool.tasks.take();
                task.execute();
                System.out.println(threadPool.tasks.size());
            }
        }catch (InterruptedException e) {

            logger.warning(Arrays.toString(e.getStackTrace()));
        }

    }
}
