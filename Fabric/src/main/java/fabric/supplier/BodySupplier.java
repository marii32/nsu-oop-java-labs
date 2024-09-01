package fabric.supplier;

import fabric.Context;
import fabric.detail.Body;
import fabric.storage.BodyStorage;

import java.util.Arrays;

public class BodySupplier extends Supplier{

    public BodySupplier(BodyStorage storage, Context context){

        super(storage,context);

    }

    @Override
    public Body createDetail(){

        return new Body();
    }

    @Override
    public void run() {

        try {
            while (!Thread.currentThread().isInterrupted()) {

                Thread.currentThread().sleep(context.bodyInterval);
                //System.out.println(interval);
                storage.addDetail(createDetail());
                context.accessoryCount++;
            }
        } catch (InterruptedException e) {
            logger.warning(Arrays.toString(e.getStackTrace()));
        }
    }
}
