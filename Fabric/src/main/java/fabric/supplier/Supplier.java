package fabric.supplier;

import fabric.Context;
import fabric.Main;
import fabric.detail.Detail;
import fabric.storage.Storage;

import java.util.Arrays;
import java.util.logging.Logger;

public abstract class Supplier implements Runnable{

    Context context;
    public Storage storage;
    public static final Logger logger = Logger.getLogger(Main.class.getName());
    public Supplier(Storage storage, Context context) {

        this.storage = storage;
        this.context = context;
    }

    public Detail createDetail(){
        return new Detail() {};
    }
    @Override
    public void run() {

        try {
            while (!Thread.currentThread().isInterrupted()) {


                storage.addDetail(createDetail());
            }
        } catch (InterruptedException e) {

            logger.warning(Arrays.toString(e.getStackTrace()));
        }
    }

}
