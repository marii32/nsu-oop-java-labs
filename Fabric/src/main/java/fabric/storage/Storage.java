package fabric.storage;

import fabric.detail.Body;
import fabric.detail.Detail;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;


public abstract class Storage {

    private int capacity = 0;
    public List<Detail> store;
    public Storage(){}
    public Storage(int capacity){

        this.capacity = capacity;

        this.store = new LinkedList<>();
    }
    public boolean isFull(){


        return (store.size() == capacity);
    }

    public synchronized void addDetail(Detail detail) throws InterruptedException{

        while (isFull()){
            wait();
        }
        store.add(detail);
        notify();
    }

    public synchronized Detail takeDetail() throws InterruptedException{

        while (isEmpty()){
            wait();
        }
        Detail  detail = store.remove(store.size() - 1);
        notify();
        return detail;
    }

    public boolean isEmpty(){


        return store.isEmpty();
    }


}

