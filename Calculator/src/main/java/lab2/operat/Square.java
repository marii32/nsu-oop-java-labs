package lab3.operat;
import lab3.App;
import lab3.Context;
import java.util.logging.Logger;

@Operation("SQRT")
public class Square implements OperationInterface {

    private static final Logger logger = Logger.getLogger(App.class.getName());
    @Override
    public void calculate(Context context, String[] args) {

        logger.info("Square");
        try {
            Double num = context.stack.pop();
            context.stack.push(Math.pow(num, 0.5));
        }
        catch (Exception e) {
            System.out.println("Нет аргументов в стеке");
        }
    }


}