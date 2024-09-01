package SecGame.General;

import SecGame.Hero.HeroModel;
import SecGame.Mob.MobController;
import SecGame.Mob.MobModel;

import java.util.ArrayList;

public class Model {

    public boolean isNewGame = false;
    public int kolMobs = 0;
    private int width;
    private int height;
    public int record = 0;
    public int current_record = 0;
    public int size_begin = 50;
    public ClassLoader classLoader = getClass().getClassLoader();
    public ArrayList<MobModel> mobs;

    public HeroModel heroModel;

    public Model(int record){

        this.record = record;
        this.height = 700;
        this.width = 1000;
        this.heroModel = new HeroModel(width, height);
        this.mobs = new ArrayList<>();
    }

    public void addMob(MobModel mob, int num){

        mobs.add(num, mob);
    }

    public void deleteMob(int num){

        mobs.remove(num);
    }

    public int getWidth(){

        return width;
    }

    public int getHeight(){

        return height;
    }
}
