package SecGame.General;



import SecGame.General.Model;
import SecGame.Hero.HeroController;
import SecGame.Mob.MobController;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class Controller {

    public Model model;

    public View2 view;
    HeroController heroController;
    public ArrayList<MobController> mobs;
    Timer timer;
    public Timeline timeline;

    public Controller(int record){

        this.mobs = new ArrayList<>();
        model = new Model(record);
        this.view = new View2(model, this);
        heroController = new HeroController(model.getWidth(), model.getHeight(), model.heroModel);
    }

    boolean check_distance(MobController mob){

        int mobRadius = mob.model.getSize() / 2;
        int heroRadius = model.heroModel.getSize() / 2;
        int mobCenterX = mob.model.getX() + mobRadius;
        int mobCenterY = mob.model.getY() + mobRadius;
        int heroCenterX = model.heroModel.getX() + heroRadius;
        int heroCenterY = model.heroModel.getY() + heroRadius;
        double distance = Math.sqrt(Math.pow(mobCenterX - heroCenterX, 2) + Math.pow(mobCenterY - heroCenterY, 2));
        return (distance < (mobRadius + heroRadius));
    }

    void update_record(){

        if (model.current_record > model.record) {
            try (OutputStream outputStream = new FileOutputStream("SecGame/src/record.txt")) {
                String newRecordString = String.valueOf(model.current_record);
                byte[] newRecordBytes = newRecordString.getBytes();
                outputStream.write(newRecordBytes);
            } catch (IOException e) {
                System.err.println("Ошибка при записи нового рекорда: " + e.getMessage());
            }
        }
    }

    public void game() {


        while (model.kolMobs != 10) {

            MobController mob = new MobController(model.getWidth(), model.getHeight(), model.heroModel.getSize());
            mobs.add(mob);
            model.addMob(mob.model, model.kolMobs);
            view.addMob(mob.model, model.kolMobs);
            model.kolMobs++;
        }

        view.run();



        timeline = new Timeline(new KeyFrame(Duration.millis(100), event -> {

            view.reprint();
            for (int i = 0; i < mobs.size(); i++) {
                MobController mob = mobs.get(i);
                if (check_distance(mob)) {
                    if (mob.model.getSize() > model.heroModel.getSize()) {

                        view.game = false;
                        break;
                    }
                    mob.model.updatePosition(false);
                    model.heroModel.size += mob.model.getSize() / 20;
                    model.current_record = model.heroModel.size - model.size_begin;
                    update_record();
                }
                if (mob.model.getX() > model.getWidth() || mob.model.getX() < -mob.model.getSize()) {

                    view.delete_mob(i);
                    model.deleteMob(i);
                    mob = new MobController(model.getWidth(),model.getHeight(), model.heroModel.size);
                    model.addMob(mob.model,i);
                    view.addMob(mob.model,i);
                    mobs.set(i, mob);
                }
            }

        }));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

    }
}
