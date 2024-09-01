package fabric;

import fabric.storage.BodyStorage;
import fabric.supplier.BodySupplier;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Main {

    public static final Logger logger = Logger.getLogger(Main.class.getName());
    public static void main(String[] args) {

        try(InputStream file =  new FileInputStream("Fabric/src/main/java/fabric/log/log.properties")) {
            LogManager.getLogManager().readConfiguration(file);
        } catch (IOException e) {

            System.out.println("Ошибка при загрузке конфигурации логирования: " + e);
        }


        Context context = new Context();

        if(context.logSale){

            logger.info("Это сообщение будет записано в файл.");
        }

        Controller controller = new Controller(context);
        controller.run();
        FactoryGUI factoryGUI = new FactoryGUI(context, controller);

    }
}