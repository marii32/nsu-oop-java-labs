package SecGame.Mob;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.Map;

public class MobView extends Pane {

    public MobModel model;
    private ImageView imageView;
    public static Map<String, Image> images;

    public MobView(MobModel model) {


        this.model = model;
        imageView = new ImageView();
        updateView();
        getChildren().add(imageView);
    }

    public static void setImages(Map<String, Image> newImages) {
        images = newImages;
    }

    public void updateView() {
        Image img = images.get(model.key);
        imageView.setImage(img);
        imageView.setFitWidth(model.getSize());
        imageView.setFitHeight(model.getSize());
        imageView.setTranslateX(model.getX());
        imageView.setTranslateY(model.getY());
    }
}