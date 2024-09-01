package SecGame;

import SecGame.General.Controller;

import SecGame.Mob.MobController;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class Main extends Application {

    private static final String FILE_PATH = "SecGame/src/record.txt";
    private Controller controller;
    private Timeline timeline;

    @Override
    public void start(Stage primaryStage) {
        int record = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line = reader.readLine();
            if (line != null && !line.isEmpty()) {
                record = Integer.parseInt(line.trim());
            }
        } catch (IOException e) {
            System.err.println("Ошибка при чтении записи из файла: " + e.getMessage());
        }

        controller = new Controller(record);
        controller.game();

        timeline = new Timeline(new KeyFrame(Duration.millis(10), event -> {
            if (controller.model.isNewGame) {
                restartGame(primaryStage);
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void restartGame(Stage primaryStage) {
        timeline.stop();
        primaryStage.close();
        new Main().start(new Stage());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
