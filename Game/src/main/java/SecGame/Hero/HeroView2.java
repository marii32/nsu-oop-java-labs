package SecGame.Hero;

import SecGame.Hero.HeroModel;

import javax.swing.*;
import java.awt.*;

public class HeroView2 extends JComponent {

    private HeroModel model;
    ClassLoader classLoader = getClass().getClassLoader();
    private ImageIcon imageLeft = new ImageIcon(classLoader.getResource("heroLeft.png"));
    private ImageIcon imageRight = new ImageIcon(classLoader.getResource("heroRight.png"));

    private ImageIcon CurrentImg;

    public HeroView2(HeroModel model) {
        this.model = model;
        setVisible(true);
    }
    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);
        if(model.side == "left"){

            CurrentImg = imageLeft;
        }
        else CurrentImg = imageRight;
        g.drawImage(CurrentImg.getImage(), model.getX(),model.getY(),model.getSize(), model.getSize(),null);
    }

}

