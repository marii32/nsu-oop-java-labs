package SecGame.General;

import SecGame.Hero.HeroView;
import SecGame.Mob.MobController;
import SecGame.Mob.MobModel;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import SecGame.Mob.MobView;


import javafx.scene.input.KeyEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class View extends Pane {

    public boolean game = true;
    private Model model;

    private Controller controller;
    private Stage stage;
    private StackPane root;
    private Map<String, Image> imagesMap;
    private Label currentScoreLabel;
    private Label highScoreLabel;
    private Scene scene;
    private Button replayButton;
    private HeroView heroView;
    private ArrayList<MobView> mobs;


    private Map<KeyCode, String> keys = new HashMap<>();;{

        keys.put(KeyCode.UP, "up");
        keys.put(KeyCode.DOWN, "down");
        keys.put(KeyCode.LEFT, "left");
        keys.put(KeyCode.RIGHT, "right");
    }

    public View(Model model, Controller controller){

        this.controller = controller;
        this.model = model;
        this.imagesMap = new HashMap<>();
        this.heroView = new HeroView(model.heroModel);
        this.mobs = new ArrayList<>();
        this.root = new StackPane();
        setPictures();

        create_window();
    }

    void setPictures() {
        Map<String, Image> imagesMap = new HashMap<>();

        for (int i = 0; i < 6; i++) {
            String rightImagePath = "/right" + (i + 1) + ".png";
            String leftImagePath = "/left" + (i + 1) + ".png";

            Image rightImage = new Image(getClass().getResourceAsStream(rightImagePath));
            Image leftImage = new Image(getClass().getResourceAsStream(leftImagePath));

            imagesMap.put("right" + (i + 1), rightImage);
            imagesMap.put("left" + (i + 1), leftImage);
        }


        MobView.images = imagesMap;
    }

    void create_label(){

        currentScoreLabel = new Label("Текущий результат: " + model.heroModel.getSize());
        currentScoreLabel.setFont(Font.font("SansSerif", 20));

        highScoreLabel = new Label("Рекорд: " + model.record);
        highScoreLabel.setFont(Font.font("SansSerif", 20));



        root.setAlignment(currentScoreLabel, Pos.TOP_LEFT );
        root.setAlignment(highScoreLabel, Pos.TOP_LEFT);

        currentScoreLabel.setTranslateX(0);
        currentScoreLabel.setTranslateY(0);

        highScoreLabel.setTranslateY(30);
        highScoreLabel.setTranslateX(0);



        root.getChildren().addAll(currentScoreLabel, highScoreLabel);
    }

    void create_replay_button(){

        Image replayImage = new Image(getClass().getResourceAsStream("/replay.png"));
        ImageView imageView = new ImageView(replayImage);
        imageView.setFitWidth(50);
        imageView.setFitHeight(50);

        replayButton = new Button();
        replayButton.setGraphic(imageView);

        StackPane.setAlignment(replayButton, Pos.BOTTOM_RIGHT);

        replayButton.setStyle("-fx-background-color: transparent;");

        replayButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.close();
                model.isNewGame = true;
            }
        });

        root.getChildren().add(replayButton);
        replayButton.toFront();
    }

    void create_window(){

        stage = new Stage();
        root.setStyle("-fx-background-image: url('fon.jpg'); -fx-background-size: cover;");
        scene = new Scene(root, model.getWidth(), model.getHeight());
        stage.setScene(scene);
        stage.setTitle("Karasiki");

        root.requestFocus();

    }

    void parse_key(KeyEvent event){

        controller.heroController.handleKeyPress(keys.get(event.getCode()));
    }

    public void run() {

        create_label();

        create_replay_button();

        for (MobView mob : mobs){

            mob.setPrefSize(mob.model.getSize(), mob.model.getSize());
            //root.getChildren().add(mob);
        }


        root.setAlignment(heroView, Pos.TOP_LEFT);

        heroView.setTranslateX(0);
        heroView.setTranslateY(0);

        root.getChildren().add(heroView);

        scene.setOnKeyPressed(event -> {

            parse_key(event);

        });


        stage.show();

    }

    void delete_mob(int num){

        MobView mobToRemove = mobs.remove(num);
        root.getChildren().remove(mobToRemove);
    }

    void addMob(MobModel mobModel, int num){

        MobView mob = new MobView(mobModel);
        mobs.add(num, mob);
        mob.setPrefSize(mob.model.getSize(), mob.model.getSize());
        root.getChildren().add(mob);

    }

    void game_end() {
        StackPane rectanglePanel = new StackPane();
        rectanglePanel.setStyle("-fx-background-color: rgba(80, 68, 155, 0.7);");
        rectanglePanel.setMinSize(300, 100);
        rectanglePanel.setMaxSize(500, 200);
        rectanglePanel.setAlignment(Pos.CENTER);

        Label frase = new Label("Рыба тебя скушала((");
        frase.setFont(Font.font("SansSerif", FontWeight.BOLD, 40));
        frase.setTextFill(Color.rgb(0, 255, 110));

        rectanglePanel.getChildren().add(frase);

        double x = (controller.model.getWidth() - rectanglePanel.getMaxWidth()) / 2;
        double y = (controller.model.getHeight() - rectanglePanel.getMaxHeight()) / 2;
        rectanglePanel.setLayoutX(x);
        rectanglePanel.setLayoutY(y);

        root.getChildren().add(rectanglePanel);
    }


    void reprint() {
        if (!game) {
            controller.timeline.stop();
            game_end();
            return;
        }

        root.setStyle("-fx-background-image: url('fon.jpg'); -fx-background-size: cover;");

        for (MobView mob : mobs) {
            mob.model.updatePosition(true);
            mob.updateView();
        }

        currentScoreLabel.setText("Текущий результат: " + controller.model.current_record);

        heroView.updateView();

        replayButton.toFront();

    }



}
