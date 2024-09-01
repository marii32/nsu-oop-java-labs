package fabric;

import fabric.task.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ThreadPool {
    private List<Thread> workerList;
    public BlockingQueue<Task> tasks;

    public ThreadPool(){

        workerList = new ArrayList<>();
        tasks = new LinkedBlockingQueue<>(100);
    }

    public int getSize(){
        return tasks.size();
    }


    public void addWorker(Thread worker){


        workerList.add(worker);

    }

    public void start(){


        for(Thread worker : workerList){


            //worker.tasks = tasks;
            worker.start();
        }
    }

    public void stop(){

        for (Thread worker : workerList){

            worker.interrupt();
        }
    }
}
