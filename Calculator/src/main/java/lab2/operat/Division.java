package lab3.operat;
import lab3.App;
import lab3.Context;
import java.util.logging.Logger;

@Operation("/")
public class Division implements OperationInterface {

    private static final Logger logger = Logger.getLogger(App.class.getName());
    @Override
    public void calculate(Context context, String[] args) {

        logger.info("Div");
        try {
            Double num1 = context.stack.pop();
            Double num2 = context.stack.pop();
            context.stack.push(num1 / num2);
        }
        catch (Exception e) {
            System.out.println("Мало аргументов в стеке");
        }
    }


}