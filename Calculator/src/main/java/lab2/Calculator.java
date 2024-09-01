package lab2;

import java.util.Scanner;
import java.util.logging.Logger;

import lab2.operat.*;

public class Calculator {

    Context context;
    OperationFactory operationFactory;

    public String[] args;

    private static final Logger logger = Logger.getLogger(App.class.getName());
    public Calculator( OperationFactory factory){

        this.operationFactory = factory;
        this.context = new Context();
    }

    public void calculate(String newComand){


            args = newComand.split(" ");
            try {

                OperationInterface oper = operationFactory.getOperation(args[0]);

                oper.calculate(context, args);

            } catch (Exception e) {
                logger.warning(e.getMessage());
            }



    }

    public void calculateWithFile(String newComand){



            args = newComand.split(" ");
            try {

                OperationInterface oper = operationFactory.getOperation(args[0]);
                oper.calculate(context, args);

            } catch (Exception e) {
                logger.warning(e.getMessage());
            }


        }

}
