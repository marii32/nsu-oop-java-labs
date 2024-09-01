package SecGame.Mob;

public class MobController {

    public MobModel model;
    public MobController(int width, int height, int maxSize){

        this.model = new MobModel(width, height, maxSize);

    }


}
