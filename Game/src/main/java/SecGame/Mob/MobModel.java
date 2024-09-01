package SecGame.Mob;

public class MobModel {

    public int x;
    private int y;
    public int imgNum;
    private int size;
    private static String[] sides = {"left", "right"};
    public int digit;
    public String side;
    public int speed;
    public String key;
    public MobModel(int WinWidth,int WinHeight, int enemy_size){


        this.speed = (int)(Math.random()*20 + 5);
        this.imgNum = 1;
        this.side = sides[(int)(Math.random()*2)];
        this.size = (int)(Math.random()*(enemy_size + 50) + 10);
        if(side == "left"){

            this.digit = 1;
            this.x = -20;
        }
        else{
            this.digit = -1;
            this.x = WinWidth;
        }
        this.key = this.side + (int)(Math.random()*6+1);
        this.y = (int)(Math.random()* (WinHeight+1));
        //this.view = new MobView(this);

    }

    public void updatePosition(boolean live){

        if (live) {

            x+=digit*speed;
        }
        else x = -400;

    }

    public int getX(){

        return x;
    }

    public int getY() {
        return y;
    }

    public int getSize(){
        return size;
    }


}
