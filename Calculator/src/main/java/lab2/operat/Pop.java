package lab3.operat;

import lab3.App;
import lab3.Context;
import java.util.logging.Logger;

@Operation("POP")
public class Pop implements OperationInterface {

    private static final Logger logger = Logger.getLogger(App.class.getName());
    @Override
    public void calculate(Context context, String[] args) {

        logger.info("Pop");
        try {

            context.stack.pop();
        }
        catch (Exception e) {
            System.out.println("Нет аргументов в стеке");
        }
    }


}
