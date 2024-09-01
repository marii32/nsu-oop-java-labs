package fabric;

import fabric.detail.Accessory;
import fabric.detail.Auto;
import fabric.detail.Body;
import fabric.detail.Engine;
import fabric.storage.AutoStorage;

import java.util.Arrays;
import java.util.logging.Logger;

public class Dealer implements Runnable{

    private AutoStorage autoStorage;
    private ControllerAutoStorage controllerAutoStorage;
    private Context context;
    public static final Logger logger = Logger.getLogger(Main.class.getName());
    public Dealer(AutoStorage autoStorage,  Context context){

        this.autoStorage = autoStorage;
        this.context = context;
    }

    @Override
    public void run(){

        try {

            while (!Thread.currentThread().isInterrupted()) {


                Thread.currentThread().sleep(context.dealerInterval);
                Auto auto = (Auto)autoStorage.takeDetail();
                context.produsedCar++;
                if(context.logSale){

                    logger.info("Number " + context.produsedCar + " Auto " + auto.id + " Body " + auto.body.id + " Motor "+ auto.engine.id + " Accessory " + auto.accessory.id);
                }
                //controllerAutoStorage.waitedCar--;

            }
        }catch (InterruptedException e) {

            logger.warning(Arrays.toString(e.getStackTrace()));
        }
    }
}
