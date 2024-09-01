package SecGame.Mob;

import SecGame.Mob.MobModel;

import javax.swing.*;
import java.awt.*;
import java.util.Map;


public class MobView2 extends JPanel{

    public MobModel model;

    ImageIcon image;
    Image img;
    ClassLoader classLoader = getClass().getClassLoader();
    public static Map<String, ImageIcon> images;


    public MobView2(MobModel model){

        setOpaque(false);
        this.model = model;

        this.image = images.get(model.key);
        this.img = this.image.getImage();
    }
    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);
        g.drawImage(img, model.getX(),model.getY(),model.getSize(), model.getSize(),null);
    }

}