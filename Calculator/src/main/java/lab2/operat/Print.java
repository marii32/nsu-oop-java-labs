package lab3.operat;
import lab3.App;
import lab3.Context;
import java.util.logging.Logger;

@Operation("PRINT")
public class Print implements OperationInterface {

    private static final Logger logger = Logger.getLogger(App.class.getName());
    @Override
    public void calculate(Context context, String[] args) {

        logger.info("Print");
        System.out.print(context.stack.peek()+"\n");
    }


}