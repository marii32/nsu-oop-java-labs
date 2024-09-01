package lab1;

import lab1.Game;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CowsTest {

    private Game game;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUp() {
        game = new Game();
        System.setOut(new PrintStream(outContent));
    }

    /*@AfterEach
    public void tearDown() {
        System.setOut(originalOut);
    }*/

    @Test
    public void testGeneration_num() {
        game.generation_num();

        StringBuilder test = new StringBuilder("Число сгенерировано\nУгадывай)\n");
        assertEquals(test.toString(), outContent.toString());
    }
}