package fabric.supplier;

import fabric.Context;
import fabric.detail.Accessory;
import fabric.storage.AccessoryStorage;

import java.util.Arrays;

public class AccessorySupplier extends Supplier{

    public AccessorySupplier(AccessoryStorage storage, Context context){

        super(storage, context);
    }

    @Override
    public Accessory createDetail() {

        return new Accessory();
    }

    @Override
    public void run() {

        try {
            while (!Thread.currentThread().isInterrupted()) {

                Thread.currentThread().sleep(context.accessoryInterval);
                //System.out.println(interval);
                storage.addDetail(createDetail());
                context.accessoryCount++;
            }
        } catch (InterruptedException e) {
            logger.warning(Arrays.toString(e.getStackTrace()));
        }
    }
}
