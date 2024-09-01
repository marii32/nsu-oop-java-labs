package lab3.operat;
import lab3.App;
import lab3.Context;
import java.util.logging.Logger;


@Operation("PUSH")
public class Push implements OperationInterface {

    private static final Logger logger = Logger.getLogger(App.class.getName());
    public boolean isLetter(String sym){

        char[] a = sym.toCharArray();
        return (Character.isLetter(a[0]));
    }
    @Override
    public void calculate(Context context, String[] args) {

        logger.info("Push");
        if (isLetter(args[1])){
            context.stack.push(context.variables.get(args[1]));
        }
        else{
            context.stack.push(Double.parseDouble(args[1]));
        }

    }


}