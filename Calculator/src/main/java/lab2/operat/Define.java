package lab3.operat;

import lab3.App;
import lab3.Context;
import java.util.logging.Logger;

@Operation("DEFINE")
public class Define implements OperationInterface {

    private static final Logger logger = Logger.getLogger(App.class.getName());
    @Override
    public void calculate(Context context, String[] args) {

        logger.info("Define");
        context.variables.put(args[1], Double.parseDouble(args[2]));
    }


}