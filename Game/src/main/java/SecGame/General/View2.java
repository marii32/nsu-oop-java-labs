

package SecGame.General;



import SecGame.Hero.HeroView;
import SecGame.Hero.HeroView2;
import SecGame.Mob.MobController;
import SecGame.Mob.MobModel;
import SecGame.Mob.MobView;
import SecGame.Mob.MobView2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class View2 extends JFrame {

    Controller controller;
    Model model;
    private ImageIcon backgroundIcon;
    private JLayeredPane layeredPane = new JLayeredPane();
    public boolean game = true;
    private JFrame frame;
    private JLabel label;
    private JLabel currentScoreLabel;
    private JLabel replay;
    ClassLoader classLoader = getClass().getClassLoader();

    private HeroView2 heroView;
    private ArrayList<MobView2> mobs;

    private Map<Integer, String> keys = new HashMap<>();{
        keys.put(KeyEvent.VK_UP, "up");
        keys.put(KeyEvent.VK_DOWN, "down");
        keys.put(KeyEvent.VK_LEFT, "left");
        keys.put(KeyEvent.VK_RIGHT, "right");
    }

    View2(Model model, Controller controller){

        this.model = model;
        this.controller = controller;
        this.backgroundIcon = new ImageIcon(classLoader.getResource("fon.jpg"));
        setPictures();
        this.heroView = new HeroView2(model.heroModel);
        this.mobs = new ArrayList<>();

        create_window();
    }

    void setPictures() {
        Map<String, ImageIcon> imagesMap = new HashMap<>();

        for (int i = 0; i < 6; i++) {
            ImageIcon rightImage = new ImageIcon(classLoader.getResource("right" + (i + 1) + ".png"));
            ImageIcon leftImage = new ImageIcon(classLoader.getResource("left" + (i + 1) + ".png"));

            imagesMap.put("right" + (i + 1), rightImage);
            imagesMap.put("left" + (i + 1), leftImage);
        }

        MobView2.images = imagesMap;
    }

    public void addMob(MobModel mobModel, int num){

        MobView2 mob = new MobView2(mobModel);
        mobs.add(num, mob);
        mob.setBounds(0,0, model.getWidth(), model.getHeight());
        layeredPane.add(mob, JLayeredPane.POPUP_LAYER);
    }

    void create_window(){

        frame = new JFrame("Karasiki");
        frame.setContentPane(layeredPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        frame.setPreferredSize(new Dimension(controller.model.getWidth(), controller.model.getHeight()));
        frame.setLocation(200, 100);
        frame.setResizable(false);
    }

    void create_label(){

        label = new JLabel(backgroundIcon);
        label.setBounds(0,0,controller.model.getWidth(),controller.model.getHeight());

        currentScoreLabel = new JLabel("Текущий результат: " + controller.model.heroModel.getSize());
        currentScoreLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));
        currentScoreLabel.setBounds(10, 10, 400, 30);
        layeredPane.add(currentScoreLabel, JLayeredPane.PALETTE_LAYER);

        JLabel highScoreLabel = new JLabel("Рекорд: " + controller.model.record);
        highScoreLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));
        highScoreLabel.setBounds(10, 50, 200, 30);
        layeredPane.add(highScoreLabel, JLayeredPane.PALETTE_LAYER);
    }

    void create_replay_button(){

        ImageIcon replayIcon = new ImageIcon(classLoader.getResource("replay.png"));
        replay = new JLabel(replayIcon);
        replay.setBounds(model.getWidth() - 100, model.getHeight()-100,50,50);
        layeredPane.add(replay, JLayeredPane.POPUP_LAYER);

        frame.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent m){

                frame.dispose();
                model.isNewGame = true;

            }
        });
    }

    void game_end(){

        JPanel rectanglePanel = new JPanel();
        rectanglePanel.setLayout(null);
        rectanglePanel.setBackground(new Color(80, 68, 155, 179));
        rectanglePanel.setBounds((model.getWidth() - 500) / 2, (model.getHeight() - 200) / 2, 440, 100);

        JLabel frase = new JLabel("Рыба тебя скушала((");
        frase.setFont(new Font("SansSerif", Font.BOLD, 40));
        frase.setForeground(new Color(0, 255, 110));

        frase.setBounds(10, 0, 450, 100);

        rectanglePanel.add(frase);

        layeredPane.add(rectanglePanel, JLayeredPane.PALETTE_LAYER);

        layeredPane.repaint();
    }

    void delete_mob(int num){

        MobView2 mobToRemove = mobs.remove(num);

        layeredPane.remove(mobToRemove);
        layeredPane.revalidate();
        layeredPane.repaint();

    }

    void parse_key(KeyEvent e){

        controller.heroController.handleKeyPress(keys.get(e.getKeyCode()));
    }

    void run(){


        create_label();

        heroView.setBounds(0,0,model.getWidth(),model.getWidth());
        layeredPane.add(heroView, JLayeredPane.PALETTE_LAYER);

        for(MobView2 mob : mobs){
            mob.setBounds(0,0, controller.model.getWidth(), controller.model.getHeight());


        }

        layeredPane.add(label, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(heroView, JLayeredPane.DRAG_LAYER);

        create_replay_button();
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                //controller.heroController.handleKeyPress(e);
                parse_key(e);
            }
        });

        frame.pack();
        frame.setVisible(true);
    }

    void reprint(){

        if(!game){

            controller.timeline.stop();
            game_end();
            return;
        }

        for(MobView2 mob:mobs){

            mob.model.updatePosition(true);
            mob.repaint();
        }

        currentScoreLabel.setText("Текущий результат: " + controller.model.current_record);
        heroView.repaint();
        replay.repaint();
    }
}
