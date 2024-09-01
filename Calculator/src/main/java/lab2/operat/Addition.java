package lab2.operat;

import lab2.App;
import lab2.Context;
import java.util.logging.Logger;

@Operation("+")
public class Addition implements OperationInterface {

    private static final Logger logger = Logger.getLogger(App.class.getName());
    @Override
    public void calculate(Context context, String[] args) {

        logger.info("Add");
        try {
            Double num1 = context.stack.pop();
            Double num2 = context.stack.pop();
            context.stack.push(num1 + num2);
        }
        catch (Exception e) {
            System.out.println("Мало аргументов в стеке");
        }
    }


}