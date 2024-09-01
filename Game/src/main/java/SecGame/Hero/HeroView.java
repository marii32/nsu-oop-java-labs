package SecGame.Hero;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;


public class HeroView extends Pane {

    private HeroModel model;
    private ImageView imageView;

    public HeroView(HeroModel model) {
        this.model = model;
        imageView = new ImageView();
        imageView.setFitWidth(model.size);
        imageView.setFitHeight(model.size);
        setDirection();
        getChildren().add(imageView);
    }

    private void setDirection() {
        String side = model.side;
        Image image;
        if (side.equals("left")) {
            image = new Image(getClass().getResourceAsStream("/heroLeft.png"));
        } else {
            image = new Image(getClass().getResourceAsStream("/heroRight.png"));
        }
        imageView.setImage(image);
    }

    public void updateView() {


        setDirection();
        imageView.setFitWidth(model.getSize());
        imageView.setFitHeight(model.getSize());
//        imageView.setLayoutX(model.getX());
//        imageView.setLayoutY(model.getY());
        imageView.setTranslateX(model.getX());
        imageView.setTranslateY(model.getY());
    }

}
