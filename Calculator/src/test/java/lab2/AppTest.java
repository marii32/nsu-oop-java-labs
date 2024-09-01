package lab2;

//import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import lab2.operat.Push;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

public class AppTest 
    extends TestCase
{
    @Test
    public void testCalculate() {
        String input = "PUSH 5\n" +
                "PUSH 3\n" +
                "+\n" +
                "PRINT\n" +
                "stop";

        InputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(in);
        OperationFactory factory = new OperationFactory();
        Calculator calculator = new Calculator(scanner, factory);
        calculator.calculate();

        assertEquals(8.0, calculator.context.stack.peek());
        try{
            in.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testPushOperation() {
        Push push = new Push();
        Context context = new Context();
        context.args = new String[]{"PUSH", "5"};
        push.calculate(context);

        assertEquals(1, context.stack.size());
        assertEquals(5.0,context.stack.peek());
    }

    @Test
    public void testCalculateWithAllOperations() {
        String input = "DEFINE x 5\n" +
                "DEFINE y 3\n" +
                "PUSH x\n" +
                "PUSH y\n" +
                "+\n" +
                "PRINT\n" +
                "PUSH x\n" +
                "PUSH y\n" +
                "-\n" +
                "PRINT\n" +
                "PUSH x\n" +
                "PUSH y\n" +
                "*\n" +
                "PRINT\n" +
                "PUSH x\n" +
                "PUSH y\n" +
                "/\n" +
                "PRINT\n" +
                "PUSH x\n" +
                "SQRT\n" +
                "PRINT\n" +
                "stop";

        InputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(in);
        OperationFactory factory = new OperationFactory();
        Calculator calculator = new Calculator(scanner, factory);
        calculator.calculate();

        assertEquals(2.23606797749979, calculator.context.stack.pop());
        assertEquals(0.6, calculator.context.stack.pop());
        assertEquals(15.0, calculator.context.stack.pop());
        assertEquals(-2.0, calculator.context.stack.pop());
        assertEquals(8.0, calculator.context.stack.pop());

        try{
            in.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
