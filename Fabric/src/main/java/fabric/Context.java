package fabric;

import fabric.detail.Accessory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Context {

    public int storageBodySize=1;
    public int storageEngineSize=1;
    public int storageAccessorySize=1;
    public int storageAutoSize=100;
    public int accessorySuppliers=5;
    public int engineSuppliers=5;
    public int bodySuppliers=2;
    public int workers=2;
    public int dealers=20;
    public int engineInterval = 1000;
    public int accessoryInterval = 1000;
    public int bodyInterval = 1000;
    public int produsedCar = 0;
    public int accessoryCount = 0;
    public int dealerInterval = 0;
    public boolean logSale = false;
    public Context(){

        Properties properties = new Properties();
        try (InputStream inputStream = getClass().getResourceAsStream("/config.txt")) {

            if (inputStream != null) {

                properties.load(inputStream);
                storageBodySize = Integer.parseInt(properties.getProperty("StorageBodySize"));
                storageEngineSize = Integer.parseInt(properties.getProperty("StorageEngineSize"));
                storageAccessorySize = Integer.parseInt(properties.getProperty("StorageAccessorySize"));
                storageAutoSize = Integer.parseInt(properties.getProperty("StorageAutoSize"));
                accessorySuppliers = Integer.parseInt(properties.getProperty("AccessorySuppliers"));
                engineSuppliers = Integer.parseInt(properties.getProperty("EngineSuppliers"));
                bodySuppliers = Integer.parseInt(properties.getProperty("BodySuppliers"));
                workers = Integer.parseInt(properties.getProperty("Workers"));
                dealers = Integer.parseInt(properties.getProperty("Dealers"));
                accessoryInterval = Integer.parseInt(properties.getProperty("accessoryInterval"));
                bodyInterval = Integer.parseInt((properties.getProperty("bodyInterval")));
                engineInterval = Integer.parseInt(properties.getProperty("engineInterval"));
                dealerInterval = Integer.parseInt(properties.getProperty("dealerInterval"));
                logSale = Boolean.parseBoolean(properties.getProperty("LogSale"));
            }
            else {
                System.err.println("File not found");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
