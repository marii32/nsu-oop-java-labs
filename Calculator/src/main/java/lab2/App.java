package lab2;

import java.io.*;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.LogManager;
import java.util.logging.SimpleFormatter;



public class App
{


    public static final Logger logger = Logger.getLogger(Calculator.class.getName());

    public static void main( String[] args )
    {
        try(InputStream file =  new FileInputStream("Calculator\\src\\main\\java\\lab2\\logg\\logging.properties")) { //try with resourse
            LogManager.getLogManager().readConfiguration(file);
        } catch (IOException e) {

            System.out.println("Ошибка при загрузке конфигурации логирования: " + e);
        }

        logger.info("Это сообщение будет записано в файл.");


        OperationFactory operationFactory = new OperationFactory();

        if (args.length == 0){

            Scanner scanner = new Scanner(System.in);
            Calculator calculator = new Calculator(operationFactory);
            String newComand = scanner.nextLine();
            while (!newComand.equals("stop")) {

                calculator.calculate(newComand);
                newComand = scanner.nextLine();
            }
        }
        else{

            try(Scanner scanner = new Scanner(new File(args[0]))){

                logger.info("Open file" + args[0]);
                Calculator calculator = new Calculator(operationFactory);
                String newComand = scanner.nextLine();
                while (newComand!=null) {
                    calculator.calculate(newComand);
                }

            }
            catch (FileNotFoundException e){
                logger.warning(e.getMessage());

            }

        }
    }
}
