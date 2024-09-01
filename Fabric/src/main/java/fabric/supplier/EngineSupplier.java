package fabric.supplier;

import fabric.Context;
import fabric.detail.Engine;
import fabric.storage.EngineStorage;

import java.util.Arrays;

public class EngineSupplier extends Supplier{

    public EngineSupplier(EngineStorage storage, Context context){

        super(storage, context);

    }

    @Override
    public Engine createDetail(){


        return new Engine();
    }
    @Override
    public void run() {

        try {
            while (!Thread.currentThread().isInterrupted()) {

                Thread.currentThread().sleep(context.engineInterval);
                //System.out.println(interval);
                storage.addDetail(createDetail());
                context.accessoryCount++;
            }
        } catch (InterruptedException e) {
            logger.warning(Arrays.toString(e.getStackTrace()));
        }
    }
}
